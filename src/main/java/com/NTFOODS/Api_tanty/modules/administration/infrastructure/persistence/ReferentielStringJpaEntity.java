package com.NTFOODS.Api_tanty.modules.administration.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "referentiel_string",
    uniqueConstraints = @UniqueConstraint(columnNames = {"categorie", "valeur"}))
@Getter @Setter @NoArgsConstructor
public class ReferentielStringJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String categorie;

    @Column(nullable = false, length = 100)
    private String valeur;
}
