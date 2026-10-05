package com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Recouvrement {

    private Long id;
    private final String referenceRecouvrement;
    private final String codeClient;
    private final String nomClient;
    private final double montantDu;
    private double montantRembourse;
    private final LocalDate dateEcheance;
    private StatutRecouvrement statut;
    private final String matriculeCommercial;
    private String observations;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Recouvrement(String referenceRecouvrement, String codeClient, String nomClient,
                          double montantDu, LocalDate dateEcheance, String matriculeCommercial,
                          LocalDateTime dateCreation) {
        this.referenceRecouvrement = Objects.requireNonNull(referenceRecouvrement);
        this.codeClient = Objects.requireNonNull(codeClient);
        this.nomClient = Objects.requireNonNull(nomClient);
        if (montantDu <= 0) throw new IllegalArgumentException("Le montant dû doit être positif");
        this.montantDu = montantDu;
        this.montantRembourse = 0;
        this.dateEcheance = Objects.requireNonNull(dateEcheance);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.statut = StatutRecouvrement.EN_COURS;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Recouvrement creer(String referenceRecouvrement, String codeClient, String nomClient,
                                      double montantDu, LocalDate dateEcheance, String matriculeCommercial) {
        return new Recouvrement(referenceRecouvrement, codeClient, nomClient, montantDu,
            dateEcheance, matriculeCommercial, LocalDateTime.now());
    }

    public static Recouvrement reconstituer(Long id, String referenceRecouvrement, String codeClient,
                                             String nomClient, double montantDu, double montantRembourse,
                                             LocalDate dateEcheance, StatutRecouvrement statut,
                                             String matriculeCommercial, String observations,
                                             LocalDateTime dateCreation, LocalDateTime dateModification,
                                             Long version) {
        Recouvrement r = new Recouvrement(referenceRecouvrement, codeClient, nomClient, montantDu,
            dateEcheance, matriculeCommercial, dateCreation);
        r.id = id;
        r.montantRembourse = montantRembourse;
        r.statut = statut;
        r.observations = observations;
        r.dateModification = dateModification;
        r.version = version;
        return r;
    }

    public void enregistrerRemboursement(double montant) {
        if (montant <= 0) throw new IllegalArgumentException("Le montant remboursé doit être positif");
        this.montantRembourse += montant;
        if (this.montantRembourse >= this.montantDu) {
            this.montantRembourse = this.montantDu;
            this.statut = StatutRecouvrement.REMBOURSE_TOTAL;
        } else {
            this.statut = StatutRecouvrement.REMBOURSE_PARTIEL;
        }
        this.dateModification = LocalDateTime.now();
    }

    public void passerEnContentieux(String motif) {
        this.statut = StatutRecouvrement.CONTENTIEUX;
        this.observations = motif;
        this.dateModification = LocalDateTime.now();
    }

    public double montantRestant() { return montantDu - montantRembourse; }

    public Long getId() { return id; }
    public String getReferenceRecouvrement() { return referenceRecouvrement; }
    public String getCodeClient() { return codeClient; }
    public String getNomClient() { return nomClient; }
    public double getMontantDu() { return montantDu; }
    public double getMontantRembourse() { return montantRembourse; }
    public LocalDate getDateEcheance() { return dateEcheance; }
    public StatutRecouvrement getStatut() { return statut; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getObservations() { return observations; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recouvrement)) return false;
        return Objects.equals(referenceRecouvrement, ((Recouvrement) o).referenceRecouvrement);
    }
    @Override public int hashCode() { return Objects.hash(referenceRecouvrement); }
}
