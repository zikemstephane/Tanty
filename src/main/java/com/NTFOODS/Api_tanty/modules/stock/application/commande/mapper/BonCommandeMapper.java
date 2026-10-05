package com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BonCommandeMapper {

    public BonCommandeResponse versResponse(BonCommande bc) {
        var lignes = bc.getLignes().stream()
            .map(l -> new BonCommandeResponse.LigneResponse(
                l.getId(), l.getCodeProduit(), l.getDesignation(), l.getSku(),
                l.getQuantiteCommandee(), l.getQuantiteRecue(), l.getPrixUnitaireHT()))
            .toList();

        return new BonCommandeResponse(
            bc.getId(), bc.getNumero(), bc.getCodeFournisseur(),
            bc.getStatut().name(), bc.getStatut().getLibelle(),
            bc.getDateCommande(), bc.getDateLivraisonPrevue(),
            bc.getDateDebutProduction(), bc.getDateFinProduction(), bc.estEnRetard(LocalDate.now()),
            bc.montantTotalHT(), bc.getMatriculeCreateur(), lignes, bc.getDateCreation(),
            bc.getDateValidation(), bc.getMatriculeValidateur()
        );
    }
}
