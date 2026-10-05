-- Référentiel des postes de production : effectifs et objectifs configurables depuis le CMS.
CREATE TABLE IF NOT EXISTS postes_production (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL,
    effectif_reference INT NOT NULL,
    production_par_heure DOUBLE PRECISION NOT NULL,
    production_journaliere DOUBLE PRECISION NOT NULL,
    unite VARCHAR(20) NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    ordre INT NOT NULL DEFAULT 0,
    categories_metier VARCHAR(500)
);
