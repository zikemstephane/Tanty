package com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;

import java.util.List;
import java.util.Optional;

public interface GrilleTarifaireRepository {
    GrilleTarifaire sauvegarder(GrilleTarifaire grille);
    Optional<GrilleTarifaire> trouver(String codeProduit, TypeClient segment);
    List<GrilleTarifaire> listerParProduit(String codeProduit);
    List<GrilleTarifaire> listerParSegment(TypeClient segment);
    List<GrilleTarifaire> listerTous();
}
