package com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.repository;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.CampagneMarketing;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository.CampagneMarketingRepository;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.CampagneMarketingJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.CampagneMarketingJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CampagneMarketingRepositoryImpl implements CampagneMarketingRepository {

    private final CampagneMarketingJpaRepository jpaRepository;

    public CampagneMarketingRepositoryImpl(CampagneMarketingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CampagneMarketing sauvegarder(CampagneMarketing campagne) {
        return toDomain(jpaRepository.save(toEntity(campagne)));
    }

    @Override
    public Optional<CampagneMarketing> trouverParCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public List<CampagneMarketing> listerTous() {
        return jpaRepository.findAllByOrderByDateCreationDesc().stream().map(this::toDomain).toList();
    }

    @Override
    public List<CampagneMarketing> listerParStatut(StatutCampagne statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<CampagneMarketing> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateDebutBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    private CampagneMarketingJpaEntity toEntity(CampagneMarketing c) {
        CampagneMarketingJpaEntity e = new CampagneMarketingJpaEntity();
        e.setId(c.getId());
        e.setCode(c.getCode());
        e.setLibelle(c.getLibelle());
        e.setDescription(c.getDescription());
        e.setType(c.getType());
        e.setDateDebut(c.getDateDebut());
        e.setDateFin(c.getDateFin());
        e.setZoneCible(c.getZoneCible());
        e.setBudgetPrevuFCFA(c.getBudgetPrevuFCFA());
        e.setBudgetDepenseFCFA(c.getBudgetDepenseFCFA());
        e.setStatut(c.getStatut());
        e.setMatriculeCreateur(c.getMatriculeCreateur());
        e.setDateCreation(c.getDateCreation());
        e.setDateModification(c.getDateModification());
        e.setPromotionsLieesCsv(String.join(",", c.getPromotionsLiees()));
        return e;
    }

    private CampagneMarketing toDomain(CampagneMarketingJpaEntity e) {
        List<String> promotions = (e.getPromotionsLieesCsv() != null && !e.getPromotionsLieesCsv().isBlank())
            ? Arrays.asList(e.getPromotionsLieesCsv().split(","))
            : Collections.emptyList();
        return CampagneMarketing.reconstituer(
            e.getId(), e.getCode(), e.getLibelle(), e.getDescription(),
            e.getType(), e.getDateDebut(), e.getDateFin(),
            e.getZoneCible(), e.getBudgetPrevuFCFA(), e.getBudgetDepenseFCFA(),
            e.getStatut(), e.getMatriculeCreateur(), e.getDateCreation(),
            e.getDateModification(), promotions
        );
    }
}
