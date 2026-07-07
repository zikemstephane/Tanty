package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.aggregate.Vehicule;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.repository.VehiculeRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.jpa.VehiculeJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.jpa.VehiculeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VehiculeRepositoryImpl implements VehiculeRepository {

    private final VehiculeJpaRepository jpaRepository;

    public VehiculeRepositoryImpl(VehiculeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Vehicule sauvegarder(Vehicule vehicule) {
        return toDomain(jpaRepository.save(toEntity(vehicule)));
    }

    @Override
    public Optional<Vehicule> trouverParImmatriculation(String immatriculation) {
        return jpaRepository.findByImmatriculation(immatriculation).map(this::toDomain);
    }

    @Override
    public List<Vehicule> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Vehicule> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Vehicule> listerActifs() {
        return jpaRepository.findByActifTrue().stream().map(this::toDomain).toList();
    }

    private VehiculeJpaEntity toEntity(Vehicule v) {
        return VehiculeJpaEntity.builder()
            .id(v.getId()).immatriculation(v.getImmatriculation()).marque(v.getMarque())
            .modele(v.getModele()).matriculeCommercial(v.getMatriculeCommercial())
            .actif(v.isActif()).consommationMoyenneL100(v.getConsommationMoyenneL100())
            .dateCreation(v.getDateCreation()).dateModification(v.getDateModification())
            .version(v.getVersion()).build();
    }

    private Vehicule toDomain(VehiculeJpaEntity e) {
        return Vehicule.reconstituer(e.getId(), e.getImmatriculation(), e.getMarque(), e.getModele(),
            e.getMatriculeCommercial(), e.isActif(), e.getConsommationMoyenneL100(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
