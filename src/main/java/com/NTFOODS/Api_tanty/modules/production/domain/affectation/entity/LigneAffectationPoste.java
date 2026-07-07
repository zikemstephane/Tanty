package com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.DureeAffectation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Affectation d'un groupe d'employés à un poste pour la journée.
 * L'objectif est auto-calculé selon la règle de proportionnalité :
 *   objectifAjuste = (effectifReel / effectifRef) × productionJour × duree.coefficient
 */
public class LigneAffectationPoste {

    private final CodePoste poste;
    private final List<String> matriculesEmployes;
    private final DureeAffectation duree;
    private final double objectifAjuste;

    public LigneAffectationPoste(CodePoste poste, List<String> matriculesEmployes, DureeAffectation duree) {
        if (matriculesEmployes == null || matriculesEmployes.isEmpty()) {
            throw new IllegalArgumentException("Au moins un employé doit être affecté au poste " + poste.getLibelle());
        }
        this.poste = Objects.requireNonNull(poste);
        this.matriculesEmployes = List.copyOf(matriculesEmployes);
        this.duree = Objects.requireNonNull(duree);
        this.objectifAjuste = poste.calculerObjectifAjuste(matriculesEmployes.size(), duree);
    }

    public int getEffectifReel() { return matriculesEmployes.size(); }

    public CodePoste getPoste() { return poste; }
    public List<String> getMatriculesEmployes() { return Collections.unmodifiableList(matriculesEmployes); }
    public DureeAffectation getDuree() { return duree; }
    public double getObjectifAjuste() { return objectifAjuste; }
}
