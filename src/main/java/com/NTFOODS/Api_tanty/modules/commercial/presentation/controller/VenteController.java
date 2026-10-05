package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service.RemiseGuardService;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service.TarificationGuardService;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.dto.response.VenteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.mapper.VenteMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.DeclencherTransactionNonMarchandeUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.EnregistrerVenteUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.FactureAvoir;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.FactureAvoirRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
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
import java.util.List;

@RestController
@RequestMapping("/api/commercial/ventes")
@Tag(name = "Commercial — Ventes", description = "Transactions de vente terrain des commerciaux — tarification verrouillée")
@SecurityRequirement(name = "bearerAuth")
public class VenteController {

    private final VenteRepository venteRepository;
    private final VenteMapper venteMapper;
    private final EventPublisher eventPublisher;
    private final TarificationGuardService tarificationGuardService;
    private final FactureAvoirRepository factureAvoirRepository;
    private final DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase;
    private final GenerateurNumeroMetier generateurNumero;
    private final EnregistrerVenteUseCase enregistrerVenteUseCase;

    public VenteController(VenteRepository venteRepository, VenteMapper venteMapper, EventPublisher eventPublisher,
                            TarificationGuardService tarificationGuardService,
                            FactureAvoirRepository factureAvoirRepository,
                            DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase,
                            GenerateurNumeroMetier generateurNumero,
                            EnregistrerVenteUseCase enregistrerVenteUseCase) {
        this.venteRepository = venteRepository;
        this.venteMapper = venteMapper;
        this.eventPublisher = eventPublisher;
        this.tarificationGuardService = tarificationGuardService;
        this.factureAvoirRepository = factureAvoirRepository;
        this.nonMarchandeUseCase = nonMarchandeUseCase;
        this.generateurNumero = generateurNumero;
        this.enregistrerVenteUseCase = enregistrerVenteUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CREER')")
    @Operation(summary = "Créer une vente",
        description = "Chaque ligne est vérifiée contre la grille tarifaire homologuée du segment du client " +
                      "(Pilier 3 — Tarification Verrouillée). Un prix différent du prix homologué n'est accepté " +
                      "que si referencePrixExceptionnel pointe vers un déblocage DG/Chargée RP valide. " +
                      "La remise, exprimée en FCFA sur la ligne entière, est déterminée par le serveur à partir " +
                      "des promotions en vigueur pour le couple produit × segment client : toute valeur " +
                      "divergente transmise par le client entraîne le refus de la vente.")
    public ResponseEntity<ApiResponse<VenteResponse>> creerVente(
        @jakarta.validation.Valid @RequestBody CreerVenteRequest req) {
        // La logique est portée par le use case : la synchronisation hors ligne
        // rejoue exactement le même chemin, règles de tarification comprises.
        Vente vente = enregistrerVenteUseCase.executer(versCommande(req));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(venteMapper.versResponse(vente), "Vente créée : " + vente.getNumeroVente()));
    }

    private EnregistrerVenteUseCase.CommandeVente versCommande(CreerVenteRequest req) {
        return new EnregistrerVenteUseCase.CommandeVente(
            req.matriculeCommercial(), req.codeClient(), req.date(), req.modeReglement(),
            req.typeVente(), req.coordonneesGPS(), req.idFeuillRoute(), req.uuidVente(),
            req.signatureClient(), req.photoProduits(),
            req.lignes().stream()
                .map(l -> new EnregistrerVenteUseCase.LigneCommande(l.codeProduit(), l.designation(),
                    l.conditionnement(), l.quantite(), l.prixUnitaire(), l.remise(),
                    l.referencePrixExceptionnel()))
                .toList());
    }

    @PatchMapping("/{numeroVente}/finaliser")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_FINALISER')")
    @Operation(summary = "Finaliser une vente terrain",
        description = "Clôture opérationnelle réalisée par le commercial lui-même à l'issue de la " +
                      "transaction : émet VenteFinaliseEvent, qui décrémente le stock mobile et " +
                      "alimente la réconciliation. Distincte du contrôle hiérarchique a posteriori.")
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
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CREER')")
    @Operation(summary = "Annuler une vente (uniquement si EN_COURS)")
    public ResponseEntity<ApiResponse<VenteResponse>> annulerVente(@PathVariable String numeroVente,
                                                                     @RequestParam String motif) {
        Vente vente = venteRepository.trouverParNumero(numeroVente)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Vente", numeroVente));
        vente.annuler(motif);
        return ResponseEntity.ok(ApiResponse.succes(venteMapper.versResponse(venteRepository.sauvegarder(vente)), "Vente annulée"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
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
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
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
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    public ResponseEntity<ApiResponse<List<VenteResponse>>> ventesParCommercialEtDate(
        @PathVariable String matricule,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var ventes = venteRepository.listerParCommercialEtDate(matricule, date).stream().map(venteMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(ventes, ventes.size() + " vente(s)"));
    }

    @GetMapping("/client/{codeClient}")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Lister les ventes d'un client")
    public ResponseEntity<ApiResponse<List<VenteResponse>>> ventesParClient(@PathVariable String codeClient) {
        List<VenteResponse> ventes = venteRepository.listerParClient(codeClient).stream()
            .map(venteMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(ventes, ventes.size() + " vente(s)"));
    }

    @GetMapping("/non-marchandes")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Lister les transactions non marchandes d'une période",
        description = "Avaries, dons, dotations et échanges déclarés depuis le terrain. Le siège doit " +
                      "pouvoir les instruire : sans cette vue, une déclaration disparaissait dans le " +
                      "flux des ventes sans jamais être relue.")
    public ResponseEntity<ApiResponse<List<VenteResponse>>> nonMarchandes(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false) TypeVente type) {
        List<VenteResponse> transactions = venteRepository.listerParPeriode(debut, fin).stream()
            .filter(v -> v.getTypeVente() != TypeVente.COMPTANT && v.getTypeVente() != TypeVente.CREDIT)
            .filter(v -> type == null || v.getTypeVente() == type)
            .map(venteMapper::versResponse)
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(transactions,
            transactions.size() + " transaction(s) non marchande(s)"));
    }

