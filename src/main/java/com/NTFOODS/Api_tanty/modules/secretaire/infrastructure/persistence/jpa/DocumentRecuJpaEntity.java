package com.NTFOODS.Api_tanty.modules.secretaire.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "secretaire_documents_recus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentRecuJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private boolean feuilleRoute;

    @Column(nullable = false)
    private boolean bonCommande;

    @Column(nullable = false)
    private boolean factures;
}
