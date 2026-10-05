package com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.dto.response.DecaissementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.mapper.DecaissementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GenererRapportDecaissementsUseCase {

    private static final List<String> ENTETES = List.of(
        "N° Décaissement", "Type", "Statut", "Bénéficiaire", "Date", "Montant FCFA", "Motif");

    private final DecaissementRepository decaissementRepository;
    private final DecaissementMapper decaissementMapper;
    private final TableExporter tableExporter;

    public GenererRapportDecaissementsUseCase(DecaissementRepository decaissementRepository,
                                                DecaissementMapper decaissementMapper,
                                                TableExporter tableExporter) {
        this.decaissementRepository = decaissementRepository;
        this.decaissementMapper = decaissementMapper;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format) {
        List<DecaissementResponse> decaissements = decaissementRepository.listerParPeriode(debut, fin).stream()
            .map(decaissementMapper::versResponse).toList();
        String titre = "Décaissements";
        String base = "decaissements-" + LocalDate.now();
        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(decaissements));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(decaissements)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(decaissements)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(decaissements)));
        };
    }

    private List<List<String>> lignes(List<DecaissementResponse> decaissements) {
        return decaissements.stream().map(d -> List.of(
            d.numeroDecaissement(), d.typeDepense(), d.statut(), d.beneficiaire(),
            String.valueOf(d.dateDepense()), String.valueOf(d.montantFCFA()),
            d.motif() != null ? d.motif() : ""
        )).toList();
    }
}
