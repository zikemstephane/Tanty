package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.event.ConditionnementAjouteEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.event.ProduitCreeEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Produit du catalogue NTFOODS TANTY.
 *
 * Décrit un produit avec une précision maximale sur son conditionnement :
 * marque / gamme / variété / désignation, et plusieurs niveaux de
 * conditionnement (sachet, boîte, carton, palette) gérés simultanément.
 *
 * INVARIANTS encapsulés :
 * - Un produit possède au plus un conditionnement par niveau.
 * - Tout produit destiné au stock doit posséder une UNITE_BASE.
 * - Les SKU et codes-barres sont uniques au sein du produit.
 */
public class Produit {

    private Long id;
    private final CodeProduit code;
    private String marque;
    private String gamme;
    private String variete;
    private String designation;
    private CategorieProduit categorie;
    private CategorieMetier categorieMetier;
    private boolean actif;

    // Tarification commerciale (par segment client, en FCFA/carton)
    private Double prixDistributeur;
    private Double prixGrossiste;
    private Double prixDetaillant;
    private boolean tva;
    private String conditionnementCommercial;
    private String uniteVenteCommerciale;

    private final List<Conditionnement> conditionnements;

    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Produit(CodeProduit code, String marque, String gamme, String variete,
                    String designation, CategorieProduit categorie, CategorieMetier categorieMetier,
                    LocalDateTime dateCreation) {
        this.code = Objects.requireNonNull(code, "Le code produit est obligatoire");
        this.marque = exigerNonVide(marque, "marque");
        this.gamme = exigerNonVide(gamme, "gamme");
        this.variete = exigerNonVide(variete, "variété");
        this.designation = exigerNonVide(designation, "désignation");
        this.categorie = Objects.requireNonNull(categorie, "La catégorie est obligatoire");
        this.categorieMetier = categorieMetier != null ? categorieMetier : CategorieMetier.AUTRE;
        this.dateCreation = dateCreation;
        this.actif = true;
        this.conditionnements = new ArrayList<>();
        this.version = 0L;
    }

    // ── Factory ──────────────────────────────────────────────────────────────

    public static Produit creer(CodeProduit code, String marque, String gamme, String variete,
                                String designation, CategorieProduit categorie) {
        return creer(code, marque, gamme, variete, designation, categorie, CategorieMetier.AUTRE);
    }

    public static Produit creer(CodeProduit code, String marque, String gamme, String variete,
                                String designation, CategorieProduit categorie, CategorieMetier categorieMetier) {
        Produit produit = new Produit(code, marque, gamme, variete, designation, categorie, categorieMetier,
            LocalDateTime.now());
        produit.domainEvents.add(new ProduitCreeEvent(
            code.getValeur(), marque, gamme, variete, designation, categorie
        ));
        return produit;
    }

    public static Produit reconstituer(Long id, CodeProduit code, String marque, String gamme,
                                       String variete, String designation, CategorieProduit categorie,
                                       boolean actif, List<Conditionnement> conditionnements,
                                       LocalDateTime dateCreation, LocalDateTime dateModification,
                                       Long version,
                                       Double prixDistributeur, Double prixGrossiste, Double prixDetaillant,
                                       boolean tva, String conditionnementCommercial, String uniteVenteCommerciale) {
        return reconstituer(id, code, marque, gamme, variete, designation, categorie, CategorieMetier.AUTRE,
            actif, conditionnements, dateCreation, dateModification, version,
            prixDistributeur, prixGrossiste, prixDetaillant, tva, conditionnementCommercial, uniteVenteCommerciale);
    }

    public static Produit reconstituer(Long id, CodeProduit code, String marque, String gamme,
                                       String variete, String designation, CategorieProduit categorie,
                                       CategorieMetier categorieMetier,
                                       boolean actif, List<Conditionnement> conditionnements,
                                       LocalDateTime dateCreation, LocalDateTime dateModification,
                                       Long version,
                                       Double prixDistributeur, Double prixGrossiste, Double prixDetaillant,
                                       boolean tva, String conditionnementCommercial, String uniteVenteCommerciale) {
        Produit produit = new Produit(code, marque, gamme, variete, designation, categorie, categorieMetier,
            dateCreation);
        produit.id = id;
        produit.actif = actif;
        if (conditionnements != null) produit.conditionnements.addAll(conditionnements);
        produit.dateModification = dateModification;
        produit.version = version;
        produit.prixDistributeur = prixDistributeur;
        produit.prixGrossiste = prixGrossiste;
        produit.prixDetaillant = prixDetaillant;
        produit.tva = tva;
        produit.conditionnementCommercial = conditionnementCommercial;
        produit.uniteVenteCommerciale = uniteVenteCommerciale;
        return produit;
    }

    public void definirPrixCommerciaux(Double prixDistributeur, Double prixGrossiste, Double prixDetaillant,
                                        boolean tva, String conditionnementCommercial, String uniteVenteCommerciale) {
        this.prixDistributeur = prixDistributeur;
        this.prixGrossiste = prixGrossiste;
        this.prixDetaillant = prixDetaillant;
        this.tva = tva;
        this.conditionnementCommercial = conditionnementCommercial;
        this.uniteVenteCommerciale = uniteVenteCommerciale;
        this.dateModification = LocalDateTime.now();
    }

