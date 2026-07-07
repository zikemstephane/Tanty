package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "objectifs_machiniste")
@Getter @Setter @NoArgsConstructor
public class ObjectifMachinisteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private SessionBroyageJpaEntity session;

    @Column(name = "machiniste_matricule", nullable = false, length = 20)
    private String machinisteMatricule;

    @Column(name = "nom_machiniste", nullable = false, length = 100)
    private String nomMachiniste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OperationBroyage operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_poudre", nullable = false, length = 20)
    private TypePoudre typePoudre;

    @Column(name = "objectif_kg", nullable = false)
    private double objectifKg;
}
