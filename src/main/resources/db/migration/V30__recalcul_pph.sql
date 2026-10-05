-- Signale qu'un PPH porte une répartition journalière ajustée manuellement
-- par le Responsable de Salle (AjusterRepartitionPPHUseCase), pour que
-- RecalculerPPHUseCase puisse avertir explicitement avant de l'écraser par
-- une répartition recalculée depuis le BC/catalogue.

ALTER TABLE plans_production_hebdomadaires
    ADD COLUMN repartition_ajustee_manuellement BOOLEAN NOT NULL DEFAULT FALSE;
