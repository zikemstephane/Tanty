package com.NTFOODS.Api_tanty.modules.production.application.prediction.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.aggregate.PredictionPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.repository.PredictionPPHRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.service.RepartitionPoudre;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.LigneBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Prédiction de production d'un plan PPH à partir du nombre de fûts saisi.
 *
 * Les produits pris en compte sont ceux du bon de commande du plan. La poudre (fûts × 50 kg) est
 * répartie entre eux au prorata du poids demandé (voir RepartitionPoudre). La prédiction est
 * enregistrée : c'est la référence que les autres rôles lisent.
 */
@Service
@Transactional
public class PredictionPPHUseCase {

    /** Poudre contenue dans un fût, en kilogrammes (règle de dosage). */
    public static final double POUDRE_PAR_FUT_KG = 50.0;

    private final PPHRepository pphRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final ProduitRepository produitRepository;
    private final FormuleProductionRepository formuleRepository;
    private final PredictionPPHRepository predictionRepository;

    public PredictionPPHUseCase(PPHRepository pphRepository, BonCommandeRepository bonCommandeRepository,
                                ProduitRepository produitRepository, FormuleProductionRepository formuleRepository,
                                PredictionPPHRepository predictionRepository) {
        this.pphRepository = pphRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.produitRepository = produitRepository;
        this.formuleRepository = formuleRepository;
        this.predictionRepository = predictionRepository;
    }

    public PredictionPPH calculer(String referencePPH, int nbFuts, String matricule) {
        if (nbFuts <= 0) {
            throw new IllegalArgumentException("Le nombre de fûts doit être supérieur à 0");
        }
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PlanProductionHebdomadaire", referencePPH));
        BonCommande bc = bonCommandeDuPlan(pph);

        List<RepartitionPoudre.Exclusion> exclusions = new ArrayList<>();
        List<RepartitionPoudre.Produit> produits = new ArrayList<>();
        for (Map.Entry<String, Long> entree : quantitesParProduit(bc).entrySet()) {
            String code = entree.getKey();
            Optional<Produit> produit = produitRepository.trouverParCode(CodeProduit.de(code));
            if (produit.isEmpty()) {
                exclusions.add(new RepartitionPoudre.Exclusion(code, "produit introuvable dans le catalogue"));
                continue;
            }
            if (!produit.get().isActif()) {
                exclusions.add(new RepartitionPoudre.Exclusion(code, "produit inactif"));
                continue;
            }
            Optional<FormuleProduction> formule = formuleRepository.trouverParCodeProduit(code);
            if (formule.isEmpty()) {
                exclusions.add(new RepartitionPoudre.Exclusion(code, "sans formule de production : aucune poudre ne peut lui être attribuée"));
                continue;
            }
            Conditionnement base = uniteDeBase(produit.get());
            produits.add(new RepartitionPoudre.Produit(code, produit.get().getDesignation(), decrireComposition(formule.get()),
                base == null ? 0 : base.getPoidsUnitaireGrammes(),
                unitesParCarton(produit.get()), entree.getValue()));
        }

        double poudreKg = nbFuts * POUDRE_PAR_FUT_KG;
        RepartitionPoudre.Resultat resultat = RepartitionPoudre.repartir(poudreKg, produits);
        exclusions.addAll(resultat.exclusions());

        PredictionPPH prediction = new PredictionPPH(referencePPH, nbFuts, poudreKg, LocalDateTime.now(), matricule,
            resultat.lignes(), exclusions);
        return predictionRepository.enregistrer(prediction);
    }

    @Transactional(readOnly = true)
    public PredictionPPH lire(String referencePPH) {
        return predictionRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Prédiction", referencePPH));
    }

    @Transactional(readOnly = true)
    public List<PredictionPPH> lister() {
        return predictionRepository.listerToutes();
    }

    private BonCommande bonCommandeDuPlan(PlanProductionHebdomadaire pph) {
        String referenceBC = pph.getReferenceBC();
        if (referenceBC == null || referenceBC.isBlank()) {
            throw new RegleMetierException("Le plan " + pph.getReferencePPH() + " n'est associé à aucun bon de commande");
        }
        return bonCommandeRepository.trouverParNumero(referenceBC)
            .orElseThrow(() -> EntiteNonTrouveeException.de("BonCommande", referenceBC));
    }

    /** Quantité commandée par produit, en cumulant les lignes d'un même produit. */
    private Map<String, Long> quantitesParProduit(BonCommande bc) {
        Map<String, Long> cumul = new LinkedHashMap<>();
        for (LigneBonCommande l : bc.getLignes()) {
            cumul.merge(l.getCodeProduit(), l.getQuantiteCommandee(), Long::sum);
        }
        return cumul;
    }

    /** Conditionnement de base : l'unité de base, à défaut l'intermédiaire. */
    private Conditionnement uniteDeBase(Produit p) {
        return p.getConditionnements().stream()
            .filter(c -> c.getNiveau() == NiveauConditionnement.UNITE_BASE)
            .findFirst()
            .orElseGet(() -> p.getConditionnements().stream()
                .filter(c -> c.getNiveau() == NiveauConditionnement.INTERMEDIAIRE)
                .findFirst()
                .orElse(null));
    }

    private int unitesParCarton(Produit p) {
        return p.getConditionnements().stream()
            .filter(c -> c.getNiveau() == NiveauConditionnement.CARTON)
            .findFirst()
            .map(c -> Math.max(1, c.getNombreUnites()))
            .orElse(1);
    }

    private String decrireComposition(FormuleProduction formule) {
        return formule.getLignes().stream()
            .map(l -> l.getMatierePremiere() + " " + l.getProportionKg().stripTrailingZeros().toPlainString())
            .collect(Collectors.joining(" / "))
            + " (base " + formule.getBaseKg().stripTrailingZeros().toPlainString() + " kg)";
    }
}
