package com.NTFOODS.Api_tanty.modules.sync.application.service;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.CodeObservationService;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase.EnregistrerPassageArretUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase.PlanifierFeuillRouteUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;

import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.service.EligibilitePrecommandeService;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.DeclencherTransactionNonMarchandeUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.EnregistrerVenteUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Aiguillage d'un push de synchronisation vers le use case qui sait le traiter.
 *
 * <p>Avant, {@code /sync/push} recevait une lambda vide : le serveur journalisait
 * la clé d'idempotence, répondait SUCCESS, et <strong>n'écrivait rien</strong>. Une
 * vente saisie hors ligne était perdue en silence tout en étant réputée
 * synchronisée. Ce dispatcher est ce qui manquait.</p>
 *
 * <p>Aucune règle métier n'est réimplémentée ici : chaque type d'entité est confié
 * au use case qui sert déjà l'appel en ligne. Une vente rejouée traverse donc la
 * même vérification de grille tarifaire et la même résolution de remise qu'une
 * vente saisie connectée. C'est la seule façon d'éviter qu'un commercial obtienne
 * hors ligne ce que le serveur lui refuserait en ligne.</p>
 */
@Service
public class SyncPushDispatcher {

    private final EnregistrerVenteUseCase enregistrerVenteUseCase;
    private final DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase;
    private final PlanifierFeuillRouteUseCase planifierFeuillRouteUseCase;
    private final EnregistrerPassageArretUseCase passageArretUseCase;
    private final PreCommandeRepository preCommandeRepository;
    private final EligibilitePrecommandeService eligibiliteService;
    private final GenerateurNumeroMetier generateurNumero;
    private final ObjectMapper objectMapper;
    private final CodeObservationService codeObservationService;

    public SyncPushDispatcher(EnregistrerVenteUseCase enregistrerVenteUseCase,
                               DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase,
                               PlanifierFeuillRouteUseCase planifierFeuillRouteUseCase,
                               EnregistrerPassageArretUseCase passageArretUseCase,
                               PreCommandeRepository preCommandeRepository,
                               EligibilitePrecommandeService eligibiliteService,
                               GenerateurNumeroMetier generateurNumero,
                               ObjectMapper objectMapper,
                               CodeObservationService codeObservationService) {
        this.enregistrerVenteUseCase = enregistrerVenteUseCase;
        this.nonMarchandeUseCase = nonMarchandeUseCase;
        this.planifierFeuillRouteUseCase = planifierFeuillRouteUseCase;
        this.passageArretUseCase = passageArretUseCase;
        this.preCommandeRepository = preCommandeRepository;
        this.eligibiliteService = eligibiliteService;
        this.generateurNumero = generateurNumero;
        this.objectMapper = objectMapper;
        this.codeObservationService = codeObservationService;
    }

    /**
     * Rejoue l'opération et retourne la référence attribuée par le serveur.
     *
     * @throws SyncPayloadInvalideException si le contenu ne permet pas de
     *         reconstituer l'opération — inutile de faire réessayer le client,
     *         le même contenu échouera toujours.
     */
    public String rejouer(SyncEntityType entityType, String payloadJson, String matriculeCommercial) {
        if (payloadJson == null || payloadJson.isBlank()) {
            throw new SyncPayloadInvalideException(
                "Aucun contenu transmis pour un push de type " + entityType
                    + " : le serveur ne peut rien reconstituer");
        }
        JsonNode noeud = lire(payloadJson);
        return switch (entityType) {
            case VENTE -> rejouerVente(noeud, matriculeCommercial);
            case TRANSACTION_NON_MARCHANDE -> rejouerNonMarchande(noeud, matriculeCommercial);
            case FEUILL_ROUTE -> rejouerFeuillRoute(noeud, matriculeCommercial);
            case PRECOMMANDE -> rejouerPrecommande(noeud, matriculeCommercial);
            case ARRET -> rejouerArret(noeud, matriculeCommercial);
            // La clôture de journée n'est pas une opération, c'en est deux : le
            // versement des espèces à la caisse et la déclaration de fin de tournée.
            // Rejouer la seconde sans la première laisserait une journée close sans
            // remise d'argent, et fabriquer un versement à partir d'un contenu
            // différé engage une somme que personne n'a comptée à ce moment-là. Le
            // chaînage des deux reste à arbitrer ; l'annoncer vaut mieux que de
            // valider dans le vide.
            case CLOTURE_JOURNEE -> throw new SyncPayloadInvalideException(
                "Le rejeu hors ligne d'une clôture de journée suppose d'enchaîner le versement et la "
                    + "déclaration de fin de tournée ; cet enchaînement n'est pas encore arrêté. "
                    + "L'opération reste dans la file du terminal, elle n'est ni perdue ni validée.");
        };
    }

