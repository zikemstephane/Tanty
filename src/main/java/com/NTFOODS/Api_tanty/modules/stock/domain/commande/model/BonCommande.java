package com.NTFOODS.Api_tanty.modules.stock.domain.commande.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.event.BonCommandeValideEvent;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Bon de Commande fournisseur.
 *
 * Cycle : BROUILLON → VALIDE → ENVOYE → RECU_PARTIEL/RECU_TOTAL → CLOTURE.
 * À la réception (liée à ce BC), les quantités reçues sont enregistrées par ligne
 * et le statut bascule automatiquement en RECU_PARTIEL ou RECU_TOTAL.
 */
public class BonCommande {

    private Long id;
    private final String numero;
    private final String codeFournisseur;
    private StatutBonCommande statut;
    private final LocalDate dateCommande;
    private LocalDate dateLivraisonPrevue;
    // Période d'exécution de la production planifiée à partir de ce BC. Nullable :
    // en son absence, PlanificationProductionService retombe sur la semaine ouvrée
    // de dateCommande (comportement historique), cf. definirPeriodeProduction().
    private LocalDate dateDebutProduction;
    private LocalDate dateFinProduction;
    private final String matriculeCreateur;
    // Trace de la validation par le chef de production. Renseignée une fois pour toutes à valider() :
    // un BC annulé depuis le brouillon reste sans validation, un BC annulé après validation la garde.
    private LocalDateTime dateValidation;
    private String matriculeValidateur;
    private final List<LigneBonCommande> lignes;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private BonCommande(String numero, String codeFournisseur, LocalDate dateCommande,
                        LocalDate dateLivraisonPrevue, String matriculeCreateur, LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero, "Le numéro de BC est obligatoire");
        this.codeFournisseur = (codeFournisseur != null) ? codeFournisseur : "NON_DEFINI";
        this.dateCommande = Objects.requireNonNull(dateCommande);
        this.dateLivraisonPrevue = dateLivraisonPrevue;
        this.matriculeCreateur = Objects.requireNonNull(matriculeCreateur);
        this.dateCreation = dateCreation;
        this.statut = StatutBonCommande.BROUILLON;
        this.lignes = new ArrayList<>();
        this.version = 0L;
    }

    public static BonCommande creer(String numero, String codeFournisseur, LocalDate dateCommande,
                                    LocalDate dateLivraisonPrevue, String matriculeCreateur) {
        return new BonCommande(numero, codeFournisseur, dateCommande, dateLivraisonPrevue,
            matriculeCreateur, LocalDateTime.now());
    }

    /** Variante avec période d'exécution de la production explicite (cf. {@link #definirPeriodeProduction}). */
    public static BonCommande creer(String numero, String codeFournisseur, LocalDate dateCommande,
                                    LocalDate dateLivraisonPrevue, String matriculeCreateur,
                                    LocalDate dateDebutProduction, LocalDate dateFinProduction) {
        BonCommande bc = new BonCommande(numero, codeFournisseur, dateCommande, dateLivraisonPrevue,
            matriculeCreateur, LocalDateTime.now());
        bc.definirPeriodeProduction(dateDebutProduction, dateFinProduction);
        return bc;
    }

    public static BonCommande reconstituer(Long id, String numero, String codeFournisseur, StatutBonCommande statut,
                                           LocalDate dateCommande, LocalDate dateLivraisonPrevue,
                                           String matriculeCreateur, List<LigneBonCommande> lignes,
                                           LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        return reconstituer(id, numero, codeFournisseur, statut, dateCommande, dateLivraisonPrevue,
            null, null, matriculeCreateur, lignes, dateCreation, dateModification, version);
    }

    /** Variante avec période d'exécution de la production (cf. {@link #definirPeriodeProduction}). */
    public static BonCommande reconstituer(Long id, String numero, String codeFournisseur, StatutBonCommande statut,
                                           LocalDate dateCommande, LocalDate dateLivraisonPrevue,
                                           LocalDate dateDebutProduction, LocalDate dateFinProduction,
                                           String matriculeCreateur, List<LigneBonCommande> lignes,
                                           LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        BonCommande bc = new BonCommande(numero, codeFournisseur, dateCommande, dateLivraisonPrevue,
            matriculeCreateur, dateCreation);
        bc.id = id;
        bc.statut = statut;
        bc.dateDebutProduction = dateDebutProduction;
        bc.dateFinProduction = dateFinProduction;
        if (lignes != null) bc.lignes.addAll(lignes);
        bc.dateModification = dateModification;
        bc.version = version;
        return bc;
    }

    /**
     * Modifie la période de production d'un BC déjà créé — utilisé pour
     * corriger la période après coup et déclencher un recalcul du PPH associé
     * (RecalculerPPHUseCase, appelé séparément par l'appelant).
     *
     * Autorisé tant que le BC n'est pas engagé au-delà de la validation
     * (ENVOYE, RECU_PARTIEL, RECU_TOTAL, CLOTURE, ANNULE) : au-delà, la
     * commande matière est déjà partie, changer la période de production
     * n'a plus de sens métier.
     */
    public void modifierPeriodeProduction(LocalDate debut, LocalDate fin) {
        if (statut != StatutBonCommande.BROUILLON && statut != StatutBonCommande.VALIDE) {
            throw new RegleMetierException(
                "La période de production ne peut plus être modifiée : le BC " + numero + " est " + statut.getLibelle());
        }
        definirPeriodeProduction(debut, fin);
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Définit la période d'exécution de la production planifiée à partir de ce
     * BC. Les deux dates sont facultatives mais solidaires : soit aucune des
     * deux (repli sur la semaine ouvrée de dateCommande dans
     * PlanificationProductionService), soit les deux, avec fin ≥ début.
     */
    public void definirPeriodeProduction(LocalDate debut, LocalDate fin) {
        if ((debut == null) != (fin == null)) {
            throw new IllegalArgumentException(
                "La période de production doit avoir une date de début ET une date de fin, ou aucune des deux");
        }
        if (debut != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException(
                "La date de fin de la période de production (" + fin + ") doit être postérieure ou égale à la date de début (" + debut + ")");
        }
        this.dateDebutProduction = debut;
        this.dateFinProduction = fin;
    }

    public LigneBonCommande ajouterLigne(String codeProduit, String designation, String sku,
                                         long quantiteCommandee, BigDecimal prixUnitaireHT) {
        if (!statut.estModifiable()) {
            throw new RegleMetierException("Un BC " + statut.getLibelle() + " ne peut plus être modifié");
        }
        LigneBonCommande ligne = LigneBonCommande.creer(codeProduit, designation, sku, quantiteCommandee, prixUnitaireHT);
        lignes.add(ligne);
        this.dateModification = LocalDateTime.now();
        return ligne;
    }

    /** Valide le BC et enregistre qui l'a validé et quand (trace conservée même après annulation ou clôture). */
    public void valider(String matriculeValidateur) {
        if (matriculeValidateur == null || matriculeValidateur.isBlank()) {
            throw new IllegalArgumentException("Le validateur du bon de commande est obligatoire");
        }
        exigerNonVide();
        transitionner(StatutBonCommande.VALIDE);
        this.dateValidation = LocalDateTime.now();
        this.matriculeValidateur = matriculeValidateur;
        domainEvents.add(new BonCommandeValideEvent(numero, codeFournisseur, dateLivraisonPrevue));
    }

    /** Restaure la trace de validation lue en base (sans transition ni événement). */
    public void restaurerValidation(LocalDateTime dateValidation, String matriculeValidateur) {
        this.dateValidation = dateValidation;
        this.matriculeValidateur = matriculeValidateur;
    }

    /** Vrai si le BC a été validé à un moment, quel que soit son statut actuel. */
    public boolean estValide() {
        return dateValidation != null;
    }

    public void envoyer() {
        transitionner(StatutBonCommande.ENVOYE);
    }

    public void annuler() {
        transitionner(StatutBonCommande.ANNULE);
    }

    public void cloturer() {
        transitionner(StatutBonCommande.CLOTURE);
    }

    /**
     * Enregistre une réception sur une ligne et met à jour le statut global du BC.
     * Appelé par le use case de réception (lien BR ↔ BC).
     */
    public void enregistrerReceptionLigne(String idLigne, long quantiteRecue) {
        if (statut != StatutBonCommande.ENVOYE && statut != StatutBonCommande.RECU_PARTIEL) {
            throw new RegleMetierException("Réception impossible : le BC doit être ENVOYE ou RECU_PARTIEL (actuel : " + statut.getLibelle() + ")");
        }
        LigneBonCommande ligne = lignes.stream().filter(l -> l.getId().equals(idLigne)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Ligne introuvable : " + idLigne));
        ligne.enregistrerReception(quantiteRecue);
        boolean toutRecu = lignes.stream().allMatch(LigneBonCommande::estCompletementRecue);
        this.statut = toutRecu ? StatutBonCommande.RECU_TOTAL : StatutBonCommande.RECU_PARTIEL;
        this.dateModification = LocalDateTime.now();
    }

    public boolean estEnRetard(LocalDate aujourdhui) {
        return dateLivraisonPrevue != null
            && aujourdhui.isAfter(dateLivraisonPrevue)
            && (statut == StatutBonCommande.ENVOYE || statut == StatutBonCommande.RECU_PARTIEL);
    }

    public BigDecimal montantTotalHT() {
        return lignes.stream().map(LigneBonCommande::montantLigneHT).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void enregistrerProduction(String codeProduit, long quantite) {
        LigneBonCommande ligne = lignes.stream()
            .filter(l -> l.getCodeProduit().equalsIgnoreCase(codeProduit))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Produit " + codeProduit + " non trouvé dans le BC"));
        ligne.enregistrerProduction(quantite);
        this.dateModification = LocalDateTime.now();
    }

    public int tauxRealisation() {
        if (lignes.isEmpty()) return 0;
        return (int) lignes.stream().mapToInt(LigneBonCommande::tauxRealisation).average().orElse(0);
    }

    private void transitionner(StatutBonCommande cible) {
        if (!statut.peutTransitionnerVers(cible)) {
            throw new RegleMetierException("Transition impossible : " + statut.getLibelle() + " → " + cible.getLibelle());
        }
        this.statut = cible;
        this.dateModification = LocalDateTime.now();
    }

    private void exigerNonVide() {
        if (lignes.isEmpty()) throw new RegleMetierException("Un BC doit contenir au moins une ligne avant validation");
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public String getCodeFournisseur() { return codeFournisseur; }
    public StatutBonCommande getStatut() { return statut; }
    public LocalDate getDateCommande() { return dateCommande; }
    public LocalDate getDateLivraisonPrevue() { return dateLivraisonPrevue; }
    public LocalDate getDateDebutProduction() { return dateDebutProduction; }
    public LocalDate getDateFinProduction() { return dateFinProduction; }
    public String getMatriculeCreateur() { return matriculeCreateur; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public String getMatriculeValidateur() { return matriculeValidateur; }
    public List<LigneBonCommande> getLignes() { return Collections.unmodifiableList(lignes); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BonCommande that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
