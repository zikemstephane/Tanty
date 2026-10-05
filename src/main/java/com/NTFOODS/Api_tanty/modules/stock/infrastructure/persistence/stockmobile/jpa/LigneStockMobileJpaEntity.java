package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lignes_stock_mobile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneStockMobileJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String conditionnement;

    @Column(nullable = false)
    private long quantiteDisponible;

    @Column(nullable = false)
    private long quantiteInitiale;

    @Column(nullable = false)
    private long quantiteVendue;

    @Column(nullable = false)
    private long quantiteAvariee;

    @Column(nullable = false)
    private long quantiteDonnee;

    private LocalDateTime dateDernierMouvement;
}
