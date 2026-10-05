package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_vente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneVenteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String designationProduit;

    @Column(nullable = false)
    private String conditionnement;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private double prixUnitaire;

    /** Pourcentage de remise (0 à 100) portant sur la ligne entière. */
    @Column(nullable = false)
    private double remise;

    /**
     * Montant net de la ligne.
     *
     * <p>Doit rester strictement aligné sur {@code LigneVente.montantHT()}, qui fait
     * référence. Cette copie n'existe que parce que plusieurs états de pilotage
     * agrègent directement les entités de persistance sans repasser par le domaine ;
     * la centraliser ici évite que la formule soit réécrite dans chacun d'eux.</p>
     *
     * <p>Depuis {@code V13}, {@code remise} est un pourcentage. Une formule restée
     * en montant aurait retranché « 10 » francs là où le domaine retranche 10 % —
     * sans qu'aucun type ni aucune contrainte ne le signale.</p>
     */
    public double montantHT() {
        return quantite * prixUnitaire * (1 - remise / 100.0);
    }
}
