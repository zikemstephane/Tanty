-- ===========================================================================
-- V4 — Requalification du segment PROSPECT
-- ===========================================================================
--
-- V3 a retiré la prospection. Il subsistait la valeur PROSPECT dans le segment
-- commercial des clients (colonne clients.type, énumération TypeClient), portée
-- par deux clients réels :
--
--   CLI-1783334554537  DoVV         Simbock
--   CLI-1783339028159  Santa Lucia  Mokolo
--
-- Ce ne sont pas des enregistrements de prospection : ce sont des clients créés
-- en tournée depuis le mobile, actifs, rattachés au commercial COM-001. Aucune
-- vente ne leur est attachée à ce jour (vérifié avant écriture), donc aucun
-- montant, aucune remise et aucune dette ne dépendent de leur segment.
--
-- Ils sont requalifiés en DETAIL. Ce n'est pas un choix arbitraire : DETAIL est
-- déjà le repli appliqué par le code à un segment non reconnu, côté serveur
-- comme côté mobile, et les deux sont de petits commerces de quartier.
--
-- Sans cette requalification, l'application ne démarrerait plus sur ces deux
-- lignes : TypeClient.valueOf("PROSPECT") lèverait une exception au chargement.
--
-- Conséquence sur la tarification : aucune promotion ne ciblait le segment
-- PROSPECT (segments utilisés en base : GROSSISTE, SEMI_GROSSISTE, DETAIL).
-- Ces deux clients deviennent en revanche éligibles aux promotions DETAIL —
-- c'est la conséquence assumée de leur requalification.
--
-- Sauvegarde de l'état antérieur :
--   _backup_avant_retrait_campagnes_prospects_20260816/db/
--
-- Idempotence : le WHERE ne retient que les lignes restantes ; un second
-- passage ne trouve plus rien et ne fait rien.
-- ===========================================================================

UPDATE clients SET type = 'DETAIL' WHERE type = 'PROSPECT';

-- Les promotions ne référencent pas ce segment, mais la colonne existe et
-- pourrait le porter : on la met en cohérence par sécurité plutôt que de la
-- laisser désigner une valeur que l'énumération ne connaît plus.
UPDATE promotions SET segment_cible = 'DETAIL' WHERE segment_cible = 'PROSPECT';

-- ── Contraintes CHECK devenues incohérentes avec les énumérations ────────────
--
-- Ces contraintes ont été générées par Hibernate à partir des énumérations Java.
-- Les valeurs retirées (PROSPECT dans les segments et la synchro,
-- COMMERCIAL_PROSPECT_GERER et COMMERCIALISATION_CAMPAGNE_GERER dans les
-- permissions) y figurent encore : la base autoriserait une valeur que le code
-- refuserait au chargement.
--
-- `ddl-auto=validate` ne vérifie pas les contraintes CHECK : sans cette section,
-- une base migrée et une base créée par Hibernate divergeraient en silence.
-- Aucune ligne existante n'est concernée (vérifié : 0 permission, 0 sync_log,
-- 0 grille et 0 promotion sur les valeurs retirées).

ALTER TABLE clients DROP CONSTRAINT IF EXISTS clients_type_check;
ALTER TABLE clients ADD CONSTRAINT clients_type_check
    CHECK (type IN ('DISTRIBUTEUR', 'DETAIL', 'GROSSISTE', 'SEMI_GROSSISTE', 'REVENDEUR'));

ALTER TABLE grilles_tarifaires DROP CONSTRAINT IF EXISTS grilles_tarifaires_segment_check;
ALTER TABLE grilles_tarifaires ADD CONSTRAINT grilles_tarifaires_segment_check
    CHECK (segment IN ('DISTRIBUTEUR', 'DETAIL', 'GROSSISTE', 'SEMI_GROSSISTE', 'REVENDEUR'));

ALTER TABLE promotions DROP CONSTRAINT IF EXISTS promotions_segment_cible_check;
ALTER TABLE promotions ADD CONSTRAINT promotions_segment_cible_check
    CHECK (segment_cible IN ('DISTRIBUTEUR', 'DETAIL', 'GROSSISTE', 'SEMI_GROSSISTE', 'REVENDEUR'));

ALTER TABLE sync_logs DROP CONSTRAINT IF EXISTS sync_logs_entity_type_check;
ALTER TABLE sync_logs ADD CONSTRAINT sync_logs_entity_type_check
    CHECK (entity_type IN ('VENTE', 'PRECOMMANDE', 'FEUILL_ROUTE', 'ARRET', 'CLOTURE_JOURNEE'));

