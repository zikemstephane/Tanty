package com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity.LigneFicheProduit;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.events.FicheEnregistreeEvent;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.events.FicheVerrouilleeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Fiche Produit Journalière.
 *
 * Saisie quotidienne de l'responsable de salle : quantités produites par produit
 * pour une journée donnée, liée à un PPH. Cumule vers le PPH.
 *
 * "LOCK NUMÉRIQUE" (Pilier 1 — Immuabilité) : dès que le Gestionnaire de Stock
 * valide physiquement la réception du lot de produit fini correspondant
 * (même referencePPH + même date), la fiche devient VERROUILLÉE. Au-delà de
 * ce point, l'responsable de salle ne peut plus modifier ses chiffres —
 * l'enregistrement devient une preuve numérique immuable pour les audits.
 *
 * Invariants :
 * - Une seule fiche par (referencePPH, date)
 * - Chaque codeProduit est unique dans la fiche
 * - Surproduction non bloquante mais tracée
 * - Aucune modification possible une fois verrouillee = true
 */
public class FicheProduitJournaliere {

    private Long id;
    private final String referencePPH;
    private final String semaine;
    private final LocalDate date;
    private final String matriculeAgentProduction;
    private final List<LigneFicheProduit> lignes;
    private final Map<String, Integer> produitsRealises;
    private final Map<String, Integer> predictionDosage;
    private final Map<String, Integer> predictionPostes;
    private String justificationEcart;
    private boolean verrouillee;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private FicheProduitJournaliere(String referencePPH, String semaine, LocalDate date,
                                     String matriculeAgentProduction,
                                     Map<String, Integer> predictionDosage,
                                     Map<String, Integer> predictionPostes,
                                     LocalDateTime dateCreation) {
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.semaine = Objects.requireNonNull(semaine);
        this.date = Objects.requireNonNull(date);
        this.matriculeAgentProduction = Objects.requireNonNull(matriculeAgentProduction);
        this.predictionDosage = predictionDosage != null ? new HashMap<>(predictionDosage) : new HashMap<>();
        this.predictionPostes = predictionPostes != null ? new HashMap<>(predictionPostes) : new HashMap<>();
        this.dateCreation = dateCreation;
        this.lignes = new ArrayList<>();
        this.produitsRealises = new HashMap<>();
        this.verrouillee = false;
        this.version = 0L;
    }

