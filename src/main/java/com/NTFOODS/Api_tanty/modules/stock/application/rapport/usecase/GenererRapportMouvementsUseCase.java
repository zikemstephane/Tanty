package com.NTFOODS.Api_tanty.modules.stock.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.dto.response.MouvementResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.usecase.lister.ListerMouvementsUseCase;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — rapport "Historique des mouvements" exportable.
 */
@Service
@Transactional(readOnly = true)
public class GenererRapportMouvementsUseCase {

    private static final List<String> ENTETES = List.of(
        "Référence", "Type", "Sens", "Code produit", "SKU", "Quantité", "Source", "Cible", "Réf. document", "Motif", "Acteur", "Date");

    private final ListerMouvementsUseCase listerMouvementsUseCase;
    private final TableExporter tableExporter;

    public GenererRapportMouvementsUseCase(ListerMouvementsUseCase listerMouvementsUseCase, TableExporter tableExporter) {
        this.listerMouvementsUseCase = listerMouvementsUseCase;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(String produit, String entrepot, String type, LocalDate debut, LocalDate fin,
                                   FormatRapport format) {
        List<MouvementResponse> mouvements = listerMouvementsUseCase.executer(produit, entrepot, type, null, debut, fin);
        String titre = "Historique des mouvements";
        String base = "mouvements-" + LocalDate.now();

        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(mouvements));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(mouvements)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(mouvements)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(mouvements)));
        };
    }

    private List<List<String>> lignes(List<MouvementResponse> mouvements) {
        return mouvements.stream().map(m -> java.util.Arrays.asList(
            m.reference(), m.typeLibelle(), m.sens(), m.codeProduit(),
            m.sku() == null ? "" : m.sku(), String.valueOf(m.quantiteEnBase()),
            m.codeEntrepotSource() == null ? "" : m.codeEntrepotSource(),
            m.codeEntrepotCible() == null ? "" : m.codeEntrepotCible(),
            m.referenceDocument() == null ? "" : m.referenceDocument(),
            m.motif() == null ? "" : m.motif(),
            m.matriculeActeur(), String.valueOf(m.horodatage())
        )).toList();
    }
}
