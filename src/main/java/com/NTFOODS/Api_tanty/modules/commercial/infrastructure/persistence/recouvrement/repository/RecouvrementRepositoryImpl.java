package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.jpa.RecouvrementJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.jpa.RecouvrementJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecouvrementRepositoryImpl implements RecouvrementRepository {

    private final RecouvrementJpaRepository jpaRepository;

    public RecouvrementRepositoryImpl(RecouvrementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Recouvrement sauvegarder(Recouvrement recouvrement) {
        return toDomain(jpaRepository.save(toEntity(recouvrement)));
    }

    @Override
    public Optional<Recouvrement> trouverParReference(String reference) {
        return jpaRepository.findByReferenceRecouvrement(reference).map(this::toDomain);
    }

    @Override
    public List<Recouvrement> listerParClient(String codeClient) {
        return jpaRepository.findByCodeClient(codeClient).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Recouvrement> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Recouvrement> listerParStatut(StatutRecouvrement statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Recouvrement> listerEnContentieux() {
        return jpaRepository.findByStatut(StatutRecouvrement.CONTENTIEUX).stream().map(this::toDomain).toList();
    }

    private RecouvrementJpaEntity toEntity(Recouvrement r) {
        return RecouvrementJpaEntity.builder()
            .id(r.getId()).referenceRecouvrement(r.getReferenceRecouvrement())
            .codeClient(r.getCodeClient()).nomClient(r.getNomClient())
            .montantDu(r.getMontantDu()).montantRembourse(r.getMontantRembourse())
            .dateEcheance(r.getDateEcheance()).statut(r.getStatut())
            .matriculeCommercial(r.getMatriculeCommercial()).observations(r.getObservations())
            .dateCreation(r.getDateCreation()).dateModification(r.getDateModification())
            .version(r.getVersion()).build();
    }

    private Recouvrement toDomain(RecouvrementJpaEntity e) {
        return Recouvrement.reconstituer(e.getId(), e.getReferenceRecouvrement(), e.getCodeClient(),
            e.getNomClient(), e.getMontantDu(), e.getMontantRembourse(), e.getDateEcheance(),
            e.getStatut(), e.getMatriculeCommercial(), e.getObservations(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
