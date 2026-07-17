-- V4__add_en_pause_sessions_dosage.sql — Ajout de l'état pause sur les sessions de dosage
-- TANTY Digital ERP — NTFOODS SARL

ALTER TABLE IF EXISTS sessions_dosage
    ADD COLUMN IF NOT EXISTS en_pause BOOLEAN NOT NULL DEFAULT FALSE;
