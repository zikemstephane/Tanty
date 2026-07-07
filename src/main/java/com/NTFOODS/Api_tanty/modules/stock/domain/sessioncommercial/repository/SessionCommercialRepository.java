package com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionCommercialRepository {
    SessionCommercial sauvegarder(SessionCommercial session);
    Optional<SessionCommercial> trouverParNumero(String numero);
    Optional<SessionCommercial> trouverSessionOuverteParCommercial(String matriculeCommercial);
    List<SessionCommercial> listerParCommercial(String matriculeCommercial);
    List<SessionCommercial> listerParDateEffet(LocalDate dateEffet);
    List<SessionCommercial> listerParStatut(String statut);
}
