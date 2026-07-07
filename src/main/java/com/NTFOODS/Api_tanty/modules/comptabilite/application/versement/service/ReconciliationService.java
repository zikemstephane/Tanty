package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.LigneRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository.BonRetourRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service applicatif — Réconciliation Financière Immédiate (Pilier 2).
 *
 * Au retour du terrain, le cash versé à la secrétaire doit être automatiquement
 * comparé au chiffre d'affaires déclaré sur l'application mobile, DÉDUCTION
 * FAITE des retours physiques validés par le stock. C'est ce montant calculé
 * qui sert de "montant attendu" de référence pour détecter l'alerte rouge sur
 * {@link com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement}.
 *
 * Limite assumée : le module Stock ne connaît jamais les montants d'argent
 * (règle d'isolation stricte) — un {@code BonRetour} ne porte donc ni client
 * ni prix. La valorisation des retours utilise ici le prix homologué du
 * segment {@code DETAIL} de la grille tarifaire comme référence par défaut ;
 * un produit sans prix homologué est valorisé à 0 (signalé dans le détail
 * retourné, jamais silencieusement ignoré côté audit).
 */
@Service
public class ReconciliationService {

    private final VenteRepository venteRepository;
    private final BonRetourRepository bonRetourRepository;
    private final GrilleTarifaireRepository grilleTarifaireRepository;

    public ReconciliationService(VenteRepository venteRepository, BonRetourRepository bonRetourRepository,
                                  GrilleTarifaireRepository grilleTarifaireRepository) {
        this.venteRepository = venteRepository;
        this.bonRetourRepository = bonRetourRepository;
        this.grilleTarifaireRepository = grilleTarifaireRepository;
    }

    public ReconciliationResultat calculer(String matriculeCommercial, LocalDate date) {
        double caDeclareFCFA = venteRepository.totalVentesParCommercialEtPeriode(matriculeCommercial, date, date);

        double valeurRetoursFCFA = 0.0;
        boolean produitsSansPrixHomologue = false;
        for (BonRetour bon : bonRetourRepository.trouverParCommercial(matriculeCommercial)) {
            if (!bon.getDateRetour().equals(date)) continue;
            for (LigneRetour ligne : bon.getLignes()) {
                var prix = grilleTarifaireRepository.trouver(ligne.getCodeProduit(), TypeClient.DETAIL);
                if (prix.isPresent()) {
                    valeurRetoursFCFA += ligne.getQuantite() * prix.get().getPrixHomologueFCFA();
                } else {
                    produitsSansPrixHomologue = true;
                }
            }
        }

        double montantAttenduFCFA = Math.max(0.0, caDeclareFCFA - valeurRetoursFCFA);

        return new ReconciliationResultat(matriculeCommercial, date, caDeclareFCFA, valeurRetoursFCFA,
            montantAttenduFCFA, produitsSansPrixHomologue);
    }

    public record ReconciliationResultat(
        String matriculeCommercial,
        LocalDate date,
        double caDeclareFCFA,
        double valeurRetoursValidesFCFA,
        double montantAttenduFCFA,
        boolean avertissementPrixHomologueManquant
    ) {}
}
