package com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DotationRepository {
    Dotation sauvegarder(Dotation dotation);
    Optional<Dotation> trouverParNumero(String numero);
    List<Dotation> trouverParStatut(StatutDotation statut);
    List<Dotation> trouverParCommercial(String matriculeCommercial);
    List<Dotation> trouverParDateEffet(LocalDate dateEffet);
    List<Dotation> trouverTous();
    long compterParAnnee(int annee);
}
