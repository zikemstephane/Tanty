import { RegistreReception } from '../models/reception.models';

const MOCK_REGISTRES: RegistreReception[] = [
  { id:1,reference:'RECEP-2026-001',ficheProductionId:1,referencePPH:'PPH-2026-006-S1',dateProduction:'2026-06-09',
    agentProductionMatricule:'TAN2026004',agentProductionNom:'Bernadette ESSAMA',
    gestionnaireMatricule:'TAN2026002',gestionnaireNom:'Marie-Claire FOUDA',
    dateReception:'2026-06-09',heureReception:'16:30',statut:'CLOTUREE',statutLibelle:'Clôturée',
    totalDeclareeUnites:1010,totalRecueUnites:1010,totalEcartUnites:0,tauxConformite:100,nbLignesEcart:0,
    commentaireGestionnaire:'Réception conforme. Tous les produits sont bien reçus et en bon état.',
    lignes:[
      {id:1,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:2,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:6,quantiteDeclareeUnites:150,quantiteRecueCartons:6,quantiteRecueUnites:150,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:3,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:4,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:4,quantiteDeclareeUnites:320,quantiteRecueCartons:4,quantiteRecueUnites:320,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:5,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:4,quantiteDeclareeUnites:320,quantiteRecueCartons:4,quantiteRecueUnites:320,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]},
  { id:2,reference:'RECEP-2026-002',ficheProductionId:2,referencePPH:'PPH-2026-006-S1',dateProduction:'2026-06-10',
    agentProductionMatricule:'TAN2026004',agentProductionNom:'Bernadette ESSAMA',
    gestionnaireMatricule:'TAN2026012',gestionnaireNom:'Henriette NDJOMO',
    dateReception:'2026-06-10',heureReception:'17:00',statut:'ECART_SIGNALE',statutLibelle:'Écart signalé',
    totalDeclareeUnites:1200,totalRecueUnites:1125,totalEcartUnites:-75,tauxConformite:93.75,nbLignesEcart:1,
    commentaireGestionnaire:'3 cartons de BSA-25 manquants à la livraison. Le reste est conforme. Lot n° BSA-250610-22.',
    lignes:[
      {id:6,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:9,quantiteDeclareeUnites:225,quantiteRecueCartons:6,quantiteRecueUnites:150,ecartCartons:-3,ecartUnites:-75,ecartPct:-33.3,statut:'ECART_NEGATIF',observationGestionnaire:'3 cartons introuvables — possible casse ou erreur de comptage en fin de production.'},
      {id:7,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:8,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:9,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:400,quantiteRecueCartons:5,quantiteRecueUnites:400,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:10,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:400,quantiteRecueCartons:5,quantiteRecueUnites:400,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]},
  { id:3,reference:'RECEP-2026-003',ficheProductionId:3,referencePPH:'PPH-2026-006-S1',dateProduction:'2026-06-11',
    agentProductionMatricule:'TAN2026009',agentProductionNom:'Gilles TSIMI',
    gestionnaireMatricule:'TAN2026002',gestionnaireNom:'Marie-Claire FOUDA',
    dateReception:'2026-06-11',heureReception:'16:45',statut:'ECART_SIGNALE',statutLibelle:'Écart signalé',
    totalDeclareeUnites:1150,totalRecueUnites:1225,totalEcartUnites:75,tauxConformite:100,nbLignesEcart:1,
    commentaireGestionnaire:'Surplus de TBSN-25 reçu — 3 cartons de plus que déclaré. A vérifier avec l\'agent.',
    justificationAgent:'Les 3 cartons supplémentaires de TBSN-25 avaient été produits la veille en fin de journée mais non comptabilisés dans la fiche du 10/06. Erreur de ma part dans le comptage.',
    dateJustification:'2026-06-12',
    lignes:[
      {id:11,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:12,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:13,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:10,quantiteRecueUnites:250,ecartCartons:3,ecartUnites:75,ecartPct:42.9,statut:'ECART_POSITIF',observationGestionnaire:'3 cartons supplémentaires reçus en surplus.'},
      {id:14,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:400,quantiteRecueCartons:5,quantiteRecueUnites:400,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:15,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:400,quantiteRecueCartons:5,quantiteRecueUnites:400,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]},
  { id:4,reference:'RECEP-2026-004',ficheProductionId:4,referencePPH:'PPH-2026-006-S1',dateProduction:'2026-06-12',
    agentProductionMatricule:'TAN2026004',agentProductionNom:'Bernadette ESSAMA',
    gestionnaireMatricule:'TAN2026002',gestionnaireNom:'Marie-Claire FOUDA',
    dateReception:'2026-06-12',heureReception:'17:15',statut:'CLOTUREE',statutLibelle:'Clôturée',
    totalDeclareeUnites:1010,totalRecueUnites:1010,totalEcartUnites:0,tauxConformite:100,nbLignesEcart:0,
    commentaireGestionnaire:'Réception conforme malgré la journée difficile. Bon travail.',
    lignes:[
      {id:16,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:17,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:125,quantiteRecueCartons:5,quantiteRecueUnites:125,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:18,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:6,quantiteDeclareeUnites:150,quantiteRecueCartons:6,quantiteRecueUnites:150,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:19,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:3,quantiteDeclareeUnites:240,quantiteRecueCartons:3,quantiteRecueUnites:240,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:20,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:4,quantiteDeclareeUnites:320,quantiteRecueCartons:4,quantiteRecueUnites:320,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]},
  { id:5,reference:'RECEP-2026-005',ficheProductionId:5,referencePPH:'PPH-2026-006-S1',dateProduction:'2026-06-13',
    agentProductionMatricule:'TAN2026009',agentProductionNom:'Gilles TSIMI',
    gestionnaireMatricule:'TAN2026012',gestionnaireNom:'Henriette NDJOMO',
    dateReception:'2026-06-13',heureReception:'16:00',statut:'CLOTUREE',statutLibelle:'Clôturée',
    totalDeclareeUnites:1585,totalRecueUnites:1585,totalEcartUnites:0,tauxConformite:100,nbLignesEcart:0,
    commentaireGestionnaire:'Excellente journée. Réception totalement conforme.',
    lignes:[
      {id:21,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:9,quantiteDeclareeUnites:225,quantiteRecueCartons:9,quantiteRecueUnites:225,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:22,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:23,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:24,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:6,quantiteDeclareeUnites:480,quantiteRecueCartons:6,quantiteRecueUnites:480,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:25,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:6,quantiteDeclareeUnites:480,quantiteRecueCartons:6,quantiteRecueUnites:480,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]},
  { id:6,reference:'RECEP-2026-006',ficheProductionId:6,referencePPH:'PPH-2026-006-S2',dateProduction:'2026-06-16',
    agentProductionMatricule:'TAN2026004',agentProductionNom:'Bernadette ESSAMA',
    gestionnaireMatricule:'TAN2026002',gestionnaireNom:'Marie-Claire FOUDA',
    dateReception:'2026-06-16',heureReception:'16:30',statut:'RECUE',statutLibelle:'Reçue',
    totalDeclareeUnites:1165,totalRecueUnites:1165,totalEcartUnites:0,tauxConformite:100,nbLignesEcart:0,
    commentaireGestionnaire:'Réception du lundi conforme.',
    lignes:[
      {id:26,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:27,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:6,quantiteDeclareeUnites:150,quantiteRecueCartons:6,quantiteRecueUnites:150,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:28,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:29,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:4,quantiteDeclareeUnites:320,quantiteRecueCartons:4,quantiteRecueUnites:320,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:30,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:4,quantiteDeclareeUnites:320,quantiteRecueCartons:4,quantiteRecueUnites:320,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]},
  { id:7,reference:'RECEP-2026-007',ficheProductionId:7,referencePPH:'PPH-2026-006-S2',dateProduction:'2026-06-17',
    agentProductionMatricule:'TAN2026011',agentProductionNom:'Christophe ABEGA',
    gestionnaireMatricule:'TAN2026002',gestionnaireNom:'Marie-Claire FOUDA',
    dateReception:'2026-06-17',heureReception:'17:00',statut:'ECART_SIGNALE',statutLibelle:'Écart signalé',
    totalDeclareeUnites:1200,totalRecueUnites:1080,totalEcartUnites:-120,tauxConformite:90,nbLignesEcart:2,
    commentaireGestionnaire:'2 produits présentent un déficit. Problème possible lors du transfert du magasin à la zone de stockage.',
    lignes:[
      {id:31,produitId:'P001',produitCode:'BSA-25',produitNom:'REINE BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 57g',quantiteDeclareeCartons:9,quantiteDeclareeUnites:225,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:-2,ecartUnites:-50,ecartPct:-22.2,statut:'ECART_NEGATIF',observationGestionnaire:'2 cartons endommagés — packaging déchiré. Produits inutilisables.'},
      {id:32,produitId:'P006',produitCode:'TBSA-25',produitNom:'TANTY BOUILLIE SOJA ARACHIDES',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:7,quantiteDeclareeUnites:175,quantiteRecueCartons:7,quantiteRecueUnites:175,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:33,produitId:'P008',produitCode:'TBSN-25',produitNom:'TANTY BOUILLIE SOJA NATURE',categorie:'BOUILLIE_SOJA',conditionnement:'25 sachets x 59g',quantiteDeclareeCartons:8,quantiteDeclareeUnites:200,quantiteRecueCartons:8,quantiteRecueUnites:200,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'},
      {id:34,produitId:'P032',produitCode:'MAE-MINI',produitNom:'MINI A GRIGNOTER ARACHIDES ENROBEES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 35g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:400,quantiteRecueCartons:4,quantiteRecueUnites:320,ecartCartons:-1,ecartUnites:-80,ecartPct:-20,statut:'ECART_NEGATIF',observationGestionnaire:'1 carton manquant — introuvable lors du comptage.'},
      {id:35,produitId:'P034',produitCode:'CRO-MINI',produitNom:'MINI A GRIGNOTER CROQUETTES',categorie:'A_GRIGNOTER',conditionnement:'80 sachets x 30g',quantiteDeclareeCartons:5,quantiteDeclareeUnites:400,quantiteRecueCartons:5,quantiteRecueUnites:400,ecartCartons:0,ecartUnites:0,ecartPct:0,statut:'CONFORME'}
    ]}
];
export default MOCK_REGISTRES;
