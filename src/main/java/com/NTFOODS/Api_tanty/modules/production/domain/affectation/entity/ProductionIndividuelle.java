package com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity;

import java.util.Objects;

/** Quantité produite par un employé à un poste donné dans la journée. */
public class ProductionIndividuelle {

    private final String matriculeEmploye;
    private final String nomEmploye;
    private final String poste;
    private final double quantiteRealisee;

    public ProductionIndividuelle(String matriculeEmploye, String nomEmploye,
                                   String poste, double quantiteRealisee) {
        if (quantiteRealisee < 0) throw new IllegalArgumentException("La quantité réalisée ne peut être négative");
        this.matriculeEmploye = Objects.requireNonNull(matriculeEmploye);
        this.nomEmploye = Objects.requireNonNull(nomEmploye);
        this.poste = Objects.requireNonNull(poste);
        this.quantiteRealisee = quantiteRealisee;
    }

    public String getMatriculeEmploye() { return matriculeEmploye; }
    public String getNomEmploye() { return nomEmploye; }
    public String getPoste() { return poste; }
    public double getQuantiteRealisee() { return quantiteRealisee; }
}
