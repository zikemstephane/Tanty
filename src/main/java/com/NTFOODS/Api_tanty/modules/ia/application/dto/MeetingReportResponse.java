package com.NTFOODS.Api_tanty.modules.ia.application.dto;

public class MeetingReportResponse {
    private String content;
    private String formattedReport;
    private String model;
    private boolean usingFallback;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getFormattedReport() { return formattedReport; }
    public void setFormattedReport(String formattedReport) { this.formattedReport = formattedReport; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public boolean isUsingFallback() { return usingFallback; }
    public void setUsingFallback(boolean usingFallback) { this.usingFallback = usingFallback; }
}
