package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.supprimer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.specification.ProduitSpecification;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Désactiver un produit (soft delete préservant la traçabilité).
 */
@Service
@Transactional
public class DesactiverProduitUseCase {

    private final ProduitRepository produitRepository;

    public DesactiverProduitUseCase(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public void executer(String codeProduit, Utilisateur acteur) {
        if (!ProduitSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException(
                "Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas gérer le catalogue produit"
            );
        }
        CodeProduit code = CodeProduit.de(codeProduit);
        Produit produit = produitRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", code.getValeur()));
        produit.desactiver();
        produitRepository.sauvegarder(produit);
    }
}
