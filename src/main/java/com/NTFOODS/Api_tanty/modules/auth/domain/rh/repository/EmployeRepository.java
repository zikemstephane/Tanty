package com.NTFOODS.Api_tanty.modules.auth.domain.rh.repository;

import com.NTFOODS.Api_tanty.modules.auth.domain.rh.model.Employe;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

import java.util.List;
import java.util.Optional;

public interface EmployeRepository {
    Employe sauvegarder(Employe employe);
    Optional<Employe> trouverParMatricule(String matricule);
    List<Employe> trouverParRole(Role role);
    List<Employe> trouverParRoleEtActif(Role role, boolean actif);
    List<Employe> trouverParSuperviseur(String superviseurMatricule);
    List<Employe> trouverTous();
    boolean existeParMatricule(String matricule);
}
