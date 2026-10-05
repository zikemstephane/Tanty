package com.NTFOODS.Api_tanty.modules.ia.infrastructure.web;

import com.NTFOODS.Api_tanty.modules.ia.application.dto.*;
import com.NTFOODS.Api_tanty.modules.ia.application.service.IaAdvancedService;
import com.NTFOODS.Api_tanty.modules.ia.application.service.IaDataAggregator;
import com.NTFOODS.Api_tanty.modules.ia.application.service.IaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ia")
@Tag(name = "IA — Intelligence Artificielle", description = "Chat, analyse, prédictions, rapports intelligents")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK','CHEF_PRODUCTION','DIRECTEUR_GENERAL','DIRECTEUR_COMMERCIAL','ADMIN','COMMERCIAL','CONTROLEUR_GENERAL','COMPTABLE')")
public class IaController {

    private static final Logger log = LoggerFactory.getLogger(IaController.class);
    private final IaService iaService;
    private final IaAdvancedService iaAdvancedService;
    private final IaDataAggregator dataAggregator;

    public IaController(IaService iaService, IaAdvancedService iaAdvancedService, IaDataAggregator dataAggregator) {
        this.iaService = iaService;
        this.iaAdvancedService = iaAdvancedService;
        this.dataAggregator = dataAggregator;
    }

