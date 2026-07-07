package com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Grille Tarifaire (Pilier 3 : Tarification Verrouillée).
 *
 * Un prix homologué par produit × segment de clientèle (Distributeur,
 * Grossiste, Détaillant...). C'est la SEULE source de vérité pour les prix
 * appliqués sur le terrain : les champs de saisie du prix sont grisés côté
 * mobile, le commercial ne peut jamais taper un prix librement. Seul un
 * {@link PrixExceptionnel} explicitement accordé par le DG ou le Directeur
 * Commercial permet de déroger, pour une transaction précise, à ce tarif.
 */
public class GrilleTarifaire {

    private Long id;
    private final String codeProduit;
    private final TypeClient segment;
    private double prixHomologueFCFA;
    private boolean actif;
    private String matriculeAuteur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private GrilleTarifaire(String codeProduit, TypeClient segment, double prixHomologueFCFA,
                             String matriculeAuteur, LocalDateTime dateCreation) {
        this.codeProduit = Objects.requireNonNull(codeProduit).trim().toUpperCase();
        this.segment = Objects.requireNonNull(segment);
        if (prixHomologueFCFA <= 0) throw new IllegalArgumentException("Le prix homologué doit être positif");
        this.prixHomologueFCFA = prixHomologueFCFA;
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur);
        this.actif = true;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static GrilleTarifaire definir(String codeProduit, TypeClient segment, double prixHomologueFCFA,
                                           String matriculeAuteur) {
        return new GrilleTarifaire(codeProduit, segment, prixHomologueFCFA, matriculeAuteur, LocalDateTime.now());
    }

    public static GrilleTarifaire reconstituer(Long id, String codeProduit, TypeClient segment,
                                                double prixHomologueFCFA, boolean actif, String matriculeAuteur,
                                                LocalDateTime dateCreation, LocalDateTime dateModification,
                                                Long version) {
        GrilleTarifaire g = new GrilleTarifaire(codeProduit, segment, prixHomologueFCFA, matriculeAuteur, dateCreation);
        g.id = id;
        g.actif = actif;
        g.dateModification = dateModification;
        g.version = version;
        return g;
    }

    public void reviser(double nouveauPrixFCFA, String matriculeAuteur) {
        if (nouveauPrixFCFA <= 0) throw new IllegalArgumentException("Le prix homologué doit être positif");
        this.prixHomologueFCFA = nouveauPrixFCFA;
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur);
        this.dateModification = LocalDateTime.now();
    }

    public void desactiver() { this.actif = false; this.dateModification = LocalDateTime.now(); }
    public void reactiver() { this.actif = true; this.dateModification = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public TypeClient getSegment() { return segment; }
    public double getPrixHomologueFCFA() { return prixHomologueFCFA; }
    public boolean isActif() { return actif; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrilleTarifaire that)) return false;
        return Objects.equals(codeProduit, that.codeProduit) && segment == that.segment;
    }
    @Override public int hashCode() { return Objects.hash(codeProduit, segment); }
}
