package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.consulter;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.mapper.ProduitMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — Consulter un produit par son code. Lecture ouverte à tous. */
@Service
@Transactional(readOnly = true)
public class ConsulterProduitUseCase {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public ConsulterProduitUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }

    public ProduitResponse executer(String codeProduit) {
        CodeProduit code = CodeProduit.de(codeProduit);
        Produit produit = produitRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", code.getValeur()));
        return produitMapper.versResponse(produit);
    }
}
