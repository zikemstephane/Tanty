package com.NTFOODS.Api_tanty.modules.production.domain.prediction.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Répartition de la poudre dosée entre les produits d'un bon de commande.
 *
 * La poudre se partage au prorata du poids de chaque produit demandé (quantité commandée × poids
 * unitaire). Chaque produit reçoit donc sa part de poudre, et la somme des parts vaut la poudre
 * totale : la poudre n'est jamais comptée deux fois.
 *
 * Un produit est exclu, avec son motif, s'il n'a pas de poids unitaire ou pas de quantité demandée.
 */
public final class RepartitionPoudre {

    private RepartitionPoudre() {}

    /** Un produit du bon de commande, avec ce qui est nécessaire pour le prédire. */
    public record Produit(String code, String designation, String composition, double poidsUnitaireGrammes,
                          int unitesParCarton, long quantiteCommandee) {}

    /** Prédiction d'un produit : poudre allouée, unités de base et cartons. */
    public record Ligne(String codeProduit, String designation, String composition, double poudreAlloueeKg,
                        int quantiteUnitesBase, int quantiteCartons, double poidsUnitaireGrammes, int unitesParCarton) {}

    /** Produit exclu de la prédiction, avec la raison. */
    public record Exclusion(String codeProduit, String motif) {}

    public record Resultat(List<Ligne> lignes, List<Exclusion> exclusions) {}

    public static Resultat repartir(double poudreTotaleKg, List<Produit> produits) {
        List<Exclusion> exclusions = new ArrayList<>();
        List<Produit> retenus = new ArrayList<>();
        for (Produit p : produits) {
            if (p.poidsUnitaireGrammes() <= 0) {
                exclusions.add(new Exclusion(p.code(), "poids unitaire manquant : la poudre ne peut pas être répartie"));
            } else if (p.quantiteCommandee() <= 0) {
                exclusions.add(new Exclusion(p.code(), "quantité commandée nulle"));
            } else {
                retenus.add(p);
            }
        }

        double besoinTotalKg = retenus.stream().mapToDouble(RepartitionPoudre::besoinKg).sum();
        List<Ligne> lignes = new ArrayList<>();
        if (besoinTotalKg <= 0 || poudreTotaleKg <= 0) {
            return new Resultat(lignes, exclusions);
        }

        for (Produit p : retenus) {
            double poudreKg = poudreTotaleKg * besoinKg(p) / besoinTotalKg;
            int unites = (int) Math.floor(poudreKg * 1000.0 / p.poidsUnitaireGrammes());
            int parCarton = Math.max(1, p.unitesParCarton());
            lignes.add(new Ligne(p.code(), p.designation(), p.composition(), arrondi(poudreKg),
                unites, unites / parCarton, p.poidsUnitaireGrammes(), parCarton));
        }
        return new Resultat(lignes, exclusions);
    }

    /** Poids demandé par un produit, en kilogrammes. */
    static double besoinKg(Produit p) {
        return p.quantiteCommandee() * p.poidsUnitaireGrammes() / 1000.0;
    }

    private static double arrondi(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
