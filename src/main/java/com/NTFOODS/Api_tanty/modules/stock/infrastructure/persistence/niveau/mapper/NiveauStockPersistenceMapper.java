package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa.NiveauStockJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NiveauStockPersistenceMapper {

    public NiveauStockJpaEntity versEntite(NiveauStock n) {
        return NiveauStockJpaEntity.builder()
            .id(n.getId())
            .codeProduit(n.getCodeProduit())
            .codeEntrepot(n.getCodeEntrepot())
            .typeEntrepot(n.getTypeEntrepot().name())
            .quantiteEnBase(n.getQuantiteEnBase())
            .dateModification(n.getDateModification())
            .version(n.getVersion())
            .build();
    }

    public NiveauStock versDomaine(NiveauStockJpaEntity e) {
        return NiveauStock.reconstituer(
            e.getId(), e.getCodeProduit(), e.getCodeEntrepot(),
            TypeEntrepot.valueOf(e.getTypeEntrepot()), e.getQuantiteEnBase(),
            e.getDateModification(), e.getVersion()
        );
    }
}
