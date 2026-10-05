package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.model.Fournisseur;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.jpa.FournisseurJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FournisseurPersistenceMapper {

    public FournisseurJpaEntity versEntite(Fournisseur f) {
        return FournisseurJpaEntity.builder()
            .id(f.getId()).code(f.getCode()).raisonSociale(f.getRaisonSociale())
            .telephone(f.getTelephone()).email(f.getEmail()).adresse(f.getAdresse())
            .actif(f.isActif()).dateCreation(f.getDateCreation()).version(f.getVersion())
            .build();
    }

    public Fournisseur versDomaine(FournisseurJpaEntity e) {
        return Fournisseur.reconstituer(e.getId(), e.getCode(), e.getRaisonSociale(), e.getTelephone(),
            e.getEmail(), e.getAdresse(), e.isActif(), e.getDateCreation(), e.getVersion());
    }
}
