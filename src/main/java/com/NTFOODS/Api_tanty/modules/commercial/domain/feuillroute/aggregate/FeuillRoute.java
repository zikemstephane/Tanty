package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agrégat Racine — Feuille de Route (§5.8).
 *
 * <p>La tournée journalière d'un commercial : la liste ordonnée des clients qu'il
 * a décidé de visiter. Chaque arrêt est tranché en {@code VISITE} ou
 * {@code NON_VISITE}.</p>
 *
 * <p><strong>La tournée n'a plus de cycle de vie propre.</strong> Elle en portait
 * un — planifiée, en cours, clôturée, verrouillée — qui doublait celui de la
 * session commerciale sans jamais lui être raccordé : on pouvait viser des
 * arrêts sans journée ouverte, et clore sa journée sur une tournée à moitié
 * saisie. Deux cycles à synchroniser à la main, c'est un cycle de trop. L'état de
 * la journée est désormais celui de la session, et {@code numeroSession} dit
 * laquelle.</p>
 *
 * <p>L'agrégat ne lit pas la session : c'en est une autre. Ce sont les use cases
 * qui l'interrogent avant d'appeler les méthodes ci-dessous.</p>
 *
 * <p>Numérotation : FR-YYYYMMDD-NNNNNN, par séquence PostgreSQL.</p>
 */
public class FeuillRoute {

    private Long id;
    private final String numeroFeuillRoute;
    private final String numeroSession;
    private final String matriculeCommercial;
    private final LocalDate date;
    private final List<ArretFeuillRoute> arrets;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private FeuillRoute(String numeroFeuillRoute, String numeroSession, String matriculeCommercial,
                        LocalDate date, LocalDateTime dateCreation) {
        this.numeroFeuillRoute = Objects.requireNonNull(numeroFeuillRoute);
        this.numeroSession = Objects.requireNonNull(numeroSession,
            "Une tournée se rattache toujours à une journée : le numéro de session est obligatoire");
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.date = Objects.requireNonNull(date);
        this.dateCreation = dateCreation;
        this.arrets = new ArrayList<>();
        this.version = 0L;
    }

    public static FeuillRoute planifier(String numeroFeuillRoute, String numeroSession,
                                         String matriculeCommercial, LocalDate date,
                                         List<ArretFeuillRoute> arrets) {
        if (arrets == null || arrets.isEmpty()) {
            throw new IllegalArgumentException("La feuille de route doit contenir au moins un arrêt");
        }
        FeuillRoute fr = new FeuillRoute(numeroFeuillRoute, numeroSession, matriculeCommercial, date,
            LocalDateTime.now());
        fr.arrets.addAll(arrets);
        return fr;
    }

    public static FeuillRoute reconstituer(Long id, String numeroFeuillRoute, String numeroSession,
                                            String matriculeCommercial, LocalDate date,
                                            List<ArretFeuillRoute> arrets, LocalDateTime dateCreation,
                                            LocalDateTime dateModification, Long version) {
        FeuillRoute fr = new FeuillRoute(numeroFeuillRoute, numeroSession, matriculeCommercial, date,
            dateCreation);
        fr.id = id;
        fr.arrets.addAll(arrets != null ? arrets : List.of());
        fr.dateModification = dateModification;
        fr.version = version;
        return fr;
    }

    /**
     * Inscrit un client rencontré en cours de route.
     *
     * <p>La tournée n'est pas un plan figé : un client croisé en chemin doit
     * pouvoir y entrer, sans quoi sa visite n'est comptée nulle part.</p>
     */
    public void ajouterArret(String codeClient) {
        boolean dejaPresent = arrets.stream().anyMatch(a -> a.getCodeClient().equals(codeClient));
        if (dejaPresent) {
            throw new IllegalArgumentException("Le client " + codeClient + " est déjà dans cette tournée");
        }
        int prochainOrdre = arrets.stream().mapToInt(ArretFeuillRoute::getOrdre).max().orElse(0) + 1;
        arrets.add(new ArretFeuillRoute(codeClient, prochainOrdre, LocalDateTime.now()));
        this.dateModification = LocalDateTime.now();
    }

    /** Le commercial est passé chez ce client. */
    public void marquerVisite(String codeClient, String numeroVente) {
        trouverArret(codeClient).marquerVisite(numeroVente);
        this.dateModification = LocalDateTime.now();
    }

    /** Le commercial n'a pas pu passer chez ce client. */
    public void marquerNonVisite(String codeClient, SaisieArret saisie) {
        trouverArret(codeClient).marquerNonVisite(saisie);
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Vrai si tous les arrêts ont été tranchés.
     *
     * <p>Consulté avant la déclaration de fin de tournée : une journée ne se clôt
     * pas sur des clients dont on ignore s'ils ont été vus.</p>
     */
    public boolean tousArretsTranches() {
        return arrets.stream().noneMatch(ArretFeuillRoute::estEnAttente);
    }

    /** Clients encore en attente d'un constat, pour l'énoncer au commercial. */
    public List<String> clientsEnAttente() {
        return arrets.stream()
            .filter(ArretFeuillRoute::estEnAttente)
            .map(ArretFeuillRoute::getCodeClient)
            .toList();
    }

    private ArretFeuillRoute trouverArret(String codeClient) {
        return arrets.stream()
            .filter(a -> a.getCodeClient().equals(codeClient))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Arrêt introuvable pour le client : " + codeClient));
    }

    public int nombreVisites() {
        return (int) arrets.stream().filter(a -> a.getStatut()
            == com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret.VISITE).count();
    }

    public int nombreNonVisites() {
        return (int) arrets.stream().filter(a -> a.getStatut()
            == com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret.NON_VISITE).count();
    }

    public Long getId() { return id; }
    public String getNumeroFeuillRoute() { return numeroFeuillRoute; }
    public String getNumeroSession() { return numeroSession; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDate() { return date; }
    public List<ArretFeuillRoute> getArrets() { return Collections.unmodifiableList(arrets); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(numeroFeuillRoute, ((FeuillRoute) o).numeroFeuillRoute);
    }

    @Override public int hashCode() { return Objects.hash(numeroFeuillRoute); }
}
