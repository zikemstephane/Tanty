package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification.BonCommandeSpecification;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — corriger la période de production d'un BC après sa création
 * (même déjà VALIDE). Ne recalcule PAS le PPH associé : c'est un acte
 * séparé et explicite (RecalculerPPHUseCase), pour ne jamais faire dériver
 * un plan de production comme effet de bord silencieux d'une simple
 * correction de date.
 */
@Service
@Transactional
public class ModifierPeriodeProductionBCUseCase {

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper bonCommandeMapper;

    public ModifierPeriodeProductionBCUseCase(BonCommandeRepository bonCommandeRepository,
                                              BonCommandeMapper bonCommandeMapper) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.bonCommandeMapper = bonCommandeMapper;
    }

    public BonCommandeResponse executer(String numero, LocalDate dateDebutProduction, LocalDate dateFinProduction,
                                        Utilisateur acteur) {
        if (!BonCommandeSpecification.peutModifierApresValidation(acteur.getRole())) {
            throw new AccesRefuseException(
                "Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas modifier la période de production d'un BC");
        }
        BonCommande bc = bonCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Bon de commande", numero));
        bc.modifierPeriodeProduction(dateDebutProduction, dateFinProduction);
        return bonCommandeMapper.versResponse(bonCommandeRepository.sauvegarder(bc));
    }
}
