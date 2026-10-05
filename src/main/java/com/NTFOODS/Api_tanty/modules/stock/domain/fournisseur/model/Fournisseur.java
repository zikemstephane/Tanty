package com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Fournisseur de matières premières et consommables.
 */
public class Fournisseur {

    private Long id;
    private final String code;
    private String raisonSociale;
    private String telephone;
    private String email;
    private String adresse;
    private boolean actif;
    private final LocalDateTime dateCreation;
    private Long version;

    private Fournisseur(String code, String raisonSociale, String telephone, String email,
                        String adresse, LocalDateTime dateCreation) {
        this.code = Objects.requireNonNull(code, "Le code fournisseur est obligatoire");
        if (raisonSociale == null || raisonSociale.isBlank()) {
            throw new IllegalArgumentException("La raison sociale est obligatoire");
        }
        this.raisonSociale = raisonSociale.trim();
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.dateCreation = dateCreation;
        this.actif = true;
        this.version = 0L;
    }

    public static Fournisseur creer(String code, String raisonSociale, String telephone,
                                    String email, String adresse) {
        return new Fournisseur(code.trim().toUpperCase(), raisonSociale, telephone, email, adresse,
            LocalDateTime.now());
    }

    public static Fournisseur reconstituer(Long id, String code, String raisonSociale, String telephone,
                                           String email, String adresse, boolean actif,
                                           LocalDateTime dateCreation, Long version) {
        Fournisseur f = new Fournisseur(code, raisonSociale, telephone, email, adresse, dateCreation);
        f.id = id;
        f.actif = actif;
        f.version = version;
        return f;
    }

    public void modifier(String raisonSociale, String telephone, String email, String adresse) {
        if (raisonSociale != null && !raisonSociale.isBlank()) this.raisonSociale = raisonSociale.trim();
        if (telephone != null) this.telephone = telephone;
        if (email != null) this.email = email;
        if (adresse != null) this.adresse = adresse;
    }

    public void desactiver() { this.actif = false; }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getRaisonSociale() { return raisonSociale; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fournisseur that)) return false;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }
}
