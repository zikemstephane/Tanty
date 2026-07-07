package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_retour")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneRetourJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bon_retour_id", nullable = false)
    private BonRetourJpaEntity bonRetour;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "quantite", nullable = false)
    private long quantite;

    @Column(name = "motif", length = 300)
    private String motif;
}
