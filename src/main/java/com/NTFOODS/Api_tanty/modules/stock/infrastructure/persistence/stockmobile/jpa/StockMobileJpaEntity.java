package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stocks_mobiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockMobileJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private LocalDate dateSession;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_mobile_id")
    private List<LigneStockMobileJpaEntity> lignes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
