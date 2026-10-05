package com.NTFOODS.Api_tanty.modules.production.domain.formule.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;

import java.util.List;
import java.util.Optional;

public interface FormuleProductionRepository {
    FormuleProduction sauvegarder(FormuleProduction formule);
    /** Absent = produit sans formule, état valide (cf. FormuleProduction). */
    Optional<FormuleProduction> trouverParCodeProduit(String codeProduit);
    List<FormuleProduction> listerToutes();
    void supprimerParCodeProduit(String codeProduit);
}
