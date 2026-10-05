package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.jpa.SessionDosageJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.jpa.SessionDosageJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionDosageRepositoryImpl implements SessionDosageRepository {

    private final SessionDosageJpaRepository jpaRepository;

    public SessionDosageRepositoryImpl(SessionDosageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public SessionDosage sauvegarder(SessionDosage session) {
        return toDomain(jpaRepository.save(toEntity(session)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionDosage> trouverParId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionDosage> trouverParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.findByReferencePPHAndDate(referencePPH, date).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDosage> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDosage> listerParDate(LocalDate date) {
        return jpaRepository.findByDate(date).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDosage> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDosage> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private SessionDosageJpaEntity toEntity(SessionDosage s) {
        var matieres = s.getMatieresUtilisees().stream()
            .map(m -> SessionDosageJpaEntity.MatierePremiereJpa.builder()
                .typeMatiere(m.getTypeMatiere()).quantiteKg(m.getQuantiteKg()).build())
            .toList();
        return SessionDosageJpaEntity.builder()
            .id(s.getId()).referencePPH(s.getReferencePPH()).date(s.getDate())
            .matriculeAgentDoseur(s.getMatriculeAgentDoseur())
            .matieresUtilisees(matieres).machinesMobilisees(s.getMachinesMobilisees())
            .nbFutsProduits(s.getNbFutsProduits()).nbFutsCasses(s.getNbFutsCasses())
            .justificationEcart(s.getJustificationEcart()).validee(s.isValidee()).enPause(s.isEnPause())
            .dateCreation(s.getDateCreation()).dateModification(s.getDateModification())
            .version(s.getVersion()).build();
    }

    private SessionDosage toDomain(SessionDosageJpaEntity e) {
        var matieres = e.getMatieresUtilisees().stream()
            .map(m -> new SessionDosage.MatierePremiereUtilisee(m.getTypeMatiere(), m.getQuantiteKg()))
            .toList();
        SessionDosage s = SessionDosage.reconstituer(e.getId(), e.getReferencePPH(), e.getDate(),
            e.getMatriculeAgentDoseur(), matieres, e.getMachinesMobilisees(),
            e.getNbFutsProduits(), e.getNbFutsCasses(), e.getJustificationEcart(), e.isValidee(), e.isEnPause(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
        return s;
    }
}