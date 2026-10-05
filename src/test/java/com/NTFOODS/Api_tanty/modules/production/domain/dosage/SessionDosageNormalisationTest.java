package com.NTFOODS.Api_tanty.modules.production.domain.dosage;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Les matières de dosage doivent toujours être stockées sous leur code canonique. */
class SessionDosageNormalisationTest {

    @Test
    void libellesAvecAccentsEtCasseMixteDeviennentDesCodesCanoniques() {
        assertThat(SessionDosage.normaliserMatiere("Maïs")).isEqualTo("MAIS");
        assertThat(SessionDosage.normaliserMatiere("Soja")).isEqualTo("SOJA");
        assertThat(SessionDosage.normaliserMatiere("  arachide ")).isEqualTo("ARACHIDE");
    }

    @Test
    void codeDejaCanoniqueEstInchange() {
        assertThat(SessionDosage.normaliserMatiere("MAIS")).isEqualTo("MAIS");
    }

    @Test
    void valeurNulleResteNulle() {
        assertThat(SessionDosage.normaliserMatiere(null)).isNull();
    }
}
