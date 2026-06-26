// ─── TANTY Core Models ──────────────────────────────────────────────────────

// ── Auth ──────────────────────────────────────────────────────────────────────
export type Role =
  | 'DIRECTEUR_GENERAL'
  | 'GESTIONNAIRE_STOCK'
  | 'CHEF_PRODUCTION'
  | 'AGENT_PRODUCTION'
  | 'CHEF_MACHINISTE'
  | 'AGENT_DOSEUR';

export const ROLE_LABELS: Record<Role, string> = {
  DIRECTEUR_GENERAL:  'Directeur Général',
  GESTIONNAIRE_STOCK: 'Gestionnaire de Stock',
  CHEF_PRODUCTION:    'Chef de Production',
  AGENT_PRODUCTION:   'Agent de Production',
  CHEF_MACHINISTE:    'Chef Machiniste',
  AGENT_DOSEUR:       'Agent Doseur'
};

export interface Utilisateur {
  id: number;
  matricule: string;
  nom: string;
  prenom: string;
  nomComplet: string;
  email: string;
  role: Role;
  roleLibelle: string;
  actif: boolean;
  dateCreation: string;
}

export interface LoginRequest {
  matricule: string;
  motDePasse: string;
}

export interface LoginResponse {
  token: string;
  matricule: string;
  nomComplet: string;
  role: Role;
  roleLibelle: string;
}

export interface RegisterRequest {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;
  confirmationMotDePasse: string;
  role: Role;
}

export interface RegisterResponse {
  matricule: string;
  message: string;
}

// ── Bon de Commande ───────────────────────────────────────────────────────────
export type StatutBC = 'BROUILLON' | 'EN_ATTENTE_VALIDATION' | 'VALIDE' | 'CLOS' | 'ANNULE';
export type CategorieProduit = 'BOUILLIE_SOJA' | 'CHOCOLAT' | 'INGREDIENTS' | 'A_GRIGNOTER';
export type GammeGrignoter = 'MINI' | 'MOYEN' | 'GRAND';

export const STATUT_BC_LABELS: Record<StatutBC, string> = {
  BROUILLON:              'Brouillon',
  EN_ATTENTE_VALIDATION:  'En attente de validation',
  VALIDE:                 'Validé',
  CLOS:                   'Clos',
  ANNULE:                 'Annulé'
};

export const CATEGORIE_LABELS: Record<CategorieProduit, string> = {
  BOUILLIE_SOJA: 'Bouillie Soja',
  CHOCOLAT:      'Chocolat',
  INGREDIENTS:   'Ingrédients',
  A_GRIGNOTER:   'À Grignoter'
};

export interface LigneBC {
  id?: number;
  numeroligne?: number;
  produitId: string;
  produitCode: string;
  produitNom: string;
  categorie: CategorieProduit;
  conditionnement: string;
  gamme?: GammeGrignoter;
  quantiteCartons: number;
  quantiteUnites?: number;
}

export interface BonDeCommande {
  id: number;
  reference: string;
  dateCreation: string;
  dateDebutProduction: string;
  dateFinProduction: string;
  nombreSemaines: number;
  statut: StatutBC;
  statutLibelle: string;
  createurMatricule: string;
  createurNom: string;
  validateurMatricule: string | null;
  validateurNom: string | null;
  dateValidation: string | null;
  lignes: LigneBC[];
  pphReferences: string[];
}

export interface CreerBCRequest {
  dateDebutProduction: string;
  dateFinProduction: string;
  lignes: Omit<LigneBC, 'id' | 'numeroligne'>[];
}

export interface ModifierBCRequest {
  lignes: Omit<LigneBC, 'id' | 'numeroligne'>[];
  codeOTP: string;
}

// ── PPH ───────────────────────────────────────────────────────────────────────
export type StatutPPH = 'PLANIFIE' | 'EN_COURS' | 'TERMINE' | 'AJUSTE';

export const STATUT_PPH_LABELS: Record<StatutPPH, string> = {
  PLANIFIE:  'Planifié',
  EN_COURS:  'En cours',
  TERMINE:   'Terminé',
  AJUSTE:    'Ajusté'
};

export interface LignePPH {
  id?: number;
  produitId: string;
  produitCode: string;
  produitNom: string;
  categorie: CategorieProduit;
  quantiteCibleCartons: number;
  quantiteCibleUnites: number;
  quantiteRealiseeCartons: number;
  quantiteRealiseeUnites: number;
  tauxRealisation: number;
}

export interface PPH {
  id: number;
  reference: string;
  referenceBC: string;
  numeroSemaine: number;
  dateDebut: string;
  dateFin: string;
  statut: StatutPPH;
  statutLibelle: string;
  lignes: LignePPH[];
  besoinsMatieresPremieres?: {
    maisKg: number;
    sojaKg: number;
    arachideKg: number;
  };
}

export interface AjusterQuantitePPHRequest {
  lignePPHId: number;
  nouvelleQuantite: number;
  motif: string;
}

// ── Produit ───────────────────────────────────────────────────────────────────
export interface Produit {
  id: string;
  code: string;
  categorie: CategorieProduit;
  gamme: string;
  nom: string;
  conditionnement: string;
  poidsUnitaire: number;
  nombreUnites: number;
  uniteCarton: string;
  actif: boolean;
  prixUnitaire: number;
}

// ── Agent Production ──────────────────────────────────────────────────────────
export interface LigneFicheProduit {
  produitId: string;
  produitCode: string;
  produitNom: string;
  quantiteCartons: number;
  quantiteUnites: number;
  observation?: string|null;
}

export interface FicheProduit {
  id: number;
  referencePPH: string;
  date: string;
  agentMatricule: string;
  agentNom: string;
  totalProduit: number;
  lignes: LigneFicheProduit[];
}

