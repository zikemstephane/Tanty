package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.List;
import java.util.Map;

public class AnalysisRequest {
    private String analysisType;
    private String period;
    private String domain;
    private Map<String, Object> stockData;
    private Map<String, Object> movementData;
    private Map<String, Object> alertData;
    private List<Map<String, Object>> products;

    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public Map<String, Object> getStockData() { return stockData; }
    public void setStockData(Map<String, Object> stockData) { this.stockData = stockData; }
    public Map<String, Object> getMovementData() { return movementData; }
    public void setMovementData(Map<String, Object> movementData) { this.movementData = movementData; }
    public Map<String, Object> getAlertData() { return alertData; }
    public void setAlertData(Map<String, Object> alertData) { this.alertData = alertData; }
    public List<Map<String, Object>> getProducts() { return products; }
    public void setProducts(List<Map<String, Object>> products) { this.products = products; }
}
