package com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.PrixExceptionnel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrixExceptionnelRepository {
    PrixExceptionnel sauvegarder(PrixExceptionnel prixExceptionnel);
    Optional<PrixExceptionnel> trouverParReference(String reference);
    List<PrixExceptionnel> listerParClient(String codeClient);
    List<PrixExceptionnel> listerParAutorisateur(String matriculeAutorisateur);
    List<PrixExceptionnel> listerParPeriode(LocalDate debut, LocalDate fin);
    String genererReference();
}
