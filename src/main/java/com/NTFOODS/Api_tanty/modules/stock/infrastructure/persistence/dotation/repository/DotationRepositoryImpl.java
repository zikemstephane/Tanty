package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.DotationJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.mapper.DotationPersistenceMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DotationRepositoryImpl implements DotationRepository {

    private final DotationJpaRepository jpaRepository;
    private final DotationPersistenceMapper mapper;

    public DotationRepositoryImpl(DotationJpaRepository jpaRepository, DotationPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Dotation sauvegarder(Dotation dotation) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(dotation)));
    }

    @Override
    public Optional<Dotation> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::versDomaine);
    }

    @Override
    public List<Dotation> trouverParStatut(StatutDotation statut) {
        return jpaRepository.findByStatut(statut.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Dotation> trouverParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Dotation> trouverParDateEffet(LocalDate dateEffet) {
        return jpaRepository.findByDateEffet(dateEffet).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Dotation> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compterParAnnee(int annee) {
        return jpaRepository.compterParAnnee(annee);
    }
}
