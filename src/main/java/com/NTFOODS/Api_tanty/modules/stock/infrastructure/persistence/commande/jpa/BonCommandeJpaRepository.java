package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BonCommandeJpaRepository extends JpaRepository<BonCommandeJpaEntity, Long> {
    Optional<BonCommandeJpaEntity> findByNumero(String numero);
    List<BonCommandeJpaEntity> findByStatut(String statut);
    List<BonCommandeJpaEntity> findByCodeFournisseur(String codeFournisseur);

    @Query("""
        SELECT bc FROM BonCommandeJpaEntity bc
        WHERE bc.dateLivraisonPrevue < :aujourdhui
          AND bc.statut IN ('ENVOYE', 'RECU_PARTIEL')
        """)
    List<BonCommandeJpaEntity> findEnRetard(@Param("aujourdhui") LocalDate aujourdhui);

    @Query("SELECT COUNT(bc) FROM BonCommandeJpaEntity bc WHERE EXTRACT(YEAR FROM bc.dateCommande) = :annee")
    long compterParAnnee(@Param("annee") int annee);
}
