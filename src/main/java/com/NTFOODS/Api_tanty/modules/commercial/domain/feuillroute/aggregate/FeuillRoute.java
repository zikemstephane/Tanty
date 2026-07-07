package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutFeuillRoute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Feuille de Route (§5.8).
 *
 * Représente la tournée journalière d'un commercial : liste ordonnée
 * d'arrêts (clients à visiter). Chaque arrêt est soit VISÉ (vente réalisée),
 * soit NON_VISÉ (code InfoClient fourni — §5.9).
 *
 * Numérotation : FR-YYYYMMDD-MAT
 * Verrouillage : après clôture, aucun arrêt ne peut plus être modifié.
 */
public class FeuillRoute {

    private Long id;
    private final String numeroFeuillRoute;
    private final String matriculeCommercial;
    private final LocalDate date;
    private final List<ArretFeuillRoute> arrets;
    private StatutFeuillRoute statut;
    private String matriculeCloture;
    private LocalDateTime heureCloture;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private FeuillRoute(String numeroFeuillRoute, String matriculeCommercial, LocalDate date, LocalDateTime dateCreation) {
        this.numeroFeuillRoute = Objects.requireNonNull(numeroFeuillRoute);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.date = Objects.requireNonNull(date);
        this.dateCreation = dateCreation;
        this.arrets = new ArrayList<>();
        this.statut = StatutFeuillRoute.PLANIFIE;
        this.version = 0L;
    }

    public static FeuillRoute planifier(String numeroFeuillRoute, String matriculeCommercial,
                                         LocalDate date, List<ArretFeuillRoute> arrets) {
        if (arrets == null || arrets.isEmpty()) {
            throw new IllegalArgumentException("La feuille de route doit contenir au moins un arrêt");
        }
        FeuillRoute fr = new FeuillRoute(numeroFeuillRoute, matriculeCommercial, date, LocalDateTime.now());
        fr.arrets.addAll(arrets);
        return fr;
    }

    public static FeuillRoute reconstituer(Long id, String numeroFeuillRoute, String matriculeCommercial,
                                            LocalDate date, List<ArretFeuillRoute> arrets, StatutFeuillRoute statut,
                                            String matriculeCloture, LocalDateTime heureCloture,
                                            LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        FeuillRoute fr = new FeuillRoute(numeroFeuillRoute, matriculeCommercial, date, dateCreation);
        fr.id = id;
        fr.arrets.addAll(arrets != null ? arrets : List.of());
        fr.statut = statut;
        fr.matriculeCloture = matriculeCloture;
        fr.heureCloture = heureCloture;
        fr.dateModification = dateModification;
        fr.version = version;
        return fr;
    }

    public void demarrer() {
        if (statut != StatutFeuillRoute.PLANIFIE) {
            throw new IllegalStateException("Seule une feuille de route PLANIFIE peut être démarrée");
        }
        this.statut = StatutFeuillRoute.EN_COURS;
        this.dateModification = LocalDateTime.now();
    }

    public void viserArret(String codeClient, String numeroVente, String coordonneesGPS) {
        verifierModifiable();
        ArretFeuillRoute arret = trouverArret(codeClient);
        arret.viser(numeroVente, coordonneesGPS);
        this.dateModification = LocalDateTime.now();
    }

    public void marquerArretNonVise(String codeClient, SaisieArret saisie, String coordonneesGPS) {
        verifierModifiable();
        ArretFeuillRoute arret = trouverArret(codeClient);
        arret.marquerNonVise(saisie, coordonneesGPS);
        this.dateModification = LocalDateTime.now();
    }

    public void cloturer(String matriculeCloture) {
        if (statut != StatutFeuillRoute.EN_COURS) {
            throw new IllegalStateException("Seule une feuille de route EN_COURS peut être clôturée");
        }
        boolean tousTraites = arrets.stream().allMatch(a ->
            a.getStatut() != com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret.PLANIFIE);
        if (!tousTraites) {
            throw new IllegalStateException("Tous les arrêts doivent être visés ou non-visés avant clôture");
        }
        this.matriculeCloture = Objects.requireNonNull(matriculeCloture);
        this.heureCloture = LocalDateTime.now();
        this.statut = StatutFeuillRoute.CLOTURE;
        this.dateModification = LocalDateTime.now();
    }

    public void verrouiller() {
        if (statut != StatutFeuillRoute.CLOTURE) {
            throw new IllegalStateException("Seule une feuille de route CLOTURE peut être verrouillée");
        }
        this.statut = StatutFeuillRoute.VERROUILLE;
        this.dateModification = LocalDateTime.now();
    }

    private void verifierModifiable() {
    if (statut != StatutFeuillRoute.EN_COURS) {
        throw new IllegalStateException(
            "Feuille de route " + statut + " — modification impossible (doit être EN_COURS)");
    }
}

    private ArretFeuillRoute trouverArret(String codeClient) {
        return arrets.stream()
            .filter(a -> a.getCodeClient().equals(codeClient))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Arrêt introuvable pour le client : " + codeClient));
    }

    public int nombreArretVises() {
        return (int) arrets.stream().filter(a ->
            a.getStatut() == com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret.VISE).count();
    }

    public int nombreArretNonVises() {
        return (int) arrets.stream().filter(a ->
            a.getStatut() == com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret.NON_VISE).count();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumeroFeuillRoute() { return numeroFeuillRoute; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDate() { return date; }
    public List<ArretFeuillRoute> getArrets() { return Collections.unmodifiableList(arrets); }
    public StatutFeuillRoute getStatut() { return statut; }
    public String getMatriculeCloture() { return matriculeCloture; }
    public LocalDateTime getHeureCloture() { return heureCloture; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeuillRoute)) return false;
        return Objects.equals(numeroFeuillRoute, ((FeuillRoute) o).numeroFeuillRoute);
    }
    @Override public int hashCode() { return Objects.hash(numeroFeuillRoute); }
}
