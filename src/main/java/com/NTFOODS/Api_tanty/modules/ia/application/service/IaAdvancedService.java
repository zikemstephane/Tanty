package com.NTFOODS.Api_tanty.modules.ia.application.service;

import com.NTFOODS.Api_tanty.modules.ia.application.dto.*;
import com.NTFOODS.Api_tanty.modules.ia.config.IaConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * IaAdvancedService — Service IA avancé utilisant WebClient (reactif, non-bloquant).
 *
 * Fonctionnalités innovantes :
 * 1. Analyse prédictive de rupture (modèle IA avancé)
 * 2. Optimisation automatique des seuils DG
 * 3. Détection d'anomalies intelligentes
 * 4. Prédiction de demande saisonnière
 * 5. Génération de rapports intelligents multi-sections
 * 6. Streaming SSE pour réponses en temps réel
 *
 * Inspiré de TSFConsult (WebClient + IA) mais largement étendu
 * pour offrir des capacités prédictives et analytiques avancées.
 */
@Service
public class IaAdvancedService {

    private static final Logger log = LoggerFactory.getLogger(IaAdvancedService.class);

    private final IaConfig iaConfig;
    private final WebClient webClient;
    private final IaPromptRegistry promptRegistry;
    private final ObjectMapper mapper = new ObjectMapper();

    public IaAdvancedService(IaConfig iaConfig,
                             @Qualifier("iaWebClient") WebClient webClient,
                             IaPromptRegistry promptRegistry) {
        this.iaConfig = iaConfig;
        this.webClient = webClient;
        this.promptRegistry = promptRegistry;
    }

    // ═══════════════════════════════════════════════════════════
    //  1. ANALYSE PRÉDICTIVE DE RUPTURE
    // ═══════════════════════════════════════════════════════════

    public PredictiveResponse predictRuptures(PredictiveRequest request) {
        if (!iaConfig.isConfigured()) {
            return fallbackPredictive(request);
        }
        try {
            String prompt = buildPredictivePrompt(request);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content",
                    promptRegistry.getSystemPrompt(request.getDomain()) +
                    "\nTu es un expert en analyse prédictive de stock. Réponds UNIQUEMENT en JSON valide."));
            messages.add(Map.of("role", "user", "content", prompt));

