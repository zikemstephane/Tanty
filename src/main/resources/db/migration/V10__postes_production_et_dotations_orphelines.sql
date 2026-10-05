-- ═══════════════════════════════════════════════════════════════════════════
-- V10 — Postes de production réalignés, dotations orphelines retirées
-- ═══════════════════════════════════════════════════════════════════════════
--
-- ── 1. `lignes_affectation_poste_check` ────────────────────────────────────
--
-- La contrainte listait onze postes qui ne sont plus ceux de l'énumération
-- `CodePoste` : elle admettait MISE_SOUS_VIDE, SAISIE, EMBALLAGE, EXPEDITION,
-- NETTOYAGE, MELANGE, CONTROLE_QUALITE, MISE_EN_CARTON, CARAMEL_MIXTE et
-- CONDITIONNEMENT — dont aucun n'existe plus côté Java — et refusait CROQUETTE,
-- ARACHIDE_ENROBEE, CHIPS_PLANTAIN, les quatre postes de conditionnement,
-- ETIQUETAGE et DATAGE, qui sont les postes réels.
--
-- Le défaut est passé inaperçu parce que `ddl-auto=validate` compare les tables,
-- les colonnes et les types, mais **ne vérifie aucune contrainte CHECK**. Toute
-- affectation à un poste réel échouait donc à l'écriture, en production comme au
-- peuplement — c'est ce qui a fait échouer `ProductionSeeder`.
--
-- La liste ci-dessous a été extraite de `CodePoste.java`, pas retranscrite à la
-- main : une liste recopiée redeviendrait fausse à la première évolution.
--
-- ── 2. Dotations orphelines ────────────────────────────────────────────────
--
-- Cinq dotations `DOT-0001` à `DOT-0005` portaient une date d'effet fixée au
-- jour du peuplement et n'appartenaient à aucune session : la migration V6 avait
-- supprimé les sessions sans leurs dotations. Elles chargeaient un camion pour
-- une journée qui n'a jamais existé. Les dotations peuplées depuis portent une
-- référence `DOT-<yyyyMMdd>-<matricule>` et sont rattachées à leur session.
--
-- Idempotence : DROP ... IF EXISTS, et suppression filtrée sur le préfixe.
-- ═══════════════════════════════════════════════════════════════════════════

ALTER TABLE lignes_affectation DROP CONSTRAINT IF EXISTS lignes_affectation_poste_check;

-- Seize lignes d'affectation portaient les anciens postes — SAISIE, EMBALLAGE,
-- MISE_EN_CARTON, CONTROLE_QUALITE — qui n'ont aucun équivalent parmi les postes
-- actuels : ceux-ci décrivent des fabrications (caramel, croquette, arachide
-- enrobée) là où les anciens décrivaient des gestes d'atelier. Leur inventer une
-- correspondance fabriquerait un historique de production qui n'a pas eu lieu.
-- Elles proviennent d'un jeu d'essai, et le peuplement en recrée de conformes.
-- Les identifiants sont figés d'abord : supprimer les lignes en premier ferait
-- perdre le moyen de retrouver les affectations à supprimer, et supprimer les
-- affectations en premier viole la clé étrangère.
CREATE TEMP TABLE affectations_obsoletes AS
SELECT DISTINCT affectation_id AS id
  FROM lignes_affectation
 WHERE affectation_id IS NOT NULL
   AND poste NOT IN ('CARAMEL', 'CROQUETTE', 'ARACHIDE_ENROBEE', 'CHIPS_PLANTAIN',
                     'CONDITIONNEMENT_MINI_GRIGNOTER', 'CONDITIONNEMENT_GRIGNOTER',
                     'CONDITIONNEMENT_SCEAUX_2L', 'CONDITIONNEMENT_RASS',
                     'CONDITIONNEMENT_PRESTIGE_CHAP', 'ETIQUETAGE', 'DATAGE');

DELETE FROM saisies_realisation_poste
 WHERE affectation_id IN (SELECT id FROM affectations_obsoletes);

DELETE FROM lignes_affectation
 WHERE affectation_id IN (SELECT id FROM affectations_obsoletes);

DELETE FROM affectations_journalieres
 WHERE id IN (SELECT id FROM affectations_obsoletes);

DROP TABLE affectations_obsoletes;

ALTER TABLE lignes_affectation
    ADD CONSTRAINT lignes_affectation_poste_check
    CHECK (poste IN (
        'CARAMEL',
        'CROQUETTE',
        'ARACHIDE_ENROBEE',
        'CHIPS_PLANTAIN',
        'CONDITIONNEMENT_MINI_GRIGNOTER',
        'CONDITIONNEMENT_GRIGNOTER',
        'CONDITIONNEMENT_SCEAUX_2L',
        'CONDITIONNEMENT_RASS',
        'CONDITIONNEMENT_PRESTIGE_CHAP',
        'ETIQUETAGE',
        'DATAGE'
    ));

DELETE FROM lignes_dotation
 WHERE dotation_id IN (SELECT id FROM dotations WHERE numero IN
       ('DOT-0001', 'DOT-0002', 'DOT-0003', 'DOT-0004', 'DOT-0005'));

DELETE FROM dotations
 WHERE numero IN ('DOT-0001', 'DOT-0002', 'DOT-0003', 'DOT-0004', 'DOT-0005');
