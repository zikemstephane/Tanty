package com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;

import java.util.List;
import java.util.Optional;

public interface BonCommandeRepository {
    BonCommande sauvegarder(BonCommande bonCommande);
    Optional<BonCommande> trouverParNumero(String numero);
    List<BonCommande> trouverParStatut(StatutBonCommande statut);
    List<BonCommande> trouverParFournisseur(String codeFournisseur);
    List<BonCommande> trouverTous();
    List<BonCommande> trouverEnRetard();
    long compterParAnnee(int annee);
}
