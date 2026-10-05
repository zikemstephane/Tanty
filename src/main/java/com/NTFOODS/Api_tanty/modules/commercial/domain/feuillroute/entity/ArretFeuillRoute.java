package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Arrêt sur la feuille de route — un client inscrit à la tournée du jour.
 *
 * <p>Le commercial tranche entre deux constats : il est passé, ou il n'a pas pu.
 * Le second porte un code InfoClient qui en donne la raison (§5.9).</p>
 *
 * <p>La position GPS relevée à la visite et l'écart au client ont été retirés :
 * ils imposaient au commercial d'attendre un point satellite avant chaque
 * marquage, pour une traçabilité que personne n'exploitait. Le numéro de vente
 * n'est plus saisi non plus — il est rattaché par le use case s'il existe une
 * vente chez ce client dans la journée, et reste nul sinon.</p>
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

    public ArretFeuillRoute(String codeClient, int ordre, LocalDateTime heurePlanifiee) {
        this.codeClient = Objects.requireNonNull(codeClient);
        if (ordre < 1) throw new IllegalArgumentException("L'ordre de l'arrêt doit être >= 1");
        this.ordre = ordre;
        this.heurePlanifiee = heurePlanifiee;
        this.statut = StatutArret.PLANIFIE;
    }

    public static ArretFeuillRoute reconstituer(Long id, String codeClient, int ordre, StatutArret statut,
                                                  String numeroVente, SaisieArret saisie,
                                                  LocalDateTime heurePlanifiee, LocalDateTime heureEffective) {
        ArretFeuillRoute a = new ArretFeuillRoute(codeClient, ordre, heurePlanifiee);
        a.id = id;
        a.statut = statut;
        a.numeroVente = numeroVente;
        a.saisie = saisie;
        a.heureEffective = heureEffective;
        return a;
    }

    /**
     * Le commercial est passé chez le client.
     *
     * <p>Le numéro de vente est facultatif : un passage qui n'aboutit à aucune
     * commande reste un passage. Il vaut {@code null} tant qu'aucune vente n'a
     * été trouvée pour ce client ce jour-là.</p>
     */
    public void marquerVisite(String numeroVente) {
        this.numeroVente = numeroVente;
        this.saisie = null;
        this.statut = StatutArret.VISITE;
        this.heureEffective = LocalDateTime.now();
    }

    /**
     * Le commercial n'a pas pu passer ; le motif est obligatoire.
     *
     * <p>{@link SaisieArret} refuse d'elle-même un code exigeant une quantité qui
     * n'en porte pas (§5.9).</p>
     */
    public void marquerNonVisite(SaisieArret saisie) {
        this.saisie = Objects.requireNonNull(saisie, "Le motif est obligatoire pour un client non visité");
        this.numeroVente = null;
        this.statut = StatutArret.NON_VISITE;
        this.heureEffective = LocalDateTime.now();
    }

    /** Vrai tant que le commercial n'a pas tranché sur cet arrêt. */
    public boolean estEnAttente() {
        return statut == StatutArret.PLANIFIE;
    }

    public Long getId() { return id; }
    public String getCodeClient() { return codeClient; }
    public int getOrdre() { return ordre; }
    public StatutArret getStatut() { return statut; }
    public String getNumeroVente() { return numeroVente; }
    public SaisieArret getSaisie() { return saisie; }
    public LocalDateTime getHeurePlanifiee() { return heurePlanifiee; }
    public LocalDateTime getHeureEffective() { return heureEffective; }
}
