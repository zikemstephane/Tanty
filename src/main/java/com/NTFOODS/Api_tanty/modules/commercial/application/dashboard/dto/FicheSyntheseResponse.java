package com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.dto;

import java.time.LocalDate;
import java.util.List;

public record FicheSyntheseResponse(
    LocalDate date,
    double caYaounde,
    double caDouala,
    double caTotal,
    int effectifCommerciaux,
    double vacInit,
    double ca,
    double vac,
    double recouvrements,
    double vteCpt,
    double soldeVAC,
    double cumulMensuelCA,
    double eCheques,
    double vstBanques,
    double depensesJustifiees,
    double depensesNonJust,
    double sCaisse,
    double montantAvaries,
    double montantDotPromo,
    List<LigneFicheSynthese> lignes
) {
    public record LigneFicheSynthese(
        String codeProduit,
        long qteProduite,
        long stockVeille,
        long stockCamion,
        long stockTotal,
        long qteSorties,
        long stockFinal,
        long qteAvoDotProm,
        long qteFacturee
    ) {}
}
