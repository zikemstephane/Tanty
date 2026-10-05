package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ModifierUtilisateurRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ProfilResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.EmailDejaUtiliseException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.UtilisateurNonTrouveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.NomComplet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GererUtilisateursUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public GererUtilisateursUseCase(UtilisateurRepository utilisateurRepository,
                                     PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<ProfilResponse> listerTous() {
        return utilisateurRepository.listerTous().stream()
            .map(this::versResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfilResponse> listerParRole(Role role) {
        return utilisateurRepository.listerParRole(role).stream()
            .map(this::versResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProfilResponse consulter(String matricule) {
        return utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .map(this::versResponse)
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));
    }

    public ProfilResponse modifier(String matricule, ModifierUtilisateurRequest request) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));

        Email nouvelEmail = Email.de(request.email());
        if (!u.getEmail().equals(nouvelEmail) && utilisateurRepository.existeParEmail(nouvelEmail)) {
            throw new EmailDejaUtiliseException(request.email());
        }

        u.changerRole(request.role());
        u.modifierProfil(
            request.telephone(),
            request.residence(),
            request.photo(),
            request.cni(),
            request.dateNaissance(),
            request.lieuNaissance(),
            request.sexe()
        );
        if (request.dateEmbauche() != null) {
            u.setDateEmbauche(request.dateEmbauche());
        }
        if (request.nouveauMotDePasse() != null && !request.nouveauMotDePasse().isBlank()) {
            u.changerMotDePasse(MotDePasse.deHash(passwordEncoder.encode(request.nouveauMotDePasse())));
        }

        Utilisateur sauvegarde = utilisateurRepository.sauvegarder(u);
        return versResponse(sauvegarde);
    }

    public void desactiver(String matricule) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));
        u.desactiver();
        utilisateurRepository.sauvegarder(u);
    }

    public void reactiver(String matricule) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));
        u.reactiver();
        utilisateurRepository.sauvegarder(u);
    }

    public void debloquer(String matricule) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));
        u.debloquer();
        utilisateurRepository.sauvegarder(u);
    }

    public void changerMotDePasse(String matricule, String nouveauMotDePasse) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));
        u.changerMotDePasse(MotDePasse.deHash(passwordEncoder.encode(nouveauMotDePasse)));
        utilisateurRepository.sauvegarder(u);
    }

    public void changerRole(String matricule, Role nouveauRole) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule));
        u.changerRole(nouveauRole);
        utilisateurRepository.sauvegarder(u);
    }

    private ProfilResponse versResponse(Utilisateur u) {
        return new ProfilResponse(
            u.getMatricule().getValeur(),
            u.getNomComplet().getPrenom(),
            u.getNomComplet().getNom(),
            u.getEmail().getValeur(),
            u.getRole(),
            u.isActif(),
            u.getTelephone(),
            u.getResidence(),
            u.getCni(),
            u.getPhoto(),
            u.getDateNaissance(),
            u.getLieuNaissance(),
            u.getDateEmbauche(),
            u.getSexe(),
            u.getDateCreation()
        );
    }
}
