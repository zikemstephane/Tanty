package com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.aggregate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Véhicule.
 * Suivi du parc automobile des commerciaux pour le calcul du rendement
 * (CA zone / litres carburant consommés).
 */
public class Vehicule {

    private Long id;
    private final String immatriculation;
    private String marque;
    private String modele;
    private String matriculeCommercial;
    private boolean actif;
    private double consommationMoyenneL100;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Vehicule(String immatriculation, String marque, String modele,
                      String matriculeCommercial, double consommationMoyenneL100,
                      LocalDateTime dateCreation) {
        this.immatriculation = Objects.requireNonNull(immatriculation, "Immatriculation obligatoire");
        this.marque = marque;
        this.modele = modele;
        this.matriculeCommercial = matriculeCommercial;
        this.actif = true;
        this.consommationMoyenneL100 = consommationMoyenneL100;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Vehicule enregistrer(String immatriculation, String marque, String modele,
                                        String matriculeCommercial, double consommationMoyenneL100) {
        return new Vehicule(immatriculation, marque, modele, matriculeCommercial,
            consommationMoyenneL100, LocalDateTime.now());
    }

    public static Vehicule reconstituer(Long id, String immatriculation, String marque, String modele,
                                         String matriculeCommercial, boolean actif,
                                         double consommationMoyenneL100,
                                         LocalDateTime dateCreation, LocalDateTime dateModification,
                                         Long version) {
        Vehicule v = new Vehicule(immatriculation, marque, modele, matriculeCommercial,
            consommationMoyenneL100, dateCreation);
        v.id = id;
        v.actif = actif;
        v.dateModification = dateModification;
        v.version = version;
        return v;
    }

    public void reassigner(String nouveauMatricule) {
        this.matriculeCommercial = Objects.requireNonNull(nouveauMatricule);
        this.dateModification = LocalDateTime.now();
    }

    public void desactiver() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    public void mettreAJourConsommation(double nouvelleConso) {
        if (nouvelleConso <= 0) throw new IllegalArgumentException("La consommation doit être positive");
        this.consommationMoyenneL100 = nouvelleConso;
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getImmatriculation() { return immatriculation; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public boolean isActif() { return actif; }
    public double getConsommationMoyenneL100() { return consommationMoyenneL100; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicule that)) return false;
        return Objects.equals(immatriculation, that.immatriculation);
    }
    @Override public int hashCode() { return Objects.hash(immatriculation); }
}
