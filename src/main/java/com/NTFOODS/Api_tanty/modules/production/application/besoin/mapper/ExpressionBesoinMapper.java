package com.NTFOODS.Api_tanty.modules.production.application.besoin.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.response.ExpressionBesoinResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.model.ExpressionBesoin;
import org.springframework.stereotype.Component;

@Component
public class ExpressionBesoinMapper {

    public ExpressionBesoinResponse versResponse(ExpressionBesoin b) {
        return new ExpressionBesoinResponse(b.getId(), b.getNumero(), b.getReferencePPH(), b.getType(),
            b.getCodeProduit(), b.getDesignationProduit(), b.getQuantiteDemandee(), b.getUnite(),
            b.getMatriculeDemandeur(), b.getDateBesoin(), b.getMotif(), b.getStatut(),
            b.getMatriculeValidateur(), b.getMotifRejet(), b.getDateCreation(), b.getDateModification());
    }
}
