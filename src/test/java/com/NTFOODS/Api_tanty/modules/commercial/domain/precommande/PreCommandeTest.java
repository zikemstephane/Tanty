package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.InfoClientEnum;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreCommandeTest {

    @Test
    void soumettrePreCommandeSucces() {
        LignePreCommande ligne = new LignePreCommande("ART-001", "Farine 1kg", "Sachet 1kg", 10);
        PreCommande pc = PreCommande.soumettre("PRE-20260703-001", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(ligne));

        assertEquals(StatutPreCommande.SOUMISE, pc.getStatut());
        assertEquals(LocalDate.of(2026, 7, 4), pc.getDateEffet());
    }

    @Test
    void validationCroisee_SecretairePuisComptable() {
        LignePreCommande ligne = new LignePreCommande("ART-001", "Farine 1kg", "Sachet 1kg", 10);
        PreCommande pc = PreCommande.soumettre("PRE-20260703-002", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(ligne));

        pc.validerParSecretaire("MAT-SEC-001");
        assertEquals(StatutPreCommande.VALIDEE_SECRETAIRE, pc.getStatut());

        pc.validerParComptable("MAT-CPT-001");
        assertEquals(StatutPreCommande.VALIDEE_COMPTABLE, pc.getStatut());

        pc.marquerLivree();
        assertEquals(StatutPreCommande.LIVREE, pc.getStatut());
    }

    @Test
    void validerParComptableAvantSecretaireEchoue() {
        LignePreCommande ligne = new LignePreCommande("ART-001", "Farine 1kg", "Sachet 1kg", 10);
        PreCommande pc = PreCommande.soumettre("PRE-20260703-003", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(ligne));

        assertThrows(IllegalStateException.class, () -> pc.validerParComptable("MAT-CPT-001"));
    }

    @Test
    void livrerAvantValidationComptableEchoue() {
        LignePreCommande ligne = new LignePreCommande("ART-001", "Farine 1kg", "Sachet 1kg", 10);
        PreCommande pc = PreCommande.soumettre("PRE-20260703-004", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(ligne));
        pc.validerParSecretaire("MAT-SEC-001");

        assertThrows(IllegalStateException.class, pc::marquerLivree);
    }

    @Test
    void infoClientEnum_CodeAbSansQuantite() {
        SaisieArretValidateur.validate(InfoClientEnum.Ab, null);
    }

    @Test
    void infoClientEnum_CodeEVAvecQuantite() {
        SaisieArretValidateur.validate(InfoClientEnum.EV, 5);
    }

    @Test
    void infoClientEnum_CodeEVSansQuantiteEchoue() {
        assertThrows(IllegalArgumentException.class,
            () -> SaisieArretValidateur.validate(InfoClientEnum.EV, null));
    }

    private static class SaisieArretValidateur {
        static void validate(InfoClientEnum code, Integer qte) {
            if (InfoClientEnum.requiresQuantity(code) && (qte == null || qte <= 0)) {
                throw new IllegalArgumentException("Quantité requise pour " + code);
            }
        }
    }
}
