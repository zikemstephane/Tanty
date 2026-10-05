package com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Comparaison d'une journée entre ce que le plan (issu du bon de commande) prévoit et ce qui a été réalisé :
 * fûts dosés, matières dosées contre formules, produits du BC et production réalisée par poste.
 * Les quantités restent dans leur unité d'origine : aucune conversion n'est faite entre sachets, sceaux et kg.
 */
public record ComparaisonBCResponse(
    String referencePPH,
    String referenceBC,
    LocalDate date,
    int nbSessionsDosage,
    FutsComparaison futs,
    List<MatiereComparaison> matieres,
    List<ProduitBC> produits,
    List<PosteRealise> postesRealises,
    ProductionBouillie productionBouillie
) {

    /** Fûts nets dosés contre fûts prévus par le plan pour la journée (1 fût = 50 kg de matières). */
    public record FutsComparaison(int futsNetsDoses, double futsPrevus, double ecart, double ecartPct) {}

    /** Besoin théorique d'une matière, détaillé par catégorie métier de produit. */
    public record ParCategorie(String categorie, double theoriqueKg) {}

    /** Matière première : théorique (formules des produits du BC) contre dosée (sessions du jour). */
    public record MatiereComparaison(
        String matiere, double theoriqueKg, double doseeKg, double ecartKg, double ecartPct,
        List<ParCategorie> theoriqueParCategorie
    ) {}

    /** Produit du BC prévu pour la journée. */
    public record ProduitBC(
        String codeProduit, String nomProduit, String categorie,
        int cartonsPrevus, int unitesPrevues, double poidsPrevuKg, double matieresPrevuesKg
    ) {}

    /** Production réalisée sur un poste, dans l'unité du poste. */
    public record PosteRealise(
        String codePoste, String libelle, Set<String> categories,
        double quantiteRealisee, double objectifAjuste, String unite
    ) {}

    /**
     * Bouillie produite dans la journée, convertie en kg à partir des sceaux réalisés et du poids d'un sceau
     * (lu sur le conditionnement, même donnée que les prédictions de fiche). Convertible seulement quand un
     * seul produit bouillie est prévu ce jour-là : plusieurs produits rendraient la conversion ambiguë.
     */
    public record ProductionBouillie(double quantiteKg, boolean convertible, String motifNonConvertible) {}
}
