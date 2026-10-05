package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.events.PreCommandeValideeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Agrégat Racine — Précommande J+1.
 *
 * Les commerciaux soumettent leurs précommandes EN SOIRÉE pour la JOURNÉE SUIVANTE.
 * Règle : soumission avant 18h00 → notification au secrétaire + rappel à 07h00 le lendemain.
 *
 * Cycle de validation : SOUMISE → VALIDEE_SECRETAIRE → VALIDEE_COMPTABLE → LIVREE
 */
public class PreCommande {

    private static final LocalTime HEURE_LIMITE = LocalTime.of(18, 0);

    private Long id;
    private final String numeroPreCommande;
    private final String matriculeCommercial;
    private final LocalDate dateSoumission;
    private final LocalDate dateEffet;
    private final List<LignePreCommande> lignes;
    private StatutPreCommande statut;
    private String matriculeValidateurSecretaire;
    private String matriculeValidateurComptable;
    private String motifRefus;
    private boolean soumiseEnSoiree;
    private String matriculeArbitre;
    private LocalDateTime dateArbitrage;
    private String motifArbitrage;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private PreCommande(String numeroPreCommande, String matriculeCommercial,
                        LocalDate dateSoumission, LocalDate dateEffet, LocalDateTime dateCreation) {
        this.numeroPreCommande = Objects.requireNonNull(numeroPreCommande);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.dateSoumission = Objects.requireNonNull(dateSoumission);
        this.dateEffet = Objects.requireNonNull(dateEffet);
        this.dateCreation = dateCreation;
        this.lignes = new ArrayList<>();
        this.statut = StatutPreCommande.SOUMISE;
        this.version = 0L;
    }

