package com.NTFOODS.Api_tanty.modules.auth.application.rh.mapper;

import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.rh.model.Employe;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
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

    /**
     * Mapping depuis un compte utilisateur actif (rôle MACHINISTE/CHEF_MACHINISTE).
     * Zone de travail et superviseur ne sont pas stockés sur Utilisateur : renseignés
     * à null pour ne pas inventer de données.
     */
    public EmployeResponse versResponse(Utilisateur u) {
        return new EmployeResponse(
            u.getId(),
            u.getMatricule().getValeur(),
            u.getNomComplet().getNom(),
            u.getNomComplet().getPrenom(),
            u.getNomComplet().getNomComplet(),
            u.getRole(),
            u.getResidence(),
            null,
            true,
            u.isActif(),
            u.getDateEmbauche(),
            u.getDateCreation(),
            u.getDateModification()
        );
    }
}
