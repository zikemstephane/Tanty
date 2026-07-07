package com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Demande de Carburant.
 * Requiert une double signature (commercial + superviseur) pour validation.
 */
public class DemandeCarburant {

    private Long id;
    private final String referenceCarburant;
    private final String matriculeCommercial;
    private final String immatriculation;
    private final String stationPartenaire;
    private final int kilometrage;
    private final double litresDemandes;
    private final double montant;
    private final LocalDate date;
    private StatutCarburant statut;
    private String signatureCommercial;
    private String signatureSuperviseur;
    private String motifRefus;
    private String photoCompteur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private DemandeCarburant(String referenceCarburant, String matriculeCommercial,
                              String immatriculation, String stationPartenaire, int kilometrage,
                              double litresDemandes, double montant,
                              LocalDate date, LocalDateTime dateCreation) {
        this.referenceCarburant = Objects.requireNonNull(referenceCarburant);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.immatriculation = Objects.requireNonNull(immatriculation);
        this.stationPartenaire = stationPartenaire;
        if (kilometrage < 0) throw new IllegalArgumentException("Le kilométrage ne peut pas être négatif");
        this.kilometrage = kilometrage;
        if (litresDemandes <= 0) throw new IllegalArgumentException("Les litres doivent être positifs");
        this.litresDemandes = litresDemandes;
        if (montant <= 0) throw new IllegalArgumentException("Le montant doit être positif");
        this.montant = montant;
        this.date = Objects.requireNonNull(date);
        this.statut = StatutCarburant.EN_ATTENTE_SIGNATURE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static DemandeCarburant creer(String referenceCarburant, String matriculeCommercial,
                                          String immatriculation, String stationPartenaire, int kilometrage,
                                          double litresDemandes, double montant, LocalDate date) {
        return new DemandeCarburant(referenceCarburant, matriculeCommercial, immatriculation,
            stationPartenaire, kilometrage, litresDemandes, montant, date, LocalDateTime.now());
    }

    public static DemandeCarburant reconstituer(Long id, String referenceCarburant, String matriculeCommercial,
                                                 String immatriculation, String stationPartenaire, int kilometrage,
                                                 double litresDemandes, double montant,
                                                 LocalDate date, StatutCarburant statut,
                                                 String signatureCommercial, String signatureSuperviseur,
                                                 String motifRefus, String photoCompteur,
                                                 LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        DemandeCarburant d = new DemandeCarburant(referenceCarburant, matriculeCommercial, immatriculation,
            stationPartenaire, kilometrage, litresDemandes, montant, date, dateCreation);
        d.id = id;
        d.statut = statut;
        d.signatureCommercial = signatureCommercial;
        d.signatureSuperviseur = signatureSuperviseur;
        d.motifRefus = motifRefus;
        d.photoCompteur = photoCompteur;
        d.dateModification = dateModification;
        d.version = version;
        return d;
    }

    public void signer(String signatureCommercial, String signatureSuperviseur) {
        if (statut != StatutCarburant.EN_ATTENTE_SIGNATURE) {
            throw new IllegalStateException("La demande de carburant a déjà été traitée");
        }
        this.signatureCommercial = Objects.requireNonNull(signatureCommercial, "Signature commercial obligatoire");
        this.signatureSuperviseur = Objects.requireNonNull(signatureSuperviseur, "Signature superviseur obligatoire");
        this.statut = StatutCarburant.VALIDEE;
        this.dateModification = LocalDateTime.now();
    }

    public void refuser(String motif) {
        if (statut != StatutCarburant.EN_ATTENTE_SIGNATURE) {
            throw new IllegalStateException("La demande de carburant a déjà été traitée");
        }
        this.motifRefus = Objects.requireNonNull(motif);
        this.statut = StatutCarburant.REFUSEE;
        this.dateModification = LocalDateTime.now();
    }

    public void setPhotoCompteur(String photoCompteur) {
        this.photoCompteur = photoCompteur;
    }

    public Long getId() { return id; }
    public String getReferenceCarburant() { return referenceCarburant; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getImmatriculation() { return immatriculation; }
    public String getStationPartenaire() { return stationPartenaire; }
    public int getKilometrage() { return kilometrage; }
    public double getLitresDemandes() { return litresDemandes; }
    public double getMontant() { return montant; }
    public LocalDate getDate() { return date; }
    public StatutCarburant getStatut() { return statut; }
    public String getSignatureCommercial() { return signatureCommercial; }
    public String getSignatureSuperviseur() { return signatureSuperviseur; }
    public String getMotifRefus() { return motifRefus; }
    public String getPhotoCompteur() { return photoCompteur; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DemandeCarburant)) return false;
        return Objects.equals(referenceCarburant, ((DemandeCarburant) o).referenceCarburant);
    }
    @Override public int hashCode() { return Objects.hash(referenceCarburant); }
}
