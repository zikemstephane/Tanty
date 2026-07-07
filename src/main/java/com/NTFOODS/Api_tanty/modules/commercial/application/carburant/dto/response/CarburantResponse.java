package com.NTFOODS.Api_tanty.modules.commercial.application.carburant.dto.response;

import java.time.LocalDate;

/**
 * @param caZoneMoisCourantFCFA CA généré par le commercial sur le mois courant (Pilier 3 :
 *                              permet au superviseur de confronter la dépense carburant au CA
 *                              généré dans la zone avant de signer).
 * @param ratioCarburantSurCaPourcent montant carburant / CA du mois, en pourcentage (0 si CA nul).
 */
public record CarburantResponse(
    Long id,
    String referenceCarburant,
    String matriculeCommercial,
    String immatriculation,
    String stationPartenaire,
    int kilometrage,
    double litresDemandes,
    double montant,
    LocalDate date,
    String statut,
    String signatureCommercial,
    String signatureSuperviseur,
    String motifRefus,
    String photoCompteur,
    double caZoneMoisCourantFCFA,
    double ratioCarburantSurCaPourcent
) {}
