package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums.StatutTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.BonTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.repository.BonTransfertRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa.BonTransfertJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.mapper.BonTransfertPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BonTransfertRepositoryImpl implements BonTransfertRepository {

    private final BonTransfertJpaRepository jpaRepository;
    private final BonTransfertPersistenceMapper mapper;

    public BonTransfertRepositoryImpl(BonTransfertJpaRepository jpaRepository, BonTransfertPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BonTransfert sauvegarder(BonTransfert bonTransfert) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(bonTransfert)));
    }

    @Override
    public Optional<BonTransfert> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::versDomaine);
    }

    @Override
    public List<BonTransfert> trouverParStatut(StatutTransfert statut) {
        return jpaRepository.findByStatut(statut.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<BonTransfert> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compterParAnnee(int annee) {
        return jpaRepository.compterParAnnee(annee);
    }
}