    @GetMapping("/status")
    @Operation(summary = "Statut du service IA — configuration, modèle")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "configured", iaService.isConfigured(),
                "model", iaService.getModel(),
                "service", "TantyAI"
        ));
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat IA — conversation avec contexte métier")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getMessage().length() > 5000) {
            return ResponseEntity.badRequest().build();
        }
        log.info("IA chat request: {}", request.getMessage().substring(0, Math.min(100, request.getMessage().length())));
        return ResponseEntity.ok(iaService.chat(request));
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyse IA — analyse de données stock avec insights et recommandations")
    public ResponseEntity<AnalysisResponse> analyze(@RequestBody AnalysisRequest request) {
        log.info("IA analysis request: type={}", request.getAnalysisType());
        return ResponseEntity.ok(iaService.analyzeStockData(request));
    }

    @PostMapping("/meeting-report")
    @Operation(summary = "Compte-rendu de réunion généré par IA")
    public ResponseEntity<MeetingReportResponse> meetingReport(@RequestBody MeetingReportRequest request) {
        log.info("IA meeting report request: {}", request.getTitle());
        return ResponseEntity.ok(iaService.generateMeetingReport(request));
    }

    @PostMapping("/suggestions")
    @Operation(summary = "Suggestions proactives IA — recommandations personnalisées par rôle")
    public ResponseEntity<SuggestionResponse> suggestions(@RequestBody SuggestionRequest request) {
        log.info("IA suggestions request: domain={}, role={}", request.getDomain(), request.getRole());
        return ResponseEntity.ok(iaService.generateSuggestions(request));
    }

    // ═══ ADVANCED IA ENDPOINTS ═══

    @PostMapping("/predict")
    @Operation(summary = "Analyse prédictive — ruptures, saisonnalité, seuils optimisés")
    public ResponseEntity<PredictiveResponse> predict(@RequestBody PredictiveRequest request) {
        log.info("IA predictive analysis: type={}, horizon={}d", request.getPredictionType(), request.getHorizonDays());
        return ResponseEntity.ok(iaAdvancedService.predictRuptures(request));
    }

    @PostMapping("/intelligent-report")
    @Operation(summary = "Rapport intelligent multi-sections avec matrice de risques")
    public ResponseEntity<IntelligentReportResponse> intelligentReport(@RequestBody IntelligentReportRequest request) {
        log.info("IA intelligent report: type={}", request.getReportType());
        return ResponseEntity.ok(iaAdvancedService.generateIntelligentReport(request));
    }

    @PostMapping(value = "/stream-chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Chat IA en streaming SSE — réponses en temps réel")
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        log.info("IA streaming chat: {}", request.getMessage());
        return iaAdvancedService.streamChat(request);
    }

    // ═══ AUTO-CONTEXT ENDPOINTS — L'IA travaille avec les données réelles du module ═══

    @GetMapping("/auto-context")
    @Operation(summary = "Contexte automatique — agrège les données réelles de tous les modules pour l'IA")
    public ResponseEntity<Map<String, Object>> autoContext() {
        return ResponseEntity.ok(dataAggregator.aggregateFullContext());
    }

    @GetMapping("/anomalies")
    @Operation(summary = "Détection d'anomalies — surstock, sous-stock, stock dormant (données réelles)")
    public ResponseEntity<List<Map<String, Object>>> anomalies() {
        return ResponseEntity.ok(dataAggregator.detectAnomalies());
    }

    @PostMapping("/auto-analyze")
    @Operation(summary = "Analyse IA automatique — fetch les données réelles et lance l'analyse sans contexte frontend")
    public ResponseEntity<AnalysisResponse> autoAnalyze(@RequestBody(required = false) AnalysisRequest request) {
        if (request == null) {
            request = new AnalysisRequest();
        }
        if (request.getAnalysisType() == null || request.getAnalysisType().isBlank()) {
            request.setAnalysisType("global_overview");
        }
        if (request.getDomain() == null || request.getDomain().isBlank()) {
            request.setDomain("stock");
        }
        request.setPeriod("30 derniers jours");
        Map<String, Object> context = dataAggregator.aggregateFullContext();
        @SuppressWarnings("unchecked")
        Map<String, Object> stockData = (Map<String, Object>) context.get("stock");
        request.setStockData(stockData);
        log.info("IA auto-analyze: type={}, domain={}", request.getAnalysisType(), request.getDomain());
        return ResponseEntity.ok(iaService.analyzeStockData(request));
    }

    @PostMapping("/auto-suggestions")
    @Operation(summary = "Suggestions IA automatiques — basées sur les données réelles du stock")
    public ResponseEntity<SuggestionResponse> autoSuggestions(@RequestBody(required = false) SuggestionRequest request) {
        if (request == null) {
            request = new SuggestionRequest();
        }
        if (request.getDomain() == null || request.getDomain().isBlank()) {
            request.setDomain("stock");
        }
        if (request.getRole() == null || request.getRole().isBlank()) {
            request.setRole("gestionnaire de stock");
        }
        if (request.getMaxSuggestions() == null || request.getMaxSuggestions() <= 0) {
            request.setMaxSuggestions(5);
        }
        Map<String, Object> context = dataAggregator.aggregateFullContext();
        request.setData(context);
        log.info("IA auto-suggestions: domain={}, role={}", request.getDomain(), request.getRole());
        return ResponseEntity.ok(iaService.generateSuggestions(request));
    }

    @PostMapping("/auto-predict")
    @Operation(summary = "Prédiction IA automatique — utilise les données réelles de stock pour prévoir les ruptures")
    public ResponseEntity<PredictiveResponse> autoPredict(@RequestBody(required = false) PredictiveRequest request) {
        if (request == null) {
            request = new PredictiveRequest();
        }
        if (request.getDomain() == null || request.getDomain().isBlank()) {
            request.setDomain("predictif");
        }
        if (request.getPredictionType() == null || request.getPredictionType().isBlank()) {
            request.setPredictionType("rupture_stock");
        }
        if (request.getHorizonDays() <= 0) {
            request.setHorizonDays(30);
        }
        Map<String, Object> context = dataAggregator.aggregateStockContext();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> topProducts = (List<Map<String, Object>>) context.get("topProducts");
        if (topProducts != null) {
            request.setStockData(topProducts);
        }
        log.info("IA auto-predict: type={}, horizon={}d", request.getPredictionType(), request.getHorizonDays());
        return ResponseEntity.ok(iaAdvancedService.predictRuptures(request));
    }
}
