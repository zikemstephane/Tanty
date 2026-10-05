package com.NTFOODS.Api_tanty.modules.production.application.formule.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.response.ReferentielProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Use Case — Référentiel de production : tous les produits du catalogue avec
 * leur formule et la composition d'emballage de leurs conditionnements.
 * Lu côté production pour ne pas exiger de permission Stock aux rôles
 * qui gèrent ces données (Chef de Production, Responsable de Salle).
 */
@Service
@Transactional(readOnly = true)
public class ListerReferentielProductionUseCase {

    private final ProduitRepository produitRepository;
    private final FormuleProductionRepository formuleRepository;

    public ListerReferentielProductionUseCase(ProduitRepository produitRepository,
                                              FormuleProductionRepository formuleRepository) {
        this.produitRepository = produitRepository;
        this.formuleRepository = formuleRepository;
    }

    public List<ReferentielProductionResponse> executer() {
        Map<String, FormuleProduction> formules = formuleRepository.listerToutes().stream()
            .collect(Collectors.toMap(FormuleProduction::getCodeProduit, Function.identity(), (a, b) -> a));
        return produitRepository.trouverTous().stream()
            .sorted(Comparator.comparing(p -> p.getCode().getValeur()))
            .map(p -> versResponse(p, formules.get(p.getCode().getValeur())))
            .toList();
    }

    public static ReferentielProductionResponse versResponse(Produit p, FormuleProduction f) {
        var formule = f == null ? null : new ReferentielProductionResponse.FormuleResponse(
            f.getBaseKg(),
            f.getLignes().stream()
                .map(l -> new ReferentielProductionResponse.LigneResponse(l.getMatierePremiere(), l.getProportionKg()))
                .toList());
        var conditionnements = p.getConditionnements().stream().map(ListerReferentielProductionUseCase::versResponse).toList();
        return new ReferentielProductionResponse(p.getCode().getValeur(), p.getDesignation(),
            p.getCategorieMetier().name(), p.isActif(), formule, conditionnements);
    }

    private static ReferentielProductionResponse.ConditionnementResponse versResponse(Conditionnement c) {
        return new ReferentielProductionResponse.ConditionnementResponse(
            c.getId(), c.getSku(), c.getLibelle(), c.getNiveau().name(), c.getPoidsUnitaireGrammes(),
            c.getType() != null ? c.getType().name() : null,
            c.getComposants().stream()
                .map(k -> new ReferentielProductionResponse.ComposantResponse(
                    k.getType().name(), k.getQuantiteComposant(), k.getQuantiteUnitesProduites()))
                .toList());
    }
}
