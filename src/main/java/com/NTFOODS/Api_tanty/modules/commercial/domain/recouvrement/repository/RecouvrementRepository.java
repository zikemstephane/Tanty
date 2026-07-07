package com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;

import java.util.List;
import java.util.Optional;

public interface RecouvrementRepository {
    Recouvrement sauvegarder(Recouvrement recouvrement);
    Optional<Recouvrement> trouverParReference(String referenceRecouvrement);
    List<Recouvrement> listerParClient(String codeClient);
    List<Recouvrement> listerParCommercial(String matriculeCommercial);
    List<Recouvrement> listerParStatut(StatutRecouvrement statut);
    List<Recouvrement> listerEnContentieux();
}
