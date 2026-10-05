-- Les colonnes poste des tables d'affectation passent de l'enum CodePoste à une chaîne
-- afin d'être pilotées par le référentiel PosteProduction.
ALTER TABLE saisies_realisation_poste ALTER COLUMN poste TYPE VARCHAR(60);
ALTER TABLE productions_individuelles ALTER COLUMN poste TYPE VARCHAR(60);