-- Liste engendrée depuis l'énumération Permission (97 valeurs) plutôt que
-- recopiée à la main : une transcription approximative aurait bloqué l'octroi
-- d'un droit sans que rien ne le signale.
ALTER TABLE utilisateur_permissions DROP CONSTRAINT IF EXISTS utilisateur_permissions_permission_check;
ALTER TABLE utilisateur_permissions ADD CONSTRAINT utilisateur_permissions_permission_check
    CHECK (permission IN (
        'STOCK_PRODUIT_CREER', 'STOCK_PRODUIT_MODIFIER', 'STOCK_PRODUIT_CONSULTER',
        'STOCK_PRODUIT_DESACTIVER', 'STOCK_CONDITIONNEMENT_GERER', 'STOCK_FOURNISSEUR_GERER',
        'STOCK_ENTREPOT_GERER', 'STOCK_BON_COMMANDE_CREER', 'STOCK_BON_COMMANDE_VALIDER',
        'STOCK_RECEPTION_GERER', 'STOCK_TRANSFERT_GERER', 'STOCK_DOTATION_GERER',
        'STOCK_RETOUR_DECLARER', 'STOCK_RETOUR_GERER', 'STOCK_NIVEAU_CONSULTER',
        'STOCK_MOUVEMENT_CONSULTER', 'STOCK_ALERTE_GERER', 'STOCK_INVENTAIRE_GERER',
        'STOCK_SEUIL_GERER', 'STOCK_RAPPORT_CONSULTER', 'PRODUCTION_PPH_GERER',
        'PRODUCTION_OF_CREER', 'PRODUCTION_OF_VALIDER', 'PRODUCTION_OF_CONSULTER',
        'PRODUCTION_LOT_GERER', 'PRODUCTION_LOT_VALIDER_STOCK',
        'PRODUCTION_EXPRESSION_BESOIN_GERER', 'PRODUCTION_FICHE_PRODUIT_GERER',
        'AGENT_PRODUCTION', 'CHEF_MACHINISTE', 'PRODUCTION_DOSAGE_GERER',
        'AGENT_PRODUCTION', 'COMMERCIAL_VENTE_CREER',
        'COMMERCIAL_VENTE_CONSULTER', 'COMMERCIAL_VENTE_FINALISER', 'COMMERCIAL_VENTE_VALIDER',
        'COMMERCIAL_PRECOMMANDE_GERER', 'COMMERCIAL_PRECOMMANDE_ARBITRER',
        'COMPTABILITE_BAREME_PRIME_GERER', 'COMPTABILITE_PARAMETRAGE_CAISSE_GERER',
        'COMPTABILITE_PARAMETRAGE_OBJECTIF_GERER', 'COMMERCIAL_REGLES_AVARIE_GERER',
        'STOCK_SESSION_DECLARER_FIN_TOURNEE', 'STOCK_SESSION_VALIDER_SECRETAIRE',
        'STOCK_SESSION_VALIDER_COMPTABLE', 'COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE',
        'COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE', 'COMMERCIAL_COMMANDE_DISTANCE_GERER',
        'COMMERCIAL_FEUILLE_ROUTE_GERER', 'COMMERCIAL_CARBURANT_GERER',
        'COMMERCIAL_RECOUVREMENT_GERER', 'COMMERCIAL_FACTURE_AVOIR_GERER',
        'COMMERCIAL_PRIX_EXCEPTIONNEL_GERER', 'COMMERCIAL_CLIENT_GERER',
        'COMMERCIAL_GRILLE_TARIFAIRE_GERER', 'COMMERCIAL_ZONE_VENTE_GERER',
        'COMMERCIAL_VEHICULE_GERER', 'COMMERCIAL_TERMINAL_GERER', 'COMPTABILITE_FACTURE_GERER',
        'COMPTABILITE_CAISSE_GERER', 'COMPTABILITE_DECAISSEMENT_PROPOSER',
        'COMPTABILITE_DECAISSEMENT_VALIDER_CG', 'COMPTABILITE_DECAISSEMENT_VALIDER_DG',
        'COMPTABILITE_DECAISSEMENT_EXECUTER', 'COMPTABILITE_OBJECTIF_GERER',
        'COMPTABILITE_PRIME_GERER', 'COMPTABILITE_VERSEMENT_GERER',
        'COMPTABILITE_VERSEMENT_VALIDER', 'COMPTABILITE_CAISSE_CONSULTER',
        'COMPTABILITE_DASHBOARD_CONSULTER', 'COMPTABILITE_DECAISSEMENT_CONSULTER',
        'COMPTABILITE_FACTURE_CONSULTER', 'COMPTABILITE_OBJECTIF_CONSULTER',
        'COMPTABILITE_PRIME_CONSULTER', 'COMPTABILITE_RAPPORT_CONSULTER',
        'COMPTABILITE_VERSEMENT_CONSULTER', 'COMMERCIALISATION_PROMOTION_GERER',
        'CONTROLE_DASHBOARD_CONSULTER', 'DG_DASHBOARD_CONSULTER', 'DG_PARAMETRES_GERER',
        'DG_VALIDATION_RESOUDRE', 'IA_ASSISTANT_UTILISER', 'RP_DASHBOARD_CONSULTER',
        'SECRETARIAT_DASHBOARD_CONSULTER', 'SYNC_PUSH_PULL', 'SYNC_HISTORIQUE_CONSULTER',
        'AUTH_USER_CREER', 'AUTH_USER_MODIFIER', 'AUTH_USER_CONSULTER', 'AUTH_USER_DESACTIVER',
        'AUTH_USER_DEBLOQUER', 'AUTH_USER_CHANGER_ROLE', 'AUTH_USER_RESET_PASSWORD',
        'AUTH_PERMISSION_GERER', 'AUTH_PERMISSION_CONSULTER', 'RH_EMPLOYE_GERER',
        'RH_EMPLOYE_CONSULTER'
    ));
