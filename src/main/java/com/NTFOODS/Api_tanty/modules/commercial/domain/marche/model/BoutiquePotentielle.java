package com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Boutique Potentielle (référentiel de recensement terrain).
 *
 * Sert de dénominateur au Taux d'Occupation des Marchés (Pilier 4) : la
 * direction compare le nombre de clients réellement livrés au nombre de
 * boutiques potentielles recensées dans chaque secteur, pour mesurer la
 * pénétration réelle de la force commerciale sur le terrain.
 */
public class BoutiquePotentielle {

    private Long id;
    private String nom;
    private final String secteur;
    private String adresse;
    private final String matriculeRecenseur;
    private boolean actif;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private BoutiquePotentielle(String nom, String secteur, String adresse, String matriculeRecenseur,
                                 LocalDateTime dateCreation) {
        this.nom = Objects.requireNonNull(nom);
        this.secteur = Objects.requireNonNull(secteur).trim();
        this.adresse = adresse;
        this.matriculeRecenseur = Objects.requireNonNull(matriculeRecenseur);
        this.actif = true;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static BoutiquePotentielle recenser(String nom, String secteur, String adresse, String matriculeRecenseur) {
        return new BoutiquePotentielle(nom, secteur, adresse, matriculeRecenseur, LocalDateTime.now());
    }

    public static BoutiquePotentielle reconstituer(Long id, String nom, String secteur, String adresse,
                                                    String matriculeRecenseur, boolean actif,
                                                    LocalDateTime dateCreation, LocalDateTime dateModification,
                                                    Long version) {
        BoutiquePotentielle b = new BoutiquePotentielle(nom, secteur, adresse, matriculeRecenseur, dateCreation);
        b.id = id;
        b.actif = actif;
        b.dateModification = dateModification;
        b.version = version;
        return b;
    }

    public void desactiver() { this.actif = false; this.dateModification = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getSecteur() { return secteur; }
    public String getAdresse() { return adresse; }
    public String getMatriculeRecenseur() { return matriculeRecenseur; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoutiquePotentielle that)) return false;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
