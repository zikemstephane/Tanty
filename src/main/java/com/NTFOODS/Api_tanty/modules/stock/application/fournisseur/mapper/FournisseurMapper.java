package com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.response.FournisseurResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.model.Fournisseur;
import org.springframework.stereotype.Component;

@Component
public class FournisseurMapper {
    public FournisseurResponse versResponse(Fournisseur f) {
        return new FournisseurResponse(f.getId(), f.getCode(), f.getRaisonSociale(),
            f.getTelephone(), f.getEmail(), f.getAdresse(), f.isActif());
    }
}
