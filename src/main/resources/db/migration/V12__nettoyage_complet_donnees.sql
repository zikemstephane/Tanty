-- ═══════════════════════════════════════════════════════════════════════════
-- V12 — Nettoyage complet des données métier (conservation des utilisateurs)
-- ═══════════════════════════════════════════════════════════════════════════
--
-- Migration **destructive** : vide toutes les tables métier pour repartir d'une
-- base propre, sans historique fictif ni données d'amorçage.
--
-- ── Ce qui est conservé ────────────────────────────────────────────────────
--
--   • utilisateurs          — comptes de connexion (19 matricules seedés)
--   • otp_codes             — codes OTP d'authentification
--   • codes_otp             — codes OTP (table legacy shared.infrastructure.otp)
--   • utilisateur_permissions — permissions rattachées aux utilisateurs
--   • flyway_schema_history — historique des migrations Flyway
--
-- ── Ce qui est supprimé ────────────────────────────────────────────────────
--
-- Tout le reste : catalogue produits, clients, ventes, précommandes, stock,
-- production, comptabilité, contrôle de gestion, RP, tournées, notifications,
-- audit, paramétrages métier (barèmes, règles d'avarie, paramétrages caisse/
-- objectif), sync logs, etc.
--
-- Les seeders non-auth ont été retirés du code : aucune donnée fictive ne sera
-- réinjectée au démarrage. L'AuthSeeder reste pour garantir la présence des
-- comptes de connexion si app.seeding.enabled=true.
--
-- ── Idempotence ─────────────────────────────────────────────────────────────
--
-- TRUNCATE est idempotent : une table vide reste vide. La migration se rejoue
-- sans erreur sur une base déjà nettoyée.
-- ═══════════════════════════════════════════════════════════════════════════

-- TRUNCATE avec CASCADE pour gérer les contraintes de clé étrangère.
-- L'ordre n'a pas d'importance avec CASCADE : PostgreSQL résout les dépendances.

-- Approche dynamique : tronquer toutes les tables du schéma public sauf
-- celles explicitement conservées. Cette méthode est robuste face aux
-- tables ajoutées ou supprimées par les migrations précédentes.

DO $$
DECLARE
    r RECORD;
    kept_tables text[] := ARRAY[
        'utilisateurs',
        'otp_codes',
        'codes_otp',
        'utilisateur_permissions',
        'flyway_schema_history'
    ];
BEGIN
    FOR r IN
        SELECT tablename FROM pg_tables
         WHERE schemaname = 'public'
           AND NOT (tablename = ANY(kept_tables))
         ORDER BY tablename
    LOOP
        EXECUTE format('TRUNCATE TABLE %I CASCADE', r.tablename);
    END LOOP;
END $$;
