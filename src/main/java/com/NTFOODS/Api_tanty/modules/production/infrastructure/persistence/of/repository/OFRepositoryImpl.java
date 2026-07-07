package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.jpa.OFJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.jpa.OFJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OFRepositoryImpl implements OFRepository {

    private final OFJpaRepository jpaRepository;

    public OFRepositoryImpl(OFJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public OrdreFabrication sauvegarder(OrdreFabrication of) {
        return toDomain(jpaRepository.save(toEntity(of)));
    }

    @Override
    public Optional<OrdreFabrication> trouverParId(String idOF) {
        return jpaRepository.findByIdOF(idOF).map(this::toDomain);
    }

    @Override
    public List<OrdreFabrication> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    public List<OrdreFabrication> listerParStatut(StatutOF statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<OrdreFabrication> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private OFJpaEntity toEntity(OrdreFabrication of) {
        return OFJpaEntity.builder()
            .id(of.getId()).idOF(of.getIdOF()).referencePPH(of.getReferencePPH())
            .codeProduit(of.getCodeProduit()).designationProduit(of.getDesignationProduit())
            .qteDemandee(of.getQteDemandee()).qteRealisee(of.getQteRealisee())
            .dateButoir(of.getDateButoir()).statut(of.getStatut())
            .dateCreation(of.getDateCreation()).dateModification(of.getDateModification())
            .version(of.getVersion()).build();
    }

    private OrdreFabrication toDomain(OFJpaEntity e) {
        return OrdreFabrication.reconstituer(e.getId(), e.getIdOF(), e.getReferencePPH(),
            e.getCodeProduit(), e.getDesignationProduit(), e.getQteDemandee(), e.getQteRealisee(),
            e.getDateButoir(), e.getStatut(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
