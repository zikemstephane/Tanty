package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ConditionnementResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import org.springframework.stereotype.Component;

import java.util.List;

/** Mapper application — Produit (domaine) → DTO de réponse. */
@Component
public class ProduitMapper {

    public ProduitResponse versResponse(Produit produit) {
        List<ConditionnementResponse> conditionnements = produit.getConditionnements().stream()
            .map(this::versConditionnementResponse)
            .toList();

        return new ProduitResponse(
            produit.getId(),
            produit.getCode().getValeur(),
            produit.getMarque(),
            produit.getGamme(),
            produit.getVariete(),
            produit.getDesignation(),
            produit.getCategorie().name(),
            produit.getCategorie().getLibelle(),
            produit.getCategorieMetier() != null ? produit.getCategorieMetier().name() : null,
            produit.getCategorieMetier() != null ? produit.getCategorieMetier().getLibelle() : null,
            produit.isActif(),
            produit.getPrixDistributeur(),
            produit.getPrixGrossiste(),
            produit.getPrixDetaillant(),
            produit.isTva(),
            produit.getConditionnementCommercial(),
            produit.getUniteVenteCommerciale(),
            conditionnements,
            produit.getDateCreation(),
            produit.getDateModification()
        );
    }

    public ConditionnementResponse versConditionnementResponse(Conditionnement c) {
        return new ConditionnementResponse(
            c.getId(),
            c.getSku(),
            c.getNiveau().name(),
            c.getNiveau().getLibelle(),
            c.getLibelle(),
            c.getFacteurConversionVersBase(),
            c.getPoidsUnitaireGrammes(),
            c.getVolumeUnitaireCm3(),
            c.getPrixUnitaireHT(),
            c.getCodeBarres()
        );
    }
}
