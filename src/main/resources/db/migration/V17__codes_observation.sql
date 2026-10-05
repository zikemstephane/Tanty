-- Référentiel des codes observation tournée, géré depuis le CMS.
CREATE TABLE IF NOT EXISTS codes_observation (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL,
    require_quantity BOOLEAN NOT NULL DEFAULT FALSE,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    ordre INT NOT NULL DEFAULT 0
);

-- Alignement de la colonne existante sur le nouveau référentiel (String libre).
ALTER TABLE arrets_feuill_route
    ALTER COLUMN code_info_client TYPE VARCHAR(10);
