package com.NTFOODS.Api_tanty.modules.production.application.dashboard.dto.response;

import java.time.LocalDate;

/** Vue d'ensemble du responsable de salle pour une journée. */
public record DashboardResponsableSalleResponse(
    LocalDate date,
    PlanEnCours planEnCours,
    int plansValides,
    AffectationDuJour affectation,
    boolean registreSaisi,
    Besoins besoins,
    Lots lots,
    Ofs ofs
) {
    /** Plan en cours : avancement = réalisé ÷ objectif sur toutes ses lignes (null sans objectif). */
    public record PlanEnCours(String reference, String semaine, LocalDate dateDebut, LocalDate dateFin,
                              int nbProduits, long objectif, long realise, Double avancementPct) {}

    /** Affectation de la journée sur le plan en cours. */
    public record AffectationDuJour(boolean existe, String statut, int postes, int employesAffectes,
                                    int saisies, int objectifsAtteints, int ecarts, double quantiteRealisee) {}

    public record Besoins(int enAttente, int valides, int rejetes) {}

    /** Lots en attente = déclarés ou réceptionnés, pas encore validés par le stock. */
    public record Lots(int enAttenteStock, int valides, int rejetes) {}

    public record Ofs(int enCours, int planifies, int honores) {}
}
