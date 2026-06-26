const MOCK = {
  fiches: [
    // ── Semaine 1 PPH-2026-006-S1 ────────────────────────────────────────
    {id:1,referencePPH:'PPH-2026-006-S1',date:'2026-06-09',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',totalProduit:2817,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:6,quantiteUnites:150,observation:null},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:7,quantiteUnites:175,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:4,quantiteUnites:320,observation:null},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:4,quantiteUnites:320,observation:null}
      ]},
    {id:2,referencePPH:'PPH-2026-006-S1',date:'2026-06-10',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',totalProduit:3090,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:9,quantiteUnites:225,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:7,quantiteUnites:175,observation:null},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:5,quantiteUnites:400,observation:null},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:5,quantiteUnites:400,observation:null}
      ]},
    {id:3,referencePPH:'PPH-2026-006-S1',date:'2026-06-11',agentMatricule:'TAN2026009',agentNom:'Gilles TSIMI',totalProduit:2975,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:7,quantiteUnites:175,observation:'Pénurie temporaire de sachets — 2 cartons en attente'},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:7,quantiteUnites:175,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:5,quantiteUnites:400,observation:null},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:5,quantiteUnites:400,observation:null}
      ]},
    {id:4,referencePPH:'PPH-2026-006-S1',date:'2026-06-12',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',totalProduit:2560,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:7,quantiteUnites:175,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:5,quantiteUnites:125,observation:null},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:6,quantiteUnites:150,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:3,quantiteUnites:240,observation:'Machine en maintenance partielle matin'},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:4,quantiteUnites:320,observation:null}
      ]},
    {id:5,referencePPH:'PPH-2026-006-S1',date:'2026-06-13',agentMatricule:'TAN2026009',agentNom:'Gilles TSIMI',totalProduit:3200,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:9,quantiteUnites:225,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:6,quantiteUnites:480,observation:null},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:6,quantiteUnites:480,observation:null}
      ]},
    // ── Semaine 2 PPH-2026-006-S2 ────────────────────────────────────────
    {id:6,referencePPH:'PPH-2026-006-S2',date:'2026-06-16',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',totalProduit:2885,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:6,quantiteUnites:150,observation:null},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:7,quantiteUnites:175,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:4,quantiteUnites:320,observation:null},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:4,quantiteUnites:320,observation:null}
      ]},
    {id:7,referencePPH:'PPH-2026-006-S2',date:'2026-06-17',agentMatricule:'TAN2026011',agentNom:'Christophe ABEGA',totalProduit:3100,
      lignes:[
        {produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteCartons:9,quantiteUnites:225,observation:null},
        {produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteCartons:7,quantiteUnites:175,observation:null},
        {produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteCartons:8,quantiteUnites:200,observation:null},
        {produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteCartons:5,quantiteUnites:400,observation:null},
        {produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteCartons:5,quantiteUnites:400,observation:null}
      ]}
  ],
  registres: [
    // ── Semaine 1 ─────────────────────────────────────────────────────────
    {id:1,referencePPH:'PPH-2026-006-S1',date:'2026-06-09',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',
      employes:[
        {nom:'BONGO Irène',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:350,unite:'sachets'},{produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteRealise:280,unite:'sachets'}],totalPoints:630,observation:null},
        {nom:'TAMBA Jean',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:300,unite:'sachets'},{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:250,unite:'sachets'}],totalPoints:550,observation:null},
        {nom:'MESSI Paul',produitsTravailles:[{produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteRealise:420,unite:'sachets'},{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:380,unite:'sachets'}],totalPoints:800,observation:null},
        {nom:'EKANE Sophie',produitsTravailles:[{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:290,unite:'sachets'}],totalPoints:290,observation:null},
        {nom:'NDOUMBE Robert',produitsTravailles:[{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:350,unite:'sachets'}],totalPoints:350,observation:'Absent matin — maladie'}
      ]},
    {id:2,referencePPH:'PPH-2026-006-S1',date:'2026-06-10',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',
      employes:[
        {nom:'BONGO Irène',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:400,unite:'sachets'},{produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteRealise:320,unite:'sachets'}],totalPoints:720,observation:null},
        {nom:'TAMBA Jean',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:360,unite:'sachets'},{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:280,unite:'sachets'}],totalPoints:640,observation:null},
        {nom:'MESSI Paul',produitsTravailles:[{produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteRealise:480,unite:'sachets'},{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:420,unite:'sachets'}],totalPoints:900,observation:null},
        {nom:'EKANE Sophie',produitsTravailles:[{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:320,unite:'sachets'}],totalPoints:320,observation:null},
        {nom:'NDOUMBE Robert',produitsTravailles:[{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:400,unite:'sachets'}],totalPoints:400,observation:null}
      ]},
    {id:3,referencePPH:'PPH-2026-006-S1',date:'2026-06-11',agentMatricule:'TAN2026009',agentNom:'Gilles TSIMI',
      employes:[
        {nom:'BONGO Irène',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:380,unite:'sachets'},{produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteRealise:310,unite:'sachets'}],totalPoints:690,observation:null},
        {nom:'TAMBA Jean',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:340,unite:'sachets'},{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:260,unite:'sachets'}],totalPoints:600,observation:null},
        {nom:'MESSI Paul',produitsTravailles:[{produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteRealise:460,unite:'sachets'},{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:400,unite:'sachets'}],totalPoints:860,observation:null},
        {nom:'EKANE Sophie',produitsTravailles:[{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:300,unite:'sachets'}],totalPoints:300,observation:null},
        {nom:'NDOUMBE Robert',produitsTravailles:[{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:410,unite:'sachets'}],totalPoints:410,observation:null}
      ]},
    {id:4,referencePPH:'PPH-2026-006-S2',date:'2026-06-16',agentMatricule:'TAN2026004',agentNom:'Bernadette ESSAMA',
      employes:[
        {nom:'BONGO Irène',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:360,unite:'sachets'},{produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantiteRealise:290,unite:'sachets'}],totalPoints:650,observation:null},
        {nom:'TAMBA Jean',produitsTravailles:[{produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantiteRealise:320,unite:'sachets'},{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:270,unite:'sachets'}],totalPoints:590,observation:null},
        {nom:'MESSI Paul',produitsTravailles:[{produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantiteRealise:440,unite:'sachets'},{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:390,unite:'sachets'}],totalPoints:830,observation:null},
        {nom:'EKANE Sophie',produitsTravailles:[{produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantiteRealise:285,unite:'sachets'}],totalPoints:285,observation:null},
        {nom:'NDOUMBE Robert',produitsTravailles:[{produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantiteRealise:380,unite:'sachets'}],totalPoints:380,observation:null}
      ]}
  ],
  rapportsHebdomadaires: [
    {
      referencePPH:'PPH-2026-006-S1', numeroSemaine:24, annee:2026,
      dateDebut:'2026-06-09', dateFin:'2026-06-13',
      totalPredite:14642, totalRealisee:14642, tauxRealisation:97.8,
      classementEmployes:[
        {rang:1,nom:'MESSI Paul',totalProduction:3450,scoreMoyen:99.2},
        {rang:2,nom:'BONGO Irène',totalProduction:3030,scoreMoyen:97.8},
        {rang:3,nom:'TAMBA Jean',produits:'multiple',totalProduction:2790,scoreMoyen:96.5},
        {rang:4,nom:'NDOUMBE Robert',totalProduction:1540,scoreMoyen:94.8},
        {rang:5,nom:'EKANE Sophie',totalProduction:1300,scoreMoyen:93.2}
      ],
      lignes:[
        {produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantitePredite:1000,quantiteRealisee:950,tauxRealisation:95,ecart:-50},
        {produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantitePredite:667,quantiteRealisee:667,tauxRealisation:100,ecart:0},
        {produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantitePredite:834,quantiteRealisee:800,tauxRealisation:95.9,ecart:-34},
        {produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantitePredite:1334,quantiteRealisee:1334,tauxRealisation:100,ecart:0},
        {produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantitePredite:1600,quantiteRealisee:1520,tauxRealisation:95,ecart:-80}
      ]
    },
    {
      referencePPH:'PPH-2026-006-S2', numeroSemaine:25, annee:2026,
      dateDebut:'2026-06-16', dateFin:'2026-06-20',
      totalPredite:5433, totalRealisee:2485, tauxRealisation:45.7,
      classementEmployes:[
        {rang:1,nom:'MESSI Paul',totalProduction:1230,scoreMoyen:98.0},
        {rang:2,nom:'BONGO Irène',totalProduction:840,scoreMoyen:96.5},
        {rang:3,nom:'TAMBA Jean',totalProduction:590,scoreMoyen:95.2},
        {rang:4,nom:'NDOUMBE Robert',totalProduction:380,scoreMoyen:93.0},
        {rang:5,nom:'EKANE Sophie',totalProduction:285,scoreMoyen:91.5}
      ],
      lignes:[
        {produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',quantitePredite:1000,quantiteRealisee:450,tauxRealisation:45,ecart:-550},
        {produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',quantitePredite:667,quantiteRealisee:300,tauxRealisation:45,ecart:-367},
        {produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',quantitePredite:833,quantiteRealisee:375,tauxRealisation:45,ecart:-458},
        {produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',quantitePredite:1333,quantiteRealisee:640,tauxRealisation:48,ecart:-693},
        {produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',quantitePredite:1600,quantiteRealisee:720,tauxRealisation:45,ecart:-880}
      ]
    }
  ]
};
export default MOCK;
