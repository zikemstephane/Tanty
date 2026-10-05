package com.NTFOODS.Api_tanty.modules.commercial.application.precommande.mapper;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.dto.response.PreCommandeResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import org.springframework.stereotype.Component;

@Component
public class PreCommandeMapper {

    private final UtilisateurRepository utilisateurRepository;

    public PreCommandeMapper(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public PreCommandeResponse versResponse(PreCommande p) {
        var lignes = p.getLignes().stream()
            .map(l -> new PreCommandeResponse.LigneResponse(l.getCodeProduit(), l.getDesignationProduit(),
                l.getConditionnement(), l.getQuantiteDemandee(), l.getQuantiteValidee()))
            .toList();
        String nomCommercial = utilisateurRepository.trouverParMatricule(Matricule.de(p.getMatriculeCommercial()))
            .map(u -> u.getNomComplet().getNomComplet())
            .orElse(p.getMatriculeCommercial());
        return new PreCommandeResponse(p.getId(), p.getNumeroPreCommande(), p.getMatriculeCommercial(),
            nomCommercial, p.getDateSoumission(), p.getDateEffet(), p.getStatut().name(),
            p.getMatriculeValidateurSecretaire(), p.getMatriculeValidateurComptable(), p.getMotifRefus(),
            p.isSoumiseEnSoiree(), p.getMatriculeArbitre(), p.getDateArbitrage(), p.getMotifArbitrage(), lignes);
    }
}