    private JsonNode lire(String payloadJson) {
        try {
            return objectMapper.readTree(payloadJson);
        } catch (Exception e) {
            throw new SyncPayloadInvalideException("Contenu illisible : " + e.getMessage());
        }
    }

    private String rejouerVente(JsonNode n, String matriculeCommercial) {
        List<EnregistrerVenteUseCase.LigneCommande> lignes = new ArrayList<>();
        JsonNode noeudLignes = n.path("lignes");
        if (!noeudLignes.isArray() || noeudLignes.isEmpty()) {
            throw new SyncPayloadInvalideException("Une vente sans ligne ne peut pas être enregistrée");
        }
        for (JsonNode l : noeudLignes) {
            lignes.add(new EnregistrerVenteUseCase.LigneCommande(
                texteObligatoire(l, "codeProduit"),
                l.path("designation").asText(""),
                l.path("conditionnement").asText(null),
                l.path("quantite").asInt(),
                l.path("prixUnitaire").asDouble(),
                l.path("remise").asDouble(0),
                l.path("referencePrixExceptionnel").asText(null)));
        }

        var commande = new EnregistrerVenteUseCase.CommandeVente(
            matriculeCommercial,
            texteObligatoire(n, "codeClient"),
            date(n, "date"),
            enumOu(ModeReglement.class, n.path("modeReglement").asText(null), ModeReglement.COMPTANT),
            enumOu(TypeVente.class, n.path("typeVente").asText(null), TypeVente.COMPTANT),
            n.path("coordonneesGPS").asText(null),
            n.path("idFeuillRoute").asText(null),
            // L'UUID vient du terminal : c'est lui qui rend le rejeu reconnaissable
            // côté vente, indépendamment de la clé d'idempotence de la synchro.
            n.path("uuidVente").asText(null),
            n.path("signatureClient").asBoolean(false),
            n.path("photoProduits").asText(null),
            lignes);

        return enregistrerVenteUseCase.executer(commande).getNumeroVente();
    }

    private String rejouerNonMarchande(JsonNode n, String matriculeCommercial) {
        var requete = new DeclencherTransactionNonMarchandeUseCase.NonMarchandeRequest(
            enumObligatoire(TypeVente.class, n, "typeVente"),
            matriculeCommercial,
            n.path("codeClient").asText(null),
            texteObligatoire(n, "codeProduit"),
            n.path("designationProduit").asText(""),
            texteObligatoire(n, "conditionnement"),
            n.path("quantite").asInt(),
            date(n, "date"),
            n.path("motif").asText(null),
            n.path("photoJustificative").asText(null),
            n.path("codeProduitEchange").asText(null));

        return nonMarchandeUseCase.executer(requete).getNumeroVente();
    }

    private String rejouerFeuillRoute(JsonNode n, String matriculeCommercial) {
        JsonNode noeudArrets = n.path("arrets");
        if (!noeudArrets.isArray() || noeudArrets.isEmpty()) {
            throw new SyncPayloadInvalideException("Une feuille de route sans arret ne peut pas etre planifiee");
        }
        List<PlanifierFeuillRouteUseCase.Arret> arrets = new ArrayList<>();
        int rang = 0;
        for (JsonNode a : noeudArrets) {
            rang++;
            arrets.add(new PlanifierFeuillRouteUseCase.Arret(
                texteObligatoire(a, "codeClient"),
                a.path("ordre").asInt(rang)));
        }
        return planifierFeuillRouteUseCase
            .executer(matriculeCommercial, date(n, "date"), arrets)
            .getNumeroFeuillRoute();
    }

    private String rejouerPrecommande(JsonNode n, String matriculeCommercial) {
        // RG-01 est verifiee au rejeu comme elle l'est en ligne. Une precommande
        // saisie hors ligne alors que la journee precedente n'etait pas apuree doit
        // etre refusee, pas absorbee : c'est un conflit que le commercial arbitrera.
        var eligibilite = eligibiliteService.evaluer(matriculeCommercial);
        if (!eligibilite.autorisee()) {
            throw new RegleMetierException(eligibilite.motif());
        }

        JsonNode noeudLignes = n.path("lignes");
        if (!noeudLignes.isArray() || noeudLignes.isEmpty()) {
            throw new SyncPayloadInvalideException("Une precommande sans ligne ne peut pas etre soumise");
        }
        List<LignePreCommande> lignes = new ArrayList<>();
        for (JsonNode l : noeudLignes) {
            // Désignation et conditionnement sont exigés par LignePreCommande, qui
            // échouerait sinon sur un NullPointerException opaque plutôt que
            // d'énoncer ce qui manque.
            lignes.add(new LignePreCommande(
                texteObligatoire(l, "codeProduit"),
                l.path("designation").asText(""),
                texteObligatoire(l, "conditionnement"),
                l.path("quantite").asInt()));
        }

        LocalDate dateSoumission = date(n, "dateSoumission");
        String numero = generateurNumero.genererNumero(FamilleNumero.PRECOMMANDE, dateSoumission);
        PreCommande pc = PreCommande.soumettre(numero, matriculeCommercial, dateSoumission, lignes);
        return preCommandeRepository.sauvegarder(pc).getNumeroPreCommande();
    }

