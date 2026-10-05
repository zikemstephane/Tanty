package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrimeJpaRepository extends JpaRepository<PrimeJpaEntity, Long> {
    Optional<PrimeJpaEntity> findByReferencePrime(String referencePrime);
    List<PrimeJpaEntity> findByMatriculeCommercial(String matriculeCommercial);
    List<PrimeJpaEntity> findByStatut(StatutPrime statut);
    List<PrimeJpaEntity> findBySemaineDebut(LocalDate semaineDebut);
    long countByMatriculeCommercialAndSemaineDebut(String matriculeCommercial, LocalDate semaineDebut);
}
