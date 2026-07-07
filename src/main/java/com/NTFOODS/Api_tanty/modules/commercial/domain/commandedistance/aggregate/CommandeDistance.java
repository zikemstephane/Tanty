package com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.entity.LigneCommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.CanalCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Commande à Distance (§5.7).
 *
 * Commande passée par un client hors de la tournée du commercial
 * (appel, WhatsApp, SMS, email). Suit le même cycle de validation
 * croisée que la précommande : RECUE → VALIDEE_SECRETAIRE → VALIDEE_COMPTABLE → LIVREE.
 *
 * Numérotation : CDIST-YYYYMMDD-NNN
 */
public class CommandeDistance {

    private Long id;
    private final String numeroCommande;
    private final String codeClient;
    private final String matriculeCommercial;
    private final CanalCommande canal;
    private final LocalDate dateCommande;
    private final List<LigneCommandeDistance> lignes;
    private StatutCommandeDistance statut;
    private String matriculeValidateurSecretaire;
    private String matriculeValidateurComptable;
    private String motifAnnulation;
    private String observations;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private CommandeDistance(String numeroCommande, String codeClient, String matriculeCommercial,
                             CanalCommande canal, LocalDate dateCommande, LocalDateTime dateCreation) {
        this.numeroCommande = Objects.requireNonNull(numeroCommande);
        this.codeClient = Objects.requireNonNull(codeClient);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.canal = Objects.requireNonNull(canal);
        this.dateCommande = Objects.requireNonNull(dateCommande);
        this.dateCreation = dateCreation;
        this.lignes = new ArrayList<>();
        this.statut = StatutCommandeDistance.RECUE;
        this.version = 0L;
    }

    public static CommandeDistance recevoir(String numeroCommande, String codeClient, String matriculeCommercial,
                                             CanalCommande canal, LocalDate dateCommande,
                                             List<LigneCommandeDistance> lignes, String observations) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins une ligne");
        }
        CommandeDistance cd = new CommandeDistance(numeroCommande, codeClient, matriculeCommercial,
            canal, dateCommande, LocalDateTime.now());
        cd.lignes.addAll(lignes);
        cd.observations = observations;
        return cd;
    }

    public static CommandeDistance reconstituer(Long id, String numeroCommande, String codeClient,
                                                  String matriculeCommercial, CanalCommande canal,
                                                  LocalDate dateCommande, List<LigneCommandeDistance> lignes,
                                                  StatutCommandeDistance statut,
                                                  String matriculeValidateurSecretaire,
                                                  String matriculeValidateurComptable,
                                                  String motifAnnulation, String observations,
                                                  LocalDateTime dateCreation, LocalDateTime dateModification,
                                                  Long version) {
        CommandeDistance cd = new CommandeDistance(numeroCommande, codeClient, matriculeCommercial,
            canal, dateCommande, dateCreation);
        cd.id = id;
        cd.lignes.addAll(lignes != null ? lignes : List.of());
        cd.statut = statut;
        cd.matriculeValidateurSecretaire = matriculeValidateurSecretaire;
        cd.matriculeValidateurComptable = matriculeValidateurComptable;
        cd.motifAnnulation = motifAnnulation;
        cd.observations = observations;
        cd.dateModification = dateModification;
        cd.version = version;
        return cd;
    }

    public void validerParSecretaire(String matriculeSecretaire) {
        if (statut != StatutCommandeDistance.RECUE) {
            throw new IllegalStateException("Seule une commande RECUE peut être validée par la secrétaire");
        }
        this.matriculeValidateurSecretaire = Objects.requireNonNull(matriculeSecretaire);
        this.statut = StatutCommandeDistance.VALIDEE_SECRETAIRE;
        this.dateModification = LocalDateTime.now();
    }

    public void validerParComptable(String matriculeComptable) {
        if (statut != StatutCommandeDistance.VALIDEE_SECRETAIRE) {
            throw new IllegalStateException("La commande doit être validée par la secrétaire d'abord");
        }
        this.matriculeValidateurComptable = Objects.requireNonNull(matriculeComptable);
        this.statut = StatutCommandeDistance.VALIDEE_COMPTABLE;
        this.dateModification = LocalDateTime.now();
    }

    public void marquerLivree() {
        if (statut != StatutCommandeDistance.VALIDEE_COMPTABLE) {
            throw new IllegalStateException("Seule une commande validée par le comptable peut être livrée");
        }
        this.statut = StatutCommandeDistance.LIVREE;
        this.dateModification = LocalDateTime.now();
    }

    public void annuler(String motif) {
        if (statut == StatutCommandeDistance.LIVREE) {
            throw new IllegalStateException("Une commande livrée ne peut pas être annulée");
        }
        this.motifAnnulation = Objects.requireNonNull(motif);
        this.statut = StatutCommandeDistance.ANNULEE;
        this.dateModification = LocalDateTime.now();
    }

    public double montantTotal() {
        return lignes.stream().mapToDouble(LigneCommandeDistance::montantHT).sum();
    }

    public Long getId() { return id; }
    public String getNumeroCommande() { return numeroCommande; }
    public String getCodeClient() { return codeClient; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public CanalCommande getCanal() { return canal; }
    public LocalDate getDateCommande() { return dateCommande; }
    public List<LigneCommandeDistance> getLignes() { return Collections.unmodifiableList(lignes); }
    public StatutCommandeDistance getStatut() { return statut; }
    public String getMatriculeValidateurSecretaire() { return matriculeValidateurSecretaire; }
    public String getMatriculeValidateurComptable() { return matriculeValidateurComptable; }
    public String getMotifAnnulation() { return motifAnnulation; }
    public String getObservations() { return observations; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandeDistance)) return false;
        return Objects.equals(numeroCommande, ((CommandeDistance) o).numeroCommande);
    }
    @Override public int hashCode() { return Objects.hash(numeroCommande); }
}
