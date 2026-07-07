package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa.EntrepotJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EntrepotPersistenceMapper {

    public EntrepotJpaEntity versEntite(Entrepot e) {
        return EntrepotJpaEntity.builder()
            .id(e.getId())
            .code(e.getCode())
            .libelle(e.getLibelle())
            .type(e.getType().name())
            .matriculeProprietaire(e.getMatriculeProprietaire())
            .actif(e.isActif())
            .dateCreation(e.getDateCreation())
            .version(e.getVersion())
            .build();
    }

    public Entrepot versDomaine(EntrepotJpaEntity e) {
        return Entrepot.reconstituer(
            e.getId(), e.getCode(), e.getLibelle(), TypeEntrepot.valueOf(e.getType()),
            e.getMatriculeProprietaire(), e.isActif(), e.getDateCreation(), e.getVersion()
        );
    }
}
