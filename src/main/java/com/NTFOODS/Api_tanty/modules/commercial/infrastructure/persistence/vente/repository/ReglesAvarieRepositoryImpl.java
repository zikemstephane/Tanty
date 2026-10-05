package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.ReglesAvarieRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.ReglesAvarieJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.ReglesAvarieJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReglesAvarieRepositoryImpl implements ReglesAvarieRepository {

    private final ReglesAvarieJpaRepository jpaRepository;

    public ReglesAvarieRepositoryImpl(ReglesAvarieJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReglesAvarie sauvegarder(ReglesAvarie regles) {
        return toDomain(jpaRepository.save(toEntity(regles)));
    }

    @Override
    public Optional<ReglesAvarie> trouverParReference(String reference) {
        return jpaRepository.findByReference(reference).map(this::toDomain);
    }

    @Override
    public Optional<ReglesAvarie> trouverApplicableA(LocalDate date) {
        return jpaRepository.findApplicables(date).stream().findFirst().map(this::toDomain);
    }

    @Override
    public Optional<ReglesAvarie> trouverCourant() {
        return jpaRepository.findFirstByDateFinIsNullOrderByDateDebutDesc().map(this::toDomain);
    }

    @Override
    public List<ReglesAvarie> listerHistorique() {
        return jpaRepository.findAllByOrderByDateDebutDesc().stream().map(this::toDomain).toList();
    }

    private ReglesAvarieJpaEntity toEntity(ReglesAvarie r) {
        return ReglesAvarieJpaEntity.builder()
            .id(r.getId()).reference(r.getReference())
            .quantiteSeuilPhotoObligatoire(r.getQuantiteSeuilPhotoObligatoire())
            .dateDebut(r.getDateDebut()).dateFin(r.getDateFin())
            .matriculeAuteur(r.getMatriculeAuteur()).motif(r.getMotif())
            .dateCreation(r.getDateCreation()).version(r.getVersion())
            .build();
    }

    private ReglesAvarie toDomain(ReglesAvarieJpaEntity e) {
        return ReglesAvarie.reconstituer(e.getId(), e.getReference(),
            e.getQuantiteSeuilPhotoObligatoire(), e.getDateDebut(), e.getDateFin(),
            e.getMatriculeAuteur(), e.getMotif(), e.getDateCreation(), e.getVersion());
    }
}
