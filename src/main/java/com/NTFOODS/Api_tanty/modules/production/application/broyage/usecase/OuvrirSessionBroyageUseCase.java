package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.OuvrirSessionBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.SessionBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.mapper.SessionBroyageMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.ObjectifMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OuvrirSessionBroyageUseCase {

    private final SessionBroyageRepository repository;
    private final SessionBroyageMapper mapper;

    public OuvrirSessionBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SessionBroyageResponse executer(OuvrirSessionBroyageRequest req, Utilisateur acteur) {
        if (!peutGererBroyage(acteur)) {
            throw new AccesRefuseException("Seul le Chef Machiniste peut ouvrir une session broyage");
        }
        if (repository.existeParPPHDateEtType(req.referencePPH(), req.date(), req.typePoudre())) {
            throw new RegleMetierException("Une session broyage existe déjà pour " +
                req.typePoudre().getLibelle() + " le " + req.date() + " (PPH: " + req.referencePPH() + ")");
        }

        List<ObjectifMachiniste> objectifs = req.objectifsMachinistes().stream()
            .map(o -> new ObjectifMachiniste(o.machinisteMatricule(), o.nomMachiniste(),
                o.operation(), req.typePoudre(), o.objectifKg()))
            .toList();

        SessionBroyage session = SessionBroyage.ouvrir(
            req.referencePPH(), req.date(), req.typePoudre(),
            req.objectifJournalierKg(), acteur.getMatricule().getValeur(), objectifs);

        return mapper.versResponse(repository.sauvegarder(session));
    }

    private boolean peutGererBroyage(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("CHEF_MACHINISTE") || role.equals("ADMIN");
    }
}
