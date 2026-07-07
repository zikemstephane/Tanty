# TANTY ERP — Spécification Architecture du Module PRODUCTION

## 1. Architecture Générale

Le module Production transforme les matières premières en produits finis. Il est orchestré par le Plan de Production Hebdomadaire (PPH), document racine dont dépendent toutes les opérations.

```
Bon de Commande (interne ou externe)
    │
    ▼
PPH (Plan Production Hebdomadaire) — BROUILLON → VALIDE → EN_COURS → CLOTURE
    │
    ├── OF (Ordres de Fabrication) — PLANIFIE → EN_COURS → HONORE / ANNULE
    ├── Fiches Produit Journalières — verrouillées après validation stock
    ├── Affectations Journalières (postes + employés) — BROUILLON → VALIDEE
    ├── Sessions de Broyage (machinistes) — OUVERTE → CLOTUREE
    ├── Sessions de Dosage (agent doseur, fûts 50kg) — ouverte → validée
    ├── Lots Produits Finis — DECLARE → VALIDATED_BY_STOCK / REJETE
    └── Registre Production Journalier (présences/rendements employés)
```

## 2. Acteurs et Responsabilités

| Acteur | Rôle | Responsabilités |
|---|---|---|
| Chef de Production (Clive) | `CHEF_PRODUCTION` | Crée/valide PPH, publie OF, supervise rendement, valide transfert lots, carburant |
| responsable de salle | `RESPONSABLE_SALLE` | Affecte employés aux postes, saisit réalisations journalières, fiches produit |
| Chef Machiniste | `CHEF_MACHINISTE` | Ouvre/clôture sessions broyage, objectifs/realisations machinistes |
| Agent Doseur | `AGENT_DOSEUR` | Dosage/mélange selon formules, conditionnement fûts 50kg |
| Gestionnaire de Stock | `GESTIONNAIRE_STOCK` | Valide lots → entrée stock central, verrouille fiches |

## 3. Agrégats Domain (DDD)

### 3.1 PlanProductionHebdomadaire (PPH)
- Référence unique, semaine, dateDebut/dateFin, référenceBC (optionnel)
- Lignes: codeProduit + quantitéPlanifiée + quantitéProduite (cumul)
- Statuts: BROUILLON → VALIDE → EN_COURS → CLOTURE
- Invariants: au moins 1 ligne, dateDebut < dateFin, cumul seulement si EN_COURS

### 3.2 OrdreFabrication (OF)
- idOF unique, référencePPH, codeProduit, qteDemandee, qteRealisee, dateButoir
- Statuts: PLANIFIE → EN_COURS → HONORE / ANNULE
- tauxAvancement() = qteRealisee / qteDemandee * 100

### 3.3 LotProduitFini
- numeroLot, référencePPH, codeProduit, quantiteKg, nbCartons, dateProduction, DLC
- **Règle du 1er/15**: dateProduction fixée au 1er si produit avant le 15, sinon au 15
- Statuts: DECLARE → VALIDATED_BY_STOCK / REJETE
- Champs `final` (quantité, cartons) → immuable après création (Lock Numérique structurel)
- Événements: LotDeclareEvent, LotValideParStockEvent

### 3.4 FicheProduitJournaliere
- référencePPH + date = identité unique
- Lignes: codeProduit + quantiteProduite + conditionnement
- **Lock Numérique**: `verrouillee` → true dès validation stock du lot correspondant
- Aucune modification possible après verrouillage (preuve numérique immuable)

### 3.5 AffectationJournaliere
- référencePPH + date = identité unique
- Lignes: poste + employés + durée (demi-journée/journée) + objectif ajusté
- Saisies: réalisation par poste + production individuelle par employé
- Classement automatique des postes par % de réalisation
- **Lock**: VALIDEE → plus aucune saisie possible

### 3.6 SessionBroyage
- référencePPH + date + typePoudre = identité unique
- Objectifs par machiniste, réalisations par opération
- Clôture: quantitéNetteBroyeeKg + pertesKg → % pertes calculé

### 3.7 SessionDosage
- référencePPH + date = identité unique
- Matières premières utilisées (maïs, soja, arachide...)
- Machines mobilisées
- Fûts: produits - cassés = nets (prédiction produits finis sur fûts nets)
- Formules: Bouillie = 40kg maïs + 9kg soja + 1kg arachide; Mélange arachide = 40kg maïs + 9kg arachide + 1kg soja
- Validation → verrouillage

