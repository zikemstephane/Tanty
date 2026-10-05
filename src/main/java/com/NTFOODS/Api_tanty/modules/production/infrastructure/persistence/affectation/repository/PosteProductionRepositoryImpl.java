package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.PosteProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.PosteProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa.PosteProductionJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa.PosteProductionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PosteProductionRepositoryImpl implements PosteProductionRepository {

    private final PosteProductionJpaRepository jpaRepository;

    public PosteProductionRepositoryImpl(PosteProductionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<PosteProduction> listerActifs() {
        return jpaRepository.findByActifTrueOrderByOrdreAsc().stream().map(this::toDomain).toList();
    }

    @Override
    public List<PosteProduction> listerTous() {
        return jpaRepository.findAllByOrderByOrdreAsc().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<PosteProduction> parCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public Optional<PosteProduction> parId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public PosteProduction sauvegarder(PosteProduction poste) {
        PosteProductionJpaEntity entity = poste.getId() != null
            ? jpaRepository.findById(poste.getId()).orElseGet(PosteProductionJpaEntity::new)
            : new PosteProductionJpaEntity();
        entity.setCode(poste.getCode());
        entity.setLibelle(poste.getLibelle());
        entity.setEffectifReference(poste.getEffectifReference());
        entity.setProductionParHeure(poste.getProductionParHeure());
        entity.setProductionJournaliere(poste.getProductionJournaliere());
        entity.setUnite(poste.getUnite());
        entity.setActif(poste.isActif());
        entity.setOrdre(poste.getOrdre());
        entity.setCategoriesMetier(String.join(",", poste.getCategoriesMetier()));
        PosteProductionJpaEntity saved = jpaRepository.save(entity);
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

    private PosteProduction toDomain(PosteProductionJpaEntity e) {
        Set<String> categories = e.getCategoriesMetier() == null || e.getCategoriesMetier().isBlank()
            ? Set.of()
            : Arrays.stream(e.getCategoriesMetier().split(",")).collect(Collectors.toSet());
        return new PosteProduction(e.getId(), e.getCode(), e.getLibelle(), e.getEffectifReference(),
            e.getProductionParHeure(), e.getProductionJournaliere(), e.getUnite(), e.isActif(),
            e.getOrdre(), categories);
    }
}
