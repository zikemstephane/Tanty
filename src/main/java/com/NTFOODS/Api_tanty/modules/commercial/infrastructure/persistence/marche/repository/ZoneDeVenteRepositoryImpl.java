package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.ZoneDeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository.ZoneDeVenteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.ZoneDeVenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.ZoneDeVenteJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ZoneDeVenteRepositoryImpl implements ZoneDeVenteRepository {

    private final ZoneDeVenteJpaRepository jpaRepository;

    public ZoneDeVenteRepositoryImpl(ZoneDeVenteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ZoneDeVente sauvegarder(ZoneDeVente zone) {
        return toDomain(jpaRepository.save(toEntity(zone)));
    }

    @Override
    public Optional<ZoneDeVente> trouverParNom(String nom) {
        return jpaRepository.findByNom(nom).map(this::toDomain);
    }

    @Override
    public Optional<ZoneDeVente> trouverParId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ZoneDeVente> listerToutes() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<ZoneDeVente> listerActives() {
        return jpaRepository.findByActifTrue().stream().map(this::toDomain).toList();
    }

    private ZoneDeVenteJpaEntity toEntity(ZoneDeVente z) {
        return ZoneDeVenteJpaEntity.builder()
            .id(z.getId()).nom(z.getNom()).type(z.getType()).description(z.getDescription())
            .clientsPotentiels(z.getClientsPotentiels()).actif(z.isActif())
            .dateCreation(z.getDateCreation()).dateModification(z.getDateModification())
            .version(z.getVersion()).build();
    }

    private ZoneDeVente toDomain(ZoneDeVenteJpaEntity e) {
        return ZoneDeVente.reconstituer(e.getId(), e.getNom(), e.getType(), e.getDescription(),
            e.getClientsPotentiels(), e.isActif(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
