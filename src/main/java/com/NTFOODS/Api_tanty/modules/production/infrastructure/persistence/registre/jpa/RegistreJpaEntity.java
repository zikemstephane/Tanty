package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "registres_production",
    uniqueConstraints = @UniqueConstraint(columnNames = {"referencePPH", "date"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistreJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referencePPH;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String matriculeChefProduction;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "registre_id")
    private List<LigneRegistreJpaEntity> lignesEmployes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
