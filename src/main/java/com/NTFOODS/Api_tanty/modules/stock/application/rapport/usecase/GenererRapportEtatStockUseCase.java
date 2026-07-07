package com.NTFOODS.Api_tanty.modules.stock.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterEtatStockUseCase;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — rapport "État des stocks" exportable en PDF / Excel / CSV / JSON.
 */
@Service
@Transactional(readOnly = true)
public class GenererRapportEtatStockUseCase {

    private static final List<String> ENTETES = List.of(
        "Code produit", "Désignation", "Marque", "Gamme", "Entrepôt", "Type", "Quantité (base)");

    private final ConsulterEtatStockUseCase consulterEtatStockUseCase;
    private final TableExporter tableExporter;

    public GenererRapportEtatStockUseCase(ConsulterEtatStockUseCase consulterEtatStockUseCase, TableExporter tableExporter) {
        this.consulterEtatStockUseCase = consulterEtatStockUseCase;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(String entrepot, String produit, String type, FormatRapport format) {
        List<NiveauStockResponse> etat = consulterEtatStockUseCase.executer(entrepot, produit, type);
        String titre = "Etat des stocks";
        String base = "etat-stocks-" + LocalDate.now();

        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(etat));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(),
                tableExporter.versCsv(ENTETES, lignes(etat)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(),
                tableExporter.versPdf(titre, ENTETES, lignes(etat)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(),
                tableExporter.versExcel(titre, ENTETES, lignes(etat)));
        };
    }

    private List<List<String>> lignes(List<NiveauStockResponse> etat) {
        return etat.stream().map(n -> List.of(
            n.codeProduit(),
            n.designation() == null ? "" : n.designation(),
            n.marque() == null ? "" : n.marque(),
            n.gamme() == null ? "" : n.gamme(),
            n.codeEntrepot(),
            n.typeEntrepot(),
            String.valueOf(n.quantiteEnBase())
        )).toList();
    }
}
