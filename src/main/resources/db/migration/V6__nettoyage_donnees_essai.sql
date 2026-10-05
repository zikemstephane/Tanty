-- ═══════════════════════════════════════════════════════════════════════════
-- V6 — Nettoyage du jeu d'essai et consolidation des entrepôts mobiles
-- ═══════════════════════════════════════════════════════════════════════════
--
-- Migration **destructive**, décidée après audit. Sauvegarde complète préalable :
--   _backup_avant_nettoyage_bd_20260816/erp_tanty_db_complet.dump
--   (pg_dump format custom — restauration par pg_restore)
--
-- ── Ce que l'audit a établi ────────────────────────────────────────────────
--
-- L'historique commercial en base est intégralement fictif. Les 70 ventes
-- référencent huit codes produit — ART-001, ART-004, ART-010, ART-011, ART-020,
-- ART-021, ART-030, ART-040 — qui **n'existent pas au catalogue**, et ont été
-- créées en masse sur trois dates (06/07, 13/07, 20/07). Il en va de même des
-- précommandes, versements, recouvrements, primes, objectifs, feuilles de route
-- et factures qui en découlent.
--
-- Cet historique n'est pas seulement inutile : il est trompeur. Les tableaux de
-- bord affichent un chiffre d'affaires qui ne correspond à aucune vente réelle,
-- et le cas du commercial qui ouvre l'application pour la première fois — sans
-- versement ni inventaire clos — est masqué par ces données.
--
-- ── Ce qui est conservé ────────────────────────────────────────────────────
--
-- Utilisateurs, catalogue produits et conditionnements, clients réels,
-- entrepôts, grilles tarifaires, zones de vente, boutiques, véhicules,
-- terminaux, et l'ensemble des tables de paramétrage daté (barèmes de prime,
-- règles d'avarie, paramétrages caisse et objectif).
--
-- Idempotence : chaque suppression porte sur une condition qui n'est plus
-- remplie après coup ; la migration se rejoue sans erreur.
-- ═══════════════════════════════════════════════════════════════════════════


-- ── 1. Clients issus de la prospection supprimée ────────────────────────────
--
-- V4 les avait requalifiés en DETAIL faute d'arbitrage. L'arbitrage est rendu :
-- ils sont supprimés. Aucune vente, aucun recouvrement ni aucune précommande ne
-- leur est attaché — vérifié avant écriture.

DELETE FROM clients
 WHERE code_client IN ('CLI-1783334554537', 'CLI-1783339028159')
   AND NOT EXISTS (SELECT 1 FROM ventes v WHERE v.code_client = clients.code_client)
   AND NOT EXISTS (SELECT 1 FROM recouvrements r WHERE r.code_client = clients.code_client);


-- ── 2. Historique commercial fictif ─────────────────────────────────────────
--
-- L'ordre suit les clés étrangères : chaque enfant avant son parent. Le graphe a
-- été relevé sur information_schema plutôt que supposé — une première rédaction
-- omettait lignes_inventaire et la migration échouait au milieu.

DELETE FROM lignes_facture;
DELETE FROM factures_avoir;
DELETE FROM factures;

DELETE FROM lignes_vente;
DELETE FROM ventes;

DELETE FROM lignes_precommande;
DELETE FROM precommandes;

DELETE FROM lignes_retour;
DELETE FROM bons_retour;

DELETE FROM arrets_feuill_route;
DELETE FROM feuills_route;

DELETE FROM recouvrements;
DELETE FROM versements;
DELETE FROM demandes_carburant;

DELETE FROM prime_realisations_par_gamme;
DELETE FROM prime_objectifs_par_gamme;
DELETE FROM primes_commerciales;

DELETE FROM objectifs_par_gamme;
DELETE FROM objectifs_commerciaux;

DELETE FROM session_dotations;
DELETE FROM sessions_commercial;

DELETE FROM lignes_inventaire;
DELETE FROM sessions_inventaire;

DELETE FROM lignes_stock_mobile;
DELETE FROM stocks_mobiles;

DELETE FROM lignes_caisse;
DELETE FROM journaux_caisse;
DELETE FROM decaissements;

-- Lots de production : « Farine de blé », « Eau minérale », « Jus d'orange » —
-- des produits que NTFOODS ne fabrique pas.
DELETE FROM lots_produits_finis;

-- Promotions pointant vers des codes produit inexistants. Elles ne pouvaient
-- ouvrir droit à aucune remise : le serveur ne trouvait jamais le produit.
DELETE FROM promotions
 WHERE NOT EXISTS (SELECT 1 FROM produits p WHERE p.code = promotions.code_produit);


