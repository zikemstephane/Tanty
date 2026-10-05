package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DotationJpaRepository extends JpaRepository<DotationJpaEntity, Long> {
    Optional<DotationJpaEntity> findByNumero(String numero);
    List<DotationJpaEntity> findByStatut(String statut);
    List<DotationJpaEntity> findByMatriculeCommercial(String matriculeCommercial);
    List<DotationJpaEntity> findByDateEffet(LocalDate dateEffet);

    @Query("SELECT COUNT(d) FROM DotationJpaEntity d WHERE EXTRACT(YEAR FROM d.dateDotation) = :annee")
    long compterParAnnee(@Param("annee") int annee);
}
