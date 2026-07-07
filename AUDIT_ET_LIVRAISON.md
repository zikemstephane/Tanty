# TANTY ERP — Rapport d'Audit & de Livraison

Ce document explique ce qui a été **audité**, ce qui a été **corrigé/ajouté** dans ce backend
Spring Boot, et ce qui **reste à faire** pour couvrir intégralement les 5 piliers stratégiques
que vous avez définis. Il est volontairement honnête sur le périmètre réellement couvert dans
cette session : le projet compte désormais **567 fichiers Java** (514 existants + 53 nouveaux/modifiés),
et une réécriture exhaustive de tous les modules ne peut pas être garantie sans compilation
réelle (voir section "Limite technique" ci-dessous).

---

## 1. Constat de départ

Le backend fourni (`Api_tanty_29052026`) était déjà une base **solide et fonctionnelle** :
architecture DDD en couches (`domain` / `application` / `infrastructure` / `presentation`),
module **Stock** très complet et conforme à votre cahier des charges (catalogue, entrepôts,
réceptions à double validation, transferts, dotations, retours, seuils, inventaires, alertes,
rapports), modules Commercial, Comptabilité, Production, Administration, Auth et DG déjà
implémentés avec un niveau de détail réel (précommandes J+1, versements avec alerte rouge,
carburant à double signature, factures/avoirs...).

Le travail de cette session a donc consisté à **auditer** ce backend contre vos 5 piliers
stratégiques, puis à combler les écarts les plus structurants et transversaux.

---

## 2. Bug critique corrigé

**`UtilisateurRepositoryImpl`** ne reportait jamais l'`id` ni la `version` lors de la conversion
vers l'entité JPA. Résultat : chaque `sauvegarder()` d'un utilisateur déjà existant
(changement de rôle, activation/désactivation, changement de mot de passe...) provoquait un
**INSERT** au lieu d'un **UPDATE**, ce qui aurait fait échouer ces opérations dès le second appel
(violation de contrainte d'unicité sur `matricule`/`email`). Corrigé.

---

## 3. Ce qui a été ajouté, pilier par pilier

### Pilier 1 — Immuabilité
- **Journal d'Audit transversal** (`shared/infrastructure/audit`) : un aspect AOP
  (`AuditAspect`) intercepte **automatiquement tous les contrôleurs de tous les modules**
  (Stock, Commercial, Comptabilité, Production, Administration, DG...) sans que chaque module
  n'ait à s'en préoccuper. Chaque appel est journalisé de façon **append-only** : matricule,
  rôle, module, endpoint, méthode HTTP, succès/échec, durée, IP.
  → Endpoints de consultation réservés DG/Contrôleur Général/Admin : `/api/audit/*`
  → Détection d'activités suspectes (annulations/modifications répétées) :
    `GET /api/audit/activites-suspectes?fenetreHeures=24&seuil=5`
- **Facture d'avoir** : déjà présent et conforme (impossible de supprimer une facture payée ;
  seule la création d'un `TypeFacture.AVOIR` référençant la facture d'origine est permise).
- **Verrouillage après validation croisée** : déjà bien géré structurellement dans le module
  Production (`LotProduitFini` : les champs de quantité sont `final`, aucune méthode de
  modification n'existe après déclaration — seule la création d'un nouveau lot est possible)
  et dans le module Stock (réceptions à double validation, transferts, dotations).

### Pilier 2 — Workflow / Validation
- **Règle J+1** : déjà implémentée (`PreCommande`, `Dotation`).
- **Double signature chargement** (secrétaire + comptable) : déjà implémentée sur le cycle
  `PreCommande` (`VALIDEE_SECRETAIRE` → `VALIDEE_COMPTABLE`).
- **Réconciliation financière immédiate** : le mécanisme d'alerte rouge sur écart existe déjà
  sur `Versement` (comparaison cash versé / montant attendu, justification obligatoire).
  *(Le rapprochement automatique avec le CA déclaré sur mobile n'a pas été retravaillé dans
  cette session — voir roadmap.)*

### Pilier 3 — Sécurisation & Anti-Fraude
- **Blocage automatique après 3 tentatives échouées** (`Utilisateur.enregistrerEchecConnexion()`)
  + déblocage manuel réservé à l'administrateur :
  `PATCH /api/administration/utilisateurs/{matricule}/debloquer`
