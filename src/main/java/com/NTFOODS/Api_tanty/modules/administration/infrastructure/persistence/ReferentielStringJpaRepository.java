package com.NTFOODS.Api_tanty.modules.administration.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferentielStringJpaRepository extends JpaRepository<ReferentielStringJpaEntity, Long> {
    List<ReferentielStringJpaEntity> findByCategorieOrderByValeurAsc(String categorie);
    void deleteByCategorieAndValeur(String categorie, String valeur);
    boolean existsByCategorieAndValeur(String categorie, String valeur);
}
