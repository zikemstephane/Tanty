package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa.MouvementStockJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MouvementStockPersistenceMapper {

    public MouvementStockJpaEntity versEntite(MouvementStock m) {
        return MouvementStockJpaEntity.builder()
            .id(m.getId())
            .reference(m.getReference())
            .type(m.getType().name())
            .codeProduit(m.getCodeProduit())
            .sku(m.getSku())
            .quantiteEnBase(m.getQuantiteEnBase())
            .codeEntrepotSource(m.getCodeEntrepotSource())
            .codeEntrepotCible(m.getCodeEntrepotCible())
            .matriculeActeur(m.getMatriculeActeur())
            .referenceDocument(m.getReferenceDocument())
            .motif(m.getMotif())
            .horodatage(m.getHorodatage())
            .build();
    }

    public MouvementStock versDomaine(MouvementStockJpaEntity e) {
        return MouvementStock.reconstituer(
            e.getId(), e.getReference(), TypeMouvement.valueOf(e.getType()), e.getCodeProduit(),
            e.getSku(), e.getQuantiteEnBase(), e.getCodeEntrepotSource(), e.getCodeEntrepotCible(),
            e.getMatriculeActeur(), e.getReferenceDocument(), e.getMotif(), e.getHorodatage()
        );
    }
}
