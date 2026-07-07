**NTFoods SARL — TANTY Digital**   |   Module Gestion Commerciale et des Ventes — v4.0 Ultra-Complete

**NTFoods SARL**

TANTY Digital — Système de Gestion Intégré

**MODULE DE GESTION DE LA**

**COMMERCIALISATION ET DES VENTES**

*Spécification Ultra-Complète — Architecture, UML, Règles Métier, Documents Terrain*

**Version 4.0 — ULTRA COMPLÈTE (intégrant la Fiche de Synthèse DG et la Feuille de Route terrain)**

| **Entreprise** | NTFoods SARL (TANTY) |
| --- | --- |
| **Module** | Gestion Commerciale et des Ventes |
| **Version** | 4.0 — Édition Ultra-Complète |
| **Date** | Mai 2026 |
| **Sources** | CDC v1 & DG consolidée, EPC, Fiche de Synthèse DG (scannée), Feuille de Route terrain (scannée), Rapport de Modélisation v4.0 |
| **Classification** | **CONFIDENTIEL — Usage interne — Distribution restreinte** |

| **⚠ Correction Architecturale Critique v4.0** Le pompiste n'a AUCUN accès au système d'information TANTY Digital. Sa présence physique est attestée exclusivement par la signature numérique du Superviseur NTFoods (Chef de Production ou Contrôleur de Gestion) via la plateforme web. Aucun compte utilisateur 'Pompiste' n'existe dans la base de données. |
| --- |

# **Table des Matières**

1  Synthèse Fonctionnelle et Positionnement*   ....  p.3*

2  Acteurs du Système*   ....  p.4*

3  Analyse des Documents Papier Terrain*   ....  p.5*

   3.1  Fiche de Synthèse Journalière / Hebdomadaire (DG)*   ....  p.5*

   3.2  Feuille de Route du Commercial*   ....  p.7*

4  Cas d'Utilisation — Application Mobile (UC-01 à UC-10)*   ....  p.9*

5  Cas d'Utilisation — Plateforme Web (UC-11 à UC-17)*   ....  p.12*

6  Entités et Classes Métier*   ....  p.14*

7  Attributs Détaillés des Classes*   ....  p.15*

8  Diagramme de Classes UML — Code PlantUML Complet*   ....  p.20*

9  Relations et Cardinalités*   ....  p.24*

10  Gestion des Zones Commerciales et Taux d'Occupation*   ....  p.25*

11  Double Signature Carburant (sans pompiste)*   ....  p.26*

12  Transactions Non Marchandes (Dons, Dotations, Avaries)*   ....  p.27*

13  Lock Numérique et Immuabilité des Données*   ....  p.28*

14  Authentification Forte (OTP) et Sécurité Mobile*   ....  p.29*

15  Interactions avec les Modules Stock et Production*   ....  p.30*

16  Synchronisation Mobile ↔ Système Central*   ....  p.31*

17  Contraintes Métier et Règles de Gestion (RG-01 à RG-20)*   ....  p.32*

18  Méthodes — Circuit de Validation Journalier*   ....  p.34*

19  Module Reporting, Classement et Primes*   ....  p.35*

20  Workflow Villes Déportées (hors Yaoundé)*   ....  p.36*

21  Préfinancement des Terminaux Mobiles*   ....  p.37*

22  Flux de Données Entrants et Sortants*   ....  p.38*

23  Risques d'Incohérence et Solutions*   ....  p.39*

24  Architecture Logique et Stack Technique*   ....  p.40*

25  Dépendances Fonctionnelles*   ....  p.41*

26  Hypothèses et Questions Restantes*   ....  p.42*

# **1. Synthèse Fonctionnelle et Positionnement**

## **1.1 Vision Stratégique**

Le module de Gestion Commerciale et des Ventes constitue le nœud central mobile de l'ERP TANTY Digital. Il vise à remplacer l'intégralité des supports papier (Fiche de Synthèse journalière, Feuille de Route, carnets manuscrits) par une solution numérique intégrée, garantissant la traçabilité totale de chaque franc CFA, de la sortie du produit du magasin jusqu'à l'encaissement final à la caisse.

| **Algorithme Fondamental de Bouclage** StockInitial + Production (validée par Stock) − Retours = Ventes Déclarées (validées Caisse ou Crédit) Déclinaison quotidienne (formule complète) : StockInitial + Chargement − Ventes − Dons − Dotations − Avaries = Retour Physique (soir) |
| --- |

## **1.2 Périmètre Fonctionnel**

| **Domaine** | **Fonctionnalités couvertes** | **Interface** |
| --- | --- | --- |
| Pré-commande J-1 | Saisie Fiche Infos Produits, soumission, double validation (Secrétaire + Comptable) | Mobile + Web |
| Chargement matinal | Validation croisée : Secrétaire → Comptable → Stock | Web |
| Facturation terrain | Bon de livraison / facture, prix homologués par type de client, offline-first | Mobile |
| Stock  (camion) | Suivi quantités chargées, vendues, retournées, avariées, données, dotées | Mobile |
| Transactions non marchandes | Dons,, Avaries — impact stock sans cash | Mobile |
| Versement & réconciliation | Cash versé vs ventes déclarées, alerte rouge, motif obligatoire | Mobile + Web |
| Recouvrement(seul le chef de procuction peut faire les recouvrements le commercial sur son interfaces du clients concernés peut juste voir si le recouvrement à été effectué ou pas) | Apurement dettes clients, reçus de caisse | Web (accès restreint ) |
| Logistique carburant | Fiche carburant, double signature Commercial + Superviseur | Mobile + Web |
| Prospection CRM | Nouveaux prospects, taux d'occupation marché, feuille de route digitale | Mobile |
| Reporting & KPI | Classement journalier,hebdo,mensuel,annuel, primes 12 000 CFA, tableaux de bord,comparaison des données  N(jour,Hebdo,mensuel,annuel) vs N-1 | Web, mobile(pour le DG) |


## **1.3 Architecture Applicative**

Le système repose sur une architecture découplée à trois niveaux, approche API First pour paralléliser le développement :

- Backend Spring Boot 3.x (Java 17+) : logique métier centralisée, API REST + WebSocket, JWT Authentication.

- Frontend Mobile Ionic 7 + Capacitor 5 : application Android/iOS, SQLite locale, mode offline-first obligatoire.

- Frontend Web Angular 18+ : plateforme interne SPA TypeScript pour la direction, la comptabilité, le stock et la production.

- Base de données PostgreSQL 15+ : référentiel unique, ACID, JSONB, géospatial, Audit Trail complet, chiffrement AES-256.


# **2. Acteurs du Système**



| --- |

| **Acteur** | **Interface** | **Responsabilités Clés** |
| --- | --- | --- |
| **Commercial** | App Mobile | Pré-commande, facturation terrain, retours, prospection, saisie carburant, justification écart, consultation classement |
| **Comptable (M. Mbakop)** | Web | Validation commandes J+1, arbitrage quantités, autorisation crédits, factures doit/avoir, définition objectifs |
| **Secrétaire (Caisse)** | Web | Encaissement cash, alerte rouge, signature commande J+1, décaissements (après DG + CG) |
| **Gestionnaire Stock (Steve)** | Web | Chargement physique, validation retours soir (lock numérique), 3 magasins |
| **Directeur Général (DG)** | Web + Mobile | Déblocage prix exceptionnels, arbitrage décaissements, vision KPI globale, objectifs |
| **Chargée RP ****&**** Commerciale** | Web | Grands comptes, classement commerciaux, calcul primes, recouvrement (restreint), promotions DLC |
| **Chef Prod. ****&**** Logistique** | Web | Superviseur carburant (double signature), achats terrain, recouvrement (restreint) |
| **Contrôleur de Gestion** | Web | Audit carburant, validation décaissements, rendement véhicule vs CA, séc. fonds |
| **Service Production(Chef de production, Chef Machiniste,Agent Doseur )** | Web | Saisie kg produits, conversion en unités,  |
| **Client (tiers externe)** | Aucune | Destinataire ventes, porteur de dettes, 

## **2.1 Hiérarchie d****'****Héritage — Classes Utilisateurs**

| abstract class Utilisateur     │     ├── class Commercial         (app mobile + idTerminal + dernierOTP)     ├── class Comptable          (web — arbitrage + facturation)     ├── class Secretaire         (web — encaissement + lock commande J+1)     ├── class GestionnaireStock  (web — chargement + retours)     ├── class DirecteurGeneral   (web/mobile — arbitrage prix + KPI)     ├── class ChargeeRP          (web — classement + primes + recouvrement)     ├── class ChefProduction     (web — superviseur carburant)     ├── class ControleurGestion  (web — audit de toutes les actions clés)     └── class AgentProduction    (web — saisie production)  abstract class Tiers     ├── class Client  (DISTRIBUTEUR │ GROSSISTE │ DETAILLANT │ SuperMarché, ClientLamda)     └── class Fournisseur |
| --- |

# **3. Analyse des Documents Papier Terrain**

L'analyse des documents physiques scannés (Fiche de Synthèse DG et Feuille de Route du Commercial) est fondamentale : ces formulaires définissent exactement les données à digitaliser, leur structure et leur circuit de validation. Chaque champ du document papier doit avoir un équivalent numérique dans l'application.

## **3.1 Fiche de Synthèse Journalière / Hebdomadaire — Document DG**

Document de pilotage central circulant entre services selon ce circuit signataire obligatoire :

| **PRODUCTION (8h-9h)** | **→** | **STOCK (9h-10h)** | **→** | **CAISSE (10h-11h)** | **→** | **SER.COM/COMPT (11h-12h)** |
| --- | --- | --- | --- | --- | --- | --- |
| **→** | **C.G (12h-14h)** | **→** | **DG (Signature finale)** |  |

### **3.1.1 Section STOCK (partie gauche de la fiche)**

Colonnes identifiées sur la fiche physique — à digitaliser champ par champ dans LigneInfoProduit :

| **Colonne Papier** | **Champ Digital (LigneInfoProduit)** | **Type** | **Règle Métier** |
| --- | --- | --- | --- |
| PROD (Code produit) | Article.codeProduit | String | Ex: MGASS(80), MCGAS(24), GGASS(14), MGPL(20), MODGPL(20), TBS(60), RASS(25), PRASS(12), RCP 1L(12), RCP 2L(4), RCP 5L(1), PTASS(12), TASS(25), BBV 1L(12), BBV 2L(4), TCH(12), THBP(1), TC 1L(12), TC 2L(4), TC 5L(1), TC 10L(1) |
| QTE Prod | qteProduite | Integer | Saisie par le Service Production |
| STOCK Veille | stockVeille | Integer | Calculé = stockFinal de J-1 |
| STOCK Voitu (camion) | stockCamion | Integer | Stock fictif actuel du commercial |
| STOCK Total | stockTotal | Integer | CALCULÉ = stockVeille + qteProduite + stockCamion |
| SORTIES | qteSorties | Integer | Chargement du commercial ce matin |
| STOCK Final | stockFinal | Integer | CALCULÉ = stockTotal − qteSorties |
| Qte avo. dot. prom | qteAvoDotProm | Integer | Avaries + Dotations + Promotions déclarées |
| QTE FACTURÉE | qteFacturee | Integer | Validé par Stock après comptage physique (lock) |

### **3.1.2 Section SECRÉTAIRE / CA Produits**

| **Colonne Papier** | **Champ Digital** | **Note** |
| --- | --- | --- |
| CA YAOUNDE | Versement.caBranche où branche='YAOUNDE' | Agrégat des CA des commerciaux Yaoundé |
| CA DOUALA | Versement.caBranche où branche='DOUALA' | Agrégat des CA des commerciaux Douala |

### **3.1.3 Section COMPTABILITÉ (partie droite de la fiche)**

Tous les champs de la colonne COMPTABILITÉ de la Fiche de Synthèse sont des agrégats calculés par le système pour Yaoundé, Douala et le TOTAL :

