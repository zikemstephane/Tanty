-- Corrige un oubli du schéma initial (V1) : RESPONSABLE_SALLE figure dans
-- employes_role_check mais avait été omis de utilisateurs_role_check, alors
-- que c'est un rôle authentifiable à part entière (cf. Role.java). Toute
-- inscription d'un utilisateur RESPONSABLE_SALLE échouait donc en base.
ALTER TABLE utilisateurs DROP CONSTRAINT IF EXISTS utilisateurs_role_check;
ALTER TABLE utilisateurs ADD CONSTRAINT utilisateurs_role_check
    CHECK (((role)::text = ANY ((ARRAY['DIRECTEUR_GENERAL'::character varying, 'DIRECTEUR_COMMERCIAL'::character varying, 'GESTIONNAIRE_STOCK'::character varying, 'CHEF_PRODUCTION'::character varying, 'RESPONSABLE_SALLE'::character varying, 'AGENT_PRODUCTION'::character varying, 'CHEF_MACHINISTE'::character varying, 'MACHINISTE'::character varying, 'AGENT_DOSEUR'::character varying, 'CONTROLEUR_GENERAL'::character varying, 'COMMERCIAL'::character varying, 'SECRETAIRE'::character varying, 'COMPTABLE'::character varying, 'CHARGEE_RP'::character varying, 'ADMIN'::character varying])::text[])));
