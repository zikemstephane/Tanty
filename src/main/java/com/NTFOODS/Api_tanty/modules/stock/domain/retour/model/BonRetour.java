package com.NTFOODS.Api_tanty.modules.stock.domain.retour.model;

import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Bon de Retour (invendus retournés par un commercial).
 *
 * À l'enregistrement : le stock tampon est recrédité et le stock mobile du
 * commercial débité (orchestré par le use case via GestionDeStock).
 */
public class BonRetour {

    private Long id;
    private final String numero;
    private final String matriculeCommercial;
    private final String matriculeGestionnaire;
    private final List<LigneRetour> lignes;
    private final LocalDate dateRetour;
    private final LocalDateTime dateCreation;
    private Long version;

    private BonRetour(String numero, String matriculeCommercial, String matriculeGestionnaire,
                      LocalDate dateRetour, LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.matriculeGestionnaire = Objects.requireNonNull(matriculeGestionnaire);
        this.dateRetour = Objects.requireNonNull(dateRetour);
        this.dateCreation = dateCreation;
        this.lignes = new ArrayList<>();
        this.version = 0L;
    }

    public static BonRetour creer(String numero, String matriculeCommercial, String matriculeGestionnaire,
                                  LocalDate dateRetour) {
        return new BonRetour(numero, matriculeCommercial, matriculeGestionnaire, dateRetour, LocalDateTime.now());
    }

    public static BonRetour reconstituer(Long id, String numero, String matriculeCommercial,
                                         String matriculeGestionnaire, List<LigneRetour> lignes,
                                         LocalDate dateRetour, LocalDateTime dateCreation, Long version) {
        BonRetour b = new BonRetour(numero, matriculeCommercial, matriculeGestionnaire, dateRetour, dateCreation);
        b.id = id;
        if (lignes != null) b.lignes.addAll(lignes);
        b.version = version;
        return b;
    }

    public LigneRetour ajouterLigne(String codeProduit, String sku, long quantite, String motif) {
        LigneRetour ligne = LigneRetour.creer(codeProduit, sku, quantite, motif);
        lignes.add(ligne);
        return ligne;
    }

    public void exigerNonVide() {
        if (lignes.isEmpty()) throw new RegleMetierException("Un bon de retour doit contenir au moins une ligne");
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getMatriculeGestionnaire() { return matriculeGestionnaire; }
    public List<LigneRetour> getLignes() { return Collections.unmodifiableList(lignes); }
    public LocalDate getDateRetour() { return dateRetour; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BonRetour that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
