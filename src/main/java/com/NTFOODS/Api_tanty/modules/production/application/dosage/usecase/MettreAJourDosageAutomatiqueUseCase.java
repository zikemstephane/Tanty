package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Use Case — Mettre à jour automatiquement les matières premières d'une session
 * à partir du nombre de fûts produits et du type de mélange.
 *
 * Formules (poudre totale = 50 kg par fût) :
 *  - Bouillie : 40 kg maïs / 9 kg soja / 1 kg arachide
 *  - Mélange Arachide : 40 kg maïs / 9 kg arachide / 1 kg soja
 */
@Service
@Transactional
public class MettreAJourDosageAutomatiqueUseCase {

    private static final double POUDRE_PAR_FUT_KG = 50.0;

    private final SessionDosageRepository repository;

    public MettreAJourDosageAutomatiqueUseCase(SessionDosageRepository repository) {
        this.repository = repository;
    }

    public SessionDosage executer(Long sessionId, int nbFuts, String typeMelange) {
        if (nbFuts < 0) {
            throw new IllegalArgumentException("Le nombre de fûts ne peut pas être négatif");
        }
        if (typeMelange == null || typeMelange.isBlank()) {
            throw new IllegalArgumentException("Le type de mélange est obligatoire");
        }

        SessionDosage session = repository.trouverParId(sessionId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", String.valueOf(sessionId)));

        Map<String, Double> quantites = calculerQuantites(nbFuts, typeMelange.trim().toUpperCase());

        session.definirMatiereUtilisee("MAIS", quantites.get("MAIS"));
        session.definirMatiereUtilisee("SOJA", quantites.get("SOJA"));
        session.definirMatiereUtilisee("ARACHIDE", quantites.get("ARACHIDE"));

        return repository.sauvegarder(session);
    }

    private Map<String, Double> calculerQuantites(int nbFuts, String typeMelange) {
        double poudreTotale = nbFuts * POUDRE_PAR_FUT_KG;
        return switch (typeMelange) {
            case "ARACHIDE" -> Map.of(
                "MAIS", poudreTotale * 40.0 / 50.0,
                "SOJA", poudreTotale * 1.0 / 50.0,
                "ARACHIDE", poudreTotale * 9.0 / 50.0
            );
            default -> Map.of(
                "MAIS", poudreTotale * 40.0 / 50.0,
                "SOJA", poudreTotale * 9.0 / 50.0,
                "ARACHIDE", poudreTotale * 1.0 / 50.0
            );
        };
    }
}
