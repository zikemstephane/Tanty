package com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypePromotion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Promotion {

    private Long id;
    private final String code;
    private String libelle;
    private String description;
    private final TypePromotion type;
    private double valeurReduction;
    private String codeProduit;
    private TypeClient segmentCible;
    private final LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutPromotion statut;
    private int nombreUtilisations;
    private int limiteUtilisations;
    private final String matriculeCreateur;
    private final LocalDate dateCreation;
    private LocalDate dateModification;
    private final List<Object> domainEvents = new ArrayList<>();

    private Promotion(String code, String libelle, TypePromotion type, LocalDate dateDebut,
                      String matriculeCreateur, LocalDate dateCreation) {
        this.code = Objects.requireNonNull(code);
        this.libelle = Objects.requireNonNull(libelle);
        this.type = Objects.requireNonNull(type);
        this.dateDebut = Objects.requireNonNull(dateDebut);
        this.matriculeCreateur = Objects.requireNonNull(matriculeCreateur);
        this.statut = StatutPromotion.BROUILLON;
        this.dateCreation = dateCreation;
        this.nombreUtilisations = 0;
    }

    public static Promotion creer(String code, String libelle, String description, TypePromotion type,
                                   double valeurReduction, String codeProduit, TypeClient segmentCible,
                                   LocalDate dateDebut, LocalDate dateFin, int limiteUtilisations,
                                   String matriculeCreateur) {
        if (valeurReduction < 0) throw new IllegalArgumentException("La valeur de réduction doit être positive");
        if (dateFin != null && dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
        Promotion p = new Promotion(code, libelle, type, dateDebut, matriculeCreateur, LocalDate.now());
        p.description = description;
        p.valeurReduction = valeurReduction;
        p.codeProduit = codeProduit;
        p.segmentCible = segmentCible;
        p.dateFin = dateFin;
        p.limiteUtilisations = limiteUtilisations;
        return p;
    }

    public static Promotion reconstituer(Long id, String code, String libelle, String description,
                                          TypePromotion type, double valeurReduction, String codeProduit,
                                          TypeClient segmentCible, LocalDate dateDebut, LocalDate dateFin,
                                          StatutPromotion statut, int nombreUtilisations, int limiteUtilisations,
                                          String matriculeCreateur, LocalDate dateCreation,
                                          LocalDate dateModification) {
        Promotion p = new Promotion(code, libelle, type, dateDebut, matriculeCreateur, dateCreation);
        p.id = id;
        p.description = description;
        p.valeurReduction = valeurReduction;
        p.codeProduit = codeProduit;
        p.segmentCible = segmentCible;
        p.dateFin = dateFin;
        p.statut = statut;
        p.nombreUtilisations = nombreUtilisations;
        p.limiteUtilisations = limiteUtilisations;
        p.dateModification = dateModification;
        return p;
    }

    public void activer() {
        if (statut != StatutPromotion.BROUILLON && statut != StatutPromotion.SUSPENDUE) {
            throw new IllegalStateException("Seule une promotion BROUILLON ou SUSPENDUE peut être activée");
        }
        this.statut = StatutPromotion.ACTIVEE;
        this.dateModification = LocalDate.now();
    }

    public void suspendre() {
        if (statut != StatutPromotion.ACTIVEE) {
            throw new IllegalStateException("Seule une promotion ACTIVEE peut être suspendue");
        }
        this.statut = StatutPromotion.SUSPENDUE;
        this.dateModification = LocalDate.now();
    }

    public void annuler() {
        this.statut = StatutPromotion.ANNULEE;
        this.dateModification = LocalDate.now();
    }

    public void enregistrerUtilisation() {
        if (statut != StatutPromotion.ACTIVEE) {
            throw new IllegalStateException("La promotion n'est pas active");
        }
        if (limiteUtilisations > 0 && nombreUtilisations >= limiteUtilisations) {
            throw new IllegalStateException("La limite d'utilisations est atteinte");
        }
        if (dateFin != null && LocalDate.now().isAfter(dateFin)) {
            this.statut = StatutPromotion.EXPIREE;
            throw new IllegalStateException("La promotion a expiré");
        }
        this.nombreUtilisations++;
    }

    public boolean estValide() {
        return statut == StatutPromotion.ACTIVEE
            && (dateFin == null || !LocalDate.now().isAfter(dateFin))
            && (limiteUtilisations <= 0 || nombreUtilisations < limiteUtilisations);
    }

    public double calculerRemise(double prixOriginal) {
        if (!estValide()) return 0;
        return switch (type) {
            case POURCENTAGE -> prixOriginal * (valeurReduction / 100.0);
            case MONTANT_FIXE -> Math.min(valeurReduction, prixOriginal);
            case GRATUITE -> 0;
            case REMISE_VOLUME -> prixOriginal * (valeurReduction / 100.0);
        };
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
    public TypePromotion getType() { return type; }
    public double getValeurReduction() { return valeurReduction; }
    public String getCodeProduit() { return codeProduit; }
    public TypeClient getSegmentCible() { return segmentCible; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public StatutPromotion getStatut() { return statut; }
    public int getNombreUtilisations() { return nombreUtilisations; }
    public int getLimiteUtilisations() { return limiteUtilisations; }
    public String getMatriculeCreateur() { return matriculeCreateur; }
    public LocalDate getDateCreation() { return dateCreation; }
    public LocalDate getDateModification() { return dateModification; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promotion that)) return false;
        return Objects.equals(code, that.code);
    }
    @Override public int hashCode() { return Objects.hash(code); }
}
