package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.PredictionProduitFiniResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.RecetteBouillie;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Use Case — Prédire la quantité de produit fini à partir du nombre de fûts nets dosés.
 *
 * Chaque fût net = 50 kg de poudre. La répartition de la poudre dépend de la recette
 * du produit (Bouillie standard ou Mélange d'arachide). Pour chaque produit BOUILLIE,
 * on calcule le nombre d'unités de base et de cartons que l'on peut produire.
 */
@Service
@Transactional(readOnly = true)
public class PredireProduitFiniDepuisFutsUseCase {

    private static final Logger log = LoggerFactory.getLogger(PredireProduitFiniDepuisFutsUseCase.class);

    private static final double POUDRE_FUT_KG = 50.0;

    private final ProduitRepository produitRepository;
    private final SessionDosageRepository sessionDosageRepository;
    private final PPHRepository pphRepository;
    private final BonCommandeRepository bonCommandeRepository;

    public PredireProduitFiniDepuisFutsUseCase(ProduitRepository produitRepository,
                                               SessionDosageRepository sessionDosageRepository,
                                               PPHRepository pphRepository,
                                               BonCommandeRepository bonCommandeRepository) {
        this.produitRepository = produitRepository;
        this.sessionDosageRepository = sessionDosageRepository;
        this.pphRepository = pphRepository;
        this.bonCommandeRepository = bonCommandeRepository;
    }

    public PredictionProduitFiniResponse executer(Long sessionId) {
        SessionDosage session = sessionDosageRepository.trouverParId(sessionId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", String.valueOf(sessionId)));

        int nbFutsNets = session.getNbFutsNets();
        log.info("[PREDICTION] sessionId={} referencePPH={} nbFutsNets={}", sessionId, session.getReferencePPH(), nbFutsNets);
        if (nbFutsNets <= 0) {
            throw new IllegalArgumentException("La session " + sessionId + " n'a aucun fût net enregistré");
        }

        double poidsTotalPoudreKg = nbFutsNets * POUDRE_FUT_KG;
        List<Produit> produits = produitsDuBonDeCommande(session.getReferencePPH());

        List<PredictionProduitFiniResponse.LignePrediction> predictions = new ArrayList<>();
        for (Produit p : produits) {
            Optional<PredictionProduitFiniResponse.LignePrediction> ligne = predirePourProduit(p, poidsTotalPoudreKg);
            if (ligne.isEmpty()) {
                log.warn("[PREDICTION] Produit {} exclu : pas de prédiction possible", p.getCode().getValeur());
            }
            ligne.ifPresent(predictions::add);
        }

        log.info("[PREDICTION] resultat : {} predictions", predictions.size());
        return new PredictionProduitFiniResponse(nbFutsNets, poidsTotalPoudreKg, predictions);
    }

    private List<Produit> produitsDuBonDeCommande(String referencePPH) {
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PlanProductionHebdomadaire", referencePPH));

        String referenceBC = pph.getReferenceBC();
        log.info("[PREDICTION] PPH={} referenceBC={}", referencePPH, referenceBC);
        if (referenceBC == null || referenceBC.isBlank()) {
            throw new IllegalArgumentException("Le PPH " + referencePPH + " n'est associé à aucun bon de commande");
        }

        BonCommande bc = bonCommandeRepository.trouverParNumero(referenceBC)
            .orElseThrow(() -> EntiteNonTrouveeException.de("BonCommande", referenceBC));

        List<String> codesBC = bc.getLignes().stream().map(l -> l.getCodeProduit()).toList();
        log.info("[PREDICTION] BC={} nbLignes={} codes={}", referenceBC, codesBC.size(), codesBC);

        List<Produit> produits = bc.getLignes().stream()
            .map(l -> produitRepository.trouverParCode(CodeProduit.de(l.getCodeProduit())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(p -> p.getCategorieMetier() == CategorieMetier.TANTY_BOUILLIE_DE_SOJA)
            .filter(Produit::isActif)
            .distinct()
            .toList();

        log.info("[PREDICTION] Produits BOUILLIE_DE_SOJA actifs retenus : {}",
            produits.stream().map(p -> p.getCode().getValeur()).toList());
        return produits;
    }

    private Optional<PredictionProduitFiniResponse.LignePrediction> predirePourProduit(Produit produit, double poidsTotalPoudreKg) {
        if (!produit.isActif()) return Optional.empty();

        RecetteBouillie recette = RecetteBouillie.depuisDesignation(produit.getDesignation());
        // Le poids du produit fini correspond au poids total de poudre dosée (conservation de la masse).
        double poidsProduitKg = poidsTotalPoudreKg;

        // Unité de base effective : UNITE_BASE si présent, sinon INTERMEDIAIRE (sachet, étui...)
        Conditionnement uniteBase = produit.getConditionnements().stream()
            .filter(c -> c.getNiveau() == NiveauConditionnement.UNITE_BASE)
            .findFirst()
            .orElseGet(() -> produit.getConditionnements().stream()
                .filter(c -> c.getNiveau() == NiveauConditionnement.INTERMEDIAIRE)
                .findFirst()
                .orElse(null));
        if (uniteBase == null) {
            log.warn("[PREDICTION] Produit {} exclu : aucun conditionnement UNITE_BASE ou INTERMEDIAIRE", produit.getCode().getValeur());
            return Optional.empty();
        }

        double poidsUnitaireGrammes = uniteBase.getPoidsUnitaireGrammes();
        if (poidsUnitaireGrammes <= 0) {
            log.warn("[PREDICTION] Produit {} exclu : poids unitaire nul ou négatif ({} g)", produit.getCode().getValeur(), poidsUnitaireGrammes);
            return Optional.empty();
        }

        int quantiteUnitesBase = (int) Math.floor((poidsProduitKg * 1000.0) / poidsUnitaireGrammes);

        // Carton
        Conditionnement carton = produit.getConditionnements().stream()
            .filter(c -> c.getNiveau() == NiveauConditionnement.CARTON)
            .findFirst()
            .orElse(null);

        int quantiteCartons = 0;
        int nombreUnitesParCarton = 1;
        if (carton != null) {
            nombreUnitesParCarton = Math.max(1, carton.getNombreUnites());
            quantiteCartons = quantiteUnitesBase / nombreUnitesParCarton;
        }

        log.info("[PREDICTION] Produit {} predit : uniteBase={} ({} g), quantiteUnites={}, cartons={}",
            produit.getCode().getValeur(), uniteBase.getNiveau(), poidsUnitaireGrammes, quantiteUnitesBase, quantiteCartons);

        return Optional.of(new PredictionProduitFiniResponse.LignePrediction(
            produit.getCode().getValeur(),
            produit.getDesignation(),
            recette.getLibelle(),
            quantiteUnitesBase,
            quantiteCartons,
            poidsUnitaireGrammes,
            nombreUnitesParCarton
        ));
    }
}
