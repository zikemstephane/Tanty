package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class IntelligentReportResponse {
    private String title;
    private String executiveSummary;
    private String markdownReport;
    private List<ReportSection> sections;
    private List<Map<String, Object>> keyMetrics;
    private List<Map<String, Object>> riskMatrix;
    private List<String> actionItems;
    private String model;
    private boolean usingFallback;

    @Data
    public static class ReportSection {
        private String heading;
        private String content;
        private String icon;
        private String severity;
    }
}
