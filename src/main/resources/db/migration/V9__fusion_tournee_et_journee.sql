-- ═══════════════════════════════════════════════════════════════════════════
-- V9 — La tournée n'a plus de cycle de vie propre
-- ═══════════════════════════════════════════════════════════════════════════
--
-- La feuille de route portait son propre cycle — PLANIFIE, EN_COURS, CLOTURE,
-- VERROUILLE — qui doublait celui de la session commerciale sans jamais lui
-- être raccordé. Deux conséquences, constatées dans le code : on pouvait viser
-- des arrêts sans journée ouverte, et clore sa journée sur une tournée à moitié
-- saisie. `VERROUILLE` n'était d'ailleurs atteignable par aucun chemin
-- applicatif — ni endpoint, ni écouteur d'événement — seulement par un seeder.
--
-- L'état de la journée est désormais celui de la session, et `numero_session`
-- dit laquelle. Déclarer la fin de tournée ferme les deux à la fois, et refuse
-- de le faire tant qu'un client reste sans constat.
--
-- Le relevé GPS de l'arrêt disparaît avec la saisie qui le produisait : il
-- obligeait le commercial à attendre un point satellite avant chaque marquage,
-- pour un écart au client que rien ne lisait — ni tableau de bord, ni rapport de
-- tournée, tous deux vérifiés. Le GPS de la *vente* n'est pas concerné : c'est
-- un autre champ, sur un autre agrégat.
--
-- `VISE` devient `VISITE` et `NON_VISE` devient `NON_VISITE`. Ce n'est pas un
-- habillage : `VISE` signifiait « une vente a été réalisée », et exigeait un
-- numéro de vente. Le constat porte maintenant sur le passage — le commercial
-- est passé, ou il n'a pas pu — et la vente s'y rattache d'elle-même quand il y
-- en a une. Garder le mot avec un sens neuf aurait laissé deux lectures
-- possibles d'une même colonne.
--
-- Vérifié avant écriture : `feuills_route` et `arrets_feuill_route` sont vides.
-- Les UPDATE ci-dessous ne portent donc sur aucune ligne réelle ; ils sont là
-- pour qu'une base restaurée d'avant cette migration passe sans intervention.
--
-- Idempotence : IF EXISTS / IF NOT EXISTS partout.
-- ═══════════════════════════════════════════════════════════════════════════

-- ── Arrêts : le constat porte sur le passage ────────────────────────────────

ALTER TABLE arrets_feuill_route DROP CONSTRAINT IF EXISTS arrets_feuill_route_statut_check;

UPDATE arrets_feuill_route SET statut = 'VISITE'     WHERE statut = 'VISE';
UPDATE arrets_feuill_route SET statut = 'NON_VISITE' WHERE statut = 'NON_VISE';

-- ANNULE n'était produit par aucun chemin applicatif ; s'il en existe malgré
-- tout, l'arrêt redevient à trancher plutôt que de bloquer la migration.
UPDATE arrets_feuill_route SET statut = 'PLANIFIE' WHERE statut = 'ANNULE';

ALTER TABLE arrets_feuill_route
    ADD CONSTRAINT arrets_feuill_route_statut_check
    CHECK (statut IN ('PLANIFIE', 'VISITE', 'NON_VISITE'));

ALTER TABLE arrets_feuill_route DROP COLUMN IF EXISTS coordonneesgps;
ALTER TABLE arrets_feuill_route DROP COLUMN IF EXISTS latitude_visite;
ALTER TABLE arrets_feuill_route DROP COLUMN IF EXISTS longitude_visite;
ALTER TABLE arrets_feuill_route DROP COLUMN IF EXISTS distance_ecart_metres;

-- ── Tournée : plus d'état propre, mais une journée de rattachement ──────────

ALTER TABLE feuills_route ADD COLUMN IF NOT EXISTS numero_session VARCHAR(255);

-- Rattrapage pour une base non vide : on relie chaque tournée à la journée du
-- même commercial à la même date. Sans correspondance, la tournée est orpheline
-- et le NOT NULL ci-dessous la ferait échouer — d'où le repli explicite, qui
-- laisse une trace lisible plutôt qu'un plantage de migration.
UPDATE feuills_route f
   SET numero_session = COALESCE(
       (SELECT s.numero_session
          FROM sessions_commercial s
         WHERE s.matricule_commercial = f.matricule_commercial
           AND s.date_effet = f.date
         LIMIT 1),
       'SESSION-INCONNUE-' || f.numero_feuill_route)
 WHERE numero_session IS NULL;

ALTER TABLE feuills_route ALTER COLUMN numero_session SET NOT NULL;

ALTER TABLE feuills_route DROP CONSTRAINT IF EXISTS feuills_route_statut_check;
ALTER TABLE feuills_route DROP COLUMN IF EXISTS statut;
ALTER TABLE feuills_route DROP COLUMN IF EXISTS matricule_cloture;
ALTER TABLE feuills_route DROP COLUMN IF EXISTS heure_cloture;
