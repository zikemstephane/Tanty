package com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockMobileRepository {
    StockMobile sauvegarder(StockMobile stockMobile);
    Optional<StockMobile> trouverParCommercialEtDate(String matriculeCommercial, LocalDate date);
    Optional<StockMobile> trouverDernierParCommercial(String matriculeCommercial);
    List<StockMobile> listerParDate(LocalDate date);
    List<StockMobile> listerParCommercial(String matriculeCommercial);
    List<StockMobile> listerTous();
}
