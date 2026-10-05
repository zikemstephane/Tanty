package com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums;

/**
 * Cycle de validation d'une réception.
 *
 * EN_ATTENTE_VALIDATION_1 (enregistrement physique gestionnaire — quantités devant lui)
 *   → EN_ATTENTE_VALIDATION_2 (Comptable pour MP / Contrôleur pour consommables)
 *   → VALIDEE (stock central mis à jour) | REJETEE
 */
public enum StatutReception {
    EN_ATTENTE_VALIDATION_1("En attente de 1ʳᵉ validation"),
    EN_ATTENTE_VALIDATION_2("En attente de 2ᵉ validation"),
    VALIDEE("Validée"),
    REJETEE("Rejetée");

    private final String libelle;

    StatutReception(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }

    public boolean estTerminal() {
        return this == VALIDEE || this == REJETEE;
    }
}
