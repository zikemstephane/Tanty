package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Arrêt sur la feuille de route — un client visité par le commercial.
 * Un arrêt peut être VISÉ (vente réalisée) ou NON_VISÉ (code InfoClient fourni).
 */
public class ArretFeuillRoute {

    private Long id;
    private final String codeClient;
    private final int ordre;
    private StatutArret statut;
    private String numeroVente;
    private SaisieArret saisie;
    private final LocalDateTime heurePlanifiee;
    private LocalDateTime heureEffective;
    private String coordonneesGPS;

    public ArretFeuillRoute(String codeClient, int ordre, LocalDateTime heurePlanifiee) {
        this.codeClient = Objects.requireNonNull(codeClient);
        if (ordre < 1) throw new IllegalArgumentException("L'ordre de l'arrêt doit être >= 1");
        this.ordre = ordre;
        this.heurePlanifiee = heurePlanifiee;
        this.statut = StatutArret.PLANIFIE;
    }

    public static ArretFeuillRoute reconstituer(Long id, String codeClient, int ordre, StatutArret statut,
                                                  String numeroVente, SaisieArret saisie,
                                                  LocalDateTime heurePlanifiee, LocalDateTime heureEffective,
                                                  String coordonneesGPS) {
        ArretFeuillRoute a = new ArretFeuillRoute(codeClient, ordre, heurePlanifiee);
        a.id = id;
        a.statut = statut;
        a.numeroVente = numeroVente;
        a.saisie = saisie;
        a.heureEffective = heureEffective;
        a.coordonneesGPS = coordonneesGPS;
        return a;
    }

    public void viser(String numeroVente, String coordonneesGPS) {
        if (statut != StatutArret.PLANIFIE && statut != StatutArret.NON_VISE) {
            throw new IllegalStateException("Un arrêt " + statut + " ne peut pas être visé");
        }
        this.numeroVente = Objects.requireNonNull(numeroVente);
        this.statut = StatutArret.VISE;
        this.heureEffective = LocalDateTime.now();
        this.coordonneesGPS = coordonneesGPS;
    }

    public void marquerNonVise(SaisieArret saisie, String coordonneesGPS) {
        if (statut != StatutArret.PLANIFIE) {
            throw new IllegalStateException("Un arrêt " + statut + " ne peut pas être marqué non-visé");
        }
        this.saisie = Objects.requireNonNull(saisie);
        this.statut = StatutArret.NON_VISE;
        this.heureEffective = LocalDateTime.now();
        this.coordonneesGPS = coordonneesGPS;
    }

    public void annuler(String motif) {
        if (statut == StatutArret.VISE) {
            throw new IllegalStateException("Un arrêt visé (vente réalisée) ne peut pas être annulé");
        }
        this.statut = StatutArret.ANNULE;
    }

    public Long getId() { return id; }
    public String getCodeClient() { return codeClient; }
    public int getOrdre() { return ordre; }
    public StatutArret getStatut() { return statut; }
    public String getNumeroVente() { return numeroVente; }
    public SaisieArret getSaisie() { return saisie; }
    public LocalDateTime getHeurePlanifiee() { return heurePlanifiee; }
    public LocalDateTime getHeureEffective() { return heureEffective; }
    public String getCoordonneesGPS() { return coordonneesGPS; }
}
