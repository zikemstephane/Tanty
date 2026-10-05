package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_registre_employe")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneRegistreJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String matriculeEmploye;

    @Column(nullable = false)
    private String nomEmploye;

    @Column(nullable = false)
    private boolean present;

    private String motifAbsence;
    private String poste;
    private String heureArrivee;
    private double qteRealisee;
    private String unite;
}
