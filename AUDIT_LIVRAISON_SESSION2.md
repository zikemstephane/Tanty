# AUDIT DE LIVRAISON — Session 2 (2026-07-03)

## Compilation
- **Statut** : `mvn clean compile -o` → **SUCCÈS** (0 erreurs, 0 warnings)
- **Java** : 17 (downgradé depuis 21 — JDK 17 installé sur le poste)
- **Mode** : Offline (Maven Central inaccessible — dépendances en cache local)

## Corrections de conformité spec

### §2.2 — Suppression capture signature graphique
- **Avant** : `Vente.signatureClientBase64` (champ + logique de signature obligatoire)
- **Après** : Champ supprimé. `Vente.finaliser()` ne requiert plus de signature.
- **Fichiers impactés** : `Vente.java`, `VenteJpaEntity.java`, `VenteRepositoryImpl.java`, `VenteMapper.java`, `VenteResponse.java`, `VenteController.java`

### §5.9 — Ajout InfoClientEnum
- 12 codes terrain exacts : Ab, NC, PA, VPC, NVP, M, PR, F, EV, AV, AF, S
- Codes EV/AV/AF marqués `requireQuantity=true` (validation automatique)

### §5.7 — Ajout TypeVente + SyncStatus sur Vente
- `TypeVente` : COMPTANT, CREDIT, AVARIE, DON, DOTATION
- `SyncStatus` : SYNCED, PENDING, CONFLICT (support offline-first)
- Ajout `coordonneesGPS` et `idFeuillRoute` sur Vente

## Nouveaux modules créés

### 1. FeuillRoute / ArretFeuillRoute (§5.8)
- **Domain** : `FeuillRoute` (aggregate), `ArretFeuillRoute` (entity), `SaisieArret`, `StatutFeuillRoute`, `StatutArret`
- **Repository** : `FeuillRouteRepository` + JPA impl
- **Application** : `FeuillRouteResponse`, `FeuillRouteMapper`
- **Controller** : `FeuillRouteController` — planifier, démarrer, viser, non-visé, clôturer, consulter
- **Règles** : Lock numérique après clôture, tous arrêts doivent être traités avant clôture

### 2. Prospect (§5.13)
- **Domain** : `Prospect` (aggregate), `StatutProspect`, `CanalProspection`
- **Repository** : `ProspectRepository` + JPA impl
- **Controller** : `ProspectController` — créer, convertir en client, consulter, lister

### 3. Decaissement (§5.11)
- **Domain** : `Decaissement` (aggregate), `StatutDecaissement`, `TypeDepense`
- **Workflow 3 étapes** : CG propose → DG valide → DG approuve → Secrétaire exécute
- **Séparation des fonctions** : Le proposant ne peut ni valider ni exécuter
- **Controller** : `DecaissementController` — proposer, valider-dg, approuver-dg, exécuter, annuler

### 4. Sync / Offline-First (§12)
- **Domain** : `SynchronisationLog` (aggregate), `SyncEntityType`, `SyncOperationType`, `SyncResult`
- **Service** : `SyncService` — push idempotent + pull incrémental
- **Controller** : `SyncController` — `/api/sync/push`, `/api/sync/pull`
- **Idempotence** : `idempotencyKey` unique, re-push retourne le résultat précédent

### 5. CommandeDistance (§5.7)
- **Domain** : `CommandeDistance` (aggregate), `LigneCommandeDistance`, `StatutCommandeDistance`, `CanalCommande`
- **Validation croisée** : RECUE → VALIDEE_SECRETAIRE → VALIDEE_COMPTABLE → LIVREE
- **Controller** : `CommandeDistanceController` — recevoir, valider, livrer

## Migrations Flyway
- `V1__init_schema.sql` — Schéma complet (utilisateurs, clients, articles, ventes, feuills_route, prospects, decaissements, commandes_distance, sync_logs)
- `V2__seed_catalogue.sql` — 26 articles catalogue (§7) + admin initial + 5 clients démo
- **Note** : Flyway dependency commentée (JAR non disponible hors-ligne). Décommenter dans `pom.xml` + `spring.flyway.enabled=true` quand Maven Central est joignable.

## Tests unitaires créés (6 classes)
- `VenteTest` — création, finalisation, annulation (contre-écriture), syncStatus, remise bornée
- `FeuillRouteTest` — planification, viser/non-visé, clôture, lock numérique, InfoClientEnum avec quantité
- `DecaissementTest` — workflow complet, séparation des fonctions, contre-écriture
- `PreCommandeTest` — validation croisée, InfoClientEnum requiresQuantity
- `CommandeDistanceTest` — validation croisée, annulation après livraison
- `ProspectTest` — création, conversion, non-intéressé
- `SynchronisationLogTest` — push idempotent, succès/conflit/rejeté

**Note** : Tests non exécutables hors-ligne (spring-boot-starter-test JAR non en cache). Compilation test possible une fois Maven Central joignable.

## Dépendances temporaires désactivées (hors-ligne)
| Dépendance | Raison | Réactivation |
|---|---|---|
| `io.minio:minio:8.5.17` | JARs transactifs (guava, okhttp, kotlin-stdlib, bouncycastle, snappy-java, checker-qual) non téléchargés | Décommenter dans `pom.xml` + restaurer `MinioConfig` et `DocumentStorageService` |
| `org.flywaydb:flyway-core` | JAR non en cache | Décommenter dans `pom.xml` + `spring.flyway.enabled=true` |

## Exclusions respectées (§2.2)
- [x] Pas de gestion de terminaux mobiles (RG-17 non implémenté)
- [x] Pas de contrôle biométrique
- [x] Pas de formulaire de signature graphique (supprimé du code existant)
- [x] Pas d'anti-capture d'écran
- [x] Pas de DELETE sur ventes finalisées (contre-écriture via avoir)
- [x] Pas de suppression physique (archivage)
