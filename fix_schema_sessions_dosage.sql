-- Correction rapide du schéma sessions_dosage
-- Exécuter sur PostgreSQL avant de redémarrer le backend

-- 1. Ajout de la colonne pause
ALTER TABLE IF EXISTS sessions_dosage
    ADD COLUMN IF NOT EXISTS en_pause BOOLEAN NOT NULL DEFAULT FALSE;

-- 2. Correction de nb_futs_casses (colonne NOT NULL sans valeur par défaut)
UPDATE sessions_dosage SET nb_futs_casses = 0 WHERE nb_futs_casses IS NULL;
ALTER TABLE IF EXISTS sessions_dosage
    ALTER COLUMN nb_futs_casses SET NOT NULL;
ALTER TABLE IF EXISTS sessions_dosage
    ALTER COLUMN nb_futs_casses SET DEFAULT 0;
