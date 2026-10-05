package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repartition_pph_jours")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepartitionJourJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pph_id")
    private Long pphId;

    @Column(name = "date_jour", nullable = false)
    private LocalDate dateJour;

    @OneToMany(mappedBy = "repartitionJour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    private List<BesoinMatierePremiereJourJpaEntity> besoinsMatieresPremiere = new ArrayList<>();

    @OneToMany(mappedBy = "repartitionJour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    private List<BesoinEmballageJourJpaEntity> besoinsEmballage = new ArrayList<>();

    @Column(name = "futs_prevus")
    private double futsPrevus;
}
