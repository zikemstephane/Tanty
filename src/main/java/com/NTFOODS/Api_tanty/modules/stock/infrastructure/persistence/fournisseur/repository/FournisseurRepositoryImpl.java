package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.model.Fournisseur;
import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.repository.FournisseurRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.jpa.FournisseurJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.mapper.FournisseurPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FournisseurRepositoryImpl implements FournisseurRepository {

    private final FournisseurJpaRepository jpaRepository;
    private final FournisseurPersistenceMapper mapper;

    public FournisseurRepositoryImpl(FournisseurJpaRepository jpaRepository, FournisseurPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Fournisseur sauvegarder(Fournisseur fournisseur) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(fournisseur)));
    }

    @Override
    public Optional<Fournisseur> trouverParCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::versDomaine);
    }

    @Override
    public boolean existeParCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public List<Fournisseur> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Fournisseur> rechercherParRaisonSociale(String partiel) {
        return jpaRepository.findByRaisonSocialeContainingIgnoreCase(partiel).stream().map(mapper::versDomaine).toList();
    }
}
