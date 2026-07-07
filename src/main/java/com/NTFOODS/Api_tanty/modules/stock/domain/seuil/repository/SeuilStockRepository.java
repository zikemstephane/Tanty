package com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;

import java.util.List;
import java.util.Optional;

public interface SeuilStockRepository {
    SeuilStock sauvegarder(SeuilStock seuil);
    Optional<SeuilStock> trouver(String codeProduit, TypeEntrepot typeEntrepot);
    List<SeuilStock> trouverParType(TypeEntrepot typeEntrepot);
    List<SeuilStock> trouverTous();
}
