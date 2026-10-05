-- ═══════════════════════════════════════════════════════════════════════════
-- V3 — Retrait des campagnes marketing et de la prospection
-- ═══════════════════════════════════════════════════════════════════════════
--
-- Les campagnes marketing sur les produits et la prospection sont sorties du
-- périmètre du projet. Le code correspondant a été retiré (agrégats, use cases,
-- endpoints, écrans web et mobile) ; cette migration retire les tables devenues
-- orphelines.
--
-- Ce que cette migration détruit, et qui ne sera pas récupérable par un
-- « rollback » Flyway — il n'en existe pas ici :
--   prospects            10 lignes de jeu d'essai
--   campagnes_marketing   5 lignes de jeu d'essai
--   rp_campagnes          0 ligne
--
-- Une sauvegarde de ces trois tables (structure et données) a été prise avant
-- exécution :
--   _backup_avant_retrait_campagnes_prospects_20260816/db/tables_supprimees.sql
--
-- Aucune clé étrangère ne pointe vers ces tables ni n'en part : la vérification
-- a été faite sur information_schema avant écriture. Le DROP est donc isolé et
-- ne cascade sur rien.
--
-- V1 (schéma de référence) crée toujours ces tables : c'est le reflet fidèle de
-- l'état du schéma à la date de la référence. Sur une base neuve, elles sont
-- donc créées par V1 puis retirées ici — c'est la lecture chronologique
-- attendue d'une suite de migrations, pas une incohérence.
--
-- Idempotence : IF EXISTS sur chaque table, la migration se rejoue sans erreur.
-- ═══════════════════════════════════════════════════════════════════════════

DROP TABLE IF EXISTS public.prospects;
DROP TABLE IF EXISTS public.campagnes_marketing;
DROP TABLE IF EXISTS public.rp_campagnes;

-- Note sur clients.type : deux clients réels portent encore le segment
-- « PROSPECT ». Ce segment appartient à la table clients, pas à la prospection
-- supprimée ici ; il n'est donc pas touché. Le requalifier relève d'une décision
-- métier et non de cette migration.
