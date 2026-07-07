package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.aggregate.Facture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.entity.LigneFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.repository.FactureRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa.FactureJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa.FactureJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa.LigneFactureJpaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class FactureRepositoryImpl implements FactureRepository {

    private final FactureJpaRepository jpaRepository;

    public FactureRepositoryImpl(FactureJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Facture sauvegarder(Facture facture) {
        FactureJpaEntity entity = toEntity(facture);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Facture> trouverParNumero(String numeroFacture) {
        return jpaRepository.findByNumeroFacture(numeroFacture).map(this::toDomain);
    }

    @Override
    public List<Facture> listerParClient(String codeClient) {
        return jpaRepository.findByCodeClient(codeClient).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Facture> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateEmissionBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Facture> listerParStatut(StatutFacture statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Facture> listerParType(TypeFacture typeFacture) {
        return jpaRepository.findByTypeFacture(typeFacture).stream().map(this::toDomain).toList();
    }

    @Override
    public String genererNumero(TypeFacture typeFacture, LocalDate date) {
        String prefix = (typeFacture == TypeFacture.AVOIR ? "AV" : "FA")
            + "-" + date.format(DateTimeFormatter.ofPattern("yyyyMM")) + "-";
        long count = jpaRepository.countByTypeFactureAndDateEmission(typeFacture, date) + 1;
        return prefix + String.format("%04d", count);
    }

    @Override
    public Page<Facture> listerParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable) {
        return jpaRepository.findByDateEmissionBetweenOrderByDateEmissionDesc(debut, fin, pageable).map(this::toDomain);
    }

    @Override
    public Page<Facture> listerParStatutPagine(StatutFacture statut, Pageable pageable) {
        return jpaRepository.findByStatutOrderByDateEmissionDesc(statut, pageable).map(this::toDomain);
    }

    @Override
    public Page<Facture> listerParClientPagine(String codeClient, Pageable pageable) {
        return jpaRepository.findByCodeClientOrderByDateEmissionDesc(codeClient, pageable).map(this::toDomain);
    }

    private FactureJpaEntity toEntity(Facture f) {
        List<LigneFactureJpaEntity> lignes = f.getLignes().stream()
            .map(l -> LigneFactureJpaEntity.builder()
                .referenceArticle(l.getReferenceArticle())
                .designation(l.getDesignation())
                .quantite(l.getQuantite())
                .prixUnitaireHT(l.getPrixUnitaireHT())
                .tauxTVA(l.getTauxTVA())
                .remise(l.getRemise())
                .build())
            .toList();
        return FactureJpaEntity.builder()
            .id(f.getId())
            .numeroFacture(f.getNumeroFacture())
            .codeClient(f.getCodeClient())
            .typeFacture(f.getTypeFacture())
            .referenceFactureOrigine(f.getReferenceFactureOrigine())
            .dateEmission(f.getDateEmission())
            .lignes(lignes)
            .statut(f.getStatut())
            .matriculeEmetteur(f.getMatriculeEmetteur())
            .dateCreation(f.getDateCreation())
            .dateModification(f.getDateModification())
            .version(f.getVersion())
            .build();
    }

    private Facture toDomain(FactureJpaEntity e) {
        List<LigneFacture> lignes = e.getLignes().stream()
            .map(l -> new LigneFacture(l.getReferenceArticle(), l.getDesignation(),
                l.getQuantite(), l.getPrixUnitaireHT(), l.getTauxTVA(), l.getRemise()))
            .toList();
        return Facture.reconstituer(e.getId(), e.getNumeroFacture(), e.getCodeClient(),
            e.getTypeFacture(), e.getReferenceFactureOrigine(), e.getDateEmission(),
            lignes, e.getStatut(), e.getMatriculeEmetteur(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
