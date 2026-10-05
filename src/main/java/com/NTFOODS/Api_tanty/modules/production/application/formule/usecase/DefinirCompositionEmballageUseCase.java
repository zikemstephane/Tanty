package com.NTFOODS.Api_tanty.modules.production.application.formule.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.request.DefinirCompositionEmballageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.response.ReferentielProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.ComposantEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — Définir (ou effacer) le type et la composition d'emballage d'un
 * conditionnement. Ne modifie aucun autre champ du conditionnement (poids,
 * prix...) : ceux-ci restent gérés par le module Stock.
 */
@Service
@Transactional
public class DefinirCompositionEmballageUseCase {

    private final ProduitRepository produitRepository;
    private final FormuleProductionRepository formuleRepository;

    public DefinirCompositionEmballageUseCase(ProduitRepository produitRepository,
                                              FormuleProductionRepository formuleRepository) {
        this.produitRepository = produitRepository;
        this.formuleRepository = formuleRepository;
    }

    public ReferentielProductionResponse executer(String codeProduit, String conditionnementId,
                                                  DefinirCompositionEmballageRequest request) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", codeProduit));
        Conditionnement conditionnement = produit.getConditionnements().stream()
            .filter(c -> c.getId().equals(conditionnementId))
            .findFirst()
            .orElseThrow(() -> EntiteNonTrouveeException.de("Conditionnement", conditionnementId));

        List<DefinirCompositionEmballageRequest.ComposantRequest> saisis =
            request.composants() == null ? List.of() : request.composants();
        if (request.type() == null || request.type().isBlank()) {
            if (!saisis.isEmpty()) {
                throw new RegleMetierException("Des composants ne peuvent pas être définis sans type d'emballage.");
            }
            conditionnement.definirTypeEtComposants(null, List.of());
        } else {
            if (saisis.isEmpty()) {
                throw new RegleMetierException(
                    "Une composition d'emballage doit contenir au moins un composant (ou aucun type pour l'effacer).");
            }
            long distincts = saisis.stream().map(DefinirCompositionEmballageRequest.ComposantRequest::type).distinct().count();
            if (distincts != saisis.size()) {
                throw new RegleMetierException("Un même composant d'emballage ne peut apparaître qu'une fois dans la composition.");
            }
            conditionnement.definirTypeEtComposants(parserType(request.type()),
                saisis.stream()
                    .map(c -> new ComposantEmballage(parserType(c.type()), c.quantiteComposant(), c.quantiteUnitesProduites()))
                    .toList());
        }

        Produit sauvegarde = produitRepository.sauvegarder(produit);
        return ListerReferentielProductionUseCase.versResponse(sauvegarde,
            formuleRepository.trouverParCodeProduit(sauvegarde.getCode().getValeur()).orElse(null));
    }

    private TypeEmballage parserType(String valeur) {
        try {
            return TypeEmballage.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegleMetierException("Type d'emballage inconnu : " + valeur);
        }
    }
}
