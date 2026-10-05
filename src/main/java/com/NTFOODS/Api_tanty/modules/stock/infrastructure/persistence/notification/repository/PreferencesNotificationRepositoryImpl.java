package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.PreferencesNotification;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository.PreferencesNotificationRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa.PreferencesNotificationJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa.PreferencesNotificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PreferencesNotificationRepositoryImpl implements PreferencesNotificationRepository {

    private final PreferencesNotificationJpaRepository jpaRepository;

    public PreferencesNotificationRepositoryImpl(PreferencesNotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PreferencesNotification sauvegarder(PreferencesNotification preferences) {
        PreferencesNotificationJpaEntity entity = jpaRepository
            .findByMatriculeUtilisateur(preferences.getMatriculeUtilisateur())
            .orElse(new PreferencesNotificationJpaEntity());

        entity.setMatriculeUtilisateur(preferences.getMatriculeUtilisateur());
        entity.setNotificationInApp(preferences.isNotificationInApp());
        entity.setNotificationEmail(preferences.isNotificationEmail());
        entity.setNotificationSms(preferences.isNotificationSms());
        entity.setNotificationPush(preferences.isNotificationPush());
        entity.setPrioriteMinimale(preferences.getPrioriteMinimale());
        entity.setHeureDebutReception(preferences.getHeureDebutReception());
        entity.setHeureFinReception(preferences.getHeureFinReception());
        entity.setTypesFiltres(preferences.getTypesFiltres() != null
            ? String.join(",", preferences.getTypesFiltres()) : null);

        PreferencesNotificationJpaEntity saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<PreferencesNotification> trouverParMatricule(String matricule) {
        return jpaRepository.findByMatriculeUtilisateur(matricule).map(this::mapToDomain);
    }

    private PreferencesNotification mapToDomain(PreferencesNotificationJpaEntity e) {
        Set<String> typesFiltres = e.getTypesFiltres() != null && !e.getTypesFiltres().isBlank()
            ? Arrays.stream(e.getTypesFiltres().split(",")).collect(Collectors.toCollection(HashSet::new))
            : new HashSet<>();
        return PreferencesNotification.reconstituer(
            e.getId(), e.getMatriculeUtilisateur(),
            e.isNotificationInApp(), e.isNotificationEmail(), e.isNotificationSms(), e.isNotificationPush(),
            e.getPrioriteMinimale(), e.getHeureDebutReception(), e.getHeureFinReception(),
            typesFiltres, e.getVersion());
    }
}
