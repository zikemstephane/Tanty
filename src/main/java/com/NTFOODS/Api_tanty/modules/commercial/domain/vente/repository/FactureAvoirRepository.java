package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.FactureAvoir;

import java.util.List;
import java.util.Optional;

public interface FactureAvoirRepository {
    FactureAvoir sauvegarder(FactureAvoir avoir);
    Optional<FactureAvoir> trouverParNumero(String numeroAvoir);
    List<FactureAvoir> listerParVenteOriginale(String numeroVenteOriginale);
    List<FactureAvoir> listerTous();
    String genererNumero(java.time.LocalDate date);
}
