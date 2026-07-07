package com.NTFOODS.Api_tanty.modules.production.domain.of.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;

import java.util.List;
import java.util.Optional;

public interface OFRepository {
    OrdreFabrication sauvegarder(OrdreFabrication of);
    Optional<OrdreFabrication> trouverParId(String idOF);
    List<OrdreFabrication> listerParPPH(String referencePPH);
    List<OrdreFabrication> listerParStatut(StatutOF statut);
    List<OrdreFabrication> listerTous();
}
