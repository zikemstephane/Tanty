package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate.RegistreProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.entity.LigneRegistreEmploye;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.repository.RegistreProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa.LigneRegistreJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa.RegistreJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa.RegistreJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class RegistreRepositoryImpl implements RegistreProductionRepository {

    private final RegistreJpaRepository jpaRepository;

    public RegistreRepositoryImpl(RegistreJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public RegistreProduction sauvegarder(RegistreProduction registre) {
        return toDomain(jpaRepository.save(toEntity(registre)));
    }

    public Optional<RegistreProduction> trouverParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.findByReferencePPHAndDate(referencePPH, date).map(this::toDomain);
    }

    public List<RegistreProduction> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existeParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.existsByReferencePPHAndDate(referencePPH, date);
    }

    @Override
    public List<RegistreProduction> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetweenOrderByDateDesc(debut, fin).stream().map(this::toDomain).toList();
    }

    private RegistreJpaEntity toEntity(RegistreProduction r) {
        List<LigneRegistreJpaEntity> lignes = r.getLignesEmployes().stream()
            .map(l -> LigneRegistreJpaEntity.builder()
                .matriculeEmploye(l.getMatriculeEmploye()).nomEmploye(l.getNomEmploye())
                .present(l.isPresent()).motifAbsence(l.getMotifAbsence()).poste(l.getPoste())
                .heureArrivee(l.getHeureArrivee()).qteRealisee(l.getQteRealisee()).unite(l.getUnite())
                .build())
            .toList();
        return RegistreJpaEntity.builder()
            .id(r.getId()).referencePPH(r.getReferencePPH()).date(r.getDate())
            .matriculeChefProduction(r.getMatriculeChefProduction()).lignesEmployes(lignes)
            .dateCreation(r.getDateCreation()).dateModification(r.getDateModification())
            .version(r.getVersion()).build();
    }

    private RegistreProduction toDomain(RegistreJpaEntity e) {
        List<LigneRegistreEmploye> lignes = e.getLignesEmployes().stream()
            .map(l -> new LigneRegistreEmploye(l.getMatriculeEmploye(), l.getNomEmploye(),
                l.getPoste(), l.isPresent(), l.getMotifAbsence(),
                l.getHeureArrivee(), l.getQteRealisee(), l.getUnite()))
            .toList();
        return RegistreProduction.reconstituer(e.getId(), e.getReferencePPH(), e.getDate(),
            e.getMatriculeChefProduction(), lignes, e.getDateCreation(), e.getDateModification(),
            e.getVersion());
    }
}
