package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.CodeObservation;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.CodeObservationRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.CodeObservationJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.CodeObservationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CodeObservationRepositoryImpl implements CodeObservationRepository {

    private final CodeObservationJpaRepository jpaRepository;

    public CodeObservationRepositoryImpl(CodeObservationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<CodeObservation> listerActifs() {
        return jpaRepository.findByActifTrueOrderByOrdreAsc().stream().map(this::toDomain).toList();
    }

    @Override
    public List<CodeObservation> listerTous() {
        return jpaRepository.findAllByOrderByOrdreAsc().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<CodeObservation> parId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<CodeObservation> parCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public CodeObservation sauvegarder(CodeObservation code) {
        CodeObservationJpaEntity entity = code.getId() != null
            ? jpaRepository.findById(code.getId()).orElseGet(CodeObservationJpaEntity::new)
            : new CodeObservationJpaEntity();
        entity.setCode(code.getCode());
        entity.setLibelle(code.getLibelle());
        entity.setRequireQuantity(code.isRequireQuantity());
        entity.setActif(code.isActif());
        entity.setOrdre(code.getOrdre());
        CodeObservationJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void supprimer(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existeParCode(String code) {
        return jpaRepository.findByCode(code).isPresent();
    }

    private CodeObservation toDomain(CodeObservationJpaEntity e) {
        return new CodeObservation(e.getId(), e.getCode(), e.getLibelle(), e.isRequireQuantity(), e.isActif(), e.getOrdre());
    }
}
