package com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;

import java.util.List;
import java.util.Optional;

public interface BonRetourRepository {
    BonRetour sauvegarder(BonRetour bonRetour);
    Optional<BonRetour> trouverParNumero(String numero);
    List<BonRetour> trouverParCommercial(String matriculeCommercial);
    List<BonRetour> trouverTous();
    long compterParAnnee(int annee);
}
