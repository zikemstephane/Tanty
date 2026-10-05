/* ═══════════════════════════════════════════════════════════
   TANTY Digital ERP — Store Mock Global v3
   Données partagées entre tous les modules web
   ═══════════════════════════════════════════════════════════ */

// ── COMMERCIAUX ────────────────────────────────────────────
export const MOCK_COMMERCIAUX = [
  { id: 'COM001', nom: 'Akono Jean-Pierre', initiales: 'AJ', telephone: '+237 677 000 001', zone: 'Yaoundé Centre', ville: 'Yaoundé', vehicule: 'LT-225-AK', objectifCA: 4500000, caRealise: 3180000, rang: 4 },
  { id: 'COM002', nom: 'Bella Marie-Claire', initiales: 'BM', telephone: '+237 677 000 002', zone: 'Yaoundé Nord', ville: 'Yaoundé', vehicule: 'LT-226-BM', objectifCA: 4000000, caRealise: 4120000, rang: 2 },
  { id: 'COM003', nom: 'Fouda Simon', initiales: 'FS', telephone: '+237 677 000 003', zone: 'Yaoundé Sud', ville: 'Yaoundé', vehicule: 'LT-227-FS', objectifCA: 3800000, caRealise: 2890000, rang: 5 },
  { id: 'COM004', nom: 'Nkomo Patricia', initiales: 'NP', telephone: '+237 677 000 004', zone: 'Douala Akwa', ville: 'Douala', vehicule: 'LT-228-NP', objectifCA: 5000000, caRealise: 4650000, rang: 1 },
  { id: 'COM005', nom: 'Tagne Robert', initiales: 'TR', telephone: '+237 677 000 005', zone: 'Douala Bonabéri', ville: 'Douala', vehicule: 'LT-229-TR', objectifCA: 4200000, caRealise: 3950000, rang: 3 },
];

// ── PRODUITS (catalogue fiche synthèse) ───────────────────
export const MOCK_PRODUITS = [
  { code: 'MGASS', designation: 'Mini Grigno Arachide Sachet S', unite: 'carton', ratioPoudre: 80, gamme: 'Snacking', prixHT: 8500 },
  { code: 'MCGAS', designation: 'Moyen Grigno Arachide Sachet', unite: 'carton', ratioPoudre: 24, gamme: 'Snacking', prixHT: 12000 },
  { code: 'GGASS', designation: 'Grand Grigno Arachide Sachet S', unite: 'carton', ratioPoudre: 14, gamme: 'Snacking', prixHT: 18000 },
  { code: 'MGPL', designation: 'Mini Grigno Piment Losange', unite: 'carton', ratioPoudre: 20, gamme: 'Snacking', prixHT: 9000 },
  { code: 'TBS', designation: 'Tanty Bouillie Soja', unite: 'carton', ratioPoudre: 60, gamme: 'Bouillie', prixHT: 9000 },
  { code: 'RBS', designation: 'Reine Bouillie Soja', unite: 'carton', ratioPoudre: 24, gamme: 'Bouillie', prixHT: 10500 },
  { code: 'PTBS', designation: 'Petite Tanty Bouillie Soja', unite: 'carton', ratioPoudre: 12, gamme: 'Bouillie', prixHT: 6500 },
  { code: 'TCH', designation: 'Tanty Choco', unite: 'carton', ratioPoudre: 12, gamme: 'Choco', prixHT: 15000 },
  { code: 'TC1L', designation: 'Tanty Choco 1L', unite: 'carton', ratioPoudre: 12, gamme: 'Choco', prixHT: 16000 },
  { code: 'TC2L', designation: 'Tanty Choco 2L', unite: 'carton', ratioPoudre: 4, gamme: 'Choco', prixHT: 28000 },
  { code: 'TC5L', designation: 'Tanty Choco 5L', unite: 'seau', ratioPoudre: 1, gamme: 'Choco', prixHT: 62000 },
  { code: 'RCP1L', designation: 'Récipient 1 Litre', unite: 'carton', ratioPoudre: 12, gamme: 'Seau', prixHT: 11000 },
  { code: 'RCP2L', designation: 'Récipient 2 Litres', unite: 'carton', ratioPoudre: 4, gamme: 'Seau', prixHT: 18000 },
  { code: 'RCP5L', designation: 'Récipient 5 Litres', unite: 'seau', ratioPoudre: 1, gamme: 'Seau', prixHT: 38000 },
  { code: 'BBV1L', designation: 'BBV 1 Litre', unite: 'carton', ratioPoudre: 12, gamme: 'BBV', prixHT: 13000 },
  { code: 'BBV2L', designation: 'BBV 2 Litres', unite: 'carton', ratioPoudre: 4, gamme: 'BBV', prixHT: 22000 },
  { code: 'PTASS', designation: 'Petite Tanty Assortis', unite: 'carton', ratioPoudre: 12, gamme: 'Assortis', prixHT: 7500 },
  { code: 'TASS', designation: 'Tanty Assortis', unite: 'carton', ratioPoudre: 25, gamme: 'Assortis', prixHT: 10000 },
  { code: 'RASS', designation: 'Reine Assortis', unite: 'carton', ratioPoudre: 25, gamme: 'Assortis', prixHT: 11500 },
  { code: 'THBP', designation: 'Tanty Huile Beurre de Palme 1kg', unite: 'seau', ratioPoudre: 1, gamme: 'Huile', prixHT: 4500 },
];