    /**
     * Rejoue le passage chez un client — visa ou non-visa.
     *
     * <p>Le terminal n'envoie pas le numéro de la feuille de route : il ne le
     * connaît pas quand la tournée a été planifiée hors ligne. Il envoie la date,
     * et le serveur retrouve la tournée du commercial pour ce jour-là.</p>
     */
    private String rejouerArret(JsonNode n, String matriculeCommercial) {
        LocalDate date = date(n, "date");
        String codeClient = texteObligatoire(n, "codeClient");

        SensPassage sens = enumObligatoire(SensPassage.class, n, "sens");
        FeuillRoute tournee = switch (sens) {
            // Un client croisé en zone blanche doit pouvoir entrer dans la tournée
            // avant que son constat ne soit rejoué : la file préserve l'ordre de
            // saisie, l'ajout précède donc toujours le marquage.
            case AJOUT -> passageArretUseCase.ajouterArret(matriculeCommercial, date, codeClient);
            case VISITE -> passageArretUseCase.marquerVisite(matriculeCommercial, date, codeClient);
            case NON_VISITE -> passageArretUseCase.marquerNonVisite(matriculeCommercial, date,
                saisie(n, codeClient));
        };
        return tournee.getNumeroFeuillRoute();
    }

    private SaisieArret saisie(JsonNode n, String codeClient) {
        String codeInfoClient = n.path("codeInfoClient").asText(null);
        Integer quantiteInfo = n.hasNonNull("quantiteInfo") ? n.path("quantiteInfo").asInt() : null;
        if (codeInfoClient == null || codeInfoClient.isBlank()) {
            throw new SyncPayloadInvalideException("Le motif est obligatoire pour un client non visité");
        }
        if (codeObservationService.exigeQuantite(codeInfoClient)
            && (quantiteInfo == null || quantiteInfo <= 0)) {
            throw new IllegalArgumentException(
                "Le code " + codeInfoClient + " exige une quantité > 0 (§5.9)");
        }
        return new SaisieArret(codeClient, codeInfoClient, quantiteInfo,
            n.path("observations").asText(null));
    }

    /** Acte consigné par le terminal sur un arrêt de tournée. */
    private enum SensPassage { AJOUT, VISITE, NON_VISITE }

    // ── Lecture défensive du contenu ────────────────────────────────────────


    private String texteObligatoire(JsonNode n, String champ) {
        String valeur = n.path(champ).asText(null);
        if (valeur == null || valeur.isBlank()) {
            throw new SyncPayloadInvalideException("Champ obligatoire absent du contenu : " + champ);
        }
        return valeur;
    }

    private LocalDate date(JsonNode n, String champ) {
        String valeur = n.path(champ).asText(null);
        if (valeur == null || valeur.isBlank()) {
            throw new SyncPayloadInvalideException("Champ obligatoire absent du contenu : " + champ);
        }
        try {
            // La date de l'opération est celle du terrain, jamais celle de la
            // synchronisation : une vente d'hier rejouée aujourd'hui reste datée d'hier.
            return LocalDate.parse(valeur.length() > 10 ? valeur.substring(0, 10) : valeur);
        } catch (Exception e) {
            throw new SyncPayloadInvalideException("Date illisible dans le champ " + champ + " : " + valeur);
        }
    }

    private <E extends Enum<E>> E enumOu(Class<E> type, String valeur, E defaut) {
        if (valeur == null || valeur.isBlank()) return defaut;
        try {
            return Enum.valueOf(type, valeur);
        } catch (IllegalArgumentException e) {
            throw new SyncPayloadInvalideException(
                "Valeur inconnue pour " + type.getSimpleName() + " : " + valeur);
        }
    }

    private <E extends Enum<E>> E enumObligatoire(Class<E> type, JsonNode n, String champ) {
        return enumOu(type, texteObligatoire(n, champ), null);
    }

    /** Contenu inexploitable : réessayer à l'identique ne servirait à rien. */
    public static class SyncPayloadInvalideException extends RuntimeException {
        public SyncPayloadInvalideException(String message) {
            super(message);
        }
    }
}
