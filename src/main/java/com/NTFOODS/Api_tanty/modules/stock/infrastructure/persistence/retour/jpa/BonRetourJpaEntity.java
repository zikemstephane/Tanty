package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bons_retour",
    uniqueConstraints = @UniqueConstraint(name = "uq_retour_numero", columnNames = "numero"),
    indexes = @Index(name = "idx_retour_commercial", columnList = "matricule_commercial"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonRetourJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "matricule_commercial", nullable = false, length = 20)
    private String matriculeCommercial;

    @Column(name = "matricule_gestionnaire", nullable = false, length = 20)
    private String matriculeGestionnaire;

    @OneToMany(mappedBy = "bonRetour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneRetourJpaEntity> lignes = new ArrayList<>();

    @Column(name = "date_retour", nullable = false)
    private LocalDate dateRetour;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Version
    private Long version;
}
