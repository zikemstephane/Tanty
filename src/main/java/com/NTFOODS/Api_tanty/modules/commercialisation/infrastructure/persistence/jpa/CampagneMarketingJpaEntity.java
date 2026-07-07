package com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypeCampagne;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "campagnes_marketing", indexes = {
    @Index(name = "idx_camp_code", columnList = "code", unique = true),
    @Index(name = "idx_camp_statut", columnList = "statut")
})
@Getter @Setter @NoArgsConstructor
public class CampagneMarketingJpaEntity {

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
    @Column(nullable = false, length = 25)
    private TypeCampagne type;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "zone_cible", length = 100)
    private String zoneCible;

    @Column(name = "budget_prevu_fcfa", nullable = false)
    private double budgetPrevuFCFA;

    @Column(name = "budget_depense_fcfa", nullable = false)
    private double budgetDepenseFCFA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatutCampagne statut;

    @Column(name = "matricule_createur", nullable = false, length = 20)
    private String matriculeCreateur;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @Column(name = "promotions_liees", length = 500)
    private String promotionsLieesCsv;
}
