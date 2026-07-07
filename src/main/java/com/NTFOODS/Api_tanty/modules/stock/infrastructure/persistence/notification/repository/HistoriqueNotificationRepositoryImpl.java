package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.HistoriqueNotification;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository.HistoriqueNotificationRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa.HistoriqueNotificationJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa.HistoriqueNotificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HistoriqueNotificationRepositoryImpl implements HistoriqueNotificationRepository {

    private final HistoriqueNotificationJpaRepository jpaRepository;

    public HistoriqueNotificationRepositoryImpl(HistoriqueNotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public HistoriqueNotification sauvegarder(HistoriqueNotification notification) {
        HistoriqueNotificationJpaEntity entity = toEntity(notification);
        HistoriqueNotificationJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<HistoriqueNotification> trouverParId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<HistoriqueNotification> trouverParMatricule(String matricule) {
        return jpaRepository.findByMatriculeDestinataireOrderByDateCreationDesc(matricule)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<HistoriqueNotification> trouverParRole(String role) {
        return jpaRepository.findByDestinataireRoleOrderByDateCreationDesc(role)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<HistoriqueNotification> trouverNonLusParMatricule(String matricule) {
        return jpaRepository.findByMatriculeDestinataireAndLuFalseOrderByDateCreationDesc(matricule)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<HistoriqueNotification> trouverTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void supprimerParId(Long id) {
        jpaRepository.deleteById(id);
    }

    private HistoriqueNotificationJpaEntity toEntity(HistoriqueNotification h) {
        HistoriqueNotificationJpaEntity entity = h.getId() != null
            ? jpaRepository.findById(h.getId()).orElse(new HistoriqueNotificationJpaEntity())
            : new HistoriqueNotificationJpaEntity();
        entity.setTypeNotification(h.getTypeNotification());
        entity.setTitre(h.getTitre());
        entity.setMessage(h.getMessage());
        entity.setDestinataireRole(h.getDestinataireRole());
        entity.setMatriculeDestinataire(h.getMatriculeDestinataire());
        entity.setCanal(h.getCanal());
        entity.setPriorite(h.getPriorite());
        entity.setReferenceLiee(h.getReferenceLiee());
        entity.setLu(h.isLu());
        entity.setDateCreation(h.getDateCreation());
        entity.setDateLecture(h.getDateLecture());
        return entity;
    }

    private HistoriqueNotification toDomain(HistoriqueNotificationJpaEntity e) {
        return HistoriqueNotification.reconstituer(
            e.getId(), e.getTypeNotification(), e.getTitre(), e.getMessage(),
            e.getDestinataireRole(), e.getMatriculeDestinataire(),
            e.getCanal(), e.getPriorite(), e.getReferenceLiee(),
            e.isLu(), e.getDateCreation(), e.getDateLecture(), e.getVersion());
    }
}
