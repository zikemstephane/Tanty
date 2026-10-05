package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa.ReceptionJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.mapper.ReceptionPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ReceptionRepositoryImpl implements ReceptionRepository {

    private final ReceptionJpaRepository jpaRepository;
    private final ReceptionPersistenceMapper mapper;

    public ReceptionRepositoryImpl(ReceptionJpaRepository jpaRepository, ReceptionPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Reception sauvegarder(Reception reception) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(reception)));
    }

    @Override
    public Optional<Reception> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::versDomaine);
    }

    @Override
    public List<Reception> trouverParStatut(StatutReception statut) {
        return jpaRepository.findByStatut(statut.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Reception> trouverParBonCommande(String numeroBonCommande) {
        return jpaRepository.findByNumeroBonCommande(numeroBonCommande).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Reception> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compterParAnnee(int annee) {
        return jpaRepository.compterParAnnee(annee);
    }
}
