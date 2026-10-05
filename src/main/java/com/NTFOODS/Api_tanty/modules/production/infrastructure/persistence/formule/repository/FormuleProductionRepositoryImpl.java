package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.LigneFormule;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.jpa.FormuleProductionJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.jpa.FormuleProductionJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.jpa.LigneFormuleJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FormuleProductionRepositoryImpl implements FormuleProductionRepository {

    private final FormuleProductionJpaRepository jpaRepository;

    public FormuleProductionRepositoryImpl(FormuleProductionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FormuleProduction sauvegarder(FormuleProduction formule) {
        return toDomain(jpaRepository.save(toEntity(formule)));
    }

    @Override
    public Optional<FormuleProduction> trouverParCodeProduit(String codeProduit) {
        return jpaRepository.findByCodeProduit(codeProduit).map(this::toDomain);
    }

    @Override
    public List<FormuleProduction> listerToutes() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void supprimerParCodeProduit(String codeProduit) {
        jpaRepository.findByCodeProduit(codeProduit).ifPresent(jpaRepository::delete);
    }

    private FormuleProductionJpaEntity toEntity(FormuleProduction formule) {
        List<LigneFormuleJpaEntity> lignes = formule.getLignes().stream()
            .map(l -> LigneFormuleJpaEntity.builder()
                .matierePremiere(l.getMatierePremiere())
                .proportionKg(l.getProportionKg())
                .build())
            .toList();
        FormuleProductionJpaEntity entite = FormuleProductionJpaEntity.builder()
            .id(formule.getId())
            .codeProduit(formule.getCodeProduit())
            .baseKg(formule.getBaseKg())
            .lignes(lignes)
            .dateCreation(formule.getDateCreation())
            .dateModification(formule.getDateModification())
            .version(formule.getVersion())
            .build();
        entite.getLignes().forEach(ligne -> ligne.setFormule(entite));
        return entite;
    }

    private FormuleProduction toDomain(FormuleProductionJpaEntity e) {
        List<LigneFormuleJpaEntity> lignesEntities = e.getLignes();
        if (lignesEntities == null) lignesEntities = List.of();
        List<LigneFormule> lignes = lignesEntities.stream()
            .map(l -> new LigneFormule(l.getMatierePremiere(), l.getProportionKg()))
            .toList();
        return FormuleProduction.reconstituer(e.getId(), e.getCodeProduit(), e.getBaseKg(), lignes,
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
