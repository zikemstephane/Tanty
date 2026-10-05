package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_reception")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneReceptionJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_id", nullable = false)
    private ReceptionJpaEntity reception;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "quantite_attendue", nullable = false)
    private long quantiteAttendue;

    @Column(name = "quantite_recue", nullable = false)
    private long quantiteRecue;

    @Column(name = "id_ligne_bc", length = 36)
    private String idLigneBC;
}
