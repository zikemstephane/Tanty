-- V2__seed_catalogue.sql — Catalogue articles NTFOODS (§7) + utilisateur admin initial
-- TANTY Digital ERP — NTFOODS SARL

-- ─── UTILISATEUR ADMIN INITIAL ─────────────────────────────────────────────────
-- Mot de passe : "admin123" (BCrypt hash — à changer impérativement en production)
INSERT INTO utilisateurs (matricule, nom, prenom, telephone, email, mot_de_passe, role, actif, compte_bloque, tentatives_echec)
VALUES ('ADM-001', 'Système', 'Administrateur', '+237600000000', 'admin@ntfoods.cm',
        '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4UaQv6h1zBfz7WZ7Z7Z7Z7Z7Z7Z', 'ADMIN', TRUE, FALSE, 0)
ON CONFLICT (matricule) DO NOTHING;

-- ─── CATALOGUE ARTICLES (§7) ───────────────────────────────────────────────────
-- Catégories : Farines, Eau, Jus, Snacks, Biscuits, Confiseries

INSERT INTO articles_catalogue (code_article, designation, categorie, unite_mesure, conditionnement, prix_vente_ht, poids_unitaire, actif)
VALUES
-- Farines
('ART-001', 'Farine de blé 1kg', 'FARINES', 'KG', 'Sachet 1kg', 1200, 1.0, TRUE),
('ART-002', 'Farine de blé 5kg', 'FARINES', 'KG', 'Sachet 5kg', 5500, 5.0, TRUE),
('ART-003', 'Farine de blé 10kg', 'FARINES', 'KG', 'Sachet 10kg', 10500, 10.0, TRUE),
('ART-004', 'Farine de maïs 1kg', 'FARINES', 'KG', 'Sachet 1kg', 1000, 1.0, TRUE),
('ART-005', 'Farine de maïs 5kg', 'FARINES', 'KG', 'Sachet 5kg', 4500, 5.0, TRUE),
-- Eau
('ART-010', 'Eau minérale 50cl', 'EAU', 'L', 'Bouteille 50cl', 300, 0.5, TRUE),
('ART-011', 'Eau minérale 1L', 'EAU', 'L', 'Bouteille 1L', 500, 1.0, TRUE),
('ART-012', 'Eau minérale 1.5L', 'EAU', 'L', 'Bouteille 1.5L', 700, 1.5, TRUE),
('ART-013', 'Eau minérale pack 6x1.5L', 'EAU', 'L', 'Pack 6x1.5L', 4000, 9.0, TRUE),
('ART-014', 'Eau minérale bonbonne 19L', 'EAU', 'L', 'Bonbonne 19L', 2500, 19.0, TRUE),
-- Jus
('ART-020', 'Jus d\'orange 25cl', 'JUS', 'L', 'Bouteille 25cl', 400, 0.25, TRUE),
('ART-021', 'Jus d\'orange 1L', 'JUS', 'L', 'Bouteille 1L', 1500, 1.0, TRUE),
('ART-022', 'Jus de mangue 25cl', 'JUS', 'L', 'Bouteille 25cl', 400, 0.25, TRUE),
('ART-023', 'Jus de mangue 1L', 'JUS', 'L', 'Bouteille 1L', 1500, 1.0, TRUE),
('ART-024', 'Jus multifruits 25cl', 'JUS', 'L', 'Bouteille 25cl', 400, 0.25, TRUE),
('ART-025', 'Jus multifruits 1L', 'JUS', 'L', 'Bouteille 1L', 1500, 1.0, TRUE),
-- Snacks
('ART-030', 'Chips nature 50g', 'SNACKS', 'G', 'Sachet 50g', 250, 0.05, TRUE),
('ART-031', 'Chips paprika 50g', 'SNACKS', 'G', 'Sachet 50g', 250, 0.05, TRUE),
('ART-032', 'Croûtons salés 100g', 'SNACKS', 'G', 'Sachet 100g', 400, 0.1, TRUE),
('ART-033', 'Croûtons sucrés 100g', 'SNACKS', 'G', 'Sachet 100g', 400, 0.1, TRUE),
-- Biscuits
('ART-040', 'Biscuits secs 200g', 'BISCUITS', 'G', 'Boîte 200g', 800, 0.2, TRUE),
('ART-041', 'Biscuits chocolat 150g', 'BISCUITS', 'G', 'Boîte 150g', 750, 0.15, TRUE),
('ART-042', 'Biscuits vanille 150g', 'BISCUITS', 'G', 'Boîte 150g', 750, 0.15, TRUE),
('ART-043', 'Biscuits assortis 500g', 'BISCUITS', 'G', 'Boîte 500g', 2200, 0.5, TRUE),
-- Confiseries
('ART-050', 'Bonbons assortis 100g', 'CONFISERIES', 'G', 'Sachet 100g', 300, 0.1, TRUE),
('ART-051', 'Caramels mous 100g', 'CONFISERIES', 'G', 'Sachet 100g', 350, 0.1, TRUE),
('ART-052', 'Pâte de fruits 100g', 'CONFISERIES', 'G', 'Sachet 100g', 400, 0.1, TRUE)
ON CONFLICT (code_article) DO NOTHING;

-- ─── CLIENTS DE DÉMONSTRATION ──────────────────────────────────────────────────
INSERT INTO clients (code_client, nom, telephone, adresse, localite, type, matricule_commercial_referent, actif)
VALUES
('CLI-001', 'Épicerie Bon Marché', '+237691111111', 'Marché Central', 'Douala', 'DISTRIBUTEUR', 'MAT-COM-001', TRUE),
('CLI-002', 'Supermarché Le Panier', '+237692222222', 'Akwa', 'Douala', 'GROSSISTE', 'MAT-COM-001', TRUE),
('CLI-003', 'Boutique Chez Paul', '+237693333333', 'Bonanjo', 'Douala', 'DETAIL', 'MAT-COM-002', TRUE),
('CLI-004', 'Quincaillerie & Alim. La Source', '+237694444444', 'Bépanda', 'Douala', 'SEMI_GROSSISTE', 'MAT-COM-002', TRUE),
('CLI-005', 'Mini- marché du Quartier', '+237695555555', 'Makepe', 'Douala', 'DETAIL', 'MAT-COM-003', TRUE)
ON CONFLICT (code_client) DO NOTHING;
