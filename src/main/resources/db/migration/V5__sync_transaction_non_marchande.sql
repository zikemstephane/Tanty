-- ═══════════════════════════════════════════════════════════════════════════
-- V5 — Synchronisation hors ligne : transactions non marchandes
-- ═══════════════════════════════════════════════════════════════════════════
--
-- Lot 4. Le rejeu d'une opération saisie hors ligne distingue désormais la vente
-- marchande de la transaction non marchande (avarie, don, dotation, échange) :
-- les deux n'empruntent pas le même chemin — l'une encaisse, l'autre fait
-- seulement varier le stock — et ne peuvent donc pas être rejouées par la même
-- logique.
--
-- La contrainte CHECK de sync_logs.entity_type est réémise pour accepter la
-- nouvelle valeur. Sans cela, un push d'avarie hors ligne échouerait à
-- l'insertion du journal : le commercial verrait une erreur technique là où son
-- opération est parfaitement valide.
--
-- Idempotence : DROP ... IF EXISTS puis ADD, rejouable sans erreur.
-- ═══════════════════════════════════════════════════════════════════════════

ALTER TABLE sync_logs DROP CONSTRAINT IF EXISTS sync_logs_entity_type_check;
ALTER TABLE sync_logs ADD CONSTRAINT sync_logs_entity_type_check
    CHECK (entity_type IN (
        'VENTE', 'TRANSACTION_NON_MARCHANDE', 'PRECOMMANDE',
        'FEUILL_ROUTE', 'ARRET', 'CLOTURE_JOURNEE'
    ));
