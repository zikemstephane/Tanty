package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.valider;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.pph.usecase.GenererPPHDepuisBCUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification.BonCommandeSpecification;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — valider un Bon de Commande (BROUILLON → VALIDE).
 *
 * La validation déclenche aussitôt la génération du Plan de Production
 * Hebdomadaire correspondant (GenererPPHDepuisBCUseCase, qui calcule lignes,
 * matières premières et répartition journalière depuis les lignes du BC).
 * Cette génération existait déjà mais n'était appelée nulle part : le BC se
 * validait sans jamais produire de plan, laissant le Responsable de Salle
 * sans rien à ajuster. Elle est volontairement dans la même transaction que
 * la validation : si le plan ne peut pas être généré (ex. codes produits du
 * BC absents du catalogue), la validation échoue elle aussi plutôt que de
 * laisser un BC validé sans plan associé.
 */
@Service
@Transactional
public class ValiderBonCommandeUseCase {

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper bonCommandeMapper;
    private final EventPublisher eventPublisher;
    private final GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase;

    public ValiderBonCommandeUseCase(BonCommandeRepository bonCommandeRepository,
                                     BonCommandeMapper bonCommandeMapper, EventPublisher eventPublisher,
                                     GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.bonCommandeMapper = bonCommandeMapper;
        this.eventPublisher = eventPublisher;
        this.genererPPHDepuisBCUseCase = genererPPHDepuisBCUseCase;
    }

    public BonCommandeResponse executer(String numero, Utilisateur acteur) {
        if (!BonCommandeSpecification.peutValider(acteur.getRole())) {
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle()
                + "' ne peut pas valider un BC. Seul le Chef de Production valide les BC.");
        }
        BonCommande bc = bonCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Bon de commande", numero));
        bc.valider(acteur.getMatricule().getValeur());
        BonCommande sauvegarde = bonCommandeRepository.sauvegarder(bc);
        bc.getDomainEvents().forEach(eventPublisher::publish);
        bc.clearDomainEvents();
        genererPPHDepuisBCUseCase.executer(sauvegarde, acteur);
        return bonCommandeMapper.versResponse(sauvegarde);
    }
}
