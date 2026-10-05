package com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Ordre de Fabrication (OF).
 * Lié à un PPH, pour un produit donné, avec quantité demandée et date butoir.
 */
public class OrdreFabrication {

    private Long id;
    private final String idOF;
    private final String referencePPH;
    private final String codeProduit;
    private final String designationProduit;
    private final int qteDemandee;
    private int qteRealisee;
    private final LocalDate dateButoir;
    private StatutOF statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private OrdreFabrication(String idOF, String referencePPH, String codeProduit,
                              String designationProduit, int qteDemandee, LocalDate dateButoir,
                              LocalDateTime dateCreation) {
        this.idOF = Objects.requireNonNull(idOF);
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designationProduit = Objects.requireNonNull(designationProduit);
        if (qteDemandee <= 0) throw new IllegalArgumentException("La quantité demandée doit être positive");
        this.qteDemandee = qteDemandee;
        this.dateButoir = Objects.requireNonNull(dateButoir);
        this.statut = StatutOF.PLANIFIE;
        this.qteRealisee = 0;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static OrdreFabrication creer(String idOF, String referencePPH, String codeProduit,
                                          String designationProduit, int qteDemandee, LocalDate dateButoir) {
        return new OrdreFabrication(idOF, referencePPH, codeProduit, designationProduit,
            qteDemandee, dateButoir, LocalDateTime.now());
    }

    public static OrdreFabrication reconstituer(Long id, String idOF, String referencePPH,
                                                 String codeProduit, String designationProduit,
                                                 int qteDemandee, int qteRealisee, LocalDate dateButoir,
                                                 StatutOF statut, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        OrdreFabrication of = new OrdreFabrication(idOF, referencePPH, codeProduit, designationProduit,
            qteDemandee, dateButoir, dateCreation);
        of.id = id;
        of.qteRealisee = qteRealisee;
        of.statut = statut;
        of.dateModification = dateModification;
        of.version = version;
        return of;
    }

    public void demarrer() {
        if (statut != StatutOF.PLANIFIE) throw new IllegalStateException("L'OF doit être PLANIFIE pour démarrer");
        this.statut = StatutOF.EN_COURS;
        this.dateModification = LocalDateTime.now();
    }

    public void enregistrerProduction(int quantite) {
        if (statut != StatutOF.EN_COURS) throw new IllegalStateException("L'OF doit être EN_COURS pour enregistrer la production");
        this.qteRealisee += quantite;
        if (this.qteRealisee >= this.qteDemandee) {
            this.statut = StatutOF.HONORE;
        }
        this.dateModification = LocalDateTime.now();
    }

    public void annuler() {
        if (statut == StatutOF.HONORE) throw new IllegalStateException("Un OF honoré ne peut pas être annulé");
        this.statut = StatutOF.ANNULE;
        this.dateModification = LocalDateTime.now();
    }

    public int tauxAvancement() {
        if (qteDemandee == 0) return 0;
        return Math.min(100, (int) ((qteRealisee * 100.0) / qteDemandee));
    }

    public Long getId() { return id; }
    public String getIdOF() { return idOF; }
    public String getReferencePPH() { return referencePPH; }
    public String getCodeProduit() { return codeProduit; }
    public String getDesignationProduit() { return designationProduit; }
    public int getQteDemandee() { return qteDemandee; }
    public int getQteRealisee() { return qteRealisee; }
    public LocalDate getDateButoir() { return dateButoir; }
    public StatutOF getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdreFabrication)) return false;
        return Objects.equals(idOF, ((OrdreFabrication) o).idOF);
    }
    @Override public int hashCode() { return Objects.hash(idOF); }
}
