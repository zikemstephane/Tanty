package com.NTFOODS.Api_tanty.modules.stock.domain.transfert.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums.StatutTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.BonTransfert;

import java.util.List;
import java.util.Optional;

public interface BonTransfertRepository {
    BonTransfert sauvegarder(BonTransfert bonTransfert);
    Optional<BonTransfert> trouverParNumero(String numero);
    List<BonTransfert> trouverParStatut(StatutTransfert statut);
    List<BonTransfert> trouverTous();
    long compterParAnnee(int annee);
}
