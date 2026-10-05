package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "fiches_produit_journalieres",
    uniqueConstraints = @UniqueConstraint(columnNames = {"reference_pph", "date"}))
@Getter @Setter @NoArgsConstructor
public class FicheProduitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_pph", nullable = false, length = 30)
    private String referencePPH;

    @Column(nullable = false, length = 20)
    private String semaine;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "matricule_agent", nullable = false, length = 20)
    private String matriculeAgentProduction;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fiche_id")
    private List<LigneFicheProduitJpaEntity> lignes = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "produits_realises", columnDefinition = "jsonb")
    private Map<String, Integer> produitsRealises;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prediction_dosage", columnDefinition = "jsonb")
    private Map<String, Integer> predictionDosage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prediction_postes", columnDefinition = "jsonb")
    private Map<String, Integer> predictionPostes;

    @Column(name = "justification_ecart")
    private String justificationEcart;

    @Column(nullable = false)
    private boolean verrouillee = false;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
