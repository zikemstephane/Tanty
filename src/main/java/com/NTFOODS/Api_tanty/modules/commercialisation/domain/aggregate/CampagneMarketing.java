package com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypeCampagne;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CampagneMarketing {

    private Long id;
    private final String code;
    private String libelle;
    private String description;
    private final TypeCampagne type;
    private final LocalDate dateDebut;
    private LocalDate dateFin;
    private String zoneCible;
    private double budgetPrevuFCFA;
    private double budgetDepenseFCFA;
    private StatutCampagne statut;
    private final String matriculeCreateur;
    private final LocalDate dateCreation;
    private LocalDate dateModification;
    private final List<String> promotionsLiees = new ArrayList<>();

    private CampagneMarketing(String code, String libelle, TypeCampagne type, LocalDate dateDebut,
                               String matriculeCreateur, LocalDate dateCreation) {
        this.code = Objects.requireNonNull(code);
        this.libelle = Objects.requireNonNull(libelle);
        this.type = Objects.requireNonNull(type);
        this.dateDebut = Objects.requireNonNull(dateDebut);
        this.matriculeCreateur = Objects.requireNonNull(matriculeCreateur);
        this.statut = StatutCampagne.PLANIFIEE;
        this.dateCreation = dateCreation;
        this.budgetDepenseFCFA = 0;
    }

    public static CampagneMarketing creer(String code, String libelle, String description, TypeCampagne type,
                                           LocalDate dateDebut, LocalDate dateFin, String zoneCible,
                                           double budgetPrevuFCFA, String matriculeCreateur) {
        if (dateFin != null && dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
        if (budgetPrevuFCFA < 0) throw new IllegalArgumentException("Le budget ne peut pas être négatif");
        CampagneMarketing c = new CampagneMarketing(code, libelle, type, dateDebut, matriculeCreateur, LocalDate.now());
        c.description = description;
        c.dateFin = dateFin;
        c.zoneCible = zoneCible;
        c.budgetPrevuFCFA = budgetPrevuFCFA;
        return c;
    }

    public static CampagneMarketing reconstituer(Long id, String code, String libelle, String description,
                                                   TypeCampagne type, LocalDate dateDebut, LocalDate dateFin,
                                                   String zoneCible, double budgetPrevuFCFA,
                                                   double budgetDepenseFCFA, StatutCampagne statut,
                                                   String matriculeCreateur, LocalDate dateCreation,
                                                   LocalDate dateModification, List<String> promotionsLiees) {
        CampagneMarketing c = new CampagneMarketing(code, libelle, type, dateDebut, matriculeCreateur, dateCreation);
        c.id = id;
        c.description = description;
        c.dateFin = dateFin;
        c.zoneCible = zoneCible;
        c.budgetPrevuFCFA = budgetPrevuFCFA;
        c.budgetDepenseFCFA = budgetDepenseFCFA;
        c.statut = statut;
        c.dateModification = dateModification;
        if (promotionsLiees != null) c.promotionsLiees.addAll(promotionsLiees);
        return c;
    }

    public void demarrer() {
        if (statut != StatutCampagne.PLANIFIEE) {
            throw new IllegalStateException("Seule une campagne PLANIFIEE peut démarrer");
        }
        this.statut = StatutCampagne.EN_COURS;
        this.dateModification = LocalDate.now();
    }

    public void terminer() {
        if (statut != StatutCampagne.EN_COURS) {
            throw new IllegalStateException("Seule une campagne EN_COURS peut être terminée");
        }
        this.statut = StatutCampagne.TERMINEE;
        this.dateModification = LocalDate.now();
    }

    public void annuler() {
        this.statut = StatutCampagne.ANNULEE;
        this.dateModification = LocalDate.now();
    }

    public void enregistrerDepense(double montantFCFA) {
        if (montantFCFA < 0) throw new IllegalArgumentException("Le montant doit être positif");
        this.budgetDepenseFCFA += montantFCFA;
        this.dateModification = LocalDate.now();
    }

    public void lierPromotion(String codePromotion) {
        if (!promotionsLiees.contains(codePromotion)) {
            promotionsLiees.add(codePromotion);
        }
    }

    public double getTauxConsommationBudget() {
        if (budgetPrevuFCFA <= 0) return 0;
        return (budgetDepenseFCFA / budgetPrevuFCFA) * 100.0;
    }

    public List<String> getPromotionsLiees() { return Collections.unmodifiableList(promotionsLiees); }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
    public TypeCampagne getType() { return type; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getZoneCible() { return zoneCible; }
    public double getBudgetPrevuFCFA() { return budgetPrevuFCFA; }
    public double getBudgetDepenseFCFA() { return budgetDepenseFCFA; }
    public StatutCampagne getStatut() { return statut; }
    public String getMatriculeCreateur() { return matriculeCreateur; }
    public LocalDate getDateCreation() { return dateCreation; }
    public LocalDate getDateModification() { return dateModification; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CampagneMarketing that)) return false;
        return Objects.equals(code, that.code);
    }
    @Override public int hashCode() { return Objects.hash(code); }
}
