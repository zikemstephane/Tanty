package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.GrilleTarifaireJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.GrilleTarifaireJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GrilleTarifaireRepositoryImpl implements GrilleTarifaireRepository {

    private final GrilleTarifaireJpaRepository jpaRepository;

    public GrilleTarifaireRepositoryImpl(GrilleTarifaireJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public GrilleTarifaire sauvegarder(GrilleTarifaire grille) {
        return toDomain(jpaRepository.save(toEntity(grille)));
    }

    @Override
    public Optional<GrilleTarifaire> trouver(String codeProduit, TypeClient segment) {
        return jpaRepository.findByCodeProduitAndSegment(codeProduit.trim().toUpperCase(), segment).map(this::toDomain);
    }

    @Override
    public List<GrilleTarifaire> listerParProduit(String codeProduit) {
        return jpaRepository.findByCodeProduit(codeProduit.trim().toUpperCase()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GrilleTarifaire> listerParSegment(TypeClient segment) {
        return jpaRepository.findBySegment(segment).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GrilleTarifaire> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private GrilleTarifaireJpaEntity toEntity(GrilleTarifaire g) {
        return GrilleTarifaireJpaEntity.builder()
            .id(g.getId()).codeProduit(g.getCodeProduit()).segment(g.getSegment())
            .prixHomologueFCFA(g.getPrixHomologueFCFA()).actif(g.isActif())
            .matriculeAuteur(g.getMatriculeAuteur()).dateCreation(g.getDateCreation())
            .dateModification(g.getDateModification()).version(g.getVersion())
            .build();
    }

    private GrilleTarifaire toDomain(GrilleTarifaireJpaEntity e) {
        return GrilleTarifaire.reconstituer(e.getId(), e.getCodeProduit(), e.getSegment(), e.getPrixHomologueFCFA(),
            e.isActif(), e.getMatriculeAuteur(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
