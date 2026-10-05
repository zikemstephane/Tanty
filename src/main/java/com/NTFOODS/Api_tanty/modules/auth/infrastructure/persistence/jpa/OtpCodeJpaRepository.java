package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtpCodeJpaRepository extends JpaRepository<OtpCodeJpaEntity, Long> {

    Optional<OtpCodeJpaEntity> findFirstByMatriculeAndUtiliseFalseOrderByDateCreationDesc(String matricule);

    List<OtpCodeJpaEntity> findByMatriculeOrderByDateCreationDesc(String matricule);

    void deleteByDateExpirationBefore(LocalDateTime date);
}
