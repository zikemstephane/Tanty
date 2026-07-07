package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.CatalogueHierarchieResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Use Case — reconstruit la hiérarchie catalogue (marque → gamme → variété) à
 * partir des champs des produits existants. Ids synthétiques stables par appel.
 */
@Service
@Transactional(readOnly = true)
public class ConsulterHierarchieCatalogueUseCase {

    private final ListerProduitsUseCase listerProduitsUseCase;

    public ConsulterHierarchieCatalogueUseCase(ListerProduitsUseCase listerProduitsUseCase) {
        this.listerProduitsUseCase = listerProduitsUseCase;
    }

    public CatalogueHierarchieResponse executer() {
        List<ProduitResponse> produits = listerProduitsUseCase.executer(null, null, null, null);

        Map<String, Long> marqueIds = new LinkedHashMap<>();          // nom marque -> id
        Map<String, Long> gammeIds = new LinkedHashMap<>();           // "marque|gamme" -> id
        List<CatalogueHierarchieResponse.Marque> marques = new ArrayList<>();
        List<CatalogueHierarchieResponse.Gamme> gammes = new ArrayList<>();
        List<CatalogueHierarchieResponse.Variete> varietes = new ArrayList<>();

        long seqMarque = 0, seqGamme = 0, seqVariete = 0;

        for (ProduitResponse p : produits) {
            String marque = valeur(p.marque());
            String gamme = valeur(p.gamme());
            String variete = valeur(p.variete());

            if (marque != null && !marqueIds.containsKey(marque)) {
                long id = ++seqMarque;
                marqueIds.put(marque, id);
                marques.add(new CatalogueHierarchieResponse.Marque(id, marque, code(marque)));
            }

            if (marque != null && gamme != null) {
                String cleGamme = marque + "|" + gamme;
                if (!gammeIds.containsKey(cleGamme)) {
                    long id = ++seqGamme;
                    gammeIds.put(cleGamme, id);
                    gammes.add(new CatalogueHierarchieResponse.Gamme(id, marqueIds.get(marque), gamme, code(gamme)));
                }

                if (variete != null) {
                    long id = ++seqVariete;
                    // une variété peut apparaître plusieurs fois ; on garde une entrée par produit
                    varietes.add(new CatalogueHierarchieResponse.Variete(
                        id, gammeIds.get(cleGamme), variete, code(variete)));
                }
            }
        }

        // Dé-doublonnage simple des variétés (gammeId + nom)
        List<CatalogueHierarchieResponse.Variete> varietesUniques = new ArrayList<>();
        Map<String, Boolean> vues = new LinkedHashMap<>();
        for (CatalogueHierarchieResponse.Variete v : varietes) {
            String cle = v.gammeId() + "|" + v.nom();
            if (vues.putIfAbsent(cle, true) == null) {
                varietesUniques.add(v);
            }
        }

        return new CatalogueHierarchieResponse(marques, gammes, varietesUniques);
    }

    private String valeur(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private String code(String nom) {
        return nom.trim().toUpperCase().replaceAll("\\s+", "_");
    }
}
