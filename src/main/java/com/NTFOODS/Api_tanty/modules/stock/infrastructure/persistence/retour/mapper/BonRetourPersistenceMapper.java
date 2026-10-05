package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.retour.enums.StatutBonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.LigneRetour;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa.BonRetourJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa.LigneRetourJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BonRetourPersistenceMapper {

    public BonRetourJpaEntity versEntite(BonRetour b) {
        BonRetourJpaEntity entite = BonRetourJpaEntity.builder()
            .id(b.getId()).numero(b.getNumero()).matriculeCommercial(b.getMatriculeCommercial())
            .matriculeGestionnaire(b.getMatriculeGestionnaire()).dateRetour(b.getDateRetour())
            .dateCreation(b.getDateCreation()).statut(b.getStatut()).dateComptage(b.getDateComptage())
            .version(b.getVersion())
            .lignes(new ArrayList<>())
            .build();

        List<LigneRetourJpaEntity> lignes = b.getLignes().stream()
            .map(l -> LigneRetourJpaEntity.builder()
                .id(l.getId()).bonRetour(entite).codeProduit(l.getCodeProduit()).sku(l.getSku())
                .quantiteDeclaree(l.getQuantiteDeclaree()).quantiteComptee(l.getQuantiteComptee())
                .motif(l.getMotif()).build())
            .toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    public BonRetour versDomaine(BonRetourJpaEntity e) {
        List<LigneRetour> lignes = e.getLignes().stream()
            .map(l -> LigneRetour.reconstituer(l.getId(), l.getCodeProduit(), l.getSku(),
                l.getQuantiteDeclaree(), l.getMotif(), l.getQuantiteComptee()))
            .toList();
        return BonRetour.reconstituer(e.getId(), e.getNumero(), e.getMatriculeCommercial(),
            e.getMatriculeGestionnaire(), lignes, e.getDateRetour(), e.getDateCreation(),
            statutDe(e), e.getDateComptage(), e.getVersion());
    }

    /**
     * Les constats enregistrés avant la mise en place du cycle en deux temps n'ont
     * pas de statut en base. Ils ont été saisis d'un seul tenant par le gestionnaire,
     * donc déjà vérifiés : les rouvrir en {@code DECLARE} laisserait croire qu'un
     * comptage reste à faire. Leur quantité comptée demeure inconnue et le reste.
     */
    private StatutBonRetour statutDe(BonRetourJpaEntity e) {
        return e.getStatut() != null ? e.getStatut() : StatutBonRetour.COMPTE;
    }
}
