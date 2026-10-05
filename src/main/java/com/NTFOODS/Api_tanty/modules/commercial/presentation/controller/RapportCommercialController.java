package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase.CalculerPrimesHebdoUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase.GenererRapportRendementCarburantUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase.GenererRapportTourneeUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase.GenererRapportVentesUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commercial/rapports")
@Tag(name = "Commercial — Rapports", description = "Rapports commerciaux exportables")
@SecurityRequirement(name = "bearerAuth")
public class RapportCommercialController {

    private final GenererRapportVentesUseCase genererRapportVentesUseCase;
    private final CalculerPrimesHebdoUseCase calculerPrimesUseCase;
    private final VenteRepository venteRepository;
    private final GenererRapportRendementCarburantUseCase rapportRendementCarburantUseCase;
    private final GenererRapportTourneeUseCase rapportTourneeUseCase;

    public RapportCommercialController(GenererRapportVentesUseCase genererRapportVentesUseCase,
                                        CalculerPrimesHebdoUseCase calculerPrimesUseCase,
                                        VenteRepository venteRepository,
                                        GenererRapportRendementCarburantUseCase rapportRendementCarburantUseCase,
                                        GenererRapportTourneeUseCase rapportTourneeUseCase) {
        this.genererRapportVentesUseCase = genererRapportVentesUseCase;
        this.calculerPrimesUseCase = calculerPrimesUseCase;
        this.venteRepository = venteRepository;
        this.rapportRendementCarburantUseCase = rapportRendementCarburantUseCase;
        this.rapportTourneeUseCase = rapportTourneeUseCase;
    }

    @GetMapping("/ventes")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Rapport des ventes (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> ventes(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier f = genererRapportVentesUseCase.executer(debut, fin, FormatRapport.depuis(format));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(f.contentType()))
            .body(f.contenu());
    }

    @GetMapping("/primes")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Calcul des primes hebdomadaires (RG-10)",
        description = "Eligible si taux global >= 80% (sinon prime nulle) ; puis 3000 CFA par gamme >= 75%, plafonne a 12000 CFA, payee moitie cash moitie nature. Seules les journees ayant atteint 60% de l'objectif quotidien comptent dans les totaux.")
    public ResponseEntity<ApiResponse<List<CalculerPrimesHebdoUseCase.PrimeResultat>>> primes(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semaineDebut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semaineFin) {
        var resultats = calculerPrimesUseCase.executer(semaineDebut, semaineFin);
        return ResponseEntity.ok(ApiResponse.succes(resultats, resultats.size() + " résultat(s)"));
    }

    @GetMapping("/comparatif-n-vs-n1")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Comparatif N vs N-1 par commercial",
        description = "Compare le CA du mois actuel vs même mois de l'année précédente par commercial.")
    public ResponseEntity<ApiResponse<List<ComparatifNvsN1Response>>> comparatifNvsN1(
        @RequestParam int annee,
        @RequestParam int mois) {
        LocalDate debutMoisN = LocalDate.of(annee, mois, 1);
        LocalDate finMoisN = debutMoisN.withDayOfMonth(debutMoisN.lengthOfMonth());
        LocalDate debutMoisN1 = LocalDate.of(annee - 1, mois, 1);
        LocalDate finMoisN1 = debutMoisN1.withDayOfMonth(debutMoisN1.lengthOfMonth());

        var ventesN = venteRepository.listerParPeriode(debutMoisN, finMoisN);
        var ventesN1 = venteRepository.listerParPeriode(debutMoisN1, finMoisN1);

        Map<String, Double> caParCommercialN = ventesN.stream()
            .collect(Collectors.groupingBy(
                v -> v.getMatriculeCommercial(),
                Collectors.summingDouble(Vente::montantTotal)));
        Map<String, Double> caParCommercialN1 = ventesN1.stream()
            .collect(Collectors.groupingBy(
                v -> v.getMatriculeCommercial(),
                Collectors.summingDouble(Vente::montantTotal)));

        Set<String> tousCommerciaux = new java.util.LinkedHashSet<>();
        tousCommerciaux.addAll(caParCommercialN.keySet());
        tousCommerciaux.addAll(caParCommercialN1.keySet());

        List<ComparatifNvsN1Response> resultats = tousCommerciaux.stream().map(matricule -> {
            double caN = caParCommercialN.getOrDefault(matricule, 0.0);
            double caN1 = caParCommercialN1.getOrDefault(matricule, 0.0);
            double evolution = caN1 > 0 ? ((caN / caN1) - 1) * 100.0 : 0.0;
            return new ComparatifNvsN1Response(matricule, caN, caN1, evolution);
        }).sorted((a, b) -> Double.compare(b.evolutionPourcent(), a.evolutionPourcent())).toList();

        return ResponseEntity.ok(ApiResponse.succes(resultats, resultats.size() + " commercial(aux)"));
    }

    @GetMapping("/rendement-carburant")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Rapport rendement carburant (CA / litres consommés)",
        description = "Calcule le rendement de chaque commercial: CA généré par litre de carburant consommé.")
    public ResponseEntity<ApiResponse<List<GenererRapportRendementCarburantUseCase.LigneRendement>>> rendementCarburant(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        var resultats = rapportRendementCarburantUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(resultats, resultats.size() + " commercial(aux)"));
    }

    @GetMapping("/tournees")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Rapport de performance des tournées",
        description = "Taux de visite par commercial et taux global sur la période.")
    public ResponseEntity<ApiResponse<GenererRapportTourneeUseCase.RapportTournee>> rapportTournees(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        var rapport = rapportTourneeUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(rapport, "Rapport généré"));
    }

    public record ComparatifNvsN1Response(String matriculeCommercial, double caMoisActuel,
        double caMoisN1, double evolutionPourcent) {}
}
