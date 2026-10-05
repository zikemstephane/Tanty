package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.EnregistrerRealisationBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.SessionBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.mapper.SessionBroyageMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.RealisationMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnregistrerRealisationBroyageUseCase {

    private final SessionBroyageRepository repository;
    private final SessionBroyageMapper mapper;

    public EnregistrerRealisationBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SessionBroyageResponse executer(EnregistrerRealisationBroyageRequest req, Utilisateur acteur) {
        if (!peutGererBroyage(acteur)) {
            throw new AccesRefuseException("Seul le Chef Machiniste peut enregistrer des réalisations de broyage");
        }
        SessionBroyage session = repository.trouverParId(req.sessionId())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Session broyage", String.valueOf(req.sessionId())));

        RealisationMachiniste realisation = new RealisationMachiniste(
            req.machinisteMatricule(), req.nomMachiniste(),
            req.operation(), req.typePoudre(),
            req.objectifKg(), req.realiseKg());

        session.enregistrerRealisation(realisation);
        return mapper.versResponse(repository.sauvegarder(session));
    }

    private boolean peutGererBroyage(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("CHEF_MACHINISTE") || role.equals("ADMIN");
    }
}
