import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiService } from '../../../core/http/api.service';
import { ApiResponse } from '../../../core/models/api-response.model';
import { PageResponse, PageParams } from '../../../core/models/page-response.model';

// ── DTOs ──────────────────────────────────────────────────────

export interface TableauBordStockBE {
  valeurStockCentral: number;
  valeurStockTampon: number;
  valeurStockMobile: number;
  nbAlertesActives: number;
  nbReceptionsEnAttente: number;
  nbMouvementsJour: number;
  nbLotsEnAttente: number;
}

export interface TableauBordEnrichiBE {
  stats: Record<string, number>;
  tampon: { produit: string; quantite: number; unite: string }[];
  stockCentral: { produit: string; quantite: number; unite: string }[];
  stockMobile: { matricule: string; nom: string; valeur: number }[];
  performances: { matricule: string; nom: string; ca: number; taux: number }[];
  invendus: { produit: string; quantite: number; valeur: number }[];
}

export interface NiveauStockBE {
  id: number;
  produitId: number | null;
  codeProduit: string;
  sku: string;
  designation: string;
  marque: string | null;
  gamme: string | null;
  categorie: string | null;
  entrepotId: number | null;
  codeEntrepot: string;
  libelleEntrepot: string;
  typeEntrepot: string;
  quantite: number;
  seuilReapprovisionnement: number;
  stockSecurite: number;
  niveauAlerte: string;
  valeurStock: number;
  dateModification: string | null;
}

export interface MouvementBE {
  id: number;
  date: string;
  type: string;
  entrepot: string;
  codeProduit: string;
  designation: string;
  quantite: number;
  unite: string;
  referenceDocument: string;
  matriculeOperateur: string;
}

export interface LigneInventaireBE {
  codeProduit: string;
  designation: string;
  quantiteTheorique: number;
  quantiteComptee: number;
  ecart: number;
  unite: string;
}

export interface InventaireBE {
  id: number;
  numero: string;
  type: string;
  entrepot: string;
  statut: string;
  dateOuverture: string;
  dateCloture?: string;
  matriculeResponsable: string;
  lignes: LigneInventaireBE[];
}

export interface LigneReceptionBE {
  codeProduit: string;
  designation: string;
  quantite: number;
  unite: string;
}

export interface ReceptionBE {
  id: number;
  numero: string;
  type: string;
  statut: string;
  bonCommande?: string;
  matriculeResponsable: string;
  dateReception: string;
  lignes: LigneReceptionBE[];
}

export interface AlerteBE {
  id: number;
  reference: string;
  type: string;
  typeLibelle: string;
  message: string;
  destinataireRole: string;
  actionSuggeree: string;
  priorite: string;
  codeProduit: string;
  codeEntrepot: string;
  referenceLiee: string;
  statut: string;
  dateCreation: string;
  dateTraitement: string | null;
}

/**
 * Session commerciale — journée d'un commercial, de la préparation à la
 * validation comptable.
 *
 * Aligné champ pour champ sur `SessionCommercialResponse` côté serveur. La
 * version actualisée nommait la référence `numero` et la liste des dotations
 * `dotations` ; le serveur émet `numeroSession` et `dotationsIds`. Les deux
 * champs arrivaient donc toujours `undefined`.
 */
export interface SessionCommercialBE {
  id: number;
  numeroSession: string;
  matriculeCommercial: string;
  datePreparation: string;
  dateEffet: string;
  statut: string;
  nombreDotations: number;
  dotationsIds: string[];
  matriculeCloture?: string;
  matriculeValidateurSecretaire?: string;
  matriculeValidateurComptable?: string;
  heureOuverture?: string;
  heureCloture?: string;
  dateCreation?: string;
}

/**
 * Constat d'invendus de fin de tournée.
 *
 * Déclaré et compté voyagent ensemble et séparément : le commercial annonce ce
 * qu'il rapporte — sans qu'aucun mouvement de stock soit produit — puis le
 * gestionnaire compte. C'est l'écart entre les deux qui est enregistré, et le
 * comptage qui fait foi.
 */
