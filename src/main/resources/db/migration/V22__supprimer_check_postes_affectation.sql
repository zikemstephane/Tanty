-- Suppression des contraintes CHECK liées à l'ancien enum CodePoste,
-- car le poste est désormais une chaîne libre pilotée par le référentiel PosteProduction.
ALTER TABLE saisies_realisation_poste DROP CONSTRAINT IF EXISTS saisies_realisation_poste_poste_check;
ALTER TABLE productions_individuelles DROP CONSTRAINT IF EXISTS productions_individuelles_poste_check;
ALTER TABLE lignes_affectation DROP CONSTRAINT IF EXISTS lignes_affectation_poste_check;