// ── CLIENTS ────────────────────────────────────────────────
export const MOCK_CLIENTS = [
  { id: 'CLI001', nom: 'Distrib Amina', type: 'GROSSISTE', ville: 'Yaoundé', telephone: '+237 699 100 001', soldeDette: 125000, commercialId: 'COM001' },
  { id: 'CLI002', nom: 'Shop Beaufort', type: 'DETAILLANT', ville: 'Yaoundé', telephone: '+237 699 100 002', soldeDette: 0, commercialId: 'COM001' },
  { id: 'CLI003', nom: 'Épicerie du Port', type: 'DETAILLANT', ville: 'Douala', telephone: '+237 699 100 003', soldeDette: 82000, commercialId: 'COM004' },
  { id: 'CLI004', nom: 'SuperMarché Mahima', type: 'GROSSISTE', ville: 'Douala', telephone: '+237 699 100 004', soldeDette: 340000, commercialId: 'COM004' },
  { id: 'CLI005', nom: 'Chez Mama Pauline', type: 'DETAILLANT', ville: 'Yaoundé', telephone: '+237 699 100 005', soldeDette: 58000, commercialId: 'COM002' },
  { id: 'CLI006', nom: 'Commerce Général Ndi', type: 'GROSSISTE', ville: 'Yaoundé', telephone: '+237 699 100 006', soldeDette: 215000, commercialId: 'COM003' },
  { id: 'CLI007', nom: 'Hôtel Mont Fébé', type: 'DISTRIBUTEUR', ville: 'Yaoundé', telephone: '+237 699 100 007', soldeDette: 0, commercialId: 'COM001' },
  { id: 'CLI008', nom: 'Resto Douala Palace', type: 'DISTRIBUTEUR', ville: 'Douala', telephone: '+237 699 100 008', soldeDette: 480000, commercialId: 'COM004' },
  { id: 'CLI009', nom: 'Mini Marché Ekounou', type: 'DETAILLANT', ville: 'Yaoundé', telephone: '+237 699 100 009', soldeDette: 32000, commercialId: 'COM002' },
  { id: 'CLI010', nom: 'Grande Surface Biyem', type: 'GROSSISTE', ville: 'Yaoundé', telephone: '+237 699 100 010', soldeDette: 0, commercialId: 'COM003' },
  { id: 'CLI011', nom: 'Boutique Akwa Center', type: 'DETAILLANT', ville: 'Douala', telephone: '+237 699 100 011', soldeDette: 145000, commercialId: 'COM005' },
  { id: 'CLI012', nom: 'Import-Export Tala', type: 'GROSSISTE', ville: 'Douala', telephone: '+237 699 100 012', soldeDette: 0, commercialId: 'COM005' },
];

// ── FICHE SYNTHÈSE DU JOUR ────────────────────────────────
export const MOCK_FICHE_SYNTHESE = {
  date: new Date(),
  caYaounde: 10190000,
  caDouala: 8600000,
  caTotal: 18790000,
  effectifCommerciaux: 5,
  vacInit: 18790000,
  ca: 18790000,
  vac: 15450000,
  recouvrements: 420000,
  vteCpt: 14200000,
  soldeVAC: 1250000,
  cumulMensuelCA: 84500000,
  eCheques: 850000,
  vstBanques: 1400000,
  depensesJustifiees: 215000,
  depensesNonJust: 35000,
  sCaisse: 4820000,
  montantAvaries: 48000,
  montantDotPromo: 125000,
  lignes: [
    { codeProduit: 'MGASS', qteProduite: 120, stockVeille: 45, stockCamion: 15, stockTotal: 180, qteSorties: 80, stockFinal: 100, qteAvoDotProm: 5, qteFacturee: 75 },
    { codeProduit: 'TBS', qteProduite: 80, stockVeille: 30, stockCamion: 8, stockTotal: 118, qteSorties: 60, stockFinal: 58, qteAvoDotProm: 2, qteFacturee: 58 },
    { codeProduit: 'TCH', qteProduite: 40, stockVeille: 12, stockCamion: 5, stockTotal: 57, qteSorties: 25, stockFinal: 32, qteAvoDotProm: 1, qteFacturee: 24 },
    { codeProduit: 'RCP2L', qteProduite: 30, stockVeille: 18, stockCamion: 3, stockTotal: 51, qteSorties: 20, stockFinal: 31, qteAvoDotProm: 0, qteFacturee: 20 },
    { codeProduit: 'BBV1L', qteProduite: 50, stockVeille: 22, stockCamion: 6, stockTotal: 78, qteSorties: 40, stockFinal: 38, qteAvoDotProm: 3, qteFacturee: 37 },
    { codeProduit: 'TC1L', qteProduite: 60, stockVeille: 15, stockCamion: 4, stockTotal: 79, qteSorties: 45, stockFinal: 34, qteAvoDotProm: 2, qteFacturee: 43 },
    { codeProduit: 'PTBS', qteProduite: 100, stockVeille: 40, stockCamion: 12, stockTotal: 152, qteSorties: 70, stockFinal: 82, qteAvoDotProm: 4, qteFacturee: 66 },
    { codeProduit: 'GGASS', qteProduite: 25, stockVeille: 8, stockCamion: 2, stockTotal: 35, qteSorties: 15, stockFinal: 20, qteAvoDotProm: 1, qteFacturee: 14 },
    { codeProduit: 'TASS', qteProduite: 45, stockVeille: 20, stockCamion: 5, stockTotal: 70, qteSorties: 35, stockFinal: 35, qteAvoDotProm: 2, qteFacturee: 33 },
    { codeProduit: 'MCGAS', qteProduite: 35, stockVeille: 14, stockCamion: 4, stockTotal: 53, qteSorties: 28, stockFinal: 25, qteAvoDotProm: 1, qteFacturee: 27 },
  ],
};

// ── VERSEMENTS DU JOUR ────────────────────────────────────
export const MOCK_VERSEMENTS = [
  { id: 'VER001', commercialId: 'COM001', date: new Date(), montantAttendu: 1850000, cashVerse: 1800000, ecart: -50000, motifEcart: 'Panne véhicule — retour dépôt partiel', statut: 'ECART_JUSTIFIE', typeVersement: 'ESPECES', heureVersement: '18:30', alerteRouge: true },
  { id: 'VER002', commercialId: 'COM002', date: new Date(), montantAttendu: 4120000, cashVerse: 4120000, ecart: 0, motifEcart: null, statut: 'VALIDE', typeVersement: 'ESPECES', heureVersement: '17:45', alerteRouge: false },
  { id: 'VER003', commercialId: 'COM003', date: new Date(), montantAttendu: 2890000, cashVerse: 0, ecart: 0, motifEcart: null, statut: 'EN_ATTENTE', typeVersement: null, heureVersement: null, alerteRouge: false },
  { id: 'VER004', commercialId: 'COM004', date: new Date(), montantAttendu: 4650000, cashVerse: 4500000, ecart: -150000, motifEcart: 'Vente à crédit autorisée — client Mahima', statut: 'VALIDE', typeVersement: 'VIREMENT', heureVersement: '16:20', alerteRouge: false },
  { id: 'VER005', commercialId: 'COM005', date: new Date(), montantAttendu: 3950000, cashVerse: 3950000, ecart: 0, motifEcart: null, statut: 'EN_ATTENTE_VALIDATION', typeVersement: 'CHEQUE', heureVersement: '18:00', alerteRouge: false },
];

