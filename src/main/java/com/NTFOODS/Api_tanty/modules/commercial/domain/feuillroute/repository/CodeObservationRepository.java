package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.CodeObservation;

import java.util.List;
import java.util.Optional;

public interface CodeObservationRepository {

    List<CodeObservation> listerActifs();

    List<CodeObservation> listerTous();

    Optional<CodeObservation> parId(Long id);

    Optional<CodeObservation> parCode(String code);

    CodeObservation sauvegarder(CodeObservation code);

    void supprimer(Long id);

    boolean existeParCode(String code);
}
