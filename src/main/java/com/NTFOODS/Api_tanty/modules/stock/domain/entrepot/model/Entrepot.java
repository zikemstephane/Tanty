package com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Entrepôt (lieu de stockage).
 *
 * Trois familles : CENTRAL (unique), TAMPON (unique), MOBILE (un par commercial,
 * identifié par le matricule du propriétaire).
 */
public class Entrepot {

    private Long id;
    private final String code;
    private String libelle;
    private final TypeEntrepot type;
    private final String matriculeProprietaire;   // non nul uniquement pour MOBILE
    private boolean actif;
    private final LocalDateTime dateCreation;
    private Long version;

    private Entrepot(String code, String libelle, TypeEntrepot type,
                     String matriculeProprietaire, LocalDateTime dateCreation) {
        this.code = Objects.requireNonNull(code, "Le code entrepôt est obligatoire");
        this.libelle = Objects.requireNonNull(libelle, "Le libellé est obligatoire");
        this.type = Objects.requireNonNull(type, "Le type d'entrepôt est obligatoire");
        if (type.estMobile() && (matriculeProprietaire == null || matriculeProprietaire.isBlank())) {
            throw new IllegalArgumentException("Un entrepôt MOBILE doit être rattaché à un commercial (matricule)");
        }
        if (!type.estMobile() && matriculeProprietaire != null) {
            throw new IllegalArgumentException("Seul un entrepôt MOBILE peut avoir un propriétaire");
        }
        this.matriculeProprietaire = matriculeProprietaire;
        this.dateCreation = dateCreation;
        this.actif = true;
        this.version = 0L;
    }

    public static Entrepot creerCentral(String code, String libelle) {
        return new Entrepot(code, libelle, TypeEntrepot.CENTRAL, null, LocalDateTime.now());
    }

    public static Entrepot creerTampon(String code, String libelle) {
        return new Entrepot(code, libelle, TypeEntrepot.TAMPON, null, LocalDateTime.now());
    }

    public static Entrepot creerMobile(String matriculeCommercial) {
        return new Entrepot(
            "MOBILE-" + matriculeCommercial,
            "Stock mobile " + matriculeCommercial,
            TypeEntrepot.MOBILE, matriculeCommercial, LocalDateTime.now()
        );
    }

    public static Entrepot reconstituer(Long id, String code, String libelle, TypeEntrepot type,
                                        String matriculeProprietaire, boolean actif,
                                        LocalDateTime dateCreation, Long version) {
        Entrepot e = new Entrepot(code, libelle, type, matriculeProprietaire, dateCreation);
        e.id = id;
        e.actif = actif;
        e.version = version;
        return e;
    }

    public void renommer(String nouveauLibelle) {
        this.libelle = Objects.requireNonNull(nouveauLibelle);
    }

    public void desactiver() { this.actif = false; }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public TypeEntrepot getType() { return type; }
    public String getMatriculeProprietaire() { return matriculeProprietaire; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrepot that)) return false;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }
}
