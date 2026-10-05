package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject;

import java.util.Objects;

public final class NomComplet {
    private final String prenom;
    private final String nom;

    private NomComplet(String prenom, String nom) {
        this.prenom = prenom;
        this.nom = nom;
    }

    public static NomComplet de(String prenom, String nom) {
        Objects.requireNonNull(prenom, "Le prénom est obligatoire");
        Objects.requireNonNull(nom, "Le nom est obligatoire");
        if (prenom.isBlank() || nom.isBlank()) {
            throw new IllegalArgumentException("Le prénom et le nom ne peuvent pas être vides");
        }
        return new NomComplet(prenom.trim(), nom.trim());
    }

    public String getPrenom() { return prenom; }
    public String getNom() { return nom; }
    public String getNomComplet() { return prenom + " " + nom; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NomComplet)) return false;
        NomComplet that = (NomComplet) o;
        return Objects.equals(prenom, that.prenom) && Objects.equals(nom, that.nom);
    }
    @Override public int hashCode() { return Objects.hash(prenom, nom); }
    @Override public String toString() { return getNomComplet(); }
}
