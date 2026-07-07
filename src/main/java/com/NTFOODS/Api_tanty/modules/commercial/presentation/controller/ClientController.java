package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.client.dto.response.ClientResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.client.mapper.ClientMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commercial/clients")
@Tag(name = "Commercial — Clients", description = "Gestion des clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientController(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'ADMIN')")
    public ResponseEntity<ApiResponse<ClientResponse>> creer(@RequestBody CreerClientRequest req) {
        String code = "CLI-" + System.currentTimeMillis();
        Client client = Client.creer(code, req.nom(), req.telephone(), req.adresse(),
            req.localite(), req.type(), req.matriculeCommercialReferent());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(clientMapper.versResponse(clientRepository.sauvegarder(client)), "Client créé"));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'ADMIN')")
    public ResponseEntity<ApiResponse<ClientResponse>> mettreAJour(@PathVariable String code,
                                                                   @RequestBody MettreAJourClientRequest req) {
        Client client = clientRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", code));
        client.mettreAJour(req.nom(), req.telephone(), req.adresse(), req.localite(), req.type());
        return ResponseEntity.ok(ApiResponse.succes(clientMapper.versResponse(clientRepository.sauvegarder(client)), "Client mis à jour"));
    }

    @PatchMapping("/{code}/desactiver")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'ADMIN')")
    public ResponseEntity<ApiResponse<ClientResponse>> desactiver(@PathVariable String code) {
        Client client = clientRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", code));
        client.desactiver();
        return ResponseEntity.ok(ApiResponse.succes(clientMapper.versResponse(clientRepository.sauvegarder(client)), "Client désactivé"));
    }

    @GetMapping("/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClientResponse>> consulter(@PathVariable String code) {
        return clientRepository.trouverParCode(code)
            .map(c -> ResponseEntity.ok(ApiResponse.succes(clientMapper.versResponse(c))))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", code));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> lister(@RequestParam(required = false) String nom,
                                                                    @RequestParam(required = false) String commercial) {
        List<Client> clients;
        if (nom != null) clients = clientRepository.rechercherParNom(nom);
        else if (commercial != null) clients = clientRepository.listerParCommercial(commercial);
        else clients = clientRepository.listerTous();
        return ResponseEntity.ok(ApiResponse.succes(clients.stream().map(clientMapper::versResponse).toList(),
            clients.size() + " client(s)"));
    }

    public record CreerClientRequest(String nom, String telephone, String adresse, String localite,
                                     TypeClient type, String matriculeCommercialReferent) {}
    public record MettreAJourClientRequest(String nom, String telephone, String adresse, String localite, TypeClient type) {}
}
