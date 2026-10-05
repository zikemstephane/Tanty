package com.NTFOODS.Api_tanty.modules.sync;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.CodeObservationService;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase.EnregistrerPassageArretUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase.PlanifierFeuillRouteUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;

import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.service.EligibilitePrecommandeService;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.DeclencherTransactionNonMarchandeUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase.EnregistrerVenteUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.sync.application.service.SyncPushDispatcher;
import com.NTFOODS.Api_tanty.modules.sync.application.service.SyncService;
import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import com.NTFOODS.Api_tanty.modules.sync.domain.repository.SynchronisationLogRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Synchronisation hors ligne — ce que le push doit garantir.
 *
 * <p>Ce que ces tests protègent avant tout : qu'un push écrive réellement. Le
 * contrôleur passait auparavant une logique de traitement vide — le serveur
 * journalisait la clé d'idempotence, répondait SUCCESS, et ne créait rien. Une
 * vente saisie en zone blanche disparaissait tout en étant réputée
 * synchronisée.</p>
 */
class SynchronisationPushTest {

    private static final String COMMERCIAL = "COM-001";

    /** Journal simulé, indexé par clé d'idempotence. */
    private Map<String, SynchronisationLog> journal;
    private SynchronisationLogRepository journalRepository;

    @BeforeEach
    void preparer() {
        journal = new HashMap<>();
        journalRepository = mock(SynchronisationLogRepository.class);
        when(journalRepository.trouverParIdempotencyKey(anyString()))
            .thenAnswer(i -> Optional.ofNullable(journal.get(i.<String>getArgument(0))));
        when(journalRepository.sauvegarder(any())).thenAnswer(i -> {
            SynchronisationLog l = i.getArgument(0);
            journal.put(l.getIdempotencyKey(), l);
            return l;
        });
    }

    private SyncService serviceAvec(SyncPushDispatcher dispatcher) {
        return new SyncService(journalRepository, dispatcher,
            mock(VenteRepository.class), mock(PreCommandeRepository.class),
            mock(FeuillRouteRepository.class));
    }

    // ── Issue d'un push ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("Issue du push")
    class Issue {

        @Test
        @DisplayName("Un push réussi rejoue réellement l'opération et retourne la référence serveur")
        void push_reussi_ecrit_vraiment() {
            SyncPushDispatcher dispatcher = mock(SyncPushDispatcher.class);
            when(dispatcher.rejouer(any(), anyString(), anyString())).thenReturn("VTE-20260816-000042");

            SynchronisationLog log = serviceAvec(dispatcher).traiterPush(
                "cle-1", COMMERCIAL, SyncEntityType.VENTE, "local-1", "hash", "{}");

            verify(dispatcher, times(1)).rejouer(SyncEntityType.VENTE, "{}", COMMERCIAL);
            assertEquals(SyncResult.SUCCESS, log.getResult());
            assertEquals("VTE-20260816-000042", log.getServerEntityRef(),
                "Le terminal doit recevoir la référence attribuée par le serveur");
        }

        @Test
        @DisplayName("Un renvoi de la même clé ne rejoue pas l'opération")
        void renvoi_idempotent() {
            SyncPushDispatcher dispatcher = mock(SyncPushDispatcher.class);
            when(dispatcher.rejouer(any(), anyString(), anyString())).thenReturn("VTE-1");
            SyncService service = serviceAvec(dispatcher);

            service.traiterPush("cle-1", COMMERCIAL, SyncEntityType.VENTE, "local-1", "hash", "{}");
            SynchronisationLog second = service.traiterPush(
                "cle-1", COMMERCIAL, SyncEntityType.VENTE, "local-1", "hash", "{}");

            verify(dispatcher, times(1)).rejouer(any(), anyString(), anyString());
            assertEquals("VTE-1", second.getServerEntityRef(),
                "Le second envoi doit renvoyer le résultat du premier, pas en créer un nouveau");
        }