- **Session unique** : chaque connexion réussie génère un nouvel identifiant de session
  (`sessionActuelle`) embarqué dans le JWT (claim `sid`) ; `JwtAuthFilter` rejette tout jeton
  dont le `sid` ne correspond plus à la session active — une connexion sur un nouvel appareil
  déconnecte donc bien la précédente, sans état supplémentaire à gérer côté serveur.
- **OTP (One-Time Password)** (`shared/infrastructure/otp`) : génération/vérification de codes
  à 6 chiffres, valables 5 min, max 3 tentatives de vérification. Intégré sur :
  - la validation d'un versement (`PATCH /api/comptabilite/versements/{ref}/valider`)
  - le déblocage d'un prix exceptionnel (`POST /api/commercial/tarification/prix-exceptionnel`)
  L'envoi SMS est simulé par défaut (`app.sms.provider=log`, le code apparaît dans les logs) —
  un vrai fournisseur (Orange CM, MTN CM, Twilio...) peut être branché en implémentant
  `SmsGatewayPort` sans toucher au reste du code.
- **Tarification verrouillée** (`modules/commercial/domain/tarification`) : grille tarifaire
  homologuée par produit × segment client (`GrilleTarifaire`, gérée exclusivement par le DG ou
  le nouveau rôle `DIRECTEUR_COMMERCIAL`) + dérogations ponctuelles tracées (`PrixExceptionnel`,
  protégées par OTP, motif obligatoire, écart calculé automatiquement).
- **Gouvernance des seuils Stock** : restreinte au DG (elle était auparavant aussi accessible au
  Gestionnaire de Stock, en contradiction avec vos règles) + historique append-only des
  modifications (`GET /api/stock/seuils/historique`).
- **Carburant à double validation** : déjà conforme (signatures commercial + superviseur).

### Pilier 4 — Performance / KPI
- **Règle d'or des Primes (80/75)** — réécriture complète du module Prime, qui appliquait
  auparavant une simple commission proportionnelle (non conforme à votre règle). Désormais :
  - Nouvel agrégat `ObjectifCommercial` : objectifs hebdomadaires (global + par gamme) fixés
    exclusivement par le DG ou le Contrôleur Général (`POST /api/comptabilite/objectifs`).
  - `PrimeCommerciale.calculer(...)` compare automatiquement le réalisé à ces objectifs :
    la prime fixe (**12 000 FCFA**, configurable via `app.prime.montant-hebdomadaire`) n'est
    débloquée que si le taux global ≥ 80 % **ET** le taux de **chaque** gamme ≥ 75 %. Le détail
    par gamme reste toujours visible (transparence), même quand la prime est refusée.

### Pilier 5 — Technique
- **Dépendances ajoutées** au `pom.xml` : `spring-boot-starter-aop` (audit trail),
  `spring-boot-starter-mail` (canal email), `spring-boot-starter-actuator` (supervision),
  `io.minio:minio` (archivage documentaire).
- **Services fondation** créés et prêts à l'emploi : `EmailService`, `DocumentStorageService`
  (+ `MinioConfig`) — le bucket est créé automatiquement au démarrage si MinIO est joignable.
- Conformément à vos consignes explicites, **rien n'a été implémenté** concernant : la gestion
  des terminaux mobiles, le contrôle biométrique, le formulaire de signature, le mode
  offline-first (SQLite/WorkManager côté mobile — hors périmètre backend).

---

## 3bis. Second passage — module par module (suite à votre demande explicite)

Vous avez demandé de reprendre le travail **module par module, sans exception**. Voici ce qui a
été ajouté dans ce second passage, au-delà du premier audit :

### Module Production — "Lock Numérique" explicite
- **Bug corrigé** : `LotController.valider()` ne publiait jamais les événements du domaine —
  l'événement `LotValideParStockEvent` était donc silencieusement perdu à chaque validation
  de lot par le stock.
- **`FicheProduitJournaliere` verrouillable** : nouveau champ `verrouillee`, méthode
  `modifierLignes()` (refusée si verrouillée) et `verrouiller()`.
