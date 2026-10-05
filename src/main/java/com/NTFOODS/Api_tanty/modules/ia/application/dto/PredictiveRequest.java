package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PredictiveRequest {
    private String domain;
    private String predictionType;
    private int horizonDays = 30;
    private List<Map<String, Object>> stockData;
    private List<Map<String, Object>> movementHistory;
    private List<Map<String, Object>> seasonalData;
    private String warehouseName;
}
