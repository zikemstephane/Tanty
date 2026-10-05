package com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.ajuster;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockDetailResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterNiveauxDetaillesUseCase;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.service.GestionDeStock;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — ajustement d'inventaire : porte la quantité d'un niveau de stock à
 * une valeur cible et trace le delta via un mouvement AJUSTEMENT_INVENTAIRE.
 */
@Service
@Transactional
public class AjusterStockUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ConsulterNiveauxDetaillesUseCase detailUseCase;

    public AjusterStockUseCase(NiveauStockRepository niveauStockRepository,
                               MouvementStockRepository mouvementRepository,
                               ConsulterNiveauxDetaillesUseCase detailUseCase) {
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.detailUseCase = detailUseCase;
    }

    public NiveauStockDetailResponse executer(Long stockLevelId, long nouvelleQuantite, String motif, String matricule) {
        if (nouvelleQuantite < 0) {
            throw new IllegalArgumentException("La nouvelle quantité ne peut pas être négative");
        }
        NiveauStock niveau = niveauStockRepository.trouverTous().stream()
            .filter(n -> stockLevelId.equals(n.getId()))
            .findFirst()
            .orElseThrow(() -> new EntiteNonTrouveeException("Niveau de stock introuvable : " + stockLevelId));

        long delta = nouvelleQuantite - niveau.getQuantiteEnBase();
        String reference = genererReferenceMouvement();
        String raison = (motif != null && !motif.isBlank()) ? motif : "Ajustement d'inventaire";

        if (delta > 0) {
            MouvementStock m = GestionDeStock.appliquerEntree(niveau, TypeMouvement.AJUSTEMENT_INVENTAIRE,
                reference, null, delta, matricule, null, raison);
            mouvementRepository.enregistrer(m);
        } else if (delta < 0) {
            MouvementStock m = GestionDeStock.appliquerSortie(niveau, TypeMouvement.AJUSTEMENT_INVENTAIRE,
                reference, null, -delta, matricule, null, raison);
            mouvementRepository.enregistrer(m);
        }

        niveauStockRepository.sauvegarder(niveau);
        return detailUseCase.consulterParId(stockLevelId)
            .orElseThrow(() -> new EntiteNonTrouveeException("Niveau de stock introuvable : " + stockLevelId));
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
