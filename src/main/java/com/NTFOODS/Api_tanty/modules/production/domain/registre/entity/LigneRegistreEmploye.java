package com.NTFOODS.Api_tanty.modules.production.domain.registre.entity;

import java.util.Objects;

public class LigneRegistreEmploye {

    private final String matriculeEmploye;
    private final String nomEmploye;
    private final String poste;
    private final boolean present;
    private final String motifAbsence;
    private final String heureArrivee;
    private final double qteRealisee;
    private final String unite;

    public LigneRegistreEmploye(String matriculeEmploye, String nomEmploye, String poste,
                                 boolean present, String motifAbsence) {
        this(matriculeEmploye, nomEmploye, poste, present, motifAbsence, null, 0, null);
    }

    public LigneRegistreEmploye(String matriculeEmploye, String nomEmploye, String poste,
                                 boolean present, String motifAbsence,
                                 String heureArrivee, double qteRealisee, String unite) {
        this.matriculeEmploye = Objects.requireNonNull(matriculeEmploye);
        this.nomEmploye = Objects.requireNonNull(nomEmploye);
        this.poste = Objects.requireNonNull(poste);
        this.present = present;
        this.motifAbsence = motifAbsence;
        this.heureArrivee = heureArrivee;
        this.qteRealisee = qteRealisee;
        this.unite = unite;
    }

    public String getMatriculeEmploye() { return matriculeEmploye; }
    public String getNomEmploye() { return nomEmploye; }
    public String getPoste() { return poste; }
    public boolean isPresent() { return present; }
    public String getMotifAbsence() { return motifAbsence; }
    public String getHeureArrivee() { return heureArrivee; }
    public double getQteRealisee() { return qteRealisee; }
    public String getUnite() { return unite; }
}
