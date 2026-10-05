package com.NTFOODS.Api_tanty.modules.production.domain.dosage;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Une machine est identifiée une seule fois par session, quelle que soit la façon dont elle est saisie. */
class SessionDosageMachineTest {

    private static SessionDosage session() {
        return SessionDosage.ouvrir("PPH-TEST", LocalDate.now(), "AD-001");
    }

    @Test
    void identifiantsSaisisDifferemmentForment_UneSeuleMachine() {
        SessionDosage s = session();
        s.enregistrerMachine("dos-01");
        s.enregistrerMachine(" DOS-01 ");

        assertThat(s.getMachinesMobilisees()).containsExactly("DOS-01");
    }

    @Test
    void identifiantVideEstRefuse() {
        assertThatThrownBy(() -> session().enregistrerMachine("   "))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
