package com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate.Prospect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;

import java.util.List;
import java.util.Optional;

public interface ProspectRepository {
    Prospect sauvegarder(Prospect prospect);
    Optional<Prospect> trouverParCode(String codeProspect);
    List<Prospect> listerParCommercial(String matriculeCommercial);
    List<Prospect> listerParStatut(StatutProspect statut);
    List<Prospect> listerTous();
}
