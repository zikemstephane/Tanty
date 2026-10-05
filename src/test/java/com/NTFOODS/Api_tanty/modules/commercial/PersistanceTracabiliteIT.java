package com.NTFOODS.Api_tanty.modules.commercial;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Aller-retour en base des données de traçabilité ajoutées au lot 0A.
 *
 * <p>Un invariant respecté en mémoire ne vaut que s'il survit à la persistance :
 * ces cas vérifient que l'arbitrage et le contexte de vente franchissent
 * réellement la couche JPA.</p>
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PersistanceTracabiliteIT {

    @Autowired
    private PreCommandeRepository preCommandeRepository;

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private GenerateurNumeroMetier generateur;

    @Test
    @DisplayName("L'arbitrage survit à l'aller-retour en base")
    void arbitrage_persiste() {
        String numero = generateur.genererNumero(FamilleNumero.PRECOMMANDE, LocalDate.now());
        PreCommande pc = PreCommande.soumettre(numero, "COM-IT-01", LocalDate.now(),
            List.of(new LignePreCommande("TBSA", "Bouillie Soja Arachide", "CARTON", 100)));
        preCommandeRepository.sauvegarder(pc);

        PreCommande rechargee = preCommandeRepository.trouverParNumero(numero).orElseThrow();
        rechargee.arbitrerQuantites(Map.of("TBSA", 70), "STK-IT-01", "Stock insuffisant");
        preCommandeRepository.sauvegarder(rechargee);

        PreCommande apresArbitrage = preCommandeRepository.trouverParNumero(numero).orElseThrow();
        LignePreCommande ligne = apresArbitrage.getLignes().get(0);

        assertEquals(100, ligne.getQuantiteDemandee(), "la demande initiale doit rester lisible en base");
        assertEquals(70, ligne.getQuantiteValidee());
        assertEquals("STK-IT-01", apresArbitrage.getMatriculeArbitre());
        assertNotNull(apresArbitrage.getDateArbitrage());
        assertEquals("Stock insuffisant", apresArbitrage.getMotifArbitrage());
        assertTrue(apresArbitrage.estArbitree());
    }

    @Test
    @DisplayName("Une précommande jamais arbitrée le reste après rechargement")
    void absence_d_arbitrage_persiste() {
        String numero = generateur.genererNumero(FamilleNumero.PRECOMMANDE, LocalDate.now());
        preCommandeRepository.sauvegarder(PreCommande.soumettre(numero, "COM-IT-02", LocalDate.now(),
            List.of(new LignePreCommande("MGAE", "À Grignoter", "CARTON", 25))));

        PreCommande rechargee = preCommandeRepository.trouverParNumero(numero).orElseThrow();

        assertFalse(rechargee.estArbitree());
        assertNull(rechargee.getMatriculeArbitre());
        assertEquals(25, rechargee.getLignes().get(0).getQuantiteValidee(),
            "sans arbitrage, la quantité servie reste celle demandée");
    }

    @Test
    @DisplayName("Position et rattachement à la tournée sont bien écrits en base")
    void contexte_de_vente_persiste() {
        String numero = generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now());
        Vente vente = Vente.creer(numero, "uuid-it-" + numero, "COM-IT-01", "CLI-IT-01",
            LocalDate.now(), ModeReglement.COMPTANT, TypeVente.COMPTANT,
            List.of(new LigneVente("TBSA", "Bouillie Soja Arachide", "CARTON", 4, 2500, 0)),
            "photo://it-" + numero);
        vente.enregistrerPosition("3.8480,11.5021");
        vente.rattacherATournee("FR-IT-001");
        venteRepository.sauvegarder(vente);

        Vente rechargee = venteRepository.trouverParNumero(numero).orElseThrow();

        assertEquals("3.8480,11.5021", rechargee.getCoordonneesGPS(),
            "la position ne doit plus être perdue entre le mobile et la base");
        assertEquals("FR-IT-001", rechargee.getIdFeuillRoute());
        assertEquals("uuid-it-" + numero, rechargee.getUuidVente(),
            "l'uuid client est le socle de l'idempotence offline");
    }

    @Test
    @DisplayName("Une vente sans GPS ni tournée reste enregistrable")
    void contexte_optionnel() {
        String numero = generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now());
        Vente vente = Vente.creer(numero, null, "COM-IT-02", "CLI-IT-02",
            LocalDate.now(), ModeReglement.COMPTANT, TypeVente.COMPTANT,
            List.of(new LigneVente("MGAE", "À Grignoter", "CARTON", 2, 1000, 0)),
            null);
        vente.enregistrerPosition(null);
        vente.rattacherATournee(null);
        venteRepository.sauvegarder(vente);

        Vente rechargee = venteRepository.trouverParNumero(numero).orElseThrow();

        assertNull(rechargee.getCoordonneesGPS());
        assertNull(rechargee.getIdFeuillRoute());
        assertNotNull(rechargee.getUuidVente(), "un uuid est attribué à défaut d'être fourni");
    }
}
