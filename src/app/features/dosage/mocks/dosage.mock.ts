const MOCK = {
  sessions: [
    // ── PPH-2026-006-S1 (semaine 1) ──────────────────────────────────────
    {id:1,referencePPH:'PPH-2026-006-S1',date:'2026-06-09',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:10,poidsNetTotalKg:500,compositionConforme:true,avertissementComposition:null,composition:{maisKg:400,sojaKg:90,arachideKg:10}},
    {id:2,referencePPH:'PPH-2026-006-S1',date:'2026-06-09',agentMatricule:'TAN2026014',agentNom:'Josephine MANGA',typeMelange:'MELANGE_ARACHIDE' as const,typeMelangeLibelle:'Mélange Arachide',nombreFuts:6,poidsNetTotalKg:300,compositionConforme:true,avertissementComposition:null,composition:{maisKg:240,sojaKg:6,arachideKg:54}},
    {id:3,referencePPH:'PPH-2026-006-S1',date:'2026-06-10',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:12,poidsNetTotalKg:600,compositionConforme:true,avertissementComposition:null,composition:{maisKg:480,sojaKg:108,arachideKg:12}},
    {id:4,referencePPH:'PPH-2026-006-S1',date:'2026-06-10',agentMatricule:'TAN2026014',agentNom:'Josephine MANGA',typeMelange:'MELANGE_ARACHIDE' as const,typeMelangeLibelle:'Mélange Arachide',nombreFuts:8,poidsNetTotalKg:400,compositionConforme:true,avertissementComposition:null,composition:{maisKg:320,sojaKg:8,arachideKg:72}},
    {id:5,referencePPH:'PPH-2026-006-S1',date:'2026-06-11',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:10,poidsNetTotalKg:500,compositionConforme:true,avertissementComposition:null,composition:{maisKg:400,sojaKg:90,arachideKg:10}},
    {id:6,referencePPH:'PPH-2026-006-S1',date:'2026-06-12',agentMatricule:'TAN2026010',agentNom:'Aurelie MESSI',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:8,poidsNetTotalKg:400,compositionConforme:false,avertissementComposition:'Ratio maïs inférieur à la norme — 38 kg au lieu de 40 kg',composition:{maisKg:304,sojaKg:72,arachideKg:24}},
    {id:7,referencePPH:'PPH-2026-006-S1',date:'2026-06-13',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:11,poidsNetTotalKg:550,compositionConforme:true,avertissementComposition:null,composition:{maisKg:440,sojaKg:99,arachideKg:11}},
    {id:8,referencePPH:'PPH-2026-006-S1',date:'2026-06-13',agentMatricule:'TAN2026014',agentNom:'Josephine MANGA',typeMelange:'MELANGE_ARACHIDE' as const,typeMelangeLibelle:'Mélange Arachide',nombreFuts:5,poidsNetTotalKg:250,compositionConforme:true,avertissementComposition:null,composition:{maisKg:200,sojaKg:5,arachideKg:45}},
    // ── PPH-2026-006-S2 (semaine 2) ──────────────────────────────────────
    {id:9,referencePPH:'PPH-2026-006-S2',date:'2026-06-16',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:12,poidsNetTotalKg:600,compositionConforme:true,avertissementComposition:null,composition:{maisKg:480,sojaKg:108,arachideKg:12}},
    {id:10,referencePPH:'PPH-2026-006-S2',date:'2026-06-16',agentMatricule:'TAN2026014',agentNom:'Josephine MANGA',typeMelange:'MELANGE_ARACHIDE' as const,typeMelangeLibelle:'Mélange Arachide',nombreFuts:7,poidsNetTotalKg:350,compositionConforme:true,avertissementComposition:null,composition:{maisKg:280,sojaKg:7,arachideKg:63}},
    {id:11,referencePPH:'PPH-2026-006-S2',date:'2026-06-17',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:10,poidsNetTotalKg:500,compositionConforme:true,avertissementComposition:null,composition:{maisKg:400,sojaKg:90,arachideKg:10}},
    {id:12,referencePPH:'PPH-2026-006-S2',date:'2026-06-17',agentMatricule:'TAN2026010',agentNom:'Aurelie MESSI',typeMelange:'MELANGE_ARACHIDE' as const,typeMelangeLibelle:'Mélange Arachide',nombreFuts:6,poidsNetTotalKg:300,compositionConforme:true,avertissementComposition:null,composition:{maisKg:240,sojaKg:6,arachideKg:54}},
    {id:13,referencePPH:'PPH-2026-006-S2',date:'2026-06-18',agentMatricule:'TAN2026006',agentNom:'Sandrine BIYA',typeMelange:'BOUILLIE' as const,typeMelangeLibelle:'Bouillie',nombreFuts:9,poidsNetTotalKg:450,compositionConforme:true,avertissementComposition:null,composition:{maisKg:360,sojaKg:81,arachideKg:9}}
  ],
  statistiques: {
    semaine: {
      dateDebut:'2026-06-16', dateFin:'2026-06-22',
      totalFuts:32, totalPoidsKg:1600,
      parType:[
        {typeMelange:'BOUILLIE' as const, nombreFuts:21, poidsKg:1050},
        {typeMelange:'MELANGE_ARACHIDE' as const, nombreFuts:11, poidsKg:550}
      ],
      evolutionTotalPct:14.3,
      semainePrecedente:{totalFuts:28, totalPoidsKg:1400, parType:[]}
    }
  }
};
export default MOCK;
