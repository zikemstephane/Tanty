package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.model.LotStock;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.jpa.LotStockJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class LotStockPersistenceMapper {

    public LotStockJpaEntity versEntite(LotStock l) {
        return LotStockJpaEntity.builder()
            .id(l.getId())
            .numeroLot(l.getNumeroLot())
            .codeProduit(l.getCodeProduit())
            .sku(l.getSku())
            .designation(l.getDesignation())
            .codeFournisseur(l.getCodeFournisseur())
            .codeEntrepot(l.getCodeEntrepot())
            .dateFabrication(l.getDateFabrication())
            .datePeremption(l.getDatePeremption())
            .quantiteInitiale(l.getQuantiteInitiale())
            .quantiteRestante(l.getQuantiteRestante())
            .statut(l.getStatut().name())
            .observations(l.getObservations())
            .dateCreation(l.getDateCreation())
            .dateDernierMouvement(l.getDateDernierMouvement())
            .version(l.getVersion())
            .build();
    }

    public LotStock versDomaine(LotStockJpaEntity e) {
        return LotStock.reconstituer(
            e.getId(), e.getNumeroLot(), e.getCodeProduit(), e.getSku(), e.getDesignation(),
            e.getCodeFournisseur(), e.getCodeEntrepot(), e.getDateFabrication(), e.getDatePeremption(),
            e.getQuantiteInitiale(), e.getQuantiteRestante(), StatutLot.valueOf(e.getStatut()),
            e.getObservations(), e.getDateCreation(), e.getDateDernierMouvement(), e.getVersion());
    }
}
