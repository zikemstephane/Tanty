-- V5__fix_nb_futs_casses_sessions_dosage.sql — Correction de la colonne nb_futs_casses sur les sessions de dosage
-- TANTY Digital ERP — NTFOODS SARL

-- La colonne existe déjà dans certaines bases (créée hors Flyway) mais est NOT NULL sans valeur par défaut,
-- ce qui bloque l'insertion de nouvelles sessions. On met à jour les lignes existantes puis on ajoute une valeur par défaut.
UPDATE sessions_dosage SET nb_futs_casses = 0 WHERE nb_futs_casses IS NULL;
ALTER TABLE IF EXISTS sessions_dosage
    ALTER COLUMN nb_futs_casses SET NOT NULL;
ALTER TABLE IF EXISTS sessions_dosage
    ALTER COLUMN nb_futs_casses SET DEFAULT 0;
