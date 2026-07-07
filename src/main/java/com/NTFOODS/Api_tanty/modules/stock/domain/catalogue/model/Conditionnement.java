package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.util.ConditionnementParser;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Entité — Conditionnement d'un produit (appartient à l'agrégat {@link Produit}).
 *
 * Chaque produit possède plusieurs conditionnements (sachet, boîte, carton, palette).
 * Le {@code facteurConversionVersBase} indique combien d'unités de base ce
 * conditionnement contient (ex : un carton de 24 boîtes de 12 sachets → 288).
 * C'est le pivot qui permet de convertir entre tous les niveaux.
 */
public class Conditionnement {

    private final String id;                       // identité locale à l'agrégat
    private final String codeProduit;              // produit parent (dénormalisé)
    private String sku;                            // code unique de ce conditionnement
    private NiveauConditionnement niveau;
    private String libelle;                        // ex : "sachet 5g", "carton 24 boîtes"
    private long facteurConversionVersBase;        // nb d'unités base contenues
    private int nombreUnites;                      // ex: 25 (dans "25 sachets x 57g")
    private String uniteConditionnement;           // ex: sachet
    private double poidsUnitaireGrammes;
    private double volumeUnitaireCm3;
    private BigDecimal prixUnitaireHT;             // prix de référence (valorisation, lecture)
    private String codeBarres;

    private Conditionnement(String id, String codeProduit, String sku, NiveauConditionnement niveau,
                            String libelle, long facteurConversionVersBase, int nombreUnites,
                            String uniteConditionnement, double poidsUnitaireGrammes,
                            double volumeUnitaireCm3, BigDecimal prixUnitaireHT, String codeBarres) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.niveau = niveau;
        this.libelle = libelle;
        this.facteurConversionVersBase = facteurConversionVersBase;
        this.nombreUnites = nombreUnites;
        this.uniteConditionnement = uniteConditionnement;
        this.poidsUnitaireGrammes = poidsUnitaireGrammes;
        this.volumeUnitaireCm3 = volumeUnitaireCm3;
        this.prixUnitaireHT = prixUnitaireHT;
        this.codeBarres = codeBarres;
    }

    public static Conditionnement creer(String codeProduit, String sku, NiveauConditionnement niveau,
                                        String libelle, long facteurConversionVersBase,
                                        double poidsUnitaireGrammes, double volumeUnitaireCm3,
                                        BigDecimal prixUnitaireHT, String codeBarres) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        Objects.requireNonNull(sku, "Le SKU est obligatoire");
        Objects.requireNonNull(niveau, "Le niveau de conditionnement est obligatoire");
        Objects.requireNonNull(libelle, "Le libellé est obligatoire");
        if (sku.isBlank()) throw new IllegalArgumentException("Le SKU ne peut pas être vide");
        if (libelle.isBlank()) throw new IllegalArgumentException("Le libellé ne peut pas être vide");
        if (niveau.estUniteBase() && facteurConversionVersBase != 1) {
            throw new IllegalArgumentException("L'unité de base doit avoir un facteur de conversion de 1");
        }
        if (facteurConversionVersBase <= 0) {
            throw new IllegalArgumentException("Le facteur de conversion doit être strictement positif");
        }
        if (poidsUnitaireGrammes < 0 || volumeUnitaireCm3 < 0) {
            throw new IllegalArgumentException("Poids et volume ne peuvent pas être négatifs");
        }

        ConditionnementParser.Resultat parse = ConditionnementParser.parse(libelle);
        int nombreUnites = parse.nombreUnites();
        String uniteConditionnement = parse.unite();
        double poidsParse = parse.poidsUnitaireGrammes();
        double poidsFinal = poidsUnitaireGrammes > 0 ? poidsUnitaireGrammes : poidsParse;

        return new Conditionnement(
            UUID.randomUUID().toString(), codeProduit, sku.trim().toUpperCase(), niveau,
            libelle.trim(), facteurConversionVersBase, nombreUnites, uniteConditionnement,
            poidsFinal, volumeUnitaireCm3,
            prixUnitaireHT != null ? prixUnitaireHT : BigDecimal.ZERO, codeBarres
        );
    }

    public static Conditionnement reconstituer(String id, String codeProduit, String sku,
                                               NiveauConditionnement niveau, String libelle,
                                               long facteurConversionVersBase, double poidsUnitaireGrammes,
                                               double volumeUnitaireCm3, BigDecimal prixUnitaireHT,
                                               String codeBarres) {
        ConditionnementParser.Resultat parse = ConditionnementParser.parse(libelle);
        int nombreUnites = parse.nombreUnites();
        String uniteConditionnement = parse.unite();
        double poidsFinal = poidsUnitaireGrammes > 0 ? poidsUnitaireGrammes : parse.poidsUnitaireGrammes();
        return new Conditionnement(id, codeProduit, sku, niveau, libelle, facteurConversionVersBase,
            nombreUnites, uniteConditionnement, poidsFinal, volumeUnitaireCm3, prixUnitaireHT, codeBarres);
    }

    /** Convertit une quantité exprimée dans ce conditionnement en unités de base. */
    public long versUnitesBase(long quantite) {
        return quantite * facteurConversionVersBase;
    }

    public void mettreAJourPrix(BigDecimal nouveauPrix) {
        if (nouveauPrix == null || nouveauPrix.signum() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        this.prixUnitaireHT = nouveauPrix;
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public NiveauConditionnement getNiveau() { return niveau; }
    public String getLibelle() { return libelle; }
    public long getFacteurConversionVersBase() { return facteurConversionVersBase; }
    public int getNombreUnites() { return nombreUnites; }
    public String getUniteConditionnement() { return uniteConditionnement; }
    public double getPoidsUnitaireGrammes() { return poidsUnitaireGrammes; }
    public double getVolumeUnitaireCm3() { return volumeUnitaireCm3; }
    public BigDecimal getPrixUnitaireHT() { return prixUnitaireHT; }
    public String getCodeBarres() { return codeBarres; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conditionnement that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
