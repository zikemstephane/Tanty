package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper.ProduitMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — Lister / rechercher les produits du catalogue.
 * Filtres optionnels : marque, gamme, variété, actif.
 */
@Service
@Transactional(readOnly = true)
public class ListerProduitsUseCase {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public ListerProduitsUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }

    public List<ProduitResponse> executer(String marque, String gamme, String variete, Boolean actif) {
        boolean sansFiltre = marque == null && gamme == null && variete == null && actif == null;
        var produits = sansFiltre
            ? produitRepository.trouverTous()
            : produitRepository.rechercher(marque, gamme, variete, actif);
        return produits.stream().map(produitMapper::versResponse).toList();
    }
}
