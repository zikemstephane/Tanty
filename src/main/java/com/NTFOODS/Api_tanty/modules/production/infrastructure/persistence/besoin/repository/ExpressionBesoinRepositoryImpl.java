package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.model.ExpressionBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository.ExpressionBesoinRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.jpa.ExpressionBesoinJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.jpa.ExpressionBesoinJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExpressionBesoinRepositoryImpl implements ExpressionBesoinRepository {

    private final ExpressionBesoinJpaRepository jpaRepository;

    public ExpressionBesoinRepositoryImpl(ExpressionBesoinJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ExpressionBesoin sauvegarder(ExpressionBesoin besoin) {
        return toDomain(jpaRepository.save(toEntity(besoin)));
    }

    @Override
    public Optional<ExpressionBesoin> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(this::toDomain);
    }

    @Override
    public List<ExpressionBesoin> listerParStatut(StatutBesoin statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ExpressionBesoin> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existeParNumero(String numero) {
        return jpaRepository.existsByNumero(numero);
    }

    private ExpressionBesoinJpaEntity toEntity(ExpressionBesoin b) {
        return ExpressionBesoinJpaEntity.builder()
            .id(b.getId()).numero(b.getNumero()).referencePPH(b.getReferencePPH())
            .type(b.getType()).codeProduit(b.getCodeProduit()).designationProduit(b.getDesignationProduit())
            .quantiteDemandee(b.getQuantiteDemandee()).unite(b.getUnite())
            .matriculeDemandeur(b.getMatriculeDemandeur()).dateBesoin(b.getDateBesoin())
            .motif(b.getMotif()).statut(b.getStatut()).matriculeValidateur(b.getMatriculeValidateur())
            .motifRejet(b.getMotifRejet()).dateCreation(b.getDateCreation())
            .dateModification(b.getDateModification()).version(b.getVersion()).build();
    }

    private ExpressionBesoin toDomain(ExpressionBesoinJpaEntity e) {
        return ExpressionBesoin.reconstituer(e.getId(), e.getNumero(), e.getReferencePPH(), e.getType(),
            e.getCodeProduit(), e.getDesignationProduit(), e.getQuantiteDemandee(), e.getUnite(),
            e.getMatriculeDemandeur(), e.getDateBesoin(), e.getMotif(), e.getStatut(),
            e.getMatriculeValidateur(), e.getMotifRejet(), e.getDateCreation(),
            e.getDateModification(), e.getVersion());
    }
}
