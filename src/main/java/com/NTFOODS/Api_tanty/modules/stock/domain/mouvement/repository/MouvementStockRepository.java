package com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/** Port — Registre (persistance) des mouvements de stock. */
public interface MouvementStockRepository {

    MouvementStock enregistrer(MouvementStock mouvement);

    List<MouvementStock> trouverParProduit(String codeProduit);

    List<MouvementStock> trouverParEntrepot(String codeEntrepot);

    List<MouvementStock> trouverParType(TypeMouvement type);

    List<MouvementStock> trouverParPeriode(LocalDate debut, LocalDate fin);

    List<MouvementStock> trouverParDocument(String referenceDocument);

    long compter();

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<MouvementStock> trouverParProduitPagine(String codeProduit, Pageable pageable);
    Page<MouvementStock> trouverParEntrepotPagine(String codeEntrepot, Pageable pageable);
    Page<MouvementStock> trouverParTypePagine(TypeMouvement type, Pageable pageable);
    Page<MouvementStock> trouverParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable);
    Page<MouvementStock> trouverTousPagine(Pageable pageable);
}
