package com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.model.Fournisseur;

import java.util.List;
import java.util.Optional;

public interface FournisseurRepository {
    Fournisseur sauvegarder(Fournisseur fournisseur);
    Optional<Fournisseur> trouverParCode(String code);
    boolean existeParCode(String code);
    List<Fournisseur> trouverTous();
    List<Fournisseur> rechercherParRaisonSociale(String partiel);
}
