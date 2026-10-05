package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {
    private static final Pattern PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final String valeur;

    private Email(String valeur) { this.valeur = valeur; }

    public static Email de(String valeur) {
        Objects.requireNonNull(valeur, "L'email est obligatoire");
        String v = valeur.trim().toLowerCase();
        if (!PATTERN.matcher(v).matches()) {
            throw new IllegalArgumentException("Format email invalide: " + valeur);
        }
        return new Email(v);
    }

    public String getValeur() { return valeur; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        return Objects.equals(valeur, ((Email) o).valeur);
    }
    @Override public int hashCode() { return Objects.hash(valeur); }
    @Override public String toString() { return valeur; }
}