export interface EnregistrerFicheProduitRequest {
  referencePPH: string;
  date: string;
  lignes: Omit<LigneFicheProduit, 'produitCode' | 'produitNom'>[];
}

export interface LigneProduitEmploye {
  produitCode: string;
  produitNom: string;
  quantiteRealise: number;
  unite: string;
}

export interface LigneRegistreEmploye {
  nom: string;
  produitsTravailles: LigneProduitEmploye[];
  totalPoints: number;
  observation?: string|null;
}

export interface RegistreProduction {
  id: number;
  referencePPH: string;
  date: string;
  agentMatricule: string;
  agentNom: string;
  employes: LigneRegistreEmploye[];
}

export interface LigneRapportProduit {
  produitCode: string;
  produitNom: string;
  quantitePredite: number;
  quantiteRealisee: number;
  tauxRealisation: number;
  ecart: number;
}

export interface ClassementEmploye {
  rang: number;
  nom: string;
  totalProduction: number;
  score: number;
}

export interface RapportHebdomadaire {
  referencePPH: string;
  numeroSemaine: number;
  annee: number;
  dateDebut: string;
  dateFin: string;
  totalPredite: number;
  totalRealisee: number;
  tauxRealisation: number;
  classementEmployes: ClassementEmploye[];
  lignes: LigneRapportProduit[];
}

// ── Broyage ───────────────────────────────────────────────────────────────────
export type TypePoudre = 'MAIS' | 'SOJA' | 'ARACHIDE';
export type StatutJournee = 'OUVERTE' | 'CLOTUREE';

export const TYPE_POUDRE_LABELS: Record<TypePoudre, string> = {
  MAIS:     'Maïs',
  SOJA:     'Soja',
  ARACHIDE: 'Arachide'
};

export interface ProductionMachiniste {
  id: number;
  machinisteMatricule: string;
  machinisteNom: string;
  typePoudre: TypePoudre;
  typePoudreLibelle: string;
  quantiteDepart: number | null;
  quantiteObtenue: number | null;
  perteKg: number | null;
  tauxPertePct: number | null;
  score: number | null;
  correction: string | null;
}

export interface JourneeProduction {
  id: number;
  referencePPH: string;
  date: string;
  statut: StatutJournee;
  statutLibelle: string;
  chefMachinisteMatricule: string;
  chefMachinisteNom: string;
  totalPoudreNetteKg: number | null;
  tauxPerteGlobalPourcentage: number | null;
  machinistes: ProductionMachiniste[];
}

export interface AssignationMachiniste {
  machinisteMatricule: string;
  typePoudre: TypePoudre;
}

export interface OuvrirJourneeRequest {
  referencePPH: string;
  date: string;
  assignations: AssignationMachiniste[];
}

export interface EnregistrerResultatRequest {
  productionMachinisteId: number;
  quantiteDepart: number;
  quantiteObtenue: number;
}

export interface CorrigerResultatRequest {
  productionMachinisteId: number;
  nouvelleQuantiteDepart: number;
  nouvelleQuantiteObtenue: number;
  motif: string;
}

export interface ClassementEntry {
  rang: number;
  machinisteMatricule: string;
  machinisteNom: string;
  totalProduit: number;
  scoreMoyen: number;
  nbJournees: number;
}

export interface Classement {
  periode: string;
  entries: ClassementEntry[];
}

// ── Dosage ────────────────────────────────────────────────────────────────────
export type TypeMelange = 'BOUILLIE' | 'MELANGE_ARACHIDE';

export const TYPE_MELANGE_LABELS: Record<TypeMelange, string> = {
  BOUILLIE:         'Bouillie',
  MELANGE_ARACHIDE: 'Mélange Arachide'
};

export const FORMULES_DOSAGE: Record<TypeMelange, { mais: number; soja: number; arachide: number }> = {
  BOUILLIE:         { mais: 40, soja: 9,  arachide: 1 },
  MELANGE_ARACHIDE: { mais: 40, soja: 1,  arachide: 9 }
};

export interface CompositionFut {
  maisKg: number;
  sojaKg: number;
  arachideKg: number;
}

export interface SessionDosage {
  id: number;
  referencePPH: string;
  date: string;
  agentMatricule: string;
  agentNom: string;
  typeMelange: TypeMelange;
  typeMelangeLibelle: string;
  nombreFuts: number;
  poidsNetTotalKg: number;
  compositionConforme: boolean;
  avertissementComposition: string | null;
  composition: CompositionFut;
}

export interface EnregistrerSessionDosageRequest {
  referencePPH: string;
  date: string;
  typeMelange: TypeMelange;
  nombreFuts: number;
  composition: CompositionFut;
}

export interface StatsDosagePeriode {
  totalFuts: number;
  totalPoidsKg: number;
  parType: { typeMelange: TypeMelange; nombreFuts: number; poidsKg: number }[];
  evolutionTotalPct?: number;
}

export interface StatistiqueDosage {
  dateDebut: string;
  dateFin: string;
  totalFuts: number;
  totalPoidsKg: number;
  parType: { typeMelange: TypeMelange; nombreFuts: number; poidsKg: number }[];
  evolutionTotalPct: number | null;
  semainePrecedente?: StatsDosagePeriode;
}

// ── API Response ──────────────────────────────────────────────────────────────
export interface ApiResponse<T> {
  succes: boolean;
  message: string;
  data: T;
  timestamp?: string;
}

// ── Navigation ────────────────────────────────────────────────────────────────
export interface BreadcrumbItem {
  label: string;
  url?: string;
  icon?: string;
}

export interface NavMenuItem {
  label: string;
  icon: string;
  route?: string;
  children?: NavMenuItem[];
  roles?: Role[];
  badge?: number;
}

export interface ToastMessage {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number;
}
