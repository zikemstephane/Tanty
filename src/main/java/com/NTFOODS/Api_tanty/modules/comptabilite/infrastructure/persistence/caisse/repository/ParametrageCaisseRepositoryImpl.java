package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.ParametrageCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.ParametrageCaisseRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.ParametrageCaisseJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.ParametrageCaisseJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ParametrageCaisseRepositoryImpl implements ParametrageCaisseRepository {

    private final ParametrageCaisseJpaRepository jpaRepository;

    public ParametrageCaisseRepositoryImpl(ParametrageCaisseJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ParametrageCaisse sauvegarder(ParametrageCaisse parametrage) {
        return toDomain(jpaRepository.save(toEntity(parametrage)));
    }

    @Override
    public Optional<ParametrageCaisse> trouverParReference(String reference) {
        return jpaRepository.findByReference(reference).map(this::toDomain);
    }

    @Override
    public Optional<ParametrageCaisse> trouverApplicableA(LocalDate date) {
        return jpaRepository.findApplicables(date).stream().findFirst().map(this::toDomain);
    }

    @Override
    public Optional<ParametrageCaisse> trouverCourant() {
        return jpaRepository.findFirstByDateFinIsNullOrderByDateDebutDesc().map(this::toDomain);
    }

    @Override
    public List<ParametrageCaisse> listerHistorique() {
        return jpaRepository.findAllByOrderByDateDebutDesc().stream().map(this::toDomain).toList();
    }

    private ParametrageCaisseJpaEntity toEntity(ParametrageCaisse p) {
        return ParametrageCaisseJpaEntity.builder()
            .id(p.getId())
            .reference(p.getReference())
            .seuilSecurisationFCFA(p.getSeuilSecurisationFCFA())
            .seuilAlerteRougeVersementFCFA(p.getSeuilAlerteRougeVersementFCFA())
            .dateDebut(p.getDateDebut())
            .dateFin(p.getDateFin())
            .matriculeAuteur(p.getMatriculeAuteur())
            .motif(p.getMotif())
            .dateCreation(p.getDateCreation())
            .version(p.getVersion())
            .build();
    }

    private ParametrageCaisse toDomain(ParametrageCaisseJpaEntity e) {
        return ParametrageCaisse.reconstituer(e.getId(), e.getReference(),
            e.getSeuilSecurisationFCFA(), e.getSeuilAlerteRougeVersementFCFA(),
            e.getDateDebut(), e.getDateFin(), e.getMatriculeAuteur(), e.getMotif(),
            e.getDateCreation(), e.getVersion());
    }
}
