package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository.BoutiquePotentielleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service applicatif — Taux d'Occupation des Marchés (Pilier 4).
 *
 * Compare, pour chaque secteur recensé, le nombre de clients réellement
 * livrés (référencés dans le module Commercial, {@code Client.localite}) au
 * nombre de boutiques potentielles recensées sur le terrain — mesure la
 * pénétration réelle de la force commerciale par zone.
 */
@Service
@Transactional(readOnly = true)
public class TauxOccupationMarcheService {

    private final BoutiquePotentielleRepository boutiqueRepository;
    private final ClientRepository clientRepository;

    public TauxOccupationMarcheService(BoutiquePotentielleRepository boutiqueRepository,
                                        ClientRepository clientRepository) {
        this.boutiqueRepository = boutiqueRepository;
        this.clientRepository = clientRepository;
    }

    public List<TauxOccupationSecteurResponse> calculer() {
        return boutiqueRepository.listerSecteurs().stream()
            .map(secteur -> {
                long boutiques = boutiqueRepository.listerParSecteur(secteur).size();
                long clients = clientRepository.listerParLocalite(secteur).size();
                double taux = boutiques > 0 ? Math.min(100.0, (clients * 100.0) / boutiques) : 0.0;
                return new TauxOccupationSecteurResponse(secteur, boutiques, clients, taux);
            })
            .toList();
    }
}
