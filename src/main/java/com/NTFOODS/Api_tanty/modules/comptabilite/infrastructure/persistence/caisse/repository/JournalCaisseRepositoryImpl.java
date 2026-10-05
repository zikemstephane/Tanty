package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.JournalCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.entity.LigneCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.JournalCaisseRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.JournalCaisseJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.JournalCaisseJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.LigneCaisseJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JournalCaisseRepositoryImpl implements JournalCaisseRepository {

    private final JournalCaisseJpaRepository jpaRepository;

    public JournalCaisseRepositoryImpl(JournalCaisseJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public JournalCaisse sauvegarder(JournalCaisse journal) {
        JournalCaisseJpaEntity entity = toEntity(journal);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<JournalCaisse> trouverParDate(LocalDate date) {
        return jpaRepository.findByDate(date).map(this::toDomain);
    }

    @Override
    public List<JournalCaisse> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existeParDate(LocalDate date) {
        return jpaRepository.existsByDate(date);
    }

    private JournalCaisseJpaEntity toEntity(JournalCaisse j) {
        List<LigneCaisseJpaEntity> lignes = j.getLignes().stream()
            .map(l -> LigneCaisseJpaEntity.builder()
                .referenceOperation(l.getReferenceOperation())
                .typeMouvement(l.getTypeMouvement())
                .montant(l.getMontant())
                .motif(l.getMotif())
                .dateOperation(l.getDateOperation())
                .matriculeOperateur(l.getMatriculeOperateur())
                .build())
            .toList();
        return JournalCaisseJpaEntity.builder()
            .id(j.getId())
            .date(j.getDate())
            .soldeCourant(j.getSoldeCourant())
            .seuilSecurisation(j.getSeuilSecurisation())
            .lignes(lignes)
            .dateCreation(j.getDateCreation())
            .dateModification(j.getDateModification())
            .version(j.getVersion())
            .build();
    }

    private JournalCaisse toDomain(JournalCaisseJpaEntity e) {
        List<LigneCaisse> lignes = e.getLignes().stream()
            .map(l -> new LigneCaisse(l.getReferenceOperation(), l.getTypeMouvement(),
                l.getMontant(), l.getMotif(), l.getDateOperation(), l.getMatriculeOperateur()))
            .toList();
        return JournalCaisse.reconstituer(e.getId(), e.getDate(), e.getSoldeCourant(),
            e.getSeuilSecurisation(), lignes, e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
