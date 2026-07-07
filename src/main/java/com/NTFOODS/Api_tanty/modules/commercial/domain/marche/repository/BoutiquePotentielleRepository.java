package com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.BoutiquePotentielle;

import java.util.List;

public interface BoutiquePotentielleRepository {
    BoutiquePotentielle sauvegarder(BoutiquePotentielle boutique);
    List<BoutiquePotentielle> listerParSecteur(String secteur);
    List<BoutiquePotentielle> listerTous();
    List<String> listerSecteurs();
}
