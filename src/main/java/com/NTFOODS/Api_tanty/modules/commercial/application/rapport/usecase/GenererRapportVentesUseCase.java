package com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.application.vente.dto.response.VenteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.mapper.VenteMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/** Use Case — rapport "Ventes" sur une période, exportable PDF/Excel/CSV/JSON. */
@Service
@Transactional(readOnly = true)
public class GenererRapportVentesUseCase {

    private static final List<String> ENTETES = List.of(
        "N° Vente", "Commercial", "Client", "Date", "Règlement", "Statut", "Montant total");

    private final VenteRepository venteRepository;
    private final VenteMapper venteMapper;
    private final TableExporter tableExporter;

    public GenererRapportVentesUseCase(VenteRepository venteRepository, VenteMapper venteMapper, TableExporter tableExporter) {
        this.venteRepository = venteRepository;
        this.venteMapper = venteMapper;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format) {
        List<VenteResponse> ventes = venteRepository.listerParPeriode(debut, fin).stream().map(venteMapper::versResponse).toList();
        String titre = "Ventes";
        String base = "ventes-" + LocalDate.now();
        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(ventes));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(ventes)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(ventes)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(ventes)));
        };
    }

    private List<List<String>> lignes(List<VenteResponse> ventes) {
        return ventes.stream().map(v -> List.of(
            v.numeroVente(), v.matriculeCommercial(), v.codeClient(), String.valueOf(v.date()),
            v.modeReglement(), v.statut(), String.valueOf(v.montantTotal())
        )).toList();
    }
}
