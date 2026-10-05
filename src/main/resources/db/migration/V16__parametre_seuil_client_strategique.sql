-- Paramètre seuil client stratégique
-- Anciennement codé en dur dans le mobile (500 000 FCFA).
INSERT INTO parametres_systeme (cle, valeur_numerique, description, modifie_par, horodatage)
SELECT 'SEUIL_CLIENT_STRATEGIQUE', 500000.0,
       'Seuil total d''achats (FCFA) au-delà duquel un client est considéré comme stratégique',
       'SYSTEME', NOW()
WHERE NOT EXISTS (SELECT 1 FROM parametres_systeme WHERE cle = 'SEUIL_CLIENT_STRATEGIQUE');
