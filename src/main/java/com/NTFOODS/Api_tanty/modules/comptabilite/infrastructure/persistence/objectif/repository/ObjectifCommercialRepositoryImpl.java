package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ObjectifCommercialJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ObjectifCommercialJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class ObjectifCommercialRepositoryImpl implements ObjectifCommercialRepository {

    private final ObjectifCommercialJpaRepository jpaRepository;

    public ObjectifCommercialRepositoryImpl(ObjectifCommercialJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ObjectifCommercial sauvegarder(ObjectifCommercial objectif) {
        return toDomain(jpaRepository.save(toEntity(objectif)));
    }

    @Override
    public Optional<ObjectifCommercial> trouver(String matriculeCommercial, LocalDate semaineDebut) {
        return jpaRepository.findByMatriculeCommercialAndSemaineDebut(matriculeCommercial, semaineDebut).map(this::toDomain);
    }

    @Override
    public List<ObjectifCommercial> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ObjectifCommercial> listerParSemaine(LocalDate semaineDebut) {
        return jpaRepository.findBySemaineDebut(semaineDebut).stream().map(this::toDomain).toList();
    }

    private ObjectifCommercialJpaEntity toEntity(ObjectifCommercial o) {
        ObjectifCommercialJpaEntity e = new ObjectifCommercialJpaEntity();
        e.setId(o.getId());
        e.setMatriculeCommercial(o.getMatriculeCommercial());
        e.setSemaineDebut(o.getSemaineDebut());
        e.setSemaineFin(o.getSemaineFin());
        e.setObjectifGlobalFCFA(o.getObjectifGlobalFCFA());
        e.setObjectifsParGammeFCFA(new HashMap<>(o.getObjectifsParGammeFCFA()));
        e.setMatriculeAuteur(o.getMatriculeAuteur());
        e.setDateCreation(o.getDateCreation());
        e.setDateModification(o.getDateModification());
        e.setVersion(o.getVersion());
        return e;
    }

    private ObjectifCommercial toDomain(ObjectifCommercialJpaEntity e) {
        return ObjectifCommercial.reconstituer(e.getId(), e.getMatriculeCommercial(), e.getSemaineDebut(),
            e.getSemaineFin(), e.getObjectifGlobalFCFA(), new HashMap<>(e.getObjectifsParGammeFCFA()),
            e.getMatriculeAuteur(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
