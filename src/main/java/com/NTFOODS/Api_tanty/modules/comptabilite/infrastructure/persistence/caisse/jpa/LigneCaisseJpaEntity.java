package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.enums.TypeMouvementCaisse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lignes_caisse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCaisseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referenceOperation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvementCaisse typeMouvement;

    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    private String motif;

    @Column(nullable = false)
    private LocalDateTime dateOperation;

    @Column(nullable = false)
    private String matriculeOperateur;
}
