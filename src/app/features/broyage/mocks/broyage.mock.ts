const MOCK = {
  journeesProduction: [
    // ── Semaine 1 — PPH-2026-006-S1 ──────────────────────────────────────
    { id:1, referencePPH:'PPH-2026-006-S1', date:'2026-06-09', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:485.5, tauxPerteGlobalPourcentage:2.9,
      machinistes:[
        {id:1,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:243,perteKg:7,tauxPertePct:2.8,score:97.2,correction:null},
        {id:2,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:242.5,perteKg:7.5,tauxPertePct:3.0,score:97.0,correction:null}
      ]},
    { id:2, referencePPH:'PPH-2026-006-S1', date:'2026-06-10', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:490.0, tauxPerteGlobalPourcentage:2.0,
      machinistes:[
        {id:3,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:246,perteKg:4,tauxPertePct:1.6,score:98.4,correction:null},
        {id:4,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:244,perteKg:6,tauxPertePct:2.4,score:97.6,correction:null}
      ]},
    { id:3, referencePPH:'PPH-2026-006-S1', date:'2026-06-11', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:495.0, tauxPerteGlobalPourcentage:1.0,
      machinistes:[
        {id:5,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:248.5,perteKg:1.5,tauxPertePct:0.6,score:99.4,correction:null},
        {id:6,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:246.5,perteKg:3.5,tauxPertePct:1.4,score:98.6,correction:null}
      ]},
    { id:4, referencePPH:'PPH-2026-006-S1', date:'2026-06-12', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:481.0, tauxPerteGlobalPourcentage:3.8,
      machinistes:[
        {id:7,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:241,perteKg:9,tauxPertePct:3.6,score:96.4,correction:null},
        {id:8,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:240,perteKg:10,tauxPertePct:4.0,score:96.0,correction:'Problème mécanique — billes de broyage usées. Remplacement effectué.'}
      ]},
    { id:5, referencePPH:'PPH-2026-006-S1', date:'2026-06-13', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:492.0, tauxPerteGlobalPourcentage:1.6,
      machinistes:[
        {id:9,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:247,perteKg:3,tauxPertePct:1.2,score:98.8,correction:null},
        {id:10,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:245,perteKg:5,tauxPertePct:2.0,score:98.0,correction:null}
      ]},
    // ── Semaine 2 — PPH-2026-006-S2 ──────────────────────────────────────
    { id:6, referencePPH:'PPH-2026-006-S2', date:'2026-06-16', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:488.0, tauxPerteGlobalPourcentage:2.4,
      machinistes:[
        {id:11,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:244,perteKg:6,tauxPertePct:2.4,score:97.6,correction:null},
        {id:12,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:244,perteKg:6,tauxPertePct:2.4,score:97.6,correction:null}
      ]},
    { id:7, referencePPH:'PPH-2026-006-S2', date:'2026-06-17', statut:'CLOTUREE' as const, statutLibelle:'Clôturée', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:496.0, tauxPerteGlobalPourcentage:0.8,
      machinistes:[
        {id:13,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:250,quantiteObtenue:249,perteKg:1,tauxPertePct:0.4,score:99.6,correction:null},
        {id:14,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:250,quantiteObtenue:247,perteKg:3,tauxPertePct:1.2,score:98.8,correction:null}
      ]},
    { id:8, referencePPH:'PPH-2026-006-S2', date:'2026-06-18', statut:'OUVERTE' as const, statutLibelle:'Ouverte', chefMachinisteMatricule:'TAN2026005', chefMachinisteNom:'Théodore OWONA', totalPoudreNetteKg:null, tauxPerteGlobalPourcentage:null,
      machinistes:[
        {id:15,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',typePoudre:'MAIS' as const,typePoudreLibelle:'Maïs',quantiteDepart:null,quantiteObtenue:null,perteKg:null,tauxPertePct:null,score:null,correction:null},
        {id:16,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',typePoudre:'SOJA' as const,typePoudreLibelle:'Soja',quantiteDepart:null,quantiteObtenue:null,perteKg:null,tauxPertePct:null,score:null,correction:null}
      ]}
  ],
  classements: {
    hebdomadaire: {
      referencePPH: 'PPH-2026-006-S1',
      semaine: 'S24-2026',
      entries: [
        {rang:1,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',totalProduit:1229.5,scoreMoyen:98.1,nbJournees:5},
        {rang:2,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',totalProduit:1218,scoreMoyen:97.4,nbJournees:5}
      ]
    },
    mensuel: {
      annee: 2026, mois: 6,
      entries: [
        {rang:1,machinisteMatricule:'TAN2026007',machinisteNom:'Rodrigue ATANGA',totalProduit:2450,scoreMoyen:97.8,nbJournees:10},
        {rang:2,machinisteMatricule:'TAN2026008',machinisteNom:'Celestine ONDO',totalProduit:2390,scoreMoyen:97.1,nbJournees:10},
        {rang:3,machinisteMatricule:'TAN2026013',machinisteNom:'Serge BIKELE',totalProduit:2280,scoreMoyen:96.5,nbJournees:9}
      ]
    }
  }
};
export default MOCK;
