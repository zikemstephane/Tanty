package com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client sauvegarder(Client client);
    Optional<Client> trouverParCode(String codeClient);
    List<Client> listerParCommercial(String matriculeCommercial);
    List<Client> listerParType(TypeClient type);
    List<Client> rechercherParNom(String nomPartiel);
    List<Client> listerParLocalite(String localite);
    List<Client> listerTous();
}
