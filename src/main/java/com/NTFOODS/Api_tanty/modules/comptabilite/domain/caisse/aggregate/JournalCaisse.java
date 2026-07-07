package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.entity.LigneCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.enums.TypeMouvementCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.events.SeuilSecurisationAtteintEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Journal de Caisse (petite caisse journalière).
 *
 * Un journal est créé par jour. Quand le solde dépasse le seuil de sécurisation
 * (500 000 FCFA par défaut, configurable), un événement est publié pour déclencher
 * la procédure de versement à la banque.
 */
public class JournalCaisse {

    private static final double SEUIL_SECURISATION_DEFAUT = 500_000.0;

    private Long id;
    private final LocalDate date;
    private double soldeCourant;
    private final double seuilSecurisation;
    private final List<LigneCaisse> lignes;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private JournalCaisse(LocalDate date, double soldeInitial, double seuilSecurisation,
                           LocalDateTime dateCreation) {
        this.date = Objects.requireNonNull(date);
        this.soldeCourant = soldeInitial;
        this.seuilSecurisation = seuilSecurisation > 0 ? seuilSecurisation : SEUIL_SECURISATION_DEFAUT;
        this.lignes = new ArrayList<>();
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static JournalCaisse ouvrir(LocalDate date, double soldeInitial) {
        return new JournalCaisse(date, soldeInitial, SEUIL_SECURISATION_DEFAUT, LocalDateTime.now());
    }

    public static JournalCaisse ouvrirAvecSeuil(LocalDate date, double soldeInitial, double seuilSecurisation) {
        return new JournalCaisse(date, soldeInitial, seuilSecurisation, LocalDateTime.now());
    }

    public static JournalCaisse reconstituer(Long id, LocalDate date, double soldeCourant,
                                              double seuilSecurisation, List<LigneCaisse> lignes,
                                              LocalDateTime dateCreation, LocalDateTime dateModification,
                                              Long version) {
        JournalCaisse j = new JournalCaisse(date, soldeCourant, seuilSecurisation, dateCreation);
        j.id = id;
        j.lignes.addAll(lignes != null ? lignes : List.of());
        j.dateModification = dateModification;
        j.version = version;
        return j;
    }

    public void enregistrerEntree(String referenceOperation, double montant, String motif,
                                   String matriculeOperateur) {
        LigneCaisse ligne = new LigneCaisse(referenceOperation, TypeMouvementCaisse.ENTREE,
            montant, motif, LocalDateTime.now(), matriculeOperateur);
        lignes.add(ligne);
        soldeCourant += montant;
        dateModification = LocalDateTime.now();

        if (soldeCourant >= seuilSecurisation) {
            domainEvents.add(new SeuilSecurisationAtteintEvent(date, soldeCourant, seuilSecurisation));
        }
    }

    public void enregistrerSortie(String referenceOperation, double montant, String motif,
                                   String matriculeOperateur) {
        if (montant > soldeCourant) {
            throw new IllegalStateException(
                String.format("Solde insuffisant : %.0f FCFA disponible, %.0f FCFA demandé", soldeCourant, montant));
        }
        LigneCaisse ligne = new LigneCaisse(referenceOperation, TypeMouvementCaisse.SORTIE,
            montant, motif, LocalDateTime.now(), matriculeOperateur);
        lignes.add(ligne);
        soldeCourant -= montant;
        dateModification = LocalDateTime.now();
    }

    public double getTotalEntrees() {
        return lignes.stream()
            .filter(l -> l.getTypeMouvement() == TypeMouvementCaisse.ENTREE)
            .mapToDouble(LigneCaisse::getMontant).sum();
    }

    public double getTotalSorties() {
        return lignes.stream()
            .filter(l -> l.getTypeMouvement() == TypeMouvementCaisse.SORTIE)
            .mapToDouble(LigneCaisse::getMontant).sum();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public double getSoldeCourant() { return soldeCourant; }
    public double getSeuilSecurisation() { return seuilSecurisation; }
    public List<LigneCaisse> getLignes() { return Collections.unmodifiableList(lignes); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalCaisse)) return false;
        return Objects.equals(date, ((JournalCaisse) o).date);
    }
    @Override public int hashCode() { return Objects.hash(date); }
}
