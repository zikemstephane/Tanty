package com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.aggregate;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.entity.LigneFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.events.FactureEmiseEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Facture / Avoir.
 * Une AVOIR est liée à une FACTURE originale par referenceFactureOrigine.
 */
public class Facture {

    private Long id;
    private final String numeroFacture;
    private final String codeClient;
    private final TypeFacture typeFacture;
    private final String referenceFactureOrigine; // null si FACTURE; rempli si AVOIR
    private final LocalDate dateEmission;
    private final List<LigneFacture> lignes;
    private StatutFacture statut;
    private String matriculeEmetteur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Facture(String numeroFacture, String codeClient, TypeFacture typeFacture,
                    String referenceFactureOrigine, LocalDate dateEmission,
                    List<LigneFacture> lignes, String matriculeEmetteur, LocalDateTime dateCreation) {
        if (typeFacture == TypeFacture.AVOIR && (referenceFactureOrigine == null || referenceFactureOrigine.isBlank())) {
            throw new IllegalArgumentException("Un avoir doit référencer la facture d'origine");
        }
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("Une facture doit avoir au moins une ligne");
        }
        this.numeroFacture = Objects.requireNonNull(numeroFacture);
        this.codeClient = Objects.requireNonNull(codeClient);
        this.typeFacture = Objects.requireNonNull(typeFacture);
        this.referenceFactureOrigine = referenceFactureOrigine;
        this.dateEmission = Objects.requireNonNull(dateEmission);
        this.lignes = new ArrayList<>(lignes);
        this.matriculeEmetteur = Objects.requireNonNull(matriculeEmetteur);
        this.statut = StatutFacture.EMISE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Facture emettre(String numeroFacture, String codeClient, TypeFacture typeFacture,
                                   String referenceFactureOrigine, LocalDate dateEmission,
                                   List<LigneFacture> lignes, String matriculeEmetteur) {
        Facture f = new Facture(numeroFacture, codeClient, typeFacture, referenceFactureOrigine,
            dateEmission, lignes, matriculeEmetteur, LocalDateTime.now());
        f.domainEvents.add(new FactureEmiseEvent(numeroFacture, codeClient, f.getMontantTotalTTC()));
        return f;
    }

    public static Facture reconstituer(Long id, String numeroFacture, String codeClient,
                                        TypeFacture typeFacture, String referenceFactureOrigine,
                                        LocalDate dateEmission, List<LigneFacture> lignes,
                                        StatutFacture statut, String matriculeEmetteur,
                                        LocalDateTime dateCreation, LocalDateTime dateModification,
                                        Long version) {
        Facture f = new Facture(numeroFacture, codeClient, typeFacture, referenceFactureOrigine,
            dateEmission, lignes, matriculeEmetteur, dateCreation);
        f.id = id;
        f.statut = statut;
        f.dateModification = dateModification;
        f.version = version;
        return f;
    }

    public void marquerPayee() {
        if (statut != StatutFacture.EMISE) {
            throw new IllegalStateException("Seule une facture émise peut être marquée payée");
        }
        this.statut = StatutFacture.PAYEE;
        this.dateModification = LocalDateTime.now();
    }

    public void annuler() {
        if (statut == StatutFacture.PAYEE) {
            throw new IllegalStateException("Une facture payée ne peut pas être annulée ; créer un avoir");
        }
        this.statut = StatutFacture.ANNULEE;
        this.dateModification = LocalDateTime.now();
    }

    public double getMontantTotalHT() {
        return lignes.stream().mapToDouble(LigneFacture::getMontantHT).sum();
    }

    public double getMontantTotalTVA() {
        return lignes.stream().mapToDouble(LigneFacture::getMontantTVA).sum();
    }

    public double getMontantTotalTTC() {
        return lignes.stream().mapToDouble(LigneFacture::getMontantTTC).sum();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumeroFacture() { return numeroFacture; }
    public String getCodeClient() { return codeClient; }
    public TypeFacture getTypeFacture() { return typeFacture; }
    public String getReferenceFactureOrigine() { return referenceFactureOrigine; }
    public LocalDate getDateEmission() { return dateEmission; }
    public List<LigneFacture> getLignes() { return Collections.unmodifiableList(lignes); }
    public StatutFacture getStatut() { return statut; }
    public String getMatriculeEmetteur() { return matriculeEmetteur; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facture)) return false;
        return Objects.equals(numeroFacture, ((Facture) o).numeroFacture);
    }
    @Override public int hashCode() { return Objects.hash(numeroFacture); }
}
