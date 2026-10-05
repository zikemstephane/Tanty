package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response.RecalculerPPHResponse;
import com.NTFOODS.Api_tanty.modules.production.application.pph.mapper.PPHMapper;
import com.NTFOODS.Api_tanty.modules.production.application.pph.service.PlanificationProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Use Case — Recalculer un Plan de Production Hebdomadaire déjà généré, à
 * partir de l'état ACTUEL du BC et du catalogue (formules de production,
 * compositions d'emballage) — utile quand l'un de ces éléments a changé
 * depuis la dernière génération/recalcul.
 *
 * Refusé sur un PPH EN_COURS ou CLOTURE (cf. {@link PlanProductionHebdomadaire#recalculer}) :
 * une production déjà réalisée ne doit jamais être écrasée.
 *
 * Écrase sans fusion toute répartition journalière ajustée manuellement par
 * le Responsable de Salle — {@link RecalculerPPHResponse#ajustementsManuelsEcrases()}
 * le signale explicitement dans la réponse, pour qu'il sache qu'il doit
 * refaire ses corrections si besoin.
 */
@Service
@Transactional
public class RecalculerPPHUseCase {

    private static final Logger log = LoggerFactory.getLogger(RecalculerPPHUseCase.class);

    private final PPHRepository pphRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final ProduitRepository produitRepository;
    private final PlanificationProductionService planificationService;
    private final PPHMapper pphMapper;

    public RecalculerPPHUseCase(PPHRepository pphRepository, BonCommandeRepository bonCommandeRepository,
                                ProduitRepository produitRepository,
                                PlanificationProductionService planificationService, PPHMapper pphMapper) {
        this.pphRepository = pphRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.produitRepository = produitRepository;
        this.planificationService = planificationService;
        this.pphMapper = pphMapper;
    }

    public RecalculerPPHResponse executer(String referencePPH) {
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", referencePPH));

        String referenceBC = pph.getReferenceBC();
        if (referenceBC == null || referenceBC.isBlank()) {
            throw new RegleMetierException(
                "Le PPH " + referencePPH + " n'est associé à aucun bon de commande : recalcul impossible.");
        }
        BonCommande bc = bonCommandeRepository.trouverParNumero(referenceBC)
            .orElseThrow(() -> EntiteNonTrouveeException.de("BonCommande", referenceBC));

        Map<String, Produit> produits = bc.getLignes().stream()
            .map(l -> produitRepository.trouverParCode(CodeProduit.de(l.getCodeProduit())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toMap(
                p -> p.getCode().getValeur(),
                p -> p,
                (existing, replacement) -> existing));

        boolean ajustementsPresents = pph.isRepartitionAjusteeManuellement();
        log.info("[RECALCUL-PPH] référence={} referenceBC={} ajustementsManuelsPresents={}",
            referencePPH, referenceBC, ajustementsPresents);

        PlanificationProductionService.ResultatPlanification plan = planificationService.calculer(bc, produits);
        if (plan.lignesPPH().isEmpty()) {
            throw new RegleMetierException(
                "Aucune ligne de production générable lors du recalcul du PPH " + referencePPH
                    + " — vérifier que les produits du BC existent toujours dans le catalogue.");
        }

        pph.recalculer(plan.semaine(), plan.dateDebut(), plan.dateFin(),
            plan.lignesPPH(), plan.lignesDetaillees(), plan.repartitionJours());

        PlanProductionHebdomadaire sauvegarde = pphRepository.sauvegarder(pph);
        log.info("[RECALCUL-PPH] ✓ référence={} recalculé — ajustementsManuelsEcrases={}", referencePPH, ajustementsPresents);
        return new RecalculerPPHResponse(pphMapper.versResponse(sauvegarde), ajustementsPresents);
    }
}
