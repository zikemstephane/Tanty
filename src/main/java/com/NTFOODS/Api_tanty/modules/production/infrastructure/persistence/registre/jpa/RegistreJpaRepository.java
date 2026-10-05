package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistreJpaRepository extends JpaRepository<RegistreJpaEntity, Long> {
    Optional<RegistreJpaEntity> findByReferencePPHAndDate(String referencePPH, LocalDate date);
    List<RegistreJpaEntity> findByReferencePPH(String referencePPH);
    boolean existsByReferencePPHAndDate(String referencePPH, LocalDate date);
    List<RegistreJpaEntity> findByDateBetweenOrderByDateDesc(LocalDate debut, LocalDate fin);
}
