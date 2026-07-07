package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.DureeAffectation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lignes_affectation")
@Getter @Setter @NoArgsConstructor
public class LigneAffectationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affectation_id", nullable = false)
    private AffectationJournaliereJpaEntity affectation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodePoste poste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DureeAffectation duree;

    @Column(name = "objectif_ajuste", nullable = false)
    private double objectifAjuste;

    /** Liste des matricules séparés par virgule. */
    @Column(name = "matricules_employes", nullable = false, length = 500)
    private String matriculesEmployesJson;
}