        @Test
        @DisplayName("Une règle métier violée donne un conflit, jamais un succès")
        void regle_metier_violee_donne_un_conflit() {
            SyncPushDispatcher dispatcher = mock(SyncPushDispatcher.class);
            when(dispatcher.rejouer(any(), anyString(), anyString()))
                .thenThrow(new RegleMetierException("Stock insuffisant pour TBSA"));

            SynchronisationLog log = serviceAvec(dispatcher).traiterPush(
                "cle-2", COMMERCIAL, SyncEntityType.VENTE, "local-2", "hash", "{}");

            assertEquals(SyncResult.CONFLICT, log.getResult());
            assertTrue(log.getConflictDetail().contains("Stock insuffisant"),
                "Le commercial doit savoir pourquoi son opération est refusée");
            assertNull(log.getServerEntityRef(), "Un conflit ne produit aucune entité serveur");
        }

        @Test
        @DisplayName("Un contenu inexploitable est rejeté, pas mis en conflit")
        void contenu_inexploitable_rejete() {
            SyncPushDispatcher dispatcher = mock(SyncPushDispatcher.class);
            when(dispatcher.rejouer(any(), any(), anyString()))
                .thenThrow(new SyncPushDispatcher.SyncPayloadInvalideException("Contenu illisible"));

            SynchronisationLog log = serviceAvec(dispatcher).traiterPush(
                "cle-3", COMMERCIAL, SyncEntityType.VENTE, "local-3", "hash", "pas du json");

            assertEquals(SyncResult.REJECTED, log.getResult(),
                "Renvoyer le même contenu donnerait le même échec : ce n'est pas un conflit à arbitrer");
        }

