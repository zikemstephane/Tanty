import { BonDeCommande } from '../../../core/models';

const MOCK: BonDeCommande[] = [
  // ── Q1 2026 ──────────────────────────────────────────────────────────────
  {
    id: 1, reference: 'BC-2026-001',
    dateCreation: '2026-01-06', dateDebutProduction: '2026-01-13', dateFinProduction: '2026-02-02', nombreSemaines: 3,
    statut: 'CLOS', statutLibelle: 'Clos',
    createurMatricule: 'TAN2026002', createurNom: 'Marie-Claire FOUDA',
    validateurMatricule: 'TAN2026003', validateurNom: 'Paul NKOMO', dateValidation: '2026-01-08',
    lignes: [
      { id: 1, numeroligne: 1, produitId: 'P001', produitCode: 'BSA-25', produitNom: 'REINE BOUILLIE SOJA ARACHIDES', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 57g', quantiteCartons: 200, quantiteUnites: 5000 },
      { id: 2, numeroligne: 2, produitId: 'P002', produitCode: 'BSC-25', produitNom: 'REINE BOUILLIE SOJA CLASSIC', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 57g', quantiteCartons: 150, quantiteUnites: 3750 },
      { id: 3, numeroligne: 3, produitId: 'P006', produitCode: 'TBSA-25', produitNom: 'TANTY BOUILLIE SOJA ARACHIDES', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 59g', quantiteCartons: 120, quantiteUnites: 3000 },
      { id: 4, numeroligne: 4, produitId: 'P028', produitCode: 'TC2L', produitNom: 'TANTY CHOCOLAT 2L', categorie: 'CHOCOLAT', conditionnement: '4 seaux x 2.2kg', quantiteCartons: 80, quantiteUnites: 320 }
    ],
    pphReferences: ['PPH-2026-001-S1','PPH-2026-001-S2','PPH-2026-001-S3']
  },
  {
    id: 2, reference: 'BC-2026-002',
    dateCreation: '2026-02-03', dateDebutProduction: '2026-02-10', dateFinProduction: '2026-03-02', nombreSemaines: 3,
    statut: 'CLOS', statutLibelle: 'Clos',
    createurMatricule: 'TAN2026003', createurNom: 'Paul NKOMO',
    validateurMatricule: 'TAN2026001', validateurNom: 'Jean-Pierre MBARGA', dateValidation: '2026-02-05',
    lignes: [
      { id: 5, numeroligne: 1, produitId: 'P011', produitCode: 'PRBA-12', produitNom: 'PRESTIGE REINE BOUILLIE ARACHIDES', categorie: 'BOUILLIE_SOJA', conditionnement: '12 etuis x 150g', quantiteCartons: 180, quantiteUnites: 2160 },
      { id: 6, numeroligne: 2, produitId: 'P016', produitCode: 'PTBA-12', produitNom: 'PRESTIGE TANTY BOUILLIE ARACHIDES', categorie: 'BOUILLIE_SOJA', conditionnement: '12 etuis x 150g', quantiteCartons: 150, quantiteUnites: 1800 },
      { id: 7, numeroligne: 3, produitId: 'P032', produitCode: 'MAE-MINI', produitNom: 'MINI A GRIGNOTER ARACHIDES ENROBEES', categorie: 'A_GRIGNOTER', conditionnement: '80 sachets x 35g', gamme: 'MINI', quantiteCartons: 200, quantiteUnites: 16000 },
      { id: 8, numeroligne: 4, produitId: 'P033', produitCode: 'CAR-MINI', produitNom: 'MINI A GRIGNOTER CARAMELS', categorie: 'A_GRIGNOTER', conditionnement: '80 sachets x 45g', gamme: 'MINI', quantiteCartons: 160, quantiteUnites: 12800 }
    ],
    pphReferences: ['PPH-2026-002-S1','PPH-2026-002-S2','PPH-2026-002-S3']
  },
  {
    id: 3, reference: 'BC-2026-003',
    dateCreation: '2026-03-03', dateDebutProduction: '2026-03-10', dateFinProduction: '2026-03-30', nombreSemaines: 3,
    statut: 'CLOS', statutLibelle: 'Clos',
    createurMatricule: 'TAN2026002', createurNom: 'Marie-Claire FOUDA',
    validateurMatricule: 'TAN2026003', validateurNom: 'Paul NKOMO', dateValidation: '2026-03-05',
    lignes: [
      { id: 9, numeroligne: 1, produitId: 'P021', produitCode: 'RCPB-12', produitNom: 'REINE COSTARD POWDER 1L BANANE', categorie: 'BOUILLIE_SOJA', conditionnement: '12 seaux x 390g', quantiteCartons: 100, quantiteUnites: 1200 },
      { id: 10, numeroligne: 2, produitId: 'P022', produitCode: 'RCPF-12', produitNom: 'REINE COSTARD POWDER 1L FRAISE', categorie: 'BOUILLIE_SOJA', conditionnement: '12 seaux x 390g', quantiteCartons: 100, quantiteUnites: 1200 },
      { id: 11, numeroligne: 3, produitId: 'P026', produitCode: 'BV1', produitNom: 'BABYVITA 1L', categorie: 'BOUILLIE_SOJA', conditionnement: '12 seaux x 390g', quantiteCartons: 80, quantiteUnites: 960 },
      { id: 12, numeroligne: 4, produitId: 'P031', produitCode: 'TCHAP-12', produitNom: 'TANTY CHAPELURE', categorie: 'INGREDIENTS', conditionnement: '12 etuis x 200g', quantiteCartons: 120, quantiteUnites: 1440 }
    ],
    pphReferences: ['PPH-2026-003-S1','PPH-2026-003-S2','PPH-2026-003-S3']
  },
  // ── Q2 2026 ──────────────────────────────────────────────────────────────
  {
    id: 4, reference: 'BC-2026-004',
    dateCreation: '2026-04-01', dateDebutProduction: '2026-04-07', dateFinProduction: '2026-04-27', nombreSemaines: 3,
    statut: 'CLOS', statutLibelle: 'Clos',
    createurMatricule: 'TAN2026003', createurNom: 'Paul NKOMO',
    validateurMatricule: 'TAN2026001', validateurNom: 'Jean-Pierre MBARGA', dateValidation: '2026-04-03',
    lignes: [
      { id: 13, numeroligne: 1, produitId: 'P003', produitCode: 'BSLC-25', produitNom: 'REINE BOUILLIE SOJA LACTEE CROISSANCE', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 57g', quantiteCartons: 180, quantiteUnites: 4500 },
      { id: 14, numeroligne: 2, produitId: 'P007', produitCode: 'TBSL-25', produitNom: 'TANTY BOUILLIE SOJA LACTEE', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 59g', quantiteCartons: 160, quantiteUnites: 4000 },
      { id: 15, numeroligne: 3, produitId: 'P037', produitCode: 'MAE-MOY', produitNom: 'MOYEN A GRIGNOTER ARACHIDES ENROBEES', categorie: 'A_GRIGNOTER', conditionnement: '24 doypack x 125g', gamme: 'MOYEN', quantiteCartons: 120, quantiteUnites: 2880 },
      { id: 16, numeroligne: 4, produitId: 'P038', produitCode: 'CAR-MOY', produitNom: 'MOYEN A GRIGNOTER CARAMELS', categorie: 'A_GRIGNOTER', conditionnement: '24 doypack x 136g', gamme: 'MOYEN', quantiteCartons: 100, quantiteUnites: 2400 },
      { id: 17, numeroligne: 5, produitId: 'P029', produitCode: 'TC5L', produitNom: 'TANTY CHOCOLAT 5L', categorie: 'CHOCOLAT', conditionnement: 'seau de 4.1kg', quantiteCartons: 60, quantiteUnites: 60 }
    ],
    pphReferences: ['PPH-2026-004-S1','PPH-2026-004-S2','PPH-2026-004-S3']
  },
  {
    id: 5, reference: 'BC-2026-005',
    dateCreation: '2026-05-05', dateDebutProduction: '2026-05-12', dateFinProduction: '2026-06-01', nombreSemaines: 3,
    statut: 'CLOS', statutLibelle: 'Clos',
    createurMatricule: 'TAN2026002', createurNom: 'Marie-Claire FOUDA',
    validateurMatricule: 'TAN2026003', validateurNom: 'Paul NKOMO', dateValidation: '2026-05-07',
    lignes: [
      { id: 18, numeroligne: 1, produitId: 'P004', produitCode: 'BSMC-25', produitNom: 'REINE BOUILLIE SOJA MULTI CEREALE', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 57g', quantiteCartons: 220, quantiteUnites: 5500 },
      { id: 19, numeroligne: 2, produitId: 'P005', produitCode: 'BSPO-25', produitNom: 'REINE BOUILLIE SOJA POISSON', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 57g', quantiteCartons: 180, quantiteUnites: 4500 },
      { id: 20, numeroligne: 3, produitId: 'P013', produitCode: 'PRBLC-12', produitNom: 'PRESTIGE REINE BOUILLIE LACTEE CROISSANCE', categorie: 'BOUILLIE_SOJA', conditionnement: '12 etuis x 150g', quantiteCartons: 140, quantiteUnites: 1680 },
      { id: 21, numeroligne: 4, produitId: 'P041', produitCode: 'MAE-GRD', produitNom: 'GRAND A GRIGNOTER ARACHIDES ENROBEES', categorie: 'A_GRIGNOTER', conditionnement: '14 doypack x 273g', gamme: 'GRAND', quantiteCartons: 80, quantiteUnites: 1120 },
      { id: 22, numeroligne: 5, produitId: 'P036', produitCode: 'BEI-MINI', produitNom: 'TANTY BEIGNETS SOUFFLES', categorie: 'A_GRIGNOTER', conditionnement: '60 doypack x 36g', gamme: 'MINI', quantiteCartons: 150, quantiteUnites: 9000 }
    ],
    pphReferences: ['PPH-2026-005-S1','PPH-2026-005-S2','PPH-2026-005-S3']
  },
  // ── BC Actuels ───────────────────────────────────────────────────────────
  {
    id: 6, reference: 'BC-2026-006',
    dateCreation: '2026-06-02', dateDebutProduction: '2026-06-09', dateFinProduction: '2026-06-29', nombreSemaines: 3,
    statut: 'VALIDE', statutLibelle: 'Validé',
    createurMatricule: 'TAN2026002', createurNom: 'Marie-Claire FOUDA',
    validateurMatricule: 'TAN2026003', validateurNom: 'Paul NKOMO', dateValidation: '2026-06-04',
    lignes: [
      { id: 23, numeroligne: 1, produitId: 'P001', produitCode: 'BSA-25', produitNom: 'REINE BOUILLIE SOJA ARACHIDES', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 57g', quantiteCartons: 120, quantiteUnites: 3000 },
      { id: 24, numeroligne: 2, produitId: 'P006', produitCode: 'TBSA-25', produitNom: 'TANTY BOUILLIE SOJA ARACHIDES', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 59g', quantiteCartons: 80, quantiteUnites: 2000 },
      { id: 25, numeroligne: 3, produitId: 'P008', produitCode: 'TBSN-25', produitNom: 'TANTY BOUILLIE SOJA NATURE', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 59g', quantiteCartons: 100, quantiteUnites: 2500 },
      { id: 26, numeroligne: 4, produitId: 'P032', produitCode: 'MAE-MINI', produitNom: 'MINI A GRIGNOTER ARACHIDES ENROBEES', categorie: 'A_GRIGNOTER', conditionnement: '80 sachets x 35g', gamme: 'MINI', quantiteCartons: 50, quantiteUnites: 4000 },
      { id: 27, numeroligne: 5, produitId: 'P034', produitCode: 'CRO-MINI', produitNom: 'MINI A GRIGNOTER CROQUETTES', categorie: 'A_GRIGNOTER', conditionnement: '80 sachets x 30g', gamme: 'MINI', quantiteCartons: 60, quantiteUnites: 4800 },
      { id: 28, numeroligne: 6, produitId: 'P028', produitCode: 'TC2L', produitNom: 'TANTY CHOCOLAT 2L', categorie: 'CHOCOLAT', conditionnement: '4 seaux x 2.2kg', quantiteCartons: 40, quantiteUnites: 160 }
    ],
    pphReferences: ['PPH-2026-006-S1','PPH-2026-006-S2','PPH-2026-006-S3']
  },
  {
    id: 7, reference: 'BC-2026-007',
    dateCreation: '2026-06-10', dateDebutProduction: '2026-06-16', dateFinProduction: '2026-06-22', nombreSemaines: 1,
    statut: 'EN_ATTENTE_VALIDATION', statutLibelle: 'En attente de validation',
    createurMatricule: 'TAN2026002', createurNom: 'Marie-Claire FOUDA',
    validateurMatricule: null, validateurNom: null, dateValidation: null,
    lignes: [
      { id: 29, numeroligne: 1, produitId: 'P030', produitCode: 'TC10L', produitNom: 'TANTY CHOCOLAT 10L', categorie: 'CHOCOLAT', conditionnement: 'seau de 9.2kg', quantiteCartons: 25, quantiteUnites: 25 },
      { id: 30, numeroligne: 2, produitId: 'P031', produitCode: 'TCHAP-12', produitNom: 'TANTY CHAPELURE', categorie: 'INGREDIENTS', conditionnement: '12 etuis x 200g', quantiteCartons: 60, quantiteUnites: 720 },
      { id: 31, numeroligne: 3, produitId: 'P042', produitCode: 'CAR-GRD', produitNom: 'GRAND A GRIGNOTER CARAMELS', categorie: 'A_GRIGNOTER', conditionnement: '14 doypack x 328g', gamme: 'GRAND', quantiteCartons: 45, quantiteUnites: 630 }
    ],
    pphReferences: []
  },
  {
    id: 8, reference: 'BC-2026-008',
    dateCreation: '2026-06-14', dateDebutProduction: '2026-06-23', dateFinProduction: '2026-07-13', nombreSemaines: 3,
    statut: 'BROUILLON', statutLibelle: 'Brouillon',
    createurMatricule: 'TAN2026003', createurNom: 'Paul NKOMO',
    validateurMatricule: null, validateurNom: null, dateValidation: null,
    lignes: [
      { id: 32, numeroligne: 1, produitId: 'P009', produitCode: 'TBSPA-25', produitNom: 'TANTY BOUILLIE SOJA PARFUME', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 59g', quantiteCartons: 160, quantiteUnites: 4000 },
      { id: 33, numeroligne: 2, produitId: 'P010', produitCode: 'TBSPO-25', produitNom: 'TANTY BOUILLIE SOJA POISSON', categorie: 'BOUILLIE_SOJA', conditionnement: '25 sachets x 59g', quantiteCartons: 140, quantiteUnites: 3500 },
      { id: 34, numeroligne: 3, produitId: 'P035', produitCode: 'CHIP-MINI', produitNom: 'MINI A GRIGNOTER CHIPS DE PLANTAINS', categorie: 'A_GRIGNOTER', conditionnement: '60 sachets x 35g', gamme: 'MINI', quantiteCartons: 90, quantiteUnites: 5400 },
      { id: 35, numeroligne: 4, produitId: 'P040', produitCode: 'CHIP-MOY', produitNom: 'MOYEN A GRIGNOTER CHIPS DE PLANTAINS', categorie: 'A_GRIGNOTER', conditionnement: '20 doypack x 100g', gamme: 'MOYEN', quantiteCartons: 70, quantiteUnites: 1400 },
      { id: 36, numeroligne: 5, produitId: 'P025', produitCode: 'RCP2B-4', produitNom: 'REINE COSTARD POWDER 2L BANANE', categorie: 'BOUILLIE_SOJA', conditionnement: '4 seaux x 880g', quantiteCartons: 50, quantiteUnites: 200 }
    ],
    pphReferences: []
  },
  {
    id: 9, reference: 'BC-2026-009',
    dateCreation: '2026-06-17', dateDebutProduction: '2026-07-14', dateFinProduction: '2026-08-03', nombreSemaines: 3,
    statut: 'BROUILLON', statutLibelle: 'Brouillon',
    createurMatricule: 'TAN2026002', createurNom: 'Marie-Claire FOUDA',
    validateurMatricule: null, validateurNom: null, dateValidation: null,
    lignes: [
      { id: 37, numeroligne: 1, produitId: 'P014', produitCode: 'PRBMC-12', produitNom: 'PRESTIGE REINE BOUILLIE MULTI CEREALE', categorie: 'BOUILLIE_SOJA', conditionnement: '12 etuis x 150g', quantiteCartons: 200, quantiteUnites: 2400 },
      { id: 38, numeroligne: 2, produitId: 'P019', produitCode: 'PTBPA-12', produitNom: 'PRESTIGE TANTY BOUILLIE PARFUME', categorie: 'BOUILLIE_SOJA', conditionnement: '12 etuis x 150g', quantiteCartons: 180, quantiteUnites: 2160 },
      { id: 39, numeroligne: 3, produitId: 'P043', produitCode: 'CRO-GRD', produitNom: 'GRAND A GRIGNOTER CROQUETTES', categorie: 'A_GRIGNOTER', conditionnement: '14 doypack x 227g', gamme: 'GRAND', quantiteCartons: 100, quantiteUnites: 1400 }
    ],
    pphReferences: []
  }
];

export default MOCK;
