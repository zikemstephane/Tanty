package com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Session Commercial.
 *
 * La journée d'un commercial commence la veille : il prend les produits à la veille
 * et le lendemain il ouvre sa session. On présente son stock mobile et autres.
 *
 * Un commercial peut avoir plusieurs dotations par journée (session). Chaque dotation
 * est liée à la session. La session se cloture dès que les autres acteurs valident la suite.
 *
 * Cycle :
 * 1. PREPARATION (la veille) — le commercial reçoit ses dotations
 * 2. OUVERTE (le lendemain) — le commercial commence sa tournée
 * 3. CLOTUREE — le commercial termine sa journée
 * 4. VALIDE_SECRETAIRE — la secrétaire valide
 * 5. VALIDE_COMPTABLE — le comptable valide
 * 6. TERMINEE — session complètement clôturée
 *
 * Numérotation : SESS-YYYYMMDD-MAT
 */
public class SessionCommercial {

    private Long id;
    private final String numeroSession;
    private final String matriculeCommercial;
    private final LocalDate datePreparation;  // la veille
    private final LocalDate dateEffet;         // le lendemain (jour de la tournée)
    private StatutSession statut;
    private final List<String> dotationsIds;   // IDs des dotations liées
    private String matriculeCloture;
    private String matriculeValidateurSecretaire;
    private String matriculeValidateurComptable;
    private LocalDateTime heureOuverture;
    private LocalDateTime heureCloture;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private SessionCommercial(String numeroSession, String matriculeCommercial,
                              LocalDate datePreparation, LocalDate dateEffet, LocalDateTime dateCreation) {
        this.numeroSession = Objects.requireNonNull(numeroSession);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.datePreparation = Objects.requireNonNull(datePreparation);
        this.dateEffet = Objects.requireNonNull(dateEffet);
        this.dateCreation = dateCreation;
        this.statut = StatutSession.PREPARATION;
        this.dotationsIds = new ArrayList<>();
        this.version = 0L;
    }

    public static SessionCommercial preparer(String numeroSession, String matriculeCommercial,
                                              LocalDate datePreparation) {
        LocalDate effet = datePreparation.plusDays(1);
        return new SessionCommercial(numeroSession, matriculeCommercial, datePreparation, effet, LocalDateTime.now());
    }

    public static SessionCommercial reconstituer(Long id, String numeroSession, String matriculeCommercial,
                                                  LocalDate datePreparation, LocalDate dateEffet,
                                                  StatutSession statut, List<String> dotationsIds,
                                                  String matriculeCloture, String matriculeValidateurSecretaire,
                                                  String matriculeValidateurComptable,
                                                  LocalDateTime heureOuverture, LocalDateTime heureCloture,
                                                  LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        SessionCommercial s = new SessionCommercial(numeroSession, matriculeCommercial, datePreparation, dateEffet, dateCreation);
        s.id = id;
        s.statut = statut;
        if (dotationsIds != null) s.dotationsIds.addAll(dotationsIds);
        s.matriculeCloture = matriculeCloture;
        s.matriculeValidateurSecretaire = matriculeValidateurSecretaire;
        s.matriculeValidateurComptable = matriculeValidateurComptable;
        s.heureOuverture = heureOuverture;
        s.heureCloture = heureCloture;
        s.dateModification = dateModification;
        s.version = version;
        return s;
    }

    public void lierDotation(String dotationId) {
        if (statut != StatutSession.PREPARATION && statut != StatutSession.OUVERTE) {
            throw new IllegalStateException("Une dotation ne peut être liée qu'à une session en préparation ou ouverte");
        }
        if (!dotationsIds.contains(dotationId)) {
            dotationsIds.add(dotationId);
            this.dateModification = LocalDateTime.now();
        }
    }

    public void ouvrir() {
        if (statut != StatutSession.PREPARATION) {
            throw new IllegalStateException("Seule une session en préparation peut être ouverte");
        }
        if (dotationsIds.isEmpty()) {
            throw new IllegalStateException("La session doit avoir au moins une dotation pour être ouverte");
        }
        this.statut = StatutSession.OUVERTE;
        this.heureOuverture = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public void cloturer(String matriculeCloture) {
        if (statut != StatutSession.OUVERTE) {
            throw new IllegalStateException("Seule une session ouverte peut être clôturée");
        }
        this.matriculeCloture = Objects.requireNonNull(matriculeCloture);
        this.heureCloture = LocalDateTime.now();
        this.statut = StatutSession.CLOTUREE;
        this.dateModification = LocalDateTime.now();
    }

    public void validerParSecretaire(String matriculeSecretaire) {
        if (statut != StatutSession.CLOTUREE) {
            throw new IllegalStateException("La session doit être clôturée par le commercial d'abord");
        }
        this.matriculeValidateurSecretaire = Objects.requireNonNull(matriculeSecretaire);
        this.statut = StatutSession.VALIDE_SECRETAIRE;
        this.dateModification = LocalDateTime.now();
    }

    public void validerParComptable(String matriculeComptable) {
        if (statut != StatutSession.VALIDE_SECRETAIRE) {
            throw new IllegalStateException("La session doit être validée par la secrétaire d'abord");
        }
        this.matriculeValidateurComptable = Objects.requireNonNull(matriculeComptable);
        this.statut = StatutSession.TERMINEE;
        this.dateModification = LocalDateTime.now();
    }

    public boolean estEnCours() {
        return statut == StatutSession.PREPARATION || statut == StatutSession.OUVERTE;
    }

    public int nombreDotations() {
        return dotationsIds.size();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumeroSession() { return numeroSession; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDatePreparation() { return datePreparation; }
    public LocalDate getDateEffet() { return dateEffet; }
    public StatutSession getStatut() { return statut; }
    public List<String> getDotationsIds() { return Collections.unmodifiableList(dotationsIds); }
    public String getMatriculeCloture() { return matriculeCloture; }
    public String getMatriculeValidateurSecretaire() { return matriculeValidateurSecretaire; }
    public String getMatriculeValidateurComptable() { return matriculeValidateurComptable; }
    public LocalDateTime getHeureOuverture() { return heureOuverture; }
    public LocalDateTime getHeureCloture() { return heureCloture; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionCommercial that)) return false;
        return Objects.equals(numeroSession, that.numeroSession);
    }

    @Override
    public int hashCode() { return Objects.hash(numeroSession); }
}
