package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.LigneBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa.BonCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa.LigneBonCommandeJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BonCommandePersistenceMapper {

    public BonCommandeJpaEntity versEntite(BonCommande bc) {
        BonCommandeJpaEntity entite = BonCommandeJpaEntity.builder()
            .id(bc.getId()).numero(bc.getNumero()).codeFournisseur(bc.getCodeFournisseur())
            .statut(bc.getStatut().name()).dateCommande(bc.getDateCommande())
            .dateLivraisonPrevue(bc.getDateLivraisonPrevue()).matriculeCreateur(bc.getMatriculeCreateur())
            .dateCreation(bc.getDateCreation()).dateModification(bc.getDateModification())
            .version(bc.getVersion()).lignes(new ArrayList<>())
            .build();

        List<LigneBonCommandeJpaEntity> lignes = bc.getLignes().stream()
            .map(l -> versLigneEntite(l, entite)).toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    private LigneBonCommandeJpaEntity versLigneEntite(LigneBonCommande l, BonCommandeJpaEntity parent) {
        return LigneBonCommandeJpaEntity.builder()
            .id(l.getId()).bonCommande(parent).codeProduit(l.getCodeProduit())
            .designation(l.getDesignation()).sku(l.getSku())
            .quantiteCommandee(l.getQuantiteCommandee()).quantiteRecue(l.getQuantiteRecue())
            .quantiteProduite(l.getQuantiteProduite()).prixUnitaireHT(l.getPrixUnitaireHT())
            .build();
    }

    public BonCommande versDomaine(BonCommandeJpaEntity e) {
        List<LigneBonCommande> lignes = e.getLignes().stream()
            .map(l -> LigneBonCommande.reconstituer(l.getId(), l.getCodeProduit(), l.getDesignation(),
                l.getSku(), l.getQuantiteCommandee(), l.getQuantiteRecue(),
                l.getQuantiteProduite() != null ? l.getQuantiteProduite() : 0L,
                l.getPrixUnitaireHT()))
            .toList();

        return BonCommande.reconstituer(e.getId(), e.getNumero(), e.getCodeFournisseur(),
            StatutBonCommande.valueOf(e.getStatut()), e.getDateCommande(), e.getDateLivraisonPrevue(),
            e.getMatriculeCreateur(), lignes, e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
