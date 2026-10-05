package com.NTFOODS.Api_tanty.modules.stock.domain.commande;

import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** La trace de validation (qui, quand) survit à l'annulation et distingue un BC validé d'un BC annulé sans validation. */
class BonCommandeValidationTest {

    private static BonCommande bcAvecLigne() {
        BonCommande bc = BonCommande.creer("BC-TEST-1", "FOUR-1", LocalDate.now(), LocalDate.now().plusDays(7), "CM-001");
        bc.ajouterLigne("PROD-1", "Produit 1", "SKU-1", 10, new BigDecimal("100"));
        return bc;
    }

    @Test
    void validerEnregistreLeValidateurEtLaDate() {
        BonCommande bc = bcAvecLigne();

        bc.valider("CP-001");

        assertThat(bc.estValide()).isTrue();
        assertThat(bc.getMatriculeValidateur()).isEqualTo("CP-001");
        assertThat(bc.getDateValidation()).isNotNull();
    }

    @Test
    void annulerDepuisLeBrouillonNeLaisseAucuneValidation() {
        BonCommande bc = bcAvecLigne();

        bc.annuler();

        assertThat(bc.estValide()).isFalse();
        assertThat(bc.getMatriculeValidateur()).isNull();
    }

    @Test
    void annulerApresValidationConserveLaTrace() {
        BonCommande bc = bcAvecLigne();
        bc.valider("CP-001");

        bc.annuler();

        assertThat(bc.estValide()).isTrue();
        assertThat(bc.getMatriculeValidateur()).isEqualTo("CP-001");
    }

    @Test
    void validerSansValidateurEstRefuse() {
        BonCommande bc = bcAvecLigne();

        assertThatThrownBy(() -> bc.valider("  "))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(bc.estValide()).isFalse();
    }
}
