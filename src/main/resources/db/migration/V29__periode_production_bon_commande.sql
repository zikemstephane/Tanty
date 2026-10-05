-- Période d'exécution de la production planifiée à partir d'un BC — remplace
-- à terme la semaine ouvrée figée sur dateCommande (PlanificationProductionService
-- .debutSemaine()) par la durée réelle du BC lorsqu'elle est renseignée.
-- Nullable : un BC existant ou créé sans cette donnée continue à utiliser le
-- repli sur la semaine ouvrée de dateCommande, sans aucun changement de
-- comportement (cf. PlanificationProductionService.resoudrePeriode()).

ALTER TABLE bons_commande ADD COLUMN date_debut_production DATE;
ALTER TABLE bons_commande ADD COLUMN date_fin_production DATE;

ALTER TABLE bons_commande ADD CONSTRAINT bons_commande_periode_production_check
    CHECK (
        (date_debut_production IS NULL AND date_fin_production IS NULL)
        OR (date_debut_production IS NOT NULL AND date_fin_production IS NOT NULL
            AND date_fin_production >= date_debut_production)
    );
