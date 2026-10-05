package com.NTFOODS.Api_tanty.modules.stock.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.infrastructure.rapport.TableExporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — Fiche NTFoods "Infos Produits TANTY".
 * Génère un PDF listant tous les produits TANTY avec leurs codes,
 * désignations, et une grille de quantités vide à remplir sur le terrain.
 */
@Service
@Transactional(readOnly = true)
public class GenererFicheNTFoodsUseCase {

    private static final List<String> ENTETES = List.of(
        "Code produit", "Désignation", "Marque", "Gamme", "Quantité");

    private final ProduitRepository produitRepository;
    private final TableExporter tableExporter;

    public GenererFicheNTFoodsUseCase(ProduitRepository produitRepository, TableExporter tableExporter) {
        this.produitRepository = produitRepository;
        this.tableExporter = tableExporter;
    }

    public RapportFichier executer(String motif, String nom, String ville, String zone, Integer nombreColis) {
        List<Produit> produits = produitRepository.trouverTous();

        String titre = "Fiche NTFoods — Infos Produits TANTY";
        if (motif != null && !motif.isBlank()) titre += " — Motif : " + motif;
        if (nom != null && !nom.isBlank()) titre += " — " + nom;
        if (ville != null && !ville.isBlank()) titre += " — " + ville;
        if (zone != null && !zone.isBlank()) titre += " — Zone : " + zone;
        if (nombreColis != null) titre += " — " + nombreColis + " colis";

        String base = "fiche-ntfoods-" + LocalDate.now();

        List<List<String>> lignes = produits.stream()
            .map(p -> List.of(
                p.getCode().getValeur(),
                p.getDesignation() == null ? "" : p.getDesignation(),
                p.getMarque() == null ? "" : p.getMarque(),
                p.getGamme() == null ? "" : p.getGamme(),
                ""
            ))
            .toList();

        byte[] contenu = tableExporter.versPdf(titre, ENTETES, lignes);
        return new RapportFichier(base + ".pdf", "application/pdf", contenu);
    }
}