        @Test
        @DisplayName("Une panne technique ne laisse aucune trace : le terminal doit pouvoir réessayer")
        void panne_technique_ne_bloque_pas_la_file() {
            SyncPushDispatcher dispatcher = mock(SyncPushDispatcher.class);
            when(dispatcher.rejouer(any(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Connexion à la base perdue"));

            assertThrows(RuntimeException.class, () -> serviceAvec(dispatcher).traiterPush(
                "cle-4", COMMERCIAL, SyncEntityType.VENTE, "local-4", "hash", "{}"));

            assertNull(journal.get("cle-4") != null ? journal.get("cle-4").getResult() : null,
                "Consigner une panne réseau comme un refus métier bloquerait une opération valide");
        }
    }

    // ── Reconstitution du contenu ───────────────────────────────────────────

    @Nested
    @DisplayName("Reconstitution de l'opération")
    class Reconstitution {

        private EnregistrerVenteUseCase venteUseCase;
        private DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase;
        private PlanifierFeuillRouteUseCase feuillRouteUseCase;
        private EnregistrerPassageArretUseCase passageArretUseCase;
        private PreCommandeRepository preCommandeRepository;
        private EligibilitePrecommandeService eligibiliteService;
        private CodeObservationService codeObservationService;
        private SyncPushDispatcher dispatcher;

        @BeforeEach
        void preparerDispatcher() {
            venteUseCase = mock(EnregistrerVenteUseCase.class);
            nonMarchandeUseCase = mock(DeclencherTransactionNonMarchandeUseCase.class);
            feuillRouteUseCase = mock(PlanifierFeuillRouteUseCase.class);
            passageArretUseCase = mock(EnregistrerPassageArretUseCase.class);
            preCommandeRepository = mock(PreCommandeRepository.class);
            codeObservationService = mock(CodeObservationService.class);
            when(codeObservationService.exigeQuantite("AV")).thenReturn(true);
            when(codeObservationService.exigeQuantite("PA")).thenReturn(false);
            when(preCommandeRepository.sauvegarder(any())).thenAnswer(i -> i.getArgument(0));

            eligibiliteService = mock(EligibilitePrecommandeService.class);
            when(eligibiliteService.evaluer(anyString())).thenReturn(
                new EligibilitePrecommandeService.Eligibilite(true, true, false, false, null, "Première tournée"));

            GenerateurNumeroMetier generateur = mock(GenerateurNumeroMetier.class);
            when(generateur.genererNumero(any(FamilleNumero.class), any()))
                .thenAnswer(i -> i.<FamilleNumero>getArgument(0).getPrefixe() + "-TEST-1");

            dispatcher = new SyncPushDispatcher(venteUseCase, nonMarchandeUseCase, feuillRouteUseCase,
                passageArretUseCase, preCommandeRepository, eligibiliteService, generateur,
                new ObjectMapper(), codeObservationService);
        }

        private Vente venteFactice(String numero) {
            return Vente.creer(numero, "COM-001", "CLI-001", LocalDate.now(),
                ModeReglement.COMPTANT, TypeVente.COMPTANT,
                List.of(new LigneVente("TBSA", "Bouillie", "TBSA-CARTON", 1, 1000, 0)));
        }

        @Test
        @DisplayName("Une vente hors ligne conserve sa date de terrain")
        void vente_conserve_sa_date_de_terrain() {
            when(venteUseCase.executer(any())).thenReturn(venteFactice("VTE-1"));

            dispatcher.rejouer(SyncEntityType.VENTE, """
                {"codeClient":"CLI-001","date":"2026-08-10","typeVente":"COMPTANT",
                 "modeReglement":"COMPTANT","uuidVente":"uuid-1",
                 "lignes":[{"codeProduit":"TBSA","designation":"Bouillie",
                            "conditionnement":"TBSA-CARTON","quantite":2,"prixUnitaire":1000}]}
                """, COMMERCIAL);

            var capteur = org.mockito.ArgumentCaptor.forClass(EnregistrerVenteUseCase.CommandeVente.class);
            verify(venteUseCase).executer(capteur.capture());
            assertEquals(LocalDate.of(2026, 8, 10), capteur.getValue().date(),
                "Une vente d'hier rejouée aujourd'hui reste datée d'hier");
            assertEquals("uuid-1", capteur.getValue().uuidVente());
            assertEquals(COMMERCIAL, capteur.getValue().matriculeCommercial(),
                "Le matricule vient de la session, pas du contenu transmis");
        }

        @Test
        @DisplayName("Le commercial est celui de la session, même si le contenu en désigne un autre")
        void matricule_non_usurpable() {
            when(venteUseCase.executer(any())).thenReturn(venteFactice("VTE-1"));

            dispatcher.rejouer(SyncEntityType.VENTE, """
                {"codeClient":"CLI-001","date":"2026-08-10","matriculeCommercial":"COM-999",
                 "lignes":[{"codeProduit":"TBSA","quantite":1,"prixUnitaire":1000}]}
                """, COMMERCIAL);

            var capteur = org.mockito.ArgumentCaptor.forClass(EnregistrerVenteUseCase.CommandeVente.class);
            verify(venteUseCase).executer(capteur.capture());
            assertEquals(COMMERCIAL, capteur.getValue().matriculeCommercial());
        }

        @Test
        @DisplayName("Un contenu absent est refusé plutôt que traité comme une réussite")
        void contenu_absent_refuse() {
            assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.VENTE, null, COMMERCIAL));
            assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.VENTE, "   ", COMMERCIAL));
            verifyNoInteractions(venteUseCase);
        }

