package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class IntelligentReportRequest {
    private String domain;
    private String reportType;
    private String period;
    private List<Map<String, Object>> stockData;
    private List<Map<String, Object>> movementData;
    private List<Map<String, Object>> alertData;
    private List<Map<String, Object>> financialData;
    private String format;
    private boolean includeCharts;
    private boolean includeRecommendations;
    private boolean includeRiskAnalysis;
}
