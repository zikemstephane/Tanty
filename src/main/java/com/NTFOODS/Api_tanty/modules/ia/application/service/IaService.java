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
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IaService {

    private static final Logger log = LoggerFactory.getLogger(IaService.class);

    private final IaConfig iaConfig;
    private final RestTemplate restTemplate;
    private final IaPromptRegistry promptRegistry;
    private final ObjectMapper mapper = new ObjectMapper();

    public IaService(IaConfig iaConfig,
                     @Qualifier("iaRestTemplate") RestTemplate restTemplate,
                     IaPromptRegistry promptRegistry) {
        this.iaConfig = iaConfig;
        this.restTemplate = restTemplate;
        this.promptRegistry = promptRegistry;
    }

    // ═══════════════════════════════════════════════════════════
    //  Public API
    // ═══════════════════════════════════════════════════════════

    public ChatResponse chat(ChatRequest request) {
        if (!iaConfig.isConfigured()) {
            return fallbackChat(request);
        }
        try {
            List<Map<String, String>> messages = buildChatMessages(request, promptRegistry.getSystemPrompt(request.getDomain()));
            String aiReply = callOpenRouter(messages, 0.7);
            return new ChatResponse(aiReply, iaConfig.getModel(), false);
        } catch (Exception e) {
            log.error("IA chat error: {}", e.getMessage());
            return fallbackChat(request);
        }
    }

    public AnalysisResponse analyzeStockData(AnalysisRequest request) {
        if (!iaConfig.isConfigured()) {
            return fallbackAnalysis(request);
        }
        try {
            String prompt = buildAnalysisPrompt(request);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", promptRegistry.getSystemPrompt(request.getDomain()) + "\nTu dois répondre en JSON valide."));
            messages.add(Map.of("role", "user", "content", prompt));

            String aiReply = callOpenRouter(messages, 0.3);
            return parseAnalysisResponse(aiReply);
        } catch (Exception e) {
            log.error("IA analysis error: {}", e.getMessage());
            return fallbackAnalysis(request);
        }
    }

    public MeetingReportResponse generateMeetingReport(MeetingReportRequest request) {
        if (!iaConfig.isConfigured()) {
            return fallbackMeetingReport(request);
        }
        try {
            String prompt = buildMeetingReportPrompt(request);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", promptRegistry.getSystemPrompt("rapport_reunion")));
            messages.add(Map.of("role", "user", "content", prompt));

            String aiReply = callOpenRouter(messages, 0.4);
            MeetingReportResponse resp = new MeetingReportResponse();
            resp.setContent(aiReply);
            resp.setFormattedReport(aiReply);
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
            return resp;
        } catch (Exception e) {
            log.error("IA meeting report error: {}", e.getMessage());
            return fallbackMeetingReport(request);
        }
    }

    /**
     * Génère des suggestions proactives d'amélioration pour un rôle donné
     * (ex: gestionnaire de stock) à partir des données métier fournies.
     * Réutilisable par n'importe quel module via request.domain.
     */
    public SuggestionResponse generateSuggestions(SuggestionRequest request) {
        if (!iaConfig.isConfigured()) {
            return fallbackSuggestions(request);
        }
        try {
            int max = request.getMaxSuggestions() != null && request.getMaxSuggestions() > 0
                    ? Math.min(request.getMaxSuggestions(), 8) : 4;
            String role = request.getRole() != null && !request.getRole().isBlank()
                    ? request.getRole() : "gestionnaire de stock";

            StringBuilder prompt = new StringBuilder();
            prompt.append("Tu conseilles un(e) ").append(role).append(" de NT Foods.\n");
            prompt.append("À partir des données ci-dessous, propose exactement ").append(max)
                  .append(" suggestions d'amélioration concrètes, actionnables et personnalisées.\n\n");
            if (request.getData() != null && !request.getData().isEmpty()) {
                prompt.append("Données:\n").append(mapper.writeValueAsString(request.getData())).append("\n\n");
            }
            prompt.append("Réponds UNIQUEMENT en JSON valide de la forme:\n");
            prompt.append("{\"suggestions\":[{\"title\":\"...\",\"message\":\"...\",");
            prompt.append("\"category\":\"REAPPRO|ORGANISATION|ALERTE|OPTIMISATION|FORMATION\",");
            prompt.append("\"priority\":\"HIGH|MEDIUM|LOW\",\"icon\":\"fa-lightbulb\"}]}");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content",
                    promptRegistry.getSystemPrompt(request.getDomain()) + "\nTu dois répondre en JSON valide."));
            messages.add(Map.of("role", "user", "content", prompt.toString()));

            String aiReply = callOpenRouter(messages, 0.5);
            return parseSuggestionResponse(aiReply);
        } catch (Exception e) {
            log.error("IA suggestions error: {}", e.getMessage());
            return fallbackSuggestions(request);
        }
    }

    public boolean isConfigured() {
        return iaConfig.isConfigured();
    }

    public String getModel() {
        return iaConfig.getModel();
    }

    // ═══════════════════════════════════════════════════════════
    //  Private: OpenRouter API call
    // ═══════════════════════════════════════════════════════════

    private String callOpenRouter(List<Map<String, String>> messages, double temperature) throws JsonProcessingException {
        ObjectNode body = mapper.createObjectNode();
        body.put("model", iaConfig.getModel());
        body.put("temperature", temperature);
        body.put("max_tokens", iaConfig.getMaxTokens());

        ArrayNode messagesNode = body.putArray("messages");
        for (Map<String, String> msg : messages) {
            ObjectNode msgNode = messagesNode.addObject();
            msgNode.put("role", msg.get("role"));
            msgNode.put("content", msg.get("content"));
        }

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), iaConfig.buildHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(
                iaConfig.getBaseUrl(),
                entity,
                String.class
        );

        JsonNode root = mapper.readTree(response.getBody());
        JsonNode choices = root.path("choices");
        if (choices.isArray() && !choices.isEmpty()) {
            JsonNode message = choices.get(0).path("message");
            String content = message.path("content").asText("");
            if (content != null && !content.isBlank()) {
                return content.replaceAll("(?s)<think>.*?</think>", "").trim();
            }
            // Some models return content in reasoning field when content is null
            String reasoning = message.path("reasoning").asText("");
            if (reasoning != null && !reasoning.isBlank()) {
                return reasoning;
            }
        }
        throw new RuntimeException("No content in OpenRouter response");
    }

    private List<Map<String, String>> buildChatMessages(ChatRequest request, String systemPrompt) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));

        if (request.getConversationHistory() != null) {
            for (Map<String, Object> hist : request.getConversationHistory()) {
                String role = String.valueOf(hist.getOrDefault("role", "user"));
                String content = String.valueOf(hist.getOrDefault("content", ""));
                if (!content.isBlank()) {
                    messages.add(Map.of("role", role, "content", content));
                }
            }
        }

        String userMsg = request.getMessage();
        if (request.getContext() != null && !request.getContext().isBlank()) {
            userMsg = "Contexte données:\n" + request.getContext() + "\n\nQuestion: " + request.getMessage();
        }
        messages.add(Map.of("role", "user", "content", userMsg));
        return messages;
    }

    private String buildAnalysisPrompt(AnalysisRequest request) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append("Analyse les données suivantes du système de stock TANTY ERP et fournis:\n");
        sb.append("1. Un résumé exécutif (summary)\n");
        sb.append("2. Des insights clés (insights: tableau de strings)\n");
        sb.append("3. Des recommandations actionables (recommendations: tableau d'objets {action, priority, product, detail})\n");
        sb.append("4. Des données pour graphiques (chartData: tableau d'objets {label, value, type})\n\n");
        sb.append("Type d'analyse: ").append(request.getAnalysisType()).append("\n");
        sb.append("Période: ").append(request.getPeriod() != null ? request.getPeriod() : "30 derniers jours").append("\n\n");

        if (request.getStockData() != null) {
            sb.append("Données stock:\n").append(mapper.writeValueAsString(request.getStockData())).append("\n\n");
        }
        if (request.getMovementData() != null) {
            sb.append("Données mouvements:\n").append(mapper.writeValueAsString(request.getMovementData())).append("\n\n");
        }
        if (request.getAlertData() != null) {
            sb.append("Données alertes:\n").append(mapper.writeValueAsString(request.getAlertData())).append("\n\n");
        }
        if (request.getProducts() != null) {
            sb.append("Produits:\n").append(mapper.writeValueAsString(request.getProducts())).append("\n\n");
        }
        sb.append("Réponds UNIQUEMENT en JSON valide avec les clés: summary, insights, recommendations, chartData.");
        return sb.toString();
    }

    private String buildMeetingReportPrompt(MeetingReportRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Génère un compte-rendu de réunion professionnel et bien structuré.\n\n");
        sb.append("Titre: ").append(req.getTitle() != null ? req.getTitle() : "Réunion").append("\n");
        sb.append("Date: ").append(req.getDate() != null ? req.getDate() : "—").append("\n");
        sb.append("Lieu: ").append(req.getLocation() != null ? req.getLocation() : "—").append("\n");
        if (req.getParticipants() != null && !req.getParticipants().isEmpty()) {
            sb.append("Participants: ").append(String.join(", ", req.getParticipants())).append("\n");
        }
        if (req.getAgenda() != null && !req.getAgenda().isBlank()) {
            sb.append("\nOrdre du jour:\n").append(req.getAgenda()).append("\n");
        }
        if (req.getNotes() != null && !req.getNotes().isBlank()) {
            sb.append("\nNotes prises pendant la réunion:\n").append(req.getNotes()).append("\n");
        }
        if (req.getDecisions() != null && !req.getDecisions().isBlank()) {
            sb.append("\nDécisions prises:\n").append(req.getDecisions()).append("\n");
        }
        if (req.getActionItems() != null && !req.getActionItems().isBlank()) {
            sb.append("\nActions à mener:\n").append(req.getActionItems()).append("\n");
        }
        sb.append("\nStructure le compte-rendu avec les sections: Informations générales, Ordre du jour, ");
        sb.append("Discussions, Décisions, Plan d'action (avec responsables et échéances), et Conclusion.");
        return sb.toString();
    }

    private AnalysisResponse parseAnalysisResponse(String aiReply) {
        AnalysisResponse resp = new AnalysisResponse();
        try {
            String json = extractJson(aiReply);
            JsonNode root = mapper.readTree(json);
            resp.setSummary(root.path("summary").asText("Analyse terminée"));
            List<String> insights = new ArrayList<>();
            JsonNode insightsNode = root.path("insights");
            if (insightsNode.isArray()) {
                insightsNode.forEach(n -> insights.add(n.asText()));
            }
            resp.setInsights(insights);

            List<Map<String, Object>> recs = new ArrayList<>();
            JsonNode recsNode = root.path("recommendations");
            if (recsNode.isArray()) {
                recsNode.forEach(n -> {
                    Map<String, Object> rec = new java.util.HashMap<>();
                    n.fields().forEachRemaining(e -> rec.put(e.getKey(), e.getValue().asText()));
                    recs.add(rec);
                });
            }
            resp.setRecommendations(recs);

            List<Map<String, Object>> chartData = new ArrayList<>();
            JsonNode chartNode = root.path("chartData");
            if (chartNode.isArray()) {
                chartNode.forEach(n -> {
                    Map<String, Object> cd = new java.util.HashMap<>();
                    n.fields().forEachRemaining(e -> {
                        if (e.getValue().isNumber()) cd.put(e.getKey(), e.getValue().asDouble());
                        else cd.put(e.getKey(), e.getValue().asText());
                    });
                    chartData.add(cd);
                });
            }
            resp.setChartData(chartData);
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        } catch (Exception e) {
            log.warn("Failed to parse IA analysis JSON, using raw text: {}", e.getMessage());
            resp.setSummary("L'analyse IA a été effectuée mais le format de réponse n'a pas pu être traité. Cliquez sur Relancer pour réessayer.");
            resp.setInsights(List.of());
            resp.setRecommendations(List.of());
            resp.setChartData(List.of());
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        }
        return resp;
    }

    private SuggestionResponse parseSuggestionResponse(String aiReply) {
        SuggestionResponse resp = new SuggestionResponse();
        try {
            JsonNode root = mapper.readTree(extractJson(aiReply));
            JsonNode arr = root.path("suggestions");
            List<SuggestionResponse.Suggestion> list = new ArrayList<>();
            if (arr.isArray()) {
                arr.forEach(n -> list.add(new SuggestionResponse.Suggestion(
                        n.path("title").asText("Suggestion"),
                        n.path("message").asText(""),
                        n.path("category").asText("OPTIMISATION"),
                        n.path("priority").asText("MEDIUM"),
                        n.path("icon").asText("fa-lightbulb")
                )));
            }
            resp.setSuggestions(list);
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        } catch (Exception e) {
            log.warn("Failed to parse IA suggestions JSON: {}", e.getMessage());
            resp.setSuggestions(List.of(new SuggestionResponse.Suggestion(
                    "Analyse IA", "L'analyse IA a été effectuée mais le format de réponse n'a pas pu être traité. Cliquez sur Actualiser pour relancer.", "OPTIMISATION", "MEDIUM", "fa-lightbulb")));
            resp.setModel(iaConfig.getModel());
            resp.setUsingFallback(false);
        }
        return resp;
    }

    private String extractJson(String text) {
        if (text == null || text.isBlank()) return text;
        // Strip markdown code fences: ```json ... ``` or ``` ... ```
        String cleaned = text.trim();
        if (cleaned.startsWith("```")) {
            int firstNewline = cleaned.indexOf('\n');
            if (firstNewline > 0) cleaned = cleaned.substring(firstNewline + 1);
            if (cleaned.endsWith("```")) cleaned = cleaned.substring(0, cleaned.length() - 3);
            cleaned = cleaned.trim();
        }
        // Strip <think>...</think> blocks from reasoning models
        cleaned = cleaned.replaceAll("(?s)<think>.*?</think>", "").trim();
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }

    // ═══════════════════════════════════════════════════════════
    //  Fallbacks (when API key is not configured or call fails)
    // ═══════════════════════════════════════════════════════════

    private ChatResponse fallbackChat(ChatRequest request) {
        String msg = request.getMessage().toLowerCase();
        String reply;
        if (msg.contains("stock")) {
            reply = "📊 Pour consulter l'état du stock, accédez au module Articles de Stock ou au Tableau de Bord. " +
                    "Vous y verrez les quantités, les alertes et la valorisation. (Mode hors-ligne — configurez la clé API IA pour des analyses avancées)";
        } else if (msg.contains("alerte")) {
            reply = "⚠️ Les alertes sont visibles dans le module Alertes. Les alertes critiques nécessitent une action immédiate. " +
                    "(Mode hors-ligne — configurez la clé API IA pour des recommandations personnalisées)";
        } else if (msg.contains("prédiction") || msg.contains("prediction") || msg.contains("ia")) {
            reply = "🤖 Le module IA Prédictions analyse vos mouvements de stock pour prévoir les ruptures. " +
                    "Configurez la clé API IA pour activer l'analyse IA avancée avec GPT-OSS 120B.";
        } else if (msg.contains("rapport") || msg.contains("réunion") || msg.contains("reunion")) {
            reply = "📝 Vous pouvez générer des rapports détaillés et des comptes-rendus de réunions depuis le module Rapports. " +
                    "Configurez la clé API IA pour la génération automatique de comptes-rendus intelligents.";
        } else {
            reply = "Je suis TantyAI, votre assistant ERP. Je peux vous aider avec le stock, les alertes, les prédictions et les rapports. " +
                    "Configurez la clé API IA (OPENAI_API_KEY) pour activer l'IA complète.";
        }
        return new ChatResponse(reply, "fallback", true);
    }

    private AnalysisResponse fallbackAnalysis(AnalysisRequest request) {
        AnalysisResponse resp = new AnalysisResponse();
        resp.setSummary("Analyse basée sur les données locales (mode hors-ligne). Configurez OPENAI_API_KEY pour l'IA avancée.");
        resp.setInsights(List.of("Données chargées depuis la base locale", "Analyse statistique de base disponible"));
        resp.setRecommendations(List.of(Map.of("action", "Configurer l'IA", "priority", "HIGH", "detail", "Ajoutez OPENAI_API_KEY pour des recommandations IA")));
        resp.setChartData(List.of());
        resp.setModel("fallback");
        resp.setUsingFallback(true);
        return resp;
    }

    private SuggestionResponse fallbackSuggestions(SuggestionRequest request) {
        SuggestionResponse resp = new SuggestionResponse();
        List<SuggestionResponse.Suggestion> list = new ArrayList<>();
        list.add(new SuggestionResponse.Suggestion(
                "Surveillez vos alertes critiques",
                "Consultez quotidiennement le module Alertes et traitez en priorité les articles en niveau CRITIQUE pour éviter les ruptures.",
                "ALERTE", "HIGH", "fa-triangle-exclamation"));
        list.add(new SuggestionResponse.Suggestion(
                "Optimisez vos seuils de réapprovisionnement",
                "Révisez périodiquement les points de commande selon la consommation réelle observée dans les mouvements.",
                "REAPPRO", "MEDIUM", "fa-rotate"));
        list.add(new SuggestionResponse.Suggestion(
                "Analysez votre classe A (Pareto)",
                "Concentrez vos efforts de suivi sur les produits de classe A qui représentent 80% de la valeur de votre stock.",
                "OPTIMISATION", "MEDIUM", "fa-chart-pie"));
        list.add(new SuggestionResponse.Suggestion(
                "Activez l'IA complète",
                "Configurez la clé OPENAI_API_KEY pour recevoir des suggestions personnalisées basées sur vos données réelles.",
                "OPTIMISATION", "LOW", "fa-wand-magic-sparkles"));
        resp.setSuggestions(list);
        resp.setModel("fallback");
        resp.setUsingFallback(true);
        return resp;
    }

    private MeetingReportResponse fallbackMeetingReport(MeetingReportRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Compte-rendu de réunion\n\n");
        sb.append("**Titre:** ").append(req.getTitle() != null ? req.getTitle() : "Réunion").append("\n");
        sb.append("**Date:** ").append(req.getDate() != null ? req.getDate() : "—").append("\n");
        sb.append("**Lieu:** ").append(req.getLocation() != null ? req.getLocation() : "—").append("\n");
        if (req.getParticipants() != null && !req.getParticipants().isEmpty()) {
            sb.append("**Participants:** ").append(String.join(", ", req.getParticipants())).append("\n");
        }
        sb.append("\n## Ordre du jour\n").append(req.getAgenda() != null ? req.getAgenda() : "—").append("\n");
        sb.append("\n## Notes\n").append(req.getNotes() != null ? req.getNotes() : "—").append("\n");
        sb.append("\n## Décisions\n").append(req.getDecisions() != null ? req.getDecisions() : "—").append("\n");
        sb.append("\n## Plan d'action\n").append(req.getActionItems() != null ? req.getActionItems() : "—").append("\n");
        sb.append("\n---\n*Compte-rendu généré en mode hors-ligne. Configurez OPENAI_API_KEY pour l'IA.*");

        MeetingReportResponse resp = new MeetingReportResponse();
        resp.setContent(sb.toString());
        resp.setFormattedReport(sb.toString());
        resp.setModel("fallback");
        resp.setUsingFallback(true);
        return resp;
    }
}
