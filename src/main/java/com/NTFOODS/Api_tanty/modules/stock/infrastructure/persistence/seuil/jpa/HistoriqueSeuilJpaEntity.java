package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Historique APPEND-ONLY des modifications de seuils (Pilier "Gouvernance des
 * seuils" : le DG est seul décisionnaire — chaque changement doit rester
 * consultable : qui, quand, ancienne valeur → nouvelle valeur).
 */
@Entity
@Table(name = "historique_seuils", indexes = {
    @Index(name = "idx_hist_seuil_produit", columnList = "codeProduit"),
    @Index(name = "idx_hist_seuil_date", columnList = "dateModification")
})
@Getter @Setter @NoArgsConstructor
public class HistoriqueSeuilJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String codeProduit;

    @Column(nullable = false, length = 20)
    private String typeEntrepot;

    @Column(nullable = false)
    private long ancienneValeur;

    @Column(nullable = false)
    private long nouvelleValeur;

    @Column(nullable = false, length = 20)
    private String matriculeAuteur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateModification;
}
