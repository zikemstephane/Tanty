package com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;

import java.util.Objects;

/**
 * Réalisation journalière d'un machiniste (kg réellement traités vs objectif).
 * Le système calcule automatiquement le % de réalisation.
 */
public class RealisationMachiniste {

    private final String machinisteMatricule;
    private final String nomMachiniste;
    private final OperationBroyage operation;
    private final TypePoudre typePoudre;
    private final double objectifKg;
    private final double realiseKg;

    public RealisationMachiniste(String machinisteMatricule, String nomMachiniste,
                                 OperationBroyage operation, TypePoudre typePoudre,
                                 double objectifKg, double realiseKg) {
        if (objectifKg < 0) throw new IllegalArgumentException("L'objectif ne peut être négatif");
        if (realiseKg < 0) throw new IllegalArgumentException("La réalisation ne peut être négative");
        this.machinisteMatricule = Objects.requireNonNull(machinisteMatricule);
        this.nomMachiniste = Objects.requireNonNull(nomMachiniste);
        this.operation = Objects.requireNonNull(operation);
        this.typePoudre = Objects.requireNonNull(typePoudre);
        this.objectifKg = objectifKg;
        this.realiseKg = realiseKg;
    }

    /** % de réalisation par rapport à l'objectif, plafonné à 200 %. Si l'objectif est 0, renvoie 0 %. */
    public double getPctRealisation() {
        if (objectifKg == 0) return 0.0;
        return Math.min((realiseKg / objectifKg) * 100.0, 200.0);
    }

    public String getMachinisteMatricule() { return machinisteMatricule; }
    public String getNomMachiniste() { return nomMachiniste; }
    public OperationBroyage getOperation() { return operation; }
    public TypePoudre getTypePoudre() { return typePoudre; }
    public double getObjectifKg() { return objectifKg; }
    public double getRealiseKg() { return realiseKg; }
}
