package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_inventaire")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneInventaireJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private SessionInventaireJpaEntity session;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "emplacement", length = 120)
    private String emplacement;

    @Column(name = "quantite_theorique", nullable = false)
    private long quantiteTheorique;

    @Column(name = "quantite_comptee")
    private Long quantiteComptee;
}
