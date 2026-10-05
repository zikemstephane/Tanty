package com.NTFOODS.Api_tanty.modules.commercial.domain.vente;

import com.NTFOODS.Api_tanty.modules.commercial.application.vente.service.ReglesAvarieService;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.DeclencherTransactionNonMarchandeUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.DeclencherTransactionNonMarchandeUseCase.NonMarchandeRequest;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Transactions non marchandes : elles n'encaissent rien mais font varier le stock.
 *
 * <p>Ce que ces tests protègent : une avarie sort réellement du stock mobile (elle
 * ne se contentait pas d'y créer une écriture), un échange rend au camion ce qu'il
 * lui retire, et la preuve photographique est arbitrée par les règles administrables
 * plutôt que par un seuil compilé que le client rendait inatteignable.</p>
 */
class TransactionNonMarchandeTest {

    private static final String COMMERCIAL = "COM-001";
    private static final String MOBILE = "MOBILE-COM-001";

    private DeclencherTransactionNonMarchandeUseCase useCase;
    private ReglesAvarieService reglesAvarieService;
    private NiveauStockRepository niveauStockRepository;

    /** Stock mobile simulé, par code produit. */
    private Map<String, NiveauStock> stockMobile;
    /** Catalogue simulé, par code produit. */
    private Map<String, Produit> catalogue;
    private List<MouvementStock> mouvements;

    @BeforeEach
    void preparer() {
        stockMobile = new HashMap<>();
        stockMobile.put("TBSA", NiveauStock.creer("TBSA", MOBILE, TypeEntrepot.MOBILE));
        stockMobile.get("TBSA").entrer(50);
        stockMobile.put("MGAE", NiveauStock.creer("MGAE", MOBILE, TypeEntrepot.MOBILE));
        stockMobile.get("MGAE").entrer(20);
        mouvements = new ArrayList<>();

        VenteRepository venteRepository = mock(VenteRepository.class);
        when(venteRepository.sauvegarder(any(Vente.class))).thenAnswer(i -> i.getArgument(0));

        reglesAvarieService = mock(ReglesAvarieService.class);
        when(reglesAvarieService.reglesApplicablesA(any())).thenReturn(ReglesAvarie.reglesHistoriques());

        EntrepotRepository entrepotRepository = mock(EntrepotRepository.class);
        when(entrepotRepository.trouverMobileParCommercial(COMMERCIAL))
            .thenReturn(Optional.of(Entrepot.creerMobile(COMMERCIAL)));

        niveauStockRepository = mock(NiveauStockRepository.class);
        when(niveauStockRepository.trouver(anyString(), eq(MOBILE)))
            .thenAnswer(i -> Optional.ofNullable(stockMobile.get(i.<String>getArgument(0))));
        when(niveauStockRepository.sauvegarder(any())).thenAnswer(i -> i.getArgument(0));

        MouvementStockRepository mouvementRepository = mock(MouvementStockRepository.class);
        when(mouvementRepository.enregistrer(any())).thenAnswer(i -> {
            mouvements.add(i.getArgument(0));
            return i.getArgument(0);
        });

        // Catalogue réel plutôt que vide : c'est lui qui porte la conversion du
        // conditionnement déclaré vers l'unité de base. Avec un catalogue vide, la
        // conversion retombait sur la quantité brute et les assertions de stock
        // passaient sans rien prouver.
        catalogue = new HashMap<>();
        catalogue.put("TBSA", produit("TBSA", "Bouillie Soja Arachide", "Sachet", 1L, "Carton de 25", 25L));
        catalogue.put("MGAE", produit("MGAE", "Moulin Gari", "Sachet", 1L, "Carton de 10", 10L));
        catalogue.put("RBSA", produitSansCarton("RBSA", "Reste Bouillie"));

        ProduitRepository produitRepository = mock(ProduitRepository.class);
        when(produitRepository.trouverParCode(any())).thenAnswer(
            i -> Optional.ofNullable(catalogue.get(i.<CodeProduit>getArgument(0).getValeur())));

        AtomicLong compteur = new AtomicLong();
        GenerateurNumeroMetier generateur = mock(GenerateurNumeroMetier.class);
        when(generateur.genererNumero(any(FamilleNumero.class), any()))
            .thenAnswer(i -> i.<FamilleNumero>getArgument(0).getPrefixe() + "-TEST-"
                + compteur.incrementAndGet());

        useCase = new DeclencherTransactionNonMarchandeUseCase(venteRepository, reglesAvarieService,
            entrepotRepository, niveauStockRepository, mouvementRepository, produitRepository, generateur);
    }

    /** Produit à deux niveaux : l'unité de base et son carton. */
    private Produit produit(String code, String designation, String libelleBase, long facteurBase,
                            String libelleCarton, long facteurCarton) {
        Produit p = produitSansCarton(code, designation);
        p.ajouterConditionnement(code + "-CARTON", NiveauConditionnement.CARTON, libelleCarton,
            facteurCarton, 0, 0, BigDecimal.ZERO, null);
        return p;
    }

    /** Produit n'existant qu'à l'unité : sert à éprouver l'échange non chiffrable. */
    private Produit produitSansCarton(String code, String designation) {
        Produit p = Produit.creer(CodeProduit.de(code), "Tanty", "Bouillies", "Nature",
            designation, CategorieProduit.PRODUIT_FINI, CategorieMetier.INGREDIENTS);
        p.ajouterConditionnement(code + "-UNITE_BASE", NiveauConditionnement.UNITE_BASE, "Sachet",
            1L, 0, 0, BigDecimal.ZERO, null);
        return p;
    }

    private NonMarchandeRequest requete(TypeVente type, int quantite, String photo, String produitRepris) {
        return requete(type, quantite, photo, produitRepris, "TBSA-UNITE_BASE");
    }

    private NonMarchandeRequest requete(TypeVente type, int quantite, String photo, String produitRepris,
                                        String sku) {
        return new NonMarchandeRequest(type, COMMERCIAL, type.name(), "TBSA", "Bouillie Soja Arachide",
            sku, quantite, LocalDate.now(), "Choc transport", photo, produitRepris);
    }

    private long stock(String codeProduit) {
        return stockMobile.get(codeProduit).getQuantiteEnBase();
    }

    @Test
    @DisplayName("Une avarie sort réellement du stock mobile")
    void avarie_decremente_le_stock() {
        useCase.executer(requete(TypeVente.AVARIE, 5, "photo.jpg", null));

        assertEquals(45, stock("TBSA"));
    }

    @Test
    @DisplayName("Une avarie n'est recréditée nulle part : elle est comptabilisée à part")
    void avarie_sans_reintegration() {
        useCase.executer(requete(TypeVente.AVARIE, 5, "photo.jpg", null));

        assertEquals(1, mouvements.size());
        MouvementStock mouvement = mouvements.get(0);
        assertEquals(TypeMouvement.SORTIE_AVARIE, mouvement.getType());
        assertEquals(MOBILE, mouvement.getCodeEntrepotSource());
        assertNull(mouvement.getCodeEntrepotCible(), "Un produit avarié ne doit redevenir vendable nulle part");
    }

    @Test
    @DisplayName("Un don sort du stock sans exiger de photo")
    void don_sans_photo() {
        useCase.executer(requete(TypeVente.DON, 8, null, null));

        assertEquals(42, stock("TBSA"));
        assertEquals(TypeMouvement.SORTIE_DON, mouvements.get(0).getType());
    }

    @Test
    @DisplayName("Un échange rend au camion ce qu'il lui retire")
    void echange_compense_le_stock() {
        useCase.executer(requete(TypeVente.ECHANGE, 3, null, "MGAE"));

        assertEquals(47, stock("TBSA"), "Le produit remis au client sort du stock");
        assertEquals(23, stock("MGAE"), "Le produit repris réintègre le stock");
    }

    @Test
    @DisplayName("Un échange trace ses deux sens de mouvement")
    void echange_trace_deux_mouvements() {
        useCase.executer(requete(TypeVente.ECHANGE, 3, null, "MGAE"));

        assertEquals(2, mouvements.size());
        assertEquals(TypeMouvement.ECHANGE_REMISE_CLIENT, mouvements.get(0).getType());
        assertEquals(TypeMouvement.ECHANGE_REPRISE_CLIENT, mouvements.get(1).getType());
        assertEquals(mouvements.get(0).getReferenceDocument(), mouvements.get(1).getReferenceDocument(),
            "Les deux sens appartiennent à la même transaction");
    }

    @Test
    @DisplayName("Un échange sans produit repris est refusé")
    void echange_sans_contrepartie_refuse() {
        assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.ECHANGE, 3, null, null)));
    }

    @Test
    @DisplayName("La photo est exigée au seuil porté par les règles en vigueur")
    void photo_exigee_selon_les_regles() {
        // Repli en vigueur depuis le 18/08/2026 : seuil à zéro, donc toute
        // avarie exige une preuve — une seule unité suffit à la déclencher.
        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.AVARIE, 1, null, null)));

        assertTrue(erreur.getMessage().contains("photo"));
        assertEquals(50, stock("TBSA"), "Un refus ne doit laisser aucune trace sur le stock");
    }

    @Test
    @DisplayName("Avec une preuve, l'avarie passe quelle que soit la quantité")
    void photo_fournie_avarie_acceptee() {
        useCase.executer(requete(TypeVente.AVARIE, 2, "photo-avarie-001.jpg", null));

        assertEquals(48, stock("TBSA"));
    }

    @Test
    @DisplayName("Le seuil suit les règles administrables, pas une valeur compilée")
    void seuil_suit_les_regles_administrables() {
        when(reglesAvarieService.reglesApplicablesA(any())).thenReturn(
            ReglesAvarie.creer("REGLES-AVARIE-20260901", 10, LocalDate.now(), "MAT-GS-001", "Assouplissement"));

        useCase.executer(requete(TypeVente.AVARIE, 9, null, null));

        assertEquals(41, stock("TBSA"));
        assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.AVARIE, 10, null, null)));
    }

    @Test
    @DisplayName("Le seuil de preuve s'apprécie en unités de base, pas en colis")
    void seuil_photo_en_unites_de_base() {
        // Un seul carton déclaré : sous l'ancienne lecture, « 1 » passait très en
        // dessous du seuil de trois et l'avarie partait sans preuve. Les vingt-cinq
        // sachets qu'il contient sont pourtant la perte réelle.
        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.AVARIE, 1, null, null, "TBSA-CARTON")));

        assertTrue(erreur.getMessage().contains("25"),
            "Le commercial doit lire la quantité réellement retenue, pas seulement le seuil");
        assertEquals(50, stock("TBSA"), "Un refus ne doit laisser aucune trace sur le stock");
    }

    @Test
    @DisplayName("Une photo jointe laisse passer une avarie en cartons")
    void avarie_en_cartons_avec_preuve() {
        useCase.executer(requete(TypeVente.AVARIE, 1, "photo.jpg", null, "TBSA-CARTON"));

        assertEquals(25, stock("TBSA"));
    }

    @Test
    @DisplayName("Un stock mobile insuffisant fait échouer la déclaration entière")
    void stock_insuffisant_refuse() {
        assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.AVARIE, 99, "photo.jpg", null)));

        assertEquals(50, stock("TBSA"));
    }

    @Test
    @DisplayName("Un commercial sans stock mobile ne peut rien déclarer")
    void sans_stock_mobile_refuse() {
        NonMarchandeRequest req = new NonMarchandeRequest(TypeVente.DON, "COM-INCONNU", "DON",
            "TBSA", "Bouillie", "TBSA-UNITE_BASE", 1, LocalDate.now(), "Test", null, null);

        assertThrows(EntiteNonTrouveeException.class, () -> useCase.executer(req));
    }

    @Test
    @DisplayName("Une vente marchande est refusée par ce use case")
    void vente_marchande_refusee() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.executer(requete(TypeVente.COMPTANT, 1, null, null)));
        assertThrows(IllegalArgumentException.class,
            () -> useCase.executer(requete(TypeVente.CREDIT, 1, null, null)));
    }

    @Test
    @DisplayName("Le motif reste obligatoire")
    void motif_obligatoire() {
        NonMarchandeRequest req = new NonMarchandeRequest(TypeVente.DON, COMMERCIAL, "DON",
            "TBSA", "Bouillie", "TBSA-UNITE_BASE", 1, LocalDate.now(), "  ", null, null);

        assertThrows(IllegalArgumentException.class, () -> useCase.executer(req));
    }

    @Test
    @DisplayName("Un conditionnement absent est énoncé, pas subi")
    void conditionnement_obligatoire() {
        NonMarchandeRequest req = new NonMarchandeRequest(TypeVente.DON, COMMERCIAL, "DON",
            "TBSA", "Bouillie", null, 1, LocalDate.now(), "Échantillons", null, null);

        IllegalArgumentException erreur =
            assertThrows(IllegalArgumentException.class, () -> useCase.executer(req));

        assertTrue(erreur.getMessage().toLowerCase().contains("conditionnement"));
    }

    @Test
    @DisplayName("Une quantité déclarée en cartons est convertie en unités de base")
    void carton_converti_vers_unite_de_base() {
        useCase.executer(requete(TypeVente.DON, 1, null, null, "TBSA-CARTON"));

        assertEquals(25, stock("TBSA"), "Un carton de 25 doit retirer 25 unités, pas 1");
        assertEquals(25, mouvements.get(0).getQuantiteEnBase(), "Le mouvement est tracé en unités de base");
        assertEquals("TBSA-CARTON", mouvements.get(0).getSku(), "Le SKU déclaré est conservé dans la trace");
    }

    @Test
    @DisplayName("Un conditionnement inconnu est refusé, jamais absorbé en silence")
    void conditionnement_inconnu_refuse() {
        // Le libellé d'affichage à la place du SKU : c'est exactement ce que le
        // mobile envoyait, et le stock partait alors à la quantité brute.
        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.DON, 1, null, null, "Carton")));

        assertTrue(erreur.getMessage().contains("TBSA-CARTON"),
            "Le message doit énoncer les conditionnements admis, obtenu : " + erreur.getMessage());
        assertEquals(50, stock("TBSA"), "Un refus ne doit rien retirer du stock");
    }

    @Test
    @DisplayName("Un échange chiffre la reprise avec le conditionnement du produit repris")
    void echange_utilise_le_conditionnement_du_produit_repris() {
        useCase.executer(requete(TypeVente.ECHANGE, 1, null, "MGAE", "TBSA-CARTON"));

        assertEquals(25, stock("TBSA"), "Un carton TBSA remis vaut 25 unités");
        assertEquals(30, stock("MGAE"), "Un carton MGAE repris vaut 10 unités, pas 25");
    }

    @Test
    @DisplayName("Un échange dont la contrepartie n'existe pas au même niveau est refusé")
    void echange_non_chiffrable_refuse() {
        assertThrows(RegleMetierException.class,
            () -> useCase.executer(requete(TypeVente.ECHANGE, 1, null, "RBSA", "TBSA-CARTON")));

        assertEquals(50, stock("TBSA"), "Aucune sortie ne doit précéder un échange irrecevable");
    }

    @Test
    @DisplayName("La transaction reçoit un numéro tiré de la séquence dédiée")
    void numero_issu_de_la_sequence() {
        Vente vente = useCase.executer(requete(TypeVente.DON, 2, null, null));

        assertTrue(vente.getNumeroVente().startsWith(FamilleNumero.TRANSACTION_NON_MARCHANDE.getPrefixe() + "-"),
            "Attendu un numéro de la famille TRANSACTION_NON_MARCHANDE, obtenu " + vente.getNumeroVente());
    }
}
