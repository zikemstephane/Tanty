package com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.dto.FicheSyntheseResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.JournalCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.JournalCaisseRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FicheSyntheseUseCase {

    private final VenteRepository venteRepository;
    private final VersementRepository versementRepository;
    private final RecouvrementRepository recouvrementRepository;
    private final JournalCaisseRepository caisseRepository;
    private final NiveauStockRepository niveauStockRepository;

    public FicheSyntheseUseCase(VenteRepository venteRepository,
                                 VersementRepository versementRepository,
                                 RecouvrementRepository recouvrementRepository,
                                 JournalCaisseRepository caisseRepository,
                                 NiveauStockRepository niveauStockRepository) {
        this.venteRepository = venteRepository;
        this.versementRepository = versementRepository;
        this.recouvrementRepository = recouvrementRepository;
        this.caisseRepository = caisseRepository;
        this.niveauStockRepository = niveauStockRepository;
    }

    public FicheSyntheseResponse executer(LocalDate date) {
        LocalDate today = date != null ? date : LocalDate.now();

        List<Vente> ventesJour = venteRepository.listerParPeriode(today, today).stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE).toList();

        double caTotal = ventesJour.stream().mapToDouble(Vente::montantTotal).sum();
        Set<String> commerciaux = ventesJour.stream()
            .map(Vente::getMatriculeCommercial).collect(Collectors.toSet());

        List<Versement> versementsJour = versementRepository.listerParDate(today);
        double vac = versementsJour.stream()
            .filter(v -> v.getStatut() != StatutVersement.VALIDE)
            .mapToDouble(Versement::getCashVerse).sum();
        double vteCpt = versementsJour.stream()
            .filter(v -> v.getStatut() == StatutVersement.VALIDE)
            .mapToDouble(Versement::getCashVerse).sum();
        double vacInit = caTotal;

        double recouvrements = recouvrementRepository.listerParStatut(StatutRecouvrement.EN_COURS).stream()
            .mapToDouble(Recouvrement::montantRestant).sum();

        double sCaisse = caisseRepository.trouverParDate(today)
            .map(JournalCaisse::getSoldeCourant).orElse(0.0);

        List<NiveauStock> stocks = niveauStockRepository.trouverTous();
        Map<String, Long> stockParProduit = stocks.stream()
            .collect(Collectors.groupingBy(NiveauStock::getCodeProduit,
                Collectors.summingLong(NiveauStock::getQuantiteEnBase)));

        Map<String, long[]> ventesParProduit = new HashMap<>();
        for (Vente v : ventesJour) {
            for (LigneVente l : v.getLignes()) {
                ventesParProduit.computeIfAbsent(l.getCodeProduit(),
                    k -> new long[2])[0] += l.getQuantite();
            }
        }

        List<FicheSyntheseResponse.LigneFicheSynthese> lignes = ventesParProduit.entrySet().stream()
            .map(e -> {
                String code = e.getKey();
                long qteVendue = e.getValue()[0];
                long stockTotal = stockParProduit.getOrDefault(code, 0L);
                return new FicheSyntheseResponse.LigneFicheSynthese(
                    code, qteVendue, Math.max(0, stockTotal - qteVendue), 0,
                    stockTotal, qteVendue, Math.max(0, stockTotal - qteVendue), 0, qteVendue
                );
            })
            .sorted(Comparator.comparing(FicheSyntheseResponse.LigneFicheSynthese::codeProduit))
            .toList();

        return new FicheSyntheseResponse(
            today, caTotal * 0.55, caTotal * 0.45, caTotal,
            commerciaux.size(), vacInit, caTotal, vac, recouvrements,
            vteCpt, Math.max(0, vacInit - vteCpt - vac), 0,
            0, 0, 0, 0, sCaisse, 0, 0, lignes
        );
    }
}
