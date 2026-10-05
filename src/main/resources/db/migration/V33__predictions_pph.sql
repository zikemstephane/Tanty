-- Prédiction de production par plan PPH : une prédiction de référence par plan, à partir du nombre de fûts.
CREATE TABLE predictions_pph (
    reference_pph varchar(255) PRIMARY KEY,
    nb_futs integer NOT NULL,
    poudre_kg double precision NOT NULL,
    date_calcul timestamp(6) without time zone NOT NULL,
    matricule varchar(255) NOT NULL
);

CREATE TABLE predictions_pph_lignes (
    reference_pph varchar(255) NOT NULL REFERENCES predictions_pph(reference_pph) ON DELETE CASCADE,
    code_produit varchar(50),
    designation varchar(255),
    composition varchar(500),
    poudre_allouee_kg double precision,
    quantite_unites_base integer,
    quantite_cartons integer,
    poids_unitaire_grammes double precision,
    unites_par_carton integer
);

CREATE TABLE predictions_pph_exclusions (
    reference_pph varchar(255) NOT NULL REFERENCES predictions_pph(reference_pph) ON DELETE CASCADE,
    code_produit varchar(50),
    motif varchar(500)
);
