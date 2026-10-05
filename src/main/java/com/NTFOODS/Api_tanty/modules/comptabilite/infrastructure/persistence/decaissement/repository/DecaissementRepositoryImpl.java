package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate.Decaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.jpa.DecaissementJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.jpa.DecaissementJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DecaissementRepositoryImpl implements DecaissementRepository {

    private final DecaissementJpaRepository jpaRepository;

    public DecaissementRepositoryImpl(DecaissementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Decaissement sauvegarder(Decaissement d) {
        return toDomain(jpaRepository.save(toEntity(d)));
    }

    @Override
    public Optional<Decaissement> trouverParNumero(String numero) {
        return jpaRepository.findByNumeroDecaissement(numero).map(this::toDomain);
    }

    @Override
    public List<Decaissement> listerParStatut(StatutDecaissement statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Decaissement> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateDepenseBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Decaissement> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public double totalDecaissementsParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateDepenseBetween(debut, fin).stream()
            .filter(e -> e.getStatut() == StatutDecaissement.EXECUTE)
            .mapToDouble(DecaissementJpaEntity::getMontantFCFA).sum();
    }

    private DecaissementJpaEntity toEntity(Decaissement d) {
        return DecaissementJpaEntity.builder()
            .id(d.getId()).numeroDecaissement(d.getNumeroDecaissement()).typeDepense(d.getTypeDepense())
            .matriculeProposant(d.getMatriculeProposant()).matriculeValidateurCG(d.getMatriculeValidateurCG())
            .matriculeValidateurDG(d.getMatriculeValidateurDG()).matriculeExecuteur(d.getMatriculeExecuteur())
            .montantFCFA(d.getMontantFCFA()).beneficiaire(d.getBeneficiaire()).dateDepense(d.getDateDepense())
            .motif(d.getMotif()).motifAnnulation(d.getMotifAnnulation()).statut(d.getStatut())
            .dateCreation(d.getDateCreation()).dateModification(d.getDateModification())
            .version(d.getVersion()).build();
    }

    private Decaissement toDomain(DecaissementJpaEntity e) {
        return Decaissement.reconstituer(e.getId(), e.getNumeroDecaissement(), e.getTypeDepense(),
            e.getMatriculeProposant(), e.getMatriculeValidateurCG(), e.getMatriculeValidateurDG(),
            e.getMatriculeExecuteur(), e.getMontantFCFA(), e.getBeneficiaire(), e.getDateDepense(),
            e.getMotif(), e.getMotifAnnulation(), e.getStatut(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