| **Élément Papier** | **Champ Digital Correspondant** | **Type** | **Observation** |
| --- | --- | --- | --- |
| Effectif | Reporting.nbCommerciaux | Integer | Nb de commerciaux actifs ce jour |
| V.A.C.Init (Valeur Avant Crédit initiale) | Reporting.vacInit | BigDecimal | CA brut avant déduction des crédits |
| C.A (Chiffre d'Affaires) | Vente.montantTTC agrégé | BigDecimal | Somme de toutes les ventes TTC |
| V.A.C (Valeur Après Crédit) | Reporting.vac | BigDecimal | CA − créances (VAC = Vte cpt + Recouvrement) |
| Destockage | MouvementStock.type=DESTOCKAGE montant | BigDecimal | Opérations de déstockage |
| RECOUVREMENTS | Recouvrement.montantEncaisse | BigDecimal | Créances client apurées ce jour |
| Vte au cpt (Vente au comptant) | Versement.totalComptant | BigDecimal | Σ des ventes COMPTANT de la journée |
| Solde V.A.C | Reporting.soldeVAC | BigDecimal | V.A.C − Vte au cpt |
| Cumul C.A | Reporting.cumulMensuelCA | BigDecimal | Cumul depuis le 1er du mois |
| E.Chèques | Versement.montantCheques | BigDecimal | Chèques encaissés |
| VST banques | Versement.montantVirements | BigDecimal | Virements bancaires reçus |
| DEPENSES just (justifiées) | Versement.depensesJustifiees | BigDecimal | Dépenses avec pièce justificative |
| DEPENSES non just | Versement.depensesNonJust | BigDecimal | Dépenses sans justificatif → alerte |
| S.Caisse (Solde Caisse) | Caisse.soldeActuel | BigDecimal | Solde en caisse physique |
| Mtant AVARIES | MouvementStock.montantAvaries | BigDecimal | Valeur des avaries déclarées |
| Mtant DOT/PROMO | Vente.montantDotPromo | BigDecimal | Valeur des dotations et promotions |
| Montant TOTAL NON VENDU | InfoProduit.montantStockRestant | BigDecimal | Valeur du stock non écoulé |
| STOCK INITIAL | StockProduitFini.qteDebut | Integer | Référence module Stock |
| STOCK AJOUTÉ | StockProduitFini.qteProduction | Integer | Production du jour validée |
| STOCK SORTIE | StockProduitFini.qteSorties | Integer | Chargements + avaries + dons |
| STOCK FINAL | StockProduitFini.qteFin | Integer | = Init + Ajouté − Sortie |

### **3.1.4 Produits identifiés sur la Fiche de Synthèse (2ème page scannée)**

La fiche recto verso fait apparaître des colonnes de gammes supplémentaires, avec des lignes de détail par type de conditionnement :

| **Gamme / Ligne produit** | **Sous-types observés** | **Codes déduits** |
| --- | --- | --- |
| CARAM (Caramel) | Montant | — |
| BOUIL (Bouillie) | Mini, Moyen, Grand, TOTAL | TBS, RBS, PRBS, PTBS |
| THBP | Seau (1 unité) | THBP |
| CROO | Mini, Moyen, Grand | CROO Mini/Moy/Grand |
| ENRO | Mini, Moyen, Grand | ENRO Mini/Moy/Grand |
| CHIPS | POIDS + colonnes | CHIPS |
| CHOCO | — | TCH, THBP |
| TCH | — | TCH(12) |
| ENRO (Enrobés) | POIDS, Mini, Moyen, Grand | ENRO |
| RCP (Récipients/Seaux) | RCP(21), RCP(1L), RCP(5L) | RCP 1L, 2L, 5L |
| BBV (1L)(2L) | Bidons | BBV 1L(12), BBV 2L(4) |
| PTASS, TASS, RASS | Sachets assortis | PTASS(12), TASS(25), RASS(25) |
| SSVAS, SSVAM | — | Variantes MGASS/MCGAS |
| TC (Tanty Choco) | TC(21L), TC(5L), TC(2L), TC(1L) | TC 10L(1), TC 5L(1), TC 2L(4), TC 1L(12) |
| BEIGN, BEIS | Beignets | — |

## **3.2 Feuille de Route du Commercial**

Document terrain individuel signé par : Commercial → Contrôleur des Ventes → Responsable Commercial → Comptable. Digitalisé en classe FeuillRoute avec ses arrêts (ArretFeuillRoute).

### **3.2.1 En-tête de la Feuille de Route**

| **Champ Papier** | **Champ Digital** | **Type** | **Classe cible** |
| --- | --- | --- | --- |
| Nom du commercial | Utilisateur.nomComplet | String | Auto-rempli depuis le profil |
| Zone | ZoneDeVente.nomZone | String | Affectation depuis le backoffice |
| Ville | ZoneDeVente.ville | String | Ex: Yaoundé, Douala, Bertoua |
| Date | FeuillRoute.dateRoute | Date | Date de la tournée |

### **3.2.2 Corps du Tableau — Colonnes Produits**

Chaque ligne = un arrêt client. Colonnes produits identifiées sur le document physique :

| **Colonne Produit** | **Code Article** | **Type** |
| --- | --- | --- |
| TBS | TBS (Tanty Bouillie Soja) | Bouillie |
| RBS | RBS (Reine Bouillie Soja) | Bouillie |
| PTBS | PTBS (Petite Tanty Bouillie Soja) | Bouillie |
| PRBS | PRBS (Petite Reine Bouillie Soja) | Bouillie |
| TFS | TFS (Tanty Farines Spéciales) | Bouillie |
| TCH | TCH (Tanty Choco) | Choco |
| BS | BS (Bouillie Soja standard) | Bouillie |
| HV | HV (Huile Végétale) | Ingrédient |
| MGASS | MGASS (Mini Grigno Arachide Sachet S) | Snacking |
| GGASS | GGASS (Grand Grigno Arachide) | Snacking |
| MoGASS | MoGASS (Moyen Grigno Arachide) | Snacking |
| RCP₁L | RCP 1 Litre | Seau |
| RCP₂L | RCP 2 Litres | Seau |
| RCP₅L | RCP 5 Litres | Seau |
| TCP₁L | TCP 1 Litre | Seau |
| TCP₂L | TCP 2 Litres | Seau |
| CA | Chiffre d'Affaires ligne | Calculé |
| RECETTES | Argent encaissé sur place | BigDecimal |
| INFOS CLIENTS | Codes information (voir liste) | Enum[] |

### **3.2.3 Bas du Tableau — Récapitulatif**

| **Champ Papier** | **Champ Digital** | **Règle** |
| --- | --- | --- |
| TOTAL LIVRAISON | FeuillRoute.qtesTotalLivrees (par article) | Somme de chaque colonne produit |
| * QUANTITÉ LIVRÉES | LigneInfoProduit.qteLivrees | Sortie physique du dépôt ce matin |
| ** QUANTITÉ TROUVÉES | LigneInfoProduit.qteRetrouvees | Comptage physique invendus le soir |
| VENTE A CREDIT : …. | Versement.totalCredit | Σ des ventes à crédit de la journée |
| RECOUVREMENT : …. | Versement.totalRecouvrement | Σ des recouvrements collectés |
| DEPENSES : …. | Versement.depensesJustifiees + depensesNonJust | Carburant + frais divers |
| VERSEMENT : …. | Versement.cashVerse | Cash remis physiquement à la Secrétaire |

### **3.2.4 Tableau Bas — Soldes Clients**

| **Colonne Papier** | **Champ Digital** | **Type** |
| --- | --- | --- |
| N° (numéro client sur la liste) | ArretFeuillRoute.numOrdre | Integer |
| Nom du Client | Client.nomClient | String |
| Solde Initial | Client.soldeDette au départ de la tournée | BigDecimal |
| Recouvrement | Recouvrement.montantEncaisse sur place | BigDecimal |
| Solde final | soldeDette − recouvrement (calculé) | BigDecimal |
| Détails | Client.observations (notes libres) | String |
| Tel Client | Client.telephone | String |

| **Liste Complète des Abréviations INFOS CLIENTS — Enum InfoClientEnum** Ab  = Absence responsable commande EV* = Emballage Vide (signaler les quantités) NC  = Nouveau Client PA  = Pas d'Argent (sans astérisque) AV* = Avaries (signaler les quantités) VPC = Vend les Produits Concurrents AF* = Affiche (signaler les quantités) NVP = Ne Vend Plus F*  = Fermé M   = Mévente PR  = Promotion S   = Seau → Les codes marqués * imposent de renseigner les quantités associées. → NB : Veuillez porter toutes remarques et mentions supplémentaires au verso. |
| --- |

### **3.2.5 Signataires de la Feuille de Route**

| **Signataire** | **Rôle** | **Moment** |
| --- | --- | --- |
| Commercial | Initiateur et responsable terrain | En fin de journée |
| Contrôleur des Ventes | Vérification des quantités et CA | Fin de journée au retour |
| Responsable Commercial | Validation stratégique de la tournée | Fin de journée ou lendemain matin |
| Comptable | Validation comptable et financière finale | Lendemain matin avant chargement J+1 |

# **4. Cas d****'****Utilisation — Application Mobile (UC-01 à UC-10)**

Dix cas d'utilisation couvrent l'intégralité des interactions du commercial terrain avec l'application mobile.

### **UC-01 : Saisir la Pré-commande (Fiche Infos Produits J-1)**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Commercial |
| Déclencheur | Fin de journée J, avant retour au dépôt |
| Pré-condition | Versement de J validé par la Secrétaire (RG-01). Exception : première commande de la semaine. |
| Flux principal | 1. Ouvrir la fiche Infos Produits sur mobile. 2. Sélectionner articles par code (catalogue synchronisé). 3. Saisir quantités souhaitées par article. 4. Soumettre → notification Secrétaire + Comptable. 5. En mode offline : stockage local, soumission différée. |
| Post-condition | Fiche en statut SAISIE. Double validation en attente (Secrétaire + Comptable). |
| Règle | RG-01 : blocage si versement J non validé. RG-09 : le Comptable peut réduire les quantités. |

### **UC-02 : Émettre une Facture / Bon de Livraison**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Commercial |
| Déclencheur | Visite d'un client sur le terrain |
| Flux principal | 1. Sélectionner client dans portefeuille. 2. Vérifier soldeDette : si > 0 et non autorisé → blocage (RG-02). 3. Sélectionner articles + quantités → prix appliqués auto selon segment. 4. Calcul automatique : HT + TVA (19,25 % sauf bouillies) + TTC. 5. Choisir typeVente : COMPTANT / CREDIT / AVARIE / DON / DOTATION. 6. Saisir coordonnées GPS (auto). 7. Enregistrer localement. Synchronisation dès connexion. |
| Flux alternatif crédit | Vérification autorisation Comptable. En mode offline : refus automatique (RG-04). |
| Flux alternatif prix hors grille | Champ grisé → envoi requête DG (UC-09). Vente bloquée jusqu'à accord. |
| Post-condition | LigneVente créée. Stock fictif décrémenté. Facture en statut SOUMISE. |

### **UC-03 : Effectuer le Versement de Fin de Journée**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | Commercial (saisie mobile) + Secrétaire (validation web) |
| Déclencheur | Retour au dépôt en fin de journée |
| Flux principal | 1. App affiche montantAttendu (Σ ventes COMPTANT + recouvrements collectés). 2. Secrétaire saisit cashVerse physique reçu. 3. Calcul : écart = cashVerse − montantAttendu. 4. Si écart ≠ 0 → alerte rouge + motif obligatoire (RG-07). 5. Si montant élevé : code OTP SMS requis (RG-13). 6. Secrétaire signe numériquement → lock. 7. Déverrouillage InfoProduit J+1. |
| Types versement | Espèces CFA, Chèques (E.Chèques), Virements bancaires (VST banques) |
| Post-condition | Versement.statut = VALIDE. InfoProduit J+1 déverrouillée. |

### **UC-04 : Gérer les Retours de Fin de Journée**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | Gestionnaire de Stock (Steve) + Commercial |
| Déclencheur | Arrivée du camion au dépôt le soir |
| Flux principal | 1. Steve ouvre session de comptage retours. 2. Pour chaque article : saisie qteRetrouvee (** QUANTITÉ TROUVÉES). 3. Calcul auto : qteVendue = (stockInit + chargement) − qteRetrouvee. 4. Comparaison avec qteFacturee de la feuille de route. 5. Steve signe numériquement → LOCK numérique (RG-05). 6. Stock réel crédité des retours. Stock fictif soldé. |
| Cas avarie | Produits cassés/périmés : MouvementStock.type = AVARIE. Photo obligatoire si > 5% du lot (RG-19). Pas de retour physique, mais décrémentation du stock fictif. |
| Post-condition | InfoProduit.statut = CLOTUREE. StockProduitFini mis à jour. |

### **UC-05 : Saisir la Consommation de Carburant (Double Signature)**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | Commercial (saisie) + Superviseur NTFoods — Chef Prod. ou CG (validation) |
| Déclencheur | Plein de carburant à la station partenaire |
| ⚠ Point critique | Le pompiste N'A PAS de compte dans le système. Il ne signe pas numériquement. |
| Flux principal | 1. Commercial initie fiche carburant sur mobile : immatriculation du véhicule, kilométrage, litres demandés, montant, nom station. 2. Commercial signe numériquement (signatureCommercial = true). 3. Superviseur (Chef Prod. ou CG) reçoit la demande sur le web. 4. Il vérifie la concordance : relevé compteur, litres, montant (validation possible à distance via photo compteur envoyée dans l'app). 5. Superviseur signe numériquement (signatureSuperviseur = true) → LOCK. 6. Dépense imputée au compte carburant NTFoods. |
| Contrôle CG | CG confronte litres/100km vs moyenne historique du véhicule → alerte si écart > seuil. |
| Anti-fraude | Sans double signature validée, la dépense carburant ne peut PAS justifier un écart de versement (RG-08). |
| Post-condition | Logistique.statut = VALIDEE. KPI rendement véhicule mis à jour. |

### **UC-06 : Saisir un Nouveau Prospect (Prospection CRM)**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Commercial (terrain) |
| Déclencheur | Identification d'un nouveau point de vente lors de la tournée |
| Flux principal | 1. Commercial ouvre le module Prospect. 2. Saisit : nom du commerce, coordonnées GPS (auto), type estimé (Grossiste/Détaillant), téléphone contact. 3. Code Info Client = NC (Nouveau Client) dans la Feuille de Route. 4. Prospect enregistré en statut EN_ATTENTE_VALIDATION. 5. Chargée RP valide et le convertit en Client actif. |
| KPI calculé | Taux d'occupation = (Clients livrés / Clients potentiels de la zone) × 100. |
| Post-condition | Prospect créé. Taux d'occupation de la zone recalculé. |

### **UC-07 : Déclarer une Transaction Non Marchande**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Commercial |
| Types concernés | DON (échantillon gratuit), DOTATION (usage interne/promo), AVARIE (cassé/périmé) |
| Flux principal | 1. Commercial sélectionne le type : DON / DOTATION / AVARIE. 2. Saisit l'article et la quantité. 3. Saisit le motif (obligatoire). 4. Si AVARIE > 5% du lot : prise de photo obligatoire (RG-19). 5. MouvementStock créé → décrémentation du stock fictif SANS génération de cash. 6. Synchronisation vers serveur central. |
| Formule impact | StockFictif restant = StockInit − Ventes − Dons − Dotations − Avaries |
| Post-condition | MouvementStock enregistré. Stock fictif mis à jour. Fiche Synthèse DG mise à jour (Mtant AVARIES, Mtant DOT/PROMO). |

### **UC-08 : Consulter le Classement et la Prime Potentielle**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Commercial |
| Déclencheur | Consultation volontaire ou notification hebdomadaire |
| Données affichées | CA réalisé vs CA objectif (montant et %), taux atteinte par gamme, position dans le classement, prime estimée (éligible ✅ ou non ❌), solde terminal préfinancé (si applicable). |
| Post-condition | Affichage en lecture seule. Aucune modification possible. |

### **UC-09 : Demander le Déblocage d****'****un Prix Exceptionnel**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | Commercial (demande) + DG ou Directeur Commercial (validation) |
| Pré-condition | Connexion internet disponible (workflow critique) |
| Flux principal | 1. Commercial tape sur le champ grisé → formulaire de justification. 2. Notification push au DG. 3. DG consulte justification + contexte client. 4. DG signe numériquement l'accord (ou le refus). 5. En cas d'accord : champ déverrouillé temporairement (1 transaction) sur le mobile. 6. LogValidation créé avec ancien et nouveau prix. |
| Post-condition | Prix exceptionnel appliqué. Tracé dans l'Audit Trail. |

### **UC-10 : Recevoir une Alerte Push**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Commercial (réception automatique) |
| Types d'alertes | Stock fictif bas (< seuil restant), objectif hebdo en danger (< 60%), prix débloqué accordé ou refusé, pré-commande validée par le Comptable, écart rouge signalé par la Secrétaire. |
| Post-condition | Notification push affichée. Aucune action obligatoire sauf si liée à un motif d'écart. |

# **5. Cas d****'****Utilisation — Plateforme Web (UC-11 à UC-17)**

### **UC-11 : Valider la Commande J+1 (Comptable)**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Comptable (M. Mbakop) |
| Pré-condition | La Secrétaire a signé le versement J du commercial concerné. |
| Flux principal | 1. Affichage des fiches Infos Produits en statut VALIDEE_SECRETAIRE. 2. Consultation du versement J et du solde du commercial. 3. Vérification des quantités demandées vs capacité de la zone. 4. Réduction possible des quantités si jugées disproportionnées (RG-09). 5. Signature numérique → fiche passe à VALIDEE_COMPTABLE. 6. Notification au Gestionnaire de Stock pour préparer le chargement. |
| Post-condition | Fiche.statut = VALIDEE_COMPTABLE. Steve peut préparer le chargement J+1. |

### **UC-12 : Débloquer un Prix Exceptionnel (DG)**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | DG ou Directeur Commercial |
| Flux principal | 1. DG reçoit notification de demande de déblocage. 2. Consulte la justification du commercial + historique du client. 3. Accord → déblocage temporaire (1 transaction) sur le mobile du commercial. 4. Refus → notification de refus au commercial. 5. LogValidation créé dans l'Audit Trail. |

### **UC-13 : Enregistrer un Recouvrement (Accès Restreint)**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs habilités | Chargée RP & Commerciale + Chef de Production uniquement (RG-12) |
| Flux principal | 1. Sélection du client dans le portefeuille. 2. Affichage historique factures impayées. 3. Saisie montant encaissé + mode paiement. 4. Sélection factures apurées. 5. Génération reçu de caisse numérique. 6. Client.soldeDette mis à jour en temps réel. 7. Si soldeDette = 0 → nouvelle commande débloquée (RG-02). |

### **UC-14 : Consulter le Classement Hebdomadaire**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | DG, Chargée RP & Commerciale |
| Données affichées | Classement par CA, taux atteinte global et par gamme, prime accordée ou non, taux d'occupation par zone, évolution N vs N-1 (tableau comparatif mensuel). |

### **UC-15 : Définir les Objectifs Mensuels**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | Comptable + DG |
| Flux principal | 1. Saisie des cibles CA global par commercial. 2. Décomposition par gamme (BOUILLIE, SNACKING, CHOCO, SEAU…). 3. Validation DG. 4. Synchronisation vers les mobiles des commerciaux concernés. |

### **UC-16 : Analyser le Taux d****'****Occupation par Zone**

| **Champ** | **Détail** |
| --- | --- |
| Acteurs | DG, Contrôleur de Gestion |
| Calcul | Taux = (Clients livrés sur les 3 derniers mois / Clients potentiels recensés) × 100 |
| Données affichées | Carte des zones avec taux colorés (vert > 70%, orange 30-70%, rouge < 30%), prospectus en attente, évolution mensuelle. |

### **UC-17 : Valider une Dépense Carburant (Superviseur)**

| **Champ** | **Détail** |
| --- | --- |
| Acteur | Chef de Production ou Contrôleur de Gestion |
| Flux principal | 1. Réception de la fiche carburant soumise par le commercial. 2. Vérification concordance : kilométrage, litres déclarés, montant vs station partenaire. 3. Photo du compteur possible via l'app. 4. Signature numérique superviseur → LOCK. 5. Dépense imputée. CG génère alerte si L/100km anormal. |

# **6. Entités et Classes Métier**

| **Catégorie** | **Entité** | **Rôle dans le Système** |
| --- | --- | --- |
| **Référentiels** | Utilisateur | Classe mère abstraite : authentification, rôle, jeton JWT, OTP |
|  | Article | Produit TANTY : code, gamme, grammage, grille tarifaire, seuilAlerteStock |
|  | Client | Partenaire commercial : segment, soldeDette, localisation, estProspect |
|  | ZoneDeVente | Secteur géographique : ville, marché, lignes, nbClientsPotentiels |
|  | Vehicule | Immatriculation, suivi carburant, rentabilité par zone |
|  | Terminal | Smartphone préfinancé : IMEI, resteAPayer, effacement MDM |
|  | Fournisseur | Entité externe : achats matières premières |
| **Opérationnel** | InfoProduit | Fiche stock mobile journalière (pré-commande + chargement + retours) |
|  | LigneInfoProduit | Détail article par ligne de fiche (toutes colonnes de la Fiche Synthèse) |
|  | FeuillRoute | Tournée journalière digitalisée (document papier fidèlement reproduit) |
|  | ArretFeuillRoute | Un arrêt client dans la tournée (N°, codes INFOS CLIENTS, CA, recettes) |
|  | Vente | Transaction terrain : facture / bordereau de livraison |
|  | LigneDeVente | Relation N-N Vente ↔ Article (qté, prix appliqué, montant ligne) |
|  | Versement | Clôture financière : Espèces + Chèques + Virements vs attendu |
|  | Recouvrement | Paiement d'une créance passée (accès restreint Chargée RP / Chef Prod) |
|  | MouvementStock | Tout flux physique : CHARGEMENT, RETOUR, AVARIE, DOTATION, DON, DESTOCKAGE |
|  | LogistiqueCarburant | Fiche carburant : double signature Commercial + Superviseur |
|  | TransactionNonMarchande | Don, Dotation, Avarie (impact stock, sans cash) |
|  | Prospect | Nouveau client potentiel en attente de validation par Chargée RP |
|  | FactureAvoir | Régularisation comptable sans suppression de la vente originale |
|  | CommandeDistance | Vente vers villes déportées (agenceTransport, fraisPort, numSuivi) |
| **Pilotage** | Objectif | Cible CA par commercial, période, gamme |
|  | TauxOccupation | Indicateur zone : nbClientsLivrés / nbClientsPotentiels |
|  | SynchronisationLog | Traçabilité des échanges mobile ↔ serveur |
|  | LogValidation | Audit Trail complet : qui, quoi, quand, ancienne/nouvelle valeur |
|  | Reporting | Agrégats KPI : CA, primes, classements, taux d'occupation, N vs N-1 |

# **7. Attributs Détaillés des Classes Principales**

## **7.1 Utilisateur (Classe Mère Abstraite)**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idUtilisateur | Long/UUID | Oui | Clé primaire unique |
| nomComplet | String | Oui | Nom et prénom |
| matricule | String | Oui | Identifiant unique employé |
| login | String | Oui | Identifiant de connexion unique |
| motDePasseHash | String | Oui | Hash Bcrypt |
| role | RoleEnum | Oui | COMMERCIAL, COMPTABLE, SECRETAIRE, STOCK, DG, CHARGEE_RP, CONTROLEUR, CHEF_PROD, PRODUCTION |
| jetonJWT | String | Non | Token actif |
| sessionActive | Boolean | Oui | Session unique : nouvelle connexion = déconnexion automatique de l'ancienne |
| actif | Boolean | Oui | Compte activé/désactivé |

## **7.2 Commercial (Hérite de Utilisateur)**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idVehicule | FK → Vehicule | Non | Véhicule assigné |
| idTerminal | FK → Terminal | Non | Smartphone préfinancé |
| idZoneVente | FK → ZoneDeVente | Non | Zone actuelle d'affectation |
| dernierOTP | String | Non | Code OTP envoyé (haché) |
| dateExpirationOTP | Timestamp | Non | Expiration dans 5 minutes |

## **7.3 Article (Produit TANTY)**

Codes identifiés sur les documents physiques : MGASS(80), MCGAS(24), GGASS(14), MGPL(20), MODGPL(20), TBS(60), RASS(25), PRASS(12), RCP 1L(12), RCP 2L(4), RCP 5L(1), PTASS(12), TASS(25), BBV 1L(12), BBV 2L(4), TCH(12), THBP(1), TC 1L(12), TC 2L(4), TC 5L(1), TC 10L(1). Le chiffre entre parenthèses est le conditionnement par carton/lot.

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| codeProduit | String | Oui | Code alphanumérique unique |
| designation | String | Oui | Nom complet du produit |
| grammage | Integer | Oui | Poids (g) ou volume (mL/L) |
| uniteDeMesure | UnitEnum | Oui | GRAMME, MILLILITRE, LITRE, UNITE |
| gamme | GammeEnum | Oui | BOUILLIE, SNACKING, CHOCO, INGREDIENT, SEAU, CROO, ENRO, CARAM, CHIPS, BEIGN |
| conditionnementCarton | Integer | Non | Nb unités par carton/lot |
| prixDistributeur | BigDecimal | Oui | Prix homologué Niveau 1 |
| prixGrossiste | BigDecimal | Oui | Prix homologué Niveau 2 |
| prixDetaillant | BigDecimal | Oui | Prix homologué Niveau 3 |
| prixPromotion | BigDecimal | Non | Prix promo DLC temporaire avec dateFinPromo |
| dateFinPromo | Date | Non | Date fin de la promotion DLC |
| seuilAlerteStock | Integer | Oui | Seuil déclenchement alerte (à définir par article) |
| tvaApplicable | Boolean | Oui | False = gamme BOUILLIE (exonérée 0%) |
| actif | Boolean | Oui | Disponible à la vente |

## **7.4 Client**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idClient | Long/UUID | Oui | Clé primaire |
| nomClient | String | Oui | Raison sociale ou nom propre |
| segment | SegmentEnum | Oui | DISTRIBUTEUR, GROSSISTE, DETAILLANT |
| telephone | String | Oui* | Obligatoire pour crédit et suivi Call Center |
| localisation | String | Non | Adresse ou quartier/marché |
| coordonneesGPS | String | Rec. | Position GPS de l'établissement |
| soldeDette | BigDecimal | Oui | Encours financier (0 = solvable) |
| idZoneVente | FK | Non | Zone de rattachement |
| estProspect | Boolean | Oui | True si pas encore livré |
| datePremiereCommande | Date | Non | Date de la première livraison |
| statut | StatutClientEnum | Oui | ACTIF, PROSPECT, SUSPENDU, INACTIF |
| codeInfoClient | InfoClientEnum[] | Non | Ab, EV, NC, PA, AV, VPC, AF, NVP, F, M, PR, S |

## **7.5 InfoProduit (Fiche Stock Mobile Journalière)**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idInfoProduit | Long/UUID | Oui | Clé primaire |
| dateSession | Date | Oui | Journée concernée |
| idCommercial | FK | Oui | Commercial responsable |
| signatureSecretaire | Boolean | Oui | Versement J validé (condition 1) |
| signatureComptable | Boolean | Oui | Quantités arbitrées (condition 2) |
| signatureStock | Boolean | Oui | Chargement physique effectué |
| statut | StatutFicheEnum | Oui | SAISIE → VALIDEE_SECRETAIRE → VALIDEE_COMPTABLE → CHARGEE → CLOTUREE |
| lignes | List<LigneInfoProduit> | Oui | Détail complet par article |

### **7.5.1 LigneInfoProduit — Correspondance exacte colonnes Fiche Synthèse**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idArticle | FK → Article | Oui | Code produit |
| qteProduite | Integer | Non | QTE Prod (saisie Production) |
| stockVeille | Integer | Non | STOCK Veille = stockFinal J-1 |
| stockCamion | Integer | Non | STOCK Voitu (stock fictif actuel) |
| stockTotal | Integer | Calculé | STOCK Total = veille + prod + camion |
| qteSorties | Integer | Non | SORTIES (chargement du matin) |
| stockFinal | Integer | Calculé | STOCK Final = total − sorties |
| qteAvoDotProm | Integer | Non | Qte avo. dot. prom déclarées |
| qteFacturee | Integer | Non | QTE FACTUREE (validé par Stock — LOCK) |
| qteLivrees | Integer | Non | * QUANTITÉ LIVRÉES (sortie dépôt) |
| qteRetrouvees | Integer | Non | ** QUANTITÉ TROUVÉES (comptage physique) |
| qteVendue | Integer | Calculé | = (stockInit + qteSorties) − qteRetrouvees |

## **7.6 Vente (Facture / Bordereau de Livraison)**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idVente | UUID | Oui | Généré côté client (anti-doublon offline) |
| dateVente | Timestamp | Oui | Date et heure de l'opération |
| idCommercial | FK | Oui | Émetteur |
| idClient | FK | Oui | Client destinataire |
| idFeuillRoute | FK | Oui | Feuille de route du jour |
| typeVente | TypeVenteEnum | Oui | COMPTANT, CREDIT, DON, AVARIE, DOTATION |
| montantHT | BigDecimal | Calculé | Σ(qté × prixHT) |
| montantTVA | BigDecimal | Calculé | 19,25 % sauf gamme BOUILLIE (0%) |
| montantTTC | BigDecimal | Calculé | HT + TVA |
| coordonneesGPS | String | Rec. | Localisation GPS de la vente |
| signatureClient | Boolean | Cond. | Obligatoire si typeVente = CREDIT |
| statut | StatutVenteEnum | Oui | BROUILLON → SOUMISE → VALIDEE → AVOIR |
| syncStatus | SyncEnum | Oui | EN_ATTENTE → SYNCHRONISEE → CONFLIT |

## **7.7 Versement ****&**** Réconciliation**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idVersement | Long/UUID | Oui | Clé primaire |
| dateVersement | Timestamp | Oui | Date et heure |
| idCommercial | FK | Oui | Commercial concerné |
| montantAttendu | BigDecimal | Calculé | Σ ventes COMPTANT + recouvrements du jour |
| cashVerse | BigDecimal | Oui | Espèces remises physiquement |
| montantCheques | BigDecimal | Non | E.Chèques (voir Fiche Synthèse) |
| montantVirements | BigDecimal | Non | VST banques |
| totalCredit | BigDecimal | Calculé | Σ ventes à crédit de la journée |
| totalRecouvrement | BigDecimal | Calculé | Σ recouvrements collectés |
| depensesJustifiees | BigDecimal | Cond. | Dépenses avec pièce justificative |
| depensesNonJust | BigDecimal | Cond. | Dépenses sans justificatif → alerte DG |
| ecart | BigDecimal | Calculé | cashVerse − montantAttendu |
| motifEcart | String | Cond. | Obligatoire si ecart ≠ 0 |
| signatureSecretaire | Boolean | Oui | Validation clôture → LOCK |
| alerteRouge | Boolean | Auto | True si cashVerse < montantAttendu |
| otpValide | Boolean | Cond. | Code OTP SMS validé (si montant élevé) |

## **7.8 LogistiqueCarburant (sans interaction pompiste)**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idLogistique | Long | Oui | Clé primaire |
| date | Date | Oui | Date du plein |
| idVehicule | FK → Vehicule | Oui | Véhicule concerné |
| idCommercial | FK | Oui | Commercial qui saisit |
| idSuperviseur | FK | Oui | Chef Prod. ou CG qui valide (PAS le pompiste) |
| stationPartenaire | String | Oui | Nom de la station-service |
| kilometrage | Integer | Oui | Relevé compteur kilométrique |
| litresCarburant | Double | Oui | Volume en litres |
| montant | BigDecimal | Oui | Montant en CFA |
| signatureCommercial | Boolean | Oui | 1ère signature numérique (commercial) |
| signatureSuperviseur | Boolean | Oui | 2ème signature numérique (superviseur NTFoods) |
| photoCompteur | String | Non | URL photo du compteur (validation à distance) |
| statut | StatutLogEnum | Oui | EN_ATTENTE → VALIDEE → LITIGIEUSE |

## **7.9 Terminal (Smartphone Préfinancé)**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idTerminal | Long | Oui | Clé primaire |
| imei | String(15) | Oui | Numéro IMEI international unique |
| modele | String | Oui | Modèle smartphone (Samsung, Xiaomi…) |
| idCommercial | FK | Oui | Commercial actuellement assigné |
| prixAchat | BigDecimal | Oui | Coût total de l'appareil |
| resteAPayer | BigDecimal | Oui | Solde restant dû (déduit des primes hebdo) |
| actif | Boolean | Oui | Terminal actif ou désactivé |
| dateDerniereConnexion | Timestamp | Non | Dernière connexion réussie |

## **7.10 FeuillRoute et ArretFeuillRoute**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| FeuillRoute.idFeuillRoute | Long/UUID | Oui | Clé primaire |
| FeuillRoute.dateRoute | Date | Oui | Date de la tournée |
| FeuillRoute.idCommercial | FK | Oui | Commercial |
| FeuillRoute.idZoneVente | FK | Oui | Zone couverte |
| FeuillRoute.statut | StatutFREnum | Oui | EN_COURS → SOUMISE → VALIDEE_CG → VALIDEE_COMPT |
| FeuillRoute.signatureCommercial | Boolean | Oui | Clôture de tournée |
| FeuillRoute.signatureCG | Boolean | Oui | Contrôleur des Ventes |
| FeuillRoute.signatureRespComm | Boolean | Oui | Responsable Commercial |
| FeuillRoute.signatureComptable | Boolean | Oui | Validation comptable finale |
| ArretFeuillRoute.numOrdre | Integer | Oui | Numéro de l'arrêt (N° sur la feuille) |
| ArretFeuillRoute.idClient | FK | Oui | Client visité |
| ArretFeuillRoute.codesInfoClient | InfoClientEnum[] | Non | Ab, EV, NC, PA, AV… |
| ArretFeuillRoute.ca | BigDecimal | Calculé | CA de cet arrêt |
| ArretFeuillRoute.recettes | BigDecimal | Non | Cash encaissé sur place |

# **8. Diagramme de Classes UML — Code PlantUML Complet**

Code PlantUML complet v4.0. À coller dans https://plantuml.com ou StarUML.

| @startuml NTFoods_Commercial_UltraComplet_V4 skinparam classBackgroundColor #1E3A5F skinparam classFontColor #FFFFFF skinparam classHeaderBackgroundColor #1A5276 skinparam defaultFontName Arial  ' ========================================== '  RÉFÉRENTIELS — UTILISATEURS ' ========================================== abstract class Utilisateur {   +idUtilisateur : Long   +nomComplet : String   +matricule : String   +login : String   +motDePasseHash : String   +role : RoleEnum   +sessionActive : Boolean   +actif : Boolean   --   +seConnecter() : Token   +seDeconnecter() : void }  class Commercial extends Utilisateur {   +idVehicule : FK   +idTerminal : FK   +idZoneVente : FK   +dernierOTP : String   +dateExpirationOTP : Timestamp   --   +saisirPrecommande() : InfoProduit   +emettreFacture() : Vente   +justifierEcart() : Versement   +saisirCarburant() : LogistiqueCarburant   +ajouterProspect() : Prospect   +declarerNonMarchand() : MouvementStock }  class Comptable extends Utilisateur {   --   +validerPrecommande(fiche: InfoProduit) : void   +arbitrerQuantites() : void   +autoriserCredit(vente: Vente) : void   +emettreFactureAvoir() : FactureAvoir   +definirObjectifs() : Objectif }  class Secretaire extends Utilisateur {   --   +validerVersement(v: Versement) : void   +signerCommandeJ1(fiche: InfoProduit) : void   +alerterEcartRouge() : void }  class GestionnaireStock extends Utilisateur {   --   +validerChargement(fiche: InfoProduit) : void   +validerRetours(fiche: InfoProduit) : void   +compterPhysique() : void }  class DirecteurGeneral extends Utilisateur {   --   +debloquerPrix(vente: Vente, prix: BigDecimal) : void   +validerDecaissement() : void   +definirObjectifs() : Objectif }  class ChargeeRP extends Utilisateur {   --   +enregistrerRecouvrement() : Recouvrement   +validerProspect(p: Prospect) : Client   +calculerPrimes() : Map   +genererClassement() : List   +gererPromotionsDLC() : void }  class ChefProduction extends Utilisateur {   ' Rôle superviseur carburant — PAS le pompiste   --   +validerCarburant(l: LogistiqueCarburant) : void   +enregistrerRecouvrement() : Recouvrement }  class ControleurGestion extends Utilisateur {   --   +auditerDepenses() : void   +calculerRendementVehicule(v: Vehicule) : Double   +validerCarburant(l: LogistiqueCarburant) : void }  ' ========================================== '  RÉFÉRENTIELS MÉTIER ' ========================================== class Article {   +codeProduit : String   +designation : String   +grammage : Integer   +gamme : GammeEnum   +prixDistributeur : BigDecimal   +prixGrossiste : BigDecimal   +prixDetaillant : BigDecimal   +prixPromotion : BigDecimal   +dateFinPromo : Date   +seuilAlerteStock : Integer   +tvaApplicable : Boolean   +conditionnementCarton : Integer   +actif : Boolean   --   +getPrixHomologue(segment: SegmentEnum) : BigDecimal   +convertirKgEnUnites(kg: Double) : Integer }  class Client {   +idClient : Long   +nomClient : String   +segment : SegmentEnum   +telephone : String   +localisation : String   +coordonneesGPS : String   +soldeDette : BigDecimal   +idZoneVente : FK   +estProspect : Boolean   +datePremiereCommande : Date   +statut : StatutClientEnum   +codeInfoClient : InfoClientEnum[]   --   +estSolvable() : Boolean }  class Prospect {   +idProspect : Long   +nom : String   +telephone : String   +coordonneesGPS : String   +localisation : String   +typeEstime : SegmentEnum   +idCommercial : FK   +dateCreation : Date   +statut : StatutProspectEnum   ' NOUVEAU → EN_ATTENTE_VALIDATION → CONVERTI }  class ZoneDeVente {   +idZone : Long   +nomZone : String   +ville : String   +nbClientsPotentiels : Integer   --   +calculerTauxOccupation() : Double   ' Taux = clientsLivres3mois / clientsPotentiels * 100 }  class Vehicule {   +idVehicule : Long   +immatriculation : String   +marque : String   +actif : Boolean }  class Terminal {   +idTerminal : Long   +imei : String   +modele : String   +idCommercial : FK   +prixAchat : BigDecimal   +resteAPayer : BigDecimal   +actif : Boolean   +dateDerniereConnexion : Timestamp }  ' ========================================== '  ENTITÉS OPÉRATIONNELLES ' ========================================== class InfoProduit {   +idInfoProduit : Long   +dateSession : Date   +idCommercial : FK   +signatureSecretaire : Boolean   +signatureComptable : Boolean   +signatureStock : Boolean   +statut : StatutFicheEnum   --   +calculerQuantiteVendue() : Integer   +validerRetourPhysique() : void }  class LigneInfoProduit {   +idLigne : Long   +idArticle : FK   +qteProduite : Integer   +stockVeille : Integer   +stockCamion : Integer   +stockTotal : Integer  {derive}   +qteSorties : Integer   +stockFinal : Integer  {derive}   +qteAvoDotProm : Integer   +qteFacturee : Integer   +qteLivrees : Integer   +qteRetrouvees : Integer   +qteVendue : Integer  {derive} }  class FeuillRoute {   +idFeuillRoute : Long   +dateRoute : Date   +idCommercial : FK   +idZoneVente : FK   +statut : StatutFREnum   +signatureCommercial : Boolean   +signatureCG : Boolean   +signatureRespComm : Boolean   +signatureComptable : Boolean   +recettesTotal : BigDecimal  {derive}   --   +genererRecapTotalLivraison() : Map }  class ArretFeuillRoute {   +idArret : Long   +numOrdre : Integer   +idClient : FK   +codesInfoClient : InfoClientEnum[]   +ca : BigDecimal  {derive}   +recettes : BigDecimal   +soldeDette : BigDecimal   +recouvrement : BigDecimal   +observations : String }  class Vente {   +idVente : UUID   +dateVente : Timestamp   +idCommercial : FK   +idClient : FK   +idFeuillRoute : FK   +typeVente : TypeVenteEnum   +montantHT : BigDecimal  {derive}   +montantTVA : BigDecimal  {derive}   +montantTTC : BigDecimal  {derive}   +coordonneesGPS : String   +signatureClient : Boolean   +statut : StatutVenteEnum   +syncStatus : SyncEnum   --   +calculerMontantTTC() : BigDecimal   +demanderDeblocagePrix() : void }  class LigneDeVente {   +idLigne : Long   +idVente : FK   +idArticle : FK   +quantite : Integer   +prixUnitaireApplique : BigDecimal   +montantLigne : BigDecimal  {derive} }  class Versement {   +idVersement : Long   +dateVersement : Timestamp   +idCommercial : FK   +montantAttendu : BigDecimal  {derive}   +cashVerse : BigDecimal   +montantCheques : BigDecimal   +montantVirements : BigDecimal   +totalCredit : BigDecimal  {derive}   +totalRecouvrement : BigDecimal  {derive}   +depensesJustifiees : BigDecimal   +depensesNonJust : BigDecimal   +ecart : BigDecimal  {derive}   +motifEcart : String   +signatureSecretaire : Boolean   +alerteRouge : Boolean   +otpValide : Boolean   --   +comparerVenteCash() : void   +signalerEcartRouge() : void }  class MouvementStock {   +idMouvement : Long   +date : Timestamp   +idArticle : FK   +idCommercial : FK   +quantite : Integer   +typesMvt : TypeMvtEnum   ' CHARGEMENT, RETOUR, AVARIE, DOTATION, DON, DESTOCKAGE   +montantImpact : BigDecimal   +motif : String   +photoJustif : String }  class Recouvrement {   +idRecouvrement : Long   +dateRecouvrement : Timestamp   +idClient : FK   +idActeur : FK   +montantEncaisse : BigDecimal   +facturesApurees : List   +modeEncaissement : ModeEncEnum }  class LogistiqueCarburant {   ' !! Pas de compte Pompiste dans le système !!   +idLogistique : Long   +date : Date   +idVehicule : FK   +idCommercial : FK   +idSuperviseur : FK   +stationPartenaire : String   +kilometrage : Integer   +litresCarburant : Double   +montant : BigDecimal   +signatureCommercial : Boolean   +signatureSuperviseur : Boolean   +photoCompteur : String   +statut : StatutLogEnum }  class FactureAvoir {   +idAvoir : Long   +dateAvoir : Timestamp   +idVenteOriginale : FK   +idComptable : FK   +motif : String   +montantAvoir : BigDecimal }  class CommandeDistance {   +idCommande : Long   +idClient : FK   +idComptable : FK   +agenceTransport : String   +fraisPort : BigDecimal   +dateExpedition : Date   +numSuivi : String   +statut : StatutCmdDistEnum }  ' ========================================== '  PILOTAGE & REPORTING ' ========================================== class Objectif {   +idObjectif : Long   +idCommercial : FK   +periode : PeriodeEnum   +cibleCA : BigDecimal   +cibleParGamme : Map   --   +calculerTauxAtteinte() : Double   +calculerPrimeHebdo() : BigDecimal   ' Prime = 12000 CFA si >=80% global ET >=75%/gamme', }  class TauxOccupation {   +idTaux : Long   +idZone : FK   +dateCalcul : Date   +nbClientsPotentiels : Integer   +nbClientsLivres : Integer   +taux : Double  {derive}   ' taux = nbLivres / nbPotentiels * 100 }  class LogValidation {   +idLog : Long   +horodatage : Timestamp   +idActeur : FK   +typeAction : String   +entiteConcernee : String   +ancienneValeur : String   +nouvelleValeur : String   +statut : LogStatutEnum   ' SUCCES / ECHEC / CONFLIT / TENTATIVE_FRAUDE }  class SynchronisationLog {   +idSync : Long   +deviceId : String   +dateSync : Timestamp   +statut : SyncStatutEnum   +nbTransactions : Integer   +erreurs : String }  ' ========================================== '  RELATIONS PRINCIPALES ' ========================================== Commercial '1' --> '0..*' Vente Commercial '1' --> '0..*' InfoProduit Commercial '1' --> '0..*' FeuillRoute Commercial '1' --> '0..1' ZoneDeVente Commercial '1' --> '0..1' Vehicule Commercial '1' --> '0..1' Terminal Commercial '1' --> '0..*' Prospect Commercial '1' --> '0..*' LogistiqueCarburant Client '1' --> '0..*' Vente Client '1' --> '0..*' Recouvrement Client '0..*' --> '1' ZoneDeVente ZoneDeVente '1' --> '0..*' TauxOccupation Vente '1' *-- '1..*' LigneDeVente : composition LigneDeVente '*..*' --> '1' Article InfoProduit '1' *-- '1..*' LigneInfoProduit : composition LigneInfoProduit '*..*' --> '1' Article FeuillRoute '1' *-- '1..*' ArretFeuillRoute : composition ArretFeuillRoute '0..*' --> '1' Client FeuillRoute '1' --> '0..*' Vente LogistiqueCarburant '1' --> '1' Vehicule LogistiqueCarburant '1' --> '1' Utilisateur : idSuperviseur Vente '1' --> '0..1' FactureAvoir Objectif '1' --> '1' Commercial MouvementStock '1' --> '1' Article MouvementStock '1' --> '1' Commercial  @enduml |
| --- |

# **9. Relations et Cardinalités UML**

| **Classe A** | **Cardinalité** | **Classe B** | **Type** | **Justification Métier** |
| --- | --- | --- | --- | --- |
| Commercial | 1 → 0..* | Vente | Association | Un commercial émet plusieurs ventes par jour |
| Commercial | 1 → 0..* | InfoProduit | Association | Une fiche par journée d'activité |
| Commercial | 1 → 0..* | FeuillRoute | Association | Une feuille de route par tournée |
| Commercial | 1 → 0..1 | ZoneDeVente | Association | Affectation à un secteur géographique |
| Commercial | 1 → 0..1 | Terminal | Association | Un smartphone préfinancé par commercial |
| Commercial | 1 → 0..* | LogistiqueCarburant | Association | Saisie des pleins carburant |
| Commercial | 1 → 0..* | Prospect | Association | Identification nouveaux clients terrain |
| Client | 1 → 0..* | Vente | Association | Historique de transactions client |
| Client | 1 → 0..* | Recouvrement | Association | Plusieurs paiements de dettes possibles |
| Client | 0..* → 1 | ZoneDeVente | Association | Appartenance à une zone de chalandise |
| ZoneDeVente | 1 → 0..* | TauxOccupation | Association | Historique des taux par zone |
| Vente | 1 → 1..* | LigneDeVente | Composition | La facture est décomposée en lignes produits |
| LigneDeVente | * ↔ * | Article | Association (via LigneDeVente) | Relation N-N décomposée |
| InfoProduit | 1 → 1..* | LigneInfoProduit | Composition | Toutes colonnes de la Fiche Synthèse |
| FeuillRoute | 1 → 1..* | ArretFeuillRoute | Composition | Chaque arrêt dans la tournée |
| ArretFeuillRoute | 0..* → 1 | Client | Association | Chaque arrêt concerne un client |
| FeuillRoute | 1 → 0..* | Vente | Association | Les ventes se rattachent à la tournée |
| LogistiqueCarburant | 1 → 1 | Vehicule | Association | Suivi par véhicule |
| LogistiqueCarburant | 1 → 1 | Utilisateur | Association | Superviseur (pas pompiste) |
| Vente | 1 → 0..1 | FactureAvoir | Association | Régularisation sans suppression |
| Objectif | 1 → 1 | Commercial | Association | Un objectif par commercial par période |

# **10. Gestion des Zones Commerciales et Taux d****'****Occupation**

## **10.1 Définition et Structure**

Une zone de vente représente un secteur géographique assigné à un ou plusieurs commerciaux. Les zones incluent les marchés (ex : Mokolo, Mfoundi, Nlongkak), les supermarchés, les lignes (axes commerciaux), les villes proches (Mbalmayo, Sangmélima) et les villes déportées (Bertoua, Bafoussam, Garoua).

| **Aspect** | **Règle / Description** |
| --- | --- |
| Attribution | Par la Chargée RP et le Comptable. Zone stable sur 3 à 4 mois. Redistribution mensuelle possible. |
| Portabilité stats | Lors d'un changement de zone : statistiques personnelles (CA, primes) suivent le commercial. Historique de la zone (ex: Mokolo) reste attaché au secteur pour analyse marché. |
| Taux d'occupation | = (Clients livrés sur les 3 derniers mois / Clients potentiels recensés) × 100. Ex : Mokolo a 100 boutiques, commercial en livre 10 → taux = 10 %. |
| Prospection | Commercial enregistre chaque nouveau point de vente (UC-06). Le système évalue la marge de manœuvre restante. La Chargée RP ajuste annuellement le nombre de clients potentiels (calibrage). |
| Contrôle présence | Validation de présence en zone dès 06h30 après badgeage bio-métreur. Capture GPS lors des ventes pour vérification géographique (anti-fraude F-09). |
| Villes déportées | Processus spécifique : cycle mensuel, commande via WhatsApp, facturation Comptable, expédition agence de voyage. Voir section 20. |

## **10.2 KPI par Zone**

| **KPI** | **Formule** | **Fréquence** |
| --- | --- | --- |
| CA par zone | Σ montantTTC des ventes de la zone sur la période | Quotidien / Mensuel |
| Taux d'occupation | (Clients livrés 3 mois / Clients potentiels) × 100 | Hebdomadaire |
| Évolution N vs N-1 | (CA mois actuel / CA même mois N-1 − 1) × 100 | Mensuel |
| Rendement véhicule | CA généré dans la zone / Litres carburant consommés | Hebdomadaire |
| Nb nouveaux prospects | Count(Prospect où statut=EN_ATTENTE et zone=X) | Hebdomadaire |
| Recouvrement zone | Σ Recouvrement.montantEncaisse par zone | Hebdomadaire |
| Taux de couverture gammes | % des gammes vendues vs objectif par gamme | Hebdomadaire |

# **11. Double Signature Carburant — Sans Interaction Pompiste**

| **Architecture Critique Corrigée v4.0** Le pompiste N'A PAS de compte dans le système TANTY Digital. Il n'interagit PAS avec l'application mobile ni avec la plateforme web. La responsabilité de la vérité terrain incombe entièrement au Superviseur NTFoods. Superviseur = Chef de Production OU Contrôleur de Gestion (deux rôles habilités). |
| --- |

## **11.1 Workflow Complet de Validation**

| **Étape** | **Acteur** | **Action** | **Système** |
| --- | --- | --- | --- |
| 1 | Commercial | Initie fiche carburant sur mobile : immatriculation, kilométrage, litres demandés, montant, nom station | LogistiqueCarburant créée en statut EN_ATTENTE |
| 2 | Commercial | Signe numériquement (signatureCommercial = true). Peut joindre une photo du compteur. | 1ère signature enregistrée |
| 3 | Superviseur (Chef Prod / CG) | Reçoit la demande sur la plateforme web. Vérifie concordance : relevé km, litres, montant. | Comparaison avec historique du véhicule |
| 4 | Superviseur | Valide à distance (photo du compteur si nécessaire). Signe numériquement (signatureSuperviseur = true). | LOCK. Statut = VALIDEE |
| 5 | Système | Impute la dépense au compte carburant NTFoods. Met à jour KPI rendement du véhicule. | Alerte si L/100km anormal vs historique |

## **11.2 Contrôles Anti-Fraude Carburant**

- Sans double signature validée, la dépense carburant ne peut PAS justifier un écart de versement (RG-08).

- Le Contrôleur de Gestion confronte les litres consommés vs le CA généré dans la zone du véhicule.

- Alerte automatique si la consommation dépasse la moyenne historique du véhicule (seuil en L/100km).

- Toute anomalie est tracée en LogValidation.statut = LITIGIEUSE pour enquête.

- Validation possible à distance via photo du compteur transmise dans l'application mobile.

# **12. Transactions Non Marchandes — Dons, Dotations, Avaries**

Ces trois types de transactions sont fondamentaux car ils impactent le stock fictif SANS générer de cash. Ils doivent être traités de manière distincte dans le système pour assurer l'équilibre de la formule de réconciliation.

| **Type** | **Définition** | **Traitement Système** | **Règles** |
| --- | --- | --- | --- |
| **DON** | Produit offert gratuitement (échantillon, cadeau commercial) | Stock fictif décrémenté. Aucun cash généré. Motif obligatoire. MouvementStock.type = DON | Validation Comptable si quantité > seuil défini |
| **DOTATION** | Produit utilisé en promotion interne ou distribué à l'équipe | Stock fictif décrémenté. Traçabilité interne. Justification obligatoire. MouvementStock.type = DOTATION | Apparaît dans Mtant DOT/PROMO de la Fiche Synthèse DG |
| **AVARIE** | Produit cassé, périmé, détérioré, impropre à la vente | Stock fictif décrémenté. Pas de retour physique. Photo obligatoire si > 5% du lot (RG-19). MouvementStock.type = AVARIE | Apparaît dans Mtant AVARIES de la Fiche Synthèse DG |

| **Formule de Réconciliation Quotidienne Complète** StockInitial (chargement matin)   − Ventes (comptant + crédit + avaries/dons impactant stock)   − Dons déclarés   − Dotations déclarées   − Avaries déclarées   = Retour Physique (comptage soir par Steve) Si résultat ≠ qteRetrouvees → écart → alerte Contrôleur de Gestion → enquête. |
| --- |

## **12.1 Cycle de Vie d****'****un Prospect → Client**

| **Statut** | **Description** | **Déclencheur** |
| --- | --- | --- |
| NOUVEAU (estProspect=true) | Point de vente identifié par le commercial sur le terrain | UC-06 : saisie sur l'app mobile |
| EN_ATTENTE_VALIDATION | Soumis à la Chargée RP pour vérification | Synchronisation vers serveur central |
| CONVERTI → Client.actif=true | Prospect validé, compte client créé | Chargée RP valide dans UC-13 |
| Client dans INFOS CLIENTS | Code NC (Nouveau Client) sur la Feuille de Route | Lors de la première visite/livraison |

# **13. Lock Numérique et Immuabilité des Données**

Le Lock Numérique est l'âme numérique du projet TANTY Digital. Il garantit que les données validées deviennent strictement non modifiables, prévenant les fraudes estimées à 90 % dans l'ancien système manuel.

| **Entité** | **Déclencheur du LOCK** | **Second Acteur Validant** |
| --- | --- | --- |
| InfoProduit (commande J+1) | signatureSecretaire = true ET signatureComptable = true | Secrétaire + Comptable |
| LigneInfoProduit (retours soir) | signatureStock = true (Steve valide le comptage physique) | Gestionnaire de Stock |
| Versement | signatureSecretaire = true (validation clôture journalière) | Secrétaire |
| LogistiqueCarburant | signatureSuperviseur = true | Chef Production ou Contrôleur |
| Vente (facture) | statut = VALIDEE (comptable valide) | Comptable |
| Objectif | DG valide numériquement les cibles | Directeur Général |
| FeuillRoute | signatureComptable = true (4ème signature) | Comptable |

## **13.1 Protocole de Modification sur Données Verrouillées**

Une modification post-lock est une exception grave nécessitant :

- Multi-autorisation : codes de 2 ou 3 personnes stratégiques simultanément (ex : DG + Comptable + CG).

- Code OTP SMS : validation supplémentaire par code à usage unique (RG-13).

- Motif de modification : justification obligatoire enregistrée dans LogValidation.

- Tracé complet : ancienne valeur, nouvelle valeur, auteur, horodatage, statut (SUCCES/ECHEC/TENTATIVE_FRAUDE).

- Pour les factures : pas de suppression — émission d'une FactureAvoir par le Comptable, puis nouvelle facture correcte. La suite numérique des documents reste intacte pour l'audit DSF.

## **13.2 Sécurité Anti-Forçage**

- Après 3 tentatives de codes erronés : blocage temporaire 30 minutes de l'accès à la fonctionnalité.

- Alerte automatique envoyée à l'administrateur système.

- L'utilisateur doit appeler l'administrateur pour justifier son action et demander un déblocage manuel.

- Un nombre élevé de demandes de corrections sur un compte = indicateur de risque de fraude → rapport DG.

## **13.3 Données Non Modifiables vs Données Évolutives**

| **Donnée** | **Statut** | **Raison** |
| --- | --- | --- |
| Facture validée (Vente.statut=VALIDEE) | IMMUABLE | Intégrité comptable et fiscale (DSF) |
| Retours soir validés (signatureStock=true) | IMMUABLE | Base du calcul de la prime et du versement |
| Versement validé (signatureSecretaire=true) | IMMUABLE | Clôture financière du jour |
| Prix homologués (Article.prixXxx) | MODIFIABLE par DG uniquement | Tarification segment marché |
| Objectif mensuel | MODIFIABLE avant début de période | Fixé par DG/Comptable |
| Solde dette client | CALCULÉ automatiquement | Évolue à chaque transaction |
| Taux d'occupation | CALCULÉ automatiquement (hebdo) | Évolue avec les livraisons |

# **14. Authentification Forte (OTP) et Sécurité Mobile**

## **14.1 Scénarios Nécessitant un Code OTP**

| **Opération Sensible** | **Contexte de Déclenchement** | **Acteur concerné** |
| --- | --- | --- |
| Versement important | Montant cashVerse > seuil paramétrable (ex : 100 000 CFA) | Secrétaire + Commercial |
| Déblocage prix exceptionnel | Accord DG transmis sur mobile du commercial | DG + Commercial |
| Annulation / correction facture validée | Workflow exception post-lock | DG + Comptable + CG |
| Modification données verrouillées | Multi-autorisation requise | 2-3 administrateurs |
| Connexion nouvel appareil | IMEI non reconnu ou changement de terminal | Commercial + Admin |

## **14.2 Paramètres Techniques OTP**

| **Paramètre** | **Valeur** | **Note** |
| --- | --- | --- |
| Canal | SMS Gateway | Envoyé sur le numéro de téléphone enregistré de l'employé |
| Validité | 5 minutes | Code expiré = nouvelle demande obligatoire |
| Tentatives max | 3 échecs | Blocage automatique 30 minutes après le 3ème échec |
| Déblocage après blocage | Manuel par administrateur | Appel téléphonique + justification obligatoire |
| Coût unitaire estimé | ~35 CFA/SMS | À facturer dans le budget infrastructure |
| Stockage OTP | Hash stocké dans Commercial.dernierOTP | Jamais en clair en base de données |

## **14.3 Mesures de Sécurité Mobile Renforcées**

| **Mesure** | **Mécanisme Technique** | **Objectif** |
| --- | --- | --- |
| Session unique | Nouvelle connexion = déconnexion automatique de l'ancienne session active (serveur révoque le JWT précédent) | Empêcher l'usurpation de compte |
| Anti-capture d'écran | Flag Android/iOS bloquant techniquement les screenshots dans l'application | Protéger les données commerciales sensibles |
| Contrôle IMEI | Vérification au démarrage : IMEI du terminal doit correspondre à la table Terminal pour ce commercial | Empêcher l'utilisation sur un appareil non autorisé |
| Effacement MDM à distance | Commande remote wipe via Mobile Device Management en cas de perte ou vol | Protéger les données stockées localement |
| Chiffrement local | Base SQLite locale chiffrée AES-256 | Données inaccessibles si l'appareil est compromis |
| Biométrie optionnelle | Empreinte digitale ou FaceID pour déverrouillage rapide (complément du mot de passe) | Confort + sécurité renforcée |
| UUID anti-doublon | UUID généré côté client pour chaque vente. Serveur rejette tout doublon UUID + timestamp. | Empêcher double saisie offline/online |
| Capture GPS | Localisation automatique lors de chaque vente. Vérification que le commercial est bien dans sa zone. | Contrôle géographique anti-fraude |

# **15. Interactions avec les Modules Stock et Production**

## **15.1 Interactions avec le Module Stock**

| **Déclencheur (Commerce)** | **Dir.** | **Action côté Stock** | **Données échangées** |
| --- | --- | --- | --- |
| Chargement matinal | → | Décrémentation stock réel produits finis | ArticleId, quantite, date, idCommercial |
| Retour invendus soir (Steve) | → | Incrémentation stock réel (retours) | ArticleId, qteRetour, idCommercial, date |
| Déclaration AVARIE | → | Mouvement AVARIE (pas de retour physique) | ArticleId, quantite, motif, photoJustif |
| Déclaration DON | → | Mouvement DON | ArticleId, quantite, motif |
| Déclaration DOTATION | → | Mouvement DOTATION | ArticleId, quantite, motif |
| Consultation dispo avant chargement | ← | Lecture stock disponible par article | List<ArticleId, qteDisponible> |
| Alerte rupture stock | ← Stock → Commerce | Notification si stock < seuilAlerteStock | ArticleId, stockActuel, seuilAlerte |
| Validation production (Steve) | ← Prod → Stock → Commerce | Mise à jour stock dispo visible commerciaux | ArticleId, qteAjoutee |
| Signalement EV* (emballage vide) | → | Mouvement consommable EMBALLAGE | CodeConsommable, quantite |
| Destockage | → | Mouvement DESTOCKAGE | ArticleId, quantite, motif, montant |

## **15.2 Interactions avec le Module Production**

| **Déclencheur** | **Direction** | **Description** |
| --- | --- | --- |
| Agrégation pré-commandes J+1 | Commerce → Production | Le Comptable consolide les besoins pour anticiper les ordres de production. Alerte si stock insuffisant. |
| Stock produits finis < seuil | Stock → Production | Déclenchement automatique d'un besoin de production ou d'approvisionnement en matières premières. |
| Production validée (Steve) | Prod → Stock → Commerce | Mise à jour stock disponible, visible immédiatement lors de la pré-commande suivante. |
| Rapport ventes par gamme | Commerce → Production | Optimisation des quantités produites selon la demande réelle terrain. |
| Lots en date limite de consommation | Production → Commerce | Alerte pour promotions DLC décidées par la Chargée RP (prixPromotion activé). |
| Datage automatisé | Production → Stock | Lot produit 1-15 du mois → date=1er. Après le 15 → date=15. |
| Conversion métrologique | Prod → Stock/Commerce | Système convertit kg en unités vendables (ex: 55 kg / 42 g = 1 309 sachets). |

# **16. Synchronisation Mobile ↔ Système Central**

## **16.1 Stratégie Offline-First**

L'application mobile fonctionne intégralement sans connexion internet. Toutes les saisies sont stockées localement (SQLite chiffré AES-256), puis synchronisées vers le serveur central via une file FIFO dès que la connexion est rétablie.

| **Donnée** | **Mode Sync** | **Règle de Conflit** |
| --- | --- | --- |
| Ventes (factures) | Push queue FIFO dès connexion | UUID client prime si non encore validée centralement |
| Stock fictif (InfoProduit) | Push obligatoire fin de journée | Blocage pré-commande J+1 si sync non effectuée |
| Catalogue articles + prix | Pull au démarrage de l'app | Serveur fait autorité (prix bloqués par DG) |
| Portefeuille clients + soldes | Pull démarrage + pull ciblé | Serveur fait autorité (soldeDette temps réel) |
| Autorisations crédit | Pull temps réel si connexion | Sans connexion : refus automatique crédit (RG-04) |
| Déblocage prix DG | Push/Pull temps réel | Connexion internet obligatoire (workflow critique) |
| Versement fin de journée | Push immédiat avec Secrétaire | Synchronisation obligatoire avant clôture |
| Feuille de route digitale | Push fin de tournée | Serveur confronte avec InfoProduit du jour |
| Prospects | Push dès connexion disponible | Statut EN_ATTENTE jusqu'à validation Chargée RP |
| Carburant | Push + connexion requise | Superviseur doit valider en ligne (workflow critique) |
| OTP | Pull temps réel | Code unique, expiration 5 min, connexion obligatoire |

## **16.2 Règles de Résolution de Conflits**

- Règle 1 — Signature prime : toute donnée portant une signature numérique d'un second acteur est authoritative et ne peut être écrasée par une version non signée.

- Règle 2 — Horodatage serveur : conflit non résolu par règle 1 → version la plus récente selon horodatage serveur UTC retenue.

- Règle 3 — Log de conflit : tout conflit enregistré dans LogValidation avec statut CONFLIT pour audit manuel.

- Règle 4 — File FIFO : données en attente stockées en queue locale ordonnée. Notification utilisateur si items non synchronisés > 4h.

- Règle 5 — Chiffrement local : données SQLite chiffrées AES-256. Effacement MDM à distance en cas de perte ou vol.

- Règle 6 — Vente offline post-rupture : si le serveur rejette une vente (stock insuffisant) → notification commercial → annulation locale obligatoire + log CONFLIT.

# **17. Contraintes Métier et Règles de Gestion (RG-01 à RG-20)**

| **#** | **Règle** | **Contrainte Logique** | **Acteur / Mécanisme** |
| --- | --- | --- | --- |
| **RG-01** | Solvabilité commercial | Commande J+1 non servie si versement J non validé par la Secrétaire. Exception : 1ère commande de la semaine. | Secrétaire |
| **RG-02** | Non-doublement client | Interdiction de livrer si une facture précédente est impayée. Dérogation Comptable uniquement. | Comptable |
| **RG-03** | Prix bloqués | Champs de prix grisés sur mobile. Seul le DG débloque temporairement (1 transaction). | DG |
| **RG-04** | Vente crédit offline | En mode offline, toute vente à crédit est automatiquement refusée. | App mobile |
| **RG-05** | Immuabilité (Lock) | Donnée validée par second acteur = non modifiable. Correction via FactureAvoir ou workflow exception multi-autorisation. | Audit Trail |
| **RG-06** | Stock positif obligatoire | Blocage si vente > stock fictif disponible. Alerte si stock réel < seuilAlerteStock. | Local + serveur |
| **RG-07** | Motif écart obligatoire | Si cashVerse ≠ montantAttendu → motif de dépense obligatoire avant clôture. | Commercial |
| **RG-08** | Double signature carburant | Validation commercial + superviseur NTFoods obligatoire. Sans double signature = dépense non justifiée. | Chef Prod / CG |
| **RG-09** | Arbitrage quantités J+1 | Le Comptable peut réduire les quantités demandées si jugées disproportionnées vs capacité de la zone. | Comptable |
| **RG-10** | Prime 12 000 CFA | Prime si taux ≥ 80 % global ET ≥ 75 % pour CHAQUE gamme assignée. Les deux conditions simultanées obligatoires. | Module Reporting |
| **RG-11** | Séparation des fonctions | Comptable facture mais n'autorise pas les décaissements. Secrétaire encaisse mais ne valide pas les quantités. | Architecture SI |
| **RG-12** | TVA différenciée | 19,25 % sur tous produits SAUF gamme BOUILLIE (0 % — exonérée). | Calcul automatique |
| **RG-13** | Recouvrement restreint | Seules la Chargée RP et le Chef de Production peuvent enregistrer des encaissements de créances clients. | Rôle système |
| **RG-14** | OTP opérations sensibles | Versements élevés, déblocages prix, corrections post-lock nécessitent OTP SMS (5 min, 3 tentatives max, blocage 30 min). | SMS Gateway |
| **RG-15** | Session unique | Nouvelle connexion → déconnexion automatique de l'ancienne session (JWT précédent révoqué). | Sécurité système |
| **RG-16** | Anti-capture écran | Screenshots techniquement bloqués dans l'application mobile (flag Android/iOS natif). | Flag sécurisé |
| **RG-17** | Terminal autorisé (IMEI) | IMEI vérifié au démarrage. Accès bloqué si IMEI non reconnu dans la table Terminal. | Authentification |
| **RG-18** | Taux d'occupation | Calculé automatiquement chaque semaine par zone. Alerte si < 30 % (zone sous-exploitée). | Module Reporting |
| **RG-19** | Avarie justifiée | Photo obligatoire si quantité avariée > 5 % du lot chargé ce jour. Sans photo = déclaration bloquée. | App mobile |
| **RG-20** | Archivage permanent | Données jamais supprimées, uniquement clôturées (statut ARCHIVE). Intégrité audit DSF garantie. | Base de données |

# **18. Méthodes et Responsabilités — Circuit de Validation Journalier**

## **18.1 Workflow Journalier Complet**

| **Étape** | **Heure** | **Acteur** | **Action** | **Statut Système** |
| --- | --- | --- | --- | --- |
| 1 | J-1 Soir | Commercial | Saisit Fiche Infos Produits sur mobile | SAISIE → notif Secrétaire + Comptable |
| 2 | J-1 Soir | Secrétaire | Vérifie versement J complet et signe la fiche | VALIDEE_SECRETAIRE |
| 3 | J-1 Nuit | Comptable | Arbitre les quantités et signe (RG-09) | VALIDEE_COMPTABLE |
| 4 | J Matin 6h30 | Commercial | Badgeage biométrique à l'entreprise | Présence enregistrée |
| 5 | J Matin 8h-10h | Steve (Stock) | Chargement physique + validation numérique | CHARGEE. Stock réel décrémenté. |
| 6 | J Journée | Commercial | Tournée : facturation, prospection, feuille de route (offline possible) | Ventes en LOCAL |
| 7 | J Si besoin | DG | Déblocage prix exceptionnel via web (UC-12) | Prix déverrouillé 1 transaction |
| 8 | J Si besoin | Commercial | Saisie carburant + signature (UC-05) | EN_ATTENTE validation superviseur |
| 9 | J Si besoin | Chef Prod / CG | Validation carburant à distance | VALIDEE. KPI mis à jour. |
| 10 | J Soir | Steve (Stock) | Comptage physique retours + signature numérique | Données gelées (LOCK). Stock crédité. |
| 11 | J Soir 10h-11h | Commercial + Secrétaire | Versement cash. Alerte rouge si écart. | VALIDE. Déverrouillage J+1. |
| 12 | J Soir | Système | Synchronisation automatique vers serveur central | Audit Trail mis à jour |
| 13 | J Nuit | Système | Génération KPI du jour (classement, objectifs, primes) | Dashboard DG mis à jour |

## **18.2 Calculs Automatiques du Système**

| **Calcul** | **Formule** | **Responsable** |
| --- | --- | --- |
| Montant TTC facture | Σ(Quantité × Prix HT) × (1 + TVA si applicable) | Système — Classe Vente |
| Quantité vendue journée | (StockInit + Chargement) − Retours − Avaries − Dons − Dotations | Système — LigneInfoProduit |
| Écart de caisse | cashVerse − montantAttendu | Système — Classe Versement |
| Taux atteinte objectif | (CA réalisé / CA cible) × 100 | Système — Classe Objectif |
| Prime hebdomadaire | 12 000 CFA si ≥ 80 % global ET ≥ 75 % par gamme | Système — Module Reporting |
| Taux d'occupation zone | (Clients livrés 3 mois / Clients potentiels) × 100 | Système — ZoneDeVente |
| Rendement véhicule | CA zone / Litres carburant consommés | Contrôleur de Gestion |
| Compte d'exploitation | CA − Charges variables − Charges fixes | Comptable |
| Conversion métrologique | kg produits / grammage unitaire = nb unités vendables | Système — Classe Article |
| VAC (Valeur Après Crédit) | CA total − créances (ventes à crédit non encore encaissées) | Système — Reporting |
| Solde VAC | VAC − Ventes au comptant | Système — Reporting |
| Cumul CA mensuel | Σ montantTTC depuis le 1er du mois courant | Système — Reporting |

# **19. Module Reporting, Classement et Primes**

## **19.1 Tableau de Bord DG — Fiche de Synthèse Digitale (****<**** 1 minute)**

Le tableau de bord DG reproduit fidèlement la structure de la Fiche de Synthèse papier, en temps réel, pour Yaoundé, Douala et le TOTAL consolidé :

| **Indicateur** | **Correspondance Fiche Synthèse** | **Fréquence** |
| --- | --- | --- |
| CA YAOUNDE / CA DOUALA | Section SECRETAIRE CA Produits | Temps réel |
| Effectif commerciaux actifs | Comptabilité — Effectif | Quotidien |
| V.A.C.Init | Comptabilité — V.A.C.Init | Quotidien |
| C.A (Chiffre d'Affaires) | Comptabilité — C.A | Temps réel |
| V.A.C (Valeur Après Crédit) | Comptabilité — V.A.C | Temps réel |
| Destockage | Comptabilité — Destockage | Quotidien |
| RECOUVREMENTS | Comptabilité — RECOUVREMENTS | Temps réel |
| Vente au comptant | Comptabilité — Vte au cpt | Temps réel |
| Solde V.A.C | Comptabilité — Solde V.A.C | Temps réel |
| Cumul C.A mensuel | Comptabilité — Cummul C.A | Quotidien |
| E.Chèques / VST Banques | Comptabilité — E.Chèques / VST banques | Quotidien |
| DEPENSES just / non just | Comptabilité — DEPENSES just / non just | Quotidien |
| S.Caisse (Solde Caisse) | Comptabilité — S.Caisse | Temps réel |
| Montant AVARIES | Comptabilité — Mtant AVARIES | Quotidien |
| Montant DOT/PROMO | Comptabilité — Mtant DOT/PROMO | Quotidien |
| Montant TOTAL NON VENDU | Comptabilité — Montant TOTAL NON VENDU | Quotidien |
| STOCK INITIAL / AJOUTÉ / SORTIE / FINAL | Section MONTANTS Stocks | Quotidien |
| Trésorerie disponible | Caisse.soldeActuel + VST banques | Temps réel |
| Rendement véhicules | CA zone / Litres carburant | Hebdomadaire |
| Évolution N vs N-1 | (CA mois actuel / CA même mois N-1 − 1) × 100 | Mensuel |

## **19.2 Classement Automatisé des Commerciaux**

Généré automatiquement chaque lundi matin. Affiché simultanément sur le web (DG, Chargée RP) et sur le mobile de chaque commercial (son propre rang).

| **Donnée Affichée** | **Description** |
| --- | --- |
| Rang du commercial | Position 1er, 2ème, 3ème… dans le classement journalier,hebdo,mensuel, annuel |
| CA réalisé vs objectif | Montant CFA et pourcentage d'atteinte |
| Taux par gamme | BOUILLIE, SNACKING, CHOCO, SEAU, ENRO, CROO, CHIPS, CARAM |
| Taux d'occupation zone | Clients livrés / clients potentiels × 100 |
| Nb nouveaux prospects enregistrés | Contribution à l'élargissement du portefeuille |
| Montant de recouvrements collectés | Contribution à l'apurement des créances |
| Indicateur prime | Eligible 12 000 CFA (Oui/Non) avec gamme(s) bloquante(s) identifiée(s) |
| Solde terminal préfinancé | Reste à payer sur le smartphone (déduit des primes si applicable) |

| **Algorithme Complet de Calcul des Primes — 12 000 CFA** Condition 1 (Globale)  : Taux atteinte global = (CA réalisé semaine / CA cible semaine) × 100 >= 80 % Condition 2 (Par gamme): Taux atteinte par gamme >= 75 % pour CHAQUE gamme assignée au commercial                         (BOUILLIE, SNACKING, CHOCO, SEAU, ENRO, CROO, CHIPS, CARAM...) → Les DEUX conditions doivent être SIMULTANÉMENT satisfaites. → Si une seule gamme est en dessous de 75 % : PRIME NON VERSÉE. → Calcul automatique chaque lundi matin par le Module Reporting. → Affichage dans le bulletin individuel (web + mobile). → Option : déduction du resteAPayer du terminal préfinancé. |
| --- |

## **19.3 Tableau de Bord Comparatif N vs N-1**

Interface spécifique permettant de comparer l'évolution mensuelle du CA par commercial et par zone :

| **Colonne** | **Description** |
| --- | --- |
| Commercial | Nom du commercial |
| CA Mois Actuel (CFA) | Chiffre d'affaires réalisé ce mois |
| CA Mois N-1 (CFA) | Même mois de l'année précédente |
| Évolution (%) | (CA actuel / CA N-1 − 1) × 100. Vert si positif, rouge si négatif. |
| Objectif Mois | Cible définie pour ce mois |
| Taux Atteinte (%) | CA actuel / Objectif × 100 |
| Zone | Zone(s) couverte(s) ce mois |

# **20. Workflow Villes Déportées (hors Yaoundé)**

Processus spécifique pour les commerciaux en déplacement longue durée dans des villes éloignées : Bertoua, Bafoussam, Garoua, Samoua, Sangmélima, etc. Le cycle n'est pas journalier mais mensuel ou bi-mensuel.

| **Étape** | **Acteur** | **Action** | **Outil / Champ** |
| --- | --- | --- | --- |
| 1 | Client / Commercial | Commande transmise via WhatsApp (groupe dédié Direction + Commercial) | Canal messagerie dédié |
| 2 | Comptable | Facturation établie (facture doit ou avoir selon avancement) | Vente + CommandeDistance |
| 3 | Comptable + Stock | Préparation produits et création de la CommandeDistance | agenceTransport, fraisPort, dateExpedition, numSuivi |
| 4 | Gestionnaire Stock | Expédition physique via agence de voyage partenaire | MouvementStock.type = EXPEDITION |
| 5 | Commercial (sur place) | Réception des produits + ventes sur place (mobile offline) | InfoProduit spéciale cycle mensuel |
| 6 | Commercial | Versement différé après retour à Yaoundé (pas de clôture quotidienne obligatoire) | Versement.flagDistant = true |
| 7 | Superviseur | Carburant : double signature obligatoire (validation à distance sur photo compteur) | LogistiqueCarburant.photoCompteur |
| 8 | Comptable | Clôture mensuelle de la session de vente déportée | InfoProduit.statut = CLOTUREE |

| **Règles Spécifiques Villes Déportées** La clôture de session peut être différée (flexibilité opérationnelle adaptée au terrain). Les règles de solvabilité (RG-01) s'appliquent à la clôture mensuelle, pas quotidiennement. Le carburant doit toujours être double-signé, même à distance via photo du compteur. Les données offline s'accumulent pendant toute la durée du déplacement. A la sync finale, le serveur vérifie la cohérence de l'ensemble du cycle mensuel. |
| --- |

# **21. Préfinancement des Terminaux Mobiles**

Pour garantir que chaque commercial dispose d'un terminal capable de faire tourner l'application Ionic, NTFoods met en place un système de préfinancement avec remboursement progressif sur les primes hebdomadaires.

## **21.1 Attributs Complets de la Classe Terminal**

| **Attribut** | **Type** | **Req.** | **Description** |
| --- | --- | --- | --- |
| idTerminal | Long | Oui | Clé primaire unique |
| imei | String(15) | Oui | Numéro IMEI international unique — clé d'authentification hardware |
| modele | String | Oui | Modèle smartphone (Samsung Galaxy A, Xiaomi Redmi, etc.) |
| idCommercial | FK | Oui | Commercial actuellement assigné |
| prixAchat | BigDecimal | Oui | Coût total d'acquisition de l'appareil |
| resteAPayer | BigDecimal | Oui | Solde restant dû (décrémenté à chaque prime versée) |
| actif | Boolean | Oui | Terminal actif en production |
| dateDerniereConnexion | Timestamp | Non | Dernière connexion réussie (surveillance usage) |

## **21.2 Gestion des Incidents Terminaux**

| **Incident** | **Action Système** | **Acteur** |
| --- | --- | --- |
| Perte ou vol | Effacement distant MDM (remote wipe), désactivation IMEI dans la table Terminal, récupération des données depuis le serveur cloud. | Admin + DG |
| Terminal inconnu | Accès bloqué si IMEI non reconnu (RG-17). Alerte admin immédiate. | Système |
| Connexion depuis 2 appareils | Session unique : le 2ème appareil déconnecte automatiquement le 1er (RG-15). | Système |
| Départ du commercial | Désactivation du terminal (actif = false). IMEI libéré ou réassigné. | Admin |
| Solde terminal consulté | Visible en temps réel dans l'application mobile du commercial (UC-08). | Commercial |

## **21.3 Remboursement Progressif**

- A chaque prime hebdomadaire accordée (12 000 CFA), une portion est déduite du resteAPayer du terminal.

- Le commercial voit en temps réel son solde terminal dans l'application mobile.

- Lorsque resteAPayer = 0, le terminal appartient définitivement au commercial.

- En cas de départ avant remboursement complet : solde déduit du dernier salaire (règle RH à formaliser).

# **22. Flux de Données Entrants et Sortants**

## **22.1 Données Entrantes dans le Module Commercialisation**

| **Source** | **Données Reçues** | **Fréquence** |
| --- | --- | --- |
| Module Stock | Disponibilité produits finis, seuils min, confirmation chargement, alertes rupture | Temps réel / A chaque demande |
| Module Production | Date fabrication lots, alertes DLC proches, quantités disponibles | Lors validation production |
| DG (web) | Prix débloqués ponctuellement, objectifs commerciaux, validation décaissements | A la demande / Début de mois |
| Comptable (web) | Autorisations crédit, arbitrage quantités, signatures, objectifs | Chaque soir J-1 |
| Secrétaire | Validation versement J (déverrouillage commande J+1) | Chaque soir |
| Contrôleur de Gestion | Budget carburant alloué par véhicule, validation dépenses | Hebdomadaire |

## **22.2 Données Sortantes du Module Commercialisation**

| **Destination** | **Données Envoyées** | **Déclencheur** |
| --- | --- | --- |
| Module Stock | Mouvements sortie (chargement), retours, avaries, dotations, dons, déstockage | Chaque validation chargement / retour |
| Module Reporting | CA journalier par commercial / gamme / zone, taux atteinte, nb prospects | Fin de journée |
| Module Comptabilité | Factures validées, versements cash, recouvrements encaissés | A chaque opération validée |
| DG | Alertes écart rouge, demandes déblocage prix, KPI temps réel, Fiche Synthèse digitale | En temps réel |
| Clients | Bons de livraison numériques, factures, reçus de caisse | Lors de chaque transaction |
| Chef de Production | Agrégation pré-commandes J+1 pour anticipation production | Chaque soir J-1 |

# **23. Risques d****'****Incohérence et Solutions Proposées**

| **#** | **Risque Identifié** | **Proba.** | **Solution Mise en Oeuvre** |
| --- | --- | --- | --- |
| **R-01** | Vente offline avec stock insuffisant côté serveur | Moy. | Stock fictif plafonné au chargement physique validé par Steve. Commercial ne peut vendre que ce qui est dans son camion. |
| **R-02** | Fraude carburant (facture gonflée) | Haute | Double signature commercial + superviseur. Confrontation L/100km vs historique véhicule. Alerte si écart > seuil. |
| **R-03** | Omission de déclaration d'avarie | Haute | Inventaire physique soir (Steve) détecte l'écart. Rapprochement stock théorique vs réel. Photo obligatoire si > 5%. |
| **R-04** | Double saisie d'une vente (offline + online) | Moy. | UUID généré côté client + hash SHA256. Rejet doublon automatique côté serveur. Log CONFLIT. |
| **R-05** | Crédit accordé sans autorisation en zone blanche | Faible | En mode offline : refus automatique toute vente à crédit (RG-04). Message explicatif affiché. |
| **R-06** | Incohérence stock réel vs stock fictif | Moy. | Algorithme de bouclage nightly. Tout écart > seuil déclenche alerte au Contrôleur de Gestion. |
| **R-07** | Manipulation données locales (fraude) | Haute | Audit Trail central = référence absolue. Données locales non signées = non validées. Comparaison systématique. |
| **R-08** | Retours falsifiés (gonflage invendus) | Haute | Comptage contradictoire Steve (* Livrées vs ** Trouvées). Croisement avec total livraison Feuille de Route. |
| **R-09** | Perte ou vol du terminal mobile | Faible | Données SQLite chiffrées AES-256. Effacement MDM à distance. Désactivation IMEI. Récupération depuis serveur. |
| **R-10** | Usurpation de compte commercial | Faible | Session unique (RG-15). Contrôle IMEI (RG-17). OTP SMS opérations sensibles (RG-14). Anti-capture écran (RG-16). |
| **R-11** | Défaut de connectivité prolongé | Moy. | Mode offline-first. Queue FIFO locale. Alerte si sync > 4h non effectuée. Blocage crédit offline. |
| **R-12** | Modification frauduleuse post-lock | T.Faible | Lock numérique + workflow exception multi-autorisation + OTP + log exhaustif. Stats anomalies pour DG. |

# **24. Architecture Logique et Stack Technique**

## **24.1 Stack Technique Recommandé**

| **Couche** | **Technologie** | **Justificatif** |
| --- | --- | --- |
| Backend API | Spring Boot 3.x (Java 17+) | Robuste, écosystème entreprise mature, patterns SI éprouvés |
| Base de données | PostgreSQL 15+ | ACID, JSONB, géospatial, full-text, chiffrement, haute disponibilité |
| Mobile | Ionic 7 + Capacitor 5 | Cross-platform Android/iOS, plugins natifs offline, SQLite local |
| Web Frontend | Angular 17+ | SPA TypeScript, composants riches, RBAC, multi-rôles |
| Synchronisation | WebSocket + REST fallback | Temps réel avec dégradation gracieuse |
| Authentification | JWT + OTP SMS | Stateless, sécurisé, MFA ready |
| Infrastructure | Docker + Kubernetes | Scalable, haute disponibilité, rolling updates |
| Chiffrement local | AES-256 (SQLite) | Protection des données offline sur le terminal mobile |
| MDM | Mobile Device Management | Effacement à distance, contrôle des terminaux préfinancés |
| SMS Gateway | Prestataire local Cameroun | OTP, alertes, notifications (~35 CFA/SMS) |

## **24.2 Schéma d****'****Intégration des Modules**

| +------------------------------------------------------------------------+   │                    BASE DE DONNEES CENTRALE                            │   │          PostgreSQL 15+ — Audit Trail — Chiffrement AES-256            │   +----------+-------------------------+--------------------------+---------+              │                         │                          │   +----------v--------+    +-----------v-------+    +------------v-----------+   │   MODULE           │    │   MODULE           │    │   MODULE               │   │   PRODUCTION       │<-->│     STOCK          │<-->│  COMMERCIALISATION     │   │   (Web Angular)    │    │  (Web Angular)     │    │  (App Ionic Mobile)    │   +-------------------+    +-------------------+    +------------------------+                                      │                           │                           +----------v---------------------------v----------+                           │         API REST  Spring Boot 3.x              │                           │   JWT Auth — WebSocket — OTP SMS Gateway       │                           │   Sync offline <-> serveur central (FIFO)      │                           +------------------------------------------------+                                                 │                                     +-----------v-----------+                                     │   MODULE REPORTING    │                                     │   KPI — Classement    │                                     │   Primes — N vs N-1   │                                     │   Fiche Synthese DG   │                                     +-----------------------+ |
| --- |

# **25. Dépendances Fonctionnelles**

| **Fonctionnalité** | **Dépend de** | **Type** | **Module Source** |
| --- | --- | --- | --- |
| Chargement stock camion | Stock PF disponible et validé par Steve | Requise | Module Stock |
| Tarification des ventes | Référentiel Article avec prix homologués à jour | Requise | Référentiel |
| Autorisation vente à crédit | Validation Comptable + soldeDette = 0 | Requise | API Comptable |
| Déblocage prix exceptionnel | Action DG en temps réel (connexion obligatoire) | Requise | API Direction |
| Calcul primes et classement | Objectifs définis + CA réel synchronisé | Requise | Module Reporting |
| Alerte rupture stock | seuilAlerteStock défini + surveillance continue | Requise | Module Stock |
| Compte d'exploitation | Module Comptabilité + toutes ventes synchronisées | Requise | Module Comptabilité |
| Recouvrement créances | soldeDette client à jour dans référentiel Client | Requise | Référentiel |
| Double signature carburant | Superviseur connecté et habilité (Chef Prod / CG) | Requise | Module Logistique |
| OTP opérations sensibles | Connectivité SMS Gateway + numéro employé enregistré | Requise | Infrastructure externe |
| Taux d'occupation marché | Base clients livrés + prospects à jour | Requise | Module Reporting |
| Préfinancement terminal | Table Terminal + resteAPayer à jour | Optionnelle | Module RH |
| Promotions DLC | dateFinPromo définie dans Article | Optionnelle | Module Production |
| Workflow villes déportées | CommandeDistance + agenceTransport paramétrée | Optionnelle | Module Commercial |

# **26.  Questions Restantes**



L'UUID de vente est généré côté client pour éviter les doublons offline.
Validé par la stratégie technique. Les sources imposent une architecture "Offline-First"
. Pour garantir l'intégrité des données sans connexion (3G/4G), la génération d'un identifiant unique (UUID) sur le terminal mobile est la solution préconisée pour synchroniser les ventes ultérieurement sans créer de collisions en base de données
.
H-02
Seuil de stock minimum déclenchant une alerte à définir par article.
Confirmé par les processus. Les sources mentionnent que le gestionnaire de stock (Steve) doit signaler les besoins de réapprovisionnement 5 à 6 mois à l'avance pour les importations (Chine) afin d'éviter les ruptures lors des 2 derniers mois de stock restant
. Le système prévoit des alertes paramétrables sur des seuils critiques
.
H-04
Nombre de clients potentiels par zone saisi annuellement par la Chargée RP.
Validé partiellement. La Chargée RP (Mme Gilde) gère effectivement la répartition des zones et des objectifs
. Le système utilise un indicateur de "taux d'occupation du marché" comparant les clients livrés aux boutiques recensées (potentiel)
. Bien que la fréquence annuelle ne soit pas explicite, le calibrage est nécessaire pour définir les objectifs mensuels
.
H-05
Promotions DLC gérées via des champs prixPromotion et dateFinPromo.
Cohérent avec le métier. Mme Gilde analyse les stocks pour identifier les produits ayant des dates de péremption proches afin de lancer des promotions
. Ces prix sont alors débloqués à distance (champs grisés devenant actifs) après autorisation de la direction
.
H-07
Synchronisation offline en file FIFO (First-In, First-Out).
Validé par le principe d'immuabilité. Le système repose sur l'idée que les données validées sont non modifiables
. Une synchronisation séquentielle (FIFO) assure que l'historique des ventes et des mouvements de stock fictifs (camion) respecte l'ordre réel des transactions terrain
.
H-08
Suivi des statistiques personnelles du commercial lors d'un changement de zone.
Confirmé explicitement. Monsieur Mbakop précise que si un commercial change de zone (ex: passe de Mokolo à Acacia), ses statistiques personnelles le suivent, tandis que les données historiques de la zone restent rattachées au secteur géographique pour analyse comparative
.
H-09
Taux d'occupation sur les clients livrés au moins une fois en 3 mois.
En accord avec les cycles de zone. Les sources confirment que l'affectation à une zone est stable sur une période de 3 à 4 mois pour permettre une analyse pertinente
. Ce délai de 3 mois sert de fenêtre glissante pour évaluer la pénétration réelle du marché par rapport au potentiel
.
Questions ou Points à clarifier
Validation des suppressions : Le système de sécurité prévoit qu'après 3 tentatives de codes erronés ou demandes de modifications "bizarres", le compte est bloqué, nécessitant un déblocage manuel par l'administrateur
.
Double Stock : La modélisation technique doit bien distinguer le stock réel (magasins physiques) du stock fictif (unités chargées dans le véhicule du commercial) pour calculer les ventes réelles au soir via la soustraction Chargement - Retours
.


## **26.2 Questions Restantes pour Finalisation**

Seuils de stock : Les sources ne mentionnent pas de seuils numériques universels (ex: 50 cartons). Cependant, une règle critique de gestion est établie pour les approvisionnements lointains (ex: emballages de Chine) : le gestionnaire de stock (Steve) doit signaler le besoin de réapprovisionnement 5 à 6 mois à l'avance pour qu'à l'échéance des 2 ou 3 mois de stock restants, la marchandise soit déjà acheminée
. Pour le stock courant, l'ERP prévoit la configuration de stocks de sécurité et d'alertes de rupture personnalisables par article
.
Seuil OTP : Les sources prévoient bien l'utilisation de codes OTP SMS pour sécuriser les opérations sensibles, notamment lors de la phase d'envoi ou de saisie de montants financiers sur l'application
. Toutefois, le montant spécifique de 100 000 CFA n'est pas mentionné dans les sources ; il s'agit d'un paramètre à définir lors de l'implémentation pour libérer l'accès aux fonctions de versement
.
Objectifs par gamme : Les cibles de 75 % d'atteinte s'appliquent aux quatre gammes principales identifiées (Bouillies, Snacks/A grignoter, Ingrédients, Choco)
. Chaque gamme validée à 75 % (sous réserve d'une performance globale de 80 %) déclenche une prime de 3 000 CFA, pour un total potentiel de 12 000 CFA par semaine
.
Format factures : Le système est conçu pour générer des factures au format PDF
. Si l'application enregistre les ventes numériquement pour tous les clients, le format PDF est spécifiquement prévu pour les grands comptes (hôtels, supermarchés) qui exigent un document physique ou une transmission numérique officielle pour leurs services comptables
.
Gestion promotions DLC : La promotion est généralement traitée comme un prix exceptionnel fixe (ex: passage de 5 000 à 4 000 CFA) plutôt qu'un pourcentage
. La décision de lancer une promotion basée sur des dates de péremption proches est pilotée par la Chargée RP (Mme Gilde) après analyse des stocks
, mais le déblocage technique du prix "grisé" sur le terminal du commercial nécessite la validation à distance de la Direction (DG)
.
Portabilité clients : Lorsqu'un commercial change de zone, ses clients actifs sont transférés au nouveau commercial affecté à ce secteur
. Les statistiques et l'historique restent rattachés à la zone (ex: Mokolo) pour assurer la continuité commerciale, même si l'identité du vendeur change
.
Villes déportées : Le cycle pour les villes comme Bertoua ou Samoua est mensuel
. Les commandes sont passées une fois par mois, et les produits sont expédiés en masse
. Concernant la synchronisation, l'application suit une stratégie Offline-First : elle stocke les données localement et se synchronise dès qu'une connexion (3G/4G) est détectée, sans attendre le retour physique du commercial au siège
.
Export DSF : Le système a pour objectif de produire une production automatique des éléments de la Déclaration Statistique et Fiscale (DSF), incluant le chiffre d'affaires, les charges et les stocks
. Il existe un module DSF spécifique prévu parmi les livrables finaux pour faciliter ces déclarations
.



2. Seuil d'alerte carburant : Écart acceptable
Les sources ne mentionnent pas de pourcentage précis (comme 15-20 %) pour déclencher une enquête, mais elles définissent une logique de confrontation systématique
.
Ratio Consommation / Chiffre d'Affaires : L'alerte se base sur la cohérence entre les dépenses de carburant et le CA généré dans la zone
. Une hausse de consommation (ex: passage de 100 000 à 400 000 CFA/mois) n'est jugée acceptable que si elle s'accompagne d'une hausse proportionnelle du travail réalisé
.
Analyse de l'historique : Le système compare les données du mois N par rapport au mois N-1
. Des consommations jugées « bizarres » (ex: deux pleins successifs en 24h sans déplacement en zone éloignée) sont immédiatement identifiées comme des indicateurs de fraude potentielle
.
3. Fréquence du Taux d'Occupation et fenêtre glissante
Le calcul et l'analyse du taux d'occupation des marchés suivent un rythme principalement mensuel
.
Calcul mensuel : C'est lors du rapport mensuel et de la réunion de début de mois que la direction (M. Mbakop) analyse la pénétration du marché (nombre de boutiques livrées vs potentiel de la zone)
.
Fenêtre de 3 mois : Les sources confirment que l'affectation d'un commercial à une zone est généralement stable sur une période de 3 à 4 mois pour permettre une analyse pertinente de sa performance et de l'évolution du taux d'occupation
.
Analyse glissante : L'interface doit permettre une comparaison avec les deux mois précédents (N-1 et N-2) pour visualiser la progression réelle en valeur absolue et non seulement en pourcentage
.
Contrôleur des Ventes : Ce rôle sur la Feuille de Route est-il le Contrôleur de Gestion ou un rôle distinct à créer ? : Non


Synthèse des indicateurs pour l'interface
Indicateur
Fréquence d'analyse
Responsable
Vérification Feuille de Route
Journalière
Contrôleur des ventes ("Policiers")
Rendement Carburant
Mensuelle
Chef de Production / Contrôleur de Gestion
Taux d'occupation
Mensuelle (Vue sur 3 mois)
Chargée RP / DG


*Document Confidentiel — NTFoods SARL — TANTY Digital*

*Module de Gestion Commerciale et des Ventes — Version 4.0 Ultra-Complete*

*Sources : CDC v1 **&** DG, EPC, Fiche de Synthèse DG (scannée), Feuille de Route terrain (scannée)*

**Distribution restreinte — Ne pas diffuser sans autorisation DG**

Document Confidentiel — Page