// ── PRÉ-COMMANDES J+1 ─────────────────────────────────────
export const MOCK_PRECOMMANDES = [
  {
    id: 'PCM001', commercialId: 'COM001', dateCommande: new Date(), statut: 'SOUMISE',
    lignes: [{ produitCode: 'MGASS', quantite: 40 }, { produitCode: 'TBS', quantite: 20 }, { produitCode: 'TCH', quantite: 15 }],
    signatureSecretaire: false, signatureComptable: false,
  },
  {
    id: 'PCM002', commercialId: 'COM002', dateCommande: new Date(), statut: 'VALIDEE_SECRETAIRE',
    lignes: [{ produitCode: 'RCP2L', quantite: 10 }, { produitCode: 'BBV1L', quantite: 25 }, { produitCode: 'TC2L', quantite: 8 }],
    signatureSecretaire: true, signatureComptable: false,
  },
  {
    id: 'PCM003', commercialId: 'COM003', dateCommande: new Date(), statut: 'VALIDEE_COMPTABLE',
    lignes: [{ produitCode: 'PTBS', quantite: 30 }, { produitCode: 'RASS', quantite: 15 }, { produitCode: 'MCGAS', quantite: 20 }],
    signatureSecretaire: true, signatureComptable: true,
  },
  {
    id: 'PCM004', commercialId: 'COM004', dateCommande: new Date(), statut: 'SOUMISE',
    lignes: [{ produitCode: 'TC1L', quantite: 20 }, { produitCode: 'GGASS', quantite: 10 }, { produitCode: 'TASS', quantite: 25 }],
    signatureSecretaire: false, signatureComptable: false,
  },
  {
    id: 'PCM005', commercialId: 'COM005', dateCommande: new Date(), statut: 'VALIDEE_SECRETAIRE',
    lignes: [{ produitCode: 'BBV2L', quantite: 12 }, { produitCode: 'MGPL', quantite: 18 }],
    signatureSecretaire: true, signatureComptable: false,
  },
];

// ── CARBURANT ─────────────────────────────────────────────
export const MOCK_CARBURANT = [
  { id: 'CAR001', commercialId: 'COM001', dateCreation: new Date(), immatriculation: 'LT-225-AK', kmCompteur: 124580, litresDemandes: 45, montant: 22500, station: 'Total Ngousso — Yaoundé', signatureCommercial: true, signatureSuperviseur: false, statut: 'EN_ATTENTE_SIGNATURE' },
  { id: 'CAR002', commercialId: 'COM003', dateCreation: new Date(Date.now() - 86400000), immatriculation: 'LT-227-FS', kmCompteur: 98340, litresDemandes: 38, montant: 19000, station: 'Total Hippodrome — Yaoundé', signatureCommercial: true, signatureSuperviseur: true, statut: 'VALIDEE', superviseur: 'Essama Paul (Chef Prod.)' },
  { id: 'CAR003', commercialId: 'COM005', dateCreation: new Date(), immatriculation: 'LT-229-TR', kmCompteur: 201450, litresDemandes: 52, montant: 26000, station: 'Total Akwa — Douala', signatureCommercial: true, signatureSuperviseur: false, statut: 'EN_ATTENTE_SIGNATURE' },
  { id: 'CAR004', commercialId: 'COM004', dateCreation: new Date(Date.now() - 172800000), immatriculation: 'LT-228-NP', kmCompteur: 187220, litresDemandes: 60, montant: 30000, station: 'Total Bonabéri — Douala', signatureCommercial: true, signatureSuperviseur: true, statut: 'VALIDEE', superviseur: 'Biya Léon (Contrôleur Gestion)' },
];

// ── DEMANDES PRIX EXCEPTIONNELS ───────────────────────────
export const MOCK_ARBITRAGES_PRIX = [
  { id: 'ARB001', commercialId: 'COM001', clientId: 'CLI007', clientNom: 'Hôtel Mont Fébé', produitCode: 'MGASS', prixNormal: 8500, prixDemande: 7000, reduction: 17.6, justification: 'Gros client potentiel — commande de démarrage 100 cartons/mois', statut: 'EN_ATTENTE', dateCreation: new Date() },
  { id: 'ARB002', commercialId: 'COM004', clientId: 'CLI008', clientNom: 'Resto Douala Palace', produitCode: 'TC2L', prixNormal: 28000, prixDemande: 24000, reduction: 14.3, justification: 'Commande 50 cartons TC2L pour événement — besoin immédiat', statut: 'EN_ATTENTE', dateCreation: new Date() },
  { id: 'ARB003', commercialId: 'COM002', clientId: 'CLI009', clientNom: 'Mini Marché Ekounou', produitCode: 'TBS', prixNormal: 9000, prixDemande: 8000, reduction: 11.1, justification: 'Client fidèle 3 ans — situation difficile provisoire', statut: 'APPROUVE', dateCreation: new Date(Date.now() - 86400000), datDecision: new Date(Date.now() - 3600000 * 8) },
  { id: 'ARB004', commercialId: 'COM003', clientId: 'CLI006', clientNom: 'Commerce Général Ndi', produitCode: 'PTBS', prixNormal: 6500, prixDemande: 5500, reduction: 15.4, justification: 'Règlement dette 215 000 FCFA en échange de remise exceptionnelle', statut: 'REFUSE', dateCreation: new Date(Date.now() - 172800000), datDecision: new Date(Date.now() - 86400000) },
];

// ── DÉCAISSEMENTS ─────────────────────────────────────────
export const MOCK_DECAISSEMENTS = [
  { id: 'DEC001', libelle: 'Carburant extra — tournée prolongée Douala', montant: 75000, demandePar: 'Nkomo Patricia (COM004)', motif: 'Bouchon autoroutier — 3h de plus', statut: 'EN_ATTENTE_DG', dateCreation: new Date(), approDG: false, approCG: false },
  { id: 'DEC002', libelle: 'Réparation pneu avant camion LT-227-FS', montant: 180000, demandePar: 'Fouda Simon (COM003)', motif: 'Crevaison terrain Yaoundé Sud', statut: 'DG_APPROUVE', dateCreation: new Date(Date.now() - 86400000), approDG: true, approCG: false, datApproDG: new Date(Date.now() - 3600000 * 6) },
  { id: 'DEC003', libelle: 'Fournitures bureau secrétariat', montant: 45000, demandePar: 'Kouma Cécile (Secrétaire Caisse)', motif: 'Stock épuisé — résines, stylos, papier', statut: 'VALIDE', dateCreation: new Date(Date.now() - 172800000), approDG: true, approCG: true },
  { id: 'DEC004', libelle: 'Prime exceptionnelle agent production', montant: 25000, demandePar: 'Essama Paul (Chef Prod.)', motif: 'Bonne performance semaine 25 — dépassement 20% objectif', statut: 'EN_ATTENTE_DG', dateCreation: new Date(), approDG: false, approCG: false },
];

// ── CA MENSUEL N VS N-1 ───────────────────────────────────
export const MOCK_CA_MENSUEL = [
  { mois: 'Jan', n: 12500000, nMoins1: 10800000 },
  { mois: 'Fév', n: 13200000, nMoins1: 11400000 },
  { mois: 'Mar', n: 11800000, nMoins1: 12100000 },
  { mois: 'Avr', n: 14600000, nMoins1: 11900000 },
  { mois: 'Mai', n: 13900000, nMoins1: 13200000 },
  { mois: 'Jun', n: 18790000, nMoins1: 15300000 },
];

