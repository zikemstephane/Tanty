package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.jpa;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String matricule;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false, length = 50)
    private String zoneTravail;

    @Column(length = 50)
    private String superviseurMatricule;

    @Column(nullable = false)
    private boolean avecCompte;

    @Column(nullable = false)
    private boolean actif;

    private LocalDate dateEmbauche;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
