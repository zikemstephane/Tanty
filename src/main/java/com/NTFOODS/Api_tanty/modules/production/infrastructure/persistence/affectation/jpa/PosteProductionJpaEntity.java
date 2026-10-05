package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "postes_production")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PosteProductionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(nullable = false, name = "effectif_reference")
    private int effectifReference;

    @Column(nullable = false, name = "production_par_heure")
    private double productionParHeure;

    @Column(nullable = false, name = "production_journaliere")
    private double productionJournaliere;

    @Column(nullable = false, length = 20)
    private String unite;

    @Column(nullable = false)
    private boolean actif;

    @Column(nullable = false)
    private int ordre;

    @Column(length = 500)
    private String categoriesMetier;
}