        @Test
        @DisplayName("Une vente sans ligne est refusée")
        void vente_sans_ligne_refusee() {
            assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.VENTE,
                    "{\"codeClient\":\"CLI-001\",\"date\":\"2026-08-10\",\"lignes\":[]}", COMMERCIAL));
        }

        @Test
        @DisplayName("Un champ obligatoire manquant est énoncé, pas deviné")
        void champ_obligatoire_manquant_enonce() {
            var erreur = assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.VENTE,
                    "{\"date\":\"2026-08-10\",\"lignes\":[{\"codeProduit\":\"TBSA\",\"quantite\":1}]}",
                    COMMERCIAL));
            assertTrue(erreur.getMessage().contains("codeClient"), erreur.getMessage());
        }

        @Test
        @DisplayName("Une avarie hors ligne emprunte le chemin des transactions non marchandes")
        void avarie_rejouee_par_son_propre_use_case() {
            when(nonMarchandeUseCase.executer(any())).thenReturn(venteFactice("NM-1"));

            String ref = dispatcher.rejouer(SyncEntityType.TRANSACTION_NON_MARCHANDE, """
                {"typeVente":"AVARIE","codeProduit":"TBSA","designationProduit":"Bouillie",
                 "conditionnement":"TBSA-CARTON","quantite":2,"date":"2026-08-12",
                 "motif":"Choc transport"}
                """, COMMERCIAL);

            assertEquals("NM-1", ref);
            verifyNoInteractions(venteUseCase);
        }

        @Test
        @DisplayName("Une feuille de route hors ligne est replanifiée avec ses arrêts")
        void feuille_de_route_rejouee() {
            FeuillRoute planifiee = FeuillRoute.planifier("FR-1", "SESS-1", COMMERCIAL, LocalDate.now(),
                List.of(new com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute("CLI-001", 1, java.time.LocalDateTime.now())));
            when(feuillRouteUseCase.executer(anyString(), any(), any())).thenReturn(planifiee);

            String ref = dispatcher.rejouer(SyncEntityType.FEUILL_ROUTE,
                "{\"date\":\"2026-08-14\",\"arrets\":[{\"codeClient\":\"CLI-001\",\"ordre\":1},"
                    + "{\"codeClient\":\"CLI-002\",\"ordre\":2}]}", COMMERCIAL);

            assertEquals("FR-1", ref);
            var capteur = org.mockito.ArgumentCaptor.forClass(List.class);
            verify(feuillRouteUseCase).executer(eq(COMMERCIAL), eq(LocalDate.of(2026, 8, 14)), capteur.capture());
            assertEquals(2, capteur.getValue().size());
        }

        @Test
        @DisplayName("Une précommande hors ligne reste soumise à RG-01")
        void precommande_soumise_a_rg01() {
            when(eligibiliteService.evaluer(anyString())).thenReturn(
                new EligibilitePrecommandeService.Eligibilite(false, false, false, true,
                    LocalDate.of(2026, 8, 14), "Versement non validé"));

            var erreur = assertThrows(com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException.class,
                () -> dispatcher.rejouer(SyncEntityType.PRECOMMANDE,
                    "{\"dateSoumission\":\"2026-08-15\",\"lignes\":[{\"codeProduit\":\"TBSA\",\"conditionnement\":\"TBSA-CARTON\",\"quantite\":3}]}",
                    COMMERCIAL));

            assertTrue(erreur.getMessage().contains("Versement non validé"));
            verifyNoInteractions(preCommandeRepository);
        }

        @Test
        @DisplayName("Une ligne de précommande sans conditionnement est énoncée, pas subie")
        void precommande_sans_conditionnement_enoncee() {
            var erreur = assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.PRECOMMANDE,
                    "{\"dateSoumission\":\"2026-08-15\",\"lignes\":[{\"codeProduit\":\"TBSA\",\"quantite\":3}]}",
                    COMMERCIAL));
            assertTrue(erreur.getMessage().contains("conditionnement"), erreur.getMessage());
        }

        @Test
        @DisplayName("Au premier usage, une précommande hors ligne passe")
        void precommande_premiere_utilisation_passe() {
            String ref = dispatcher.rejouer(SyncEntityType.PRECOMMANDE,
                "{\"dateSoumission\":\"2026-08-15\",\"lignes\":[{\"codeProduit\":\"TBSA\",\"conditionnement\":\"TBSA-CARTON\",\"quantite\":3}]}",
                COMMERCIAL);

            assertEquals("PCM-TEST-1", ref);
            var capteur = org.mockito.ArgumentCaptor.forClass(PreCommande.class);
            verify(preCommandeRepository).sauvegarder(capteur.capture());
            assertEquals(COMMERCIAL, capteur.getValue().getMatriculeCommercial());
        }

        // ── Passage chez un client ──────────────────────────────────────────

        private FeuillRoute tourneeFactice() {
            return FeuillRoute.planifier("FR-20260815-000001", "SESS-20260814-COM-001", COMMERCIAL,
                LocalDate.of(2026, 8, 15),
                List.of(new ArretFeuillRoute("CLI-001", 1, java.time.LocalDateTime.now())));
        }

        @Test
        @DisplayName("Une visite hors ligne se rattache par le commercial et la date, sans numéro de tournée")
        void visite_rattachee_par_cle_naturelle() {
            when(passageArretUseCase.marquerVisite(anyString(), any(), anyString()))
                .thenReturn(tourneeFactice());

            String ref = dispatcher.rejouer(SyncEntityType.ARRET,
                "{\"sens\":\"VISITE\",\"date\":\"2026-08-15\",\"codeClient\":\"CLI-001\"}",
                COMMERCIAL);

            assertEquals("FR-20260815-000001", ref);
            verify(passageArretUseCase).marquerVisite(eq(COMMERCIAL), eq(LocalDate.of(2026, 8, 15)),
                eq("CLI-001"));
        }

        @Test
        @DisplayName("Le matricule vient de la session, jamais du contenu transmis")
        void visite_ignore_le_matricule_du_contenu() {
            when(passageArretUseCase.marquerVisite(anyString(), any(), anyString()))
                .thenReturn(tourneeFactice());

            dispatcher.rejouer(SyncEntityType.ARRET,
                "{\"sens\":\"VISITE\",\"date\":\"2026-08-15\",\"codeClient\":\"CLI-001\","
                    + "\"matriculeCommercial\":\"COM-999\"}",
                COMMERCIAL);

            verify(passageArretUseCase).marquerVisite(eq(COMMERCIAL), any(), anyString());
            verify(passageArretUseCase, never()).marquerVisite(eq("COM-999"), any(), anyString());
        }

        @Test
        @DisplayName("Un client non visité transporte son code InfoClient et son observation")
        void non_visite_conserve_le_motif() {
            when(passageArretUseCase.marquerNonVisite(anyString(), any(), any()))
                .thenReturn(tourneeFactice());

            dispatcher.rejouer(SyncEntityType.ARRET,
                "{\"sens\":\"NON_VISITE\",\"date\":\"2026-08-15\",\"codeClient\":\"CLI-001\","
                    + "\"codeInfoClient\":\"PA\",\"observations\":\"Reviendra vendredi\"}",
                COMMERCIAL);

            var capteur = org.mockito.ArgumentCaptor.forClass(SaisieArret.class);
            verify(passageArretUseCase).marquerNonVisite(eq(COMMERCIAL), eq(LocalDate.of(2026, 8, 15)),
                capteur.capture());
            assertEquals("PA", capteur.getValue().getCodeInfoClient());
            assertEquals("Reviendra vendredi", capteur.getValue().getObservations());
        }

        @Test
        @DisplayName("Un code exigeant une quantité ne passe pas sans elle, même rejoué")
        void non_visite_sans_quantite_refuse() {
            // §5.9 : EV, AV et AF exigent une quantité. La règle vit dans SaisieArret
            // et n'est pas redite ici — elle doit simplement rester traversée au rejeu.
            assertThrows(IllegalArgumentException.class,
                () -> dispatcher.rejouer(SyncEntityType.ARRET,
                    "{\"sens\":\"NON_VISITE\",\"date\":\"2026-08-15\",\"codeClient\":\"CLI-001\","
                        + "\"codeInfoClient\":\"AV\"}",
                    COMMERCIAL));
            verifyNoInteractions(passageArretUseCase);
        }

        @Test
        @DisplayName("Un sens de passage absent est rejeté, jamais deviné")
        void sens_absent_rejete() {
            var erreur = assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.ARRET,
                    "{\"date\":\"2026-08-15\",\"codeClient\":\"CLI-001\"}", COMMERCIAL));
            assertTrue(erreur.getMessage().contains("sens"), erreur.getMessage());
        }

        @Test
        @DisplayName("Une clôture de journée reste refusée, et le dit")
        void cloture_journee_refusee_explicitement() {
            var erreur = assertThrows(SyncPushDispatcher.SyncPayloadInvalideException.class,
                () -> dispatcher.rejouer(SyncEntityType.CLOTURE_JOURNEE, "{}", COMMERCIAL));

            assertTrue(erreur.getMessage().contains("versement"),
                "Le commercial doit savoir ce qui manque, pas seulement que c'est refusé");
            assertTrue(erreur.getMessage().contains("ni perdue ni validée"));
        }
    }
}
