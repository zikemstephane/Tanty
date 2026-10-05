package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.recouvrement.dto.response.RecouvrementResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.recouvrement.mapper.RecouvrementMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commercial/recouvrements")
@Tag(name = "Commercial — Recouvrements", description = "Suivi des créances clients")
public class RecouvrementController {

    private final RecouvrementRepository recouvrementRepository;
    private final RecouvrementMapper recouvrementMapper;

    public RecouvrementController(RecouvrementRepository recouvrementRepository, RecouvrementMapper recouvrementMapper) {
        this.recouvrementRepository = recouvrementRepository;
        this.recouvrementMapper = recouvrementMapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_RECOUVREMENT_GERER')")
    public ResponseEntity<ApiResponse<RecouvrementResponse>> creer(@RequestBody CreerRecouvrementRequest req) {
        String reference = "REC-" + req.codeClient() + "-" + System.currentTimeMillis();
        Recouvrement recouvrement = Recouvrement.creer(reference, req.codeClient(), req.nomClient(),
            req.montantDu(), req.dateEcheance(), req.matriculeCommercial());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(recouvrementMapper.versResponse(recouvrementRepository.sauvegarder(recouvrement)), "Recouvrement créé"));
    }

    @PatchMapping("/{reference}/rembourser")
    @PreAuthorize("hasAuthority('COMMERCIAL_RECOUVREMENT_GERER')")
    public ResponseEntity<ApiResponse<RecouvrementResponse>> rembourser(@PathVariable String reference, @RequestParam double montant) {
        Recouvrement r = recouvrementRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Recouvrement", reference));
        r.enregistrerRemboursement(montant);
        return ResponseEntity.ok(ApiResponse.succes(recouvrementMapper.versResponse(recouvrementRepository.sauvegarder(r)), "Remboursement enregistré"));
    }

    @PatchMapping("/{reference}/contentieux")
    @PreAuthorize("hasAuthority('COMMERCIAL_RECOUVREMENT_GERER')")
    public ResponseEntity<ApiResponse<RecouvrementResponse>> passerEnContentieux(@PathVariable String reference, @RequestParam String motif) {
        Recouvrement r = recouvrementRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Recouvrement", reference));
        r.passerEnContentieux(motif);
        return ResponseEntity.ok(ApiResponse.succes(recouvrementMapper.versResponse(recouvrementRepository.sauvegarder(r)), "Passé en contentieux"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<RecouvrementResponse>>> lister(@RequestParam(required = false) String commercial,
                                                                          @RequestParam(required = false) String client,
                                                                          @RequestParam(required = false) StatutRecouvrement statut) {
        List<Recouvrement> resultats;
        if (commercial != null) resultats = recouvrementRepository.listerParCommercial(commercial);
        else if (client != null) resultats = recouvrementRepository.listerParClient(client);
        else if (statut != null) resultats = recouvrementRepository.listerParStatut(statut);
        else resultats = recouvrementRepository.listerEnContentieux();
        return ResponseEntity.ok(ApiResponse.succes(resultats.stream().map(recouvrementMapper::versResponse).toList(),
            resultats.size() + " recouvrement(s)"));
    }

    public record CreerRecouvrementRequest(String codeClient, String nomClient, double montantDu,
                                           LocalDate dateEcheance, String matriculeCommercial) {}
}
