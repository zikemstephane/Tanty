# SPEC_COMPTABILITE.md — Module Comptabilité NTFOODS TANTY ERP

## 1. Architecture DDD

### 1.1 Bounded Contexts

| Sous-domaine | Agrégat Racine | Entités/Value Objects | Repository |
|---|---|---|---|
| Caisse | `JournalCaisse` | `LigneCaisse` | `JournalCaisseRepository` |
| Décaissement | `Decaissement` | — | `DecaissementRepository` |
| Facture | `Facture` | `LigneFacture` | `FactureRepository` |
| Versement | `Versement` | — | `VersementRepository` |
| Prime | `PrimeCommerciale` | — | `PrimeRepository` |
| Objectif | `ObjectifCommercial` | — | `ObjectifCommercialRepository` |

### 1.2 Événements de Domaine

| Événement | Producteur | Consommateurs |
|---|---|---|
| `FactureEmiseEvent` | Facture.emettre() | Commercial (notification), DG (surveillance) |
| `VersementValideEvent` | Versement.valider() | Caisse (entrée), DG (contrôle) |
| `AlerteRougeVersementEvent` | Versement.enregistrer() | DG, Contrôleur Général (alerte) |
| `PrimeValideeEvent` | PrimeCommerciale.valider() | Caisse (décaissement), Commercial (notification) |
| `SeuilSecurisationAtteintEvent` | JournalCaisse.enregistrerEntree() | DG (transfert banque) |

### 1.3 Règles Métier Clés

- **Règle d'or 80/75**: Prime hebdo (12 000 FCFA) si ≥ 80% objectif global ET ≥ 75% par gamme
- **Séparation des fonctions (§4.2)**: Proposant ≠ Validateur ≠ Exécuteur pour les décaissements
- **Workflow décaissement 3 étapes**: PROPOSE → VALIDEE_CG → VALIDEE_DG → EXECUTE
- **Alerte rouge versement**: Écart > 5 000 FCFA et cashVerse < montantAttendu
- **Seuil sécurisation caisse**: 500 000 FCFA → événement pour transfert banque
- **Immutabilité facture payée**: Une facture payée ne peut pas être annulée → créer un avoir
- **OTP validation versement**: Validation financière nécessite code OTP

## 2. Contrôleurs REST (7 existants + 1 nouveau)

| Contrôleur | Endpoints | Statut |
|---|---|---|
| CaisseController | POST ouvrir, POST entree, POST sortie, GET par date, GET par période | ✅ Complet |
| DecaissementController | POST, PATCH valider-dg, PATCH approuver-dg, PATCH executer, PATCH annuler, GET par numéro, GET tous, GET par période | ✅ Manque: filtrer par statut |
| FactureController | POST, PATCH payer, PATCH annuler, GET par client, GET par période | ✅ Manque: consulter par numéro, filtrer par statut |
| VersementController | GET reconciliation, POST, PATCH valider, PATCH justifier, GET alertes, GET lister | ✅ Complet |
| PrimeController | POST calculer, PATCH valider, PATCH verser, GET par commercial, GET par semaine | ✅ Manque: consulter par référence |
| ObjectifCommercialController | POST, GET par commercial, GET par semaine | ✅ Complet |
| RapportComptabiliteController | GET factures | ✅ Manque: rapport décaissements, versements, primes, dashboard |
| **DashboardComptabiliteController** (nouveau) | GET /dashboard | ❌ À créer |

## 3. Gaps Identifiés et Corrections

### Gap 1: Dashboard Comptabilité
- Créer `DashboardComptabiliteController` + `DashboardComptabiliteUseCase`
  - Solde caisse du jour, total factures émises/payées/impayées, total décaissements exécutés, versements en attente, alertes rouges, primes à verser

### Gap 2: Rapport Décaissements
- Créer `GenererRapportDecaissementsUseCase` + endpoint

### Gap 3: Rapport Versements
- Créer `GenererRapportVersementsUseCase` + endpoint

### Gap 4: Rapport Primes
- Créer `GenererRapportPrimesUseCase` + endpoint

### Gap 5: Facture consulter par numéro
- Ajouter `GET /{numero}` à `FactureController`

### Gap 6: Facture filtrer par statut
- Ajouter `GET /statut/{statut}` à `FactureController`

### Gap 7: Decaissement filtrer par statut
- Ajouter `GET /statut/{statut}` à `DecaissementController`

### Gap 8: Prime consulter par référence
- Ajouter `GET /{reference}` à `PrimeController`

## 4. Sécurité et Rôles

| Rôle | Permissions Comptabilité |
|---|---|
| SECRETAIRE | Ouvrir caisse, entrées/sorties, enregistrer versements, exécuter décaissements |
| COMPTABLE | Toutes opérations caisse, factures, versements, primes, objectifs (lecture) |
| CONTROLEUR_GENERAL | Proposer décaissements, consulter tous rapports, objectifs |
| DIRECTEUR_GENERAL | Valider décaissements, consulter tous rapports, définir objectifs |
| ADMIN | Tous droits |
