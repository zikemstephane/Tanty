package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.valider;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.notification.dto.NotificationMessage;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.HistoriqueNotification;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository.HistoriqueNotificationRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.websocket.NotificationWebSocketService;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — 1ʳᵉ validation d'une réception (vérification physique, Gestionnaire). */
@Service
@Transactional
public class ValiderReceptionEtape1UseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionMapper receptionMapper;
    private final NotificationWebSocketService notificationWsService;
    private final HistoriqueNotificationRepository notificationRepository;

    public ValiderReceptionEtape1UseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper,
                                         NotificationWebSocketService notificationWsService,
                                         HistoriqueNotificationRepository notificationRepository) {
        this.receptionRepository = receptionRepository;
        this.receptionMapper = receptionMapper;
        this.notificationWsService = notificationWsService;
        this.notificationRepository = notificationRepository;
    }

    public ReceptionResponse executer(String numero, Utilisateur acteur, String notes) {
        Reception reception = receptionRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Réception", numero));
        reception.validerEtape1(acteur.getMatricule().getValeur(), acteur.getRole(), notes);
        ReceptionResponse response = receptionMapper.versResponse(receptionRepository.sauvegarder(reception));

        String secondRole = reception.getCategorie() == CategorieProduit.MATIERE_PREMIERE
            ? "COMPTABLE" : "CONTROLEUR_GENERAL";
        NotificationMessage msg = NotificationMessage.de(
            "RECEPTION_VALIDEE_1",
            "Réception en attente de 2ᵉ validation",
            "La réception " + numero + " a été validée en 1ʳᵉ étape. Validation " + secondRole + " requise.",
            secondRole,
            "Valider la réception",
            numero);
        notificationWsService.diffuser(msg);
        notificationRepository.sauvegarder(new HistoriqueNotification(
            "RECEPTION_VALIDEE_1", msg.titre(), msg.message(),
            secondRole, null, "WS", "HAUTE", numero));

        return response;
    }
}
