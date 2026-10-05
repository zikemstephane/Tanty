package com.NTFOODS.Api_tanty.modules.production.domain.fiche;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.service.CalculateurPredictionsFiche;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Rattachement d'un produit grignoter à son poste fabricant, par le nom du produit. */
class PosteFabricantTest {

    @Test
    void caramelCroquetteArachideEtChipsSontRattachesAuLeurPoste() {
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("Caramel 100 g")).contains("CARAMEL");
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("Croquette coco")).contains("CROQUETTE");
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("Arachide enrobée 80 g")).contains("ARACHIDE_ENROBEE");
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("Chips plantain")).contains("CHIPS_PLANTAIN");
    }

    @Test
    void lesAccentsEtLaCasseNeChangentRien() {
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("ARACHIDE ENROBÉE")).contains("ARACHIDE_ENROBEE");
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("CARAMEL")).contains("CARAMEL");
    }

    @Test
    void unProduitSansPosteAssocieNEstPasRattache() {
        assertThat(CalculateurPredictionsFiche.posteFabricantDe("Bouillie de soja 2 L")).isEmpty();
        assertThat(CalculateurPredictionsFiche.posteFabricantDe(null)).isEmpty();
    }
}
