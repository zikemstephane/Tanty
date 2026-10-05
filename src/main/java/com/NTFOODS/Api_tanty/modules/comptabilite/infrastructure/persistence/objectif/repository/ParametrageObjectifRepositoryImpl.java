package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ParametrageObjectif;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ParametrageObjectifRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ParametrageObjectifJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ParametrageObjectifJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ParametrageObjectifRepositoryImpl implements ParametrageObjectifRepository {

    private final ParametrageObjectifJpaRepository jpaRepository;

    public ParametrageObjectifRepositoryImpl(ParametrageObjectifJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ParametrageObjectif sauvegarder(ParametrageObjectif parametrage) {
        return toDomain(jpaRepository.save(toEntity(parametrage)));
    }

    @Override
    public Optional<ParametrageObjectif> trouverParReference(String reference) {
        return jpaRepository.findByReference(reference).map(this::toDomain);
    }

    @Override
    public Optional<ParametrageObjectif> trouverApplicableA(LocalDate date) {
        return jpaRepository.findApplicables(date).stream().findFirst().map(this::toDomain);
    }

    @Override
    public Optional<ParametrageObjectif> trouverCourant() {
        return jpaRepository.findFirstByDateFinIsNullOrderByDateDebutDesc().map(this::toDomain);
    }

    @Override
    public List<ParametrageObjectif> listerHistorique() {
        return jpaRepository.findAllByOrderByDateDebutDesc().stream().map(this::toDomain).toList();
    }

    private ParametrageObjectifJpaEntity toEntity(ParametrageObjectif p) {
        return ParametrageObjectifJpaEntity.builder()
            .id(p.getId()).reference(p.getReference())
            .nombreJoursOuvres(p.getNombreJoursOuvres())
            .dateDebut(p.getDateDebut()).dateFin(p.getDateFin())
            .matriculeAuteur(p.getMatriculeAuteur()).motif(p.getMotif())
            .dateCreation(p.getDateCreation()).version(p.getVersion())
            .build();
    }

    private ParametrageObjectif toDomain(ParametrageObjectifJpaEntity e) {
        return ParametrageObjectif.reconstituer(e.getId(), e.getReference(), e.getNombreJoursOuvres(),
            e.getDateDebut(), e.getDateFin(), e.getMatriculeAuteur(), e.getMotif(),
            e.getDateCreation(), e.getVersion());
    }
}
