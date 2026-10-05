package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.pph.service.PlanificationProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Use Case — Générer automatiquement un Plan de Production Hebdomadaire (PPH)
 * à partir d'un bon de commande validé.
 */
@Service
@Transactional
public class GenererPPHDepuisBCUseCase {

    private static final Logger log = LoggerFactory.getLogger(GenererPPHDepuisBCUseCase.class);

    private final PPHRepository pphRepository;
    private final ProduitRepository produitRepository;
    private final PlanificationProductionService planificationService;

    public GenererPPHDepuisBCUseCase(PPHRepository pphRepository,
                                     ProduitRepository produitRepository,
                                     PlanificationProductionService planificationService) {
        this.pphRepository = pphRepository;
        this.produitRepository = produitRepository;
        this.planificationService = planificationService;
    }

    public PlanProductionHebdomadaire executer(BonCommande bc, Utilisateur acteur) {
        log.info("[GENERER-PPH] ▶ Début génération PPH — BC={} lignes={} acteur={}",
            bc.getNumero(), bc.getLignes().size(), acteur.getMatricule().getValeur());

        // Résolution des produits du catalogue (un BC peut contenir plusieurs
        // lignes avec le même code produit, par exemple des SKU différents).
        // On garde le premier produit trouvé pour chaque code.
        Map<String, Produit> produits = bc.getLignes().stream()
            .map(l -> produitRepository.trouverParCode(com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit.de(l.getCodeProduit())))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(Collectors.toMap(
                p -> p.getCode().getValeur(),
                p -> p,
                (existing, replacement) -> existing));

        log.info("[GENERER-PPH] Produits résolus depuis catalogue : {}/{} — codes trouvés : {}",
            produits.size(), bc.getLignes().size(), produits.keySet());

        if (produits.isEmpty()) {
            log.warn("[GENERER-PPH] ✗ Aucun produit du BC trouvé dans le catalogue — codes demandés : {}",
                bc.getLignes().stream().map(l -> l.getCodeProduit()).collect(Collectors.toList()));
        }

        // Calcul du plan
        log.info("[GENERER-PPH] Appel PlanificationProductionService.calculer...");
        PlanificationProductionService.ResultatPlanification plan = planificationService.calculer(bc, produits);
        log.info("[GENERER-PPH] Plan calculé — semaine={} dateDebut={} dateFin={} lignesPPH={} repartitionJours={}",
            plan.semaine(), plan.dateDebut(), plan.dateFin(),
            plan.lignesPPH().size(), plan.repartitionJours().size());

        if (plan.lignesPPH().isEmpty()) {
            log.error("[GENERER-PPH] ✗ Aucune ligne PPH générée — BC={} — produits catalogue trouvés={} — vérifier que les codes produits du BC existent dans le catalogue",
                bc.getNumero(), produits.size());
            throw new RegleMetierException("Aucune ligne de production générable à partir du BC " + bc.getNumero());
        }

        String referencePPH = genererReferencePPH(bc.getNumero(), plan.dateDebut());
        log.info("[GENERER-PPH] Référence PPH générée : {}", referencePPH);

        if (pphRepository.trouverParReference(referencePPH).isPresent()) {
            log.error("[GENERER-PPH] ✗ PPH déjà existant avec la référence {} — doublon évité", referencePPH);
            throw new RegleMetierException("Un PPH existe déjà avec la référence " + referencePPH);
        }

        PlanProductionHebdomadaire pph = PlanProductionHebdomadaire.creerAvecDetails(
            referencePPH,
            bc.getNumero(),
            plan.semaine(),
            plan.dateDebut(),
            plan.dateFin(),
            acteur.getMatricule().getValeur(),
            plan.lignesPPH(),
            plan.lignesDetaillees(),
            plan.repartitionJours(),
            LocalDate.now()
        );

        PlanProductionHebdomadaire sauvegarde = pphRepository.sauvegarder(pph);
        log.info("[GENERER-PPH] ✓ PPH sauvegardé — référence={} id={}", sauvegarde.getReferencePPH(), sauvegarde.getId());
        return sauvegarde;
    }

    private String genererReferencePPH(String referenceBC, LocalDate dateDebut) {
        return "PPH-" + referenceBC + "-" + dateDebut.toString();
    }
}
