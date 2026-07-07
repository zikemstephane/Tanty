package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_transfert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneTransfertJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bon_transfert_id", nullable = false)
    private BonTransfertJpaEntity bonTransfert;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "quantite", nullable = false)
    private long quantite;
}
