package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.util.ConditionnementParser;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    // Type physique du conditionnement (SACHET, SEAU, GAINE, ÉTUI...) et
    // composition d'emballage associée. Nullable / vide par défaut — un
    // conditionnement sans type ni composition est un état valide, à
    // l'instar d'un produit sans FormuleProduction : calculerComposantsEmballage
    // le refuse explicitement plutôt que de deviner une composition.
    private TypeEmballage type;
    private List<ComposantEmballage> composants;

    private Conditionnement(String id, String codeProduit, String sku, NiveauConditionnement niveau,
                            String libelle, long facteurConversionVersBase, int nombreUnites,
                            String uniteConditionnement, double poidsUnitaireGrammes,
                            double volumeUnitaireCm3, BigDecimal prixUnitaireHT, String codeBarres,
                            TypeEmballage type, List<ComposantEmballage> composants) {
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
        this.type = type;
        this.composants = composants == null ? new ArrayList<>() : new ArrayList<>(composants);
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
            prixUnitaireHT != null ? prixUnitaireHT : BigDecimal.ZERO, codeBarres,
            null, List.of()
        );
    }

    public static Conditionnement reconstituer(String id, String codeProduit, String sku,
                                               NiveauConditionnement niveau, String libelle,
                                               long facteurConversionVersBase, double poidsUnitaireGrammes,
                                               double volumeUnitaireCm3, BigDecimal prixUnitaireHT,
                                               String codeBarres) {
        return reconstituer(id, codeProduit, sku, niveau, libelle, facteurConversionVersBase,
            poidsUnitaireGrammes, volumeUnitaireCm3, prixUnitaireHT, codeBarres, null, List.of());
    }

    /** Variante complète, utilisée par la persistance pour restituer type et composition d'emballage. */
    public static Conditionnement reconstituer(String id, String codeProduit, String sku,
                                               NiveauConditionnement niveau, String libelle,
                                               long facteurConversionVersBase, double poidsUnitaireGrammes,
                                               double volumeUnitaireCm3, BigDecimal prixUnitaireHT,
                                               String codeBarres, TypeEmballage type,
                                               List<ComposantEmballage> composants) {
        ConditionnementParser.Resultat parse = ConditionnementParser.parse(libelle);
        int nombreUnites = parse.nombreUnites();
        String uniteConditionnement = parse.unite();
        double poidsFinal = poidsUnitaireGrammes > 0 ? poidsUnitaireGrammes : parse.poidsUnitaireGrammes();
        return new Conditionnement(id, codeProduit, sku, niveau, libelle, facteurConversionVersBase,
            nombreUnites, uniteConditionnement, poidsFinal, volumeUnitaireCm3, prixUnitaireHT, codeBarres,
            type, composants);
    }

    /** Convertit une quantité exprimée dans ce conditionnement en unités de base. */
    public long versUnitesBase(long quantite) {
        return quantite * facteurConversionVersBase;
    }

    /**
     * Définit (ou remplace) le type physique et la composition d'emballage
     * de ce conditionnement. Distincte du constructeur pour ne pas imposer
     * cette donnée, encore souvent inconnue, à la création d'un conditionnement.
     */
    public void definirTypeEtComposants(TypeEmballage type, List<ComposantEmballage> composants) {
        this.type = type;
        this.composants = composants == null ? new ArrayList<>() : new ArrayList<>(composants);
    }

    /**
     * Calcule, pour une quantité commandée exprimée dans ce conditionnement
     * (ex. un nombre de cartons), le besoin en chaque composant d'emballage
     * de sa composition — jamais en supposant Sachet/Couvercle/Scellé pour
     * tous les produits, uniquement les composants réellement déclarés pour
     * CE conditionnement.
     *
     * <p>La quantité commandée est d'abord convertie en unités de base via
     * {@link #versUnitesBase}, puis chaque composant est calculé par
     * arrondi au SUPÉRIEUR (jamais de demi-couvercle) : un composant partagé
     * entre plusieurs unités (ex. 1 couvercle pour 24 sachets) ne peut être
     * sous-évalué, une commande incomplète en composants serait pire qu'un
     * léger surplus.</p>
     *
     * @throws RegleMetierException si le type ou la composition sont
     *         manquants ou incomplets — jamais une liste vide ou des
     *         quantités à zéro renvoyées en silence.
     */
    public List<BesoinEmballage> calculerComposantsEmballage(long quantiteCommandee) {
        if (quantiteCommandee < 0) {
            throw new IllegalArgumentException("La quantité commandée ne peut pas être négative (conditionnement " + sku + ")");
        }
        if (type == null) {
            throw new RegleMetierException(
                "Type d'emballage manquant pour le conditionnement " + sku + " (produit " + codeProduit
                    + ") : impossible de calculer les besoins d'emballage sans lui.");
        }
        if (composants.isEmpty()) {
            throw new RegleMetierException(
                "Composition d'emballage manquante pour le conditionnement " + sku + " (produit " + codeProduit
                    + ") : impossible de calculer les besoins d'emballage sans elle.");
        }

        long unitesProduites = versUnitesBase(quantiteCommandee);
        List<BesoinEmballage> resultat = new ArrayList<>(composants.size());
        for (ComposantEmballage composant : composants) {
            long besoin = diviserVersLeHaut(
                unitesProduites * composant.getQuantiteComposant(), composant.getQuantiteUnitesProduites());
            resultat.add(new BesoinEmballage(composant.getType(), besoin));
        }
        return Collections.unmodifiableList(resultat);
    }

    private long diviserVersLeHaut(long numerateur, long denominateur) {
        return (numerateur + denominateur - 1) / denominateur;
    }

    public void mettreAJourPrix(BigDecimal nouveauPrix) {
        if (nouveauPrix == null || nouveauPrix.signum() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        this.prixUnitaireHT = nouveauPrix;
    }

    public void mettreAJour(String libelle, Long facteurConversionVersBase,
                            double poidsUnitaireGrammes, double volumeUnitaireCm3,
                            BigDecimal prixUnitaireHT, String codeBarres) {
        if (libelle != null && !libelle.isBlank()) {
            this.libelle = libelle.trim();
            ConditionnementParser.Resultat parse = ConditionnementParser.parse(libelle);
            this.nombreUnites = parse.nombreUnites();
            this.uniteConditionnement = parse.unite();
            double poidsParse = parse.poidsUnitaireGrammes();
            this.poidsUnitaireGrammes = poidsUnitaireGrammes > 0 ? poidsUnitaireGrammes : poidsParse;
        }
        if (facteurConversionVersBase != null) {
            if (niveau.estUniteBase() && facteurConversionVersBase != 1) {
                throw new IllegalArgumentException("L'unité de base doit avoir un facteur de conversion de 1");
            }
            if (facteurConversionVersBase <= 0) {
                throw new IllegalArgumentException("Le facteur de conversion doit être strictement positif");
            }
            this.facteurConversionVersBase = facteurConversionVersBase;
        }
        if (poidsUnitaireGrammes >= 0) this.poidsUnitaireGrammes = poidsUnitaireGrammes;
        if (volumeUnitaireCm3 >= 0) this.volumeUnitaireCm3 = volumeUnitaireCm3;
        if (prixUnitaireHT != null) {
            if (prixUnitaireHT.signum() < 0) throw new IllegalArgumentException("Le prix ne peut pas être négatif");
            this.prixUnitaireHT = prixUnitaireHT;
        }
        if (codeBarres != null) this.codeBarres = codeBarres;
    }

    public TypeEmballage getType() { return type; }
    public List<ComposantEmballage> getComposants() { return Collections.unmodifiableList(composants); }
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
