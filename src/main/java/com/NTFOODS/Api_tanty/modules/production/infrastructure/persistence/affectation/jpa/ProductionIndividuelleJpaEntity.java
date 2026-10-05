package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productions_individuelles")
@Getter @Setter @NoArgsConstructor
public class ProductionIndividuelleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saisie_id", nullable = false)
    private SaisieRealisationJpaEntity saisie;

    @Column(name = "matricule_employe", nullable = false, length = 20)
    private String matriculeEmploye;

    @Column(name = "nom_employe", nullable = false, length = 100)
    private String nomEmploye;

    @Column(nullable = false, length = 60)
    private String poste;

    @Column(name = "quantite_realisee", nullable = false)
    private double quantiteRealisee;
}
