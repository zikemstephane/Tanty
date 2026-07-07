package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Matricule {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z]{2,4}-?\\d{3,6}$");
    private final String valeur;

    private Matricule(String valeur) {
        this.valeur = valeur;
    }

    public static Matricule de(String valeur) {
        Objects.requireNonNull(valeur, "Le matricule est obligatoire");
        String v = valeur.trim().toUpperCase();
        if (!PATTERN.matcher(v).matches()) {
            throw new IllegalArgumentException("Matricule invalide: " + valeur + " (attendu ex: COM001, DG001)");
        }
        return new Matricule(v);
    }

    public String getValeur() { return valeur; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matricule)) return false;
        return Objects.equals(valeur, ((Matricule) o).valeur);
    }
    @Override public int hashCode() { return Objects.hash(valeur); }
    @Override public String toString() { return valeur; }
}
