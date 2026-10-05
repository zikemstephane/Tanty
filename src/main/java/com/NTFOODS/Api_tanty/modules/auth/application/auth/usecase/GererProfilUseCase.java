package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ModifierProfilRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ProfilResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GererProfilUseCase {

    private final UtilisateurRepository utilisateurRepository;

    public GererProfilUseCase(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional(readOnly = true)
    public ProfilResponse consulter(String matricule) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));
        return versResponse(u);
    }

    public ProfilResponse modifier(String matricule, ModifierProfilRequest request) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));

        u.modifierProfil(
            request.telephone(),
            request.residence(),
            request.photo(),
            request.cni(),
            request.dateNaissance(),
            request.lieuNaissance(),
            request.sexe()
        );

        Utilisateur sauvegarde = utilisateurRepository.sauvegarder(u);
        return versResponse(sauvegarde);
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
