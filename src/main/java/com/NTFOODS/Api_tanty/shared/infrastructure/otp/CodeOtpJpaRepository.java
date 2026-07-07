package com.NTFOODS.Api_tanty.shared.infrastructure.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeOtpJpaRepository extends JpaRepository<CodeOtpJpaEntity, Long> {
    Optional<CodeOtpJpaEntity> findFirstByMatriculeAndOperationCibleAndUtiliseFalseOrderByDateGenerationDesc(
        String matricule, String operationCible);
}
