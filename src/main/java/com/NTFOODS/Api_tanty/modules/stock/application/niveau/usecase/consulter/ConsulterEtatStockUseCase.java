package com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.mapper.NiveauStockMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — consulter l'état des stocks en temps réel.
 * Filtres : par entrepôt, par produit, ou par type d'entrepôt (central/tampon/mobile).
 */
@Service
@Transactional(readOnly = true)
public class ConsulterEtatStockUseCase {

    private final NiveauStockRepository niveauStockRepository;
    private final NiveauStockMapper niveauStockMapper;

    public ConsulterEtatStockUseCase(NiveauStockRepository niveauStockRepository, NiveauStockMapper niveauStockMapper) {
        this.niveauStockRepository = niveauStockRepository;
        this.niveauStockMapper = niveauStockMapper;
    }

    public List<NiveauStockResponse> executer(String codeEntrepot, String codeProduit, String typeEntrepot) {
        List<NiveauStock> niveaux;
        if (codeEntrepot != null && !codeEntrepot.isBlank()) {
            niveaux = niveauStockRepository.trouverParEntrepot(codeEntrepot);
        } else if (codeProduit != null && !codeProduit.isBlank()) {
            niveaux = niveauStockRepository.trouverParProduit(codeProduit.trim().toUpperCase());
        } else if (typeEntrepot != null && !typeEntrepot.isBlank()) {
            niveaux = niveauStockRepository.trouverParType(TypeEntrepot.valueOf(typeEntrepot.trim().toUpperCase()));
        } else {
            niveaux = niveauStockRepository.trouverTous();
        }
        return niveaux.stream().map(niveauStockMapper::versResponse).toList();
    }
}
