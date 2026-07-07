package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.repository;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.NomComplet;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private final UtilisateurJpaRepository jpa;

    public UtilisateurRepositoryImpl(UtilisateurJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Utilisateur sauvegarder(Utilisateur u) {
        UtilisateurJpaEntity entity = toEntity(u);
        entity = jpa.save(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Utilisateur> trouverParMatricule(Matricule matricule) {
        return jpa.findByMatricule(matricule.getValeur()).map(this::toDomain);
    }

    @Override
    public Optional<Utilisateur> trouverParEmail(Email email) {
        return jpa.findByEmail(email.getValeur()).map(this::toDomain);
    }

    @Override
    public boolean existeParEmail(Email email) {
        return jpa.existsByEmail(email.getValeur());
    }

    @Override
    public boolean existeParMatricule(Matricule matricule) {
        return jpa.existsByMatricule(matricule.getValeur());
    }

    @Override
    public List<Utilisateur> listerParRole(Role role) {
        return jpa.findByRole(role).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Utilisateur> listerTous() {
        return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private UtilisateurJpaEntity toEntity(Utilisateur u) {
        UtilisateurJpaEntity e = new UtilisateurJpaEntity();
        // CORRECTIF : l'id et la version doivent être reportés, sinon Hibernate
        // exécute un INSERT à chaque sauvegarde (au lieu d'un UPDATE) et viole
        // les contraintes d'unicité sur matricule/email dès la 2e sauvegarde
        // d'un même utilisateur (changement de rôle, (dés)activation, mot de passe...).
        e.setId(u.getId());
        e.setMatricule(u.getMatricule().getValeur());
        e.setPrenom(u.getNomComplet().getPrenom());
        e.setNom(u.getNomComplet().getNom());
        e.setEmail(u.getEmail().getValeur());
        e.setMotDePasseHash(u.getMotDePasse().getHash());
        e.setRole(u.getRole());
        e.setActif(u.isActif());
        e.setTentativesEchouees(u.getTentativesEchouees());
        e.setCompteBloque(u.isCompteBloque());
        e.setSessionActuelle(u.getSessionActuelle());
        e.setDateCreation(u.getDateCreation());
        e.setDateModification(u.getDateModification());
        e.setVersion(u.getVersion());
        e.setCni(u.getCni());
        e.setTelephone(u.getTelephone());
        e.setResidence(u.getResidence());
        e.setPhoto(u.getPhoto());
        e.setDateNaissance(u.getDateNaissance());
        e.setLieuNaissance(u.getLieuNaissance());
        e.setDateEmbauche(u.getDateEmbauche());
        e.setSexe(u.getSexe());
        return e;
    }

    private Utilisateur toDomain(UtilisateurJpaEntity e) {
        return Utilisateur.reconstituer(
            e.getId(),
            Matricule.de(e.getMatricule()),
            NomComplet.de(e.getPrenom(), e.getNom()),
            Email.de(e.getEmail()),
            MotDePasse.deHash(e.getMotDePasseHash()),
            e.getRole(),
            e.isActif(),
            e.getTentativesEchouees(),
            e.isCompteBloque(),
            e.getSessionActuelle(),
            e.getDateCreation(),
            e.getDateModification(),
            e.getVersion(),
            e.getCni(),
            e.getTelephone(),
            e.getResidence(),
            e.getPhoto(),
            e.getDateNaissance(),
            e.getLieuNaissance(),
            e.getDateEmbauche(),
            e.getSexe()
        );
    }
}
