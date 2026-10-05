package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request.AccorderPrixExceptionnelRequest;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.PrixExceptionnelResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper.PrixExceptionnelMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.PrixExceptionnel;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.PrixExceptionnelRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Accorder un prix exceptionnel (Pilier 3 : Tarification Verrouillée).
 *
 * Réservé au DG, au Directeur Commercial, à la Chargée RP &amp; Commercial et au
 * Comptable, exclusivement via leur interface web ("à distance"). Le prix
 * homologué de référence est lu directement depuis la grille tarifaire du
 * client concerné : la dérogation est donc toujours mesurable (écart tracé),
 * jamais un prix saisi "à l'aveugle".
 */
@Service
@Transactional
public class AccorderPrixExceptionnelUseCase {

    private final PrixExceptionnelRepository prixExceptionnelRepository;
    private final GrilleTarifaireRepository grilleTarifaireRepository;
    private final ClientRepository clientRepository;
    private final PrixExceptionnelMapper mapper;
    private final EventPublisher eventPublisher;

    public AccorderPrixExceptionnelUseCase(PrixExceptionnelRepository prixExceptionnelRepository,
                                            GrilleTarifaireRepository grilleTarifaireRepository,
                                            ClientRepository clientRepository, PrixExceptionnelMapper mapper,
                                            EventPublisher eventPublisher) {
        this.prixExceptionnelRepository = prixExceptionnelRepository;
        this.grilleTarifaireRepository = grilleTarifaireRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    public PrixExceptionnelResponse executer(AccorderPrixExceptionnelRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.DIRECTEUR_GENERAL && acteur.getRole() != Role.DIRECTEUR_COMMERCIAL
            && acteur.getRole() != Role.CHARGEE_RP && acteur.getRole() != Role.COMPTABLE
            && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException(
                "Seuls le Directeur Général, le Directeur Commercial, la Chargée RP & Commercial et le "
                + "Comptable peuvent débloquer un prix exceptionnel. Rôle '"
                + acteur.getRole().getLibelle() + "' non autorisé.");
        }

        String matricule = acteur.getMatricule().getValeur();
        String reference = prixExceptionnelRepository.genererReference();

        Client client = clientRepository.trouverParCode(request.codeClient())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", request.codeClient()));

        GrilleTarifaire grille = grilleTarifaireRepository.trouver(request.codeProduit(), client.getType())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Grille tarifaire",
                request.codeProduit() + " / " + client.getType()));

        PrixExceptionnel accorde = PrixExceptionnel.accorder(reference, request.codeProduit(), request.codeClient(),
            grille.getPrixHomologueFCFA(), request.prixAppliqueFCFA(), request.motif(), matricule,
            request.referenceVenteCible());

        PrixExceptionnel sauvegarde = prixExceptionnelRepository.sauvegarder(accorde);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();

        return mapper.versResponse(sauvegarde);
    }
}
