package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TerminalJpaRepository extends JpaRepository<TerminalJpaEntity, Long> {
    Optional<TerminalJpaEntity> findByImei(String imei);
    Optional<TerminalJpaEntity> findByMatriculeCommercial(String matriculeCommercial);
    List<TerminalJpaEntity> findByActifTrue();
}
