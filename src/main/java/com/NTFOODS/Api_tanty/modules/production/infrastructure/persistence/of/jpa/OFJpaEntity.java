package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordres_fabrication")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OFJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idOF;

    @Column(nullable = false)
    private String referencePPH;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String designationProduit;

    @Column(nullable = false)
    private int qteDemandee;

    @Column(nullable = false)
    private int qteRealisee;

    @Column(nullable = false)
    private LocalDate dateButoir;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutOF statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
