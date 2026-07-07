package com.NTFOODS.Api_tanty.shared.application.rapport;

/** Format d'export d'un rapport. Partagé par tous les modules. */
public enum FormatRapport {
    PDF("application/pdf", "pdf"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    CSV("text/csv", "csv"),
    JSON("application/json", "json");

    private final String contentType;
    private final String extension;

    FormatRapport(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getContentType() { return contentType; }
    public String getExtension() { return extension; }

    public static FormatRapport depuis(String valeur) {
        if (valeur == null || valeur.isBlank()) return XLSX;
        try {
            return FormatRapport.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Format de rapport invalide : '" + valeur + "'");
        }
    }
}
