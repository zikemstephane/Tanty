package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fournisseurs",
    uniqueConstraints = @UniqueConstraint(name = "uq_fournisseur_code", columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FournisseurJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "raison_sociale", nullable = false, length = 200)
    private String raisonSociale;

    @Column(name = "telephone", length = 30)
    private String telephone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "adresse", length = 300)
    private String adresse;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Version
    private Long version;
}
