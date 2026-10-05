package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "codes_observation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CodeObservationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(nullable = false)
    private boolean requireQuantity;

    @Column(nullable = false)
    private boolean actif;

    @Column(nullable = false)
    private int ordre;
}
