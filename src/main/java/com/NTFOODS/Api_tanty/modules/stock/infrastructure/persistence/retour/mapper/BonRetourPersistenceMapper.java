package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.mapper;

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
            .dateCreation(b.getDateCreation()).version(b.getVersion())
            .lignes(new ArrayList<>())
            .build();

        List<LigneRetourJpaEntity> lignes = b.getLignes().stream()
            .map(l -> LigneRetourJpaEntity.builder()
                .id(l.getId()).bonRetour(entite).codeProduit(l.getCodeProduit()).sku(l.getSku())
                .quantite(l.getQuantite()).motif(l.getMotif()).build())
            .toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    public BonRetour versDomaine(BonRetourJpaEntity e) {
        List<LigneRetour> lignes = e.getLignes().stream()
            .map(l -> LigneRetour.reconstituer(l.getId(), l.getCodeProduit(), l.getSku(), l.getQuantite(), l.getMotif()))
            .toList();
        return BonRetour.reconstituer(e.getId(), e.getNumero(), e.getMatriculeCommercial(),
            e.getMatriculeGestionnaire(), lignes, e.getDateRetour(), e.getDateCreation(), e.getVersion());
    }
}
