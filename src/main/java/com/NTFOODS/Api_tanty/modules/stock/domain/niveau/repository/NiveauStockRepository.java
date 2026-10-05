package com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;

import java.util.List;
import java.util.Optional;

/** Port — Persistance des niveaux de stock. */
public interface NiveauStockRepository {

    NiveauStock sauvegarder(NiveauStock niveau);

    Optional<NiveauStock> trouver(String codeProduit, String codeEntrepot);

    List<NiveauStock> trouverParEntrepot(String codeEntrepot);

    List<NiveauStock> trouverParProduit(String codeProduit);

    List<NiveauStock> trouverParType(TypeEntrepot type);

    List<NiveauStock> trouverTous();
}
