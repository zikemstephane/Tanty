package com.NTFOODS.Api_tanty.shared.infrastructure.parametres;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParametresSystemeJpaRepository extends JpaRepository<ParametresSystemeJpaEntity, Long> {
    Optional<ParametresSystemeJpaEntity> findByCle(String cle);
    List<ParametresSystemeJpaEntity> findAllByOrderByCleAsc();
}
