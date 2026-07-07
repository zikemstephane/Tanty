package com.NTFOODS.Api_tanty.modules.controle.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "controle_variance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VarianceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String poste;

    @Column(nullable = false)
    private double standard;

    @Column(nullable = false)
    private double reel;

    @Column(nullable = false)
    private String unite;
}
