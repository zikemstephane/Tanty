package com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.transferer;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockDetailResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterNiveauxDetaillesUseCase;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.service.GestionDeStock;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — transfert d'une quantité (unités de base) du stock CENTRAL vers le
 * stock TAMPON, à partir de l'id du niveau central source.
 */
@Service
@Transactional
public class TransfererVersTamponUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final NiveauStockRepository niveauStockRepository;
    private final EntrepotRepository entrepotRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ConsulterNiveauxDetaillesUseCase detailUseCase;

    public TransfererVersTamponUseCase(NiveauStockRepository niveauStockRepository,
                                       EntrepotRepository entrepotRepository,
                                       MouvementStockRepository mouvementRepository,
                                       ConsulterNiveauxDetaillesUseCase detailUseCase) {
        this.niveauStockRepository = niveauStockRepository;
        this.entrepotRepository = entrepotRepository;
        this.mouvementRepository = mouvementRepository;
        this.detailUseCase = detailUseCase;
    }

    public NiveauStockDetailResponse executer(Long stockLevelId, long quantite, String matricule) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité à transférer doit être positive");
        }
        NiveauStock source = niveauStockRepository.trouverTous().stream()
            .filter(n -> stockLevelId.equals(n.getId()))
            .findFirst()
            .orElseThrow(() -> new EntiteNonTrouveeException("Niveau de stock introuvable : " + stockLevelId));

        if (source.getTypeEntrepot() != TypeEntrepot.CENTRAL) {
            throw new RegleMetierException("Le transfert vers le tampon doit partir d'un stock central");
        }

        Entrepot tampon = entrepotRepository.trouverUniqueParType(TypeEntrepot.TAMPON)
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun entrepôt tampon configuré"));

        NiveauStock cible = niveauStockRepository.trouver(source.getCodeProduit(), tampon.getCode())
            .orElseGet(() -> NiveauStock.creer(source.getCodeProduit(), tampon.getCode(), TypeEntrepot.TAMPON));

        MouvementStock mouvement = GestionDeStock.appliquerTransfert(source, cible, genererReferenceMouvement(),
            null, quantite, matricule, null, "Transfert central → tampon");

        niveauStockRepository.sauvegarder(source);
        niveauStockRepository.sauvegarder(cible);
        mouvementRepository.enregistrer(mouvement);

        return detailUseCase.consulterParId(stockLevelId)
            .orElseThrow(() -> new EntiteNonTrouveeException("Niveau de stock introuvable : " + stockLevelId));
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
