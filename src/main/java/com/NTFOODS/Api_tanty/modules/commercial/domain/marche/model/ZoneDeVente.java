package com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Zone de Vente.
 * Secteur géographique assigné à un ou plusieurs commerciaux.
 * Stable sur 3-4 mois. Redistribution mensuelle possible.
 * Le taux d'occupation = (clients livrés 3 derniers mois / clients potentiels) × 100.
 */
public class ZoneDeVente {

    private Long id;
    private final String nom;
    private final String type;
    private String description;
    private int clientsPotentiels;
    private boolean actif;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private ZoneDeVente(String nom, String type, String description, int clientsPotentiels,
                         LocalDateTime dateCreation) {
        this.nom = Objects.requireNonNull(nom, "Nom de zone obligatoire");
        this.type = Objects.requireNonNull(type, "Type de zone obligatoire");
        this.description = description;
        if (clientsPotentiels < 0) throw new IllegalArgumentException("Le nombre de clients potentiels ne peut pas être négatif");
        this.clientsPotentiels = clientsPotentiels;
        this.actif = true;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static ZoneDeVente creer(String nom, String type, String description, int clientsPotentiels) {
        return new ZoneDeVente(nom, type, description, clientsPotentiels, LocalDateTime.now());
    }

    public static ZoneDeVente reconstituer(Long id, String nom, String type, String description,
                                            int clientsPotentiels, boolean actif,
                                            LocalDateTime dateCreation, LocalDateTime dateModification,
                                            Long version) {
        ZoneDeVente z = new ZoneDeVente(nom, type, description, clientsPotentiels, dateCreation);
        z.id = id;
        z.actif = actif;
        z.dateModification = dateModification;
        z.version = version;
        return z;
    }

    public void calibrerClientsPotentiels(int nouveauNombre) {
        if (nouveauNombre < 0) throw new IllegalArgumentException("Le nombre ne peut pas être négatif");
        this.clientsPotentiels = nouveauNombre;
        this.dateModification = LocalDateTime.now();
    }

    public double calculerTauxOccupation(int clientsLivres3Mois) {
        if (clientsPotentiels == 0) return 0.0;
        return Math.min(100.0, (clientsLivres3Mois * 100.0) / clientsPotentiels);
    }

    public boolean estSousExploitee(int clientsLivres3Mois) {
        return calculerTauxOccupation(clientsLivres3Mois) < 30.0;
    }

    public void desactiver() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public int getClientsPotentiels() { return clientsPotentiels; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZoneDeVente that)) return false;
        return Objects.equals(nom, that.nom);
    }
    @Override public int hashCode() { return Objects.hash(nom); }
}
