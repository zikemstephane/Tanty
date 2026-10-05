package com.NTFOODS.Api_tanty.modules.production.application.dosage.service;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Résout, pour une session de dosage (identifiée par le PPH qu'elle
 * référence), l'UNIQUE {@link FormuleProduction} applicable aux produits
 * BOUILLIE_DE_SOJA actifs du bon de commande associé.
 *
 * <p>Remplace toute sélection manuelle d'un « type de mélange » (ex. les
 * anciens couples de proportions maïs/soja/arachide figés 40/9/1 et 40/1/9) :
 * la formule vient toujours du produit réellement commandé, jamais d'un choix
 * fait à la main par l'Agent Doseur. Si les produits du BC portent des
 * formules aux proportions différentes, ou si aucun n'a de formule, la
 * résolution est délibérément considérée comme ambiguë — {@link
 * Optional#empty()} — plutôt que de retomber sur une recette par défaut
 * inventée.</p>
 */
@Service
public class ResolveurFormuleParSessionService {

    private static final Logger log = LoggerFactory.getLogger(ResolveurFormuleParSessionService.class);

    private final PPHRepository pphRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final ProduitRepository produitRepository;
    private final FormuleProductionRepository formuleProductionRepository;

    public ResolveurFormuleParSessionService(PPHRepository pphRepository, BonCommandeRepository bonCommandeRepository,
                                             ProduitRepository produitRepository,
                                             FormuleProductionRepository formuleProductionRepository) {
        this.pphRepository = pphRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.produitRepository = produitRepository;
        this.formuleProductionRepository = formuleProductionRepository;
    }

    public Optional<FormuleProduction> resoudre(String referencePPH) {
        Optional<PlanProductionHebdomadaire> pphOpt = pphRepository.trouverParReference(referencePPH);
        if (pphOpt.isEmpty() || pphOpt.get().getReferenceBC() == null || pphOpt.get().getReferenceBC().isBlank()) {
            return Optional.empty();
        }
        Optional<BonCommande> bcOpt = bonCommandeRepository.trouverParNumero(pphOpt.get().getReferenceBC());
        if (bcOpt.isEmpty()) {
            return Optional.empty();
        }

        List<Produit> produits = bcOpt.get().getLignes().stream()
            .map(l -> produitRepository.trouverParCode(CodeProduit.de(l.getCodeProduit())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(p -> p.getCategorieMetier() == CategorieMetier.TANTY_BOUILLIE_DE_SOJA)
            .filter(Produit::isActif)
            .distinct()
            .toList();

        List<FormuleProduction> formules = produits.stream()
            .map(p -> formuleProductionRepository.trouverParCodeProduit(p.getCode().getValeur()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        if (formules.isEmpty()) {
            return Optional.empty();
        }

        Set<String> signatures = formules.stream().map(this::signatureProportions).collect(Collectors.toSet());
        if (signatures.size() != 1) {
            log.info("[RESOLUTION-FORMULE] PPH {} : {} formule(s) distincte(s) parmi les produits du BC — résolution ambiguë",
                referencePPH, signatures.size());
            return Optional.empty();
        }
        return Optional.of(formules.get(0));
    }

    private String signatureProportions(FormuleProduction formule) {
        return formule.getLignes().stream()
            .map(l -> normaliser(l.getMatierePremiere()) + "="
                + l.getProportionKg().divide(formule.getBaseKg(), 6, RoundingMode.HALF_UP))
            .sorted()
            .collect(Collectors.joining(";"));
    }

    private String normaliser(String nom) {
        String sansAccents = Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sansAccents.trim().toUpperCase(Locale.ROOT);
    }
}
