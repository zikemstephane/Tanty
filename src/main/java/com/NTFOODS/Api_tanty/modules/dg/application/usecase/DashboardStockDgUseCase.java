package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.StatutAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardStockDgUseCase {

    private final NiveauStockRepository niveauStockRepository;
    private final AlerteRepository alerteRepository;
    private final SeuilStockRepository seuilStockRepository;

    public DashboardStockDgUseCase(NiveauStockRepository niveauStockRepository,
                                    AlerteRepository alerteRepository,
                                    SeuilStockRepository seuilStockRepository) {
        this.niveauStockRepository = niveauStockRepository;
        this.alerteRepository = alerteRepository;
        this.seuilStockRepository = seuilStockRepository;
    }

    public DashboardStock executer() {
        LocalDate today = LocalDate.now();

        List<NiveauStock> stockCentral = niveauStockRepository.trouverParType(TypeEntrepot.CENTRAL);
        List<NiveauStock> stockTampon = niveauStockRepository.trouverParType(TypeEntrepot.TAMPON);
        List<NiveauStock> stockMobile = niveauStockRepository.trouverParType(TypeEntrepot.MOBILE);

        long totalCentral = stockCentral.stream().mapToLong(NiveauStock::getQuantiteEnBase).sum();
        long totalTampon = stockTampon.stream().mapToLong(NiveauStock::getQuantiteEnBase).sum();
        long totalMobile = stockMobile.stream().mapToLong(NiveauStock::getQuantiteEnBase).sum();

        int nbAlertesActives = alerteRepository.trouverParStatut(StatutAlerte.NOUVELLE).size();
        long totalAlertes = alerteRepository.compter();

        int nbProduitsSousSeuil = 0;
        for (NiveauStock niveau : stockCentral) {
            var seuil = seuilStockRepository.trouver(niveau.getCodeProduit(), niveau.getTypeEntrepot());
            if (seuil.isPresent() && seuil.get().estFranchiPar(niveau.getQuantiteEnBase())) {
                nbProduitsSousSeuil++;
            }
        }
        for (NiveauStock niveau : stockTampon) {
            var seuil = seuilStockRepository.trouver(niveau.getCodeProduit(), niveau.getTypeEntrepot());
            if (seuil.isPresent() && seuil.get().estFranchiPar(niveau.getQuantiteEnBase())) {
                nbProduitsSousSeuil++;
            }
        }

        return new DashboardStock(
            today,
            stockCentral.size(), totalCentral,
            stockTampon.size(), totalTampon,
            stockMobile.size(), totalMobile,
            nbAlertesActives, totalAlertes,
            nbProduitsSousSeuil
        );
    }

    public record DashboardStock(
        LocalDate date,
        int nbProduitsCentral,
        long totalQuantiteCentral,
        int nbProduitsTampon,
        long totalQuantiteTampon,
        int nbProduitsMobile,
        long totalQuantiteMobile,
        int nbAlertesActives,
        long totalAlertes,
        int nbProduitsSousSeuil
    ) {}
}
