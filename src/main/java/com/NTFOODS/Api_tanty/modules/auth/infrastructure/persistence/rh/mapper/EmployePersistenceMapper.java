package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.mapper;

import com.NTFOODS.Api_tanty.modules.auth.domain.rh.model.Employe;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.jpa.EmployeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployePersistenceMapper {

    public EmployeJpaEntity versEntite(Employe e) {
        return EmployeJpaEntity.builder()
            .id(e.getId())
            .matricule(e.getMatricule())
            .nom(e.getNom())
            .prenom(e.getPrenom())
            .role(e.getRole())
            .zoneTravail(e.getZoneTravail())
            .superviseurMatricule(e.getSuperviseurMatricule())
            .avecCompte(e.isAvecCompte())
            .actif(e.isActif())
            .dateEmbauche(e.getDateEmbauche())
            .dateCreation(e.getDateCreation())
            .dateModification(e.getDateModification())
            .version(e.getVersion())
            .build();
    }

    public Employe versDomaine(EmployeJpaEntity e) {
        return Employe.reconstituer(
            e.getId(),
            e.getMatricule(),
            e.getNom(),
            e.getPrenom(),
            e.getRole(),
            e.getZoneTravail(),
            e.getSuperviseurMatricule(),
            e.isAvecCompte(),
            e.isActif(),
            e.getDateEmbauche(),
            e.getDateCreation(),
            e.getDateModification(),
            e.getVersion()
        );
    }
}
