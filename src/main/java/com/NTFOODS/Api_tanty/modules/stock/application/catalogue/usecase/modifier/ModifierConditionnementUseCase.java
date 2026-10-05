package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.ModifierConditionnementRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper.ProduitMapper;
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
 * Use Case — Modifier un conditionnement existant d'un produit.
 */
@Service
@Transactional
public class ModifierConditionnementUseCase {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    private final EventPublisher eventPublisher;

    public ModifierConditionnementUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper,
                                           EventPublisher eventPublisher) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
        this.eventPublisher = eventPublisher;
    }

    public ProduitResponse executer(String codeProduit, String conditionnementId,
                                    ModifierConditionnementRequest request, Utilisateur acteur) {
        if (!ProduitSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException(
                "Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas gérer le catalogue produit"
            );
        }

        CodeProduit code = CodeProduit.de(codeProduit);
        Produit produit = produitRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", code.getValeur()));

        produit.modifierConditionnement(
            conditionnementId,
            request.libelle(),
            request.facteurConversionVersBase(),
            request.poidsUnitaireGrammes() != null ? request.poidsUnitaireGrammes() : -1,
            request.volumeUnitaireCm3() != null ? request.volumeUnitaireCm3() : -1,
            request.prixUnitaireHT(),
            request.codeBarres()
        );

        Produit sauvegarde = produitRepository.sauvegarder(produit);
        produit.getDomainEvents().forEach(eventPublisher::publish);
        produit.clearDomainEvents();

        return produitMapper.versResponse(sauvegarde);
    }
}
