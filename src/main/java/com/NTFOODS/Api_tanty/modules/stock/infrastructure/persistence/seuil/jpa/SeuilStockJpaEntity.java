package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "seuils_stock",
    uniqueConstraints = @UniqueConstraint(name = "uq_seuil_produit_type", columnNames = {"code_produit", "type_entrepot"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeuilStockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "type_entrepot", nullable = false, length = 20)
    private String typeEntrepot;

    @Column(name = "seuil_reapprovisionnement", nullable = false)
    private long seuilReapprovisionnement;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
