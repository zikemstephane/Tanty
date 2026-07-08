package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity.LigneFicheProduit;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa.FicheProduitJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa.FicheProduitJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa.LigneFicheProduitJpaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FicheProduitRepositoryImpl implements FicheProduitRepository {

    private final FicheProduitJpaRepository jpaRepository;

    public FicheProduitRepositoryImpl(FicheProduitJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public FicheProduitJournaliere sauvegarder(FicheProduitJournaliere fiche) {
        return toDomain(jpaRepository.save(toEntity(fiche)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FicheProduitJournaliere> trouverParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.findByReferencePPHAndDate(referencePPH, date).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FicheProduitJournaliere> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FicheProduitJournaliere> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.existsByReferencePPHAndDate(referencePPH, date);
    }

    private FicheProduitJpaEntity toEntity(FicheProduitJournaliere f) {
        List<LigneFicheProduitJpaEntity> lignes = f.getLignes().stream()
            .map(l -> {
                LigneFicheProduitJpaEntity e = new LigneFicheProduitJpaEntity();
                e.setCodeProduit(l.getCodeProduit());
                e.setNomProduit(l.getNomProduit());
                e.setQuantiteProduite(l.getQuantiteProduite());
                return e;
            }).toList();
        FicheProduitJpaEntity entity = new FicheProduitJpaEntity();
        entity.setId(f.getId());
        entity.setReferencePPH(f.getReferencePPH());
        entity.setSemaine(f.getSemaine());
        entity.setDate(f.getDate());
        entity.setMatriculeAgentProduction(f.getMatriculeAgentProduction());
        entity.setLignes(new ArrayList<>(lignes));
        entity.setProduitsRealises(f.getProduitsRealises().isEmpty() ? null : new java.util.HashMap<>(f.getProduitsRealises()));
        entity.setPredictionDosage(f.getPredictionDosage().isEmpty() ? null : new java.util.HashMap<>(f.getPredictionDosage()));
        entity.setPredictionPostes(f.getPredictionPostes().isEmpty() ? null : new java.util.HashMap<>(f.getPredictionPostes()));
        entity.setJustificationEcart(f.getJustificationEcart());
        entity.setVerrouillee(f.isVerrouillee());
        entity.setDateCreation(f.getDateCreation());
        entity.setDateModification(f.getDateModification());
        entity.setVersion(f.getVersion());
        return entity;
    }

    private FicheProduitJournaliere toDomain(FicheProduitJpaEntity e) {
        List<LigneFicheProduit> lignes = e.getLignes().stream()
            .map(l -> new LigneFicheProduit(l.getCodeProduit(), l.getNomProduit(), l.getQuantiteProduite()))
            .toList();
        return FicheProduitJournaliere.reconstituer(e.getId(), e.getReferencePPH(), e.getSemaine(),
            e.getDate(), e.getMatriculeAgentProduction(), lignes,
            e.getProduitsRealises() != null ? e.getProduitsRealises() : new java.util.HashMap<>(),
            e.getPredictionDosage() != null ? e.getPredictionDosage() : new java.util.HashMap<>(),
            e.getPredictionPostes() != null ? e.getPredictionPostes() : new java.util.HashMap<>(),
            e.getJustificationEcart(), e.isVerrouillee(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}