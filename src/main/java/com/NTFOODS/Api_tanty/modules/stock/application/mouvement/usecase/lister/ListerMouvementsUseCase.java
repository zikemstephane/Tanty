package com.NTFOODS.Api_tanty.modules.stock.application.mouvement.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.dto.response.MouvementResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.mapper.MouvementMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — historique des mouvements de stock.
 * Filtres : produit, entrepôt, type, période, document.
 */
@Service
@Transactional(readOnly = true)
public class ListerMouvementsUseCase {

    private final MouvementStockRepository mouvementRepository;
    private final MouvementMapper mouvementMapper;

    public ListerMouvementsUseCase(MouvementStockRepository mouvementRepository, MouvementMapper mouvementMapper) {
        this.mouvementRepository = mouvementRepository;
        this.mouvementMapper = mouvementMapper;
    }

    public List<MouvementResponse> executer(String codeProduit, String codeEntrepot, String type,
                                            String referenceDocument, LocalDate debut, LocalDate fin) {
        List<com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock> mouvements;
        if (codeProduit != null && !codeProduit.isBlank()) {
            mouvements = mouvementRepository.trouverParProduit(codeProduit.trim().toUpperCase());
        } else if (codeEntrepot != null && !codeEntrepot.isBlank()) {
            mouvements = mouvementRepository.trouverParEntrepot(codeEntrepot);
        } else if (type != null && !type.isBlank()) {
            mouvements = mouvementRepository.trouverParType(TypeMouvement.valueOf(type.trim().toUpperCase()));
        } else if (referenceDocument != null && !referenceDocument.isBlank()) {
            mouvements = mouvementRepository.trouverParDocument(referenceDocument);
        } else if (debut != null && fin != null) {
            mouvements = mouvementRepository.trouverParPeriode(debut, fin);
        } else {
            mouvements = mouvementRepository.trouverParPeriode(LocalDate.now().minusDays(30), LocalDate.now());
        }
        return mouvements.stream().map(mouvementMapper::versResponse).toList();
    }

    /**
     * Variante paginée — retourne une {@link Page} de mouvements selon les mêmes filtres.
     */
    public Page<MouvementResponse> executerPagine(String codeProduit, String codeEntrepot, String type,
                                                   String referenceDocument, LocalDate debut, LocalDate fin,
                                                   Pageable pageable) {
        Page<MouvementStock> page;
        if (codeProduit != null && !codeProduit.isBlank()) {
            page = mouvementRepository.trouverParProduitPagine(codeProduit.trim().toUpperCase(), pageable);
        } else if (codeEntrepot != null && !codeEntrepot.isBlank()) {
            page = mouvementRepository.trouverParEntrepotPagine(codeEntrepot, pageable);
        } else if (type != null && !type.isBlank()) {
            page = mouvementRepository.trouverParTypePagine(TypeMouvement.valueOf(type.trim().toUpperCase()), pageable);
        } else if (referenceDocument != null && !referenceDocument.isBlank()) {
            // Document filter is not paginated at JPA level (rare, small result set)
            List<MouvementStock> list = mouvementRepository.trouverParDocument(referenceDocument);
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), list.size());
            List<MouvementResponse> content = list.subList(start, end).stream().map(mouvementMapper::versResponse).toList();
            return new org.springframework.data.domain.PageImpl<>(content, pageable, list.size());
        } else if (debut != null && fin != null) {
            page = mouvementRepository.trouverParPeriodePagine(debut, fin, pageable);
        } else {
            page = mouvementRepository.trouverTousPagine(pageable);
        }
        return page.map(mouvementMapper::versResponse);
    }
}
