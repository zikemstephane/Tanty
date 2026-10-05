package com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate.DemandeCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.repository.CarburantRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenererRapportRendementCarburantUseCase {

    private final VenteRepository venteRepository;
    private final CarburantRepository carburantRepository;

    public GenererRapportRendementCarburantUseCase(VenteRepository venteRepository,
                                                     CarburantRepository carburantRepository) {
        this.venteRepository = venteRepository;
        this.carburantRepository = carburantRepository;
    }

    public List<LigneRendement> executer(LocalDate debut, LocalDate fin) {
        List<Vente> ventes = venteRepository.listerParPeriode(debut, fin);
        List<DemandeCarburant> carburants = carburantRepository.listerParPeriode(debut, fin);

        Map<String, Double> caParCommercial = ventes.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .collect(Collectors.groupingBy(
                Vente::getMatriculeCommercial,
                Collectors.summingDouble(Vente::montantTotal)));

        Map<String, Double> litresParCommercial = carburants.stream()
            .filter(d -> d.getStatut() == StatutCarburant.VALIDEE)
            .collect(Collectors.groupingBy(
                DemandeCarburant::getMatriculeCommercial,
                Collectors.summingDouble(DemandeCarburant::getLitresDemandes)));

        Set<String> tousCommerciaux = new LinkedHashSet<>();
        tousCommerciaux.addAll(caParCommercial.keySet());
        tousCommerciaux.addAll(litresParCommercial.keySet());

        return tousCommerciaux.stream().map(matricule -> {
            double ca = caParCommercial.getOrDefault(matricule, 0.0);
            double litres = litresParCommercial.getOrDefault(matricule, 0.0);
            double rendement = litres > 0 ? ca / litres : 0.0;
            return new LigneRendement(matricule, ca, litres, rendement);
        }).sorted(Comparator.comparingDouble(LigneRendement::rendementCAparLitre).reversed()).toList();
    }

    public record LigneRendement(
        String matriculeCommercial,
        double caPeriode,
        double litresConsommes,
        double rendementCAparLitre
    ) {}
}