export const MOCK_CA_PAR_ZONE = [
  { zone: 'Yaounde Centre', ca: 3180000, objectif: 4500000 },
  { zone: 'Yaounde Nord', ca: 4120000, objectif: 4000000 },
  { zone: 'Yaounde Sud', ca: 2890000, objectif: 3800000 },
  { zone: 'Douala Akwa', ca: 4650000, objectif: 5000000 },
  { zone: 'Douala Bonaberi', ca: 3950000, objectif: 4200000 },
];

// ── RECOUVREMENT ──────────────────────────────────────────
export const MOCK_RECOUVREMENT = [
  { id: 'REC001', clientId: 'CLI004', clientNom: 'SuperMarché Mahima', montantDette: 340000, montantEncaisse: 150000, soldeRestant: 190000, dateEncaissement: new Date(Date.now() - 86400000), commercialId: 'COM004', statut: 'PARTIEL' },
  { id: 'REC002', clientId: 'CLI001', clientNom: 'Distrib Amina', montantDette: 125000, montantEncaisse: 125000, soldeRestant: 0, dateEncaissement: new Date(Date.now() - 172800000), commercialId: 'COM001', statut: 'APURE' },
  { id: 'REC003', clientId: 'CLI008', clientNom: 'Resto Douala Palace', montantDette: 480000, montantEncaisse: 0, soldeRestant: 480000, dateEncaissement: null, commercialId: 'COM004', statut: 'EN_ATTENTE' },
  { id: 'REC004', clientId: 'CLI006', clientNom: 'Commerce Général Ndi', montantDette: 215000, montantEncaisse: 100000, soldeRestant: 115000, dateEncaissement: new Date(), commercialId: 'COM003', statut: 'PARTIEL' },
  { id: 'REC005', clientId: 'CLI011', clientNom: 'Boutique Akwa Center', montantDette: 145000, montantEncaisse: 145000, soldeRestant: 0, dateEncaissement: new Date(), commercialId: 'COM005', statut: 'APURE' },
];

// ── PLAN DE PRODUCTION ─────────────────────────────────────
export const MOCK_PLANS_PRODUCTION = [
  {
    id: 'PP001', semaine: 'S26-2026', dateDebut: '2026-06-23', dateFin: '2026-06-28', statut: 'EN_COURS',
    chefProduction: 'Essama Paul',
    lignesMatieresPremières: [
      { nom: 'Soja décortiqué', quantite: 2500, unite: 'kg' },
      { nom: 'Arachide grillée', quantite: 1800, unite: 'kg' },
      { nom: 'Sucre', quantite: 600, unite: 'kg' },
      { nom: 'Huile végétale', quantite: 400, unite: 'L' },
    ],
    lignesProduitsFinis: [
      { codeProduit: 'MGASS', qtePrevue: 120, qteRealisee: 80 },
      { codeProduit: 'TBS', qtePrevue: 80, qteRealisee: 60 },
      { codeProduit: 'TCH', qtePrevue: 40, qteRealisee: 40 },
      { codeProduit: 'PTBS', qtePrevue: 100, qteRealisee: 55 },
    ],
  },
];

// ── REGISTRES JOURNALIERS ─────────────────────────────────
export const MOCK_REGISTRES_BROYAGE = [
  { id: 'RB001', date: new Date(), machiniste: 'Ndinga Thomas', quantiteNetProduite: 4850, pourcentagePerte: 3.2, matieresEntrees: 5010, machinesUtilisees: 'BRY-01, BRY-02', heureDebut: '06:30', heureFin: '14:30' },
  { id: 'RB002', date: new Date(Date.now() - 86400000), machiniste: 'Obam Claude', quantiteNetProduite: 4620, pourcentagePerte: 3.8, matieresEntrees: 4800, machinesUtilisees: 'BRY-01', heureDebut: '06:30', heureFin: '15:00' },
  { id: 'RB003', date: new Date(Date.now() - 172800000), machiniste: 'Ndinga Thomas', quantiteNetProduite: 5100, pourcentagePerte: 2.9, matieresEntrees: 5253, machinesUtilisees: 'BRY-01, BRY-02, BRY-03', heureDebut: '06:00', heureFin: '14:00' },
];

export const MOCK_REGISTRES_DOSAGE = [
  { id: 'RD001', date: new Date(), agentDoseur: 'Mbarga Suzanne', nombreFuts: 48, machinesUtilisees: 'DOS-01, DOS-02', heureDebut: '07:00', heureFin: '15:30', lignesMatieres: [{ matiere: 'Poudre de soja', quantite: 1200 }, { matiere: 'Poudre arachide', quantite: 900 }] },
  { id: 'RD002', date: new Date(Date.now() - 86400000), agentDoseur: 'Nanga Berthe', nombreFuts: 40, machinesUtilisees: 'DOS-01', heureDebut: '07:00', heureFin: '16:00', lignesMatieres: [{ matiere: 'Poudre de soja', quantite: 1000 }, { matiere: 'Sucre', quantite: 300 }] },
];

export const MOCK_LOTS_PRODUITS_FINIS = [
  { id: 'LOT001', codeProduit: 'MGASS', productionDate: new Date(), batchDate: new Date(), declaredQuantityKg: 840, equivalentUnits: 80, declaredBy: 'Essama Paul', statut: 'DECLARED_BY_PRODUCTION' },
  { id: 'LOT002', codeProduit: 'TBS', productionDate: new Date(), batchDate: new Date(), declaredQuantityKg: 540, equivalentUnits: 60, declaredBy: 'Essama Paul', statut: 'VALIDATED_BY_STOCK' },
  { id: 'LOT003', codeProduit: 'TCH', productionDate: new Date(Date.now() - 86400000), batchDate: new Date(Date.now() - 86400000), declaredQuantityKg: 480, equivalentUnits: 40, declaredBy: 'Essama Paul', statut: 'VALIDATED_BY_STOCK' },
  { id: 'LOT004', codeProduit: 'PTBS', productionDate: new Date(), batchDate: new Date(), declaredQuantityKg: 357, equivalentUnits: 55, declaredBy: 'Essama Paul', statut: 'DECLARED_BY_PRODUCTION' },
];

