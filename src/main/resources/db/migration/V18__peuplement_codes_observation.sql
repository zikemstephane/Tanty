-- Peuplement initial du référentiel des codes observation tournée.
-- Les codes correspondants à l'ancienne enum InfoClientEnum.
INSERT INTO codes_observation (code, libelle, require_quantity, actif, ordre) VALUES
('Ab', 'Absence responsable', false, true, 1),
('NC', 'Nouveau client', false, true, 2),
('PA', 'Pas d''argent', false, true, 3),
('VPC', 'Vend produits concurrents', false, true, 4),
('NVP', 'Ne vend plus', false, true, 5),
('M', 'Mévente', false, true, 6),
('PR', 'Promotion', false, true, 7),
('F', 'Fermé', false, true, 8),
('S', 'Seau', false, true, 9),
('EV', 'Emballage vide', true, true, 10),
('AV', 'Avaries', true, true, 11),
('AF', 'Affiche', true, true, 12)
ON CONFLICT (code) DO NOTHING;
