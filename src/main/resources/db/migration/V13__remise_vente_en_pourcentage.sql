-- ═══════════════════════════════════════════════════════════════════════════
-- V13 — La remise d'une ligne de vente devient un pourcentage
-- ═══════════════════════════════════════════════════════════════════════════
--
-- `lignes_vente.remise` portait un **montant en FCFA** retranché du brut.
-- `lignes_facture.remise` portait, dans le même système, un **pourcentage** —
-- `quantite * prix * (1 - remise / 100)`. Deux lignes décrivant la même
-- marchandise employaient donc un champ de même nom et de sens opposé, sans
-- qu'aucun type ni aucune contrainte ne l'indique.
--
-- Le commanditaire a tranché le 18/08/2026 : le pourcentage l'emporte. La
-- facture était déjà conforme ; seule la vente est convertie ici.
--
-- ── Conversion ─────────────────────────────────────────────────────────────
--
--   pourcentage = montant_FCFA / (quantite × prix_unitaire) × 100
--
-- Le brut nul est traité à part. Diviser par zéro produirait NULL sur une
-- colonne NOT NULL, ou pire un `NaN` qui traverserait ensuite tous les états de
-- chiffre d'affaires sans jamais déclencher d'erreur. Une ligne sans brut
-- n'ouvre droit à aucune remise : zéro est la seule valeur juste.
--
-- Le résultat est plafonné à 100. L'ancienne validation refusait déjà une
-- remise supérieure au brut, donc le cas ne devrait pas exister — mais une
-- migration ne doit pas produire des lignes que le domaine refuserait de
-- relire.
--
-- ── Portée réelle ──────────────────────────────────────────────────────────
--
-- Au moment d'écrire cette migration, les 120 lignes de vente en base portent
-- toutes une remise à zéro : la conversion ne change rien. Elle est écrite pour
-- les environnements qui, eux, en portent.
--
-- ── Rejeu ──────────────────────────────────────────────────────────────────
--
-- La conversion est **délibérément inconditionnelle**. Rien dans la donnée ne
-- permet de distinguer une remise déjà convertie d'une remise en francs : « 10 »
-- est un pourcentage plausible et un montant plausible. Une première rédaction
-- tentait de les séparer par un seuil ; elle laissait passer les petites
-- remises, qui restaient en francs et devenaient des pourcentages absurdes.
--
-- C'est Flyway qui garantit l'exécution unique, par son historique et sa somme
-- de contrôle. Rejouer ce fichier à la main sur une base déjà migrée diviserait
-- une seconde fois — ne le faites pas.
-- ═══════════════════════════════════════════════════════════════════════════

UPDATE lignes_vente
   SET remise = LEAST(
           100.0,
           CASE
               WHEN quantite * prix_unitaire > 0
                   THEN remise / (quantite * prix_unitaire) * 100.0
               ELSE 0.0
           END
       );

COMMENT ON COLUMN lignes_vente.remise IS
    'Pourcentage de remise (0 a 100) portant sur la ligne entiere. Etait un montant en FCFA avant V13.';
