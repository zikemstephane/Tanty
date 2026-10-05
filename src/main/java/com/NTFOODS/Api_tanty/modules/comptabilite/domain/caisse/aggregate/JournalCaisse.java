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

    // Aucun seuil par défaut ici : il provient du ParametrageCaisse en vigueur,
    // seule source de vérité. Une valeur de repli à cet endroit serait une source
    // concurrente, susceptible de diverger silencieusement du paramétrage.

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
        if (seuilSecurisation <= 0) {
            throw new IllegalArgumentException("Le seuil de sécurisation doit être strictement positif");
        }
        this.seuilSecurisation = seuilSecurisation;
        this.lignes = new ArrayList<>();
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    /**
     * Ouvre un journal en figeant le seuil de sécurisation du jour.
     *
     * <p>Le seuil est transmis par l'appelant, qui le tient du
     * {@code ParametrageCaisse} en vigueur : le journal conserve ainsi la règle
     * qui lui a été appliquée, et un paramétrage ultérieur ne redéfinit pas
     * rétroactivement le moment où la caisse aurait dû être sécurisée.</p>
     */
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
