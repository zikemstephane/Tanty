package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.jpa;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
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
@Table(name = "sessions_commercial")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionCommercialJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroSession;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private LocalDate datePreparation;

    @Column(nullable = false)
    private LocalDate dateEffet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSession statut;

    @ElementCollection
    @CollectionTable(name = "session_dotations", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "dotation_id")
    private List<String> dotationsIds;

    private String matriculeCloture;
    private String matriculeValidateurSecretaire;
    private String matriculeValidateurComptable;
    private LocalDateTime heureOuverture;
    private LocalDateTime heureCloture;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
