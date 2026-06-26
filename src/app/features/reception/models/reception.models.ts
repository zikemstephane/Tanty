// ─── Module Réception Produits Finis ────────────────────────────────────────
// Géré par le Gestionnaire de Stock
// Ce module n'est pas dans le backend actuel mais est nécessaire au workflow métier

export type StatutReception = 'EN_ATTENTE' | 'RECUE' | 'ECART_SIGNALE' | 'CLOTUREE';
export type StatutLigneReception = 'CONFORME' | 'ECART_POSITIF' | 'ECART_NEGATIF' | 'MANQUANT';

export const STATUT_RECEPTION_LABELS: Record<StatutReception, string> = {
  EN_ATTENTE:    'En attente',
  RECUE:         'Reçue',
  ECART_SIGNALE: 'Écart signalé',
  CLOTUREE:      'Clôturée'
};

export const STATUT_LIGNE_LABELS: Record<StatutLigneReception, string> = {
  CONFORME:       'Conforme',
  ECART_POSITIF:  'Surplus',
  ECART_NEGATIF:  'Déficit',
  MANQUANT:       'Manquant'
};

/** Ligne de réception = comparaison entre déclaration agent et réception gestionnaire */
export interface LigneRegistreReception {
  id?: number;
  produitId:        string;
  produitCode:      string;
  produitNom:       string;
  categorie:        string;
  conditionnement:  string;

  // Ce que l'Agent de Production a déclaré
  quantiteDeclareeCartons: number;
  quantiteDeclareeUnites:  number;

  // Ce que le Gestionnaire a physiquement reçu
  quantiteRecueCartons:    number;
  quantiteRecueUnites:     number;

  // Calculs automatiques
  ecartCartons:    number;   // reçu - déclaré
  ecartUnites:     number;
  ecartPct:        number;   // % d'écart
  statut:          StatutLigneReception;

  // Observations du gestionnaire
  observationGestionnaire?: string;
}

/** En-tête du registre de réception */
export interface RegistreReception {
  id:                number;
  reference:         string;   // RECEP-2026-XXX
  ficheProductionId: number;
  referencePPH:      string;
  dateProduction:    string;   // date de la fiche de production

  // Acteurs
  agentProductionMatricule: string;
  agentProductionNom:       string;
  gestionnaireMatricule:    string;
  gestionnaireNom:          string;

  dateReception:   string;
  heureReception?: string;
  statut:          StatutReception;
  statutLibelle:   string;

  // Totaux
  totalDeclareeUnites: number;
  totalRecueUnites:    number;
  totalEcartUnites:    number;
  tauxConformite:      number; // %
  nbLignesEcart:       number;

  lignes: LigneRegistreReception[];

  // Commentaire global du gestionnaire
  commentaireGestionnaire?: string;

  // Justification de l'agent (si écart)
  justificationAgent?: string;
  dateJustification?:  string;
}

/** Demande de création d'un registre de réception */
export interface CreerRegistreReceptionRequest {
  ficheProductionId: number;
  dateReception:     string;
  heureReception?:   string;
  lignes: {
    produitId:            string;
    quantiteRecueCartons: number;
    quantiteRecueUnites:  number;
    observationGestionnaire?: string;
  }[];
  commentaireGestionnaire?: string;
}

/** Justification soumise par l'Agent de Production */
export interface JustificationAgentRequest {
  registreId:          number;
  justificationAgent:  string;
}

/** Tableau de bord réception */
export interface DashboardReception {
  totalFichesEnAttente:  number;
  totalFichesRecues:     number;
  totalEcartsSignales:   number;
  totalJustificationsEnAttente: number;
  derniersRegistres:     RegistreReception[];
}
