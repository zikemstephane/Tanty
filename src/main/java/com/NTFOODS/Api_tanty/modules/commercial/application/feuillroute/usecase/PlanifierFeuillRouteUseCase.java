package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.JourneeCommercialeService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use Case — Le commercial arrête la tournée qu'il va faire.
 *
 * <p>C'est lui qui décide, sur son terminal, quels clients il visite ce jour-là.
 * La tournée se rattache à sa journée de travail — la session — et n'existe pas
 * sans elle.</p>
 *
 * <p>La numérotation passe par une séquence PostgreSQL. Le contrôleur portait un
 * {@code AtomicLong} statique remis à zéro à chaque démarrage : deux tournées
 * créées le même jour de part et d'autre d'un redémarrage recevaient le même
 * numéro.</p>
 */
@Service
@Transactional
public class PlanifierFeuillRouteUseCase {

    private final FeuillRouteRepository feuillRouteRepository;
    private final JourneeCommercialeService journee;
    private final GenerateurNumeroMetier generateurNumero;

    public PlanifierFeuillRouteUseCase(FeuillRouteRepository feuillRouteRepository,
                                        JourneeCommercialeService journee,
                                        GenerateurNumeroMetier generateurNumero) {
        this.feuillRouteRepository = feuillRouteRepository;
        this.journee = journee;
        this.generateurNumero = generateurNumero;
    }

    public FeuillRoute executer(String matriculeCommercial, LocalDate date, List<Arret> arrets) {
        var session = journee.journeeAPreparer(matriculeCommercial, date);

        // Une seconde tournée le même jour serait refusée par l'index unique posé
        // en V8, mais le dire ici vaut mieux qu'une violation de contrainte remontée
        // brute au terminal.
        if (!feuillRouteRepository.listerParCommercialEtDate(matriculeCommercial, date).isEmpty()) {
            throw new RegleMetierException("Une tournée existe déjà pour le " + date
                + " : ajoutez-y vos clients plutôt que d'en créer une seconde");
        }

        String numero = generateurNumero.genererNumero(FamilleNumero.FEUILLE_ROUTE, date);
        List<ArretFeuillRoute> lignes = arrets.stream()
            .map(a -> new ArretFeuillRoute(a.codeClient(), a.ordre(), LocalDateTime.now()))
            .toList();
        FeuillRoute fr = FeuillRoute.planifier(numero, session.getNumeroSession(),
            matriculeCommercial, date, lignes);
        return feuillRouteRepository.sauvegarder(fr);
    }

    public record Arret(String codeClient, int ordre) {}
}
