-- Ajoute EN_PAUSE au cycle de vie de l'Ordre de Fabrication : un OF en cours
-- peut être suspendu sans être annulé, puis repris. Permet aussi à l'agent
-- de production de renvoyer un OF non encore avancé au statut PLANIFIE
-- (« brouillon ») ou de le marquer HONORE (« terminé ») manuellement.
ALTER TABLE ordres_fabrication DROP CONSTRAINT IF EXISTS ordres_fabrication_statut_check;
ALTER TABLE ordres_fabrication ADD CONSTRAINT ordres_fabrication_statut_check
    CHECK (((statut)::text = ANY ((ARRAY['PLANIFIE'::character varying, 'EN_COURS'::character varying, 'EN_PAUSE'::character varying, 'HONORE'::character varying, 'ANNULE'::character varying])::text[])));