- **Verrouillage automatique** : `FicheVerrouillageListener` écoute `LotValideParStockEvent`
  (même `referencePPH` + même `date`) et verrouille la fiche correspondante — c'est exactement
  l'exemple que vous citiez : dès que le magasinier valide, l'responsable de salle ne peut plus
  toucher à ses chiffres.
- Nouvel endpoint `PATCH /api/production/fiches/pph/{referencePPH}/date/{date}` pour corriger
  une fiche **avant** verrouillage uniquement.
- *Limite assumée* : le cumul du `PlanProductionHebdomadaire` n'est pas recalculé automatiquement
  lors d'une correction de fiche (nécessiterait une méthode `ajusterProduction` acceptant les
  deltas négatifs, absente de l'agrégat existant) — documenté dans le code.

### Module Comptabilité — Réconciliation Financière Immédiate
- `ReconciliationService` : calcule automatiquement le CA déclaré (ventes du jour du commercial)
  moins la valeur des retours physiques déjà validés par le stock (valorisés au prix homologué
  DETAIL de la grille tarifaire — voir limite ci-dessous).
- `montantAttendu` du versement devient **optionnel** : s'il n'est pas fourni, il est calculé
  automatiquement. Nouvel endpoint de prévisualisation :
  `GET /api/comptabilite/versements/reconciliation?matricule=X&date=Y`.
- *Limite assumée* : un `BonRetour` (module Stock) ne porte ni client ni prix (règle
  d'isolation stricte du Stock vis-à-vis de l'argent) — la valorisation utilise donc le prix
  homologué du segment DETAIL par défaut, pas le prix réel de la vente d'origine.

### Module DG — Comparaisons multi-niveaux & Taux d'Occupation des Marchés
- `RapportComparatifService` : remplace le calcul mensuel figé par un moteur générique
  **jour / semaine / mois / année**, méthode "à ce jour" (compare équitablement une période
  encore en cours à la même tranche relative de la période précédente), toujours en **valeur
  absolue** + variation %. `GET /api/dg/comparatif?periode=MOIS&date=2026-07-03`.
- Nouveau référentiel **Boutiques Potentielles** (`modules/commercial/domain/marche`), recensées
  par secteur (`POST /api/commercial/boutiques-potentielles`).
- `TauxOccupationMarcheService` : compare clients livrés (par `localite`) vs boutiques recensées
  par secteur. `GET /api/dg/taux-occupation-marches`.

### Module Auth/Administration — Robustesse du matricule
- `RegisterUseCase` retente désormais la création (jusqu'à 3 fois, **dans une transaction neuve
  à chaque tentative** via un collaborateur dédié `CreerUtilisateurTransactionnel`) en cas de
  collision de matricule sous concurrence — le comptage n'étant pas atomique, deux créations
  simultanées pouvaient auparavant provoquer une erreur de contrainte d'unicité au lieu de se
  résoudre proprement.

### Module Stock — Double Signature pour le Chargement (gap comblé)
`ApprouverDotationUseCase` ne vérifiait **aucune** signature avant de libérer les produits — le
gestionnaire pouvait approuver une dotation sans qu'aucune pré-commande ni aucun versement
n'existent. Nouveau `VerificationChargementService` (cross-module Stock ↔ Commercial ↔
Comptabilité, sans jamais manipuler de montant côté Stock — règle d'isolation respectée) : bloque
l'approbation tant que (a) la pré-commande du commercial pour la date d'effet n'a pas atteint
`VALIDEE_COMPTABLE`, et (b) qu'aucun versement `VALIDE`/`JUSTIFIE` n'existe pour la date de
travail correspondante. C'est une règle stricte, sans dérogation, y compris pour l'ADMIN — cohérent
avec votre exigence de rigueur "sans exception".

### Vérifications de clôture — Stock
- `Reception` et `LotProduitFini` : confirmé qu'aucune méthode de modification des quantités
  n'existe après création (champs `final`, seules des transitions d'état sont possibles) — le
  "Lock Numérique" y est donc structurellement garanti, sans risque de régression future.
- Conditionnement : confirmé que `Vente`, `PreCommande`, `Dotation` et `BonRetour` portent tous
  un identifiant de conditionnement/SKU par ligne — sélectionnable à chaque saisie, comme exigé.

