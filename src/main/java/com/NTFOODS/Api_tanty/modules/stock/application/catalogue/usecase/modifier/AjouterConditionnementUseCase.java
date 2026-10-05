package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.AjouterConditionnementRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper.ProduitMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.specification.ProduitSpecification;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Ajouter un conditionnement à un produit existant.
 */
@Service
@Transactional
public class AjouterConditionnementUseCase {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    private final EventPublisher eventPublisher;

    public AjouterConditionnementUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper,
                                         EventPublisher eventPublisher) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
        this.eventPublisher = eventPublisher;
    }

    public ProduitResponse executer(String codeProduit, AjouterConditionnementRequest request, Utilisateur acteur) {
        if (!ProduitSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException(
                "Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas gérer le catalogue produit"
            );
        }

        CodeProduit code = CodeProduit.de(codeProduit);
        Produit produit = produitRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", code.getValeur()));

        NiveauConditionnement niveau;
        try {
            niveau = NiveauConditionnement.valueOf(request.niveau().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niveau de conditionnement invalide : '" + request.niveau() + "'");
        }

        produit.ajouterConditionnement(
            request.sku(), niveau, request.libelle(), request.facteurConversionVersBase(),
            request.poidsUnitaireGrammes(), request.volumeUnitaireCm3(),
            request.prixUnitaireHT(), request.codeBarres()
        );

        Produit sauvegarde = produitRepository.sauvegarder(produit);
        produit.getDomainEvents().forEach(eventPublisher::publish);
        produit.clearDomainEvents();

        return produitMapper.versResponse(sauvegarde);
    }
}
