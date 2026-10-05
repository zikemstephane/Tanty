-- ═══════════════════════════════════════════════════════════════════════════
-- Poids unitaires manquants — à compléter avec les vraies valeurs
-- ═══════════════════════════════════════════════════════════════════════════
--
-- Ce fichier N'EST PAS une migration Flyway : il n'est jamais appliqué
-- automatiquement au démarrage (il ne se trouve pas dans db/migration/).
-- C'est un script autonome, à remplir puis exécuter manuellement une fois
-- prêt (ex. via psql ou un client SQL).
--
-- Contexte : 42 des 47 produits du catalogue (seed minimal) ont un poids
-- unitaire à 0, valeur provisoire posée faute de données réelles disponibles
-- au moment du seed. Depuis PlanificationProductionService, tout calcul de
-- plan de production impliquant un de ces produits échoue désormais
-- explicitement (RegleMetierException) plutôt que de produire un résultat à
-- zéro en silence.
--
-- Pour chaque ligne : remplacer REMPLACER_PAR_LE_POIDS_EN_GRAMMES par le
-- poids réel, en grammes, d'UNE unité de base (le sachet/étui/seau vendu à
-- l'unité — pas le carton). Toute ligne laissée telle quelle provoquera une
-- erreur de syntaxe si le script est exécuté sans modification : c'est
-- volontaire, pour ne jamais écrire une valeur inventée par erreur.
--
-- Les 5 produits déjà connus (BVT-001, BVT-002, TCH-001, TCH-002, TCH-003)
-- ont déjà un vrai poids en base et ne figurent pas dans ce script.

-- ── TANTY BOUILLIE DE SOJA — Sachets ─────────────────────────────────────
-- REINE BOUILLIE SOJA ARACHIDES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-001-UNITE';
-- REINE BOUILLIE SOJA CLASSIC
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-002-UNITE';
-- REINE BOUILLIE SOJA LACTEE CROISSANCE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-003-UNITE';
-- REINE BOUILLIE SOJA MULTI CEREALE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-004-UNITE';
-- REINE BOUILLIE SOJA POISSON
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-005-UNITE';
-- TANTY BOUILLIE SOJA ARACHIDES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-006-UNITE';
-- TANTY BOUILLIE SOJA LACTEE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-007-UNITE';
-- TANTY BOUILLIE SOJA NATURE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-008-UNITE';
-- TANTY BOUILLIE SOJA PARFUME
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-009-UNITE';
-- TANTY BOUILLIE SOJA POISSON
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-SAC-010-UNITE';

-- ── TANTY BOUILLIE DE SOJA — Étuis ───────────────────────────────────────
-- PRESTIGE REINE BOUILLIE ARACHIDES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-001-UNITE';
-- PRESTIGE REINE BOUILLIE CLASSIC
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-002-UNITE';
-- PRESTIGE REINE BOUILLIE LACTEE CROISSANCE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-003-UNITE';
-- PRESTIGE REINE BOUILLIE MULTI CEREALE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-004-UNITE';
-- PRESTIGE REINE BOUILLIE POISSON
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-005-UNITE';
-- PRESTIGE TANTY BOUILLIE ARACHIDES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-006-UNITE';
-- PRESTIGE TANTY BOUILLIE LACTEE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-007-UNITE';
-- PRESTIGE TANTY BOUILLIE NATURE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-008-UNITE';
-- PRESTIGE TANTY BOUILLIE PARFUME
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-009-UNITE';
-- PRESTIGE TANTY BOUILLIE POISSON
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TBS-ETU-010-UNITE';

-- ── Reine Costard Powder — Seaux 1L / 2L / 5L ────────────────────────────
-- REINE COSTARD POWDER 1L BANANE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-1L-001-UNITE';
-- REINE COSTARD POWDER 1L FRAISE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-1L-002-UNITE';
-- REINE COSTARD POWDER 1L FRUITS ET OEUFS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-1L-003-UNITE';
-- REINE COSTARD POWDER 1L LACTEE BISCUITE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-1L-004-UNITE';
-- REINE COSTARD POWDER 2L BANANE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-2L-001-UNITE';
-- REINE COSTARD POWDER 2L FRAISE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-2L-002-UNITE';
-- REINE COSTARD POWDER 2L FRUITS ET OEUFS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-2L-003-UNITE';
-- REINE COSTARD POWDER 2L LACTEE BISCUITE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-2L-004-UNITE';
-- REINE COSTARD POWDER 5L FRUITS ET OEUFS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'RCP-5L-001-UNITE';

-- ── Ingrédients ───────────────────────────────────────────────────────────
-- TANTY CHAPELURE
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'ING-CHP-001-UNITE';

-- ── TANTY À GRIGNOTER — Minis / Moyens / Grands ──────────────────────────
-- MIN A GRIGNOTER ARACHIDES ENROBEES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MIN-001-UNITE';
-- MIN A GRIGNOTER CARAMELS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MIN-002-UNITE';
-- MIN A GRIGNOTER CROQUETTES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MIN-003-UNITE';
-- MINI A GRIGNOTER CHIPS DE PLANTAINS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MIN-004-UNITE';
-- TANTY BEIGNETS SOUFFLES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MIN-005-UNITE';
-- MOYEN A GRIGNOTER ARACHIDES ENROBEES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MOY-001-UNITE';
-- MOYEN A GRIGNOTER CARAMELS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MOY-002-UNITE';
-- MOYEN A GRIGNOTER CROQUETTES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MOY-003-UNITE';
-- MOYEN A GRIGNOTER CHIPS DE PLANTAINS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-MOY-004-UNITE';
-- GRAND A GRIGNOTER ARACHIDES ENROBEES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-GRD-001-UNITE';
-- GRAND A GRIGNOTER CARAMELS
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-GRD-002-UNITE';
-- GRAND A GRIGNOTER CROQUETTES
UPDATE conditionnements SET poids_unitaire_grammes = REMPLACER_PAR_LE_POIDS_EN_GRAMMES WHERE sku = 'TAG-GRD-003-UNITE';

-- ── Vérification après remplissage ───────────────────────────────────────
-- Doit renvoyer 0 ligne une fois le script complété et exécuté :
-- SELECT p.code, p.designation, c.sku
-- FROM produits p JOIN conditionnements c ON c.produit_id = p.id
-- WHERE c.poids_unitaire_grammes IS NULL OR c.poids_unitaire_grammes <= 0;
