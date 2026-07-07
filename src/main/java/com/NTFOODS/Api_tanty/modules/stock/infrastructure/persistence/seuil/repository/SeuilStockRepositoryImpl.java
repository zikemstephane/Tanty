package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.SeuilStockJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.SeuilStockJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SeuilStockRepositoryImpl implements SeuilStockRepository {

    private final SeuilStockJpaRepository jpaRepository;

    public SeuilStockRepositoryImpl(SeuilStockJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SeuilStock sauvegarder(SeuilStock seuil) {
        return toDomain(jpaRepository.save(toEntity(seuil)));
    }

    @Override
    public Optional<SeuilStock> trouver(String codeProduit, TypeEntrepot typeEntrepot) {
        return jpaRepository.findByCodeProduitAndTypeEntrepot(codeProduit, typeEntrepot.name()).map(this::toDomain);
    }

    @Override
    public List<SeuilStock> trouverParType(TypeEntrepot typeEntrepot) {
        return jpaRepository.findByTypeEntrepot(typeEntrepot.name()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<SeuilStock> trouverTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private SeuilStockJpaEntity toEntity(SeuilStock s) {
        return SeuilStockJpaEntity.builder()
            .id(s.getId()).codeProduit(s.getCodeProduit()).typeEntrepot(s.getTypeEntrepot().name())
            .seuilReapprovisionnement(s.getSeuilReapprovisionnement()).actif(s.isActif())
            .dateModification(s.getDateModification()).version(s.getVersion())
            .build();
    }

    private SeuilStock toDomain(SeuilStockJpaEntity e) {
        return SeuilStock.reconstituer(e.getId(), e.getCodeProduit(), TypeEntrepot.valueOf(e.getTypeEntrepot()),
            e.getSeuilReapprovisionnement(), e.isActif(), e.getDateModification(), e.getVersion());
    }
}
