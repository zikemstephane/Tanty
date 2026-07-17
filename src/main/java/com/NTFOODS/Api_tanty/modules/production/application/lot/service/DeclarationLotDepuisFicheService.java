package com.NTFOODS.Api_tanty.modules.production.application.lot.service;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity.LigneFicheProduit;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service applicatif déclarant automatiquement les lots de produits finis à
 * partir des quantités réelles saisies dans la fiche info-produit journalière.
 */
@Service
public class DeclarationLotDepuisFicheService {

    private static final Logger log = LoggerFactory.getLogger(DeclarationLotDepuisFicheService.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final LotRepository lotRepository;
    private final PPHRepository pphRepository;
    private final ProduitRepository produitRepository;

    public DeclarationLotDepuisFicheService(LotRepository lotRepository,
                                           PPHRepository pphRepository,
                                           ProduitRepository produitRepository) {
        this.lotRepository = lotRepository;
        this.pphRepository = pphRepository;
        this.produitRepository = produitRepository;
    }

    @Transactional
    public void declarerLots(FicheProduitJournaliere fiche, String matriculeAgent) {
        Optional<PlanProductionHebdomadaire> pphOpt = pphRepository.trouverParReference(fiche.getReferencePPH());
        if (pphOpt.isEmpty()) {
            log.warn("[DECLARER-LOT] PPH {} introuvable — aucun lot déclaré", fiche.getReferencePPH());
            return;
        }
        PlanProductionHebdomadaire pph = pphOpt.get();

        for (LigneFicheProduit ligne : fiche.getLignes()) {
            if (ligne.getQuantiteProduite() <= 0) {
                continue;
            }
            if (lotExisteDeja(fiche.getReferencePPH(), fiche.getDate(), ligne.getCodeProduit())) {
                log.debug("[DECLARER-LOT] Lot déjà existant pour {}/{}/{} — ignoré",
                    fiche.getReferencePPH(), fiche.getDate(), ligne.getCodeProduit());
                continue;
            }

            double quantiteKg = convertirEnKg(ligne.getCodeProduit(), ligne.getQuantiteProduite(), pph);
            int nbCartons = calculerNbCartons(ligne.getCodeProduit(), ligne.getQuantiteProduite(), pph);
            LocalDate dlc = calculerDLC(fiche.getDate(), ligne.getCodeProduit());

            String numeroLot = genererNumeroLot(fiche.getDate());
            LotProduitFini lot = LotProduitFini.declarer(
                numeroLot,
                fiche.getReferencePPH(),
                ligne.getCodeProduit(),
                ligne.getNomProduit(),
                quantiteKg,
                nbCartons,
                fiche.getDate(),
                dlc,
                matriculeAgent
            );
            lotRepository.sauvegarder(lot);
            log.info("[DECLARER-LOT] Lot {} déclaré : {} / {} kg / {} cartons (PPH {}, date {})",
                numeroLot, ligne.getCodeProduit(), quantiteKg, nbCartons,
                fiche.getReferencePPH(), fiche.getDate());
        }
    }

    private boolean lotExisteDeja(String referencePPH, LocalDate date, String codeProduit) {
        return lotRepository.listerParPeriode(date, date).stream()
            .anyMatch(l -> l.getReferencePPH().equals(referencePPH)
                && l.getCodeProduit().equalsIgnoreCase(codeProduit)
                && l.getDateProduction().equals(date));
    }

    private double convertirEnKg(String codeProduit, double quantiteProduite, PlanProductionHebdomadaire pph) {
        Optional<LignePlanDetail> detailOpt = pph.getLignesDetaillees().stream()
            .filter(d -> d.getCodeProduit().equals(codeProduit))
            .findFirst();

        if (detailOpt.isPresent()) {
            LignePlanDetail detail = detailOpt.get();
            if (detail.getPoidsUnitaireGrammes() > 0) {
                return (quantiteProduite * detail.getPoidsUnitaireGrammes()) / 1000.0;
            }
            // Produit en kg : la quantité saisie est déjà en kg
            return quantiteProduite;
        }

        Optional<Produit> produitOpt = produitRepository.trouverParCode(CodeProduit.de(codeProduit));
        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            try {
                Conditionnement base = produit.trouverParNiveau(NiveauConditionnement.UNITE_BASE);
                if (base.getPoidsUnitaireGrammes() > 0) {
                    return (quantiteProduite * base.getPoidsUnitaireGrammes()) / 1000.0;
                }
            } catch (IllegalArgumentException ignored) {
                // pas d'unité de base
            }
        }
        return quantiteProduite;
    }

    private int calculerNbCartons(String codeProduit, double quantiteProduite, PlanProductionHebdomadaire pph) {
        Optional<LignePlanDetail> detailOpt = pph.getLignesDetaillees().stream()
            .filter(d -> d.getCodeProduit().equals(codeProduit))
            .findFirst();

        if (detailOpt.isPresent()) {
            LignePlanDetail detail = detailOpt.get();
            if (detail.getNombreUnitesParConditionnement() > 0) {
                return (int) Math.ceil(quantiteProduite / detail.getNombreUnitesParConditionnement());
            }
        }

        Optional<Produit> produitOpt = produitRepository.trouverParCode(CodeProduit.de(codeProduit));
        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            Optional<Conditionnement> cartonOpt = produit.getConditionnements().stream()
                .filter(c -> c.getNiveau() == NiveauConditionnement.CARTON)
                .findFirst();
            if (cartonOpt.isPresent() && cartonOpt.get().getNombreUnites() > 0) {
                return (int) Math.ceil(quantiteProduite / cartonOpt.get().getNombreUnites());
            }
        }
        return 0;
    }

    private LocalDate calculerDLC(LocalDate dateProduction, String codeProduit) {
        int dureeJours = 30;
        Optional<Produit> produitOpt = produitRepository.trouverParCode(CodeProduit.de(codeProduit));
        if (produitOpt.isPresent() && "BOUILLIE".equalsIgnoreCase(produitOpt.get().getCategorieMetier().name())) {
            dureeJours = 18 * 30; // ~18 mois pour la bouillie
        }
        return dateProduction.plusDays(dureeJours);
    }

    private String genererNumeroLot(LocalDate date) {
        long seq = lotRepository.compterParStatutEtDate(
            com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot.DECLARE, date) + 1;
        return "LOT-" + FMT.format(date) + "-" + String.format("%03d", seq);
    }
}
