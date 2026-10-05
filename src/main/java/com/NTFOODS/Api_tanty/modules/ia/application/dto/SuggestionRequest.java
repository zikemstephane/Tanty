package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.Map;

/**
 * Requête de suggestions proactives d'amélioration.
 * Réutilisable par n'importe quel module via le champ "domain".
 */
public class SuggestionRequest {
    private String domain;
    private String role;
    private Map<String, Object> data;
    private Integer maxSuggestions;

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    public Integer getMaxSuggestions() { return maxSuggestions; }
    public void setMaxSuggestions(Integer maxSuggestions) { this.maxSuggestions = maxSuggestions; }
}