export interface ConstatInvendusBE {
  id: number;
  numero: string;
  matriculeCommercial: string;
  /** Renseigné au comptage seulement. */
  matriculeGestionnaire: string | null;
  dateRetour: string;
  statut: string;
  dateComptage: string | null;
  /** Écart déclaré ↔ compté. `null` tant que le comptage n'a pas eu lieu. */
  ecartTotal: number | null;
  lignes: LigneConstatInvendusBE[];
}

/**
 * Les champs issus du comptage sont `null` — et non absents — tant qu'il n'a
 * pas eu lieu : le serveur les sérialise ainsi, et « pas encore compté » se
 * distingue de « compté à zéro ».
 */
export interface LigneConstatInvendusBE {
  id: string;
  codeProduit: string;
  sku: string;
  quantiteDeclaree: number;
  quantiteComptee: number | null;
  ecart: number | null;
  motif: string | null;
}

export interface LigneBonCommandeBE {
  id?: number;
  codeProduit: string;
  sku?: string;
  designation: string;
  quantiteCommandee: number;
  quantiteProduite: number;
  quantiteRecue?: number;
  tauxRealisation: number;
  unite: string;
  prixUnitaireHT?: number;
}

export interface ConditionnementBE {
  id: string;
  sku: string;
  niveau: string;
  niveauLibelle?: string;
  libelle: string;
  facteurConversionVersBase: number;
  poidsUnitaireGrammes: number;
  volumeUnitaireCm3?: number;
  prixUnitaireHT?: number;
  codeBarres?: string;
}

export interface ProduitBE {
  id: number;
  code: string;
  marque: string;
  gamme: string;
  variete: string;
  designation: string;
  categorie: string;
  categorieLibelle?: string;
  categorieMetier: string;
  categorieMetierLibelle?: string;
  actif: boolean;
  prixDistributeur?: number;
  prixGrossiste?: number;
  prixDetaillant?: number;
  conditionnements: ConditionnementBE[];
}

export interface FournisseurBE {
  id: number;
  code: string;
  raisonSociale: string;
  telephone?: string;
  email?: string;
  adresse?: string;
  actif: boolean;
}

export interface BonCommandeBE {
  id: number;
  numero: string;
  codeFournisseur: string;
  nomFournisseur?: string;
  statut: string;
  statutLibelle?: string;
  dateCreation: string;
  dateCommande?: string;
  dateLivraisonPrevue?: string;
  /** Période d'exécution de la production (facultative, les deux ou aucune) ; repli sur la semaine ouvrée de dateCommande. */
  dateDebutProduction?: string | null;
  dateFinProduction?: string | null;
  dateValidation?: string | null;
  matriculeValidateur?: string | null;
  matriculeCreateur?: string;
  montantTotalHT?: number;
  tauxRealisation: number;
  enRetard?: boolean;
  lignes: LigneBonCommandeBE[];
}

@Injectable({ providedIn: 'root' })
export class StockService {
  private readonly api = inject(ApiService);

  // ── Tableau de bord ─────────────────────────────────────────

  getDashboard(): Observable<TableauBordStockBE> {
    return this.api.get<ApiResponse<TableauBordStockBE>>('stock/dashboard')
      .pipe(map(r => r.donnees!));
  }

  getDashboardEnrichi(): Observable<TableauBordEnrichiBE> {
    return this.api.get<ApiResponse<TableauBordEnrichiBE>>('stock/dashboard/enrichi')
      .pipe(map(r => r.donnees!));
  }

  // ── État des stocks ─────────────────────────────────────────

  getEtatStock(params: { entrepot?: string } = {}): Observable<NiveauStockBE[]> {
    const p: Record<string, string> = {};
    if (params.entrepot) p['entrepot'] = params.entrepot;
    return this.api.get<ApiResponse<NiveauStockBE[]>>('stock/levels', p)
      .pipe(map(r => r.donnees ?? []));
  }

