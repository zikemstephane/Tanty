package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.StatutAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model.Alerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa.AlerteJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa.AlerteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AlerteRepositoryImpl implements AlerteRepository {

    private final AlerteJpaRepository jpaRepository;

    public AlerteRepositoryImpl(AlerteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Alerte sauvegarder(Alerte alerte) {
        return toDomain(jpaRepository.save(toEntity(alerte)));
    }

    @Override
    public Optional<Alerte> trouverParReference(String reference) {
        return jpaRepository.findByReference(reference).map(this::toDomain);
    }

    @Override
    public List<Alerte> trouverParStatut(StatutAlerte statut) {
        return jpaRepository.findByStatutOrderByDateCreationDesc(statut.name()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Alerte> trouverParDestinataire(String role) {
        return jpaRepository.findByDestinataireRoleOrderByDateCreationDesc(role).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Alerte> trouverTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existeAlerteActive(TypeAlerte type, String codeProduit, String codeEntrepot) {
        return jpaRepository.existsByTypeAndCodeProduitAndCodeEntrepotAndStatutIn(
            type.name(), codeProduit, codeEntrepot, List.of(StatutAlerte.NOUVELLE.name(), StatutAlerte.LUE.name()));
    }

    @Override
    public long compter() {
        return jpaRepository.count();
    }

    private AlerteJpaEntity toEntity(Alerte a) {
        return AlerteJpaEntity.builder()
            .id(a.getId()).reference(a.getReference()).type(a.getType().name()).message(a.getMessage())
            .destinataireRole(a.getDestinataireRole()).actionSuggeree(a.getActionSuggeree())
            .codeProduit(a.getCodeProduit()).codeEntrepot(a.getCodeEntrepot()).referenceLiee(a.getReferenceLiee())
            .statut(a.getStatut().name()).matriculeTraitant(a.getMatriculeTraitant())
            .dateCreation(a.getDateCreation()).dateLecture(a.getDateLecture()).dateTraitement(a.getDateTraitement())
            .version(a.getVersion())
            .build();
    }

    private Alerte toDomain(AlerteJpaEntity e) {
        return Alerte.reconstituer(e.getId(), e.getReference(), TypeAlerte.valueOf(e.getType()), e.getMessage(),
            e.getCodeProduit(), e.getCodeEntrepot(), e.getReferenceLiee(), StatutAlerte.valueOf(e.getStatut()),
            e.getMatriculeTraitant(), e.getDateCreation(), e.getDateLecture(), e.getDateTraitement(), e.getVersion());
    }
}
