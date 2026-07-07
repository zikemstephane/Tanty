package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa.NiveauStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.mapper.NiveauStockPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NiveauStockRepositoryImpl implements NiveauStockRepository {

    private final NiveauStockJpaRepository jpaRepository;
    private final NiveauStockPersistenceMapper mapper;

    public NiveauStockRepositoryImpl(NiveauStockJpaRepository jpaRepository, NiveauStockPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public NiveauStock sauvegarder(NiveauStock niveau) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(niveau)));
    }

    @Override
    public Optional<NiveauStock> trouver(String codeProduit, String codeEntrepot) {
        return jpaRepository.findByCodeProduitAndCodeEntrepot(codeProduit, codeEntrepot).map(mapper::versDomaine);
    }

    @Override
    public List<NiveauStock> trouverParEntrepot(String codeEntrepot) {
        return jpaRepository.findByCodeEntrepot(codeEntrepot).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<NiveauStock> trouverParProduit(String codeProduit) {
        return jpaRepository.findByCodeProduit(codeProduit).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<NiveauStock> trouverParType(TypeEntrepot type) {
        return jpaRepository.findByTypeEntrepot(type.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<NiveauStock> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }
}