### Module Commercial — Tarification Verrouillée réellement appliquée (gap critique comblé)
J'avais construit la grille tarifaire et les prix exceptionnels lors du premier passage, mais
**jamais branché sur la création de vente elle-même** : `VenteController.creerVente()` acceptait
n'importe quel `prixUnitaire` soumis, sans aucune vérification — un trou de sécurité direct sur
le Pilier 3. Corrigé avec `TarificationGuardService` : chaque ligne de vente est désormais
vérifiée contre le prix homologué du segment du client ; un écart n'est accepté que si
`referencePrixExceptionnel` pointe vers un déblocage DG/Directeur Commercial valide (client,
produit et montant strictement identiques), sinon la vente est refusée. Comportement non-bloquant
uniquement si aucun prix n'a encore été homologué pour ce produit (fluidité pendant le
paramétrage initial) — mais dès qu'un prix EST homologué, aucun écart silencieux n'est possible.

### Module Commercial — Carburant vs CA de la zone
La double signature existait déjà, mais rien ne "confrontait la dépense au chiffre d'affaires
généré dans la zone" comme l'exige la règle. `CarburantMapper` calcule désormais le CA du
commercial sur le mois courant et l'expose avec chaque demande (`caZoneMoisCourantFCFA`,
`ratioCarburantSurCaPourcent`) — visible par le superviseur avant qu'il ne signe. La décision
reste humaine (aucun blocage automatique), conformément à l'esprit "confrontation" plutôt que
"rejet automatique" du texte.

### Module Comptabilité — Caisse (vérifié, déjà conforme)
`JournalCaisse` implémente déjà un seuil de sécurisation par journée de caisse avec émission
d'un `SeuilSecurisationAtteintEvent` au franchissement — aucun gap trouvé, rien à corriger.

### Module Production (approfondi) — deux gaps trouvés et corrigés
En vérifiant les agrégats restants (`SessionBroyage`, `OrdreFabrication`, `RegistreProduction`
déjà conformes — aucune modification possible hors des transitions d'état prévues) :
- **`AffectationJournaliere.saisirRealisation()`** : le Javadoc de `valider()` affirmait "les
  saisies ne peuvent plus être modifiées", mais **aucun garde-fou ne l'appliquait réellement**
  dans le code. Corrigé : `saisirRealisation()` refuse maintenant tout appel une fois le statut
  `VALIDEE` atteint.
- **`valider()` était inatteignable** : aucun use case ni endpoint ne l'appelait jamais — le
  verrou existait dans le domaine mais personne ne pouvait le déclencher. Ajout de
  `ValiderAffectationUseCase` + `PATCH /api/production/affectations/{id}/valider` (Chef de
  Production / Admin).

### Module Comptabilité — Facture/Avoir (vérifié en détail, déjà conforme)
`Facture.annuler()` bloque explicitement toute facture au statut `PAYEE` avec un message dirigeant
vers la création d'un avoir ; l'émission d'un avoir réutilise l'endpoint générique
`POST /api/comptabilite/factures` avec `typeFacture=AVOIR` + `referenceFactureOrigine` (validé
obligatoire par l'agrégat). Aucun gap trouvé.

### Audit systématique des 26 événements de domaine — deux bugs trouvés
Suite au bug déjà trouvé sur `LotValideParStockEvent`, j'ai vérifié **les 26 classes
d'événements du projet** une par une (leur méthode de déclenchement dans l'agrégat, puis leur
publication réelle dans le contrôleur/use case appelant) :
- **`FactureEmiseEvent`** n'était jamais publié — `FactureController.emettre()` n'injectait même
  pas `EventPublisher`. Corrigé.
- **`PPHValideEvent`** n'était jamais publié — pour cause plus grave : **il n'existait tout
  simplement aucun moyen de créer ou valider un PPH via l'API** (`PPHController` n'exposait que
  des `GET`). Or le PPH est le document racine dont dépendent toutes les fiches journalières,
  affectations, sessions de broyage et ordres de fabrication (`referencePPH`). Ajout complet :
  `CreerPPHUseCase`, `ValiderPPHUseCase`, et les endpoints `POST /api/production/pph`,
  `PATCH /.../valider`, `/.../demarrer`, `/.../cloturer`.
