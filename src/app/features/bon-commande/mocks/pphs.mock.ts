import { PPH } from '../../../core/models';

const MOCK: PPH[] = [
  // ── BC-2026-001 (CLOS) 3 semaines ───────────────────────────────────────
  { id:1, reference:'PPH-2026-001-S1', referenceBC:'BC-2026-001', numeroSemaine:1, dateDebut:'2026-01-13', dateFin:'2026-01-19', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:1,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:67,quantiteCibleUnites:1667,quantiteRealiseeCartons:65,quantiteRealiseeUnites:1625,tauxRealisation:97.5},
      {id:2,produitId:'P002',produitCode:'BSC-25',produitNom:'REINE BOUILLIE SOJA CLASSIC',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:50,quantiteCibleUnites:1250,quantiteRealiseeCartons:50,quantiteRealiseeUnites:1250,tauxRealisation:100},
      {id:3,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:40,quantiteCibleUnites:1000,quantiteRealiseeCartons:38,quantiteRealiseeUnites:950,tauxRealisation:95},
      {id:4,produitId:'P028',produitCode:'TC2L',produitNom:'TANTY CHOCOLAT 2L',categorie:'CHOCOLAT',quantiteCibleCartons:27,quantiteCibleUnites:108,quantiteRealiseeCartons:27,quantiteRealiseeUnites:108,tauxRealisation:100}
    ], besoinsMatieresPremieres:{maisKg:480,sojaKg:108,arachideKg:12} },
  { id:2, reference:'PPH-2026-001-S2', referenceBC:'BC-2026-001', numeroSemaine:2, dateDebut:'2026-01-20', dateFin:'2026-01-26', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:5,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:67,quantiteCibleUnites:1667,quantiteRealiseeCartons:70,quantiteRealiseeUnites:1750,tauxRealisation:100},
      {id:6,produitId:'P002',produitCode:'BSC-25',produitNom:'REINE BOUILLIE SOJA CLASSIC',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:50,quantiteCibleUnites:1250,quantiteRealiseeCartons:48,quantiteRealiseeUnites:1200,tauxRealisation:96},
      {id:7,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:40,quantiteCibleUnites:1000,quantiteRealiseeCartons:40,quantiteRealiseeUnites:1000,tauxRealisation:100},
      {id:8,produitId:'P028',produitCode:'TC2L',produitNom:'TANTY CHOCOLAT 2L',categorie:'CHOCOLAT',quantiteCibleCartons:27,quantiteCibleUnites:108,quantiteRealiseeCartons:25,quantiteRealiseeUnites:100,tauxRealisation:92.6}
    ], besoinsMatieresPremieres:{maisKg:480,sojaKg:108,arachideKg:12} },
  { id:3, reference:'PPH-2026-001-S3', referenceBC:'BC-2026-001', numeroSemaine:3, dateDebut:'2026-01-27', dateFin:'2026-02-02', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:9,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:66,quantiteCibleUnites:1650,quantiteRealiseeCartons:66,quantiteRealiseeUnites:1650,tauxRealisation:100},
      {id:10,produitId:'P002',produitCode:'BSC-25',produitNom:'REINE BOUILLIE SOJA CLASSIC',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:50,quantiteCibleUnites:1250,quantiteRealiseeCartons:52,quantiteRealiseeUnites:1300,tauxRealisation:100},
      {id:11,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:40,quantiteCibleUnites:1000,quantiteRealiseeCartons:39,quantiteRealiseeUnites:975,tauxRealisation:97.5},
      {id:12,produitId:'P028',produitCode:'TC2L',produitNom:'TANTY CHOCOLAT 2L',categorie:'CHOCOLAT',quantiteCibleCartons:26,quantiteCibleUnites:104,quantiteRealiseeCartons:26,quantiteRealiseeUnites:104,tauxRealisation:100}
    ], besoinsMatieresPremieres:{maisKg:480,sojaKg:108,arachideKg:12} },
  // ── BC-2026-002 (CLOS) 3 semaines ───────────────────────────────────────
  { id:4, reference:'PPH-2026-002-S1', referenceBC:'BC-2026-002', numeroSemaine:1, dateDebut:'2026-02-10', dateFin:'2026-02-16', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:13,produitId:'P011',produitCode:'PRBA-12',produitNom:'PRESTIGE REINE BOUILLIE ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:60,quantiteCibleUnites:720,quantiteRealiseeCartons:58,quantiteRealiseeUnites:696,tauxRealisation:96.7},
      {id:14,produitId:'P016',produitCode:'PTBA-12',produitNom:'PRESTIGE TANTY BOUILLIE ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:50,quantiteCibleUnites:600,quantiteRealiseeCartons:50,quantiteRealiseeUnites:600,tauxRealisation:100},
      {id:15,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',quantiteCibleCartons:67,quantiteCibleUnites:5360,quantiteRealiseeCartons:65,quantiteRealiseeUnites:5200,tauxRealisation:97},
      {id:16,produitId:'P033',produitCode:'CAR-MINI',produitNom:'MINI A GRIGNOTER CARAMELS',categorie:'A_GRIGNOTER',quantiteCibleCartons:54,quantiteCibleUnites:4320,quantiteRealiseeCartons:54,quantiteRealiseeUnites:4320,tauxRealisation:100}
    ], besoinsMatieresPremieres:{maisKg:520,sojaKg:117,arachideKg:13} },
  { id:5, reference:'PPH-2026-002-S2', referenceBC:'BC-2026-002', numeroSemaine:2, dateDebut:'2026-02-17', dateFin:'2026-02-23', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:17,produitId:'P011',produitCode:'PRBA-12',produitNom:'PRESTIGE REINE BOUILLIE ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:60,quantiteCibleUnites:720,quantiteRealiseeCartons:62,quantiteRealiseeUnites:744,tauxRealisation:100},
      {id:18,produitId:'P016',produitCode:'PTBA-12',produitNom:'PRESTIGE TANTY BOUILLIE ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:50,quantiteCibleUnites:600,quantiteRealiseeCartons:47,quantiteRealiseeUnites:564,tauxRealisation:94},
      {id:19,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',quantiteCibleCartons:66,quantiteCibleUnites:5280,quantiteRealiseeCartons:66,quantiteRealiseeUnites:5280,tauxRealisation:100},
      {id:20,produitId:'P033',produitCode:'CAR-MINI',produitNom:'MINI A GRIGNOTER CARAMELS',categorie:'A_GRIGNOTER',quantiteCibleCartons:53,quantiteCibleUnites:4240,quantiteRealiseeCartons:50,quantiteRealiseeUnites:4000,tauxRealisation:94.3}
    ], besoinsMatieresPremieres:{maisKg:520,sojaKg:117,arachideKg:13} },
  { id:6, reference:'PPH-2026-002-S3', referenceBC:'BC-2026-002', numeroSemaine:3, dateDebut:'2026-02-24', dateFin:'2026-03-02', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:21,produitId:'P011',produitCode:'PRBA-12',produitNom:'PRESTIGE REINE BOUILLIE ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:60,quantiteCibleUnites:720,quantiteRealiseeCartons:59,quantiteRealiseeUnites:708,tauxRealisation:98.3},
      {id:22,produitId:'P016',produitCode:'PTBA-12',produitNom:'PRESTIGE TANTY BOUILLIE ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:50,quantiteCibleUnites:600,quantiteRealiseeCartons:51,quantiteRealiseeUnites:612,tauxRealisation:100},
      {id:23,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',quantiteCibleCartons:67,quantiteCibleUnites:5360,quantiteRealiseeCartons:67,quantiteRealiseeUnites:5360,tauxRealisation:100},
      {id:24,produitId:'P033',produitCode:'CAR-MINI',produitNom:'MINI A GRIGNOTER CARAMELS',categorie:'A_GRIGNOTER',quantiteCibleCartons:53,quantiteCibleUnites:4240,quantiteRealiseeCartons:53,quantiteRealiseeUnites:4240,tauxRealisation:100}
    ], besoinsMatieresPremieres:{maisKg:520,sojaKg:117,arachideKg:13} },
  // ── BC-2026-006 EN COURS ─────────────────────────────────────────────────
  { id:16, reference:'PPH-2026-006-S1', referenceBC:'BC-2026-006', numeroSemaine:1, dateDebut:'2026-06-09', dateFin:'2026-06-15', statut:'TERMINE', statutLibelle:'Terminé',
    lignes:[
      {id:50,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:40,quantiteCibleUnites:1000,quantiteRealiseeCartons:38,quantiteRealiseeUnites:950,tauxRealisation:95},
      {id:51,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:27,quantiteCibleUnites:667,quantiteRealiseeCartons:27,quantiteRealiseeUnites:667,tauxRealisation:100},
      {id:52,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:34,quantiteCibleUnites:834,quantiteRealiseeCartons:32,quantiteRealiseeUnites:800,tauxRealisation:95.9},
      {id:53,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',quantiteCibleCartons:17,quantiteCibleUnites:1334,quantiteRealiseeCartons:17,quantiteRealiseeUnites:1334,tauxRealisation:100},
      {id:54,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',quantiteCibleCartons:20,quantiteCibleUnites:1600,quantiteRealiseeCartons:19,quantiteRealiseeUnites:1520,tauxRealisation:95}
    ], besoinsMatieresPremieres:{maisKg:520,sojaKg:117,arachideKg:13} },
  { id:17, reference:'PPH-2026-006-S2', referenceBC:'BC-2026-006', numeroSemaine:2, dateDebut:'2026-06-16', dateFin:'2026-06-22', statut:'EN_COURS', statutLibelle:'En cours',
    lignes:[
      {id:55,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:40,quantiteCibleUnites:1000,quantiteRealiseeCartons:18,quantiteRealiseeUnites:450,tauxRealisation:45},
      {id:56,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:27,quantiteCibleUnites:667,quantiteRealiseeCartons:12,quantiteRealiseeUnites:300,tauxRealisation:45},
      {id:57,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:33,quantiteCibleUnites:833,quantiteRealiseeCartons:15,quantiteRealiseeUnites:375,tauxRealisation:45},
      {id:58,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',quantiteCibleCartons:17,quantiteCibleUnites:1333,quantiteRealiseeCartons:8,quantiteRealiseeUnites:640,tauxRealisation:48},
      {id:59,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',quantiteCibleCartons:20,quantiteCibleUnites:1600,quantiteRealiseeCartons:9,quantiteRealiseeUnites:720,tauxRealisation:45}
    ], besoinsMatieresPremieres:{maisKg:520,sojaKg:117,arachideKg:13} },
  { id:18, reference:'PPH-2026-006-S3', referenceBC:'BC-2026-006', numeroSemaine:3, dateDebut:'2026-06-23', dateFin:'2026-06-29', statut:'PLANIFIE', statutLibelle:'Planifié',
    lignes:[
      {id:60,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:40,quantiteCibleUnites:1000,quantiteRealiseeCartons:0,quantiteRealiseeUnites:0,tauxRealisation:0},
      {id:61,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:26,quantiteCibleUnites:666,quantiteRealiseeCartons:0,quantiteRealiseeUnites:0,tauxRealisation:0},
      {id:62,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',quantiteCibleCartons:33,quantiteCibleUnites:833,quantiteRealiseeCartons:0,quantiteRealiseeUnites:0,tauxRealisation:0},
      {id:63,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',quantiteCibleCartons:16,quantiteCibleUnites:1333,quantiteRealiseeCartons:0,quantiteRealiseeUnites:0,tauxRealisation:0},
      {id:64,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',quantiteCibleCartons:20,quantiteCibleUnites:1600,quantiteRealiseeCartons:0,quantiteRealiseeUnites:0,tauxRealisation:0}
    ], besoinsMatieresPremieres:{maisKg:520,sojaKg:117,arachideKg:13} }
];
export default MOCK;
