# Pagination Backend — Documentation des contrats API

## Vue d'ensemble

Cette documentation décrit les endpoints migrés vers la pagination Spring Data (`Pageable`/`Page<T>`), les anciens/nouveaux contrats, et les endpoints laissés non-paginés avec justification.

---

## DTO partagé : `PageResponse<T>`

```java
public class PageResponse<T> {
    private List<T> contenu;
    private long totalElements;
    private int totalPages;
    private int pageCourante;
    private int taillePage;
    private boolean dernierePage;
}
```

**Réponse JSON :**
```json
{
  "succes": true,
  "message": "20 vente(s)",
  "donnees": {
    "contenu": [ ... ],
    "totalElements": 150,
    "totalPages": 8,
    "pageCourante": 0,
    "taillePage": 20,
    "dernierePage": false
  }
}
```

### Paramètres de pagination (tous optionnels)

| Paramètre | Default | Description |
|-----------|---------|-------------|
| `page`    | `0`     | Index de page (0-based) |
| `size`    | `20`    | Taille de page |
| `sort`    | variable | Tri au format `champ,direction` (ex: `date,desc`) |

---

## Endpoints migrés vers la pagination

### 1. Ventes (Commercial)

| Endpoint | Ancien contrat | Nouveau contrat | Tri par défaut |
|----------|----------------|-----------------|----------------|
| `GET /commercial/ventes/periode` | `ApiResponse<List<VenteResponse>>` | `ApiResponse<PageResponse<VenteResponse>>` | `date,desc` |
| `GET /commercial/ventes/commercial/{matricule}` | `ApiResponse<List<VenteResponse>>` | `ApiResponse<PageResponse<VenteResponse>>` | `date,desc` |

**Ancien appel :**
```
GET /api/commercial/ventes/periode?debut=2026-01-01&fin=2026-01-31
```

**Nouvel appel :**
```
GET /api/commercial/ventes/periode?debut=2026-01-01&fin=2026-01-31&page=0&size=20&sort=date,desc
```

### 2. Mouvements de stock (Stock)

| Endpoint | Ancien contrat | Nouveau contrat | Tri par défaut |
|----------|----------------|-----------------|----------------|
| `GET /stock/mouvements` | `ApiResponse<List<MouvementResponse>>` | `ApiResponse<PageResponse<MouvementResponse>>` | `horodatage,desc` |

**Nouvel appel :**
```
GET /api/stock/mouvements?produit=PRD001&type=ENTRY_FROM_SUPPLIER&page=0&size=20&sort=horodatage,desc
```

### 3. Journal d'audit (Audit)

| Endpoint | Ancien contrat | Nouveau contrat | Tri par défaut |
|----------|----------------|-----------------|----------------|
| `GET /audit/utilisateur/{matricule}` | `ApiResponse<List<JournalAuditResponse>>` | `ApiResponse<PageResponse<JournalAuditResponse>>` | `horodatage,desc` |
| `GET /audit/periode` | `ApiResponse<List<JournalAuditResponse>>` | `ApiResponse<PageResponse<JournalAuditResponse>>` | `horodatage,desc` |
| `GET /audit/module/{module}` | `ApiResponse<List<JournalAuditResponse>>` | `ApiResponse<PageResponse<JournalAuditResponse>>` | `horodatage,desc` |
| `GET /audit/type/{typeAction}` | `ApiResponse<List<JournalAuditResponse>>` | `ApiResponse<PageResponse<JournalAuditResponse>>` | `horodatage,desc` |
| `GET /audit/echecs` | `ApiResponse<List<JournalAuditResponse>>` | `ApiResponse<PageResponse<JournalAuditResponse>>` | `horodatage,desc` |

### 4. Factures (Comptabilite)

| Endpoint | Ancien contrat | Nouveau contrat | Tri par défaut |
|----------|----------------|-----------------|----------------|
| `GET /comptabilite/factures` | `ApiResponse<List<FactureResponse>>` | `ApiResponse<PageResponse<FactureResponse>>` | `dateEmission,desc` |
| `GET /comptabilite/factures/statut/{statut}` | `ApiResponse<List<FactureResponse>>` | `ApiResponse<PageResponse<FactureResponse>>` | `dateEmission,desc` |
| `GET /comptabilite/factures/client/{codeClient}` | `ApiResponse<List<FactureResponse>>` | `ApiResponse<PageResponse<FactureResponse>>` | `dateEmission,desc` |

### 5. Précommandes (Commercial)

