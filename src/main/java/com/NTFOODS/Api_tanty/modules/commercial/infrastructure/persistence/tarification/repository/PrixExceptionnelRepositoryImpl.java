package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.PrixExceptionnel;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.PrixExceptionnelRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.PrixExceptionnelJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.PrixExceptionnelJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class PrixExceptionnelRepositoryImpl implements PrixExceptionnelRepository {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PrixExceptionnelJpaRepository jpaRepository;

    public PrixExceptionnelRepositoryImpl(PrixExceptionnelJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PrixExceptionnel sauvegarder(PrixExceptionnel prixExceptionnel) {
        return toDomain(jpaRepository.save(toEntity(prixExceptionnel)));
    }

    @Override
    public Optional<PrixExceptionnel> trouverParReference(String reference) {
        return jpaRepository.findByReference(reference).map(this::toDomain);
    }

    @Override
    public List<PrixExceptionnel> listerParClient(String codeClient) {
        return jpaRepository.findByCodeClient(codeClient).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PrixExceptionnel> listerParAutorisateur(String matriculeAutorisateur) {
        return jpaRepository.findByMatriculeAutorisateur(matriculeAutorisateur).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PrixExceptionnel> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateAutorisationBetween(debut.atStartOfDay(), fin.atTime(LocalTime.MAX))
            .stream().map(this::toDomain).toList();
    }

    @Override
    public String genererReference() {
        long compteurDuJour = jpaRepository.countByDateCreationAfter(LocalDate.now().atStartOfDay()) + 1;
        return "PEX-" + LocalDate.now().format(FMT) + "-" + String.format("%04d", compteurDuJour);
    }

    private PrixExceptionnelJpaEntity toEntity(PrixExceptionnel p) {
        return PrixExceptionnelJpaEntity.builder()
            .id(p.getId()).reference(p.getReference()).codeProduit(p.getCodeProduit())
            .codeClient(p.getCodeClient()).prixHomologueFCFA(p.getPrixHomologueFCFA())
            .prixAppliqueFCFA(p.getPrixAppliqueFCFA()).motif(p.getMotif())
            .matriculeAutorisateur(p.getMatriculeAutorisateur()).referenceVenteCible(p.getReferenceVenteCible())
            .dateAutorisation(p.getDateAutorisation()).dateCreation(p.getDateCreation()).version(p.getVersion())
            .build();
    }

    private PrixExceptionnel toDomain(PrixExceptionnelJpaEntity e) {
        return PrixExceptionnel.reconstituer(e.getId(), e.getReference(), e.getCodeProduit(), e.getCodeClient(),
            e.getPrixHomologueFCFA(), e.getPrixAppliqueFCFA(), e.getMotif(), e.getMatriculeAutorisateur(),
            e.getReferenceVenteCible(), e.getDateAutorisation(), e.getDateCreation(), e.getVersion());
    }
}
