package com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.dto.response.PrimeResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.mapper.PrimeMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.PrimeRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GenererRapportPrimesUseCase {

    private static final List<String> ENTETES = List.of(
        "Référence", "Commercial", "Semaine Début", "Semaine Fin", "Objectif Global",
        "Ventes Globales", "Taux Global %", "Éligible", "Montant Prime", "Montant Cash", "Montant Nature", "Statut");

    private final PrimeRepository primeRepository;
    private final PrimeMapper primeMapper;
    private final TableExporter tableExporter;

    public GenererRapportPrimesUseCase(PrimeRepository primeRepository,
                                         PrimeMapper primeMapper,
                                         TableExporter tableExporter) {
        this.primeRepository = primeRepository;
        this.primeMapper = primeMapper;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(LocalDate semaineDebut, FormatRapport format) {
        List<PrimeResponse> primes = primeRepository.listerParSemaine(semaineDebut).stream()
            .map(primeMapper::versResponse).toList();
        String titre = "Primes — semaine du " + semaineDebut;
        String base = "primes-" + semaineDebut;
        return switch (format) {
            case JSON -> new RapportFichier(base + ".json", format.getContentType(), tableExporter.versJson(primes));
            case CSV -> new RapportFichier(base + ".csv", format.getContentType(), tableExporter.versCsv(ENTETES, lignes(primes)));
            case PDF -> new RapportFichier(base + ".pdf", format.getContentType(), tableExporter.versPdf(titre, ENTETES, lignes(primes)));
            case XLSX -> new RapportFichier(base + ".xlsx", format.getContentType(), tableExporter.versExcel(titre, ENTETES, lignes(primes)));
        };
    }

    private List<List<String>> lignes(List<PrimeResponse> primes) {
        return primes.stream().map(p -> List.of(
            p.referencePrime(), p.matriculeCommercial(), String.valueOf(p.semaineDebut()),
            String.valueOf(p.semaineFin()), String.valueOf(p.objectifGlobalFCFA()),
            String.valueOf(p.totalVentesGlobalFCFA()), String.valueOf(p.tauxRealisationGlobalPourcent()),
            p.eligible() ? "OUI" : "NON", String.valueOf(p.montantPrimeFCFA()),
            String.valueOf(p.montantCashFCFA()), String.valueOf(p.montantNatureFCFA()), p.statut()
        )).toList();
    }
}
