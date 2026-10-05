package com.NTFOODS.Api_tanty.modules.production.domain.lot.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotRepository {
    LotProduitFini sauvegarder(LotProduitFini lot);
    Optional<LotProduitFini> trouverParNumero(String numeroLot);
    List<LotProduitFini> listerParStatut(StatutLot statut);
    List<LotProduitFini> listerParPPH(String referencePPH);
    List<LotProduitFini> listerParCodeProduit(String codeProduit);
    List<LotProduitFini> listerParPeriode(LocalDate debut, LocalDate fin);
    /** Compte tous les lots d'une date de production, quel que soit leur statut — sert à numéroter le prochain lot. */
    long compterParDate(LocalDate date);
}
