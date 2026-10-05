package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.client.dto.response.ClientResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.client.mapper.ClientMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
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
    private final RecouvrementRepository recouvrementRepository;

    public ClientController(ClientRepository clientRepository, ClientMapper clientMapper,
                            RecouvrementRepository recouvrementRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.recouvrementRepository = recouvrementRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_CLIENT_GERER')")
    public ResponseEntity<ApiResponse<ClientResponse>> creer(@RequestBody CreerClientRequest req) {
        String code = "CLI-" + System.currentTimeMillis();
        Client client = Client.creer(code, req.nom(), req.telephone(), req.adresse(),
            req.localite(), req.type(), req.matriculeCommercialReferent(), req.latitude(), req.longitude());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(clientMapper.versResponse(clientRepository.sauvegarder(client)), "Client créé"));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('COMMERCIAL_CLIENT_GERER')")
    public ResponseEntity<ApiResponse<ClientResponse>> mettreAJour(@PathVariable String code,
                                                                   @RequestBody MettreAJourClientRequest req) {
        Client client = clientRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", code));
        client.mettreAJour(req.nom(), req.telephone(), req.adresse(), req.localite(), req.type(),
            req.latitude(), req.longitude());
        return ResponseEntity.ok(ApiResponse.succes(clientMapper.versResponse(clientRepository.sauvegarder(client)), "Client mis à jour"));
    }

    @PatchMapping("/{code}/desactiver")
    @PreAuthorize("hasAuthority('COMMERCIAL_CLIENT_GERER')")
    public ResponseEntity<ApiResponse<ClientResponse>> desactiver(@PathVariable String code) {
        Client client = clientRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", code));
        client.desactiver();
        return ResponseEntity.ok(ApiResponse.succes(clientMapper.versResponse(clientRepository.sauvegarder(client)), "Client désactivé"));
    }

    @GetMapping("/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClientResponse>> consulter(@PathVariable String code) {
        Client client = clientRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", code));
        return ResponseEntity.ok(ApiResponse.succes(clientMapper.versResponse(client, calculerDette(code))));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> lister(@RequestParam(required = false) String nom,
                                                                    @RequestParam(required = false) String commercial) {
        List<Client> clients;
        if (nom != null) clients = clientRepository.rechercherParNom(nom);
        else if (commercial != null) clients = clientRepository.listerParCommercial(commercial);
        else clients = clientRepository.listerTous();
        List<ClientResponse> result = clients.stream()
            .map(c -> clientMapper.versResponse(c, calculerDette(c.getCodeClient())))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(result, result.size() + " client(s)"));
    }

    private double calculerDette(String codeClient) {
        return recouvrementRepository.listerParClient(codeClient).stream()
            .mapToDouble(Recouvrement::montantRestant).sum();
    }

    public record CreerClientRequest(String nom, String telephone, String adresse, String localite,
                                     TypeClient type, String matriculeCommercialReferent,
                                     Double latitude, Double longitude) {}
    public record MettreAJourClientRequest(String nom, String telephone, String adresse, String localite,
                                           TypeClient type, Double latitude, Double longitude) {}
}
