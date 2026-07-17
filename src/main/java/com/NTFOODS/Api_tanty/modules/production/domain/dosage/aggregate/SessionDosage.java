package com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Session de Dosage/Mélange (Agent Doseur).
 *
 * L'Agent Doseur récupère les poudres broyées, vérifie les quantités vs machiniste,
 * procède au dosage-mélange selon les formules, et conditionne en fûts de 50 kg.
 *
 * Règles métier :
 * - Le nombre de fûts sortis correspond au nombre de fûts produits
 * - La prédiction des produits finis est calculée sur les fûts sortis uniquement
 * - Surproduction autorisée avec justification et traçabilité
 * - Toutes les opérations rattachées au PPH en cours
 * - Contrôle de cohérence : compare fûts prévus vs fûts produits
 *
 * Formules de mélange :
 * - Bouillie : 40 kg maïs + 9 kg soja + 1 kg arachide = 50 kg par fût
 * - Mélange arachide : 40 kg maïs + 9 kg arachide + 1 kg soja = 50 kg par fût
 */
public class SessionDosage {

    private Long id;
    private final String referencePPH;
    private final LocalDate date;
    private final String matriculeAgentDoseur;
    private final List<MatierePremiereUtilisee> matieresUtilisees;
    private final List<String> machinesMobilisees;
    private int nbFutsProduits;
    private int nbFutsCasses;
    private String justificationEcart;
    private boolean validee;
    private boolean enPause;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private SessionDosage(String referencePPH, LocalDate date, String matriculeAgentDoseur,
                           LocalDateTime dateCreation) {
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.date = Objects.requireNonNull(date);
        this.matriculeAgentDoseur = Objects.requireNonNull(matriculeAgentDoseur);
        this.dateCreation = dateCreation;
        this.matieresUtilisees = new ArrayList<>();
        this.machinesMobilisees = new ArrayList<>();
        this.nbFutsProduits = 0;
        this.nbFutsCasses = 0;
        this.validee = false;
        this.enPause = false;
        this.version = 0L;
    }

    public static SessionDosage ouvrir(String referencePPH, LocalDate date, String matriculeAgentDoseur) {
        return new SessionDosage(referencePPH, date, matriculeAgentDoseur, LocalDateTime.now());
    }

    public static SessionDosage reconstituer(Long id, String referencePPH, LocalDate date,
                                              String matriculeAgentDoseur,
                                              List<MatierePremiereUtilisee> matieresUtilisees,
                                              List<String> machinesMobilisees,
                                              int nbFutsProduits,
                                              int nbFutsCasses,
                                              String justificationEcart, boolean validee,
                                              boolean enPause,
                                              LocalDateTime dateCreation,
                                              LocalDateTime dateModification, Long version) {
        SessionDosage s = new SessionDosage(referencePPH, date, matriculeAgentDoseur, dateCreation);
        s.id = id;
        if (matieresUtilisees != null) s.matieresUtilisees.addAll(matieresUtilisees);
        if (machinesMobilisees != null) s.machinesMobilisees.addAll(machinesMobilisees);
        s.nbFutsProduits = nbFutsProduits;
        s.nbFutsCasses = nbFutsCasses;
        s.justificationEcart = justificationEcart;
        s.validee = validee;
        s.enPause = enPause;
        s.dateModification = dateModification;
        s.version = version;
        return s;
    }

    private void verifierModifiable() {
        if (validee) throw new IllegalStateException("Session validée — modification impossible");
        if (enPause) throw new IllegalStateException("Session en pause — reprise nécessaire avant modification");
    }

    public void enregistrerMatiereUtilisee(String typeMatiere, double quantiteKg) {
        verifierModifiable();
        if (quantiteKg < 0) throw new IllegalArgumentException("La quantité ne peut pas être négative");
        matieresUtilisees.add(new MatierePremiereUtilisee(typeMatiere, quantiteKg));
        this.dateModification = LocalDateTime.now();
    }

