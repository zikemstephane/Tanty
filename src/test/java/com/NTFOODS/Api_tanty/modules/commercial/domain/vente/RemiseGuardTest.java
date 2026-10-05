package com.NTFOODS.Api_tanty.modules.commercial.domain.vente;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service.RemiseGuardService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.Promotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypePromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository.PromotionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Résolution de la remise à partir des promotions en vigueur.
 *
 * <p>L'invariant protégé : la remise n'est jamais une saisie du commercial, mais
 * une conséquence des promotions. Et lorsque plusieurs se chevauchent, c'est la
 * plus avantageuse <em>pour ce montant</em> qui l'emporte — comparer les
 * paramètres bruts revenait à opposer un pourcentage à des FCFA.</p>
 *
 * <p>Depuis le 18/08/2026 le service rend un <strong>pourcentage</strong>, aligné
 * sur {@code LigneVente}. Les promotions, elles, continuent de produire un
 * montant : c'est le service qui le rapporte au brut, en un seul endroit.</p>
 */
class RemiseGuardTest {

    private static final String CLIENT = "CLI-001";
    private static final String PRODUIT = "TBSA";
    private static final double DELTA = 0.0001;

    private RemiseGuardService service;
    private PromotionRepository promotionRepository;

    @BeforeEach
    void preparer() {
        ClientRepository clientRepository = mock(ClientRepository.class);
        when(clientRepository.trouverParCode(CLIENT)).thenReturn(Optional.of(
            Client.creer(CLIENT, "Boutique Essos", "690000000", "Essos", "Yaoundé",
                TypeClient.DETAIL, "COM-001", null, null)));

        promotionRepository = mock(PromotionRepository.class);
        when(promotionRepository.listerParProduit(any())).thenReturn(List.of());

        service = new RemiseGuardService(clientRepository, promotionRepository);
    }

    private Promotion promotion(String code, TypePromotion type, double valeur, TypeClient segment) {
        Promotion p = Promotion.creer(code, code, null, type, valeur, PRODUIT, segment,
            LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, "MAT-RP-001");
        p.activer();
        return p;
    }

    @Test
    @DisplayName("Sans promotion en vigueur, seule une remise nulle est acceptée")
    void aucune_promotion_aucune_remise() {
        assertEquals(0.0, service.resoudreRemise(CLIENT, PRODUIT, 20_000, 0));

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> service.resoudreRemise(CLIENT, PRODUIT, 20_000, 7.5));
        assertTrue(erreur.getMessage().contains("Aucune promotion"));
    }

    @Test
    @DisplayName("Une promotion en pourcentage se retrouve telle quelle sur la ligne")
    void promotion_pourcentage() {
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(
            List.of(promotion("PROMO-10", TypePromotion.POURCENTAGE, 10, TypeClient.DETAIL)));

        // 10 % de 20 000 = 2 000 FCFA, soit 10 % du brut : l'aller-retour est neutre.
        assertEquals(10.0, service.resoudreRemise(CLIENT, PRODUIT, 20_000, 10), DELTA);
    }

    @Test
    @DisplayName("Une remise divergente est refusée, jamais corrigée en silence")
    void remise_divergente_refusee() {
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(
            List.of(promotion("PROMO-10", TypePromotion.POURCENTAGE, 10, TypeClient.DETAIL)));

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> service.resoudreRemise(CLIENT, PRODUIT, 20_000, 25));
        assertTrue(erreur.getMessage().contains("PROMO-10"));
    }

    @Test
    @DisplayName("Entre deux promotions, la plus avantageuse pour ce montant l'emporte")
    void plus_avantageuse_pour_ce_montant() {
        // 10 % de 20 000 = 2 000 FCFA, contre une remise fixe de 500 FCFA (2,5 %).
        // La comparaison des paramètres bruts aurait retenu 500 (500 > 10).
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(List.of(
            promotion("PROMO-PCT", TypePromotion.POURCENTAGE, 10, TypeClient.DETAIL),
            promotion("PROMO-FIXE", TypePromotion.MONTANT_FIXE, 500, TypeClient.DETAIL)));

        assertEquals(10.0, service.resoudreRemise(CLIENT, PRODUIT, 20_000, 10), DELTA);
    }

    @Test
    @DisplayName("Sur un petit montant, la remise fixe redevient la plus avantageuse")
    void arbitrage_depend_du_montant() {
        // 10 % de 3 000 = 300 FCFA, contre 500 FCFA fixes — soit 16,67 % du brut.
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(List.of(
            promotion("PROMO-PCT", TypePromotion.POURCENTAGE, 10, TypeClient.DETAIL),
            promotion("PROMO-FIXE", TypePromotion.MONTANT_FIXE, 500, TypeClient.DETAIL)));

        double attendu = 500.0 / 3_000.0 * 100.0;
        assertEquals(attendu, service.resoudreRemise(CLIENT, PRODUIT, 3_000, attendu), DELTA);
    }

    @Test
    @DisplayName("Une promotion visant un autre segment ne s'applique pas")
    void segment_non_concerne_ignore() {
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(
            List.of(promotion("PROMO-GROS", TypePromotion.POURCENTAGE, 15, TypeClient.GROSSISTE)));

        assertEquals(0.0, service.resoudreRemise(CLIENT, PRODUIT, 20_000, 0));
    }

    @Test
    @DisplayName("Une promotion sans segment cible couvre tous les segments")
    void promotion_sans_segment_couvre_tout() {
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(
            List.of(promotion("PROMO-TOUS", TypePromotion.POURCENTAGE, 5, null)));

        assertEquals(5.0, service.resoudreRemise(CLIENT, PRODUIT, 20_000, 5), DELTA);
    }

    @Test
    @DisplayName("Une promotion non activée reste sans effet")
    void promotion_en_brouillon_sans_effet() {
        Promotion brouillon = Promotion.creer("PROMO-BROUILLON", "Brouillon", null,
            TypePromotion.POURCENTAGE, 20, PRODUIT, TypeClient.DETAIL,
            LocalDate.now().minusDays(1), null, 0, "MAT-RP-001");
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(List.of(brouillon));

        assertEquals(0.0, service.resoudreRemise(CLIENT, PRODUIT, 20_000, 0));
    }

    @Test
    @DisplayName("Une promotion démesurée est plafonnée à 100 % de la ligne")
    void remise_plafonnee_au_montant() {
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(
            List.of(promotion("PROMO-ENORME", TypePromotion.MONTANT_FIXE, 50_000, TypeClient.DETAIL)));

        assertEquals(100.0, service.resoudreRemise(CLIENT, PRODUIT, 3_000, 100), DELTA);
    }

    @Test
    @DisplayName("Un brut nul n'ouvre droit à aucune remise, sans division par zéro")
    void brut_nul_sans_division_par_zero() {
        when(promotionRepository.listerParProduit(PRODUIT)).thenReturn(
            List.of(promotion("PROMO-FIXE", TypePromotion.MONTANT_FIXE, 500, TypeClient.DETAIL)));

        double remise = service.resoudreRemise(CLIENT, PRODUIT, 0, 0);

        assertEquals(0.0, remise);
        assertFalse(Double.isNaN(remise), "un NaN traverserait tous les états sans jamais lever d'erreur");
    }
}
