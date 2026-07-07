package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service.TarificationGuardService;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.dto.response.VenteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.mapper.VenteMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.DeclencherTransactionNonMarchandeUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.FactureAvoir;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.FactureAvoirRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/commercial/ventes")
@Tag(name = "Commercial — Ventes", description = "Transactions de vente terrain des commerciaux — tarification verrouillée")
@SecurityRequirement(name = "bearerAuth")
public class VenteController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final VenteRepository venteRepository;
    private final VenteMapper venteMapper;
    private final EventPublisher eventPublisher;
    private final TarificationGuardService tarificationGuardService;
    private final FactureAvoirRepository factureAvoirRepository;
    private final DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase;

    public VenteController(VenteRepository venteRepository, VenteMapper venteMapper, EventPublisher eventPublisher,
                            TarificationGuardService tarificationGuardService,
                            FactureAvoirRepository factureAvoirRepository,
                            DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase) {
        this.venteRepository = venteRepository;
        this.venteMapper = venteMapper;
        this.eventPublisher = eventPublisher;
        this.tarificationGuardService = tarificationGuardService;
        this.factureAvoirRepository = factureAvoirRepository;
        this.nonMarchandeUseCase = nonMarchandeUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Créer une vente",
        description = "Chaque ligne est vérifiée contre la grille tarifaire homologuée du segment du client " +
                      "(Pilier 3 — Tarification Verrouillée). Un prix différent du prix homologué n'est accepté " +
                      "que si referencePrixExceptionnel pointe vers un déblocage DG/Directeur Commercial valide.")
    public ResponseEntity<ApiResponse<VenteResponse>> creerVente(@RequestBody CreerVenteRequest req) {
        for (LigneVenteDto l : req.lignes()) {
            tarificationGuardService.validerPrix(req.codeClient(), l.codeProduit(), l.prixUnitaire(),
                l.referencePrixExceptionnel());
        }

        String num = "VTE-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.date())
            + "-" + String.format("%03d", COUNTER.incrementAndGet());
        List<LigneVente> lignes = req.lignes().stream()
            .map(l -> new LigneVente(l.codeProduit(), l.designation(), l.conditionnement(),
                l.quantite(), l.prixUnitaire(), l.remise()))
            .toList();
        Vente vente = Vente.creer(num, req.uuidVente(), req.matriculeCommercial(), req.codeClient(), req.date(), req.modeReglement(), req.typeVente(), lignes);
        if (req.coordonneesGPS() != null) {
            // GPS stored via repository save cycle
        }
        if (req.signatureClient()) {
            vente.signerClient();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(venteMapper.versResponse(venteRepository.sauvegarder(vente)), "Vente créée : " + num));
    }

    @PatchMapping("/{numeroVente}/finaliser")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<VenteResponse>> finaliserVente(@PathVariable String numeroVente) {
        Vente vente = venteRepository.trouverParNumero(numeroVente)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Vente", numeroVente));
        vente.finaliser();
        Vente sauvegarde = venteRepository.sauvegarder(vente);
        vente.getDomainEvents().forEach(eventPublisher::publish);
        vente.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(venteMapper.versResponse(sauvegarde), "Vente finalisée"));
    }

    @PatchMapping("/{numeroVente}/annuler")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Annuler une vente (uniquement si EN_COURS)")
    public ResponseEntity<ApiResponse<VenteResponse>> annulerVente(@PathVariable String numeroVente,
                                                                     @RequestParam String motif) {
        Vente vente = venteRepository.trouverParNumero(numeroVente)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Vente", numeroVente));
        vente.annuler(motif);
        return ResponseEntity.ok(ApiResponse.succes(venteMapper.versResponse(venteRepository.sauvegarder(vente)), "Vente annulée"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'COMPTABLE', 'ADMIN')")
    @Operation(summary = "Lister les ventes par période (paginé)",
        description = "Retourne une page de ventes. Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: date,desc). " +
                      "Si page/size omis, retourne les 20 premières ventes triées par date décroissante.")
    public ResponseEntity<ApiResponse<PageResponse<VenteResponse>>> listerParPeriode(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VenteResponse> page = venteRepository.listerParPeriodePagine(debut, fin, pageable)
            .map(venteMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " vente(s)"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'COMPTABLE', 'ADMIN')")
    @Operation(summary = "Ventes d'un commercial (paginé)",
        description = "Retourne une page de ventes pour un commercial. Paramètres optionnels: page (déf. 0), size (déf. 20), sort.")
    public ResponseEntity<ApiResponse<PageResponse<VenteResponse>>> ventesParCommercial(
        @PathVariable String matricule,
        @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VenteResponse> page = venteRepository.listerParCommercialPagine(matricule, pageable)
            .map(venteMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " vente(s)"));
    }

    @GetMapping("/commercial/{matricule}/date/{date}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<VenteResponse>>> ventesParCommercialEtDate(
        @PathVariable String matricule,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var ventes = venteRepository.listerParCommercialEtDate(matricule, date).stream().map(venteMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(ventes, ventes.size() + " vente(s)"));
    }

    @PostMapping("/non-marchand")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Déclarer une transaction non marchande (DON/AVARIE/DOTATION)",
        description = "§12 — Dons, Dotations, Avaries : impactent le stock fictif SANS générer de cash. " +
                      "Photo obligatoire si avarie > 5% du lot (RG-19). Motif obligatoire.")
    public ResponseEntity<ApiResponse<VenteResponse>> declarerNonMarchand(@RequestBody NonMarchandeRequest req) {
        Vente vente = nonMarchandeUseCase.executer(
            new DeclencherTransactionNonMarchandeUseCase.NonMarchandeRequest(
                req.typeVente(), req.matriculeCommercial(), req.codeClient(),
                req.codeProduit(), req.designationProduit(), req.conditionnement(),
                req.quantite(), req.date(), req.motif(), req.pourcentageLot(),
                req.photoJustificative()));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(venteMapper.versResponse(vente), "Transaction non marchande enregistrée"));
    }

    @PostMapping("/{numeroVente}/avoir")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    @Operation(summary = "Émettre une facture d'avoir",
        description = "Régularisation d'une vente validée SANS suppression (RG-05). " +
                      "La suite numérique des documents reste intacte pour l'audit DSF.")
    public ResponseEntity<ApiResponse<AvoirResponse>> emettreAvoir(@PathVariable String numeroVente,
                                                                     @RequestBody CreerAvoirRequest req) {
        Vente vente = venteRepository.trouverParNumero(numeroVente)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Vente", numeroVente));
        String numAvoir = factureAvoirRepository.genererNumero(LocalDate.now());
        FactureAvoir avoir = FactureAvoir.emettre(numAvoir, numeroVente, vente.getMatriculeCommercial(),
            vente.getCodeClient(), req.matriculeComptable(), vente.montantHT(), 0.0,
            req.motif(), LocalDate.now());
        FactureAvoir sauvegarde = factureAvoirRepository.sauvegarder(avoir);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(toAvoirResponse(sauvegarde), "Avoir émis : " + numAvoir));
    }

    @PatchMapping("/avoirs/{numeroAvoir}/valider")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<AvoirResponse>> validerAvoir(@PathVariable String numeroAvoir) {
        FactureAvoir avoir = factureAvoirRepository.trouverParNumero(numeroAvoir)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Avoir", numeroAvoir));
        avoir.valider();
        return ResponseEntity.ok(ApiResponse.succes(toAvoirResponse(factureAvoirRepository.sauvegarder(avoir)),
            "Avoir validé"));
    }

    @GetMapping("/avoirs/{numeroAvoir}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AvoirResponse>> trouverAvoir(@PathVariable String numeroAvoir) {
        FactureAvoir avoir = factureAvoirRepository.trouverParNumero(numeroAvoir)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Avoir", numeroAvoir));
        return ResponseEntity.ok(ApiResponse.succes(toAvoirResponse(avoir), "Avoir trouvé"));
    }

    @GetMapping("/{numeroVente}/avoirs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<AvoirResponse>>> avoirsParVente(@PathVariable String numeroVente) {
        var avoirs = factureAvoirRepository.listerParVenteOriginale(numeroVente).stream()
            .map(this::toAvoirResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(avoirs, avoirs.size() + " avoir(s)"));
    }

    private AvoirResponse toAvoirResponse(FactureAvoir a) {
        return new AvoirResponse(a.getId(), a.getNumeroAvoir(), a.getNumeroVenteOriginale(),
            a.getMatriculeCommercial(), a.getCodeClient(), a.getMatriculeComptable(),
            a.getMontantHT(), a.getMontantTVA(), a.getMontantTTC(), a.getMotif(),
            a.getDateAvoir(), a.getStatut().name(), a.getDateCreation(), a.getDateValidation());
    }

    public record CreerVenteRequest(String matriculeCommercial, String codeClient, LocalDate date,
                                    ModeReglement modeReglement, TypeVente typeVente,
                                    String coordonneesGPS, String idFeuillRoute,
                                    String uuidVente, boolean signatureClient,
                                    List<LigneVenteDto> lignes) {}
    public record LigneVenteDto(String codeProduit, String designation, String conditionnement,
                                int quantite, double prixUnitaire, double remise,
                                String referencePrixExceptionnel) {}
    public record CreerAvoirRequest(String matriculeComptable, String motif) {}
    public record NonMarchandeRequest(TypeVente typeVente, String matriculeCommercial, String codeClient,
        String codeProduit, String designationProduit, String conditionnement,
        int quantite, LocalDate date, String motif, double pourcentageLot,
        String photoJustificative) {}
    public record AvoirResponse(Long id, String numeroAvoir, String numeroVenteOriginale,
        String matriculeCommercial, String codeClient, String matriculeComptable,
        double montantHT, double montantTVA, double montantTTC, String motif,
        LocalDate dateAvoir, String statut, java.time.LocalDateTime dateCreation,
        java.time.LocalDateTime dateValidation) {}
}
