package com.NTFOODS.Api_tanty.modules.secretaire.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "secretaire_petite_caisse")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PetiteCaisseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    private String heure;

    @Column(nullable = false)
    private String categorie;

    @Column(nullable = false)
    private LocalDateTime dateOperation;
}