### 3.8 RegistreProduction
- référencePPH + date = identité unique
- Lignes employés: matricule + présent/absent + rendement
- Calculs: nbPresents, nbAbsents

## 4. Flux et Processus Métier

### 4.1 Cycle de Production
```
DG/Commercial exprime besoin
    → Chef Production crée PPH (BROUILLON)
    → Chef Production valide PPH (VALIDE)
    → Chef Production démarre PPH (EN_COURS)
    → Chef Production publie OF par produit
    → Agent Production crée affectation journalière
    → Machiniste ouvre session broyage
    → Agent Doseur ouvre session dosage
    → Agent Production enregistre fiche produit journalière
    → Chef Production déclare lot produit fini
    → Gestionnaire Stock valide lot → entrée stock central
    → Fiche produit verrouillée automatiquement (Lock Numérique)
    → PPH cumule production → taux d'avancement
    → PPH clôturé en fin de semaine
```

### 4.2 Règle du 1er/15 (Datage Production)
- Date de fabrication: 1er du mois si produit avant le 15, sinon 15 du mois
- Date de péremption (DLC): calculée à partir de la date de fabrication + durée de conservation
- Appliquée automatiquement lors de la déclaration de lot

### 4.3 Verrouillage Numérique (Pilier Immuabilité)
- LotProduitFini: champs quantité `final` → immuable structurellement
- FicheProduitJournaliere: `verrouillee` déclenché par LotValideParStockEvent
- AffectationJournaliere: `VALIDEE` → plus aucune saisie
- SessionDosage: `validee` → plus aucune modification
- SessionBroyage: `CLOTUREE` → plus aucune modification

## 5. Tableaux de Bord (Dashboards)

### 5.1 Dashboard Chef de Production
- KPIs: quantités produites (kg + cartons), rendement matière, TRS, productivité horaire
- État des OF: avancement par OF, taux de charge de la semaine
- Valorisation production: conversion en FCFA
- Analyse de variance: coûts standards vs réels
- Suivi carburant: consommation par véhicule vs CA zone
- Expression de besoins: alertes ruptures matières premières/emballages

### 5.2 Dashboard responsable de salle
- Tâches du jour: liste OF à réaliser par poste
- Avancement: progression par lot (% produit vs commande)
- KPIs individuels: quantité journalière, taux de rendement
- Statut de validation: fiche gelée/non modifiable après validation stock

## 6. Rapports

| Rapport | Description | Formats |
|---|---|---|
| Rapport lots | Lots par période, statut, produit | PDF, Excel, CSV, JSON |
| Rapport hebdomadaire | Quantités par réf, taux avancement BC, pertes, bilan fûts | JSON |
| Rapport quotidien | Production journalière détaillée par poste | PDF, Excel |
| Rapport comparatif BC vs Production | Écarts entre commandé et produit | PDF, Excel |
| Rapport de rendement | Rendement matière par produit/période | PDF, Excel |

## 7. Intégrations Inter-Modules

### Événements publiés par Production:
- `production.lot.declare` → Module Stock (déclenche réception produits finis)
- `production.lot.valideParStock` → Module Stock (entrée stock central)
- `production.pph.valide` → Module Stock (disponibilité PPH pour réceptions)
- `production.fiche.verrouillee` → Audit trail

### Événements consommés par Production:
- `stock.bc.valide` → BC disponible pour création PPH
- `commercial.besoin.exprime` → Déclenche création OF

## 8. Gaps Identifiés et Corrections Apportées

1. **RegistreProduction** — domaine existant, manquait controller + use case → **AJOUTÉ**
2. **OFController** — seul GET existait, manquait create/demarrer/produire/annuler → **AJOUTÉ**
3. **Dashboard Production** — aucun endpoint KPI → **AJOUTÉ** (Chef + Agent)
4. **Règle du 1er/15** — non implémentée dans LotProduitFini → **AJOUTÉE**
5. **Expression de besoins** — non implémentée → **AJOUTÉE** (signaler ruptures MP)
6. **Rapport comparatif BC vs Production** — manquait → **AJOUTÉ**
7. **Rapport quotidien** — manquait → **AJOUTÉ**
