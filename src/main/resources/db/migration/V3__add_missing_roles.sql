-- V3__add_missing_roles.sql — Ajout des rôles manquants au type enum role_utilisateur
-- TANTY Digital ERP — NTFOODS SARL

-- Rôles présents dans Role.java mais absents de l'enum PostgreSQL (V1)
ALTER TYPE role_utilisateur ADD VALUE IF NOT EXISTS 'CHEF_MACHINISTE';
ALTER TYPE role_utilisateur ADD VALUE IF NOT EXISTS 'MACHINISTE';
ALTER TYPE role_utilisateur ADD VALUE IF NOT EXISTS 'AGENT_DOSEUR';
ALTER TYPE role_utilisateur ADD VALUE IF NOT EXISTS 'CHARGEE_RP';
