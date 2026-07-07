package com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Seuils de stock d'un produit pour un type
 * d'entrepôt (central ou tampon). Exprimé en unités de base.
 *
 * Le DG définit TOUTES les politiques de seuils :
 * - seuilReapprovisionnement : niveau déclenchant un Bon de Commande
 * - seuilSecurite : niveau critique en dessous duquel une alerte urgente est émise
 */
public class SeuilStock {

    private Long id;
    private final String codeProduit;
    private final TypeEntrepot typeEntrepot;
    private long seuilReapprovisionnement;
    private long seuilSecurite;
    private boolean actif;
    private LocalDateTime dateModification;
    private Long version;

    private SeuilStock(String codeProduit, TypeEntrepot typeEntrepot, long seuilReapprovisionnement, long seuilSecurite) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.typeEntrepot = Objects.requireNonNull(typeEntrepot);
        if (typeEntrepot == TypeEntrepot.MOBILE) {
            throw new IllegalArgumentException("Les seuils ne s'appliquent qu'au central et au tampon");
        }
        if (seuilReapprovisionnement < 0) throw new IllegalArgumentException("Le seuil de réappro ne peut pas être négatif");
        if (seuilSecurite < 0) throw new IllegalArgumentException("Le seuil de sécurité ne peut pas être négatif");
        if (seuilSecurite > seuilReapprovisionnement) {
            throw new IllegalArgumentException("Le seuil de sécurité ne peut pas dépasser le seuil de réapprovisionnement");
        }
        this.seuilReapprovisionnement = seuilReapprovisionnement;
        this.seuilSecurite = seuilSecurite;
        this.actif = true;
    }

    public static SeuilStock definir(String codeProduit, TypeEntrepot typeEntrepot, long seuil) {
        return new SeuilStock(codeProduit.trim().toUpperCase(), typeEntrepot, seuil, 0);
    }

    public static SeuilStock definirAvecSeuilSecurite(String codeProduit, TypeEntrepot typeEntrepot,
                                                       long seuilReappro, long seuilSecurite) {
        return new SeuilStock(codeProduit.trim().toUpperCase(), typeEntrepot, seuilReappro, seuilSecurite);
    }

    public static SeuilStock reconstituer(Long id, String codeProduit, TypeEntrepot typeEntrepot, long seuil,
                                          boolean actif, LocalDateTime dateModification, Long version) {
        SeuilStock s = new SeuilStock(codeProduit, typeEntrepot, seuil, 0);
        s.id = id;
        s.actif = actif;
        s.dateModification = dateModification;
        s.version = version;
        return s;
    }

    public static SeuilStock reconstituerAvecSeuilSecurite(Long id, String codeProduit, TypeEntrepot typeEntrepot,
                                                            long seuilReappro, long seuilSecurite,
                                                            boolean actif, LocalDateTime dateModification, Long version) {
        SeuilStock s = new SeuilStock(codeProduit, typeEntrepot, seuilReappro, seuilSecurite);
        s.id = id;
        s.actif = actif;
        s.dateModification = dateModification;
        s.version = version;
        return s;
    }

    public void modifierSeuil(long nouveauSeuil) {
        if (nouveauSeuil < 0) throw new IllegalArgumentException("Le seuil ne peut pas être négatif");
        this.seuilReapprovisionnement = nouveauSeuil;
        this.dateModification = LocalDateTime.now();
    }

    public void modifierSeuilSecurite(long nouveauSeuilSecurite) {
        if (nouveauSeuilSecurite < 0) throw new IllegalArgumentException("Le seuil de sécurité ne peut pas être négatif");
        if (nouveauSeuilSecurite > seuilReapprovisionnement) {
            throw new IllegalArgumentException("Le seuil de sécurité ne peut pas dépasser le seuil de réapprovisionnement");
        }
        this.seuilSecurite = nouveauSeuilSecurite;
        this.dateModification = LocalDateTime.now();
    }

    public boolean estFranchiPar(long quantiteEnBase) {
        return actif && quantiteEnBase <= seuilReapprovisionnement;
    }

    public boolean estEnSeuilSecurite(long quantiteEnBase) {
        return actif && quantiteEnBase <= seuilSecurite;
    }

    public Long getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public TypeEntrepot getTypeEntrepot() { return typeEntrepot; }
    public long getSeuilReapprovisionnement() { return seuilReapprovisionnement; }
    public long getSeuilSecurite() { return seuilSecurite; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeuilStock that)) return false;
        return Objects.equals(codeProduit, that.codeProduit) && typeEntrepot == that.typeEntrepot;
    }

    @Override
    public int hashCode() { return Objects.hash(codeProduit, typeEntrepot); }
}
