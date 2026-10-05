package com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.repository;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.Promotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository.PromotionRepository;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.PromotionJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.PromotionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PromotionRepositoryImpl implements PromotionRepository {

    private final PromotionJpaRepository jpaRepository;

    public PromotionRepositoryImpl(PromotionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Promotion sauvegarder(Promotion promotion) {
        return toDomain(jpaRepository.save(toEntity(promotion)));
    }

    @Override
    public Optional<Promotion> trouverParCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public List<Promotion> listerTous() {
        return jpaRepository.findAllByOrderByDateCreationDesc().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Promotion> listerParStatut(StatutPromotion statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Promotion> listerActives() {
        return jpaRepository.findByStatut(StatutPromotion.ACTIVEE).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Promotion> listerParProduit(String codeProduit) {
        return jpaRepository.findByCodeProduit(codeProduit).stream().map(this::toDomain).toList();
    }

    private PromotionJpaEntity toEntity(Promotion p) {
        PromotionJpaEntity e = new PromotionJpaEntity();
        e.setId(p.getId());
        e.setCode(p.getCode());
        e.setLibelle(p.getLibelle());
        e.setDescription(p.getDescription());
        e.setType(p.getType());
        e.setValeurReduction(p.getValeurReduction());
        e.setCodeProduit(p.getCodeProduit());
        e.setSegmentCible(p.getSegmentCible());
        e.setDateDebut(p.getDateDebut());
        e.setDateFin(p.getDateFin());
        e.setStatut(p.getStatut());
        e.setNombreUtilisations(p.getNombreUtilisations());
        e.setLimiteUtilisations(p.getLimiteUtilisations());
        e.setMatriculeCreateur(p.getMatriculeCreateur());
        e.setDateCreation(p.getDateCreation());
        e.setDateModification(p.getDateModification());
        return e;
    }

    private Promotion toDomain(PromotionJpaEntity e) {
        return Promotion.reconstituer(
            e.getId(), e.getCode(), e.getLibelle(), e.getDescription(),
            e.getType(), e.getValeurReduction(), e.getCodeProduit(),
            e.getSegmentCible(), e.getDateDebut(), e.getDateFin(),
            e.getStatut(), e.getNombreUtilisations(), e.getLimiteUtilisations(),
            e.getMatriculeCreateur(), e.getDateCreation(), e.getDateModification()
        );
    }
}
