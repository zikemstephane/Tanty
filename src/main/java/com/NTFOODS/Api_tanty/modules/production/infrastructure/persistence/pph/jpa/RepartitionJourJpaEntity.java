package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "repartition_pph_jours")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepartitionJourJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pph_id")
    private Long pphId;

    @Column(name = "date_jour", nullable = false)
    private LocalDate dateJour;

    @Column(name = "poudre_mais_kg")
    private double poudreMaisKg;

    @Column(name = "poudre_soja_kg")
    private double poudreSojaKg;

    @Column(name = "poudre_arachide_kg")
    private double poudreArachideKg;

    @Column(name = "futs_prevus")
    private int futsPrevus;

    @Column(name = "sachets_prevus")
    private int sachetsPrevus;

    @Column(name = "couvercles_prevus")
    private int couverclesPrevus;

    @Column(name = "scelles_prevus")
    private int scellesPrevus;
}
