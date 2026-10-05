package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command.EnregistrerVersementCommand;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command.JustifierEcartCommand;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command.ValiderVersementCommand;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response.VersementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.mapper.VersementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.service.ReconciliationService;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.usecase.enregistrer.EnregistrerVersementUseCase;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.usecase.valider.ValiderVersementUseCase;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.TypeVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comptabilite/versements")
@Tag(name = "Comptabilité — Versements", description = "Versements des commerciaux et alertes rouges")
public class VersementController {

    private final EnregistrerVersementUseCase enregistrerVersementUseCase;
    private final ValiderVersementUseCase validerVersementUseCase;
    private final VersementRepository versementRepository;
    private final VersementMapper versementMapper;
    private final ReconciliationService reconciliationService;

    public VersementController(EnregistrerVersementUseCase enregistrerVersementUseCase,
                              ValiderVersementUseCase validerVersementUseCase,
                              VersementRepository versementRepository, VersementMapper versementMapper,
                              ReconciliationService reconciliationService) {
        this.enregistrerVersementUseCase = enregistrerVersementUseCase;
        this.validerVersementUseCase = validerVersementUseCase;
        this.versementRepository = versementRepository;
        this.versementMapper = versementMapper;
        this.reconciliationService = reconciliationService;
    }

    @GetMapping("/reconciliation")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @io.swagger.v3.oas.annotations.Operation(summary = "Prévisualiser la réconciliation financière immédiate",
        description = "CA déclaré (ventes du jour) moins la valeur des retours physiques déjà validés par le stock.")
    public ResponseEntity<ApiResponse<ReconciliationService.ReconciliationResultat>> reconciliation(
        @RequestParam String matricule,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.succes(reconciliationService.calculer(matricule, date),
            "Réconciliation calculée"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<VersementResponse>> enregistrer(@RequestBody EnregistrerVersementRequest req) {
        EnregistrerVersementCommand cmd = new EnregistrerVersementCommand(req.matriculeCommercial(),
            req.montantAttendu(), req.cashVerse(), req.typeVersement(),
            req.date() != null ? req.date() : LocalDate.now(), req.justificationEcart());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(enregistrerVersementUseCase.executer(cmd), "Versement enregistré"));
    }

    @PatchMapping("/{reference}/valider")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    @io.swagger.v3.oas.annotations.Operation(summary = "Valider un versement",
        description = "Un code OTP est exigé lorsque le second facteur est actif " +
                      "(app.otp.enabled=true) ; il se génère alors via POST /api/securite/otp/generer " +
                      "avec operationCible = 'VERSEMENT:{reference}'. Le second facteur étant coupé " +
                      "pour l'instant, le paramètre est facultatif.")
    public ResponseEntity<ApiResponse<VersementResponse>> valider(@PathVariable String reference,
                                                                  @RequestParam String matriculeValidateur,
                                                                  @RequestParam(required = false) String codeOtp) {
        return ResponseEntity.ok(ApiResponse.succes(
            validerVersementUseCase.executer(new ValiderVersementCommand(reference, matriculeValidateur, codeOtp)),
            "Versement validé"));
    }

    @PatchMapping("/{reference}/justifier")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'CONTROLEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<VersementResponse>> justifier(@PathVariable String reference,
                                                                    @RequestBody JustifierEcartCommand cmd) {
        Versement versement = versementRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Versement", reference));
        versement.justifierEcart(cmd.justification());
        return ResponseEntity.ok(ApiResponse.succes(versementMapper.versResponse(versementRepository.sauvegarder(versement)), "Écart justifié"));
    }

    @GetMapping("/alertes")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<VersementResponse>>> alertesRouges() {
        var v = versementRepository.listerAlertesRouges().stream().map(versementMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(v, v.size() + " alerte(s) rouge(s)"));
    }

    // Lecture gardée par la permission, non par une liste de rôles. Celle-ci
    // omettait le DIRECTEUR_COMMERCIAL, à qui la matrice accorde pourtant
    // COMPTABILITE_VERSEMENT_GERER : il administrait les versements sans
    // pouvoir en consulter un seul, et son écran Versements recevait un 403 à
    // chaque chargement. Même figure que la secrétaire face aux décaissements.
    //
    // Tous les rôles de l'ancienne liste détiennent COMPTABILITE_VERSEMENT_CONSULTER ;
    // rien n'est retiré, et seul le directeur commercial est ajouté — sur une
    // lecture, les validations restant sur leurs rôles.
    @GetMapping
    @PreAuthorize("hasAuthority('COMPTABILITE_VERSEMENT_CONSULTER')")
    public ResponseEntity<ApiResponse<List<VersementResponse>>> lister(
        @RequestParam(required = false) String commercial,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(required = false) StatutVersement statut) {
        List<Versement> resultats;
        if (commercial != null) resultats = versementRepository.listerParCommercial(commercial);
        else if (date != null) resultats = versementRepository.listerParDate(date);
        else if (statut != null) resultats = versementRepository.listerParStatut(statut);
        else resultats = versementRepository.listerParDate(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.succes(resultats.stream().map(versementMapper::versResponse).toList(),
            resultats.size() + " versement(s)"));
    }

    /**
     * @param justificationEcart motif saisi par le commercial lorsqu'il constate un écart au
     *                            moment de déclarer sa fin de tournée ; appliqué immédiatement
     *                            si le versement déclenche une alerte
     */
    public record EnregistrerVersementRequest(String matriculeCommercial, Double montantAttendu, double cashVerse,
                                              TypeVersement typeVersement, LocalDate date,
                                              String justificationEcart) {}
}
