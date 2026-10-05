-- Les matières de dosage sont stockées sous leur code canonique : MAIS, SOJA, ARACHIDE.
-- Les saisies manuelles écrivaient le libellé brut (« Maïs », « Soja »), que le tableau de bord
-- ne reconnaissait pas. On aligne les lignes existantes sur la même règle que le domaine :
-- sans accents, sans espaces superflus, en majuscules.
UPDATE dosage_matieres
SET type_matiere = UPPER(TRANSLATE(TRIM(type_matiere),
    'àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ',
    'aaaeeeeiioouuucAAAEEEEIIOOUUUC'))
WHERE type_matiere IS NOT NULL;
