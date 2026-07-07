package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.jpa.ClientJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.jpa.ClientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientJpaRepository jpaRepository;

    public ClientRepositoryImpl(ClientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Client sauvegarder(Client client) {
        return toDomain(jpaRepository.save(toEntity(client)));
    }

    @Override
    public Optional<Client> trouverParCode(String codeClient) {
        return jpaRepository.findByCodeClient(codeClient).map(this::toDomain);
    }

    @Override
    public List<Client> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercialReferent(matriculeCommercial)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Client> listerParType(TypeClient type) {
        return jpaRepository.findByType(type).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Client> rechercherParNom(String nomPartiel) {
        return jpaRepository.findByNomContainingIgnoreCase(nomPartiel).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Client> listerParLocalite(String localite) {
        return jpaRepository.findByLocaliteIgnoreCaseAndActifTrue(localite).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Client> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private ClientJpaEntity toEntity(Client c) {
        return ClientJpaEntity.builder()
            .id(c.getId()).codeClient(c.getCodeClient()).nom(c.getNom())
            .telephone(c.getTelephone()).adresse(c.getAdresse()).localite(c.getLocalite())
            .type(c.getType()).matriculeCommercialReferent(c.getMatriculeCommercialReferent())
            .actif(c.isActif()).dateCreation(c.getDateCreation()).dateModification(c.getDateModification())
            .version(c.getVersion()).build();
    }

    private Client toDomain(ClientJpaEntity e) {
        return Client.reconstituer(e.getId(), e.getCodeClient(), e.getNom(), e.getTelephone(),
            e.getAdresse(), e.getLocalite(), e.getType(), e.getMatriculeCommercialReferent(),
            e.isActif(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
