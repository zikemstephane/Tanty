package com.NTFOODS.Api_tanty.shared.infrastructure.parametres;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parametres_systeme")
@Getter @Setter @NoArgsConstructor
public class ParametresSystemeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String cle;

    @Column(nullable = false)
    private double valeurNumerique;

    @Column(length = 200)
    private String description;

    @Column(length = 40)
    private String modifiePar;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime horodatage;
}