            String aiReply = callOpenRouterReactive(messages, 0.2, iaConfig.getModel());
            return parsePredictiveResponse(aiReply);
        } catch (Exception e) {
            log.error("IA predictive analysis error: {}", e.getMessage());
            return fallbackPredictive(request);
        }
    }

    private String buildPredictivePrompt(PredictiveRequest req) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append("Analyse les données suivantes et fournis des prédictions de rupture de stock pour les ")
          .append(req.getHorizonDays()).append(" prochains jours.\n\n");
        sb.append("Entrepôt: ").append(req.getWarehouseName() != null ? req.getWarehouseName() : "Tous").append("\n\n");

        if (req.getStockData() != null && !req.getStockData().isEmpty()) {
            sb.append("Niveaux de stock actuels:\n").append(mapper.writeValueAsString(req.getStockData())).append("\n\n");
        }
        if (req.getMovementHistory() != null && !req.getMovementHistory().isEmpty()) {
            sb.append("Historique des mouvements (entrées/sorties):\n")
              .append(mapper.writeValueAsString(req.getMovementHistory())).append("\n\n");
        }
        if (req.getSeasonalData() != null && !req.getSeasonalData().isEmpty()) {
            sb.append("Données saisonnières:\n").append(mapper.writeValueAsString(req.getSeasonalData())).append("\n\n");
        }

        sb.append("Réponds UNIQUEMENT en JSON valide avec cette structure:\n");
        sb.append("{\n");
        sb.append("  \"summary\": \"résumé exécutif de l'analyse\",\n");
        sb.append("  \"rupturePredictions\": [{\n");
        sb.append("    \"productSku\": \"SKU\", \"productName\": \"nom\", \"warehouse\": \"nom\",\n");
        sb.append("    \"daysUntilRupture\": 0, \"probability\": 0.0,\n");
        sb.append("    \"severity\": \"HIGH|MEDIUM|LOW\", \"recommendedAction\": \"action\",\n");
        sb.append("    \"suggestedQuantity\": 0\n");
        sb.append("  }],\n");
        sb.append("  \"seasonalPatterns\": [{\n");
        sb.append("    \"productSku\": \"SKU\", \"pattern\": \"description\",\n");
        sb.append("    \"peakMonth\": \"mois\", \"lowMonth\": \"mois\",\n");
        sb.append("    \"variationPercent\": 0.0, \"recommendation\": \"recommandation\"\n");
        sb.append("  }],\n");
        sb.append("  \"optimizedThresholds\": [{\n");
        sb.append("    \"productSku\": \"SKU\", \"currentReorderPoint\": 0,\n");
        sb.append("    \"suggestedReorderPoint\": 0, \"currentSafetyStock\": 0,\n");
        sb.append("    \"suggestedSafetyStock\": 0, \"reason\": \"justification\"\n");
        sb.append("  }],\n");
        sb.append("  \"anomalies\": [{\n");
        sb.append("    \"type\": \"OVERSTOCK|UNDERSTOCK|IRREGULAR|STALE\",\n");
        sb.append("    \"productSku\": \"SKU\", \"description\": \"description\",\n");
        sb.append("    \"severity\": 0.0, \"detectedPattern\": \"pattern\",\n");
        sb.append("    \"suggestedAction\": \"action\"\n");
        sb.append("  }]\n");
        sb.append("}");
        return sb.toString();
    }

    private PredictiveResponse parsePredictiveResponse(String aiReply) {
        PredictiveResponse resp = new PredictiveResponse();
        try {
            String json = extractJson(aiReply);
            JsonNode root = mapper.readTree(json);
            resp.setSummary(root.path("summary").asText("Analyse prédictive terminée"));

            List<PredictiveResponse.RupturePrediction> ruptures = new ArrayList<>();
            JsonNode rupturesNode = root.path("rupturePredictions");
            if (rupturesNode.isArray()) {
                rupturesNode.forEach(n -> {
                    PredictiveResponse.RupturePrediction rp = new PredictiveResponse.RupturePrediction();
                    rp.setProductSku(n.path("productSku").asText(""));
                    rp.setProductName(n.path("productName").asText(""));
                    rp.setWarehouse(n.path("warehouse").asText(""));
                    rp.setDaysUntilRupture(n.path("daysUntilRupture").asInt(0));
                    rp.setProbability(n.path("probability").asDouble(0));
                    rp.setSeverity(n.path("severity").asText("MEDIUM"));
                    rp.setRecommendedAction(n.path("recommendedAction").asText(""));
                    rp.setSuggestedQuantity(n.path("suggestedQuantity").asDouble(0));
                    ruptures.add(rp);
                });
            }
            resp.setRupturePredictions(ruptures);

            List<PredictiveResponse.SeasonalPattern> patterns = new ArrayList<>();
            JsonNode patternsNode = root.path("seasonalPatterns");
            if (patternsNode.isArray()) {
                patternsNode.forEach(n -> {
                    PredictiveResponse.SeasonalPattern sp = new PredictiveResponse.SeasonalPattern();
                    sp.setProductSku(n.path("productSku").asText(""));
                    sp.setPattern(n.path("pattern").asText(""));
                    sp.setPeakMonth(n.path("peakMonth").asText(""));
                    sp.setLowMonth(n.path("lowMonth").asText(""));
                    sp.setVariationPercent(n.path("variationPercent").asDouble(0));
                    sp.setRecommendation(n.path("recommendation").asText(""));
                    patterns.add(sp);
                });
            }
            resp.setSeasonalPatterns(patterns);

            List<Map<String, Object>> thresholds = new ArrayList<>();
            JsonNode thresholdsNode = root.path("optimizedThresholds");
            if (thresholdsNode.isArray()) {
                thresholdsNode.forEach(n -> {
                    Map<String, Object> t = new java.util.HashMap<>();
                    n.fields().forEachRemaining(e -> {
                        if (e.getValue().isNumber()) t.put(e.getKey(), e.getValue().asDouble());
                        else t.put(e.getKey(), e.getValue().asText());
                    });
                    thresholds.add(t);
                });
            }
            resp.setOptimizedThresholds(thresholds);

            List<PredictiveResponse.AnomalyDetection> anomalies = new ArrayList<>();
            JsonNode anomaliesNode = root.path("anomalies");
            if (anomaliesNode.isArray()) {
                anomaliesNode.forEach(n -> {
                    PredictiveResponse.AnomalyDetection ad = new PredictiveResponse.AnomalyDetection();
                    ad.setType(n.path("type").asText("IRREGULAR"));
                    ad.setProductSku(n.path("productSku").asText(""));
                    ad.setDescription(n.path("description").asText(""));
                    ad.setSeverity(n.path("severity").asDouble(0));
                    ad.setDetectedPattern(n.path("detectedPattern").asText(""));
                    ad.setSuggestedAction(n.path("suggestedAction").asText(""));
                    anomalies.add(ad);
                });
            }
            resp.setAnomalies(anomalies);

            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        } catch (Exception e) {
            log.warn("Failed to parse predictive JSON: {}", e.getMessage());
            resp.setSummary(aiReply);
            resp.setRupturePredictions(List.of());
            resp.setSeasonalPatterns(List.of());
            resp.setOptimizedThresholds(List.of());
            resp.setAnomalies(List.of());
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        }
        return resp;
    }

    private PredictiveResponse fallbackPredictive(PredictiveRequest request) {
        PredictiveResponse resp = new PredictiveResponse();
        resp.setSummary("Analyse prédictive en mode hors-ligne. Configurez OPENAI_API_KEY pour l'IA avancée.");
        resp.setRupturePredictions(List.of());
        resp.setSeasonalPatterns(List.of());
        resp.setOptimizedThresholds(List.of());
        resp.setAnomalies(List.of());
        resp.setModel("fallback");
        resp.setUsingFallback(true);
        return resp;
    }

    // ═══════════════════════════════════════════════════════════
    //  2. RAPPORT INTELLIGENT MULTI-SECTIONS
    // ═══════════════════════════════════════════════════════════

    public IntelligentReportResponse generateIntelligentReport(IntelligentReportRequest request) {
        if (!iaConfig.isConfigured()) {
            return fallbackIntelligentReport(request);
        }
        try {
            String prompt = buildReportPrompt(request);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content",
                    promptRegistry.getSystemPrompt(request.getDomain()) +
                    "\nTu es un expert en génération de rapports analytiques. Réponds UNIQUEMENT en JSON valide."));
            messages.add(Map.of("role", "user", "content", prompt));

            String aiReply = callOpenRouterReactive(messages, 0.3, iaConfig.getModel());
            return parseIntelligentReportResponse(aiReply, request);
        } catch (Exception e) {
            log.error("IA intelligent report error: {}", e.getMessage());
            return fallbackIntelligentReport(request);
        }
    }

    private String buildReportPrompt(IntelligentReportRequest req) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append("Génère un rapport intelligent détaillé pour le module: ").append(req.getReportType()).append("\n");
        sb.append("Période: ").append(req.getPeriod() != null ? req.getPeriod() : "30 derniers jours").append("\n\n");

        if (req.getStockData() != null) {
            sb.append("Données stock:\n").append(mapper.writeValueAsString(req.getStockData())).append("\n\n");
        }
        if (req.getMovementData() != null) {
            sb.append("Mouvements:\n").append(mapper.writeValueAsString(req.getMovementData())).append("\n\n");
        }
        if (req.getAlertData() != null) {
            sb.append("Alertes:\n").append(mapper.writeValueAsString(req.getAlertData())).append("\n\n");
        }
        if (req.getFinancialData() != null) {
            sb.append("Données financières:\n").append(mapper.writeValueAsString(req.getFinancialData())).append("\n\n");
        }

        sb.append("Réponds UNIQUEMENT en JSON valide:\n");
        sb.append("{\n");
        sb.append("  \"title\": \"titre du rapport\",\n");
        sb.append("  \"executiveSummary\": \"résumé exécutif en 3-4 lignes\",\n");
        sb.append("  \"markdownReport\": \"rapport complet en Markdown\",\n");
        sb.append("  \"sections\": [{\"heading\":\"titre\",\"content\":\"contenu\",\"icon\":\"fa-icon\",\"severity\":\"INFO|WARNING|CRITICAL|SUCCESS\"}],\n");
        sb.append("  \"keyMetrics\": [{\"label\":\"nom\",\"value\":0,\"unit\":\"unité\",\"trend\":\"UP|DOWN|STABLE\",\"change\":0}],\n");
        sb.append("  \"riskMatrix\": [{\"risk\":\"nom\",\"probability\":0,\"impact\":0,\"level\":\"LOW|MEDIUM|HIGH\",\"mitigation\":\"action\"}],\n");
        sb.append("  \"actionItems\": [\"action 1\", \"action 2\"]\n");
        sb.append("}");
        return sb.toString();
    }

    private IntelligentReportResponse parseIntelligentReportResponse(String aiReply, IntelligentReportRequest req) {
        IntelligentReportResponse resp = new IntelligentReportResponse();
        try {
            String json = extractJson(aiReply);
            JsonNode root = mapper.readTree(json);
            resp.setTitle(root.path("title").asText("Rapport"));
            resp.setExecutiveSummary(root.path("executiveSummary").asText(""));
            resp.setMarkdownReport(root.path("markdownReport").asText(aiReply));

            List<IntelligentReportResponse.ReportSection> sections = new ArrayList<>();
            JsonNode sectionsNode = root.path("sections");
            if (sectionsNode.isArray()) {
                sectionsNode.forEach(n -> {
                    IntelligentReportResponse.ReportSection s = new IntelligentReportResponse.ReportSection();
                    s.setHeading(n.path("heading").asText(""));
                    s.setContent(n.path("content").asText(""));
                    s.setIcon(n.path("icon").asText("fa-file"));
                    s.setSeverity(n.path("severity").asText("INFO"));
                    sections.add(s);
                });
            }
            resp.setSections(sections);

            List<Map<String, Object>> metrics = new ArrayList<>();
            JsonNode metricsNode = root.path("keyMetrics");
            if (metricsNode.isArray()) {
                metricsNode.forEach(n -> {
                    Map<String, Object> m = new java.util.HashMap<>();
                    n.fields().forEachRemaining(e -> {
                        if (e.getValue().isNumber()) m.put(e.getKey(), e.getValue().asDouble());
                        else m.put(e.getKey(), e.getValue().asText());
                    });
                    metrics.add(m);
                });
            }
            resp.setKeyMetrics(metrics);

            List<Map<String, Object>> risks = new ArrayList<>();
            JsonNode risksNode = root.path("riskMatrix");
            if (risksNode.isArray()) {
                risksNode.forEach(n -> {
                    Map<String, Object> r = new java.util.HashMap<>();
                    n.fields().forEachRemaining(e -> {
                        if (e.getValue().isNumber()) r.put(e.getKey(), e.getValue().asDouble());
                        else r.put(e.getKey(), e.getValue().asText());
                    });
                    risks.add(r);
                });
            }
            resp.setRiskMatrix(risks);

            List<String> actions = new ArrayList<>();
            JsonNode actionsNode = root.path("actionItems");
            if (actionsNode.isArray()) {
                actionsNode.forEach(n -> actions.add(n.asText()));
            }
            resp.setActionItems(actions);

            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        } catch (Exception e) {
            log.warn("Failed to parse intelligent report JSON: {}", e.getMessage());
            resp.setTitle("Rapport");
            resp.setExecutiveSummary(aiReply);
            resp.setMarkdownReport(aiReply);
            resp.setSections(List.of());
            resp.setKeyMetrics(List.of());
            resp.setRiskMatrix(List.of());
            resp.setActionItems(List.of());
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        }
        return resp;
    }

    private IntelligentReportResponse fallbackIntelligentReport(IntelligentReportRequest req) {
        IntelligentReportResponse resp = new IntelligentReportResponse();
        resp.setTitle("Rapport " + (req.getReportType() != null ? req.getReportType() : "Stock"));
        resp.setExecutiveSummary("Rapport généré en mode hors-ligne. Configurez OPENAI_API_KEY pour l'IA avancée.");
        resp.setMarkdownReport("# Rapport\n\nMode hors-ligne — configurez l'IA pour un rapport complet.");
        resp.setSections(List.of());
        resp.setKeyMetrics(List.of());
        resp.setRiskMatrix(List.of());
        resp.setActionItems(List.of());
        resp.setModel("fallback");
        resp.setUsingFallback(true);
        return resp;
    }

    // ═══════════════════════════════════════════════════════════
    //  3. STREAMING SSE — Réponses IA en temps réel
    // ═══════════════════════════════════════════════════════════

    public Flux<String> streamChat(ChatRequest request) {
        if (!iaConfig.isConfigured()) {
            return Flux.just("Mode hors-ligne — configurez OPENAI_API_KEY pour le streaming IA.");
        }
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", iaConfig.getModel());
            body.put("temperature", 0.7);
            body.put("max_tokens", iaConfig.getMaxTokens());
            body.put("stream", true);

            ArrayNode messagesNode = body.putArray("messages");
            ObjectNode sysMsg = messagesNode.addObject();
            sysMsg.put("role", "system");
            sysMsg.put("content", promptRegistry.getSystemPrompt(request.getDomain()));
            ObjectNode userMsg = messagesNode.addObject();
            userMsg.put("role", "user");
            String content = request.getMessage();
            if (request.getContext() != null && !request.getContext().isBlank()) {
                content = "Contexte:\n" + request.getContext() + "\n\nQuestion: " + request.getMessage();
            }
            userMsg.put("content", content);

            return webClient.post()
                    .bodyValue(body.toString())
                    .retrieve()
                    .bodyToFlux(String.class)
                    .filter(line -> !line.isBlank() && !line.equals("[DONE]"))
                    .map(line -> {
                        try {
                            JsonNode root = mapper.readTree(line);
                            JsonNode delta = root.path("choices").get(0).path("delta").path("content");
                            return delta.isMissingNode() ? "" : delta.asText();
                        } catch (Exception e) {
                            return "";
                        }
                    })
                    .filter(chunk -> !chunk.isEmpty())
                    .onErrorResume(e -> {
                        log.error("IA streaming error: {}", e.getMessage());
                        return Flux.just("\n[Erreur de streaming IA: " + e.getMessage() + "]");
                    });
        } catch (Exception e) {
            log.error("IA stream setup error: {}", e.getMessage());
            return Flux.just("[Erreur: " + e.getMessage() + "]");
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  PRIVATE: Reactive OpenRouter call via WebClient
    // ═══════════════════════════════════════════════════════════

    private String callOpenRouterReactive(List<Map<String, String>> messages, double temperature, String model) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", model);
            body.put("temperature", temperature);
            body.put("max_tokens", iaConfig.getMaxTokens());

            ArrayNode messagesNode = body.putArray("messages");
            for (Map<String, String> msg : messages) {
                ObjectNode msgNode = messagesNode.addObject();
                msgNode.put("role", msg.get("role"));
                msgNode.put("content", msg.get("content"));
            }

            String response = webClient.post()
                    .bodyValue(body.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = mapper.readTree(response);
            JsonNode choices = root.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                String content = choices.get(0).path("message").path("content").asText();
                return content != null ? content.replaceAll("(?s)<think>.*?</think>", "").trim() : "";
            }
            throw new RuntimeException("No content in OpenRouter response");
        } catch (Exception e) {
            throw new RuntimeException("OpenRouter call failed: " + e.getMessage(), e);
        }
    }

    private String extractJson(String text) {
        if (text == null || text.isBlank()) return text;
        String cleaned = text.trim();
        if (cleaned.startsWith("```")) {
            int firstNewline = cleaned.indexOf('\n');
            if (firstNewline > 0) cleaned = cleaned.substring(firstNewline + 1);
            if (cleaned.endsWith("```")) cleaned = cleaned.substring(0, cleaned.length() - 3);
            cleaned = cleaned.trim();
        }
        cleaned = cleaned.replaceAll("(?s)<think>.*?</think>", "").trim();
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }
}
