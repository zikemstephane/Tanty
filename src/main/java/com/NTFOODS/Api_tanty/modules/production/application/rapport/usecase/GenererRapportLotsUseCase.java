package com.NTFOODS.Api_tanty.modules.production.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.lot.dto.response.LotResponse;
import com.NTFOODS.Api_tanty.modules.production.application.lot.mapper.LotMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — rapport "Lots de production" sur une période, exportable PDF/Excel/CSV/JSON.
 */
@Service
@Transactional(readOnly = true)
public class GenererRapportLotsUseCase {

    private static final List<String> ENTETES = List.of(
        "N° Lot", "PPH", "SKU", "Désignation", "Qté (kg)", "Unités équiv.",
        "Conditionnement", "Qté cond.", "Production", "DLC", "Statut");

    private final LotRepository lotRepository;
    private final LotMapper lotMapper;
    private final TableExporter tableExporter;

    public GenererRapportLotsUseCase(LotRepository lotRepository, LotMapper lotMapper, TableExporter tableExporter) {
        this.lotRepository = lotRepository;
        this.lotMapper = lotMapper;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format) {
        List<LotResponse> lots = lotRepository.listerParPeriode(debut, fin).stream().map(lotMapper::versResponse).toList();
        String titre = "Lots de production";
        String base = "lots-production-" + LocalDate.now();

        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(lots));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(lots)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(lots)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(lots)));
        };
    }

    private List<List<String>> lignes(List<LotResponse> lots) {
        return lots.stream().map(l -> List.of(
            l.numeroLot(), n(l.referencePPH()), l.productSku(), n(l.productName()),
            String.valueOf(l.declaredQuantityKg()), String.valueOf(l.equivalentUnits()),
            n(l.conditioningType()), String.valueOf(l.conditioningQty()),
            String.valueOf(l.dateProduction()), String.valueOf(l.dlc()), l.statut()
        )).toList();
    }

    private String n(String v) { return v == null ? "" : v; }
}
