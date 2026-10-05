-- La colonne code doit pouvoir accueillir les noms longs de l'enum originale.
ALTER TABLE postes_production ALTER COLUMN code TYPE VARCHAR(50);

-- Peuplement initial correspondant aux noms de l'ancienne enum CodePoste,
-- pour rester compatible avec les appels Angular/Flutter existants.
INSERT INTO postes_production (code, libelle, effectif_reference, production_par_heure, production_journaliere, unite, actif, ordre, categories_metier) VALUES
('CARAMEL', 'Caramel', 1, 8.2, 65.6, 'KG', true, 1, 'TANTY_A_GRIGNOTER'),
('CROQUETTE', 'Croquette', 1, 3.6, 28.8, 'KG', true, 2, 'TANTY_A_GRIGNOTER'),
('ARACHIDE_ENROBEE', 'Arachide enrobée', 1, 6.1, 48.8, 'KG', true, 3, 'TANTY_A_GRIGNOTER'),
('CHIPS_PLANTAIN', 'Chips plantain', 2, 7.5, 60.0, 'KG', true, 4, 'TANTY_A_GRIGNOTER'),
('CONDITIONNEMENT_MINI_GRIGNOTER', 'Conditionnement mini à grignoter (Chips)', 3, 30.0, 2400.0, 'SACHET', true, 5, 'TANTY_A_GRIGNOTER'),
('CONDITIONNEMENT_GRIGNOTER', 'Conditionnement à grignoter', 2, 200.0, 1600.0, 'SACHET', true, 6, 'TANTY_A_GRIGNOTER'),
('CONDITIONNEMENT_SCEAUX_2L', 'Conditionnement sceaux 2L', 4, 100.0, 800.0, 'SCEAU', true, 7, 'TANTY_BOUILLIE_DE_SOJA'),
('CONDITIONNEMENT_RASS', 'Conditionnement RASS', 2, 625.0, 5000.0, 'SACHET', true, 8, 'TANTY_CHOCOLAT'),
('CONDITIONNEMENT_PRESTIGE_CHAP', 'Conditionnement prestige/chap.', 2, 50.0, 400.0, 'ETUI', true, 9, 'TANTY_CHOCOLAT'),
('ETIQUETAGE', 'Étiquetage', 1, 225.0, 1800.0, 'SCEAU', true, 10, 'TANTY_BOUILLIE_DE_SOJA,TANTY_CHOCOLAT,TANTY_A_GRIGNOTER,INGREDIENTS'),
('DATAGE', 'Datage', 1, 313.0, 2500.0, 'ETIQUETTE', true, 11, 'TANTY_BOUILLIE_DE_SOJA,TANTY_CHOCOLAT,TANTY_A_GRIGNOTER,INGREDIENTS')
ON CONFLICT (code) DO NOTHING;