| Endpoint | Ancien contrat | Nouveau contrat | Tri par défaut |
|----------|----------------|-----------------|----------------|
| `GET /commercial/precommandes/a-valider` | `ApiResponse<List<PreCommandeResponse>>` | `ApiResponse<PageResponse<PreCommandeResponse>>` | `dateSoumission,desc` |
| `GET /commercial/precommandes/date-effet/{date}` | `ApiResponse<List<PreCommandeResponse>>` | `ApiResponse<PageResponse<PreCommandeResponse>>` | `dateSoumission,desc` |

---

## Tri frontend → backend migré

| Composant frontend | Colonne triée | Avant | Après |
|--------------------|---------------|-------|-------|
| `AdminAuditComponent` | `horodatage` desc | `.sort()` côté Angular | Backend `@PageableDefault(sort = "horodatage", direction = DESC)` |

---

## Endpoints NON migrés (avec justification)

| Endpoint | Raison |
|----------|--------|
| `GET /commercial/ventes/{numero}` | Retourne une seule entité — pas de pagination nécessaire |
| `GET /commercial/ventes/avoirs/{numero}` | Retourne une liste d'avoirs par vente — volume limité (1-3 avoirs par vente) |
| `GET /commercial/clients` | Volume limité (clients d'un commercial) — pas de pagination immédiate |
| `GET /commercial/recouvrements` | Volume modéré — pagination possible dans une V2 si besoin |
| `GET /commercial/carburant` | Volume faible (quelques demandes par mois) |
| `GET /comptabilite/versements` | Volume modéré — pagination possible dans une V2 |
| `GET /comptabilite/decaissements` | Volume modéré — pagination possible dans une V2 |
| `GET /comptabilite/objectifs/*` | Volume très faible (objectifs hebdomadaires) |
| `GET /stock/levels` | Volume limité au nombre de produits × entrepôts |
| `GET /stock/levels/alerts` | Volume faible (alertes actives seulement) |
| `GET /stock/levels/alerts/critical` | Volume très faible |
| `GET /stock/inventaires` | Volume limité — pagination possible dans une V2 |
| `GET /stock/receptions` | Volume modéré — pagination possible dans une V2 |
| `GET /stock/alertes` | Volume faible (alertes actives) |
| `GET /stock/sessions/*` | Volume limité aux sessions du jour |
| `GET /audit/activites-suspectes` | Retourne des agrégats, pas une liste d'entités — pas de pagination |

---

## Couches modifiées

### Backend (Spring Boot)

- **DTO partagé** : `shared/application/dto/PageResponse.java`
- **JPA Repositories** : ajout de méthodes `Page<T> findBy...(Pageable)` pour Ventes, Mouvements, Audit, Factures, Précommandes
- **Domain Repositories** : ajout d'interfaces paginées (`lister...Pagine`)
- **Repository Impl** : implémentation déléguant au JPA repo avec `.map(this::toDomain)`
- **Controllers** : endpoints mis à jour avec `@PageableDefault(size=20, sort="...", direction=DESC)`

### Frontend Angular

- **Modèle** : `core/models/page-response.model.ts` (`PageResponse<T>`, `PageParams`)
- **Services** : `CommercialService`, `ComptableService`, `AuditService`, `StockService` — méthodes mises à jour pour envoyer `page/size/sort` via `HttpParams` et retourner `PageResponse<T>`
- **Composants** : extraction `.contenu` depuis la réponse paginée dans tous les composants consommateurs
- **Tri frontend supprimé** : `AdminAuditComponent` ne trie plus côté frontend (backend s'en charge)

### Frontend Flutter

- **Modèle** : `core/network/paginated_result.dart` (`PaginatedResult<T>`)
- **Repository** : `IVenteRepository` — ajout de `getVentesPaginees({page, size, sort})`
- **Repository Impl** : `VenteRepositoryImpl` — `getVentes()` gère la rétrocompatibilité (List ou PageResponse), `getVentesPaginees()` pour la pagination explicite
- **PrecommandeRepositoryImpl** : `getPrecommandeForTomorrow()` gère la rétrocompatibilité (List ou PageResponse)

---

## Rétrocompatibilité

- Les anciens endpoints non paginés sont **conservés** — les appels sans paramètres `page/size/sort` reçoivent la première page avec la taille par défaut (20)
- Le format de réponse change : `donnees` passe de `List<T>` à `PageResponse<T>` (objet avec `contenu`)
- Les clients Flutter gèrent les deux formats (List ou PageResponse) pour la transition
- Les clients Angular ont été mis à jour pour extraire `.contenu`
