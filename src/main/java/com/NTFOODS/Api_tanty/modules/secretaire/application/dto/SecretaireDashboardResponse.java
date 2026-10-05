package com.NTFOODS.Api_tanty.modules.secretaire.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SecretaireDashboardResponse(
    int versementsEnAttente,
    int commandesAValider,
    int alertesRouges,
    double totalVerse,
    double totalEntrees,
    double totalSorties,
    double soldeCaisse,
    boolean seuilDepasse,
    int microStockBas,
    double valeurMicroStock,
    int docsIncomplets,
    List<PetiteCaisseResponse> petiteCaisse,
    List<MicroStockResponse> microStock,
    List<DocumentRecuResponse> documents
) {
    public record PetiteCaisseResponse(
        Long id, String libelle, String type, double montant, String heure, String categorie, LocalDateTime dateOperation
    ) {}

    public record MicroStockResponse(
        Long id, String code, String designation, int qte, double prixUnite, int seuil
    ) {}

    public record DocumentRecuResponse(
        Long id, String matriculeCommercial, String nom, boolean feuilleRoute, boolean bonCommande, boolean factures
    ) {}
}
