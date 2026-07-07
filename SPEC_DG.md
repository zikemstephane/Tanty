# SPEC_DG.md — Module Pilotage Directeur Général NTFOODS TANTY ERP

## 1. Rôle du DG

Le DG est le **superadmin** du système. Il voit tout, contrôle tout, fixe tous les seuils.
Aucune donnée ne lui est masquée. Il a accès transversalement à tous les modules.

## 2. Architecture

Module transverse sans domaine propre — lit les agrégats et repositories des autres modules.

### 2.1 Services existants (7)

| Service | Rôle | Statut |
|---|---|---|
| `ObtenirKpisDgUseCase` | KPIs agrégés (CA, ventes, commerciaux) | ⚠️ Incomplet (stocks/trésorerie/marge = 0) |
| `ObtenirClassementDgUseCase` | Classement CA commerciaux | ✅ |
| `RapportComparatifService` | Comparaison N vs N-1 (jour/semaine/mois/année) | ✅ |
| `TauxOccupationMarcheService` | Pénétration par secteur | ✅ |
| `ZonePerformanceService` | Performance CA par secteur | ✅ |
| `ValidationsEnAttenteService` | Vue unifiée validations en attente | ✅ |
| `AnomaliesService` | Vue unifiée anomalies | ✅ |

### 2.2 Endpoints existants (7)

| Endpoint | Méthode | Description |
|---|---|---|
| `/api/dg/kpis` | GET | KPIs agrégés du mois |
| `/api/dg/classement-commerciaux` | GET | Classement CA commerciaux |
| `/api/dg/comparatif` | GET | Comparaison N vs N-1 |
| `/api/dg/taux-occupation-marches` | GET | Pénétration par secteur |
| `/api/dg/zones` | GET | Performance par secteur |
| `/api/dg/validations` | GET | Validations en attente |
| `/api/dg/anomalies` | GET | Anomalies détectées |

## 3. Gaps Identifiés

### Gap 1: Enrichir KPIs DG
- `ObtenirKpisDgUseCase` — remplacer les zéros par de vraies valeurs :
  - `stockMatieresPremieres` → somme `NiveauStock` type CENTRAL
  - `stockConsommables` → somme `NiveauStock` type TAMPON
  - `stockProduitsFinis` → somme `NiveauStock` type CENTRAL (produits finis)
  - `tresorerie` → solde caisse du jour + total versements validés - total décaissements exécutés
  - `margeGlobale` → CA - coût production (approximation: CA * 0.3 si pas de données de coût)

### Gap 2: Dashboard financier consolidé
- Nouveau use case + endpoint `GET /api/dg/dashboard-financier`
  - Solde caisse, total factures émises/payées/impayées, total décaissements (exécutés/en attente), total versements (en attente/validés), total primes à verser, total recouvrements (en cours/contentieux)

### Gap 3: Dashboard production consolidé
- Nouveau use case + endpoint `GET /api/dg/dashboard-production`
  - PPH en cours, OF en cours/honorés/annulés, lots déclarés/réceptionnés/rejetés, quantité produite (kg), nombre cartons

### Gap 4: Dashboard stock consolidé
- Nouveau use case + endpoint `GET /api/dg/dashboard-stock`
  - Niveaux par type d'entrepôt (CENTRAL, TAMPON, MOBILE), nombre d'alertes actives, nombre de produits sous seuil

### Gap 5: Journal d'audit
- Nouveau endpoint `GET /api/dg/audit`
  - Filtres: matricule, période, module, type d'action
  - Le DG voit TOUTES les actions tracées dans le système

### Gap 6: Vue utilisateurs
- Nouveau endpoint `GET /api/dg/utilisateurs`
  - Liste tous les utilisateurs avec rôle, statut actif/inactif, matricule, nom

### Gap 7: Paramètres système (seuils)
- Nouveau controller `ParametresSystemeController`
  - `GET /api/dg/parametres` — consulter tous les seuils
  - `PUT /api/dg/parametres` — modifier les seuils (caisse, alerte rouge, prime)
  - Le DG est le SEUL autorisé à modifier ces paramètres

## 4. Sécurité

Tous les endpoints DG sont réservés à `DIRECTEUR_GENERAL` et `ADMIN`.
Le DG a accès en lecture à TOUT, sans exception.
