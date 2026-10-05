package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa;

import com.NTFOODS.Api_tanty.modules.stock.domain.retour.enums.StatutBonRetour;
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

    /**
     * Renseigné au comptage seulement : au moment de la déclaration du commercial,
     * aucun gestionnaire n'est encore intervenu.
     */
    @Column(name = "matricule_gestionnaire", length = 20)
    private String matriculeGestionnaire;

    @OneToMany(mappedBy = "bonRetour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneRetourJpaEntity> lignes = new ArrayList<>();

    @Column(name = "date_retour", nullable = false)
    private LocalDate dateRetour;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    /**
     * Nul pour les constats antérieurs à la mise en place du cycle en deux temps :
     * ils ont été traités d'un seul tenant et sont donc considérés comme comptés.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 20)
    private StatutBonRetour statut;

    @Column(name = "date_comptage")
    private LocalDateTime dateComptage;

    @Version
    private Long version;
}
