package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.cycle;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification.BonCommandeSpecification;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — transitions du cycle de vie d'un BC : envoyer, annuler, clôturer.
 */
@Service
@Transactional
public class TransitionBonCommandeUseCase {

    public enum Action { ENVOYER, ANNULER, CLOTURER, SUPPRIMER }

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper bonCommandeMapper;

    public TransitionBonCommandeUseCase(BonCommandeRepository bonCommandeRepository, BonCommandeMapper bonCommandeMapper) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.bonCommandeMapper = bonCommandeMapper;
    }

    public BonCommandeResponse executer(String numero, Action action, Utilisateur acteur) {
        if (!BonCommandeSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas modifier un BC");
        }
        BonCommande bc = bonCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Bon de commande", numero));
        if (action == Action.SUPPRIMER) {
            if (bc.getStatut() != StatutBonCommande.BROUILLON) {
                throw new RegleMetierException("Seul un BC en BROUILLON peut être supprimé");
            }
            bonCommandeRepository.supprimer(bc);
            return null;
        }
        switch (action) {
            case ENVOYER -> bc.envoyer();
            case ANNULER -> bc.annuler();
            case CLOTURER -> bc.cloturer();
        }
        return bonCommandeMapper.versResponse(bonCommandeRepository.sauvegarder(bc));
    }
}
