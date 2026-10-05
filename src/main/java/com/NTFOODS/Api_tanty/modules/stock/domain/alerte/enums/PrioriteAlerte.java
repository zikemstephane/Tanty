package com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums;

/**
 * Niveau de priorité d'une alerte — détermine le canal et la réactivité
 * de la notification (§5.2 du cahier des charges).
 *
 * CRITIQUE → Notification immédiate + email + SMS
 * HAUTE    → Notification immédiate + email
 * MOYENNE  → Notification dans l'heure
 * BASSE    → Notification quotidienne
 */
public enum PrioriteAlerte {

    CRITIQUE("Critique", 4),
    HAUTE("Haute", 3),
    MOYENNE("Moyenne", 2),
    BASSE("Basse", 1);

    private final String libelle;
    private final int niveau;

    PrioriteAlerte(String libelle, int niveau) {
        this.libelle = libelle;
        this.niveau = niveau;
    }

    public String getLibelle() { return libelle; }
    public int getNiveau() { return niveau; }

    public boolean estCritique() { return this == CRITIQUE; }
    public boolean requireEmail() { return niveau >= 3; }
    public boolean requireSMS() { return this == CRITIQUE; }
}
