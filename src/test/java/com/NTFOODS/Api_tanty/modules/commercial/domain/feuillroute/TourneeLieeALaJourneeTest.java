package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;

import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.usecase.DeclarerFinTourneeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * La tournée n'a plus de cycle de vie propre : elle vit avec la journée.
 *
 * <p>Ce que ces tests protègent : qu'on ne puisse plus revenir à deux cycles
 * indépendants. La feuille de route en portait un — planifiée, en cours,
 * clôturée, verrouillée — qui doublait celui de la session sans lui être
 * raccordé, si bien qu'une journée pouvait se clore sur une tournée à moitié
 * saisie.</p>
 */
class TourneeLieeALaJourneeTest {

    private static final String COMMERCIAL = "COM-001";
    private static final LocalDate JOUR = LocalDate.of(2026, 8, 17);
    private static final String SESSION = "SESS-20260816-COM-001";

    private FeuillRoute tourneeDe(String... clients) {
        List<ArretFeuillRoute> arrets = new java.util.ArrayList<>();
        int ordre = 1;
        for (String c : clients) {
            arrets.add(new ArretFeuillRoute(c, ordre++, LocalDateTime.now()));
        }
        return FeuillRoute.planifier("FR-20260817-000001", SESSION, COMMERCIAL, JOUR, arrets);
    }

    @Nested
    @DisplayName("La tournée")
    class Tournee {

        @Test
        @DisplayName("Une tournée sans journée de rattachement est refusée")
        void tournee_sans_session_refusee() {
            List<ArretFeuillRoute> arrets = List.of(new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now()));

            assertThrows(NullPointerException.class,
                () -> FeuillRoute.planifier("FR-1", null, COMMERCIAL, JOUR, arrets),
                "Une tournée orpheline ne pourrait plus être ni ouverte ni close");
        }

        @Test
        @DisplayName("Une tournée vide est refusée")
        void tournee_vide_refusee() {
            assertThrows(IllegalArgumentException.class,
                () -> FeuillRoute.planifier("FR-1", SESSION, COMMERCIAL, JOUR, List.of()));
        }

        @Test
        @DisplayName("Un passage se constate sans vente : la visite n'est pas la commande")
        void visite_sans_vente() {
            FeuillRoute tournee = tourneeDe("CLI-001");

            tournee.marquerVisite("CLI-001", null);

            assertEquals(StatutArret.VISITE, tournee.getArrets().get(0).getStatut());
            assertNull(tournee.getArrets().get(0).getNumeroVente());
            assertEquals(1, tournee.nombreVisites());
        }

        @Test
        @DisplayName("Une visite qui a produit une commande porte son numéro de vente")
        void visite_avec_vente() {
            FeuillRoute tournee = tourneeDe("CLI-001");

            tournee.marquerVisite("CLI-001", "VTE-20260817-000004");

            assertEquals("VTE-20260817-000004", tournee.getArrets().get(0).getNumeroVente());
        }

        @Test
        @DisplayName("Un client non visité sans motif est refusé")
        void non_visite_sans_motif_refusee() {
            FeuillRoute tournee = tourneeDe("CLI-001");

            assertThrows(NullPointerException.class, () -> tournee.marquerNonVisite("CLI-001", null),
                "Un « pas visité » muet ne s'explique nulle part au siège");
        }

        @Test
        @DisplayName("Le commercial peut corriger un constat tant que la journée est ouverte")
        void constat_corrigeable() {
            FeuillRoute tournee = tourneeDe("CLI-001");
            tournee.marquerNonVisite("CLI-001",
                new SaisieArret("CLI-001", "Ab", null, "Responsable absent"));

            tournee.marquerVisite("CLI-001", "VTE-1");

            assertEquals(StatutArret.VISITE, tournee.getArrets().get(0).getStatut());
            assertNull(tournee.getArrets().get(0).getSaisie(),
                "Le motif d'absence ne doit pas survivre à une visite finalement faite");
        }

