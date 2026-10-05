package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.entity;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.enums.TypeMouvementCaisse;

import java.time.LocalDateTime;
import java.util.Objects;

public class LigneCaisse {

    private final String referenceOperation;
    private final TypeMouvementCaisse typeMouvement;
    private final double montant;
    private final String motif;
    private final LocalDateTime dateOperation;
    private final String matriculeOperateur;

    public LigneCaisse(String referenceOperation, TypeMouvementCaisse typeMouvement,
                        double montant, String motif, LocalDateTime dateOperation,
                        String matriculeOperateur) {
        if (montant <= 0) throw new IllegalArgumentException("Le montant doit être positif");
        this.referenceOperation = Objects.requireNonNull(referenceOperation);
        this.typeMouvement = Objects.requireNonNull(typeMouvement);
        this.montant = montant;
        this.motif = Objects.requireNonNull(motif);
        this.dateOperation = Objects.requireNonNull(dateOperation);
        this.matriculeOperateur = Objects.requireNonNull(matriculeOperateur);
    }

    public String getReferenceOperation() { return referenceOperation; }
    public TypeMouvementCaisse getTypeMouvement() { return typeMouvement; }
    public double getMontant() { return montant; }
    public String getMotif() { return motif; }
    public LocalDateTime getDateOperation() { return dateOperation; }
    public String getMatriculeOperateur() { return matriculeOperateur; }
}
