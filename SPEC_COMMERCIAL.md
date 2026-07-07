# SPEC_COMMERCIAL.md — Module Commercial NTFOODS TANTY ERP

## 1. Architecture DDD

### 1.1 Bounded Contexts (sous-domaines)

| Sous-domaine | Agrégat Racine | Entités/Value Objects | Repository |
|---|---|---|---|
| Vente | `Vente` | `LigneVente`, `FactureAvoir` | `VenteRepository`, `FactureAvoirRepository` |
| Précommande | `PreCommande` | `LignePreCommande` | `PreCommandeRepository` |
| Commande Distance | `CommandeDistance` | `LigneCommandeDistance` | `CommandeDistanceRepository` |
| Feuille de Route | `FeuilleDeRoute` | `ArretFeuillRoute`, `SaisieArret` | `FeuillRouteRepository` |
| Client | `Client` | — | `ClientRepository` |
| Prospect | `Prospect` | — | `ProspectRepository` |
| Recouvrement | `Recouvrement` | — | `RecouvrementRepository` |
| Carburant | `DemandeCarburant` | — | `CarburantRepository` |
| Véhicule | `Vehicule` | — | `VehiculeRepository` |
| Terminal | `Terminal` | — | `TerminalRepository` |
| Tarification | `GrilleTarifaire`, `PrixExceptionnel` | — | `GrilleTarifaireRepository`, `PrixExceptionnelRepository` |
| Marché | `ZoneDeVente`, `BoutiquePotentielle` | — | `ZoneDeVenteRepository`, `BoutiquePotentielleRepository` |

### 1.2 Événements de Domaine

| Événement | Producteur | Consommateurs |
|---|---|---|
| `VenteFinaliseEvent` | Vente.finaliser() | Stock (décrément), Comptabilité (facturation) |
| `PreCommandeValideeEvent` | PreCommande.validerParSecretaire/Comptable() | Stock (préparation), Production (planification) |

### 1.3 Règles Métier Clés

- **RG-04**: UUID côté client pour anti-doublon offline
- **RG-05**: Pas de suppression de vente validée → facture d'avoir (immutabilité)
- **RG-10**: Prime 12 000 CFA si taux global ≥ 80% ET taux par gamme ≥ 75%
- **RG-19**: Photo obligatoire si avarie > 5% du lot
- **Tarification Verrouillée**: Prix exclusivement de la grille homologuée, dérogation via prix exceptionnel OTP
- **Double signature carburant**: Commercial + Superviseur
- **Précommande J+1**: Soumission avant 18h, validation croisée secrétaire → comptable → livrée
- **Commande distance**: Même cycle que précommande (RECUE → VALIDEE_SECRETAIRE → VALIDEE_COMPTABLE → LIVREE)
- **Prospect → Client**: Conversion avec traçabilité du code client converti
- **Terminal préfinancé**: Remboursement par déduction sur primes hebdo
- **Taux d'occupation**: clients livrés / clients potentiels × 100

## 2. Contrôleurs REST (14 existants + 1 nouveau)

### 2.1 Contrôleurs Existants (Audit)

| Contrôleur | Endpoints | Statut |
|---|---|---|
| VenteController | POST, PATCH finaliser, POST non-marchand, POST avoir, PATCH valider avoir, GET avoirs | ✅ Manque: annuler, lister par période |
| PreCommandeController | POST, GET a-valider, GET date-effet, PATCH valider-secretaire, PATCH valider-comptable | ✅ Manque: refuser, livrer |
| CommandeDistanceController | POST, PATCH valider-secretaire, PATCH valider-comptable, PATCH livrer, GET | ✅ Manque: annuler |
| FeuillRouteController | POST, PATCH démarrer, PATCH viser, PATCH non-visé, PATCH clôturer, GET | ✅ Complet |
| ClientController | POST, PUT, GET, GET lister | ✅ Manque: désactiver |
| ProspectController | POST, PATCH convertir, GET, GET par commercial | ✅ Manque: non-interesse, archiver |
| RecouvrementController | POST, PATCH rembourser, PATCH contentieux, GET | ✅ Complet |
| CarburantController | POST, PATCH signer, PATCH refuser, GET | ✅ Complet |
| VehiculeController | POST, GET, GET lister, PATCH réassigner, PATCH consommation | ✅ Complet |
| TerminalController | POST, GET, GET lister, PATCH déduire, PATCH désactiver, PATCH réassigner | ✅ Complet |
| TarificationController | POST grille, GET grille, POST prix-exceptionnel, GET par client, GET par période | ✅ Complet |
| ZoneDeVenteController | POST, GET, GET par nom, PATCH calibrer, GET taux-occupation, PATCH désactiver | ✅ Complet |
| BoutiquePotentielleController | POST, GET par secteur, GET secteurs | ✅ Complet |
| RapportCommercialController | GET ventes, GET primes, GET comparatif N vs N-1 | ✅ Manque: rendement carburant, retours/prospection |