    public static PreCommande soumettre(String numeroPreCommande, String matriculeCommercial,
                                         LocalDate dateSoumission, List<LignePreCommande> lignes) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La précommande doit contenir au moins une ligne");
        }
        LocalDate dateEffet = dateSoumission.plusDays(1);
        LocalDateTime now = LocalDateTime.now();
        PreCommande pc = new PreCommande(numeroPreCommande, matriculeCommercial, dateSoumission, dateEffet, now);
        pc.lignes.addAll(lignes);
        pc.soumiseEnSoiree = now.toLocalTime().isBefore(LocalTime.MIDNIGHT) && now.toLocalTime().isAfter(HEURE_LIMITE.minusMinutes(1));
        return pc;
    }

    public static PreCommande reconstituer(Long id, String numeroPreCommande, String matriculeCommercial,
                                            LocalDate dateSoumission, LocalDate dateEffet,
                                            List<LignePreCommande> lignes, StatutPreCommande statut,
                                            String matriculeValidateurSecretaire,
                                            String matriculeValidateurComptable,
                                            String motifRefus, boolean soumiseEnSoiree,
                                            LocalDateTime dateCreation, LocalDateTime dateModification,
                                            Long version) {
        return reconstituer(id, numeroPreCommande, matriculeCommercial, dateSoumission, dateEffet, lignes,
            statut, matriculeValidateurSecretaire, matriculeValidateurComptable, motifRefus, soumiseEnSoiree,
            null, null, null, dateCreation, dateModification, version);
    }

    public static PreCommande reconstituer(Long id, String numeroPreCommande, String matriculeCommercial,
                                            LocalDate dateSoumission, LocalDate dateEffet,
                                            List<LignePreCommande> lignes, StatutPreCommande statut,
                                            String matriculeValidateurSecretaire,
                                            String matriculeValidateurComptable,
                                            String motifRefus, boolean soumiseEnSoiree,
                                            String matriculeArbitre, LocalDateTime dateArbitrage,
                                            String motifArbitrage,
                                            LocalDateTime dateCreation, LocalDateTime dateModification,
                                            Long version) {
        PreCommande pc = new PreCommande(numeroPreCommande, matriculeCommercial, dateSoumission, dateEffet, dateCreation);
        pc.id = id;
        pc.lignes.addAll(lignes != null ? lignes : List.of());
        pc.statut = statut;
        pc.matriculeValidateurSecretaire = matriculeValidateurSecretaire;
        pc.matriculeValidateurComptable = matriculeValidateurComptable;
        pc.motifRefus = motifRefus;
        pc.soumiseEnSoiree = soumiseEnSoiree;
        pc.matriculeArbitre = matriculeArbitre;
        pc.dateArbitrage = dateArbitrage;
        pc.motifArbitrage = motifArbitrage;
        pc.dateModification = dateModification;
        pc.version = version;
        return pc;
    }

    public void validerParSecretaire(String matriculeSecretaire) {
        if (statut != StatutPreCommande.SOUMISE) {
            throw new IllegalStateException("Seule une précommande SOUMISE peut être validée par la secrétaire");
        }
        this.matriculeValidateurSecretaire = Objects.requireNonNull(matriculeSecretaire);
        this.statut = StatutPreCommande.VALIDEE_SECRETAIRE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new PreCommandeValideeEvent(
            numeroPreCommande, matriculeCommercial, statut, dateEffet
        ));
    }

    public void validerParComptable(String matriculeComptable, Map<String, Integer> quantitesValideesParProduit) {
        if (statut != StatutPreCommande.VALIDEE_SECRETAIRE) {
            throw new IllegalStateException("La précommande doit être validée par la secrétaire d'abord");
        }

        if (quantitesValideesParProduit != null && !quantitesValideesParProduit.isEmpty()) {
            for (Map.Entry<String, Integer> entree : quantitesValideesParProduit.entrySet()) {
                LignePreCommande ligne = lignes.stream()
                    .filter(l -> l.getCodeProduit().equals(entree.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Produit absent de la précommande " + numeroPreCommande + " : " + entree.getKey()));
                ligne.ajusterQuantiteValidee(entree.getValue() != null ? entree.getValue() : ligne.getQuantiteDemandee());
            }
        }

        this.matriculeValidateurComptable = Objects.requireNonNull(matriculeComptable);
        this.statut = StatutPreCommande.VALIDEE_COMPTABLE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new PreCommandeValideeEvent(
            numeroPreCommande, matriculeCommercial, statut, dateEffet
        ));
    }

    public void refuser(String motif, String matriculeValidateur) {
        if (statut == StatutPreCommande.LIVREE) {
            throw new IllegalStateException("Une précommande livrée ne peut pas être refusée");
        }
        this.motifRefus = Objects.requireNonNull(motif);
        this.statut = StatutPreCommande.REFUSEE;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Arbitrage des quantités par le gestionnaire de stock, au vu de la disponibilité réelle.
     *
     * <p>Seule la quantité <em>validée</em> de chaque ligne est ajustée : la quantité
     * demandée par le commercial reste intacte, afin que l'écart entre demande et
     * service reste lisible après coup. Une ligne absente de la demande d'arbitrage
     * conserve sa quantité validée courante.</p>
     *
     * <p>L'arbitrage reste possible tant que la précommande n'est ni livrée ni refusée :
     * la disponibilité peut évoluer jusqu'au moment de la sortie de stock.</p>
     *
     * @param quantitesParProduit quantités retenues, par code produit
     * @param matriculeArbitre    auteur de l'arbitrage, conservé pour la traçabilité
     * @param motif               justification facultative
     * @throws IllegalStateException    si la précommande est déjà livrée ou refusée
     * @throws IllegalArgumentException si un code produit ne figure pas dans la précommande
     */
    public void arbitrerQuantites(Map<String, Integer> quantitesParProduit, String matriculeArbitre, String motif) {
        if (statut == StatutPreCommande.LIVREE) {
            throw new IllegalStateException("Les quantités d'une précommande livrée ne peuvent plus être arbitrées");
        }
        if (statut == StatutPreCommande.REFUSEE) {
            throw new IllegalStateException("Les quantités d'une précommande refusée ne peuvent pas être arbitrées");
        }
        if (quantitesParProduit == null || quantitesParProduit.isEmpty()) {
            throw new IllegalArgumentException("L'arbitrage doit porter sur au moins une ligne");
        }

        for (Map.Entry<String, Integer> entree : quantitesParProduit.entrySet()) {
            LignePreCommande ligne = lignes.stream()
                .filter(l -> l.getCodeProduit().equals(entree.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Produit absent de la précommande " + numeroPreCommande + " : " + entree.getKey()));
            ligne.ajusterQuantiteValidee(entree.getValue() != null ? entree.getValue() : 0);
        }

        this.matriculeArbitre = Objects.requireNonNull(matriculeArbitre, "L'auteur de l'arbitrage est obligatoire");
        this.dateArbitrage = LocalDateTime.now();
        this.motifArbitrage = (motif != null && !motif.isBlank()) ? motif : null;
        this.dateModification = this.dateArbitrage;
    }

    /** Vrai dès qu'un arbitrage a eu lieu, quel qu'en soit le résultat. */
    public boolean estArbitree() {
        return matriculeArbitre != null;
    }

    public void marquerLivree() {
        if (statut != StatutPreCommande.VALIDEE_COMPTABLE) {
            throw new IllegalStateException("Seule une précommande validée par le comptable peut être livrée");
        }
        this.statut = StatutPreCommande.LIVREE;
        this.dateModification = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumeroPreCommande() { return numeroPreCommande; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDateSoumission() { return dateSoumission; }
    public LocalDate getDateEffet() { return dateEffet; }
    public List<LignePreCommande> getLignes() { return Collections.unmodifiableList(lignes); }
    public StatutPreCommande getStatut() { return statut; }
    public String getMatriculeValidateurSecretaire() { return matriculeValidateurSecretaire; }
    public String getMatriculeValidateurComptable() { return matriculeValidateurComptable; }
    public String getMotifRefus() { return motifRefus; }
    public boolean isSoumiseEnSoiree() { return soumiseEnSoiree; }
    public String getMatriculeArbitre() { return matriculeArbitre; }
    public LocalDateTime getDateArbitrage() { return dateArbitrage; }
    public String getMotifArbitrage() { return motifArbitrage; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreCommande)) return false;
        return Objects.equals(numeroPreCommande, ((PreCommande) o).numeroPreCommande);
    }
    @Override public int hashCode() { return Objects.hash(numeroPreCommande); }
}
