package com.NTFOODS.Api_tanty.modules.auth.application.rh.mapper;

import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.rh.model.Employe;
import org.springframework.stereotype.Component;

@Component
public class EmployeMapper {

    public EmployeResponse versResponse(Employe e) {
        return new EmployeResponse(
            e.getId(),
            e.getMatricule(),
            e.getNom(),
            e.getPrenom(),
            e.getNomComplet(),
            e.getRole(),
            e.getZoneTravail(),
            e.getSuperviseurMatricule(),
            e.isAvecCompte(),
            e.isActif(),
            e.getDateEmbauche(),
            e.getDateCreation(),
            e.getDateModification()
        );
    }
}