// ── EMPLOYÉS PRODUCTION DU JOUR ───────────────────────────
export const MOCK_REGISTRE_EMPLOYES = [
  { id: 'EMP001', nom: 'Ndinga Thomas', poste: 'Machiniste Broyage', present: true, heureArrivee: '06:15', qteRealisee: 4850, unite: 'kg' },
  { id: 'EMP002', nom: 'Obam Claude', poste: 'Machiniste Broyage', present: true, heureArrivee: '06:30', qteRealisee: 2100, unite: 'kg' },
  { id: 'EMP003', nom: 'Mbarga Suzanne', poste: 'Agent Dosage', present: true, heureArrivee: '07:00', qteRealisee: 48, unite: 'fûts' },
  { id: 'EMP004', nom: 'Nanga Berthe', poste: 'Agent Dosage', present: true, heureArrivee: '07:15', qteRealisee: 32, unite: 'fûts' },
  { id: 'EMP005', nom: 'Biyong Albert', poste: 'Agent Emballage', present: false, heureArrivee: null, qteRealisee: 0, unite: 'cartons' },
  { id: 'EMP006', nom: 'Mefane Rose', poste: 'Agent Emballage', present: true, heureArrivee: '07:30', qteRealisee: 120, unite: 'cartons' },
];

// ── UTILISATEURS SYSTÈME ──────────────────────────────────
export const MOCK_USERS = [
  { id: 'USR001', nom: 'Kamga Jean-Marie', prenom: 'Jean-Marie', email: 'dg@ntfoods.cm', role: 'DIRECTEUR_GENERAL', matricule: 'DG001', actif: true, dateCreation: '2024-01-01', derniereConnexion: new Date() },
  { id: 'USR002', nom: 'Mvondo Jean-Baptiste', prenom: 'Jean-Baptiste', email: 'j.mvondo@ntfoods.cm', role: 'GESTIONNAIRE_STOCK', matricule: 'STK001', actif: true, dateCreation: '2024-01-15', derniereConnexion: new Date() },
  { id: 'USR003', nom: 'Mbakop Henri', prenom: 'Henri', email: 'h.mbakop@ntfoods.cm', role: 'COMPTABLE', matricule: 'CPT001', actif: true, dateCreation: '2024-01-15', derniereConnexion: new Date(Date.now() - 3600000) },
  { id: 'USR004', nom: 'Kouma Cécile', prenom: 'Cécile', email: 'c.kouma@ntfoods.cm', role: 'SECRETAIRE', matricule: 'SEC001', actif: true, dateCreation: '2024-01-15', derniereConnexion: new Date() },
  { id: 'USR005', nom: 'Ngo Marie', prenom: 'Marie', email: 'm.ngo@ntfoods.cm', role: 'CHARGEE_RP', matricule: 'RP001', actif: true, dateCreation: '2024-02-01', derniereConnexion: new Date(Date.now() - 86400000) },
  { id: 'USR006', nom: 'Essama Paul', prenom: 'Paul', email: 'p.essama@ntfoods.cm', role: 'CHEF_PRODUCTION', matricule: 'PRD001', actif: true, dateCreation: '2024-01-10', derniereConnexion: new Date() },
  { id: 'USR007', nom: 'Biya Léon', prenom: 'Léon', email: 'l.biya@ntfoods.cm', role: 'CONTROLEUR_GENERAL', matricule: 'CG001', actif: true, dateCreation: '2024-03-01', derniereConnexion: new Date(Date.now() - 3600000 * 2) },
  { id: 'USR008', nom: 'Akono Jean-Pierre', prenom: 'Jean-Pierre', email: 'j.akono@ntfoods.cm', role: 'COMMERCIAL', matricule: 'COM001', actif: true, dateCreation: '2024-01-20', terminalImei: '357123456700001', derniereConnexion: new Date() },
  { id: 'USR009', nom: 'Bella Marie-Claire', prenom: 'Marie-Claire', email: 'm.bella@ntfoods.cm', role: 'COMMERCIAL', matricule: 'COM002', actif: true, dateCreation: '2024-01-20', terminalImei: '357123456700002', derniereConnexion: new Date() },
  { id: 'USR010', nom: 'Fouda Simon', prenom: 'Simon', email: 's.fouda@ntfoods.cm', role: 'COMMERCIAL', matricule: 'COM003', actif: true, dateCreation: '2024-02-10', terminalImei: '357123456700003', derniereConnexion: new Date() },
  { id: 'USR011', nom: 'Nkomo Patricia', prenom: 'Patricia', email: 'p.nkomo@ntfoods.cm', role: 'COMMERCIAL', matricule: 'COM004', actif: true, dateCreation: '2024-02-10', terminalImei: '357123456700004', derniereConnexion: new Date() },
  { id: 'USR012', nom: 'Tagne Robert', prenom: 'Robert', email: 'r.tagne@ntfoods.cm', role: 'COMMERCIAL', matricule: 'COM005', actif: false, dateCreation: '2024-03-15', terminalImei: '357123456700005', derniereConnexion: new Date(Date.now() - 172800000) },
  { id: 'USR013', nom: 'Admin Système', prenom: 'Sys', email: 'admin@ntfoods.cm', role: 'ADMIN', matricule: 'ADM001', actif: true, dateCreation: '2024-01-01', derniereConnexion: new Date() },
];

// ── AUDIT LOG ─────────────────────────────────────────────
export const MOCK_AUDIT_LOG = [
  { id: 'AUD001', ts: new Date(), utilisateur: 'Akono Jean-Pierre', role: 'COMMERCIAL', action: 'CONNEXION', details: 'Connexion depuis Android (IMEI: ...00001)', ip: '192.168.1.101', niveau: 'INFO' },
  { id: 'AUD002', ts: new Date(Date.now() - 600000), utilisateur: 'Kouma Cécile', role: 'SECRETAIRE', action: 'VALIDATION_VERSEMENT', details: 'Versement VER002 validé — Bella Marie-Claire 4 120 000 FCFA', ip: '192.168.1.10', niveau: 'INFO' },
  { id: 'AUD003', ts: new Date(Date.now() - 1200000), utilisateur: 'Kamga Jean-Marie', role: 'DIRECTEUR_GENERAL', action: 'ARBITRAGE_PRIX_APPROUVE', details: 'ARB003 approuvé — TBS 9000→8000 pour Mini Marché Ekounou', ip: '192.168.1.2', niveau: 'IMPORTANT' },
  { id: 'AUD004', ts: new Date(Date.now() - 1800000), utilisateur: 'Mbakop Henri', role: 'COMPTABLE', action: 'VALIDATION_COMMANDE', details: 'PCM003 validée comptable — Fouda Simon chargement autorisé', ip: '192.168.1.11', niveau: 'INFO' },
  { id: 'AUD005', ts: new Date(Date.now() - 3600000), utilisateur: 'Mvondo Jean-Baptiste', role: 'GESTIONNAIRE_STOCK', action: 'LOCK_SESSION', details: 'Session retours COM002 clôturée — 4 020 unités vérifiées', ip: '192.168.1.20', niveau: 'INFO' },
  { id: 'AUD006', ts: new Date(Date.now() - 5400000), utilisateur: 'Nkomo Patricia', role: 'COMMERCIAL', action: 'VERSEMENT_SOUMIS', details: 'VER004 soumis — 4 500 000 FCFA (virement)', ip: '192.168.1.102', niveau: 'INFO' },
  { id: 'AUD007', ts: new Date(Date.now() - 7200000), utilisateur: 'Essama Paul', role: 'CHEF_PRODUCTION', action: 'LOT_DECLARE', details: 'LOT001 déclaré — MGASS 840kg = 80 cartons', ip: '192.168.1.30', niveau: 'INFO' },
  { id: 'AUD008', ts: new Date(Date.now() - 9000000), utilisateur: 'Admin Système', role: 'ADMIN', action: 'USER_DEACTIVATED', details: 'Compte USR012 Tagne Robert suspendu — absence prolongée', ip: '192.168.1.1', niveau: 'IMPORTANT' },
  { id: 'AUD009', ts: new Date(Date.now() - 10800000), utilisateur: 'Biya Léon', role: 'CONTROLEUR_GENERAL', action: 'SIGNATURE_CARBURANT', details: 'CAR004 validé — Nkomo Patricia 60L Total Bonabéri', ip: '192.168.1.15', niveau: 'INFO' },
  { id: 'AUD010', ts: new Date(Date.now() - 14400000), utilisateur: 'Kouma Cécile', role: 'SECRETAIRE', action: 'ALERTE_ROUGE', details: 'Alerte rouge VER001 — Akono JP écart -50 000 FCFA', ip: '192.168.1.10', niveau: 'ALERTE' },
];

