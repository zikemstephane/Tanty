package com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.TypeVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.events.AlerteRougeVersementEvent;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.events.VersementValideEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Versement.
 *
 * Reçu par la secrétaire de la part des commerciaux.
 * Alerte rouge si écart > seuil autorisé (montantAttendu - cashVerse > tolérance).
 * La validation financière est gérée ici ; le module Stock ne voit que le statut physique.
 */
public class Versement {

    // Le seuil d'alerte rouge n'est plus une constante : il provient du
    // ParametrageCaisse en vigueur à la date du versement et est transmis à
    // enregistrer(). Conserver une valeur ici en ferait une source concurrente.

    private Long id;
    private final String referenceVersement;
    private final String matriculeCommercial;
    private final double montantAttendu;
    private final double cashVerse;
    private final double ecart;
    private boolean alerteRouge;
    private final TypeVersement typeVersement;
    private final LocalDate date;
    private StatutVersement statut;
    private String justificationEcart;
    private String matriculeValidateur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Versement(String referenceVersement, String matriculeCommercial, double montantAttendu,
                       double cashVerse, TypeVersement typeVersement, LocalDate date,
                       double seuilAlerteRouge, LocalDateTime dateCreation) {
        this.referenceVersement = Objects.requireNonNull(referenceVersement);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        if (montantAttendu < 0) throw new IllegalArgumentException("Le montant attendu ne peut pas être négatif");
        this.montantAttendu = montantAttendu;
        if (cashVerse < 0) throw new IllegalArgumentException("Le cash versé ne peut pas être négatif");
        this.cashVerse = cashVerse;
        this.ecart = cashVerse - montantAttendu;
        // Seul un manquant déclenche l'alerte : un versement excédentaire, lui, ne
        // met pas la recette en risque.
        this.alerteRouge = Math.abs(this.ecart) > seuilAlerteRouge && cashVerse < montantAttendu;
        this.typeVersement = Objects.requireNonNull(typeVersement);
        this.date = Objects.requireNonNull(date);
        this.statut = this.alerteRouge ? StatutVersement.ALERTE_ROUGE : StatutVersement.EN_ATTENTE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    /**
     * Enregistre un versement, l'alerte étant appréciée au seuil en vigueur à la
     * date concernée plutôt qu'à une valeur compilée.
     */
    public static Versement enregistrer(String referenceVersement, String matriculeCommercial,
                                         double montantAttendu, double cashVerse,
                                         TypeVersement typeVersement, LocalDate date,
                                         double seuilAlerteRouge) {
        Versement v = new Versement(referenceVersement, matriculeCommercial, montantAttendu,
            cashVerse, typeVersement, date, seuilAlerteRouge, LocalDateTime.now());

        if (v.alerteRouge) {
            v.domainEvents.add(new AlerteRougeVersementEvent(
                referenceVersement, matriculeCommercial, montantAttendu, cashVerse, v.ecart
            ));
        }
        return v;
    }

    public static Versement reconstituer(Long id, String referenceVersement, String matriculeCommercial,
                                          double montantAttendu, double cashVerse, TypeVersement typeVersement,
                                          LocalDate date, StatutVersement statut,
                                          String justificationEcart, String matriculeValidateur,
                                          LocalDateTime dateCreation, LocalDateTime dateModification,
                                          Long version) {
        // Le statut persisté est restauré juste après : le seuil n'intervient donc
        // pas ici, et un changement de paramétrage ne requalifie jamais un versement
        // déjà enregistré.
        Versement v = new Versement(referenceVersement, matriculeCommercial, montantAttendu,
            cashVerse, typeVersement, date, Double.MAX_VALUE, dateCreation);
        v.id = id;
        v.statut = statut;
        v.alerteRouge = statut == StatutVersement.ALERTE_ROUGE;
        v.justificationEcart = justificationEcart;
        v.matriculeValidateur = matriculeValidateur;
        v.dateModification = dateModification;
        v.version = version;
        return v;
    }

    public void valider(String matriculeValidateur) {
        if (statut == StatutVersement.VALIDE) {
            throw new IllegalStateException("Le versement est déjà validé");
        }
        if (alerteRouge && (justificationEcart == null || justificationEcart.isBlank())) {
            throw new IllegalStateException("Une justification est obligatoire avant de valider un versement en alerte rouge");
        }
        this.matriculeValidateur = Objects.requireNonNull(matriculeValidateur);
        this.statut = StatutVersement.VALIDE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new VersementValideEvent(referenceVersement, matriculeCommercial, cashVerse));
    }

    public void justifierEcart(String justification) {
        if (statut != StatutVersement.ALERTE_ROUGE) {
            throw new IllegalStateException("Seuls les versements en alerte rouge nécessitent une justification");
        }
        this.justificationEcart = Objects.requireNonNull(justification);
        this.statut = StatutVersement.JUSTIFIE;
        this.dateModification = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getReferenceVersement() { return referenceVersement; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public double getMontantAttendu() { return montantAttendu; }
    public double getCashVerse() { return cashVerse; }
    public double getEcart() { return ecart; }
    public boolean isAlerteRouge() { return alerteRouge; }
    public TypeVersement getTypeVersement() { return typeVersement; }
    public LocalDate getDate() { return date; }
    public StatutVersement getStatut() { return statut; }
    public String getJustificationEcart() { return justificationEcart; }
    public String getMatriculeValidateur() { return matriculeValidateur; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Versement)) return false;
        return Objects.equals(referenceVersement, ((Versement) o).referenceVersement);
    }
    @Override public int hashCode() { return Objects.hash(referenceVersement); }
}
