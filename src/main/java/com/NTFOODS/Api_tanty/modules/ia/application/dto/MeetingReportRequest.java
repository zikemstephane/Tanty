package com.NTFOODS.Api_tanty.modules.ia.application.dto;

import java.util.List;

public class MeetingReportRequest {
    private String title;
    private String date;
    private String location;
    private List<String> participants;
    private String agenda;
    private String notes;
    private String decisions;
    private String actionItems;
    private String format;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    public String getAgenda() { return agenda; }
    public void setAgenda(String agenda) { this.agenda = agenda; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDecisions() { return decisions; }
    public void setDecisions(String decisions) { this.decisions = decisions; }
    public String getActionItems() { return actionItems; }
    public void setActionItems(String actionItems) { this.actionItems = actionItems; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}
