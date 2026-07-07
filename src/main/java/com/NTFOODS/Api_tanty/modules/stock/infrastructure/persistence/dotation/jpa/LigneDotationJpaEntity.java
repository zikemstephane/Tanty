package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_dotation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneDotationJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dotation_id", nullable = false)
    private DotationJpaEntity dotation;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "quantite_demandee", nullable = false)
    private long quantiteDemandee;

    @Column(name = "quantite_approuvee", nullable = false)
    private long quantiteApprouvee;

    @Column(name = "motif_modification", length = 300)
    private String motifModification;
}
