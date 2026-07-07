package com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;

import java.util.List;
import java.util.Optional;

/** Port — Persistance des entrepôts. */
public interface EntrepotRepository {

    Entrepot sauvegarder(Entrepot entrepot);

    Optional<Entrepot> trouverParCode(String code);

    Optional<Entrepot> trouverMobileParCommercial(String matriculeCommercial);

    Optional<Entrepot> trouverUniqueParType(TypeEntrepot type);

    boolean existeParCode(String code);

    List<Entrepot> trouverTous();

    List<Entrepot> trouverParType(TypeEntrepot type);
}
