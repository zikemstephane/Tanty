package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.creer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.CreerProduitRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper.ProduitMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.specification.ProduitSpecification;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Créer un produit avec ses conditionnements initiaux.
 * Accessible : Gestionnaire de Stock, DG, Admin.
 */
@Service
@Transactional
public class CreerProduitUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreerProduitUseCase.class);

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    private final EventPublisher eventPublisher;

    public CreerProduitUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper,
                               EventPublisher eventPublisher) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
        this.eventPublisher = eventPublisher;
    }

    public ProduitResponse executer(CreerProduitRequest request, Utilisateur acteur) {
        if (!ProduitSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException(
                "Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas gérer le catalogue produit"
            );
        }

        CodeProduit code = CodeProduit.de(request.code());
        if (produitRepository.existeParCode(code)) {
            throw new RegleMetierException("Un produit existe déjà avec le code : " + code.getValeur());
        }

        CategorieProduit categorie = parserCategorie(request.categorie());
        CategorieMetier categorieMetier = parserCategorieMetier(request.categorieMetier());
        Produit produit = Produit.creer(
            code, request.marque(), request.gamme(), request.variete(),
            request.designation(), categorie, categorieMetier
        );

        if (request.prixDistributeur() != null || request.prixGrossiste() != null
                || request.prixDetaillant() != null) {
            produit.definirPrixCommerciaux(
                request.prixDistributeur(), request.prixGrossiste(), request.prixDetaillant(),
                Boolean.TRUE.equals(request.tva()),
                request.conditionnementCommercial(), request.uniteVenteCommerciale()
            );
        }

        if (request.conditionnements() != null) {
            for (CreerProduitRequest.ConditionnementRequest c : request.conditionnements()) {
                produit.ajouterConditionnement(
                    c.sku(), parserNiveau(c.niveau()), c.libelle(),
                    c.facteurConversionVersBase(), c.poidsUnitaireGrammes(),
                    c.volumeUnitaireCm3(), c.prixUnitaireHT(), c.codeBarres()
                );
            }
        }

        Produit sauvegarde = produitRepository.sauvegarder(produit);
        produit.getDomainEvents().forEach(eventPublisher::publish);
        produit.clearDomainEvents();

        log.info("Produit créé : {} par {}", code.getValeur(), acteur.getMatricule().getValeur());
        return produitMapper.versResponse(sauvegarde);
    }

    private CategorieProduit parserCategorie(String valeur) {
        try {
            return CategorieProduit.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Catégorie de produit invalide : '" + valeur + "'");
        }
    }

    private CategorieMetier parserCategorieMetier(String valeur) {
        if (valeur == null || valeur.isBlank()) return CategorieMetier.AUTRE;
        try {
            return CategorieMetier.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CategorieMetier.depuisLibelle(valeur);
        }
    }

    private NiveauConditionnement parserNiveau(String valeur) {
        try {
            return NiveauConditionnement.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niveau de conditionnement invalide : '" + valeur + "'");
        }
    }
}
