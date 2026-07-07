package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.repository.CarburantRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service applicatif — Vue unifiée de toutes les validations en attente, tous
 * modules confondus (Pilier 2 — Workflow). Donne au DG une visibilité
 * transversale sans avoir à parcourir chaque module séparément.
 */
@Service
@Transactional(readOnly = true)
public class ValidationsEnAttenteService {

    private final PreCommandeRepository preCommandeRepository;
    private final DotationRepository dotationRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final ReceptionRepository receptionRepository;
    private final VersementRepository versementRepository;
    private final CarburantRepository carburantRepository;

    public ValidationsEnAttenteService(PreCommandeRepository preCommandeRepository,
                                        DotationRepository dotationRepository,
                                        BonCommandeRepository bonCommandeRepository,
                                        ReceptionRepository receptionRepository,
                                        VersementRepository versementRepository,
                                        CarburantRepository carburantRepository) {
        this.preCommandeRepository = preCommandeRepository;
        this.dotationRepository = dotationRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.receptionRepository = receptionRepository;
        this.versementRepository = versementRepository;
        this.carburantRepository = carburantRepository;
    }

    public List<ValidationEnAttenteResponse> lister() {
        List<ValidationEnAttenteResponse> resultats = new ArrayList<>();

        preCommandeRepository.listerParStatut(StatutPreCommande.SOUMISE).forEach(pc ->
            resultats.add(new ValidationEnAttenteResponse("PRECOMMANDE_SECRETAIRE", pc.getNumeroPreCommande(),
                "Précommande en attente de validation secrétaire (effet " + pc.getDateEffet() + ")",
                pc.getMatriculeCommercial(), pc.getDateCreation())));

        preCommandeRepository.listerParStatut(StatutPreCommande.VALIDEE_SECRETAIRE).forEach(pc ->
            resultats.add(new ValidationEnAttenteResponse("PRECOMMANDE_COMPTABLE", pc.getNumeroPreCommande(),
                "Précommande en attente de validation comptable (effet " + pc.getDateEffet() + ")",
                pc.getMatriculeCommercial(), pc.getDateCreation())));

        dotationRepository.trouverParStatut(StatutDotation.SOUMISE).forEach(d ->
            resultats.add(new ValidationEnAttenteResponse("DOTATION", d.getNumero(),
                "Dotation en attente d'approbation gestionnaire (effet " + d.getDateEffet() + ")",
                d.getMatriculeCommercial(), d.getDateCreation())));

        bonCommandeRepository.trouverParStatut(StatutBonCommande.BROUILLON).forEach(bc ->
            resultats.add(new ValidationEnAttenteResponse("BON_COMMANDE", bc.getNumero(),
                "Bon de commande en brouillon, non encore validé (fournisseur " + bc.getCodeFournisseur() + ")",
                bc.getMatriculeCreateur(), bc.getDateCreation())));

        receptionRepository.trouverParStatut(StatutReception.EN_ATTENTE_VALIDATION_1).forEach(r ->
            resultats.add(new ValidationEnAttenteResponse("RECEPTION_ETAPE1", r.getNumero(),
                "Réception en attente de constat physique (gestionnaire)", null, r.getDateCreation())));

        receptionRepository.trouverParStatut(StatutReception.EN_ATTENTE_VALIDATION_2).forEach(r ->
            resultats.add(new ValidationEnAttenteResponse("RECEPTION_ETAPE2", r.getNumero(),
                "Réception en attente de 2ᵉ validation (comptable/contrôleur)", null, r.getDateCreation())));

        versementRepository.listerParStatut(StatutVersement.EN_ATTENTE).forEach(v ->
            resultats.add(new ValidationEnAttenteResponse("VERSEMENT", v.getReferenceVersement(),
                "Versement en attente de validation (OTP requis)", v.getMatriculeCommercial(), null)));

        carburantRepository.listerParStatut(StatutCarburant.EN_ATTENTE_SIGNATURE).forEach(c ->
            resultats.add(new ValidationEnAttenteResponse("CARBURANT", c.getReferenceCarburant(),
                "Demande carburant en attente de double signature", c.getMatriculeCommercial(), c.getDateCreation())));

        return resultats;
    }
}
