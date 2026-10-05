package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.List;
import java.util.Map;

public class ChatResponse {
    private String reply;
    private String model;
    private boolean usingFallback;
    private List<Map<String, Object>> suggestions;

    public ChatResponse() {}

    public ChatResponse(String reply, String model, boolean usingFallback) {
        this.reply = reply;
        this.model = model;
        this.usingFallback = usingFallback;
    }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public boolean isUsingFallback() { return usingFallback; }
    public void setUsingFallback(boolean usingFallback) { this.usingFallback = usingFallback; }
    public List<Map<String, Object>> getSuggestions() { return suggestions; }
    public void setSuggestions(List<Map<String, Object>> suggestions) { this.suggestions = suggestions; }
}