- Tous les autres événements (Produit, BonCommande, Inventaire, Caisse, Reception, Transfert,
  Versement, Vente, PreCommande, Prime, Dotation, Alerte...) sont correctement publiés — vérifiés
  un par un, aucune correction nécessaire.
- **Gap restant documenté, non corrigé** : `MouvementEnregistreEvent` n'est en réalité jamais
  construit nulle part (`MouvementStock` ne porte aucun mécanisme de domain events). Le contrat
  `stock.mouvement.enregistre → Module Comptabilité` de votre cahier des charges n'est donc pas
  honoré. Corriger cela proprement demande de retoucher tous les points d'appel de
  `MouvementStock.enregistrer()` (réception, transfert, dotation, retour) — plus risqué sans
  compilation possible ici, laissé en roadmap prioritaire.

### Module DG — Les 3 derniers endpoints stub complétés
`/api/dg/zones`, `/api/dg/validations` et `/api/dg/anomalies` retournaient une liste vide
(non implémenté). Désormais :
- **`/zones`** : performance CA/clients/ventes par secteur sur le mois courant
  (`ZonePerformanceService`), complète le taux d'occupation déjà existant.
- **`/validations`** : vue unifiée de TOUTES les validations en attente tous modules confondus
  (précommandes secrétaire/comptable, dotations, bons de commande brouillon, réceptions étape
  1/2, versements, carburant) — évite au DG de parcourir chaque module séparément.
- **`/anomalies`** : vue unifiée des signaux à risque (alertes rouges de versement, stocks
  mobiles anormaux, écarts de réception, activités suspectes détectées par le Journal d'Audit).

### Le dernier gap connu comblé — `MouvementEnregistreEvent`
Le contrat `stock.mouvement.enregistre → Comptabilité` n'était honoré nulle part. Solution
finalement plus simple et plus sûre que prévu : `MouvementStockRepository.enregistrer(...)` est
le point d'appel **unique** utilisé par TOUS les flux qui créent un mouvement (réception,
transfert, dotation, retour, inventaire). Publier l'événement à cet endroit unique — plutôt que
de retoucher chacun de ces use cases — couvre tous les cas en un seul changement, à faible risque.

### Auth — Déconnexion explicite ajoutée
`Utilisateur.cloreSession()` existait déjà (Pilier 3 — Session Unique) mais n'était jamais
exposée. Ajout de `POST /api/auth/logout` : clôture la session active immédiatement, utile en
cas de perte/vol d'appareil sans attendre l'expiration naturelle du JWT.