  // ── Mouvements ──────────────────────────────────────────────

  getMouvements(params: {
    produit?: string; entrepot?: string; type?: string;
    document?: string; debut?: string; fin?: string;
  } = {}, page?: PageParams): Observable<PageResponse<MouvementBE>> {
    const p: Record<string, string> = {};
    if (params.produit) p['produit'] = params.produit;
    if (params.entrepot) p['entrepot'] = params.entrepot;
    if (params.type) p['type'] = params.type;
    if (params.document) p['document'] = params.document;
    if (params.debut) p['debut'] = params.debut;
    if (params.fin) p['fin'] = params.fin;
    if (page?.page !== undefined) p['page'] = String(page.page);
    if (page?.size !== undefined) p['size'] = String(page.size);
    if (page?.sort) p['sort'] = page.sort;
    return this.api.get<ApiResponse<PageResponse<MouvementBE>>>('stock/mouvements', p)
      .pipe(map(r => r.donnees!));
  }

  // ── Inventaires ─────────────────────────────────────────────

  getInventaires(statut?: string): Observable<InventaireBE[]> {
    const params: Record<string, string> = {};
    if (statut) params['statut'] = statut;
    return this.api.get<ApiResponse<InventaireBE[]>>('stock/inventaires', params)
      .pipe(map(r => r.donnees ?? []));
  }

  getInventaire(numero: string): Observable<InventaireBE> {
    return this.api.get<ApiResponse<InventaireBE>>(`stock/inventaires/${encodeURIComponent(numero)}`)
      .pipe(map(r => r.donnees!));
  }

  ouvrirInventaire(req: {
    type: string; entrepot: string; justification?: string;
  }): Observable<InventaireBE> {
    return this.api.post<ApiResponse<InventaireBE>>('stock/inventaires', req)
      .pipe(map(r => r.donnees!));
  }

  saisirComptage(numero: string, req: {
    codeProduit: string; quantiteComptee: number;
  }): Observable<InventaireBE> {
    return this.api.patch<ApiResponse<InventaireBE>>(
      `stock/inventaires/${encodeURIComponent(numero)}/comptage`, req
    ).pipe(map(r => r.donnees!));
  }

  cloturerComptage(numero: string): Observable<InventaireBE> {
    return this.api.post<ApiResponse<InventaireBE>>(
      `stock/inventaires/${encodeURIComponent(numero)}/cloturer-comptage`, {}
    ).pipe(map(r => r.donnees!));
  }

  validerInventaire(numero: string, justification?: string): Observable<InventaireBE> {
    const params: Record<string, string> = {};
    if (justification) params['justification'] = justification;
    return this.api.post<ApiResponse<InventaireBE>>(
      `stock/inventaires/${encodeURIComponent(numero)}/valider`, {}, 
    ).pipe(map(r => r.donnees!));
  }

  // ── Réceptions ──────────────────────────────────────────────

  getReceptions(params: { statut?: string; bonCommande?: string } = {}): Observable<ReceptionBE[]> {
    const p: Record<string, string> = {};
    if (params.statut) p['statut'] = params.statut;
    if (params.bonCommande) p['bonCommande'] = params.bonCommande;
    return this.api.get<ApiResponse<ReceptionBE[]>>('stock/receptions', p)
      .pipe(map(r => r.donnees ?? []));
  }

  getReception(numero: string): Observable<ReceptionBE> {
    return this.api.get<ApiResponse<ReceptionBE>>(`stock/receptions/${encodeURIComponent(numero)}`)
      .pipe(map(r => r.donnees!));
  }

  creerReceptionFournisseur(req: {
    bonCommande: string; lignes: { codeProduit: string; quantite: number; unite: string }[];
  }): Observable<ReceptionBE> {
    return this.api.post<ApiResponse<ReceptionBE>>('stock/receptions/fournisseur', req)
      .pipe(map(r => r.donnees!));
  }

