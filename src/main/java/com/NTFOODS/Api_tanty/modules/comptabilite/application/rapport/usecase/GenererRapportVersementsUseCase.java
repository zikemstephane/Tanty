package com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response.VersementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.mapper.VersementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GenererRapportVersementsUseCase {

    private static final List<String> ENTETES = List.of(
        "Référence", "Commercial", "Date", "Montant Attendu", "Cash Versé", "Écart", "Alerte", "Statut");

    private final VersementRepository versementRepository;
    private final VersementMapper versementMapper;
    private final TableExporter tableExporter;

    public GenererRapportVersementsUseCase(VersementRepository versementRepository,
                                             VersementMapper versementMapper,
                                             TableExporter tableExporter) {
        this.versementRepository = versementRepository;
        this.versementMapper = versementMapper;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format) {
        List<VersementResponse> versements = versementRepository.listerParPeriode(debut, fin).stream()
            .map(versementMapper::versResponse).toList();
        String titre = "Versements";
        String base = "versements-" + LocalDate.now();
        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(versements));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(versements)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(versements)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(versements)));
        };
    }

    private List<List<String>> lignes(List<VersementResponse> versements) {
        return versements.stream().map(v -> List.of(
            v.referenceVersement(), v.matriculeCommercial(), String.valueOf(v.date()),
            String.valueOf(v.montantAttendu()), String.valueOf(v.cashVerse()),
            String.valueOf(v.ecart()), v.alerteRouge() ? "OUI" : "NON", v.statut()
        )).toList();
    }
}