### Rapports PDF/Excel — Charte Graphique TANTY appliquée
`TableExporter` (utilisé par tous les modules pour les exports) applique désormais les tokens
officiels de la charte : en-têtes Tanty Green (#14532D) avec texte blanc, lignes alternées Cream
(#FBF6E9), texte en Ink (#1A1A16), polices Poppins (titres) / Inter (corps) — et jamais de texte
sur fond jaune (règle de contraste de la charte).

### Ce qui reste, même après ce second passage
Certains éléments sont **volontairement exclus** sur vos instructions explicites (terminaux
mobiles, contrôle biométrique, formulaire de signature, SQLite/WorkManager côté mobile). D'autres
restent de vraies limites techniques honnêtes :
- Le wiring complet de l'archivage MinIO sur chaque type de document reste à faire au cas par cas.
- Les notifications email/SMS ne sont pas encore déclenchées automatiquement sur tous les
  événements de domaine (l'infrastructure existe, peu d'écouteurs concrets sont branchés).
- La génération de rapports "instantanée <1 min" est satisfaite par la légèreté architecturale
  actuelle (pas d'appel externe bloquant), mais n'a pas été mesurée en charge réelle.
- Cet environnement ne permet toujours pas de lancer `mvn compile` (voir section 4) — la
  vérification s'est faite par relecture systématique + contrôle automatisé (accolades,
  imports, cohérence package/chemin) sur les **584 fichiers** du projet, 0 anomalie détectée.
  **`mvn clean compile` reste la première chose à faire chez vous.**


Cet environnement d'exécution **n'a pas accès à Maven Central** (seuls certains dépôts comme
PyPI, npm, GitHub sont autorisés en sortie réseau ici). Il n'a donc **pas été possible de faire
tourner `mvn compile` dans ce sandbox** pour valider la compilation réelle du projet complet.

Ce qui a été fait à la place pour limiter le risque d'erreur :
- Relecture systématique des conventions exactes du projet (agrégats, value objects,
  repositories, mappers, contrôleurs) avant d'écrire le moindre nouveau fichier.
- Vérification automatisée, sur les **567 fichiers** du projet : équilibre des accolades/parenthèses,
  cohérence stricte `package` ↔ chemin de fichier, et **résolution de tous les imports internes**
  (`com.NTFOODS.*`) vers un fichier source existant. **0 anomalie détectée.**

Cela ne remplace pas une compilation Maven réelle. **La toute première chose à faire est :**

```bash
cd Api_tanty
mvn clean compile
```

et de me communiquer les éventuelles erreurs pour correction immédiate.

---

## 5. Roadmap — ce qu'il reste à faire

Le périmètre réel de votre cahier des charges (ERP complet à 9 modules) dépasse ce qu'il est
raisonnable de retravailler intégralement en une session, surtout sans pouvoir compiler pour
vérifier. Voici ce qui n'a **pas** été traité et que je recommande en prochaine étape,
par ordre de priorité :

1. **Rapprochement CA mobile ↔ cash déclaré** : automatiser la comparaison entre le CA déclaré
   sur l'app mobile et le cash versé (actuellement le module `Versement` prend `montantAttendu`
   en entrée — il faudrait le calculer automatiquement depuis les `Vente` du commercial sur la
   période, déduction faite des retours validés par le stock).
2. **Rapports comparatifs (jour/semaine/mois/année, N vs N-1)** : moteur de comparaison
   générique en valeur absolue, exposé sur toutes les interfaces de direction.
3. **Taux d'occupation des marchés** : comparer le nombre de clients livrés au nombre de
   boutiques recensées par secteur (nécessite un référentiel "boutiques potentielles" à créer).
4. **Auto-calcul des primes depuis les ventes réelles** : actuellement `totalVentesGlobalFCFA`
   et `realisationsParGammeFCFA` sont fournis en entrée de l'endpoint de calcul ; les calculer
   automatiquement en agrégeant les `Vente`/`LigneVente` par `Produit.gamme` sur la semaine.
5. **Wiring complet de l'archivage MinIO** sur chaque type de document (BC, BR, BT, BD, factures,
   exports de rapports) — la fondation (`DocumentStorageService`) est prête, il reste à l'appeler
   depuis chaque contrôleur/use case concerné.
6. **Notifications email/SMS effectivement déclenchées** sur les événements métier (le canal
   existe, `EmailService`/`SmsGatewayPort`, mais peu d'écouteurs `@EventListener` concrets
   consomment aujourd'hui les événements de domaine pour déclencher un envoi réel).
7. Étendre le pattern "règle d'or" et "tarification verrouillée" aux éventuels autres écarts
   métier non couverts ici, module par module, en suivant la même méthode d'audit.

---

## 6. Nouveaux endpoints principaux

| Domaine | Endpoint | Rôles |
|---|---|---|
| Sécurité | `POST /api/securite/otp/generer` | Authentifié |
| Sécurité | `POST /api/securite/otp/verifier` | Authentifié |
| Administration | `PATCH /api/administration/utilisateurs/{matricule}/debloquer` | ADMIN, DG |
| Audit | `GET /api/audit/utilisateur/{matricule}` | DG, Contrôleur Général, ADMIN |
| Audit | `GET /api/audit/activites-suspectes` | DG, Contrôleur Général, ADMIN |
| Stock | `GET /api/stock/seuils/historique` | DG, Contrôleur Général, Gestionnaire Stock, ADMIN |
| Tarification | `POST /api/commercial/tarification/grille` | DG, Directeur Commercial, ADMIN |
| Tarification | `POST /api/commercial/tarification/prix-exceptionnel` | DG, Directeur Commercial, ADMIN |
| Objectifs | `POST /api/comptabilite/objectifs` | DG, Contrôleur Général, ADMIN |
| Primes | `POST /api/comptabilite/primes/calculer` | Comptable, DG, ADMIN |

La documentation interactive complète reste disponible sur `/swagger-ui.html` une fois
l'application démarrée.
