package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.jpa.VersementJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.jpa.VersementJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class VersementRepositoryImpl implements VersementRepository {

    private final VersementJpaRepository jpaRepository;

    public VersementRepositoryImpl(VersementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Versement sauvegarder(Versement versement) {
        VersementJpaEntity entity = toEntity(versement);
        VersementJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Versement> trouverParReference(String referenceVersement) {
        return jpaRepository.findByReferenceVersement(referenceVersement).map(this::toDomain);
    }

    @Override
    public List<Versement> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Versement> listerParDate(LocalDate date) {
        return jpaRepository.findByDate(date).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Versement> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Versement> listerParStatut(StatutVersement statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Versement> listerAlertesRouges() {
        return jpaRepository.findByAlerteRougeTrue().stream().map(this::toDomain).toList();
    }

    @Override
    public String genererReference(LocalDate date) {
        String prefix = "VRS-" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
        long count = jpaRepository.count() + 1;
        return prefix + String.format("%03d", count);
    }

    private VersementJpaEntity toEntity(Versement v) {
        return VersementJpaEntity.builder()
            .id(v.getId())
            .referenceVersement(v.getReferenceVersement())
            .matriculeCommercial(v.getMatriculeCommercial())
            .montantAttendu(v.getMontantAttendu())
            .cashVerse(v.getCashVerse())
            .ecart(v.getEcart())
            .alerteRouge(v.isAlerteRouge())
            .typeVersement(v.getTypeVersement())
            .date(v.getDate())
            .statut(v.getStatut())
            .justificationEcart(v.getJustificationEcart())
            .matriculeValidateur(v.getMatriculeValidateur())
            .dateCreation(v.getDateCreation())
            .dateModification(v.getDateModification())
            .version(v.getVersion())
            .build();
    }

    private Versement toDomain(VersementJpaEntity e) {
        return Versement.reconstituer(
            e.getId(), e.getReferenceVersement(), e.getMatriculeCommercial(),
            e.getMontantAttendu(), e.getCashVerse(), e.getTypeVersement(),
            e.getDate(), e.getStatut(), e.getJustificationEcart(),
            e.getMatriculeValidateur(), e.getDateCreation(), e.getDateModification(),
            e.getVersion()
        );
    }
}
