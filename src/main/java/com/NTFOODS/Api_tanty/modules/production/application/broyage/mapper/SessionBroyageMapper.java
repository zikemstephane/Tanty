package com.NTFOODS.Api_tanty.modules.production.application.broyage.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.SessionBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import org.springframework.stereotype.Component;

@Component
public class SessionBroyageMapper {

    public SessionBroyageResponse versResponse(SessionBroyage s) {
        var objectifs = s.getObjectifsMachinistes().stream()
            .map(o -> new SessionBroyageResponse.ObjectifMachinisteResponse(
                o.getMachinisteMatricule(), o.getNomMachiniste(),
                o.getOperation().name(), o.getOperation().getLibelle(),
                o.getObjectifKg()))
            .toList();

        var realisations = s.getRealisations().stream()
            .map(r -> new SessionBroyageResponse.RealisationMachinisteResponse(
                r.getMachinisteMatricule(), r.getNomMachiniste(),
                r.getOperation().name(), r.getOperation().getLibelle(),
                r.getObjectifKg(), r.getRealiseKg(), r.getPctRealisation()))
            .toList();

        return new SessionBroyageResponse(
            s.getId(), s.getReferencePPH(), s.getDate(),
            s.getTypePoudre(), s.getTypePoudre().getLibelle(),
            s.getObjectifJournalierKg(), s.getMatriculeChefMachiniste(),
            objectifs, realisations,
            s.getQuantiteNetteBroyeeKg(), s.getPertesKg(), s.getPctPertes(),
            s.totalRealiseKg(), s.getDepassementAutoriseKg(), s.estJourneeValidee(),
            s.getJustificatifNonValidation(), s.getStatut(),
            s.getDateCreation(), s.getDateModification()
        );
    }
}