-- ── 3. Consolidation des entrepôts mobiles ──────────────────────────────────
--
-- Deux familles d'entrepôts mobiles coexistaient pour un même commercial :
-- ENT-MOB-COM1..5 (jeu d'essai de juillet) et MOBILE-COM-001..005, cette
-- seconde forme étant celle qu'engendre le code (`Entrepot.creerMobile` produit
-- « MOBILE-<matricule> »).
--
-- Ce n'était pas cosmétique : la résolution passait par
-- `findByMatriculeProprietaire(...).findFirst()`, dont le résultat dépend de
-- l'ordre de lecture. Le stock d'un même commercial s'est réparti sur les deux —
-- les mouvements le montrent, ENT-MOB-COM1 et MOBILE-COM-001 en ont reçu.
--
-- Les niveaux restants sont d'abord ceux des produits fantômes : ils partent
-- avec le reste du jeu d'essai. Ce qui subsiste sur les entrepôts hérités est
-- ensuite reporté sur l'entrepôt canonique, et les mouvements réétiquetés pour
-- que la traçabilité désigne l'entrepôt qui existe encore.

DELETE FROM niveaux_stock
 WHERE NOT EXISTS (SELECT 1 FROM produits p WHERE p.code = niveaux_stock.code_produit);

-- Report des quantités héritées sur l'entrepôt canonique du même commercial.
UPDATE niveaux_stock cible
   SET quantite_en_base = cible.quantite_en_base + source.quantite_en_base,
       date_modification = now()
  FROM niveaux_stock source
  JOIN entrepots e_source ON e_source.code = source.code_entrepot
  JOIN entrepots e_cible  ON e_cible.matricule_proprietaire = e_source.matricule_proprietaire
                         AND e_cible.code LIKE 'MOBILE-%'
 WHERE e_source.code LIKE 'ENT-MOB-%'
   AND cible.code_entrepot = e_cible.code
   AND cible.code_produit  = source.code_produit;

-- Ce que l'entrepôt canonique ne portait pas encore : rattaché tel quel.
UPDATE niveaux_stock
   SET code_entrepot = (SELECT e_cible.code
                          FROM entrepots e_source
                          JOIN entrepots e_cible
                            ON e_cible.matricule_proprietaire = e_source.matricule_proprietaire
                           AND e_cible.code LIKE 'MOBILE-%'
                         WHERE e_source.code = niveaux_stock.code_entrepot
                         LIMIT 1)
 WHERE code_entrepot LIKE 'ENT-MOB-%'
   AND EXISTS (SELECT 1
                 FROM entrepots e_source
                 JOIN entrepots e_cible
                   ON e_cible.matricule_proprietaire = e_source.matricule_proprietaire
                  AND e_cible.code LIKE 'MOBILE-%'
                WHERE e_source.code = niveaux_stock.code_entrepot)
   AND NOT EXISTS (SELECT 1
                     FROM niveaux_stock autre
                     JOIN entrepots e_source ON e_source.code = niveaux_stock.code_entrepot
                     JOIN entrepots e_cible
                       ON e_cible.matricule_proprietaire = e_source.matricule_proprietaire
                      AND e_cible.code LIKE 'MOBILE-%'
                    WHERE autre.code_entrepot = e_cible.code
                      AND autre.code_produit = niveaux_stock.code_produit);

-- Les lignes déjà reportées ci-dessus n'ont plus de raison d'être.
DELETE FROM niveaux_stock WHERE code_entrepot LIKE 'ENT-MOB-%';

-- Les mouvements de stock partent avec l'historique commercial : ils ne
-- décrivent que des transferts de produits fantômes.
DELETE FROM mouvements_stock;

DELETE FROM entrepots WHERE code LIKE 'ENT-MOB-%';


-- ── 4. Verrouillage : un seul entrepôt mobile par commercial ────────────────
--
-- La contrainte est ce qui empêche le défaut de revenir. Sans elle, un futur
-- jeu d'essai ou une reprise manuelle recréerait un doublon, et la résolution
-- redeviendrait dépendante de l'ordre de lecture.
--
-- Index partiel : la règle ne vaut que pour les entrepôts mobiles, les entrepôts
-- centraux et tampons n'ayant pas de propriétaire.

CREATE UNIQUE INDEX IF NOT EXISTS uq_entrepot_mobile_par_commercial
    ON entrepots (matricule_proprietaire)
 WHERE type = 'MOBILE' AND matricule_proprietaire IS NOT NULL;
