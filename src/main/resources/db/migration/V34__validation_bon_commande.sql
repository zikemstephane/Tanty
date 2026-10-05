-- Trace de la validation d'un bon de commande : qui l'a validé et quand.
-- Le statut seul ne suffit pas (un BC annulé depuis le brouillon n'a jamais été validé).

ALTER TABLE bons_commande
    ADD COLUMN IF NOT EXISTS date_validation TIMESTAMP(6),
    ADD COLUMN IF NOT EXISTS matricule_validateur VARCHAR(20);

-- Reprise des BC déjà validés : date approximative = dernière modification.
-- Les BC annulés ne sont pas repris : on ne sait pas s'ils ont été validés avant d'être annulés.
-- Le validateur n'est pas reconstituable : laissé vide (affiché « — »).
UPDATE bons_commande
   SET date_validation = COALESCE(date_modification, date_creation)
 WHERE statut IN ('VALIDE', 'ENVOYE', 'RECU_PARTIEL', 'RECU_TOTAL', 'CLOTURE')
   AND date_validation IS NULL;
