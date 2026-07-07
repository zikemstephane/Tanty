package com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;

import java.util.Objects;

/**
 * Objectif journalier assigné à un machiniste par le Chef Machiniste.
 * Un machiniste est assigné à une opération précise sur un type de poudre.
 */
public class ObjectifMachiniste {

    private final String machinisteMatricule;
    private final String nomMachiniste;
    private final OperationBroyage operation;
    private final TypePoudre typePoudre;
    private final double objectifKg;

    public ObjectifMachiniste(String machinisteMatricule, String nomMachiniste,
                              OperationBroyage operation, TypePoudre typePoudre, double objectifKg) {
        if (objectifKg <= 0) throw new IllegalArgumentException("L'objectif doit être positif");
        this.machinisteMatricule = Objects.requireNonNull(machinisteMatricule, "Matricule requis");
        this.nomMachiniste = Objects.requireNonNull(nomMachiniste, "Nom requis");
        this.operation = Objects.requireNonNull(operation, "Opération requise");
        this.typePoudre = Objects.requireNonNull(typePoudre, "Type de poudre requis");
        this.objectifKg = objectifKg;
    }

    public String getMachinisteMatricule() { return machinisteMatricule; }
    public String getNomMachiniste() { return nomMachiniste; }
    public OperationBroyage getOperation() { return operation; }
    public TypePoudre getTypePoudre() { return typePoudre; }
    public double getObjectifKg() { return objectifKg; }
}
