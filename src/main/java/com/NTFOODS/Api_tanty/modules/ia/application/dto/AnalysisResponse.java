package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.List;
import java.util.Map;

public class AnalysisResponse {
    private String summary;
    private List<String> insights;
    private List<Map<String, Object>> recommendations;
    private List<Map<String, Object>> chartData;
    private String model;
    private boolean usingFallback;

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }
    public List<Map<String, Object>> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Map<String, Object>> recommendations) { this.recommendations = recommendations; }
    public List<Map<String, Object>> getChartData() { return chartData; }
    public void setChartData(List<Map<String, Object>> chartData) { this.chartData = chartData; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public boolean isUsingFallback() { return usingFallback; }
    public void setUsingFallback(boolean usingFallback) { this.usingFallback = usingFallback; }
}
