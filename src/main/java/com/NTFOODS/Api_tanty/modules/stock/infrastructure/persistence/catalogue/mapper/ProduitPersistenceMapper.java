package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ConditionnementJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** Mapper Infrastructure — Produit (domaine) ↔ ProduitJpaEntity. */
@Component
public class ProduitPersistenceMapper {

    public ProduitJpaEntity versEntite(Produit produit) {
        ProduitJpaEntity entite = ProduitJpaEntity.builder()
            .id(produit.getId())
            .code(produit.getCode().getValeur())
            .marque(produit.getMarque())
            .gamme(produit.getGamme())
            .variete(produit.getVariete())
            .designation(produit.getDesignation())
            .categorie(produit.getCategorie().name())
            .categorieMetier(produit.getCategorieMetier().name())
            .actif(produit.isActif())
            .prixDistributeur(produit.getPrixDistributeur())
            .prixGrossiste(produit.getPrixGrossiste())
            .prixDetaillant(produit.getPrixDetaillant())
            .tva(produit.isTva())
            .conditionnementCommercial(produit.getConditionnementCommercial())
            .uniteVenteCommerciale(produit.getUniteVenteCommerciale())
            .dateCreation(produit.getDateCreation())
            .dateModification(produit.getDateModification())
            .version(produit.getVersion())
            .conditionnements(new ArrayList<>())
            .build();

        List<ConditionnementJpaEntity> conds = produit.getConditionnements().stream()
            .map(c -> versConditionnementEntite(c, entite))
            .toList();
        entite.getConditionnements().addAll(conds);
        return entite;
    }

    private ConditionnementJpaEntity versConditionnementEntite(Conditionnement c, ProduitJpaEntity parent) {
        return ConditionnementJpaEntity.builder()
            .id(c.getId())
            .produit(parent)
            .codeProduit(c.getCodeProduit())
            .sku(c.getSku())
            .niveau(c.getNiveau().name())
            .libelle(c.getLibelle())
            .facteurConversionVersBase(c.getFacteurConversionVersBase())
            .nombreUnites(c.getNombreUnites())
            .uniteConditionnement(c.getUniteConditionnement())
            .poidsUnitaireGrammes(c.getPoidsUnitaireGrammes())
            .volumeUnitaireCm3(c.getVolumeUnitaireCm3())
            .prixUnitaireHT(c.getPrixUnitaireHT())
            .codeBarres(c.getCodeBarres())
            .build();
    }

    public Produit versDomaine(ProduitJpaEntity entite) {
        List<Conditionnement> conds = entite.getConditionnements().stream()
            .map(this::versConditionnementDomaine)
            .toList();

        return Produit.reconstituer(
            entite.getId(),
            CodeProduit.de(entite.getCode()),
            entite.getMarque(),
            entite.getGamme(),
            entite.getVariete(),
            entite.getDesignation(),
            CategorieProduit.valueOf(entite.getCategorie()),
            parseCategorieMetier(entite.getCategorieMetier()),
            entite.isActif(),
            conds,
            entite.getDateCreation(),
            entite.getDateModification(),
            entite.getVersion(),
            entite.getPrixDistributeur(),
            entite.getPrixGrossiste(),
            entite.getPrixDetaillant(),
            entite.isTva(),
            entite.getConditionnementCommercial(),
            entite.getUniteVenteCommerciale()
        );
    }

    private CategorieMetier parseCategorieMetier(String valeur) {
        if (valeur == null || valeur.isBlank()) return CategorieMetier.AUTRE;
        try {
            return CategorieMetier.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CategorieMetier.AUTRE;
        }
    }

    private Conditionnement versConditionnementDomaine(ConditionnementJpaEntity e) {
        return Conditionnement.reconstituer(
            e.getId(),
            e.getCodeProduit(),
            e.getSku(),
            NiveauConditionnement.valueOf(e.getNiveau()),
            e.getLibelle(),
            e.getFacteurConversionVersBase(),
            e.getPoidsUnitaireGrammes(),
            e.getVolumeUnitaireCm3(),
            e.getPrixUnitaireHT(),
            e.getCodeBarres()
        );
    }
}
