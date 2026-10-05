package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.BoutiquePotentielle;
import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository.BoutiquePotentielleRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.BoutiquePotentielleJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.BoutiquePotentielleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoutiquePotentielleRepositoryImpl implements BoutiquePotentielleRepository {

    private final BoutiquePotentielleJpaRepository jpaRepository;

    public BoutiquePotentielleRepositoryImpl(BoutiquePotentielleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BoutiquePotentielle sauvegarder(BoutiquePotentielle boutique) {
        return toDomain(jpaRepository.save(toEntity(boutique)));
    }

    @Override
    public List<BoutiquePotentielle> listerParSecteur(String secteur) {
        return jpaRepository.findBySecteurAndActifTrue(secteur).stream().map(this::toDomain).toList();
    }

    @Override
    public List<BoutiquePotentielle> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<String> listerSecteurs() {
        return jpaRepository.findDistinctSecteurs();
    }

    private BoutiquePotentielleJpaEntity toEntity(BoutiquePotentielle b) {
        return BoutiquePotentielleJpaEntity.builder()
            .id(b.getId()).nom(b.getNom()).secteur(b.getSecteur()).adresse(b.getAdresse())
            .matriculeRecenseur(b.getMatriculeRecenseur()).actif(b.isActif())
            .dateCreation(b.getDateCreation()).dateModification(b.getDateModification()).version(b.getVersion())
            .build();
    }

    private BoutiquePotentielle toDomain(BoutiquePotentielleJpaEntity e) {
        return BoutiquePotentielle.reconstituer(e.getId(), e.getNom(), e.getSecteur(), e.getAdresse(),
            e.getMatriculeRecenseur(), e.isActif(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
