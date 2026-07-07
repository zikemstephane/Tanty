package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response;

import java.util.List;

/**
 * Hiérarchie du catalogue dérivée des produits existants (marque → gamme →
 * variété). Les ids sont synthétiques et stables au sein d'une réponse ;
 * il n'existe pas de tables dédiées au domaine.
 */
public record CatalogueHierarchieResponse(
    List<Marque> marques,
    List<Gamme> gammes,
    List<Variete> varietes
) {
    public record Marque(long id, String nom, String code) {}
    public record Gamme(long id, long marqueId, String nom, String code) {}
    public record Variete(long id, long gammeId, String nom, String code) {}
}