        @Test
        @DisplayName("Un client rencontré en route entre dans la tournée")
        void client_ajoute_en_route() {
            FeuillRoute tournee = tourneeDe("CLI-001");

            tournee.ajouterArret("CLI-009");

            assertEquals(2, tournee.getArrets().size());
            assertEquals(2, tournee.getArrets().get(1).getOrdre());
        }

        @Test
        @DisplayName("Le même client n'est pas inscrit deux fois")
        void client_deja_present_refuse() {
            FeuillRoute tournee = tourneeDe("CLI-001");

            assertThrows(IllegalArgumentException.class, () -> tournee.ajouterArret("CLI-001"));
        }

        @Test
        @DisplayName("Les clients sans constat sont énoncés nommément")
        void clients_en_attente_enonces() {
            FeuillRoute tournee = tourneeDe("CLI-001", "CLI-002", "CLI-003");
            tournee.marquerVisite("CLI-001", null);

            assertFalse(tournee.tousArretsTranches());
            assertEquals(List.of("CLI-002", "CLI-003"), tournee.clientsEnAttente());
        }
    }

    @Nested
    @DisplayName("La fin de tournée")
    class FinDeTournee {

        private SessionCommercialRepository sessionRepository;
        private FeuillRouteRepository feuillRouteRepository;
        private DeclarerFinTourneeUseCase useCase;
        private SessionCommercial journee;

        @BeforeEach
        void preparer() {
            journee = SessionCommercial.preparer(SESSION, COMMERCIAL, JOUR.minusDays(1));
            journee.lierDotation("DOT-1");
            journee.ouvrir();

            sessionRepository = mock(SessionCommercialRepository.class);
            when(sessionRepository.trouverParNumero(SESSION)).thenReturn(Optional.of(journee));
            when(sessionRepository.sauvegarder(any())).thenAnswer(i -> i.getArgument(0));

            feuillRouteRepository = mock(FeuillRouteRepository.class);
            useCase = new DeclarerFinTourneeUseCase(sessionRepository, feuillRouteRepository);
        }

        @Test
        @DisplayName("Une tournée inachevée bloque la clôture de la journée")
        void tournee_inachevee_bloque() {
            FeuillRoute tournee = tourneeDe("CLI-001", "CLI-002");
            tournee.marquerVisite("CLI-001", null);
            when(feuillRouteRepository.listerParCommercialEtDate(anyString(), any()))
                .thenReturn(List.of(tournee));

            var erreur = assertThrows(RegleMetierException.class,
                () -> useCase.executer(SESSION, COMMERCIAL));

            assertTrue(erreur.getMessage().contains("CLI-002"),
                "Le commercial doit savoir quel client il lui reste à trancher");
            verify(sessionRepository, never()).sauvegarder(any());
        }

        @Test
        @DisplayName("Une tournée entièrement tranchée laisse passer la clôture")
        void tournee_tranchee_passe() {
            FeuillRoute tournee = tourneeDe("CLI-001", "CLI-002");
            tournee.marquerVisite("CLI-001", "VTE-1");
            tournee.marquerNonVisite("CLI-002",
                new SaisieArret("CLI-002", "F", null, "Boutique fermée"));
            when(feuillRouteRepository.listerParCommercialEtDate(anyString(), any()))
                .thenReturn(List.of(tournee));

            useCase.executer(SESSION, COMMERCIAL);

            verify(sessionRepository).sauvegarder(any());
        }

        @Test
        @DisplayName("Une journée sans tournée reste clôturable")
        void journee_sans_tournee_clotturable() {
            // Le commercial a pu passer sa journée sur autre chose : c'est la
            // tournée inachevée qui bloque, pas son absence.
            when(feuillRouteRepository.listerParCommercialEtDate(anyString(), any()))
                .thenReturn(List.of());

            useCase.executer(SESSION, COMMERCIAL);

            verify(sessionRepository).sauvegarder(any());
        }
    }
}
