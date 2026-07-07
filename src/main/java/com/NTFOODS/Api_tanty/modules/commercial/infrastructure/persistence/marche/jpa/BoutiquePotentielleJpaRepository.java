package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoutiquePotentielleJpaRepository extends JpaRepository<BoutiquePotentielleJpaEntity, Long> {
    List<BoutiquePotentielleJpaEntity> findBySecteurAndActifTrue(String secteur);

    @Query("SELECT DISTINCT b.secteur FROM BoutiquePotentielleJpaEntity b WHERE b.actif = true ORDER BY b.secteur")
    List<String> findDistinctSecteurs();
}
