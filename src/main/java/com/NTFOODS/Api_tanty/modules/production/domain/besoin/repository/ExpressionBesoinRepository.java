package com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.model.ExpressionBesoin;

import java.util.List;
import java.util.Optional;

public interface ExpressionBesoinRepository {
    ExpressionBesoin sauvegarder(ExpressionBesoin besoin);
    Optional<ExpressionBesoin> trouverParNumero(String numero);
    List<ExpressionBesoin> listerParStatut(StatutBesoin statut);
    List<ExpressionBesoin> listerTous();
    boolean existeParNumero(String numero);
}
