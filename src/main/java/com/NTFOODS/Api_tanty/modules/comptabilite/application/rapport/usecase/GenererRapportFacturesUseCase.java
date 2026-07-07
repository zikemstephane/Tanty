package com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.dto.response.FactureResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.mapper.FactureMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.repository.FactureRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/** Use Case — rapport "Factures" sur une période, exportable PDF/Excel/CSV/JSON. */
@Service
@Transactional(readOnly = true)
public class GenererRapportFacturesUseCase {

    private static final List<String> ENTETES = List.of(
        "N° Facture", "Type", "Client", "Date", "Statut", "Total HT", "Total TVA", "Total TTC");

    private final FactureRepository factureRepository;
    private final FactureMapper factureMapper;
    private final TableExporter tableExporter;

    public GenererRapportFacturesUseCase(FactureRepository factureRepository, FactureMapper factureMapper,
                                         TableExporter tableExporter) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format) {
        List<FactureResponse> factures = factureRepository.listerParPeriode(debut, fin).stream()
            .map(factureMapper::versResponse).toList();
        String titre = "Factures";
        String base = "factures-" + LocalDate.now();
        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(factures));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(factures)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(factures)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(factures)));
        };
    }

    private List<List<String>> lignes(List<FactureResponse> factures) {
        return factures.stream().map(f -> List.of(
            f.numeroFacture(), f.typeFacture(), f.codeClient(), String.valueOf(f.dateEmission()),
            f.statut(), String.valueOf(f.montantTotalHT()), String.valueOf(f.montantTotalTVA()),
            String.valueOf(f.montantTotalTTC())
        )).toList();
    }
}