    @PostMapping("/non-marchand")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CREER')")
    @Operation(summary = "Déclarer une transaction non marchande (AVARIE/DON/DOTATION/ECHANGE)",
        description = "§12 — Ces transactions n'encaissent rien mais font varier le stock mobile du " +
                      "commercial : sortie sèche pour une avarie, un don ou une dotation ; sortie du " +
                      "produit remis et entrée du produit repris pour un échange. Motif obligatoire. " +
                      "La photo de preuve est exigée à partir du seuil porté par les règles d'avarie " +
                      "en vigueur à la date de la déclaration.")
    public ResponseEntity<ApiResponse<VenteResponse>> declarerNonMarchand(@RequestBody NonMarchandeRequest req) {
        Vente vente = nonMarchandeUseCase.executer(
            new DeclencherTransactionNonMarchandeUseCase.NonMarchandeRequest(
                req.typeVente(), req.matriculeCommercial(), req.codeClient(),
                req.codeProduit(), req.designationProduit(), req.conditionnement(),
                req.quantite(), req.date(), req.motif(),
                req.photoJustificative(), req.codeProduitEchange()));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(venteMapper.versResponse(vente), "Transaction non marchande enregistrée"));
    }

    @PostMapping("/{numeroVente}/avoir")
    @PreAuthorize("hasAuthority('COMMERCIAL_FACTURE_AVOIR_GERER')")
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
    @PreAuthorize("hasAuthority('COMMERCIAL_FACTURE_AVOIR_GERER')")
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

    /**
     * Les champs obligatoires sont déclarés ici plutôt que laissés au domaine.
     * Sans cela, une désignation ou un conditionnement absent traversait la
     * couche application et ressortait en NullPointerException — soit un 500
     * « erreur interne » là où l'appelant avait simplement omis un champ, sans
     * qu'on lui dise lequel.
     */
    public record CreerVenteRequest(
        String matriculeCommercial,
        @jakarta.validation.constraints.NotBlank(message = "Le code client est obligatoire") String codeClient,
        LocalDate date,
        @jakarta.validation.constraints.NotNull(message = "Le mode de règlement est obligatoire") ModeReglement modeReglement,
        TypeVente typeVente,
        String coordonneesGPS, String idFeuillRoute,
        String uuidVente, boolean signatureClient,
        @jakarta.validation.constraints.NotBlank(message = "La photo des produits est obligatoire") String photoProduits,
        @jakarta.validation.constraints.NotEmpty(message = "Une vente doit comporter au moins une ligne")
        @jakarta.validation.Valid List<LigneVenteDto> lignes) {}

    public record LigneVenteDto(
        @jakarta.validation.constraints.NotBlank(message = "Le code produit est obligatoire") String codeProduit,
        @jakarta.validation.constraints.NotBlank(message = "La désignation du produit est obligatoire") String designation,
        @jakarta.validation.constraints.NotBlank(message = "Le conditionnement (SKU) est obligatoire") String conditionnement,
        @jakarta.validation.constraints.Positive(message = "La quantité doit être strictement positive") int quantite,
        @jakarta.validation.constraints.PositiveOrZero(message = "Le prix unitaire ne peut pas être négatif") double prixUnitaire,
        double remise,
        String referencePrixExceptionnel) {}
    public record CreerAvoirRequest(String matriculeComptable, String motif) {}
    public record NonMarchandeRequest(TypeVente typeVente, String matriculeCommercial, String codeClient,
        String codeProduit, String designationProduit, String conditionnement,
        int quantite, LocalDate date, String motif,
        String photoJustificative, String codeProduitEchange) {}
    public record AvoirResponse(Long id, String numeroAvoir, String numeroVenteOriginale,
        String matriculeCommercial, String codeClient, String matriculeComptable,
        double montantHT, double montantTVA, double montantTTC, String motif,
        LocalDate dateAvoir, String statut, java.time.LocalDateTime dateCreation,
        java.time.LocalDateTime dateValidation) {}
}
