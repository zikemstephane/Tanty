package com.NTFOODS.Api_tanty.modules.rp.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rp_campagnes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampagneJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String cible;

    @Column(nullable = false)
    private String canal;

    @Column(nullable = false)
    private String statut;

    private LocalDate datePublication;

    @Column(nullable = false)
    private int portee;
}
