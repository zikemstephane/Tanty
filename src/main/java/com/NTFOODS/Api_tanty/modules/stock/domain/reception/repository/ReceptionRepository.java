package com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;

import java.util.List;
import java.util.Optional;

public interface ReceptionRepository {
    Reception sauvegarder(Reception reception);
    Optional<Reception> trouverParNumero(String numero);
    List<Reception> trouverParStatut(StatutReception statut);
    List<Reception> trouverParBonCommande(String numeroBonCommande);
    List<Reception> trouverTous();
    long compterParAnnee(int annee);
}
