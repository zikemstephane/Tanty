package com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.StatutAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model.Alerte;

import java.util.List;
import java.util.Optional;

public interface AlerteRepository {
    Alerte sauvegarder(Alerte alerte);
    Optional<Alerte> trouverParReference(String reference);
    List<Alerte> trouverParStatut(StatutAlerte statut);
    List<Alerte> trouverParDestinataire(String role);
    List<Alerte> trouverTous();
    boolean existeAlerteActive(TypeAlerte type, String codeProduit, String codeEntrepot);
    long compter();
}