    public static FicheProduitJournaliere enregistrer(String referencePPH, String semaine,
                                                       LocalDate date, String matriculeAgentProduction,
                                                       List<LigneFicheProduit> lignes,
                                                       Map<String, Integer> predictionDosage,
                                                       Map<String, Integer> predictionPostes) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La fiche doit contenir au moins une ligne de production");
        }
        Set<String> codes = new HashSet<>();
        for (LigneFicheProduit l : lignes) {
            if (!codes.add(l.getCodeProduit())) {
                throw new IllegalArgumentException("Produit en double dans la fiche: " + l.getCodeProduit());
            }
        }
        FicheProduitJournaliere fiche = new FicheProduitJournaliere(
            referencePPH, semaine, date, matriculeAgentProduction, predictionDosage, predictionPostes, LocalDateTime.now()
        );
        fiche.lignes.addAll(lignes);
        for (LigneFicheProduit l : lignes) {
            fiche.produitsRealises.put(l.getCodeProduit(), (int) Math.round(l.getQuantiteProduite()));
        }
        fiche.domainEvents.add(new FicheEnregistreeEvent(referencePPH, date, matriculeAgentProduction, lignes.size()));
        return fiche;
    }

    public static FicheProduitJournaliere reconstituer(Long id, String referencePPH, String semaine,
                                                        LocalDate date, String matriculeAgentProduction,
                                                        List<LigneFicheProduit> lignes,
                                                        Map<String, Integer> produitsRealises,
                                                        Map<String, Integer> predictionDosage,
                                                        Map<String, Integer> predictionPostes,
                                                        String justificationEcart, boolean verrouillee,
                                                        LocalDateTime dateCreation,
                                                        LocalDateTime dateModification, Long version) {
        FicheProduitJournaliere fiche = new FicheProduitJournaliere(
            referencePPH, semaine, date, matriculeAgentProduction, predictionDosage, predictionPostes, dateCreation
        );
        fiche.id = id;
        fiche.lignes.addAll(lignes != null ? lignes : List.of());
        fiche.produitsRealises.putAll(produitsRealises != null ? produitsRealises : Map.of());
        fiche.predictionPostes.putAll(predictionPostes != null ? predictionPostes : Map.of());
        fiche.justificationEcart = justificationEcart;
        fiche.verrouillee = verrouillee;
        fiche.dateModification = dateModification;
        fiche.version = version;
        return fiche;
    }

    public int calculerEcart(String codeProduit) {
        Integer realise = produitsRealises.get(codeProduit);
        Integer predit = predictionDosage.get(codeProduit);
        if (realise == null) return predit != null ? -predit : 0;
        if (predit == null) return realise;
        return realise - predit;
    }

    public double totalProduit() {
        return lignes.stream().mapToDouble(LigneFicheProduit::getQuantiteProduite).sum();
    }

    /**
     * Corrige les chiffres de production AVANT validation stock uniquement.
     * Refuse toute modification dès que la fiche est verrouillée.
     */
    public void modifierLignes(List<LigneFicheProduit> nouvellesLignes) {
        if (verrouillee) {
            throw new IllegalStateException(
                "Fiche verrouillée : le lot de produit fini correspondant a déjà été validé par le stock. " +
                "Aucune modification n'est plus possible (preuve numérique immuable).");
        }
        if (nouvellesLignes == null || nouvellesLignes.isEmpty()) {
            throw new IllegalArgumentException("La fiche doit contenir au moins une ligne de production");
        }
        Set<String> codes = new HashSet<>();
        for (LigneFicheProduit l : nouvellesLignes) {
            if (!codes.add(l.getCodeProduit())) {
                throw new IllegalArgumentException("Produit en double dans la fiche: " + l.getCodeProduit());
            }
        }
        this.lignes.clear();
        this.lignes.addAll(nouvellesLignes);
        this.produitsRealises.clear();
        for (LigneFicheProduit l : nouvellesLignes) {
            this.produitsRealises.put(l.getCodeProduit(), (int) Math.round(l.getQuantiteProduite()));
        }
        this.dateModification = LocalDateTime.now();
    }

    /**
     * "Lock Numérique" : appelé automatiquement (via événement) dès que le
     * Gestionnaire de Stock valide physiquement le lot de produit fini
     * correspondant. Opération idempotente (un second appel ne fait rien).
     */
    public void verrouiller() {
        if (this.verrouillee) return;
        this.verrouillee = true;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new FicheVerrouilleeEvent(referencePPH, date, matriculeAgentProduction));
    }

    public void definirPredictions(Map<String, Integer> predictionDosage,
                                   Map<String, Integer> predictionPostes) {
        if (verrouillee) {
            throw new IllegalStateException("Fiche verrouillée : impossible de modifier les prédictions");
        }
        this.predictionDosage.clear();
        if (predictionDosage != null) this.predictionDosage.putAll(predictionDosage);
        this.predictionPostes.clear();
        if (predictionPostes != null) this.predictionPostes.putAll(predictionPostes);
        this.dateModification = LocalDateTime.now();
    }

    public void definirJustificationEcart(String justification) {
        if (verrouillee) {
            throw new IllegalStateException("Fiche verrouillée : impossible de modifier la justification d'écart");
        }
        this.justificationEcart = justification;
        this.dateModification = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getReferencePPH() { return referencePPH; }
    public String getSemaine() { return semaine; }
    public LocalDate getDate() { return date; }
    public String getMatriculeAgentProduction() { return matriculeAgentProduction; }
    public List<LigneFicheProduit> getLignes() { return Collections.unmodifiableList(lignes); }
    public Map<String, Integer> getProduitsRealises() { return Collections.unmodifiableMap(produitsRealises); }
    public Map<String, Integer> getPredictionDosage() { return Collections.unmodifiableMap(predictionDosage); }
    public Map<String, Integer> getPredictionPostes() { return Collections.unmodifiableMap(predictionPostes); }
    public String getJustificationEcart() { return justificationEcart; }
    public boolean isVerrouillee() { return verrouillee; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FicheProduitJournaliere)) return false;
        FicheProduitJournaliere that = (FicheProduitJournaliere) o;
        return Objects.equals(referencePPH, that.referencePPH) && Objects.equals(date, that.date);
    }
    @Override public int hashCode() { return Objects.hash(referencePPH, date); }
    @Override public String toString() {
        return "FicheProduitJournaliere{pph=" + referencePPH + ", date=" + date + ", lignes=" + lignes.size()
            + ", verrouillee=" + verrouillee + "}";
    }
}
