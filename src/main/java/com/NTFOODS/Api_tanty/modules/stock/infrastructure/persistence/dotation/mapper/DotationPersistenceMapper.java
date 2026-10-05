package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.LigneDotation;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.DotationJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.LigneDotationJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DotationPersistenceMapper {

    public DotationJpaEntity versEntite(Dotation d) {
        DotationJpaEntity entite = DotationJpaEntity.builder()
            .id(d.getId()).numero(d.getNumero()).matriculeCommercial(d.getMatriculeCommercial())
            .dateDotation(d.getDateDotation()).dateEffet(d.getDateEffet()).statut(d.getStatut().name())
            .matriculeGestionnaire(d.getMatriculeGestionnaire()).motifRefus(d.getMotifRefus())
            .dateCreation(d.getDateCreation()).dateDecision(d.getDateDecision()).version(d.getVersion())
            .lignes(new ArrayList<>())
            .build();

        List<LigneDotationJpaEntity> lignes = d.getLignes().stream()
            .map(l -> LigneDotationJpaEntity.builder()
                .id(l.getId()).dotation(entite).codeProduit(l.getCodeProduit()).sku(l.getSku())
                .quantiteDemandee(l.getQuantiteDemandee()).quantiteApprouvee(l.getQuantiteApprouvee())
                .motifModification(l.getMotifModification()).build())
            .toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    public Dotation versDomaine(DotationJpaEntity e) {
        List<LigneDotation> lignes = e.getLignes().stream()
            .map(l -> LigneDotation.reconstituer(l.getId(), l.getCodeProduit(), l.getSku(),
                l.getQuantiteDemandee(), l.getQuantiteApprouvee(), l.getMotifModification()))
            .toList();
        return Dotation.reconstituer(e.getId(), e.getNumero(), e.getMatriculeCommercial(), e.getDateDotation(),
            e.getDateEffet(), StatutDotation.valueOf(e.getStatut()), lignes, e.getMatriculeGestionnaire(),
            e.getMotifRefus(), e.getDateCreation(), e.getDateDecision(), e.getVersion());
    }
}
