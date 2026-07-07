package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutFeuillRoute;
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
@Table(name = "feuills_route")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FeuillRouteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroFeuillRoute;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "feuill_route_id")
    @OrderColumn(name = "ordre_arr")
    private List<ArretFeuillRouteJpaEntity> arrets;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFeuillRoute statut;

    private String matriculeCloture;
    private LocalDateTime heureCloture;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
