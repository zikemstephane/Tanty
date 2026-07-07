package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service applicatif — Performance par zone (Pilier 4).
 * Complète {@link TauxOccupationMarcheService} (pénétration) avec la
 * performance commerciale réelle (CA, ventes) par secteur, sur le mois courant.
 */
@Service
@Transactional(readOnly = true)
public class ZonePerformanceService {

    private final ClientRepository clientRepository;
    private final VenteJpaRepository venteJpaRepository;

    public ZonePerformanceService(ClientRepository clientRepository, VenteJpaRepository venteJpaRepository) {
        this.clientRepository = clientRepository;
        this.venteJpaRepository = venteJpaRepository;
    }

    public List<ZonePerformanceResponse> calculer() {
        LocalDate debutMois = LocalDate.now().withDayOfMonth(1);
        LocalDate aujourdHui = LocalDate.now();

        List<Client> tousClients = clientRepository.listerTous();
        Set<String> secteurs = tousClients.stream()
            .map(Client::getLocalite)
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.toSet());

        List<VenteJpaEntity> ventesMois = venteJpaRepository.findByDateBetween(debutMois, aujourdHui);

        return secteurs.stream()
            .map(secteur -> {
                Set<String> codesClientsSecteur = tousClients.stream()
                    .filter(c -> secteur.equals(c.getLocalite()))
                    .map(Client::getCodeClient)
                    .collect(Collectors.toSet());

                List<VenteJpaEntity> ventesSecteur = ventesMois.stream()
                    .filter(v -> codesClientsSecteur.contains(v.getCodeClient()))
                    .toList();

                double ca = ventesSecteur.stream()
                    .flatMap(v -> v.getLignes().stream())
                    .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantite() * (1.0 - l.getRemise() / 100.0))
                    .sum();

                return new ZonePerformanceResponse(secteur, ca, codesClientsSecteur.size(), ventesSecteur.size());
            })
            .sorted((a, b) -> Double.compare(b.caMoisCourantFCFA(), a.caMoisCourantFCFA()))
            .toList();
    }
}
