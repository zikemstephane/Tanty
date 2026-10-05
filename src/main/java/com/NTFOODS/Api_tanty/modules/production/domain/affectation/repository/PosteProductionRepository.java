package com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.PosteProduction;

import java.util.List;
import java.util.Optional;

public interface PosteProductionRepository {

    List<PosteProduction> listerActifs();

    List<PosteProduction> listerTous();

    Optional<PosteProduction> parCode(String code);

    Optional<PosteProduction> parId(Long id);

    PosteProduction sauvegarder(PosteProduction poste);

    void supprimer(Long id);

    boolean existeParCode(String code);
}
