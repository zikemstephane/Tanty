package com.NTFOODS.Api_tanty.modules.production.domain.besoin.model;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.TypeBesoin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Expression de Besoin (signalement de rupture ou manque).
 *
 * Permet au Chef de Production de signaler une rupture ou un manque de matières
 * premières ou d'emballages, afin de déclencher un processus d'achat.
 */
public class ExpressionBesoin {

    private Long id;
    private final String numero;
    private final String referencePPH;
    private final TypeBesoin type;
    private final String codeProduit;
    private final String designationProduit;
    private final double quantiteDemandee;
    private final String unite;
    private final String matriculeDemandeur;
    private final LocalDate dateBesoin;
    private final String motif;
    private StatutBesoin statut;
    private String matriculeValidateur;
    private String motifRejet;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private ExpressionBesoin(String numero, String referencePPH, TypeBesoin type, String codeProduit,
                             String designationProduit, double quantiteDemandee, String unite,
                             String matriculeDemandeur, LocalDate dateBesoin, String motif,
                             LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.referencePPH = referencePPH;
        this.type = Objects.requireNonNull(type);
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designationProduit = Objects.requireNonNull(designationProduit);
        if (quantiteDemandee <= 0) throw new IllegalArgumentException("La quantité demandée doit être positive");
        this.quantiteDemandee = quantiteDemandee;
        this.unite = Objects.requireNonNull(unite);
        this.matriculeDemandeur = Objects.requireNonNull(matriculeDemandeur);
        this.dateBesoin = Objects.requireNonNull(dateBesoin);
        this.motif = motif;
        this.dateCreation = dateCreation;
        this.statut = StatutBesoin.EN_ATTENTE;
        this.version = 0L;
    }

    public static ExpressionBesoin signaler(String numero, String referencePPH, TypeBesoin type,
                                             String codeProduit, String designationProduit,
                                             double quantiteDemandee, String unite,
                                             String matriculeDemandeur, LocalDate dateBesoin, String motif) {
        return new ExpressionBesoin(numero, referencePPH, type, codeProduit, designationProduit,
            quantiteDemandee, unite, matriculeDemandeur, dateBesoin, motif, LocalDateTime.now());
    }

    public static ExpressionBesoin reconstituer(Long id, String numero, String referencePPH, TypeBesoin type,
                                                 String codeProduit, String designationProduit,
                                                 double quantiteDemandee, String unite,
                                                 String matriculeDemandeur, LocalDate dateBesoin,
                                                 String motif, StatutBesoin statut, String matriculeValidateur,
                                                 String motifRejet, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        ExpressionBesoin b = new ExpressionBesoin(numero, referencePPH, type, codeProduit, designationProduit,
            quantiteDemandee, unite, matriculeDemandeur, dateBesoin, motif, dateCreation);
        b.id = id;
        b.statut = statut;
        b.matriculeValidateur = matriculeValidateur;
        b.motifRejet = motifRejet;
        b.dateModification = dateModification;
        b.version = version;
        return b;
    }

    public void valider(String matriculeValidateur) {
        if (statut != StatutBesoin.EN_ATTENTE) {
            throw new IllegalStateException("Seul un besoin EN_ATTENTE peut être validé");
        }
        this.statut = StatutBesoin.VALIDE;
        this.matriculeValidateur = matriculeValidateur;
        this.dateModification = LocalDateTime.now();
    }

    public void rejeter(String matriculeValidateur, String motifRejet) {
        if (statut != StatutBesoin.EN_ATTENTE) {
            throw new IllegalStateException("Seul un besoin EN_ATTENTE peut être rejeté");
        }
        this.statut = StatutBesoin.REJETE;
        this.matriculeValidateur = matriculeValidateur;
        this.motifRejet = Objects.requireNonNull(motifRejet);
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public String getReferencePPH() { return referencePPH; }
    public TypeBesoin getType() { return type; }
    public String getCodeProduit() { return codeProduit; }
    public String getDesignationProduit() { return designationProduit; }
    public double getQuantiteDemandee() { return quantiteDemandee; }
    public String getUnite() { return unite; }
    public String getMatriculeDemandeur() { return matriculeDemandeur; }
    public LocalDate getDateBesoin() { return dateBesoin; }
    public String getMotif() { return motif; }
    public StatutBesoin getStatut() { return statut; }
    public String getMatriculeValidateur() { return matriculeValidateur; }
    public String getMotifRejet() { return motifRejet; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionBesoin that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
