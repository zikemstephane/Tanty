package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parseur de libellés de conditionnement.
 *
 * Exemples supportés :
 * - "25 sachets x 57g"       → 25 sachets, 57 g
 * - "12 etuis x 150g"        → 12 etuis, 150 g
 * - "4 seaux x 2,2kg"        → 4 seaux, 2200 g
 * - "4 seaux x 880g"         → 4 seaux, 880 g
 * - "seau de 1600g"          → 1 seau, 1600 g
 * - "seau de  4,1kg"         → 1 seau, 4100 g
 */
public class ConditionnementParser {

    private static final Pattern PATTERN_NOMBRE_X_POIDS =
        Pattern.compile("^(\\d+)\\s+([a-zA-Z_\\-]+)\\s*x\\s*(\\d+[,.]?\\d*)\\s*(g|kg|gr|grammes|kilos|kilogrammes)?$", Pattern.CASE_INSENSITIVE);

    private static final Pattern PATTERN_UNITE_DE_POIDS =
        Pattern.compile("^([a-zA-Z_\\-]+)\\s+de\\s+(\\d+[,.]?\\d*)\\s*(g|kg|gr|grammes|kilos|kilogrammes)?$", Pattern.CASE_INSENSITIVE);

    private ConditionnementParser() {}

    public static Resultat parse(String libelle) {
        Objects.requireNonNull(libelle, "Le libellé de conditionnement est obligatoire");
        String nettoye = libelle.trim().toLowerCase().replaceAll("\\s+", " ");

        Matcher m1 = PATTERN_NOMBRE_X_POIDS.matcher(nettoye);
        if (m1.matches()) {
            int nombre = Integer.parseInt(m1.group(1));
            String unite = normaliserUnite(m1.group(2));
            double poids = parsePoids(m1.group(3), m1.group(4));
            return new Resultat(nombre, unite, poids);
        }

        Matcher m2 = PATTERN_UNITE_DE_POIDS.matcher(nettoye);
        if (m2.matches()) {
            String unite = normaliserUnite(m2.group(1));
            double poids = parsePoids(m2.group(2), m2.group(3));
            return new Resultat(1, unite, poids);
        }

        return new Resultat(1, "unité", 0.0);
    }

    private static double parsePoids(String valeur, String unite) {
        double valeurNumerique = Double.parseDouble(valeur.replace(",", ".").trim());
        if (unite != null) {
            String u = unite.toLowerCase();
            if (u.startsWith("kg") || u.startsWith("kilo")) {
                return valeurNumerique * 1000.0;
            }
        }
        return valeurNumerique;
    }

    private static String normaliserUnite(String unite) {
        String u = unite.trim().toLowerCase();
        return u.endsWith("s") && u.length() > 1 ? u.substring(0, u.length() - 1) : u;
    }

    public record Resultat(int nombreUnites, String unite, double poidsUnitaireGrammes) {}
}