// ── FACTURATION ───────────────────────────────────────────
export const MOCK_FACTURES = [
  { id: 'FAC001', numero: 'FAC-2026-0847', commercialId: 'COM001', clientId: 'CLI001', clientNom: 'Distrib Amina', dateFacture: new Date(), montantHT: 425000, tva: 81813, montantTTC: 506813, statut: 'EMISE', type: 'FACTURE' },
  { id: 'FAC002', numero: 'FAC-2026-0848', commercialId: 'COM002', clientId: 'CLI005', clientNom: 'Chez Mama Pauline', dateFacture: new Date(), montantHT: 280000, tva: 53900, montantTTC: 333900, statut: 'PAYEE', type: 'FACTURE' },
  { id: 'FAC003', numero: 'AVO-2026-0012', commercialId: 'COM004', clientId: 'CLI004', clientNom: 'SuperMarché Mahima', dateFacture: new Date(Date.now() - 86400000), montantHT: 150000, tva: 28875, montantTTC: 178875, statut: 'EMISE', type: 'AVOIR' },
  { id: 'FAC004', numero: 'FAC-2026-0849', commercialId: 'COM004', clientId: 'CLI008', clientNom: 'Resto Douala Palace', dateFacture: new Date(), montantHT: 840000, tva: 161700, montantTTC: 1001700, statut: 'EMISE', type: 'FACTURE' },
  { id: 'FAC005', numero: 'FAC-2026-0850', commercialId: 'COM005', clientId: 'CLI012', clientNom: 'Import-Export Tala', dateFacture: new Date(), montantHT: 1200000, tva: 231000, montantTTC: 1431000, statut: 'PAYEE', type: 'FACTURE' },
];

// ── PARAMÈTRES SYSTÈME ────────────────────────────────────
export const MOCK_PARAMETRES = {
  tvaTaux: 19.25,
  primeMontant: 12000,
  alerteStockSeuil: 20,
  zones: ['Yaoundé Centre', 'Yaoundé Nord', 'Yaoundé Sud', 'Douala Akwa', 'Douala Bonabéri'],
  gammeProduits: ['Snacking', 'Bouillie', 'Choco', 'Seau', 'BBV', 'Assortis', 'Huile'],
  villes: ['Yaoundé', 'Douala', 'Bertoua', 'Bafoussam'],
};

// ═══════════════════════════════════════════════════════════
//  DONNÉES DE PILOTAGE — DG / Comptable / Contrôle / RP / Prod
// ═══════════════════════════════════════════════════════════

// ── STOCK CONSOLIDÉ 3 MAGASINS (valorisation PMP) ─────────
export const MOCK_STOCK_MAGASINS = [
  { code: 'MP',  nom: 'Matières Premières', icon: 'fa-wheat-awn', valeurPMP: 18450000, nbArticles: 24, nbAlertes: 3, tauxRemplissage: 72 },
  { code: 'CONS', nom: 'Consommables & Emballages', icon: 'fa-box-open', valeurPMP: 6320000, nbArticles: 41, nbAlertes: 5, tauxRemplissage: 58 },
  { code: 'PF',  nom: 'Produits Finis', icon: 'fa-boxes-stacked', valeurPMP: 24780000, nbArticles: 32, nbAlertes: 2, tauxRemplissage: 81 },
];

// ── PERFORMANCE PAR GAMME (RP / DG) ───────────────────────
export const MOCK_PERF_GAMME = [
  { gamme: 'Bouillie',  ca: 6850000, objectif: 7000000, marge: 38, stock: 'OK' },
  { gamme: 'Snacking',  ca: 4920000, objectif: 4500000, marge: 42, stock: 'OK' },
  { gamme: 'Choco',     ca: 3680000, objectif: 4200000, marge: 35, stock: 'FAIBLE' },
  { gamme: 'Seau',      ca: 1850000, objectif: 2000000, marge: 28, stock: 'OK' },
  { gamme: 'BBV',       ca: 920000,  objectif: 1500000, marge: 31, stock: 'PEREMPTION' },
  { gamme: 'Assortis',  ca: 570000,  objectif: 800000,  marge: 33, stock: 'OK' },
];

// ── PRIMES HEBDOMADAIRES (double seuil 80% global / 75% gamme) ──
export const MOCK_PRIMES = [
  { commercialId: 'COM004', nom: 'Nkomo Patricia',     tauxGlobal: 93, gammesValidees: 5, gammesTotal: 6, eligible: true,  montant: 12000 },
  { commercialId: 'COM002', nom: 'Bella Marie-Claire', tauxGlobal: 103, gammesValidees: 6, gammesTotal: 6, eligible: true,  montant: 12000 },
  { commercialId: 'COM005', nom: 'Tagne Robert',       tauxGlobal: 94, gammesValidees: 4, gammesTotal: 6, eligible: false, montant: 0 },
  { commercialId: 'COM001', nom: 'Akono Jean-Pierre',  tauxGlobal: 71, gammesValidees: 3, gammesTotal: 6, eligible: false, montant: 0 },
  { commercialId: 'COM003', nom: 'Fouda Simon',        tauxGlobal: 76, gammesValidees: 3, gammesTotal: 6, eligible: false, montant: 0 },
];

