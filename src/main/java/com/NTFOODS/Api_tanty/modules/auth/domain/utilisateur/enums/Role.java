package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums;

/**
 * Value Object Enum — Rôle métier d'un utilisateur NTFOODS TANTY.
 *
 * Le rôle détermine les droits d'accès transversaux sur tous les modules
 * (Stock, Production, Commercial, Comptabilité, Administration).
 */
public enum Role {

    DIRECTEUR_GENERAL("Directeur Général"),
    DIRECTEUR_COMMERCIAL("Directeur Commercial"),
    GESTIONNAIRE_STOCK("Gestionnaire de Stock"),
    CHEF_PRODUCTION("Chef de Production"),
    RESPONSABLE_SALLE("responsable de salle"),
    CHEF_MACHINISTE("Chef Machiniste"),
    MACHINISTE("Machiniste"),
    AGENT_DOSEUR("Agent Doseur"),
    AGENT_PRODUCTION("Agent Production"),
    CONTROLEUR_GENERAL("Contrôleur Général"),
    COMMERCIAL("Commercial"),
    SECRETAIRE("Secrétaire"),
    COMPTABLE("Comptable"),
    CHARGEE_RP("Chargée RP & Commercial"),
    ADMIN("Administrateur Système");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
