package com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Use Case — Déclaration de Transaction Non Marchande (§12).
 *
 * Dons, Dotations, Avaries : impactent le stock fictif SANS générer de cash.
 *
 * Règles :
 * - DON : motif obligatoire, validation Comptable si quantité > seuil défini
 * - DOTATION : justification obligatoire, apparaît dans Mtant DOT/PROMO de la Fiche Synthèse DG
 * - AVARIE : photo obligatoire si quantité > 5% du lot chargé ce jour (RG-19)
 *
 * Formule de réconciliation :
 * StockInitial − Ventes − Dons − Dotations − Avaries = Retour Physique
 */
@Service
@Transactional
public class DeclencherTransactionNonMarchandeUseCase {

    private final VenteRepository venteRepository;

    public DeclencherTransactionNonMarchandeUseCase(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }

    public Vente executer(NonMarchandeRequest req) {
        Objects.requireNonNull(req.typeVente(), "Type de transaction obligatoire");
        if (req.typeVente() == TypeVente.COMPTANT || req.typeVente() == TypeVente.CREDIT) {
            throw new IllegalArgumentException("Ce use case ne traite que les transactions non marchandes (DON, AVARIE, DOTATION)");
        }
        if (req.motif() == null || req.motif().isBlank()) {
            throw new IllegalArgumentException("Motif obligatoire pour une transaction non marchande");
        }

        if (req.typeVente() == TypeVente.AVARIE) {
            if (req.pourcentageLot() > 5.0 && (req.photoJustificative() == null || req.photoJustificative().isBlank())) {
                throw new IllegalStateException("Photo obligatoire pour avarie > 5% du lot (RG-19)");
            }
        }

        LigneVente ligne = new LigneVente(req.codeProduit(), req.designationProduit(),
            req.conditionnement(), req.quantite(), 0.0, 0.0);

        String num = "NM-" + req.date().toString().replace("-", "") + "-"
            + req.typeVente().name().charAt(0) + "-" + System.currentTimeMillis() % 10000;

        Vente vente = Vente.creer(num, req.matriculeCommercial(), req.codeClient(),
            req.date(), ModeReglement.NON_APPLICABLE, req.typeVente(), List.of(ligne));
        vente.getObservations();

        Vente sauvegarde = venteRepository.sauvegarder(vente);
        return sauvegarde;
    }

    public record NonMarchandeRequest(
        TypeVente typeVente,
        String matriculeCommercial,
        String codeClient,
        String codeProduit,
        String designationProduit,
        String conditionnement,
        int quantite,
        LocalDate date,
        String motif,
        double pourcentageLot,
        String photoJustificative
    ) {}
}
