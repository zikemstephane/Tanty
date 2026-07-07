package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa.EntrepotJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.mapper.EntrepotPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EntrepotRepositoryImpl implements EntrepotRepository {

    private final EntrepotJpaRepository jpaRepository;
    private final EntrepotPersistenceMapper mapper;

    public EntrepotRepositoryImpl(EntrepotJpaRepository jpaRepository, EntrepotPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Entrepot sauvegarder(Entrepot entrepot) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(entrepot)));
    }

    @Override
    public Optional<Entrepot> trouverParCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::versDomaine);
    }

    @Override
    public Optional<Entrepot> trouverMobileParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeProprietaire(matriculeCommercial).map(mapper::versDomaine);
    }

    @Override
    public Optional<Entrepot> trouverUniqueParType(TypeEntrepot type) {
        return jpaRepository.findByType(type.name()).stream().findFirst().map(mapper::versDomaine);
    }

    @Override
    public boolean existeParCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public List<Entrepot> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Entrepot> trouverParType(TypeEntrepot type) {
        return jpaRepository.findByType(type.name()).stream().map(mapper::versDomaine).toList();
    }
}
