package com.NTFOODS.Api_tanty.modules.production.application.lot.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.lot.dto.response.LotResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import org.springframework.stereotype.Component;

@Component
public class LotMapper {

    public LotResponse versResponse(LotProduitFini lot) {
        return new LotResponse(lot.getId(), lot.getNumeroLot(), lot.getReferencePPH(),
            lot.getProductSku(), lot.getProductName(), lot.getProductUnit(),
            lot.getDeclaredQuantityKg(), lot.getEquivalentUnits(),
            lot.getDateProduction(), lot.getDlc(),
            lot.getMatriculeChefProduction(), lot.getDeclaredBy(),
            lot.getStatut().name(), lot.getMatriculeGestionnaireValidation(), lot.getMotifRejet(),
            lot.getConditioningType(), lot.getConditioningQty(), lot.getNotes(),
            lot.getDateCreation() != null ? lot.getDateCreation().toLocalDate() : null,
            lot.getValidatedAt() != null ? lot.getValidatedAt().toLocalDate() : null);
    }
}
