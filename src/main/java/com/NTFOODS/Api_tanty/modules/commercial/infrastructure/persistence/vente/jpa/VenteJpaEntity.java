package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.SyncStatus;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroVente;

    @Column(unique = true)
    private String uuidVente;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private String codeClient;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "vente_id")
    private List<LigneVenteJpaEntity> lignes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeReglement modeReglement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVente statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVente typeVente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncStatus syncStatus;

    @Column(length = 100)
    private String coordonneesGPS;

    @Column(length = 50)
    private String idFeuillRoute;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(nullable = false)
    private boolean signatureClient;

    @Column(columnDefinition = "TEXT")
    private String photoProduits;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