    public void definirMatiereUtilisee(String typeMatiere, double quantiteKg) {
        verifierModifiable();
        if (quantiteKg < 0) throw new IllegalArgumentException("La quantité ne peut pas être négative");
        matieresUtilisees.removeIf(m -> m.getTypeMatiere().equalsIgnoreCase(typeMatiere));
        matieresUtilisees.add(new MatierePremiereUtilisee(typeMatiere, quantiteKg));
        this.dateModification = LocalDateTime.now();
    }

    public void enregistrerMachine(String machineId) {
        verifierModifiable();
        if (!machinesMobilisees.contains(machineId)) {
            machinesMobilisees.add(machineId);
            this.dateModification = LocalDateTime.now();
        }
    }

    public void enregistrerFuts(int nbProduits) {
        verifierModifiable();
        if (nbProduits < 0) throw new IllegalArgumentException("Le nombre de fûts ne peut pas être négatif");
        this.nbFutsProduits = nbProduits;
        this.dateModification = LocalDateTime.now();
    }

    public void justifierEcart(String justification) {
        verifierModifiable();
        this.justificationEcart = Objects.requireNonNull(justification);
        this.dateModification = LocalDateTime.now();
    }

    public void mettreEnPause() {
        if (validee) throw new IllegalStateException("Session validée — mise en pause impossible");
        if (enPause) throw new IllegalStateException("Session déjà en pause");
        this.enPause = true;
        this.dateModification = LocalDateTime.now();
    }

    public void reprendre() {
        if (validee) throw new IllegalStateException("Session validée — reprise impossible");
        if (!enPause) throw new IllegalStateException("La session n'est pas en pause");
        this.enPause = false;
        this.dateModification = LocalDateTime.now();
    }

    public void valider() {
        if (validee) throw new IllegalStateException("Session déjà validée");
        if (enPause) throw new IllegalStateException("Session en pause — reprise nécessaire avant validation");
        if (nbFutsProduits == 0 && matieresUtilisees.isEmpty()) {
            throw new IllegalStateException("La session doit contenir au moins des matières ou des fûts");
        }
        this.validee = true;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Clôture la session de dosage.
     * Dans le workflow TANTY, clôturer équivaut à valider définitivement la session.
     */
    public void cloturer() {
        if (!validee) {
            valider();
        }
    }

    public boolean isCloturee() {
        return validee;
    }

    public boolean isEnPause() {
        return enPause;
    }

    public int getNbFutsNets() {
        return nbFutsProduits;
    }

    public int getNbFutsCasses() {
        return nbFutsCasses;
    }

    public boolean aEcartAvecPrevision(int nbFutsPrevus) {
        return nbFutsProduits != nbFutsPrevus;
    }

    public boolean necessiteJustification(int nbFutsPrevus) {
        return aEcartAvecPrevision(nbFutsPrevus) && justificationEcart == null;
    }

    public Long getId() { return id; }
    public String getReferencePPH() { return referencePPH; }
    public LocalDate getDate() { return date; }
    public String getMatriculeAgentDoseur() { return matriculeAgentDoseur; }
    public List<MatierePremiereUtilisee> getMatieresUtilisees() { return Collections.unmodifiableList(matieresUtilisees); }
    public List<String> getMachinesMobilisees() { return Collections.unmodifiableList(machinesMobilisees); }
    public int getNbFutsProduits() { return nbFutsProduits; }
    public String getJustificationEcart() { return justificationEcart; }
    public boolean isValidee() { return validee; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    public static class MatierePremiereUtilisee {
        private final String typeMatiere;
        private final double quantiteKg;

        public MatierePremiereUtilisee(String typeMatiere, double quantiteKg) {
            this.typeMatiere = Objects.requireNonNull(typeMatiere);
            this.quantiteKg = quantiteKg;
        }

        public String getTypeMatiere() { return typeMatiere; }
        public double getQuantiteKg() { return quantiteKg; }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionDosage that)) return false;
        return Objects.equals(referencePPH, that.referencePPH) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() { return Objects.hash(referencePPH, date); }
}