// ── OCCUPATION DES MARCHÉS (boutiques livrées / répertoriées) ──
export const MOCK_OCCUPATION_MARCHES = [
  { zone: 'Yaoundé Centre', commercialId: 'COM001', livrees: 42, repertoriees: 95,  potentiel: 'ÉLEVÉ' },
  { zone: 'Yaoundé Nord',   commercialId: 'COM002', livrees: 68, repertoriees: 80,  potentiel: 'SATURÉ' },
  { zone: 'Yaoundé Sud',    commercialId: 'COM003', livrees: 31, repertoriees: 110, potentiel: 'ÉLEVÉ' },
  { zone: 'Douala Akwa',    commercialId: 'COM004', livrees: 75, repertoriees: 120, potentiel: 'MOYEN' },
  { zone: 'Douala Bonabéri',commercialId: 'COM005', livrees: 54, repertoriees: 90,  potentiel: 'MOYEN' },
];

// ── GRANDS COMPTES (CRM RP) ───────────────────────────────
export const MOCK_GRANDS_COMPTES = [
  { id: 'GC001', nom: 'Hôtel Mont Fébé', type: 'HÔTEL', contact: 'M. Eyenga', telephone: '+237 699 300 001', caMensuel: 1850000, derniereCommande: new Date(Date.now() - 86400000 * 28), prochaineRelance: new Date(Date.now() + 86400000 * 2), statut: 'RELANCE_DUE' },
  { id: 'GC002', nom: 'Supermarché Santa Lucia', type: 'GMS', contact: 'Mme Foning', telephone: '+237 699 300 002', caMensuel: 4200000, derniereCommande: new Date(Date.now() - 86400000 * 6), prochaineRelance: new Date(Date.now() + 86400000 * 12), statut: 'ACTIF' },
  { id: 'GC003', nom: 'Carrefour Market Douala', type: 'GMS', contact: 'M. Tchato', telephone: '+237 699 300 003', caMensuel: 5600000, derniereCommande: new Date(Date.now() - 86400000 * 3), prochaineRelance: new Date(Date.now() + 86400000 * 18), statut: 'ACTIF' },
  { id: 'GC004', nom: 'Boulangerie Calafatas', type: 'BOULANGERIE', contact: 'M. Nana', telephone: '+237 699 300 004', caMensuel: 980000, derniereCommande: new Date(Date.now() - 86400000 * 33), prochaineRelance: new Date(Date.now() - 86400000 * 1), statut: 'RELANCE_DUE' },
  { id: 'GC005', nom: 'Hôtel Hilton Yaoundé', type: 'HÔTEL', contact: 'Mme Abena', telephone: '+237 699 300 005', caMensuel: 2400000, derniereCommande: new Date(Date.now() - 86400000 * 10), prochaineRelance: new Date(Date.now() + 86400000 * 8), statut: 'ACTIF' },
];

// ── CAMPAGNES MARKETING ───────────────────────────────────
export const MOCK_CAMPAGNES = [
  { id: 'CAM001', titre: 'Promo Bouillie Soja -15%', cible: 'Détaillants Yaoundé', canal: 'WhatsApp Business', statut: 'EN_COURS', datePublication: new Date(Date.now() - 86400000 * 2), portee: 320 },
  { id: 'CAM002', titre: 'Déstockage BBV (péremption proche)', cible: 'Tous commerciaux', canal: 'App mobile', statut: 'PLANIFIEE', datePublication: new Date(Date.now() + 86400000 * 1), portee: 0 },
  { id: 'CAM003', titre: 'Visuel Tanty Choco fêtes', cible: 'Grands comptes GMS', canal: 'Email + visuel', statut: 'TERMINEE', datePublication: new Date(Date.now() - 86400000 * 15), portee: 12 },
];

// ── ANOMALIES / DEMANDES BIZARRES (DG / Contrôle) ─────────
export const MOCK_ANOMALIES = [
  { id: 'ANO001', type: 'MODIFICATION_CHIFFRE', utilisateur: 'Akono Jean-Pierre', role: 'COMMERCIAL', description: '4 demandes de modification du CA déclaré en 3 jours', occurrences: 4, niveau: 'ELEVE', date: new Date() },
  { id: 'ANO002', type: 'ANNULATION_FACTURE', utilisateur: 'Fouda Simon', role: 'COMMERCIAL', description: 'Tentative d\'annulation facture FAC déjà versée', occurrences: 2, niveau: 'MOYEN', date: new Date(Date.now() - 86400000) },
  { id: 'ANO003', type: 'ECART_CAISSE', utilisateur: 'Akono Jean-Pierre', role: 'COMMERCIAL', description: 'Écart caisse récurrent non justifié (-50 000)', occurrences: 3, niveau: 'ELEVE', date: new Date() },
  { id: 'ANO004', type: 'CARBURANT_CA', utilisateur: 'Tagne Robert', role: 'COMMERCIAL', description: 'Consommation carburant > moyenne zone (+38%) vs CA faible', occurrences: 1, niveau: 'MOYEN', date: new Date(Date.now() - 86400000 * 2) },
];

// ── MARGES PAR PRODUIT (Contrôle / Comptable) ─────────────
export const MOCK_MARGES_PRODUIT = [
  { code: 'TBS',   designation: 'Tanty Bouillie Soja', prixVente: 9000,  coutRevient: 5580, ventesMois: 920 },
  { code: 'MGASS', designation: 'Mini Grigno Arachide', prixVente: 8500,  coutRevient: 4930, ventesMois: 1240 },
  { code: 'TCH',   designation: 'Tanty Choco', prixVente: 15000, coutRevient: 9750, ventesMois: 410 },
  { code: 'RCP2L', designation: 'Récipient 2 Litres', prixVente: 18000, coutRevient: 12960, ventesMois: 180 },
  { code: 'BBV1L', designation: 'BBV 1 Litre', prixVente: 13000, coutRevient: 8970, ventesMois: 95 },
  { code: 'PTBS',  designation: 'Petite Tanty Bouillie', prixVente: 6500,  coutRevient: 4355, ventesMois: 760 },
];

// ── BUDGET / ENGAGEMENTS (Contrôle) ───────────────────────
export const MOCK_BUDGET = [
  { poste: 'Achats matières premières', budget: 22000000, engage: 18450000, realise: 16200000 },
  { poste: 'Carburant & logistique', budget: 3500000, engage: 3180000, realise: 2890000 },
  { poste: 'Masse salariale production', budget: 8000000, engage: 7600000, realise: 7600000 },
  { poste: 'Emballages & consommables', budget: 5000000, engage: 5320000, realise: 4100000 },
  { poste: 'Frais généraux', budget: 2200000, engage: 1450000, realise: 1320000 },
];

