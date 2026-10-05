-- V15 — Lots de stock : traçabilité et péremption (18/08/2026)
--
-- Seul manque fonctionnel réel relevé en confrontant le module Stock livré par
-- le collègue à la baseline : le suivi des lots par date de péremption.
-- Toutes les autres familles d'appels avaient déjà leur contrepartie en
-- français (niveaux, entrepôts, seuils, mouvements, alertes, inventaires,
-- rapports) ; celle-ci n'existait nulle part.
--
-- Ce que la table porte, et ce qu'elle ne porte pas
-- ------------------------------------------------
-- Un lot rattache des marchandises à leur arrivage et à leur date limite.
-- Il ne fait pas autorité sur les quantités détenues : celle-ci reste à
-- niveaux_stock, alimentée par les mouvements. Le lot dit d'où vient la
-- marchandise ; le mouvement dit combien il en reste. Deux tables qui
-- prétendraient au même chiffre finiraient par diverger, et la seconde à
-- s'écarter serait celle que personne ne regarde.

CREATE TABLE IF NOT EXISTS lots_stock (
    id                     BIGSERIAL PRIMARY KEY,
    numero_lot             VARCHAR(40)  NOT NULL,
    code_produit           VARCHAR(50)  NOT NULL,
    sku                    VARCHAR(60),
    designation            VARCHAR(200),
    code_fournisseur       VARCHAR(50),
    code_entrepot          VARCHAR(60)  NOT NULL,
    date_fabrication       DATE,
    date_peremption        DATE,
    quantite_initiale      BIGINT       NOT NULL,
    quantite_restante      BIGINT       NOT NULL,
    statut                 VARCHAR(20)  NOT NULL,
    observations           VARCHAR(1000),
    date_creation          TIMESTAMP    NOT NULL,
    date_dernier_mouvement TIMESTAMP,
    version                BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT uq_lot_numero UNIQUE (numero_lot),
    CONSTRAINT ck_lot_quantite_initiale  CHECK (quantite_initiale > 0),
    CONSTRAINT ck_lot_quantite_restante  CHECK (quantite_restante >= 0
                                                AND quantite_restante <= quantite_initiale),
    -- Une péremption antérieure à la fabrication est une saisie fautive, pas un
    -- cas métier. On la refuse ici aussi, et pas seulement dans le domaine :
    -- une donnée impossible ne doit pas pouvoir entrer par une autre porte.
    CONSTRAINT ck_lot_dates CHECK (date_fabrication IS NULL
                                   OR date_peremption IS NULL
                                   OR date_peremption >= date_fabrication),
    CONSTRAINT ck_lot_statut CHECK (statut IN
        ('DISPONIBLE', 'RESERVE', 'QUARANTAINE', 'PERIME', 'EPUISE', 'RETIRE'))
);

CREATE INDEX IF NOT EXISTS idx_lot_produit    ON lots_stock (code_produit);
CREATE INDEX IF NOT EXISTS idx_lot_sku        ON lots_stock (sku);
CREATE INDEX IF NOT EXISTS idx_lot_entrepot   ON lots_stock (code_entrepot);
CREATE INDEX IF NOT EXISTS idx_lot_statut     ON lots_stock (statut);

-- La sortie se fait au plus proche de la péremption : cet index sert la
-- requête de prélèvement, qui cherche les lots consommables d'un SKU dans un
-- entrepôt, triés par date limite.
CREATE INDEX IF NOT EXISTS idx_lot_peremption ON lots_stock (date_peremption);
CREATE INDEX IF NOT EXISTS idx_lot_sortie_fefo
    ON lots_stock (sku, code_entrepot, date_peremption, date_creation);

COMMENT ON TABLE  lots_stock IS
    'Lots de stock — traçabilité de l''arrivage et date de péremption. Les quantités détenues font foi dans niveaux_stock.';
COMMENT ON COLUMN lots_stock.quantite_restante IS
    'Reste du lot après prélèvements. Sert la traçabilité, pas le calcul du stock disponible.';
COMMENT ON COLUMN lots_stock.statut IS
    'DISPONIBLE, RESERVE, QUARANTAINE, PERIME, EPUISE, RETIRE. Seuls DISPONIBLE et RESERVE autorisent un prélèvement.';
COMMENT ON COLUMN lots_stock.date_peremption IS
    'Dernier jour de validité inclus : le jour même, le lot est déjà hors délai pour la revente.';
