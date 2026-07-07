package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.AutoriserDepassementBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.SessionBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.mapper.SessionBroyageMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Autoriser un dépassement de quantité sur une session broyage.
 * Le Chef Machiniste peut augmenter la quantité autorisée en fonction des capacités de production.
 */
@Service
@Transactional
public class AutoriserDepassementBroyageUseCase {

    private final SessionBroyageRepository repository;
    private final SessionBroyageMapper mapper;

    public AutoriserDepassementBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SessionBroyageResponse executer(AutoriserDepassementBroyageRequest req, Utilisateur acteur) {
        if (!peutGererBroyage(acteur)) {
            throw new AccesRefuseException("Seul le Chef Machiniste peut autoriser un dépassement");
        }
        SessionBroyage session = repository.trouverParId(req.sessionId())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Session broyage", String.valueOf(req.sessionId())));

        session.autoriserDepassement(req.quantiteSupplementaireKg());
        return mapper.versResponse(repository.sauvegarder(session));
    }

    private boolean peutGererBroyage(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("CHEF_MACHINISTE") || role.equals("ADMIN");
    }
}
