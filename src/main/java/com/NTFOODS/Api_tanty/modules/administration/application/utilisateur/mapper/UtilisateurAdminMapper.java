package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.mapper;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurAdminMapper {
    public UtilisateurResponse versResponse(Utilisateur u) {
        return new UtilisateurResponse(
            u.getId(), u.getMatricule().getValeur(),
            u.getNomComplet().getPrenom(), u.getNomComplet().getNom(),
            u.getNomComplet().getNomComplet(),
            u.getEmail().getValeur(), u.getRole().name(), u.getRole().getLibelle(),
            u.isActif(), u.isCompteBloque(), u.getTentativesEchouees(),
            u.getSessionActuelle() != null,
            u.getTelephone(), u.getResidence(), u.getCni(), u.getPhoto(),
            u.getDateNaissance(), u.getLieuNaissance(), u.getDateEmbauche(), u.getSexe(),
            u.getDateCreation());
    }
}
