-- Les identifiants de machines sont normalisés (sans espaces, en majuscules) à l'écriture.
-- On aligne les lignes existantes, puis on supprime les doublons qui en résultent par session.
UPDATE dosage_machines
SET machine_id = UPPER(TRIM(machine_id))
WHERE machine_id IS NOT NULL;

DELETE FROM dosage_machines a
USING dosage_machines b
WHERE a.session_id = b.session_id
  AND a.machine_id = b.machine_id
  AND a.ctid > b.ctid;
