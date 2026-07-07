package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate.Prospect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.repository.ProspectRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.jpa.ProspectJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.jpa.ProspectJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProspectRepositoryImpl implements ProspectRepository {

    private final ProspectJpaRepository jpaRepository;

    public ProspectRepositoryImpl(ProspectJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Prospect sauvegarder(Prospect prospect) {
        return toDomain(jpaRepository.save(toEntity(prospect)));
    }

    @Override
    public Optional<Prospect> trouverParCode(String codeProspect) {
        return jpaRepository.findByCodeProspect(codeProspect).map(this::toDomain);
    }

    @Override
    public List<Prospect> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Prospect> listerParStatut(StatutProspect statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Prospect> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private ProspectJpaEntity toEntity(Prospect p) {
        return ProspectJpaEntity.builder()
            .id(p.getId()).codeProspect(p.getCodeProspect()).nom(p.getNom())
            .telephone(p.getTelephone()).adresse(p.getAdresse()).localite(p.getLocalite())
            .matriculeCommercial(p.getMatriculeCommercial()).canal(p.getCanal())
            .statut(p.getStatut()).observations(p.getObservations())
            .codeClientConverti(p.getCodeClientConverti()).dateProspection(p.getDateProspection())
            .dateCreation(p.getDateCreation()).dateModification(p.getDateModification())
            .version(p.getVersion()).build();
    }

    private Prospect toDomain(ProspectJpaEntity e) {
        return Prospect.reconstituer(e.getId(), e.getCodeProspect(), e.getNom(), e.getTelephone(),
            e.getAdresse(), e.getLocalite(), e.getMatriculeCommercial(), e.getCanal(), e.getStatut(),
            e.getObservations(), e.getCodeClientConverti(), e.getDateProspection(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
