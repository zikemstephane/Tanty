package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.FactureAvoir;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.FactureAvoirRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.FactureAvoirJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.FactureAvoirJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class FactureAvoirRepositoryImpl implements FactureAvoirRepository {

    private final FactureAvoirJpaRepository jpaRepository;

    public FactureAvoirRepositoryImpl(FactureAvoirJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FactureAvoir sauvegarder(FactureAvoir avoir) {
        return toDomain(jpaRepository.save(toEntity(avoir)));
    }

    @Override
    public Optional<FactureAvoir> trouverParNumero(String numeroAvoir) {
        return jpaRepository.findByNumeroAvoir(numeroAvoir).map(this::toDomain);
    }

    @Override
    public List<FactureAvoir> listerParVenteOriginale(String numeroVenteOriginale) {
        return jpaRepository.findByNumeroVenteOriginale(numeroVenteOriginale).stream().map(this::toDomain).toList();
    }

    @Override
    public List<FactureAvoir> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public String genererNumero(LocalDate date) {
        long count = jpaRepository.countByDateAvoir(date) + 1;
        return "AVOIR-" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%03d", count);
    }

    private FactureAvoirJpaEntity toEntity(FactureAvoir a) {
        return FactureAvoirJpaEntity.builder()
            .id(a.getId()).numeroAvoir(a.getNumeroAvoir()).numeroVenteOriginale(a.getNumeroVenteOriginale())
            .matriculeCommercial(a.getMatriculeCommercial()).codeClient(a.getCodeClient())
            .matriculeComptable(a.getMatriculeComptable()).montantHT(a.getMontantHT())
            .montantTVA(a.getMontantTVA()).montantTTC(a.getMontantTTC()).motif(a.getMotif())
            .dateAvoir(a.getDateAvoir()).statut(a.getStatut()).dateCreation(a.getDateCreation())
            .dateValidation(a.getDateValidation()).version(a.getVersion()).build();
    }

    private FactureAvoir toDomain(FactureAvoirJpaEntity e) {
        return FactureAvoir.reconstituer(e.getId(), e.getNumeroAvoir(), e.getNumeroVenteOriginale(),
            e.getMatriculeCommercial(), e.getCodeClient(), e.getMatriculeComptable(),
            e.getMontantHT(), e.getMontantTVA(), e.getMotif(), e.getDateAvoir(), e.getStatut(),
            e.getDateCreation(), e.getDateValidation(), e.getVersion());
    }
}
