package com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.StatutInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;

import java.util.List;
import java.util.Optional;

public interface InventaireRepository {
    SessionInventaire sauvegarder(SessionInventaire session);
    Optional<SessionInventaire> trouverParNumero(String numero);
    List<SessionInventaire> trouverParStatut(StatutInventaire statut);
    List<SessionInventaire> trouverTous();
    long compterParAnnee(int annee);
}