  creerReceptionProduction(req: {
    lotProduction: string; lignes: { codeProduit: string; quantite: number; unite: string }[];
  }): Observable<ReceptionBE> {
    return this.api.post<ApiResponse<ReceptionBE>>('stock/receptions/production', req)
      .pipe(map(r => r.donnees!));
  }

  validerReceptionEtape1(numero: string): Observable<ReceptionBE> {
    return this.api.post<ApiResponse<ReceptionBE>>(
      `stock/receptions/${encodeURIComponent(numero)}/valider-etape1`, {}
    ).pipe(map(r => r.donnees!));
  }

  validerReceptionEtape2(numero: string): Observable<ReceptionBE> {
    return this.api.post<ApiResponse<ReceptionBE>>(
      `stock/receptions/${encodeURIComponent(numero)}/valider-etape2`, {}
    ).pipe(map(r => r.donnees!));
  }

  rejeterReception(numero: string, motif: string): Observable<ReceptionBE> {
    return this.api.post<ApiResponse<ReceptionBE>>(
      `stock/receptions/${encodeURIComponent(numero)}/rejeter?motif=${encodeURIComponent(motif)}`, {}
    ).pipe(map(r => r.donnees!));
  }

  // ── Alertes ─────────────────────────────────────────────────

  getAlertes(params: { statut?: string; role?: string } = {}): Observable<AlerteBE[]> {
    const p: Record<string, string> = {};
    if (params.statut) p['statut'] = params.statut;
    if (params.role) p['role'] = params.role;
    return this.api.get<ApiResponse<AlerteBE[]>>('stock/alertes', p)
      .pipe(map(r => r.donnees ?? []));
  }

  getStockLevelAlertes(): Observable<NiveauStockBE[]> {
    return this.api.get<ApiResponse<NiveauStockBE[]>>('stock/levels/alerts')
      .pipe(map(r => r.donnees ?? []));
  }

  getStockLevelAlertesCritiques(): Observable<NiveauStockBE[]> {
    return this.api.get<ApiResponse<NiveauStockBE[]>>('stock/levels/alerts/critical')
      .pipe(map(r => r.donnees ?? []));
  }

  getStockLevelsByWarehouse(entrepotId: number): Observable<NiveauStockBE[]> {
    return this.api.get<ApiResponse<NiveauStockBE[]>>(`stock/levels/warehouse/${entrepotId}`)
      .pipe(map(r => r.donnees ?? []));
  }

  adjustStock(stockLevelId: number, newQuantity: number, reason: string): Observable<NiveauStockBE> {
    return this.api.post<ApiResponse<NiveauStockBE>>('stock/levels/adjust', { stockLevelId, newQuantity, reason })
      .pipe(map(r => r.donnees!));
  }

  transferToBuffer(id: number, quantity: number): Observable<NiveauStockBE> {
    return this.api.post<ApiResponse<NiveauStockBE>>(`stock/levels/${id}/transfer-to-buffer?quantity=${quantity}`, {})
      .pipe(map(r => r.donnees!));
  }

  marquerAlerteLue(reference: string): Observable<AlerteBE> {
    return this.api.post<ApiResponse<AlerteBE>>(
      `stock/alertes/${encodeURIComponent(reference)}/lue`, {}
    ).pipe(map(r => r.donnees!));
  }

  traiterAlerte(reference: string): Observable<AlerteBE> {
    return this.api.post<ApiResponse<AlerteBE>>(
      `stock/alertes/${encodeURIComponent(reference)}/traiter`, {}
    ).pipe(map(r => r.donnees!));
  }

  // ── Sessions commerciaux ────────────────────────────────────

  getSessionsParCommercial(matricule: string): Observable<SessionCommercialBE[]> {
    return this.api.get<ApiResponse<SessionCommercialBE[]>>(`stock/sessions/commercial/${encodeURIComponent(matricule)}`)
      .pipe(map(r => r.donnees ?? []));
  }

