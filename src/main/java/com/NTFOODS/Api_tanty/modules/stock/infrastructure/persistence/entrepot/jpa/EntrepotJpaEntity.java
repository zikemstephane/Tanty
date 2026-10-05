package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "entrepots",
    uniqueConstraints = @UniqueConstraint(name = "uq_entrepot_code", columnNames = "code"),
    indexes = @Index(name = "idx_entrepot_type", columnList = "type"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepotJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 60)
    private String code;

    @Column(name = "libelle", nullable = false, length = 120)
    private String libelle;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "matricule_proprietaire", length = 20)
    private String matriculeProprietaire;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Version
    private Long version;
}
