package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate.DemandeCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.repository.CarburantRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.jpa.CarburantJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.jpa.CarburantJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class CarburantRepositoryImpl implements CarburantRepository {

    private final CarburantJpaRepository jpaRepository;

    public CarburantRepositoryImpl(CarburantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DemandeCarburant sauvegarder(DemandeCarburant demande) {
        return toDomain(jpaRepository.save(toEntity(demande)));
    }

    @Override
    public Optional<DemandeCarburant> trouverParReference(String reference) {
        return jpaRepository.findByReferenceCarburant(reference).map(this::toDomain);
    }

    @Override
    public List<DemandeCarburant> listerParStatut(StatutCarburant statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<DemandeCarburant> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<DemandeCarburant> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    public String genererReference(LocalDate date) {
        long count = jpaRepository.countByDate(date) + 1;
        return "CAR-" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%03d", count);
    }

    private CarburantJpaEntity toEntity(DemandeCarburant d) {
        return CarburantJpaEntity.builder()
            .id(d.getId()).referenceCarburant(d.getReferenceCarburant())
            .matriculeCommercial(d.getMatriculeCommercial()).immatriculation(d.getImmatriculation())
            .stationPartenaire(d.getStationPartenaire()).kilometrage(d.getKilometrage())
            .litresDemandes(d.getLitresDemandes()).montant(d.getMontant()).date(d.getDate())
            .statut(d.getStatut()).signatureCommercial(d.getSignatureCommercial())
            .signatureSuperviseur(d.getSignatureSuperviseur()).motifRefus(d.getMotifRefus())
            .photoCompteur(d.getPhotoCompteur())
            .dateCreation(d.getDateCreation()).dateModification(d.getDateModification())
            .version(d.getVersion()).build();
    }

    private DemandeCarburant toDomain(CarburantJpaEntity e) {
        return DemandeCarburant.reconstituer(e.getId(), e.getReferenceCarburant(), e.getMatriculeCommercial(),
            e.getImmatriculation(), e.getStationPartenaire(), e.getKilometrage(),
            e.getLitresDemandes(), e.getMontant(), e.getDate(), e.getStatut(),
            e.getSignatureCommercial(), e.getSignatureSuperviseur(), e.getMotifRefus(),
            e.getPhotoCompteur(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
