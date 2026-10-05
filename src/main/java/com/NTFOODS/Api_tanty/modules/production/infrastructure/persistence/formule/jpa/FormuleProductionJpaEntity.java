package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "formules_production")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FormuleProductionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_produit", nullable = false, unique = true, length = 50)
    private String codeProduit;

    @Column(name = "base_kg", nullable = false, precision = 12, scale = 3)
    private BigDecimal baseKg;

    @OneToMany(mappedBy = "formule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<LigneFormuleJpaEntity> lignes;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
