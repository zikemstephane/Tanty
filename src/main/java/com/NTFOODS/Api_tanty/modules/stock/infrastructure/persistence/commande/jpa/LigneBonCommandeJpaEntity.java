package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "lignes_bon_commande")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneBonCommandeJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bon_commande_id", nullable = false)
    private BonCommandeJpaEntity bonCommande;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "designation", length = 255)
    private String designation;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "quantite_commandee", nullable = false)
    private long quantiteCommandee;

    @Column(name = "quantite_recue", nullable = false)
    private long quantiteRecue;

    @Column(name = "prix_unitaire_ht", precision = 15, scale = 2)
    private BigDecimal prixUnitaireHT;
}
