package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PredictiveResponse {
    private String summary;
    private List<RupturePrediction> rupturePredictions;
    private List<SeasonalPattern> seasonalPatterns;
    private List<Map<String, Object>> optimizedThresholds;
    private List<AnomalyDetection> anomalies;
    private String model;
    private boolean usingFallback;

    @Data
    public static class RupturePrediction {
        private String productSku;
        private String productName;
        private String warehouse;
        private int daysUntilRupture;
        private double probability;
        private String severity;
        private String recommendedAction;
        private double suggestedQuantity;
    }

    @Data
    public static class SeasonalPattern {
        private String productSku;
        private String pattern;
        private String peakMonth;
        private String lowMonth;
        private double variationPercent;
        private String recommendation;
    }

    @Data
    public static class AnomalyDetection {
        private String type;
        private String productSku;
        private String description;
        private double severity;
        private String detectedPattern;
        private String suggestedAction;
    }
}
