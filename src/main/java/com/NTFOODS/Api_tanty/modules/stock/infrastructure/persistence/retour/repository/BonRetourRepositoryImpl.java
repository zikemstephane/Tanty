package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository.BonRetourRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa.BonRetourJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.mapper.BonRetourPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BonRetourRepositoryImpl implements BonRetourRepository {

    private final BonRetourJpaRepository jpaRepository;
    private final BonRetourPersistenceMapper mapper;

    public BonRetourRepositoryImpl(BonRetourJpaRepository jpaRepository, BonRetourPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BonRetour sauvegarder(BonRetour bonRetour) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(bonRetour)));
    }

    @Override
    public Optional<BonRetour> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::versDomaine);
    }

    @Override
    public List<BonRetour> trouverParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<BonRetour> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compterParAnnee(int annee) {
        return jpaRepository.compterParAnnee(annee);
    }
}