  getSessionsParDate(date: string): Observable<SessionCommercialBE[]> {
    return this.api.get<ApiResponse<SessionCommercialBE[]>>(`stock/sessions/date/${date}`)
      .pipe(map(r => r.donnees ?? []));
  }

  getSessionsParStatut(statut: string): Observable<SessionCommercialBE[]> {
    return this.api.get<ApiResponse<SessionCommercialBE[]>>(`stock/sessions/statut/${encodeURIComponent(statut)}`)
      .pipe(map(r => r.donnees ?? []));
  }

  preparerSession(req: {
    matriculeCommercial: string; datePreparation: string;
  }): Observable<SessionCommercialBE> {
    return this.api.post<ApiResponse<SessionCommercialBE>>('stock/sessions/preparer', req)
      .pipe(map(r => r.donnees!));
  }

  ouvrirSession(numero: string): Observable<SessionCommercialBE> {
    return this.api.patch<ApiResponse<SessionCommercialBE>>(
      `stock/sessions/${encodeURIComponent(numero)}/ouvrir`, {}
    ).pipe(map(r => r.donnees!));
  }

  /**
   * Le commercial déclare la fin de sa tournée.
   *
   * La route est `declarer-fin-tournee` et non `cloturer` : le serveur refuse
   * la déclaration tant que des arrêts restent à trancher, et nomme les clients
   * concernés. « Clôturer » laissait croire à une simple fermeture d'état.
   */
  declarerFinTournee(numero: string, matricule: string): Observable<SessionCommercialBE> {
    return this.api.patch<ApiResponse<SessionCommercialBE>>(
      `stock/sessions/${encodeURIComponent(numero)}/declarer-fin-tournee?matricule=${encodeURIComponent(matricule)}`, {}
    ).pipe(map(r => r.donnees!));
  }

  validerSessionSecretaire(numero: string, matricule: string): Observable<SessionCommercialBE> {
    return this.api.patch<ApiResponse<SessionCommercialBE>>(
      `stock/sessions/${encodeURIComponent(numero)}/valider-secretaire?matricule=${encodeURIComponent(matricule)}`, {}
    ).pipe(map(r => r.donnees!));
  }

  validerSessionComptable(numero: string, matricule: string): Observable<SessionCommercialBE> {
    return this.api.patch<ApiResponse<SessionCommercialBE>>(
      `stock/sessions/${encodeURIComponent(numero)}/valider-comptable?matricule=${encodeURIComponent(matricule)}`, {}
    ).pipe(map(r => r.donnees!));
  }

  // ── Invendus : constat puis comptage ────────────────────────

  /**
   * Liste les constats d'invendus. Les deux filtres sont facultatifs et
   * combinables : `statut: 'DECLARE'` donne ce qui reste à compter,
   * `statut: 'COMPTE'` ce qui l'a été.
   */
  getConstatsInvendus(filtres?: { commercial?: string; statut?: string }): Observable<ConstatInvendusBE[]> {
    const params: Record<string, string> = {};
    if (filtres?.commercial) params['commercial'] = filtres.commercial;
    if (filtres?.statut) params['statut'] = filtres.statut;
    return this.api.get<ApiResponse<ConstatInvendusBE[]>>('stock/retours', params)
      .pipe(map(r => r.donnees ?? []));
  }

  /**
   * Le gestionnaire saisit les quantités qu'il constate.
   *
   * Zéro est une valeur admise et signifiante — le produit annoncé n'a pas été
   * rapporté. C'est l'omission d'une ligne que le serveur refuse, jamais le zéro.
   */
  compterInvendus(
    numero: string,
    lignes: { codeProduit: string; quantiteComptee: number }[]
  ): Observable<ConstatInvendusBE> {
    return this.api.post<ApiResponse<ConstatInvendusBE>>(
      `stock/retours/${encodeURIComponent(numero)}/comptage`, { lignes }
    ).pipe(map(r => r.donnees!));
  }

