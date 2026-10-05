-- V14 — Observations de validation des réceptions (18/08/2026)
--
-- L'écran de validation demandait au validateur des observations et un « code
-- de confirmation ». Le serveur n'offrait aucun champ pour les recevoir : les
-- deux endpoints n'acceptaient pas de corps. Ce qui était saisi était perdu à
-- l'envoi.
--
-- Les observations sont désormais conservées, avec l'horodatage de l'étape à
-- laquelle elles ont été écrites — date_modification ne retenant que la
-- dernière écriture, elle ne permettait pas d'attribuer une observation à son
-- moment.
--
-- Le code de confirmation n'est PAS repris. Dans la version de référence il
-- n'était comparé à rien : seulement exigé non vide au-delà d'un seuil d'écart,
-- puis jeté. Le système ne comporte pas de code secret par fonctionnalité.

ALTER TABLE receptions
    ADD COLUMN IF NOT EXISTS notes_validation1 VARCHAR(1000),
    ADD COLUMN IF NOT EXISTS notes_validation2 VARCHAR(1000),
    ADD COLUMN IF NOT EXISTS date_validation1  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS date_validation2  TIMESTAMP;

COMMENT ON COLUMN receptions.notes_validation1 IS
    'Observations du validateur de 1ʳᵉ étape (vérification physique). Facultatif.';
COMMENT ON COLUMN receptions.notes_validation2 IS
    'Observations du validateur de 2ᵉ étape (contrôle de second niveau). Facultatif.';
COMMENT ON COLUMN receptions.date_validation1 IS
    'Horodatage de la 1ʳᵉ validation. NULL tant qu''elle n''a pas eu lieu.';
COMMENT ON COLUMN receptions.date_validation2 IS
    'Horodatage de la 2ᵉ validation. NULL tant qu''elle n''a pas eu lieu.';

-- Les réceptions déjà validées avant cette migration n'ont pas d'observation :
-- il n'en a jamais été enregistré. On ne rétro-date pas non plus leurs
-- horodatages depuis date_modification, qui ne dit pas de quelle étape il
-- s'agit — inventer une date serait pire que l'absence.
