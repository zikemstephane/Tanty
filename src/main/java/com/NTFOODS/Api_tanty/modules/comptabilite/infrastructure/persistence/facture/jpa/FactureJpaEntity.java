package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "factures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FactureJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroFacture;

    @Column(nullable = false)
    private String codeClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeFacture typeFacture;

    private String referenceFactureOrigine;

    @Column(nullable = false)
    private LocalDate dateEmission;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "facture_id")
    private List<LigneFactureJpaEntity> lignes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statut;

    @Column(nullable = false)
    private String matriculeEmetteur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
