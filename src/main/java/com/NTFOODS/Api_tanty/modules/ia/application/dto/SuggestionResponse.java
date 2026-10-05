package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.ArrayList;
import java.util.List;

/** Réponse contenant des suggestions proactives d'amélioration. */
public class SuggestionResponse {

    public static class Suggestion {
        private String title;
        private String message;
        private String category;
        private String priority;
        private String icon;

        public Suggestion() {}

        public Suggestion(String title, String message, String category, String priority, String icon) {
            this.title = title;
            this.message = message;
            this.category = category;
            this.priority = priority;
            this.icon = icon;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }

    private List<Suggestion> suggestions = new ArrayList<>();
    private String model;
    private boolean usingFallback;

    public List<Suggestion> getSuggestions() { return suggestions; }
    public void setSuggestions(List<Suggestion> suggestions) { this.suggestions = suggestions; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public boolean isUsingFallback() { return usingFallback; }
    public void setUsingFallback(boolean usingFallback) { this.usingFallback = usingFallback; }
}
