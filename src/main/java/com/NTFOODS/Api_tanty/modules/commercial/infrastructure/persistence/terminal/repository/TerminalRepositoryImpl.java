package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.aggregate.Terminal;
import com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.repository.TerminalRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.jpa.TerminalJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.jpa.TerminalJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TerminalRepositoryImpl implements TerminalRepository {

    private final TerminalJpaRepository jpaRepository;

    public TerminalRepositoryImpl(TerminalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Terminal sauvegarder(Terminal terminal) {
        return toDomain(jpaRepository.save(toEntity(terminal)));
    }

    @Override
    public Optional<Terminal> trouverParImei(String imei) {
        return jpaRepository.findByImei(imei).map(this::toDomain);
    }

    @Override
    public Optional<Terminal> trouverParMatricule(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).map(this::toDomain);
    }

    @Override
    public List<Terminal> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Terminal> listerActifs() {
        return jpaRepository.findByActifTrue().stream().map(this::toDomain).toList();
    }

    private TerminalJpaEntity toEntity(Terminal t) {
        return TerminalJpaEntity.builder()
            .id(t.getId()).imei(t.getImei()).modele(t.getModele())
            .matriculeCommercial(t.getMatriculeCommercial())
            .prixAchat(t.getPrixAchat()).resteAPayer(t.getResteAPayer())
            .actif(t.isActif()).dateDerniereConnexion(t.getDateDerniereConnexion())
            .dateCreation(t.getDateCreation()).dateModification(t.getDateModification())
            .version(t.getVersion()).build();
    }

    private Terminal toDomain(TerminalJpaEntity e) {
        return Terminal.reconstituer(e.getId(), e.getImei(), e.getModele(), e.getMatriculeCommercial(),
            e.getPrixAchat(), e.getResteAPayer(), e.isActif(), e.getDateDerniereConnexion(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
