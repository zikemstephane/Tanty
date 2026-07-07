package com.NTFOODS.Api_tanty.modules.controle.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "controle_budget")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BudgetJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String poste;

    @Column(nullable = false)
    private double budget;

    @Column(nullable = false)
    private double engage;

    @Column(nullable = false)
    private double realise;
}
