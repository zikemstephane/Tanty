package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.JustifierNonValidationBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.SessionBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.mapper.SessionBroyageMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Justifier une journée de broyage en dessous du seuil de validation (85 %).
 */
@Service
@Transactional
public class JustifierNonValidationBroyageUseCase {

    private final SessionBroyageRepository repository;
    private final SessionBroyageMapper mapper;

    public JustifierNonValidationBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SessionBroyageResponse executer(JustifierNonValidationBroyageRequest req, Utilisateur acteur) {
        if (!peutGererBroyage(acteur)) {
            throw new AccesRefuseException("Seul le Chef Machiniste peut justifier une non-validation");
        }
        SessionBroyage session = repository.trouverParId(req.sessionId())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Session broyage", String.valueOf(req.sessionId())));

        session.justifierNonValidation(req.justificatif());
        return mapper.versResponse(repository.sauvegarder(session));
    }

    private boolean peutGererBroyage(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("CHEF_MACHINISTE") || role.equals("ADMIN");
    }
}
