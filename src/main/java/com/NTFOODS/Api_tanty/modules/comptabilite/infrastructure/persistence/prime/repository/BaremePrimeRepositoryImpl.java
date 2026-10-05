package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.BaremePrimeRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa.BaremePrimeJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa.BaremePrimeJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class BaremePrimeRepositoryImpl implements BaremePrimeRepository {

    private final BaremePrimeJpaRepository jpaRepository;

    public BaremePrimeRepositoryImpl(BaremePrimeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BaremePrime sauvegarder(BaremePrime bareme) {
        return toDomain(jpaRepository.save(toEntity(bareme)));
    }

    @Override
    public Optional<BaremePrime> trouverParReference(String reference) {
        return jpaRepository.findByReference(reference).map(this::toDomain);
    }

    @Override
    public Optional<BaremePrime> trouverApplicableA(LocalDate date) {
        // La requête est ordonnée par prise d'effet décroissante : en cas de
        // chevauchement accidentel, le barème le plus récent l'emporte.
        return jpaRepository.findApplicables(date).stream().findFirst().map(this::toDomain);
    }

    @Override
    public Optional<BaremePrime> trouverCourant() {
        return jpaRepository.findFirstByDateFinIsNullOrderByDateDebutDesc().map(this::toDomain);
    }

    @Override
    public List<BaremePrime> listerHistorique() {
        return jpaRepository.findAllByOrderByDateDebutDesc().stream().map(this::toDomain).toList();
    }

    private BaremePrimeJpaEntity toEntity(BaremePrime b) {
        return BaremePrimeJpaEntity.builder()
            .id(b.getId())
            .reference(b.getReference())
            .tauxGlobalMinimumPourcent(b.getTauxGlobalMinimumPourcent())
            .tauxGammeMinimumPourcent(b.getTauxGammeMinimumPourcent())
            .montantParGammeFCFA(b.getMontantParGammeFCFA())
            .montantMaximumFCFA(b.getMontantMaximumFCFA())
            .dateDebut(b.getDateDebut())
            .dateFin(b.getDateFin())
            .matriculeAuteur(b.getMatriculeAuteur())
            .motif(b.getMotif())
            .dateCreation(b.getDateCreation())
            .version(b.getVersion())
            .build();
    }

    private BaremePrime toDomain(BaremePrimeJpaEntity e) {
        return BaremePrime.reconstituer(e.getId(), e.getReference(), e.getTauxGlobalMinimumPourcent(),
            e.getTauxGammeMinimumPourcent(), e.getMontantParGammeFCFA(), e.getMontantMaximumFCFA(),
            e.getDateDebut(), e.getDateFin(), e.getMatriculeAuteur(), e.getMotif(),
            e.getDateCreation(), e.getVersion());
    }
}
