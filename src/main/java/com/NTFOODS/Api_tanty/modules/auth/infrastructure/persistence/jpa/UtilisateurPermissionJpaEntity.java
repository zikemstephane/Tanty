package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Permission;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA — Override de permission pour un utilisateur spécifique.
 *
 * Permet d'ajouter (type=ACCORDER) ou de retirer (type=REFUSER) une permission
 * individuelle par rapport aux permissions de base du rôle.
 *
 * Les permissions effectives d'un utilisateur = (permissions du rôle + ACCORDER) - REFUSER.
 */
@Entity
@Table(name = "utilisateur_permissions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"matricule", "permission"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurPermissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricule", nullable = false, length = 20)
    private String matricule;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false, length = 60)
    private Permission permission;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_override", nullable = false, length = 10)
    private TypeOverride typeOverride;

    @Column(name = "motif", length = 255)
    private String motif;

    @Column(name = "matricule_auteur", nullable = false, length = 20)
    private String matriculeAuteur;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private java.time.LocalDateTime dateCreation;

    /**
     * Type d'override :
     * - ACCORDER : accorde une permission que le rôle n'a pas normalement
     * - REFUSER : retire une permission que le rôle a normalement
     */
    public enum TypeOverride {
        ACCORDER,
        REFUSER
    }
}