  // ── Rapports (téléchargement fichiers) ──────────────────────

  getRapportEtatStock(params: {
    entrepot?: string; produit?: string; type?: string; format?: string;
  } = {}): Observable<Blob> {
    const p: Record<string, string> = { format: params.format ?? 'xlsx' };
    if (params.entrepot) p['entrepot'] = params.entrepot;
    if (params.produit) p['produit'] = params.produit;
    if (params.type) p['type'] = params.type;
    return this.api.get('stock/rapports/etat-stock', p) as unknown as Observable<Blob>;
  }

  // ── Catalogue produits ────────────────────────────────────────

  getProduits(params: { marque?: string; gamme?: string; variete?: string; actif?: boolean } = {}): Observable<ProduitBE[]> {
    const p: Record<string, string> = {};
    if (params.marque) p['marque'] = params.marque;
    if (params.gamme) p['gamme'] = params.gamme;
    if (params.variete) p['variete'] = params.variete;
    if (params.actif != null) p['actif'] = String(params.actif);
    return this.api.get<ApiResponse<ProduitBE[]>>('stock/produits', p)
      .pipe(map(r => r.donnees ?? []));
  }

  // ── Fournisseurs ──────────────────────────────────────────────

  getFournisseurs(recherche?: string): Observable<FournisseurBE[]> {
    const p: Record<string, string> = {};
    if (recherche) p['recherche'] = recherche;
    return this.api.get<ApiResponse<FournisseurBE[]>>('stock/fournisseurs', p)
      .pipe(map(r => r.donnees ?? []));
  }

  // ── Bons de commande (pour le module production) ────────────

  /** `valides: true` renvoie tous les BC validés à un moment (validés, envoyés, reçus, clôturés, ou annulés après validation). */
  getBonsCommande(params: { statut?: string; valides?: boolean } = {}): Observable<BonCommandeBE[]> {
    const p: Record<string, string> = {};
    if (params.statut) p['statut'] = params.statut;
    if (params.valides) p['valides'] = 'true';
    return this.api.get<ApiResponse<BonCommandeBE[]>>('stock/bons-commande', p)
      .pipe(map(r => r.donnees ?? []));
  }

  getBonCommande(numero: string): Observable<BonCommandeBE | null> {
    return this.api.get<ApiResponse<BonCommandeBE>>(`stock/bons-commande/${encodeURIComponent(numero)}`)
      .pipe(map(r => r.donnees ?? null));
  }

  validerBonCommande(numero: string): Observable<BonCommandeBE> {
    return this.api.post<ApiResponse<BonCommandeBE>>(
      `stock/bons-commande/${encodeURIComponent(numero)}/valider`, {}
    ).pipe(map(r => r.donnees!));
  }

  creerBonCommande(req: {
    codeFournisseur: string | null;
    dateCommande: string;
    dateLivraisonPrevue?: string | null;
    dateDebutProduction?: string | null;
    dateFinProduction?: string | null;
    lignes: {
      codeProduit: string;
      designation?: string | null;
      sku?: string | null;
      quantiteCommandee: number;
      prixUnitaireHT?: number | null;
    }[];
  }): Observable<BonCommandeBE> {
    return this.api.post<ApiResponse<BonCommandeBE>>('stock/bons-commande', req)
      .pipe(map(r => r.donnees!));
  }

  /** Corrige la période de production d'un BC (même validé). Ne recalcule pas le PPH : appeler recalculerPPH ensuite. */
  modifierPeriodeProductionBC(numero: string, dateDebutProduction: string | null, dateFinProduction: string | null): Observable<BonCommandeBE> {
    return this.api.put<ApiResponse<BonCommandeBE>>(
      `stock/bons-commande/${encodeURIComponent(numero)}/periode-production`, { dateDebutProduction, dateFinProduction }
    ).pipe(map(r => r.donnees!));
  }
}
