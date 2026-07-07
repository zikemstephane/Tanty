package com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.dto.response.CaisseResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.JournalCaisse;
import org.springframework.stereotype.Component;

@Component
public class CaisseMapper {
    public CaisseResponse versResponse(JournalCaisse j) {
        var lignes = j.getLignes().stream()
            .map(l -> new CaisseResponse.LigneResponse(l.getReferenceOperation(), l.getTypeMouvement().name(),
                l.getMontant(), l.getMotif(), l.getDateOperation(), l.getMatriculeOperateur()))
            .toList();
        return new CaisseResponse(j.getId(), j.getDate(), j.getSoldeCourant(), j.getSeuilSecurisation(),
            j.getTotalEntrees(), j.getTotalSorties(), lignes);
    }
}
