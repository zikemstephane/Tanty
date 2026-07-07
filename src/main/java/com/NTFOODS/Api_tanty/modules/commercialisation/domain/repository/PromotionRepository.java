package com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.Promotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {
    Promotion sauvegarder(Promotion promotion);
    Optional<Promotion> trouverParCode(String code);
    List<Promotion> listerTous();
    List<Promotion> listerParStatut(StatutPromotion statut);
    List<Promotion> listerActives();
    List<Promotion> listerParProduit(String codeProduit);
}