    // ── Comportements métier ──────────────────────────────────────────────────

    public Conditionnement ajouterConditionnement(String sku, NiveauConditionnement niveau, String libelle,
                                                  long facteurConversionVersBase, double poidsUnitaireGrammes,
                                                  double volumeUnitaireCm3, BigDecimal prixUnitaireHT,
                                                  String codeBarres) {
        verifierActif();
        if (existeNiveau(niveau)) {
            throw new IllegalStateException("Le produit possède déjà un conditionnement de niveau " + niveau.getLibelle());
        }
        if (sku != null && existeSku(sku)) {
            throw new IllegalArgumentException("Le SKU '" + sku + "' est déjà utilisé dans ce produit");
        }
        if (codeBarres != null && !codeBarres.isBlank() && existeCodeBarres(codeBarres)) {
            throw new IllegalArgumentException("Le code-barres '" + codeBarres + "' est déjà utilisé dans ce produit");
        }

        Conditionnement conditionnement = Conditionnement.creer(
            code.getValeur(), sku, niveau, libelle, facteurConversionVersBase,
            poidsUnitaireGrammes, volumeUnitaireCm3, prixUnitaireHT, codeBarres
        );
        conditionnements.add(conditionnement);
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new ConditionnementAjouteEvent(code.getValeur(), conditionnement.getSku(), niveau));
        return conditionnement;
    }

    public void modifierInfos(String marque, String gamme, String variete, String designation) {
        verifierActif();
        if (marque != null) this.marque = exigerNonVide(marque, "marque");
        if (gamme != null) this.gamme = exigerNonVide(gamme, "gamme");
        if (variete != null) this.variete = exigerNonVide(variete, "variété");
        if (designation != null) this.designation = exigerNonVide(designation, "désignation");
        this.dateModification = LocalDateTime.now();
    }

    public void definirCategorieMetier(CategorieMetier categorieMetier) {
        verifierActif();
        this.categorieMetier = categorieMetier != null ? categorieMetier : CategorieMetier.AUTRE;
        this.dateModification = LocalDateTime.now();
    }

    public void desactiver() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    public void reactiver() {
        this.actif = true;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Convertit une quantité d'un niveau source vers un niveau cible en
     * passant par l'unité de base (pivot).
     */
    public long convertir(long quantite, NiveauConditionnement source, NiveauConditionnement cible) {
        Conditionnement condSource = trouverParNiveau(source);
        Conditionnement condCible = trouverParNiveau(cible);
        long enBase = condSource.versUnitesBase(quantite);
        if (enBase % condCible.getFacteurConversionVersBase() != 0) {
            throw new IllegalArgumentException(
                "Conversion non entière de " + source.getLibelle() + " vers " + cible.getLibelle()
            );
        }
        return enBase / condCible.getFacteurConversionVersBase();
    }

    public Conditionnement trouverParNiveau(NiveauConditionnement niveau) {
        return conditionnements.stream()
            .filter(c -> c.getNiveau() == niveau)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Aucun conditionnement de niveau " + niveau.getLibelle() + " pour le produit " + code.getValeur()
            ));
    }

    public Optional<Conditionnement> trouverParSku(String sku) {
        return conditionnements.stream().filter(c -> c.getSku().equalsIgnoreCase(sku)).findFirst();
    }

    // ── Règles privées ─────────────────────────────────────────────────────────

    private void verifierActif() {
        if (!actif) throw new IllegalStateException("Le produit " + code.getValeur() + " est désactivé");
    }

    private boolean existeNiveau(NiveauConditionnement niveau) {
        return conditionnements.stream().anyMatch(c -> c.getNiveau() == niveau);
    }

    private boolean existeSku(String sku) {
        return conditionnements.stream().anyMatch(c -> c.getSku().equalsIgnoreCase(sku.trim()));
    }

    private boolean existeCodeBarres(String codeBarres) {
        return conditionnements.stream()
            .anyMatch(c -> codeBarres.equals(c.getCodeBarres()));
    }

    private static String exigerNonVide(String valeur, String champ) {
        Objects.requireNonNull(valeur, "Le champ '" + champ + "' est obligatoire");
        if (valeur.isBlank()) throw new IllegalArgumentException("Le champ '" + champ + "' ne peut pas être vide");
        return valeur.trim();
    }

    // ── Domain Events ──────────────────────────────────────────────────────────

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    // ── Getters ────────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public CodeProduit getCode() { return code; }
    public String getMarque() { return marque; }
    public String getGamme() { return gamme; }
    public String getVariete() { return variete; }
    public String getDesignation() { return designation; }
    public CategorieProduit getCategorie() { return categorie; }
    public CategorieMetier getCategorieMetier() { return categorieMetier; }
    public boolean isActif() { return actif; }
    public List<Conditionnement> getConditionnements() { return Collections.unmodifiableList(conditionnements); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }
    public Double getPrixDistributeur() { return prixDistributeur; }
    public Double getPrixGrossiste() { return prixGrossiste; }
    public Double getPrixDetaillant() { return prixDetaillant; }
    public boolean isTva() { return tva; }
    public String getConditionnementCommercial() { return conditionnementCommercial; }
    public String getUniteVenteCommerciale() { return uniteVenteCommerciale; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produit produit)) return false;
        return Objects.equals(code, produit.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }
}
