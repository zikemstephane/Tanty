-- V1__init_schema.sql — Migration initiale : schéma de base + tables fondamentales
-- TANTY Digital ERP — NTFOODS SARL

-- ─── TYPES ENUM ────────────────────────────────────────────────────────────────
CREATE TYPE IF NOT EXISTS role_utilisateur AS ENUM (
    'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'GESTIONNAIRE_STOCK',
    'CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'CONTROLEUR_GENERAL',
    'COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'ADMIN'
);

-- ─── TABLE : utilisateurs ─────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS utilisateurs (
    id              BIGSERIAL PRIMARY KEY,
    matricule       VARCHAR(20)  NOT NULL UNIQUE,
    nom             VARCHAR(100) NOT NULL,
    prenom          VARCHAR(100) NOT NULL,
    telephone       VARCHAR(20),
    email           VARCHAR(150),
    mot_de_passe    VARCHAR(255) NOT NULL,
    role            role_utilisateur NOT NULL,
    actif           BOOLEAN      NOT NULL DEFAULT TRUE,
    compte_bloque   BOOLEAN      NOT NULL DEFAULT FALSE,
    tentatives_echec INTEGER     NOT NULL DEFAULT 0,
    date_creation   TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification TIMESTAMP,
    version         BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : clients ──────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS clients (
    id                          BIGSERIAL PRIMARY KEY,
    code_client                 VARCHAR(20)  NOT NULL UNIQUE,
    nom                         VARCHAR(150) NOT NULL,
    telephone                   VARCHAR(20),
    adresse                     VARCHAR(255),
    localite                    VARCHAR(100),
    type                        VARCHAR(30)  NOT NULL,
    matricule_commercial_referent VARCHAR(20),
    actif                       BOOLEAN      NOT NULL DEFAULT TRUE,
    date_creation               TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification           TIMESTAMP,
    version                     BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : articles_catalogue (§7) ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS articles_catalogue (
    id              BIGSERIAL PRIMARY KEY,
    code_article    VARCHAR(20)  NOT NULL UNIQUE,
    designation     VARCHAR(200) NOT NULL,
    categorie       VARCHAR(50)  NOT NULL,
    unite_mesure    VARCHAR(20)  NOT NULL,
    conditionnement VARCHAR(50)  NOT NULL,
    prix_vente_ht   DOUBLE PRECISION NOT NULL,
    poids_unitaire  DOUBLE PRECISION,
    actif           BOOLEAN      NOT NULL DEFAULT TRUE,
    date_creation   TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification TIMESTAMP,
    version         BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : ventes ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS ventes (
    id              BIGSERIAL PRIMARY KEY,
    numero_vente    VARCHAR(30)  NOT NULL UNIQUE,
    matricule_commercial VARCHAR(20) NOT NULL,
    code_client     VARCHAR(20)  NOT NULL,
    date            DATE         NOT NULL,
    mode_reglement  VARCHAR(20)  NOT NULL,
    type_vente      VARCHAR(20)  NOT NULL DEFAULT 'COMPTANT',
    statut          VARCHAR(20)  NOT NULL DEFAULT 'EN_COURS',
    sync_status     VARCHAR(20)  NOT NULL DEFAULT 'SYNCED',
    coordonnees_gps VARCHAR(100),
    id_feuill_route VARCHAR(50),
    observations    TEXT,
    date_creation   TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification TIMESTAMP,
    version         BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : lignes_vente ─────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS lignes_vente (
    id              BIGSERIAL PRIMARY KEY,
    vente_id        BIGINT       REFERENCES ventes(id) ON DELETE CASCADE,
    code_produit    VARCHAR(20)  NOT NULL,
    designation_produit VARCHAR(200) NOT NULL,
    conditionnement VARCHAR(50)  NOT NULL,
    quantite        INTEGER      NOT NULL,
    prix_unitaire   DOUBLE PRECISION NOT NULL,
    remise          DOUBLE PRECISION NOT NULL DEFAULT 0
);

-- ─── TABLE : feuills_route (§5.8) ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS feuills_route (
    id                  BIGSERIAL PRIMARY KEY,
    numero_feuill_route VARCHAR(30)  NOT NULL UNIQUE,
    matricule_commercial VARCHAR(20) NOT NULL,
    date                DATE         NOT NULL,
    statut              VARCHAR(20)  NOT NULL DEFAULT 'PLANIFIE',
    matricule_cloture   VARCHAR(20),
    heure_cloture       TIMESTAMP,
    date_creation       TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification   TIMESTAMP,
    version             BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : arrets_feuill_route ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS arrets_feuill_route (
    id                  BIGSERIAL PRIMARY KEY,
    feuill_route_id     BIGINT       REFERENCES feuills_route(id) ON DELETE CASCADE,
    code_client         VARCHAR(20)  NOT NULL,
    ordre               INTEGER      NOT NULL,
    statut              VARCHAR(20)  NOT NULL DEFAULT 'PLANIFIE',
    numero_vente        VARCHAR(30),
    code_info_client    VARCHAR(10),
    quantite_info       INTEGER,
    observations_arret  TEXT,
    heure_planifiee     TIMESTAMP    NOT NULL,
    heure_effective     TIMESTAMP,
    coordonnees_gps     VARCHAR(100)
);

-- ─── TABLE : prospects (§5.13) ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS prospects (
    id                  BIGSERIAL PRIMARY KEY,
    code_prospect       VARCHAR(20)  NOT NULL UNIQUE,
    nom                 VARCHAR(150) NOT NULL,
    telephone           VARCHAR(20),
    adresse             VARCHAR(255),
    localite            VARCHAR(100),
    matricule_commercial VARCHAR(20),
    canal               VARCHAR(30),
    statut              VARCHAR(20)  NOT NULL DEFAULT 'NOUVEAU',
    observations        TEXT,
    code_client_converti VARCHAR(20),
    date_prospection    DATE,
    date_creation       TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification   TIMESTAMP,
    version             BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : decaissements (§5.11) ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS decaissements (
    id                      BIGSERIAL PRIMARY KEY,
    numero_decaissement     VARCHAR(30)  NOT NULL UNIQUE,
    type_depense            VARCHAR(30)  NOT NULL,
    matricule_proposant     VARCHAR(20)  NOT NULL,
    matricule_validateur_cg VARCHAR(20),
    matricule_validateur_dg VARCHAR(20),
    matricule_executeur     VARCHAR(20),
    montant_fcfa            DOUBLE PRECISION NOT NULL,
    beneficiaire            VARCHAR(150) NOT NULL,
    date_depense            DATE         NOT NULL,
    motif                   TEXT,
    motif_annulation        TEXT,
    statut                  VARCHAR(20)  NOT NULL DEFAULT 'PROPOSE',
    date_creation           TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification       TIMESTAMP,
    version                 BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : commandes_distance (§5.7) ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS commandes_distance (
    id                          BIGSERIAL PRIMARY KEY,
    numero_commande             VARCHAR(30)  NOT NULL UNIQUE,
    code_client                 VARCHAR(20)  NOT NULL,
    matricule_commercial        VARCHAR(20)  NOT NULL,
    canal                       VARCHAR(30)  NOT NULL,
    date_commande               DATE         NOT NULL,
    statut                      VARCHAR(20)  NOT NULL DEFAULT 'RECUE',
    matricule_validateur_secretaire VARCHAR(20),
    matricule_validateur_comptable  VARCHAR(20),
    motif_annulation            TEXT,
    observations                TEXT,
    date_creation               TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_modification           TIMESTAMP,
    version                     BIGINT       NOT NULL DEFAULT 0
);

-- ─── TABLE : lignes_commande_distance ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS lignes_commande_distance (
    id                      BIGSERIAL PRIMARY KEY,
    commande_distance_id    BIGINT       REFERENCES commandes_distance(id) ON DELETE CASCADE,
    code_produit            VARCHAR(20)  NOT NULL,
    designation             VARCHAR(200) NOT NULL,
    conditionnement         VARCHAR(50)  NOT NULL,
    quantite                INTEGER      NOT NULL,
    prix_unitaire           DOUBLE PRECISION NOT NULL
);

-- ─── TABLE : sync_logs (§12) ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS sync_logs (
    id                  BIGSERIAL PRIMARY KEY,
    idempotency_key     VARCHAR(100) NOT NULL UNIQUE,
    matricule_commercial VARCHAR(20) NOT NULL,
    entity_type         VARCHAR(30)  NOT NULL,
    operation_type      VARCHAR(30)  NOT NULL,
    entity_ref          VARCHAR(50),
    payload_hash        VARCHAR(100),
    result              VARCHAR(20),
    conflict_detail     TEXT,
    server_entity_ref   VARCHAR(50),
    date_reception      TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_traitement     TIMESTAMP
);

-- ─── INDEX ────────────────────────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_ventes_commercial ON ventes(matricule_commercial);
CREATE INDEX IF NOT EXISTS idx_ventes_date ON ventes(date);
CREATE INDEX IF NOT EXISTS idx_ventes_statut ON ventes(statut);
CREATE INDEX IF NOT EXISTS idx_feuills_route_commercial ON feuills_route(matricule_commercial);
CREATE INDEX IF NOT EXISTS idx_prospects_commercial ON prospects(matricule_commercial);
CREATE INDEX IF NOT EXISTS idx_decaissements_statut ON decaissements(statut);
CREATE INDEX IF NOT EXISTS idx_sync_logs_key ON sync_logs(idempotency_key);
CREATE INDEX IF NOT EXISTS idx_commandes_distance_statut ON commandes_distance(statut);
