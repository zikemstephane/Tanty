-- ═══════════════════════════════════════════════════════════════════════════
-- V11 — Colonnes héritées de `lots_produits_finis`
-- ═══════════════════════════════════════════════════════════════════════════
--
-- La table portait deux jeux de colonnes décrivant la même chose :
--
--   hérité (NOT NULL)          courant (NOT NULL, mappé par l'entité)
--   ───────────────────        ──────────────────────────────────────
--   code_produit               product_sku
--   designation_produit        product_name
--   quantite_kg                declared_quantity_kg
--   nb_cartons                 equivalent_units
--
-- `LotProduitFiniJpaEntity` ne mappe que le second jeu. Le premier restait donc
-- NULL à chaque insertion, alors qu'il est déclaré NOT NULL sans valeur par
-- défaut : **aucune déclaration de lot ne pouvait aboutir**, ni au peuplement ni
-- en usage réel. Le défaut est resté invisible parce que `ddl-auto=validate`
-- vérifie les tables, les colonnes et les types attendus, mais ignore les
-- colonnes surnuméraires — celles que l'entité ne déclare pas.
--
-- Les colonnes héritées sont retirées plutôt que rendues nullables : les laisser
-- vides à jamais reviendrait à présenter comme « donnée manquante » ce qui est
-- en réalité une donnée qui a déménagé. Rien ne les lit — ni entité, ni requête
-- native, ni migration antérieure — et la table est vide, donc rien n'est perdu.
--
-- Idempotence : DROP COLUMN IF EXISTS.
-- ═══════════════════════════════════════════════════════════════════════════

ALTER TABLE lots_produits_finis DROP COLUMN IF EXISTS code_produit;
ALTER TABLE lots_produits_finis DROP COLUMN IF EXISTS designation_produit;
ALTER TABLE lots_produits_finis DROP COLUMN IF EXISTS quantite_kg;
ALTER TABLE lots_produits_finis DROP COLUMN IF EXISTS nb_cartons;
