package com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.registre.entity.LigneRegistreEmploye;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Registre de Production Journalier.
 * Suivi des présences et des rendements individuels de chaque employé pour une journée.
 */
public class RegistreProduction {

    private Long id;
    private final String referencePPH;
    private final LocalDate date;
    private final String matriculeChefProduction;
    private final List<LigneRegistreEmploye> lignesEmployes;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private RegistreProduction(String referencePPH, LocalDate date,
                                String matriculeChefProduction, LocalDateTime dateCreation) {
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.date = Objects.requireNonNull(date);
        this.matriculeChefProduction = Objects.requireNonNull(matriculeChefProduction);
        this.dateCreation = dateCreation;
        this.lignesEmployes = new ArrayList<>();
        this.version = 0L;
    }

    public static RegistreProduction enregistrer(String referencePPH, LocalDate date,
                                                  String matriculeChefProduction,
                                                  List<LigneRegistreEmploye> lignes) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("Le registre doit contenir au moins un employé");
        }
        RegistreProduction registre = new RegistreProduction(referencePPH, date, matriculeChefProduction, LocalDateTime.now());
        registre.lignesEmployes.addAll(lignes);
        return registre;
    }

    public static RegistreProduction reconstituer(Long id, String referencePPH, LocalDate date,
                                                   String matriculeChefProduction,
                                                   List<LigneRegistreEmploye> lignes,
                                                   LocalDateTime dateCreation,
                                                   LocalDateTime dateModification, Long version) {
        RegistreProduction r = new RegistreProduction(referencePPH, date, matriculeChefProduction, dateCreation);
        r.id = id;
        r.lignesEmployes.addAll(lignes != null ? lignes : List.of());
        r.dateModification = dateModification;
        r.version = version;
        return r;
    }

    /**
     * Corrige les lignes du registre. Un registre ne se corrige que le jour même (avant minuit) : passé ce
     * délai, il devient une preuve d'audit définitive, comme le reste de la journée qu'il décrit.
     */
    public void modifierLignes(List<LigneRegistreEmploye> nouvellesLignes, LocalDate aujourdhui) {
        if (nouvellesLignes == null || nouvellesLignes.isEmpty()) {
            throw new IllegalArgumentException("Le registre doit contenir au moins un employé");
        }
        if (!date.equals(aujourdhui)) {
            throw new IllegalStateException(
                "Registre verrouillé : il ne peut être corrigé que le jour même de la date qu'il décrit.");
        }
        this.lignesEmployes.clear();
        this.lignesEmployes.addAll(nouvellesLignes);
        this.dateModification = LocalDateTime.now();
    }

    /** Vrai tant que le registre peut encore être corrigé (jour même). */
    public boolean estCorrigible(LocalDate aujourdhui) {
        return date.equals(aujourdhui);
    }

    public long nbPresents() {
        return lignesEmployes.stream().filter(LigneRegistreEmploye::isPresent).count();
    }

    public long nbAbsents() {
        return lignesEmployes.stream().filter(l -> !l.isPresent()).count();
    }

    public Long getId() { return id; }
    public String getReferencePPH() { return referencePPH; }
    public LocalDate getDate() { return date; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
    public List<LigneRegistreEmploye> getLignesEmployes() { return Collections.unmodifiableList(lignesEmployes); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistreProduction)) return false;
        RegistreProduction that = (RegistreProduction) o;
        return Objects.equals(referencePPH, that.referencePPH) && Objects.equals(date, that.date);
    }
    @Override public int hashCode() { return Objects.hash(referencePPH, date); }
}
