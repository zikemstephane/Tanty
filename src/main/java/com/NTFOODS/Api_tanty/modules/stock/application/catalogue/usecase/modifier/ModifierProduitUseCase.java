package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.ModifierProduitRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper.ProduitMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.specification.ProduitSpecification;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Modifier les informations descriptives d'un produit.
 */
@Service
@Transactional
public class ModifierProduitUseCase {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public ModifierProduitUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }

    public ProduitResponse executer(String codeProduit, ModifierProduitRequest request, Utilisateur acteur) {
        if (!ProduitSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException(
                "Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas gérer le catalogue produit"
            );
        }

        CodeProduit code = CodeProduit.de(codeProduit);
        Produit produit = produitRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", code.getValeur()));

        produit.modifierInfos(request.marque(), request.gamme(), request.variete(), request.designation());
        if (request.categorieMetier() != null && !request.categorieMetier().isBlank()) {
            produit.definirCategorieMetier(CategorieMetier.depuisLibelle(request.categorieMetier()));
        }
        if (request.prixDistributeur() != null || request.prixGrossiste() != null
                || request.prixDetaillant() != null) {
            produit.definirPrixCommerciaux(
                request.prixDistributeur(), request.prixGrossiste(), request.prixDetaillant(),
                Boolean.TRUE.equals(request.tva()),
                request.conditionnementCommercial(), request.uniteVenteCommerciale()
            );
        }

        Produit sauvegarde = produitRepository.sauvegarder(produit);
        return produitMapper.versResponse(sauvegarde);
    }
}
