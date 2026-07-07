package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.PrimeRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa.PrimeJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa.PrimeJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class PrimeRepositoryImpl implements PrimeRepository {

    private final PrimeJpaRepository jpaRepository;

    public PrimeRepositoryImpl(PrimeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PrimeCommerciale sauvegarder(PrimeCommerciale prime) {
        PrimeJpaEntity entity = toEntity(prime);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<PrimeCommerciale> trouverParReference(String referencePrime) {
        return jpaRepository.findByReferencePrime(referencePrime).map(this::toDomain);
    }

    @Override
    public List<PrimeCommerciale> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<PrimeCommerciale> listerParStatut(StatutPrime statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PrimeCommerciale> listerParSemaine(LocalDate semaineDebut) {
        return jpaRepository.findBySemaineDebut(semaineDebut).stream().map(this::toDomain).toList();
    }

    @Override
    public String genererReference(String matriculeCommercial, LocalDate semaineDebut) {
        String semaine = semaineDebut.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = jpaRepository.countByMatriculeCommercialAndSemaineDebut(matriculeCommercial, semaineDebut) + 1;
        return "PRM-" + matriculeCommercial + "-" + semaine + "-" + String.format("%02d", count);
    }

    private PrimeJpaEntity toEntity(PrimeCommerciale p) {
        return PrimeJpaEntity.builder()
            .id(p.getId())
            .referencePrime(p.getReferencePrime())
            .matriculeCommercial(p.getMatriculeCommercial())
            .semaineDebut(p.getSemaineDebut())
            .semaineFin(p.getSemaineFin())
            .objectifGlobalFCFA(p.getObjectifGlobalFCFA())
            .totalVentesGlobalFCFA(p.getTotalVentesGlobalFCFA())
            .objectifsParGammeFCFA(new HashMap<>(p.getObjectifsParGammeFCFA()))
            .realisationsParGammeFCFA(new HashMap<>(p.getRealisationsParGammeFCFA()))
            .montantPrimeFCFA(p.getMontantPrimeFCFA())
            .statut(p.getStatut())
            .matriculeValidateur(p.getMatriculeValidateur())
            .dateCreation(p.getDateCreation())
            .dateModification(p.getDateModification())
            .version(p.getVersion())
            .build();
    }

    private PrimeCommerciale toDomain(PrimeJpaEntity e) {
        return PrimeCommerciale.reconstituer(e.getId(), e.getReferencePrime(), e.getMatriculeCommercial(),
            e.getSemaineDebut(), e.getSemaineFin(), e.getObjectifGlobalFCFA(), e.getTotalVentesGlobalFCFA(),
            new HashMap<>(e.getObjectifsParGammeFCFA()), new HashMap<>(e.getRealisationsParGammeFCFA()),
            e.getMontantPrimeFCFA(), e.getStatut(), e.getMatriculeValidateur(), e.getDateCreation(),
            e.getDateModification(), e.getVersion());
    }
}
