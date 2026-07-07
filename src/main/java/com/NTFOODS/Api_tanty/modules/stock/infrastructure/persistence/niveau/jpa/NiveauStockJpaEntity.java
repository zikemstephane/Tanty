package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "niveaux_stock",
    uniqueConstraints = @UniqueConstraint(name = "uq_niveau_produit_entrepot",
        columnNames = {"code_produit", "code_entrepot"}),
    indexes = {
        @Index(name = "idx_niveau_entrepot", columnList = "code_entrepot"),
        @Index(name = "idx_niveau_produit", columnList = "code_produit")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NiveauStockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "code_entrepot", nullable = false, length = 60)
    private String codeEntrepot;

    @Column(name = "type_entrepot", nullable = false, length = 20)
    private String typeEntrepot;

    @Column(name = "quantite_en_base", nullable = false)
    private long quantiteEnBase;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
