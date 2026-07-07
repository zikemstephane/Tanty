package com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.ZoneDeVente;

import java.util.List;
import java.util.Optional;

public interface ZoneDeVenteRepository {
    ZoneDeVente sauvegarder(ZoneDeVente zone);
    Optional<ZoneDeVente> trouverParNom(String nom);
    Optional<ZoneDeVente> trouverParId(Long id);
    List<ZoneDeVente> listerToutes();
    List<ZoneDeVente> listerActives();
}
