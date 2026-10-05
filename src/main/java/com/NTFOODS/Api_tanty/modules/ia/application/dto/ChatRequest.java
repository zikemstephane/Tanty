package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.List;
import java.util.Map;

public class ChatRequest {
    private String message;
    private String context;
    private String domain;
    private List<Map<String, Object>> conversationHistory;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public List<Map<String, Object>> getConversationHistory() { return conversationHistory; }
    public void setConversationHistory(List<Map<String, Object>> conversationHistory) { this.conversationHistory = conversationHistory; }
}
