package com.NTFOODS.Api_tanty.modules.production.domain.registre;

import com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate.RegistreProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.entity.LigneRegistreEmploye;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Un registre ne se corrige que le jour même de la date qu'il décrit ; passé ce délai, il est définitif. */
class RegistreProductionTest {

    private static final LocalDate AUJOURDHUI = LocalDate.of(2026, 10, 5);

    private static LigneRegistreEmploye ligne(String matricule, boolean present, double qte) {
        return new LigneRegistreEmploye(matricule, "Agent " + matricule, "CARAMEL", present,
            present ? null : "Maladie", present ? "08:00" : null, qte, present ? "KG" : null);
    }

    private static RegistreProduction registre(LocalDate date) {
        return RegistreProduction.enregistrer("PPH-001", date, "RS-001",
            List.of(ligne("AGP001", true, 10), ligne("AGP002", false, 0)));
    }

    @Test
    void corrigibleTantQueLaDateDecriteEstAujourdhui() {
        RegistreProduction r = registre(AUJOURDHUI);
        assertThat(r.estCorrigible(AUJOURDHUI)).isTrue();

        r.modifierLignes(List.of(ligne("AGP001", true, 25)), AUJOURDHUI);

        assertThat(r.getLignesEmployes()).hasSize(1);
        assertThat(r.getLignesEmployes().get(0).getQteRealisee()).isEqualTo(25);
        assertThat(r.getDateModification()).isNotNull();
    }

    @Test
    void refuseLaCorrectionUneFoisLeJourPasse() {
        RegistreProduction r = registre(AUJOURDHUI.minusDays(1));
        assertThat(r.estCorrigible(AUJOURDHUI)).isFalse();

        assertThatThrownBy(() -> r.modifierLignes(List.of(ligne("AGP001", true, 25)), AUJOURDHUI))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void refuseUneListeDeLignesVideOuNulle() {
        RegistreProduction r = registre(AUJOURDHUI);

        assertThatThrownBy(() -> r.modifierLignes(List.of(), AUJOURDHUI))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> r.modifierLignes(null, AUJOURDHUI))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nbPresentsEtNbAbsentsReconnaissentLaCorrection() {
        RegistreProduction r = registre(AUJOURDHUI);
        assertThat(r.nbPresents()).isEqualTo(1);
        assertThat(r.nbAbsents()).isEqualTo(1);

        r.modifierLignes(List.of(ligne("AGP001", true, 10), ligne("AGP002", true, 8)), AUJOURDHUI);

        assertThat(r.nbPresents()).isEqualTo(2);
        assertThat(r.nbAbsents()).isZero();
    }
}
