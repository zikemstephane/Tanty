package com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.aggregate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Terminal (Smartphone Préfinancé).
 * Suivi du préfinancement des terminaux mobiles des commerciaux.
 * Remboursement progressif du resteAPayer via déduction sur les primes hebdo.
 */
public class Terminal {

    private Long id;
    private final String imei;
    private final String modele;
    private String matriculeCommercial;
    private final double prixAchat;
    private double resteAPayer;
    private boolean actif;
    private LocalDateTime dateDerniereConnexion;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Terminal(String imei, String modele, String matriculeCommercial,
                     double prixAchat, double resteAPayer, LocalDateTime dateCreation) {
        this.imei = Objects.requireNonNull(imei, "IMEI obligatoire");
        if (imei.length() != 15) throw new IllegalArgumentException("L'IMEI doit comporter 15 caractères");
        this.modele = Objects.requireNonNull(modele, "Modèle obligatoire");
        this.matriculeCommercial = matriculeCommercial;
        if (prixAchat < 0) throw new IllegalArgumentException("Le prix d'achat ne peut pas être négatif");
        this.prixAchat = prixAchat;
        if (resteAPayer < 0) throw new IllegalArgumentException("Le reste à payer ne peut pas être négatif");
        this.resteAPayer = resteAPayer;
        this.actif = true;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Terminal attribuer(String imei, String modele, String matriculeCommercial,
                                      double prixAchat) {
        return new Terminal(imei, modele, matriculeCommercial, prixAchat, prixAchat, LocalDateTime.now());
    }

    public static Terminal reconstituer(Long id, String imei, String modele, String matriculeCommercial,
                                         double prixAchat, double resteAPayer, boolean actif,
                                         LocalDateTime dateDerniereConnexion,
                                         LocalDateTime dateCreation, LocalDateTime dateModification,
                                         Long version) {
        Terminal t = new Terminal(imei, modele, matriculeCommercial, prixAchat, resteAPayer, dateCreation);
        t.id = id;
        t.actif = actif;
        t.dateDerniereConnexion = dateDerniereConnexion;
        t.dateModification = dateModification;
        t.version = version;
        return t;
    }

    public void deduirePrime(double montant) {
        if (montant <= 0) throw new IllegalArgumentException("Le montant déduit doit être positif");
        this.resteAPayer = Math.max(0, this.resteAPayer - montant);
        this.dateModification = LocalDateTime.now();
    }

    public void desactiver() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    public void reactiver() {
        this.actif = true;
        this.dateModification = LocalDateTime.now();
    }

    public void reassigner(String nouveauMatricule) {
        this.matriculeCommercial = Objects.requireNonNull(nouveauMatricule);
        this.dateModification = LocalDateTime.now();
    }

    public void enregistrerConnexion() {
        this.dateDerniereConnexion = LocalDateTime.now();
    }

    public boolean estRembourse() {
        return resteAPayer == 0;
    }

    public Long getId() { return id; }
    public String getImei() { return imei; }
    public String getModele() { return modele; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public double getPrixAchat() { return prixAchat; }
    public double getResteAPayer() { return resteAPayer; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateDerniereConnexion() { return dateDerniereConnexion; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terminal that)) return false;
        return Objects.equals(imei, that.imei);
    }
    @Override public int hashCode() { return Objects.hash(imei); }
}
