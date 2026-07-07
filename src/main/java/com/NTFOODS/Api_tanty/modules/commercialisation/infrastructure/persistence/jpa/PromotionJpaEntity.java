package com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypePromotion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "promotions", indexes = {
    @Index(name = "idx_promo_code", columnList = "code", unique = true),
    @Index(name = "idx_promo_statut", columnList = "statut"),
    @Index(name = "idx_promo_produit", columnList = "code_produit")
})
@Getter @Setter @NoArgsConstructor
public class PromotionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String libelle;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypePromotion type;

    @Column(name = "valeur_reduction", nullable = false)
    private double valeurReduction;

    @Column(name = "code_produit", length = 30)
    private String codeProduit;

    @Enumerated(EnumType.STRING)
    @Column(name = "segment_cible", length = 20)
    private TypeClient segmentCible;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatutPromotion statut;

    @Column(name = "nombre_utilisations", nullable = false)
    private int nombreUtilisations;

    @Column(name = "limite_utilisations", nullable = false)
    private int limiteUtilisations;

    @Column(name = "matricule_createur", nullable = false, length = 20)
    private String matriculeCreateur;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;
}
