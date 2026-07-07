package com.NTFOODS.Api_tanty.modules.commercial.application.carburant.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.carburant.dto.response.CarburantResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate.DemandeCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Mapper Carburant — enrichit chaque réponse avec le CA du mois du commercial
 * (Pilier 3 : confronter la dépense carburant au chiffre d'affaires généré
 * dans la zone), pour que le superviseur dispose de cette donnée AVANT de
 * signer. Comparaison informative — la décision de signer reste humaine.
 */
@Component
public class CarburantMapper {

    private final VenteRepository venteRepository;

    public CarburantMapper(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }

    public CarburantResponse versResponse(DemandeCarburant d) {
        LocalDate debutMois = d.getDate().withDayOfMonth(1);
        double caZone = venteRepository.totalVentesParCommercialEtPeriode(
            d.getMatriculeCommercial(), debutMois, d.getDate());
        double ratio = caZone > 0 ? (d.getMontant() / caZone) * 100.0 : 0.0;

        return new CarburantResponse(d.getId(), d.getReferenceCarburant(), d.getMatriculeCommercial(),
            d.getImmatriculation(), d.getStationPartenaire(), d.getKilometrage(),
            d.getLitresDemandes(), d.getMontant(), d.getDate(), d.getStatut().name(),
            d.getSignatureCommercial(), d.getSignatureSuperviseur(), d.getMotifRefus(),
            d.getPhotoCompteur(), caZone, ratio);
    }
}
