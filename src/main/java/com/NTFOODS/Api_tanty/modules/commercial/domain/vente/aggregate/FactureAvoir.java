package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Facture d'Avoir.
 * Permet la régularisation d'une vente validée SANS suppression (RG-05).
 * La suite numérique des documents reste intacte pour l'audit DSF.
 * Une fois créée et validée, la facture d'avoir est IMMUABLE.
 */
public class FactureAvoir {

    private Long id;
    private final String numeroAvoir;
    private final String numeroVenteOriginale;
    private final String matriculeCommercial;
    private final String codeClient;
    private final String matriculeComptable;
    private final double montantHT;
    private final double montantTVA;
    private final double montantTTC;
    private final String motif;
    private final LocalDate dateAvoir;
    private StatutAvoir statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateValidation;
    private Long version;

    private FactureAvoir(String numeroAvoir, String numeroVenteOriginale, String matriculeCommercial,
                          String codeClient, String matriculeComptable, double montantHT, double montantTVA,
                          String motif, LocalDate dateAvoir, LocalDateTime dateCreation) {
        this.numeroAvoir = Objects.requireNonNull(numeroAvoir);
        this.numeroVenteOriginale = Objects.requireNonNull(numeroVenteOriginale);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.codeClient = Objects.requireNonNull(codeClient);
        this.matriculeComptable = Objects.requireNonNull(matriculeComptable);
        this.montantHT = montantHT;
        this.montantTVA = montantTVA;
        this.montantTTC = montantHT + montantTVA;
        this.motif = Objects.requireNonNull(motif, "Motif obligatoire pour un avoir");
        this.dateAvoir = Objects.requireNonNull(dateAvoir);
        this.statut = StatutAvoir.EN_ATTENTE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static FactureAvoir emettre(String numeroAvoir, String numeroVenteOriginale,
                                        String matriculeCommercial, String codeClient,
                                        String matriculeComptable, double montantHT, double montantTVA,
                                        String motif, LocalDate dateAvoir) {
        return new FactureAvoir(numeroAvoir, numeroVenteOriginale, matriculeCommercial, codeClient,
            matriculeComptable, montantHT, montantTVA, motif, dateAvoir, LocalDateTime.now());
    }

    public static FactureAvoir reconstituer(Long id, String numeroAvoir, String numeroVenteOriginale,
                                             String matriculeCommercial, String codeClient,
                                             String matriculeComptable, double montantHT, double montantTVA,
                                             String motif, LocalDate dateAvoir, StatutAvoir statut,
                                             LocalDateTime dateCreation, LocalDateTime dateValidation,
                                             Long version) {
        FactureAvoir fa = new FactureAvoir(numeroAvoir, numeroVenteOriginale, matriculeCommercial,
            codeClient, matriculeComptable, montantHT, montantTVA, motif, dateAvoir, dateCreation);
        fa.id = id;
        fa.statut = statut;
        fa.dateValidation = dateValidation;
        fa.version = version;
        return fa;
    }

    public void valider() {
        if (statut != StatutAvoir.EN_ATTENTE) {
            throw new IllegalStateException("L'avoir a déjà été traité");
        }
        this.statut = StatutAvoir.VALIDE;
        this.dateValidation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNumeroAvoir() { return numeroAvoir; }
    public String getNumeroVenteOriginale() { return numeroVenteOriginale; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getCodeClient() { return codeClient; }
    public String getMatriculeComptable() { return matriculeComptable; }
    public double getMontantHT() { return montantHT; }
    public double getMontantTVA() { return montantTVA; }
    public double getMontantTTC() { return montantTTC; }
    public String getMotif() { return motif; }
    public LocalDate getDateAvoir() { return dateAvoir; }
    public StatutAvoir getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactureAvoir that)) return false;
        return Objects.equals(numeroAvoir, that.numeroAvoir);
    }
    @Override public int hashCode() { return Objects.hash(numeroAvoir); }

    public enum StatutAvoir {
        EN_ATTENTE,
        VALIDE
    }
}
