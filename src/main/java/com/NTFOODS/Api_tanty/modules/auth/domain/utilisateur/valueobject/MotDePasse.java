package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject;

import java.util.Objects;

/** Encapsule un hash bcrypt — jamais le mot de passe en clair. */
public final class MotDePasse {
    private final String hash;

    private MotDePasse(String hash) { this.hash = hash; }

    public static MotDePasse deHash(String hash) {
        Objects.requireNonNull(hash, "Le hash du mot de passe est obligatoire");
        if (hash.isBlank()) throw new IllegalArgumentException("Le hash ne peut pas être vide");
        return new MotDePasse(hash);
    }

    public String getHash() { return hash; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MotDePasse)) return false;
        return Objects.equals(hash, ((MotDePasse) o).hash);
    }
    @Override public int hashCode() { return Objects.hash(hash); }
    @Override public String toString() { return "***"; }
}
