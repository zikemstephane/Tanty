package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.pph.service.PlanificationProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
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
        Map<String, Produit> produits = bc.getLignes().stream()
            .map(l -> produitRepository.trouverParCode(com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit.de(l.getCodeProduit())))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(Collectors.toMap(p -> p.getCode().getValeur(), p -> p));

        PlanificationProductionService.ResultatPlanification plan = planificationService.calculer(bc, produits);

        if (plan.lignesPPH().isEmpty()) {
            throw new RegleMetierException("Aucune ligne de production générable à partir du BC " + bc.getNumero());
        }

        String referencePPH = genererReferencePPH(bc.getNumero(), plan.dateDebut());
        if (pphRepository.trouverParReference(referencePPH).isPresent()) {
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

        return pphRepository.sauvegarder(pph);
    }

    private String genererReferencePPH(String referenceBC, LocalDate dateDebut) {
        return "PPH-" + referenceBC + "-" + dateDebut.toString();
    }
}
