package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service applicatif — Réconciliation Financière Immédiate (Pilier 2).
 *
 * <p>Au retour du terrain, le cash remis à la secrétaire est comparé au chiffre
 * d'affaires réellement encaissé sur la journée. Ce montant sert de référence à
 * l'alerte rouge portée par
 * {@link com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement}.</p>
 *
 * <p><strong>Les invendus n'entrent plus dans ce calcul.</strong> Ils étaient
 * auparavant valorisés au prix homologué du segment DETAIL puis retranchés du
 * chiffre d'affaires. Or un invendu n'a précisément jamais été vendu : il ne
 * figure pas dans le chiffre d'affaires, et l'en déduire retranchait une seconde
 * fois une somme qui ne s'y trouvait pas. Le cash attendu s'en trouvait
 * sous-estimé, et des manquants restaient sous le seuil d'alerte.</p>
 */
@Service
public class ReconciliationService {

    private final VenteRepository venteRepository;

    public ReconciliationService(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }

    public ReconciliationResultat calculer(String matriculeCommercial, LocalDate date) {
        double caDeclareFCFA = venteRepository.totalVentesParCommercialEtPeriode(matriculeCommercial, date, date);
        return new ReconciliationResultat(matriculeCommercial, date, caDeclareFCFA, caDeclareFCFA);
    }

    public record ReconciliationResultat(
        String matriculeCommercial,
        LocalDate date,
        double caDeclareFCFA,
        double montantAttenduFCFA
    ) {}
}