### 2.2 Nouveau Contrôleur

| Contrôleur | Endpoints | Description |
|---|---|---|
| DashboardCommercialController | GET /dashboard/commercial, GET /dashboard/dg | KPIs temps réel: CA jour/semaine, top clients/produits, taux visite, retards recouvrement |

## 3. Gaps Identifiés et Corrections

### Gap 1: Dashboard Commercial
- **Problème**: Aucun endpoint de dashboard pour les KPIs commerciaux
- **Correction**: Créer `DashboardCommercialController` + `DashboardCommercialUseCase`
  - Dashboard Commercial: CA du jour, CA semaine, nombre de ventes, top 5 clients, top 5 produits
  - Dashboard DG: CA global, taux de visite (visé/non-visé), retards recouvrement, rendement carburant

### Gap 2: Endpoints manquants — PreCommande
- **Problème**: `refuser()` et `marquerLivree()` existent dans le domaine mais ne sont pas exposés
- **Correction**: Ajouter `PATCH /{numero}/refuser` et `PATCH /{numero}/livrer`

### Gap 3: Endpoint manquant — CommandeDistance
- **Problème**: `annuler()` existe mais n'est pas exposé
- **Correction**: Ajouter `PATCH /{numero}/annuler`

### Gap 4: Endpoints manquants — Prospect
- **Problème**: `marquerNonInteresse()` et `archiver()` existent mais ne sont pas exposés
- **Correction**: Ajouter `PATCH /{code}/non-interesse` et `PATCH /{code}/archiver`

### Gap 5: Endpoint manquant — Client
- **Problème**: `desactiver()` existe mais n'est pas exposé
- **Correction**: Ajouter `PATCH /{code}/desactiver`

### Gap 6: Endpoints manquants — Vente
- **Problème**: `annuler()` existe mais n'est pas exposé; pas de listing par période/statut
- **Correction**: Ajouter `PATCH /{numeroVente}/annuler` et `GET /ventes` (liste par période/statut)

### Gap 7: Rapport Rendement Carburant
- **Problème**: Aucun rapport de rendement (CA zone / litres carburant)
- **Correction**: Créer `GenererRapportRendementCarburantUseCase` + endpoint

### Gap 8: Rapport Retours et Prospection
- **Problème**: Aucun rapport de performance des tournées (taux de visite) ni de prospection
- **Correction**: Créer `GenererRapportTournneeUseCase` + endpoint

## 4. Inter-Module Events

| Événement | Module Source → Module Cible | Trigger |
|---|---|---|
| VenteFinaliseEvent | Commercial → Stock | Vente finalisée → décrément stock |
| VenteFinaliseEvent | Commercial → Comptabilité | Vente finalisée → facturation |
| PreCommandeValideeEvent | Commercial → Stock | Précommande validée → préparation |
| PreCommandeValideeEvent | Commercial → Production | Précommande validée → planification |
| ProspectConvertiEvent | Commercial → Commercial | Prospect converti → création client |

## 5. Sécurité et Rôles

| Rôle | Permissions Commercial |
|---|---|
| COMMERCIAL | Créer ventes, précommandes, feuilles de route, prospects, carburant |
| SECRETAIRE | Valider précommandes/commandes distance, créer clients, recouvrements |
| COMPTABLE | Valider précommandes/commandes distance, émettre avoirs, recouvrements |
| DIRECTEUR_GENERAL | Tous droits, prix exceptionnels, dashboards |
| DIRECTEUR_COMMERCIAL | Grille tarifaire, prix exceptionnels, dashboards |
| CHARGEE_RP | Zones de vente, terminaux, primes |
| ADMIN | Tous droits |
