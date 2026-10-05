package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository {
    Utilisateur sauvegarder(Utilisateur utilisateur);
    Optional<Utilisateur> trouverParMatricule(Matricule matricule);
    Optional<Utilisateur> trouverParEmail(Email email);
    boolean existeParEmail(Email email);
    boolean existeParMatricule(Matricule matricule);
    List<Utilisateur> listerParRole(Role role);
    List<Utilisateur> listerTous();
}
