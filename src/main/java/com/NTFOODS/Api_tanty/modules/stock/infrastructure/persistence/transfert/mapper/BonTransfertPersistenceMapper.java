package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums.StatutTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.BonTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.LigneTransfert;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa.BonTransfertJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa.LigneTransfertJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BonTransfertPersistenceMapper {

    public BonTransfertJpaEntity versEntite(BonTransfert bt) {
        BonTransfertJpaEntity entite = BonTransfertJpaEntity.builder()
            .id(bt.getId()).numero(bt.getNumero())
            .codeEntrepotSource(bt.getCodeEntrepotSource()).codeEntrepotCible(bt.getCodeEntrepotCible())
            .statut(bt.getStatut().name()).matriculeGestionnaire(bt.getMatriculeGestionnaire())
            .declencheParSeuil(bt.isDeclencheParSeuil()).dateCreation(bt.getDateCreation())
            .dateExecution(bt.getDateExecution()).version(bt.getVersion())
            .lignes(new ArrayList<>())
            .build();

        List<LigneTransfertJpaEntity> lignes = bt.getLignes().stream()
            .map(l -> LigneTransfertJpaEntity.builder()
                .id(l.getId()).bonTransfert(entite).codeProduit(l.getCodeProduit())
                .sku(l.getSku()).quantite(l.getQuantite()).build())
            .toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    public BonTransfert versDomaine(BonTransfertJpaEntity e) {
        List<LigneTransfert> lignes = e.getLignes().stream()
            .map(l -> LigneTransfert.reconstituer(l.getId(), l.getCodeProduit(), l.getSku(), l.getQuantite()))
            .toList();
        return BonTransfert.reconstituer(e.getId(), e.getNumero(), e.getCodeEntrepotSource(),
            e.getCodeEntrepotCible(), StatutTransfert.valueOf(e.getStatut()), e.getMatriculeGestionnaire(),
            e.isDeclencheParSeuil(), lignes, e.getDateCreation(), e.getDateExecution(), e.getVersion());
    }
}
