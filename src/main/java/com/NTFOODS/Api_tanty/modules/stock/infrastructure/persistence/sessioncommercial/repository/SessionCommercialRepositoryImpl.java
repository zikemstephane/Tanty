package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.jpa.SessionCommercialJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.jpa.SessionCommercialJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionCommercialRepositoryImpl implements SessionCommercialRepository {

    private final SessionCommercialJpaRepository jpaRepository;

    public SessionCommercialRepositoryImpl(SessionCommercialJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SessionCommercial sauvegarder(SessionCommercial session) {
        return toDomain(jpaRepository.save(toEntity(session)));
    }

    @Override
    public Optional<SessionCommercial> trouverParNumero(String numero) {
        return jpaRepository.findByNumeroSession(numero).map(this::toDomain);
    }

    @Override
    public Optional<SessionCommercial> trouverSessionOuverteParCommercial(String matriculeCommercial) {
        return jpaRepository.findFirstByMatriculeCommercialAndStatutInOrderByDateCreationDesc(
            matriculeCommercial, List.of(StatutSession.PREPARATION, StatutSession.OUVERTE)
        ).map(this::toDomain);
    }

    @Override
    public List<SessionCommercial> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<SessionCommercial> listerParDateEffet(LocalDate dateEffet) {
        return jpaRepository.findByDateEffet(dateEffet).stream().map(this::toDomain).toList();
    }

    @Override
    public List<SessionCommercial> listerParStatut(String statut) {
        return jpaRepository.findByStatut(StatutSession.valueOf(statut)).stream().map(this::toDomain).toList();
    }

    private SessionCommercialJpaEntity toEntity(SessionCommercial s) {
        return SessionCommercialJpaEntity.builder()
            .id(s.getId()).numeroSession(s.getNumeroSession()).matriculeCommercial(s.getMatriculeCommercial())
            .datePreparation(s.getDatePreparation()).dateEffet(s.getDateEffet()).statut(s.getStatut())
            .dotationsIds(s.getDotationsIds()).matriculeCloture(s.getMatriculeCloture())
            .matriculeValidateurSecretaire(s.getMatriculeValidateurSecretaire())
            .matriculeValidateurComptable(s.getMatriculeValidateurComptable())
            .heureOuverture(s.getHeureOuverture()).heureCloture(s.getHeureCloture())
            .dateCreation(s.getDateCreation()).dateModification(s.getDateModification())
            .version(s.getVersion()).build();
    }

    private SessionCommercial toDomain(SessionCommercialJpaEntity e) {
        return SessionCommercial.reconstituer(e.getId(), e.getNumeroSession(), e.getMatriculeCommercial(),
            e.getDatePreparation(), e.getDateEffet(), e.getStatut(), e.getDotationsIds(),
            e.getMatriculeCloture(), e.getMatriculeValidateurSecretaire(), e.getMatriculeValidateurComptable(),
            e.getHeureOuverture(), e.getHeureCloture(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
