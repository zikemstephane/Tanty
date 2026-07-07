package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.StatutInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository.InventaireRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa.SessionInventaireJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.mapper.InventairePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventaireRepositoryImpl implements InventaireRepository {

    private final SessionInventaireJpaRepository jpaRepository;
    private final InventairePersistenceMapper mapper;

    public InventaireRepositoryImpl(SessionInventaireJpaRepository jpaRepository, InventairePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public SessionInventaire sauvegarder(SessionInventaire session) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(session)));
    }

    @Override
    public Optional<SessionInventaire> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::versDomaine);
    }

    @Override
    public List<SessionInventaire> trouverParStatut(StatutInventaire statut) {
        return jpaRepository.findByStatut(statut.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<SessionInventaire> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compterParAnnee(int annee) {
        return jpaRepository.compterParAnnee(annee);
    }
}