// ── VARIANCE COÛTS STANDARD vs RÉEL (Contrôle / Comptable) ──
export const MOCK_VARIANCE = [
  { poste: 'Coût matière / kg poudre', standard: 1150, reel: 1208, unite: 'FCFA/kg' },
  { poste: 'Main d\'œuvre / lot', standard: 18000, reel: 17200, unite: 'FCFA/lot' },
  { poste: 'Taux de rebut broyage', standard: 2.5, reel: 3.2, unite: '%' },
  { poste: 'Frais généraux / unité', standard: 850, reel: 910, unite: 'FCFA/u' },
];

// ── CHARGES COMPTABLES ────────────────────────────────────
export const MOCK_CHARGES = [
  { categorie: 'Achats matières premières', montant: 16200000, part: 52 },
  { categorie: 'Masse salariale', montant: 7600000, part: 24 },
  { categorie: 'Emballages', montant: 4100000, part: 13 },
  { categorie: 'Carburant & transport', montant: 2890000, part: 9 },
  { categorie: 'Frais généraux', montant: 1320000, part: 4 },
];

// ── PETITE CAISSE SECRÉTARIAT ─────────────────────────────
export const MOCK_PETITE_CAISSE = [
  { id: 'PC001', libelle: 'Recettes ventes comptant', type: 'ENTREE', montant: 142000, heure: '11:20', categorie: 'VENTE' },
  { id: 'PC002', libelle: 'Achat café & sucre accueil', type: 'SORTIE', montant: 3500, heure: '09:15', categorie: 'DEPENSE' },
  { id: 'PC003', libelle: 'Photocopies dossiers clients', type: 'SORTIE', montant: 2000, heure: '10:40', categorie: 'DEPENSE' },
  { id: 'PC004', libelle: 'Encaissement recouvrement CLI006', type: 'ENTREE', montant: 100000, heure: '14:05', categorie: 'RECOUVREMENT' },
  { id: 'PC005', libelle: 'Demande fonds COM003 (carburant)', type: 'SORTIE', montant: 75000, heure: '15:30', categorie: 'DEMANDE_FONDS' },
];

// ── MICRO-STOCK SECRÉTARIAT (mini-snacks accueil) ─────────
export const MOCK_MICRO_STOCK = [
  { code: 'MGASS', designation: 'Mini Grigno Arachide', qte: 24, prixUnite: 250, seuil: 10 },
  { code: 'MGPL',  designation: 'Mini Grigno Piment', qte: 8,  prixUnite: 250, seuil: 10 },
  { code: 'PTBS',  designation: 'Petite Tanty Bouillie', qte: 15, prixUnite: 500, seuil: 8 },
  { code: 'TCH',   designation: 'Tanty Choco (unité)', qte: 6,  prixUnite: 1500, seuil: 5 },
];

// ── RÉCEPTION DOCUMENTS TERRAIN (Secrétaire Caisse) ──────
export const MOCK_DOCUMENTS_RECUS = [
  { commercialId: 'COM001', nom: 'Akono Jean-Pierre', feuilleRoute: true, bonCommande: true, factures: false },
  { commercialId: 'COM002', nom: 'Bella Marie-Claire', feuilleRoute: true, bonCommande: true, factures: true },
  { commercialId: 'COM003', nom: 'Fouda Simon', feuilleRoute: false, bonCommande: false, factures: false },
  { commercialId: 'COM004', nom: 'Nkomo Patricia', feuilleRoute: true, bonCommande: true, factures: true },
  { commercialId: 'COM005', nom: 'Tagne Robert', feuilleRoute: true, bonCommande: false, factures: true },
];

// ── ORDRES DE FABRICATION (Production) ────────────────────
export const MOCK_ORDRES_FABRICATION = [
  { id: 'OF001', codeProduit: 'MGASS', designation: 'Mini Grigno Arachide', qteDemandee: 120, qteRealisee: 80, statut: 'EN_COURS', dateButoir: new Date(Date.now() + 86400000 * 2) },
  { id: 'OF002', codeProduit: 'TBS', designation: 'Tanty Bouillie Soja', qteDemandee: 80, qteRealisee: 80, statut: 'HONORE', dateButoir: new Date(Date.now() - 86400000) },
  { id: 'OF003', codeProduit: 'TCH', designation: 'Tanty Choco', qteDemandee: 40, qteRealisee: 12, statut: 'EN_COURS', dateButoir: new Date(Date.now() + 86400000 * 1) },
  { id: 'OF004', codeProduit: 'PTBS', designation: 'Petite Tanty Bouillie', qteDemandee: 100, qteRealisee: 0, statut: 'PLANIFIE', dateButoir: new Date(Date.now() + 86400000 * 3) },
  { id: 'OF005', codeProduit: 'RCP2L', designation: 'Récipient 2 Litres', qteDemandee: 30, qteRealisee: 30, statut: 'HONORE', dateButoir: new Date(Date.now() - 86400000 * 2) },
];

// ── MAINTENANCE MATÉRIEL (Production) ─────────────────────
export const MOCK_MAINTENANCE = [
  { equipement: 'Broyeuse BRY-01', etat: 'OPERATIONNEL', derniereMaint: new Date(Date.now() - 86400000 * 12), prochaineMaint: new Date(Date.now() + 86400000 * 18) },
  { equipement: 'Broyeuse BRY-02', etat: 'OPERATIONNEL', derniereMaint: new Date(Date.now() - 86400000 * 20), prochaineMaint: new Date(Date.now() + 86400000 * 10) },
  { equipement: 'Doseuse DOS-01', etat: 'MAINTENANCE_DUE', derniereMaint: new Date(Date.now() - 86400000 * 55), prochaineMaint: new Date(Date.now() - 86400000 * 5) },
  { equipement: 'Conditionneuse CND-01', etat: 'PANNE', derniereMaint: new Date(Date.now() - 86400000 * 8), prochaineMaint: new Date(Date.now() + 86400000 * 1) },
];

// ── PRODUCTION KPI INDUSTRIELS ────────────────────────────
export const MOCK_PRODUCTION_KPI = {
  quantiteProduiteKg: 4850,
  quantiteConditionneeCartons: 312,
  rendementMatiere: 96.8,
  tauxRebut: 3.2,
  trs: 78,
  productiviteHoraire: 606,
  valorisationJour: 8420000,
  effectifPresent: 5,
  effectifTotal: 6,
};

// ── HELPERS ───────────────────────────────────────────────
export function fCFA(n: number): string {
  return new Intl.NumberFormat('fr-CM').format(Math.round(n)) + ' FCFA';
}

export function tauxAtteinte(ca: number, objectif: number): number {
  return Math.round((ca / objectif) * 100);
}

export function getPrimeEligible(ca: number, objectif: number): boolean {
  return ca >= objectif;
}
