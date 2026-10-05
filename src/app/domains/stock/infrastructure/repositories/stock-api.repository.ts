// ═══ FICHIER : src/app/domains/stock/infrastructure/repositories/stock-api.repository.ts ═══
// URLs alignées sur le backend Spring Boot (stock/...)

import { Injectable, inject } from '@angular/core';
import { Observable, concat, of, throwError } from 'rxjs';
import { catchError, last, map, switchMap } from 'rxjs/operators';
import { ApiService } from '../../../../core/http/api.service';
import { ApiResponse } from '../../../../core/models/api-response.model';
import {
  StockLevel, Receipt, ProductionBatch, InternalOrder,
  StockMovement, Commercial, InfoProduits, DashboardStatsResponse,
  StockAlert, Warehouse, Product, Brand, ProductLine, ProductVariant, Supplier,
  ReceptionType, ReceiptStatus, MovementType,
  DotationRequest, CreateDotationRequest, DotationStatus,
  StockBatch, BatchStatus, StockAuditLog, StockAlertEntity,
  MobileStockSummary, MobileStockRotation, SlowStockCommercial,
  Category, UnitType
} from '../../domain/models/stock.models';

/**
 * Hiérarchie du catalogue telle que le serveur la rend : marques, gammes et
 * variétés dérivées des produits, sans tables dédiées.
 */
interface CatalogueHierarchie {
  marques: { id: number; nom: string; code: string }[];
  gammes: { id: number; marqueId: number; nom: string; code: string }[];
  varietes: { id: number; gammeId: number; nom: string; code: string }[];
}

/**
 * Type d'emplacement côté écran → type d'entrepôt côté serveur.
 * Le client parle en STOCK_CENTRAL / STOCK_BUFFER / STOCK_MOBILE ; le serveur
 * en CENTRAL / TAMPON / MOBILE. Une valeur inconnue retombe sur le central,
 * seul type toujours présent.
 */
function typeEntrepotServeur(locationType: string): 'CENTRAL' | 'TAMPON' | 'MOBILE' {
  const brut = (locationType ?? '').toUpperCase();
  if (brut.includes('BUFFER') || brut.includes('TAMPON')) return 'TAMPON';
  if (brut.includes('MOBILE')) return 'MOBILE';
  return 'CENTRAL';
}

/**
 * Les écrans transmettent des instants ISO ; les rapports du serveur attendent
 * des dates seules (`yyyy-MM-dd`).
 */
function jourSeul(instant: string): string {
  return (instant ?? '').slice(0, 10);
}

/** Type d'entrepôt serveur → type d'emplacement écran. */
const TYPE_ENTREPOT_VERS_CLIENT: Record<string, StockLocationDto['type']> = {
  CENTRAL: 'STOCK_CENTRAL',
  TAMPON: 'STOCK_BUFFER',
  MOBILE: 'STOCK_MOBILE',
};

/**
 * Emplacement serveur → emplacement écran.
 *
 * Le type devait être traduit : le serveur applique `TypeEntrepot.valueOf()` au
 * filtre reçu, si bien qu'un « STOCK_CENTRAL » envoyé tel quel faisait échouer
 * la requête — et la liste des entrepôts de destination revenait vide, rendant
 * toute création de réception impossible depuis l'écran.
 */
function mapEntrepotToLocation(e: any): StockLocationDto {
  const brut = String(e.type ?? '').toUpperCase();
  return {
    id: String(e.id ?? ''),
    type: TYPE_ENTREPOT_VERS_CLIENT[brut] ?? (brut as StockLocationDto['type']),
    typeLabel: e.typeLabel || e.typeLibelle || '',
    name: e.name || e.libelle || '',
    description: e.description ?? e.code,
    code: e.code,
    assignedUserId: e.matriculeProprietaire ?? undefined,
    active: e.actif ?? e.active ?? true,
  };
}

/** Catégorie serveur (français) → catégorie client (anglais). */
const CATEGORIE_VERS_CLIENT: Record<string, Category> = {
  'PRODUIT_FINI': 'FINISHED_PRODUCT', 'MATIERE_PREMIERE': 'RAW_MATERIAL',
  'CONSOMMABLE': 'CONSUMABLE', 'MATERIEL': 'EQUIPMENT', 'EMBALLAGE': 'PACKAGING',
};

/** Map backend ProduitResponse → frontend Product.
 *  Le serveur rend des champs français : code, marque, gamme, variete,
 *  designation, categorie, prix*, conditionnements. */
function mapProduitToProduct(d: any): Product {
  const matType = d.materialType || d.categorie || d.categorieLibelle || d.gamme || '';
  const category: Category = CATEGORIE_VERS_CLIENT[matType] ?? 'FINISHED_PRODUCT';
  const unit = (d.unit || d.uniteVenteCommerciale || d.conditionnementCommercial || 'KG') as UnitType;
  return {
    id: d.id,
    sku: d.sku || d.code || '',
    variantId: undefined,
    barcode: undefined,
    category,
    unit,
    unitPriceAmount: d.unitPriceAmount ?? d.prixDetaillant ?? d.prixGrossiste ?? d.prixDistributeur ?? 0,
    active: d.active ?? d.actif ?? true,
    designation: d.designation || '',
    packagingType: d.packagingType || d.conditionnementCommercial || undefined,
    quantityPerCarton: d.quantityPerCarton ?? undefined,
    unitWeight: undefined,
    volume: undefined,
    materialType: matType as any,
  };
}

/**
 * Map JournalAuditResponse (journal d'audit général) → StockAuditLog.
 *
 * La ressource du journal est la classe du contrôleur appelé (« ReceptionController »),
 * sa référence l'entité concernée quand l'appel en désigne une.
 */
function mapJournalToAuditLog(d: any): StockAuditLog {
  return {
    id: d.id,
    entityType: d.ressource ?? '',
    entityId: d.referenceEntite ?? '',
    action: d.typeAction ?? '',
    userMatricule: d.matricule,
    userName: d.matricule,
    oldValues: undefined,
    newValues: undefined,
    reason: d.succes === false ? (d.messageErreur ?? d.details) : d.details,
    timestamp: d.horodatage ?? '',
  };
}

/** Statuts de lot — serveur (français) ↔ écran (anglais). */
const STATUT_LOT_VERS_CLIENT: Record<string, BatchStatus> = {
  DISPONIBLE: 'AVAILABLE',
  RESERVE: 'RESERVED',
  QUARANTAINE: 'QUARANTINE',
  PERIME: 'EXPIRED',
  EPUISE: 'EMPTY',
  RETIRE: 'DELETED',
};

const STATUT_LOT_VERS_SERVEUR: Record<BatchStatus, string> = {
  AVAILABLE: 'DISPONIBLE',
  RESERVED: 'RESERVE',
  QUARANTINE: 'QUARANTAINE',
  EXPIRED: 'PERIME',
  EMPTY: 'EPUISE',
  DELETED: 'RETIRE',
};

/**
 * Map LotStockResponse (champs français) → StockBatch.
 *
 * Le serveur calcule lui-même ce que la date implique aujourd'hui — périmé,
 * périmant bientôt, jours restants. L'écran n'a pas à le recalculer : deux
 * calculs de la même chose finissent par ne plus dire la même chose.
 */
function mapLotToBatch(d: any): StockBatch {
  const brut = d?.statut ?? '';
  return {
    id: d?.id,
    batchNumber: d?.numeroLot ?? '',
    productId: 0,
    productCode: d?.codeProduit ?? '',
    productSku: d?.sku ?? d?.codeProduit ?? '',
    productName: d?.designation ?? undefined,
    supplierName: d?.codeFournisseur ?? undefined,
    manufactureDate: d?.dateFabrication ?? undefined,
    expiryDate: d?.datePeremption ?? undefined,
    initialQuantity: d?.quantiteInitiale ?? 0,
    remainingQuantity: d?.quantiteRestante ?? 0,
    locationId: d?.codeEntrepot ?? undefined,
    status: STATUT_LOT_VERS_CLIENT[brut] ?? (brut as BatchStatus),
    statusLabel: d?.statutLibelle ?? undefined,
    expired: d?.perime ?? false,
    expiringSoon: d?.perimeBientot ?? false,
    daysUntilExpiry: d?.joursAvantPeremption ?? null,
    notes: d?.observations ?? undefined,
    createdAt: d?.dateCreation ?? '',
    lastMovementAt: d?.dateDernierMouvement ?? undefined,
  };
}

/**
 * Statuts de dotation — le serveur en connaît trois, l'écran sept.
 * Les quatre autres correspondent à des étapes de contrôle qui n'existent pas
 * dans le circuit servi ; aucune dotation ne peut donc les porter.
 */
const STATUT_DOTATION_VERS_CLIENT: Record<string, DotationStatus> = {
  SOUMISE: 'PENDING',
  APPROUVEE: 'APPROVED',
  REFUSEE: 'REJECTED',
};

/**
 * Map DotationResponse (champs français) → DotationRequest (modèle anglais).
 *
 * Sans cette traduction, les objets arrivaient en français dans un modèle
 * anglais : `status` et `referenceNumber` restaient indéfinis, tous les filtres
 * d'onglet échouaient, et les actions partaient vers `/undefined/approuver`.
 */
function mapDotation(d: any): DotationRequest {
  const brut = d.statut ?? d.status;
  return {
    id: d.id,
    commercialId: d.matriculeCommercial ?? '',
    commercialMatricule: d.matriculeCommercial ?? '',
    commercialName: d.nomCommercial ?? d.matriculeCommercial ?? '',
    justification: d.justification,
    status: STATUT_DOTATION_VERS_CLIENT[brut] ?? (brut as DotationStatus),
    referenceNumber: d.numero ?? '',
    requestedAt: d.dateDotation,
    scheduledDate: d.dateEffet,
    approvedBy: d.matriculeGestionnaire,
    items: (d.lignes ?? d.items ?? []).map((l: any) => ({
      id: l.id,
      productId: l.productId ?? 0,
      productSku: l.sku ?? l.codeProduit ?? '',
      productName: l.designation ?? l.codeProduit ?? '',
      packagingType: l.sku,
      requestedQuantity: l.quantiteDemandee ?? 0,
      approvedQuantity: l.quantiteApprouvee ?? undefined,
      notes: l.motifModification,
    })),
  } as DotationRequest;
}

/** Map backend NiveauStockDetailResponse (French fields) → frontend StockLevel (English fields). */
function mapNiveauToStockLevel(d: any): StockLevel {
  return {
    id: d.id,
    productId: d.produitId,
    warehouseId: d.entrepotId,
    quantity: d.quantite,
    reservedQty: 0,
    reorderPoint: d.seuilReapprovisionnement,
    safetyStock: d.stockSecurite,
    lastUpdated: d.dateModification,
    product: undefined,
    warehouse: undefined,
    availableQty: d.quantite,
    alertLevel: d.niveauAlerte,
    stockValue: d.valeurStock != null ? Number(d.valeurStock) : undefined,
    productName: d.designation,
    productSku: d.sku || d.codeProduit,
    productUnit: undefined,
    productCategory: d.categorie,
    warehouseName: d.libelleEntrepot,
    warehouseType: d.typeEntrepot,
    materialType: d.materialType || d.gamme,
    brandName: d.brandName || d.marque,
  };
}

/** Map backend TableauBordStockResponse (French fields) → frontend DashboardStatsResponse (English fields). */
function mapDashboardResponse(d: any): DashboardStatsResponse {
  return {
    totalStockLevels: d.totalLignesStock ?? 0,
    totalStockValue: d.valeurTotaleStock != null ? Number(d.valeurTotaleStock) : 0,
    criticalAlerts: d.alertesCritiques ?? 0,
    lowAlerts: d.alertesFaibles ?? 0,
    pendingReceipts: d.receptionsEnAttente ?? 0,
    todayReceipts: d.receptionsDuJour ?? 0,
    todayMovements: d.mouvementsDuJour ?? 0,
    pendingBatches: d.lotsEnAttente ?? 0,
    activeInternalOrders: d.commandesInternesActives ?? 0,
    todaySales: d.ventesDuJour != null ? Number(d.ventesDuJour) : 0,
  };
}

/**
 * Statuts de réception — traduction serveur ↔ client.
 *
 * Le serveur nomme `EN_ATTENTE_VALIDATION_1`, `EN_ATTENTE_VALIDATION_2`,
 * `VALIDEE`, `REJETEE` (`StatutReception`). Les écrans comparent à des valeurs
 * anglaises. Sans traduction, `mapReceptionToReceipt` recopiait le statut
 * français dans un champ que l'interface confrontait à ses constantes
 * anglaises : aucune comparaison ne réussissait, et toutes les réceptions
 * tombaient dans la même case.
 */
const STATUT_VERS_CLIENT: Record<string, ReceiptStatus> = {
  EN_ATTENTE_VALIDATION_1: 'PENDING_FIRST_VALIDATION',
  EN_ATTENTE_VALIDATION_2: 'PENDING_SECOND_VALIDATION',
  VALIDEE: 'VALIDATED',
  REJETEE: 'REJECTED',
};

const STATUT_VERS_SERVEUR: Record<ReceiptStatus, string> = {
  PENDING_FIRST_VALIDATION: 'EN_ATTENTE_VALIDATION_1',
  PENDING_SECOND_VALIDATION: 'EN_ATTENTE_VALIDATION_2',
  VALIDATED: 'VALIDEE',
  REJECTED: 'REJETEE',
};

/**
 * Statut client d'une réception.
 *
 * Un statut inconnu n'est pas remplacé par « en attente de 1ʳᵉ validation » :
 * une réception rejetée présentée comme en attente serait revalidée par
 * quelqu'un. Il est renvoyé tel quel, et l'anomalie se voit à l'écran.
 */
function statutClient(d: any): ReceiptStatus {
  const brut = d.status || d.statut;
  if (!brut) return 'PENDING_FIRST_VALIDATION';
  return STATUT_VERS_CLIENT[brut] ?? (brut as ReceiptStatus);
}

/**
 * Corps d'une validation de réception. Une observation vide n'est pas envoyée :
 * le serveur normalise déjà le blanc en absence, autant ne pas le faire voyager.
 */
function corpsNotes(notes?: string): { notes?: string } {
  const propre = notes?.trim();
  return propre ? { notes: propre } : {};
}

/** Map backend ReceiptResponse (English fields) → frontend Receipt.
 *  Backend ReceiptResponse: receiptNumber, receptionType, receptionTypeLabel, sourceLabel,
 *  destinationLocationId, destinationLocationName, status, statusLabel, items, etc. */
function mapReceptionToReceipt(d: any): Receipt {
  return {
    id: d.id,
    receiptNumber: d.receiptNumber || d.numero || '',
    receptionType: d.receptionType || d.categorie || 'FOURNISSEUR',
    receptionTypeLabel: d.receptionTypeLabel || d.receptionType || d.categorie || '',
    sourceLabel: d.sourceLabel || d.source || '',
    sourceId: d.sourceId,
    receiptDate: d.receiptDate || d.dateReception || '',
    destinationLocationId: d.destinationLocationId || d.codeEntrepotCible || '',
    destinationLocationName: d.destinationLocationName || d.codeEntrepotCible || '',
    status: statutClient(d),
    statusLabel: d.statusLabel || d.statutLibelle || d.status || '',
    createdBy: d.createdBy,
    firstValidator: d.firstValidator || d.matriculeValidateur1,
    firstValidatedAt: d.firstValidatedAt || d.dateValidation1,
    firstValidationNotes: d.firstValidationNotes || d.notesValidation1,
    secondValidator: d.secondValidator || d.matriculeValidateur2,
    secondValidatedAt: d.secondValidatedAt || d.dateValidation2,
    secondValidationNotes: d.secondValidationNotes || d.notesValidation2,
    // Le serveur calcule l'écart significatif (aEcartSignificatif) : au moins
    // une ligne dépasse le seuil de la réception. On le montre au validateur.
    hasSignificantDeviation: d.ecartSignificatif ?? false,
    // Le serveur n'exige aucun code de confirmation : il n'existe pas de code
    // secret par fonctionnalité. Le champ reste dans le modèle pour ne pas
    // casser les gabarits, toujours faux.
    requiresAuthCode: false,
    rejectionReason: d.rejectionReason || d.motifRejet,
    rejectedBy: d.rejectedBy,
    rejectedAt: d.rejectedAt,
    items: (d.items || d.lignes || []).map((l: any) => ({
      id: l.id ? Number(l.id) : undefined,
      productId: l.productId ?? 0,
      productName: l.productName || l.codeProduit || l.sku || '',
      productSku: l.productSku || l.sku || l.codeProduit || '',
      productUnit: l.productUnit || '',
      orderedQty: l.orderedQty ?? l.quantiteAttendue ?? 0,
      receivedQty: l.receivedQty ?? l.quantiteRecue ?? 0,
      deviation: l.deviation ?? l.ecart ?? 0,
      deviationPercent: l.deviationPercent ?? l.pourcentageEcart ?? 0,
    })),
  };
}

/** Map backend MouvementResponse (French fields) → frontend StockMovement (English fields). */
function mapMouvementToStockMovement(d: any): StockMovement {
  return {
    id: d.id,
    type: d.type as MovementType,
    fromLocationId: d.codeEntrepotSource,
    toLocationId: d.codeEntrepotCible,
    productId: 0,
    productSku: d.sku || d.codeProduit,
    quantity: d.quantiteEnBase,
    referenceNumber: d.referenceDocument || d.reference,
    notes: d.motif,
    requestedAt: d.horodatage,
    reference: d.reference,
    createdBy: undefined,
    createdAt: d.horodatage,
  };
}

export interface ReportData {
  id: number;
  title?: string;
  type: string;
  periodStart: string;
  periodEnd: string;
  format: string;
  generatedBy: string;
  generatedAt: string;
  status?: string;
  filePath?: string;
}

export interface GenerateReportRequest {
  type: string;
  periodStart: string;
  periodEnd: string;
  generatedBy: string;
  format: string;
}

export interface StockLocationDto {
  id: string;
  /** Type côté écran ; le serveur dit CENTRAL / TAMPON / MOBILE. */
  type: 'STOCK_CENTRAL' | 'STOCK_BUFFER' | 'STOCK_MOBILE' | 'MAGASIN';
  typeLabel: string;
  name: string;
  description?: string;
  /** Code de l'entrepôt côté serveur — c'est par lui que les stocks s'y rattachent. */
  code?: string;
  /** Matricule du commercial pour un entrepôt mobile. */
  assignedUserId?: string;
  active?: boolean;
}

export interface StockThresholdDto {
  id?: number;
  locationId: string;
  productId: number;
  productSku: string;
  minimumThreshold: number;
  maximumThreshold: number;
  reorderThreshold: number;
  reorderQuantity: number;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}

/** Un produit vu par ses attributs de classification. */
export interface ProductClassification {
  id: number;
  brand: string;
  range: string;
  variety: string;
  packaging: string;
  quantityPerCarton: number;
  classificationCode: string;
  designation: string;
}

export interface BufferValuationItem {
  productSku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  totalValue: number;
  priceType: string;
  currency: string;
}

export interface BufferValuationResponse {
  totalValue: number;
  itemCount: number;
  currency: string;
  items: BufferValuationItem[];
}

interface PageResponse<T> {
  contenu: T[];
  totalElements?: number;
  totalPages?: number;
  pageCourante?: number;
  taillePage?: number;
  dernierePage?: boolean;
}

@Injectable({ providedIn: 'root' })
export class StockApiRepository {
  private readonly api = inject(ApiService);

  private pageParams = { page: '0', size: '200' };

  // ── CATALOGUE ─────────────────────────────────────────────
  /**
   * Le serveur expose le catalogue sous `stock/produits`. Il n'accepte comme
   * filtres que marque, gamme, variété et actif : envoyer `materialType` le
   * ferait ignorer en silence et rendrait une liste complète qui aurait l'air
   * filtrée. Le tri par catégorie se fait donc ici, sur la réponse.
   */
  getProducts(materialType?: ReceptionType): Observable<Product[]> {
    return this.api.get<any[]>('stock/produits')
      .pipe(
        map(r => {
          const raw = (r as any);
          const list = Array.isArray(raw) ? raw : (raw?.donnees ?? []);
          const produits: Product[] = list.map(mapProduitToProduct);
          if (!materialType) return produits;
          const attendue = CATEGORIE_VERS_CLIENT[materialType];
          return attendue ? produits.filter(p => p.category === attendue) : produits;
        }),
        catchError(() => of([] as Product[]))
      );
  }

  /**
   * Marques, gammes et variétés n'ont pas de tables dédiées : le serveur les
   * dérive des produits et les sert d'un bloc sous `stock/catalogue/hierarchie`,
   * avec des ids synthétiques stables au sein d'une réponse. Les trois getters
   * ci-dessous découpent cette réponse unique.
   */
  private hierarchieCatalogue(): Observable<CatalogueHierarchie> {
    return this.api.get<ApiResponse<CatalogueHierarchie>>('stock/catalogue/hierarchie').pipe(
      map(r => r.donnees ?? { marques: [], gammes: [], varietes: [] }),
      catchError(() => of({ marques: [], gammes: [], varietes: [] } as CatalogueHierarchie))
    );
  }
  getBrands(): Observable<Brand[]> {
    return this.hierarchieCatalogue().pipe(
      map(h => h.marques.map(m => ({ id: m.id, name: m.nom, code: m.code, active: true } as Brand)))
    );
  }
  getLines(brandId?: number): Observable<ProductLine[]> {
    return this.hierarchieCatalogue().pipe(
      map(h => h.gammes
        .filter(g => brandId == null || g.marqueId === brandId)
        .map(g => ({ id: g.id, brandId: g.marqueId, name: g.nom, code: g.code, active: true } as ProductLine)))
    );
  }
  getVariants(productLineId?: number): Observable<ProductVariant[]> {
    return this.hierarchieCatalogue().pipe(
      map(h => h.varietes
        .filter(v => productLineId == null || v.gammeId === productLineId)
        .map(v => ({ id: v.id, productLineId: v.gammeId, name: v.nom, code: v.code, active: true } as ProductVariant)))
    );
  }
  /**
   * Crée un produit au catalogue.
   *
   * Le serveur exige marque, gamme et variété : un produit sans place dans la
   * hiérarchie ne serait retrouvable par aucun filtre. Les écrans qui n'en
   * proposent pas — le catalogue matériel, par exemple — les dérivent de la
   * catégorie.
   */
  createProduct(data: {
    code: string; designation: string; categorie: string;
    marque?: string; gamme?: string; variete?: string;
    conditionnementCommercial?: string; uniteVenteCommerciale?: string;
    prixDetaillant?: number;
  }): Observable<Product> {
    const secours = data.categorie;
    return this.api.post<ApiResponse<any>>('stock/produits', {
      code: data.code,
      marque: data.marque || secours,
      gamme: data.gamme || secours,
      variete: data.variete || data.designation,
      designation: data.designation,
      categorie: data.categorie,
      prixDetaillant: data.prixDetaillant,
      conditionnementCommercial: data.conditionnementCommercial,
      uniteVenteCommerciale: data.uniteVenteCommerciale,
      conditionnements: [],
    }).pipe(map(r => mapProduitToProduct(r.donnees)));
  }

  /** Modifie les informations descriptives d'un produit. Les champs omis ne bougent pas. */
  updateProduct(code: string, data: {
    designation?: string; marque?: string; gamme?: string; variete?: string;
    conditionnementCommercial?: string; uniteVenteCommerciale?: string; prixDetaillant?: number;
  }): Observable<Product> {
    return this.api.put<ApiResponse<any>>(`stock/produits/${encodeURIComponent(code)}`, data)
      .pipe(map(r => mapProduitToProduct(r.donnees)));
  }

  /**
   * Désactive un produit. Le serveur ne le supprime pas : un produit référencé
   * par des mouvements passés doit rester lisible.
   */
  deactivateProduct(code: string): Observable<void> {
    return this.api.delete<ApiResponse<void>>(`stock/produits/${encodeURIComponent(code)}`)
      .pipe(map(() => undefined));
  }

  /**
   * Classification d'un produit : marque → gamme → variété → conditionnement.
   *
   * Il n'existe pas de table de classifications : ces attributs sont portés
   * par le produit lui-même. Un produit est donc sa propre classification, et
   * le serveur sait déjà filtrer sur marque, gamme et variété.
   */
  getClassifications(marque?: string, gamme?: string, variete?: string): Observable<ProductClassification[]> {
    const params: Record<string, string> = {};
    if (marque) params['marque'] = marque;
    if (gamme) params['gamme'] = gamme;
    if (variete) params['variete'] = variete;
    return this.api.get<ApiResponse<any[]>>('stock/produits', Object.keys(params).length ? params : undefined).pipe(
      map(r => (r.donnees ?? []).map((d: any): ProductClassification => ({
        id: d.id,
        brand: d.marque ?? '',
        range: d.gamme ?? '',
        variety: d.variete ?? '',
        packaging: d.conditionnementCommercial ?? d.uniteVenteCommerciale ?? '',
        quantityPerCarton: (d.conditionnements ?? [])[0]?.facteurConversionVersBase ?? 0,
        classificationCode: d.code ?? '',
        designation: d.designation ?? '',
      }))),
      catchError(() => of([] as ProductClassification[]))
    );
  }

  /** Marques distinctes, telles que le serveur les dérive du catalogue. */
  getBrandNames(): Observable<string[]> {
    return this.hierarchieCatalogue().pipe(map(h => h.marques.map(m => m.nom).filter(Boolean)));
  }

  /** Gammes d'une marque. */
  getRangeNames(marque: string): Observable<string[]> {
    return this.hierarchieCatalogue().pipe(map(h => {
      const m = h.marques.find(x => x.nom === marque);
      if (!m) return [];
      return h.gammes.filter(g => g.marqueId === m.id).map(g => g.nom).filter(Boolean);
    }));
  }

  getWarehouses(): Observable<Warehouse[]> {
    return this.api.get<any>('stock/entrepots').pipe(
      map(r => {
        const raw = r as any;
        const list = Array.isArray(raw) ? raw : (raw?.donnees ?? []);
        return list as Warehouse[];
      }),
      catchError(() => of([] as Warehouse[]))
    );
  }
  getSuppliers(): Observable<Supplier[]> {
    return this.api.get<ApiResponse<Supplier[]>>('stock/fournisseurs').pipe(map(r => r.donnees ?? []), catchError(() => of([] as Supplier[])));
  }

  // ── DASHBOARD (/api/stock/dashboard/...) ───────────────
  getDashboard(): Observable<DashboardStatsResponse> {
    return this.api.get<ApiResponse<any>>('stock/dashboard').pipe(map(r => mapDashboardResponse(r.donnees)), catchError(() => of({ totalStockLevels: 0, totalStockValue: 0, criticalAlerts: 0, lowAlerts: 0, pendingReceipts: 0, todayReceipts: 0, todayMovements: 0, pendingBatches: 0, activeInternalOrders: 0, todaySales: 0 })));
  }

  getStockLevels(): Observable<StockLevel[]> {
    return this.api.get<ApiResponse<any[]>>('stock/levels').pipe(
      map(r => (r.donnees ?? []).map(mapNiveauToStockLevel)),
      catchError(() => of([]))
    );
  }

  getStockLevelsByWarehouse(id: number): Observable<StockLevel[]> {
    return this.api.get<ApiResponse<any[]>>(`stock/levels/warehouse/${id}`).pipe(map(r => (r.donnees ?? []).map(mapNiveauToStockLevel)), catchError(() => of([])));
  }

  getAlerts(): Observable<StockLevel[]> {
    return this.api.get<ApiResponse<any[]>>('stock/levels/alerts').pipe(
      map(r => (r.donnees ?? []).map(mapNiveauToStockLevel)),
      catchError(() => of([]))
    );
  }

  getCriticalAlerts(): Observable<StockLevel[]> {
    return this.getAlerts().pipe(map(levels => levels.filter(sl => sl.alertLevel === 'CRITIQUE')));
  }

  adjustStock(stockLevelId: number, newQuantity: number, reason: string, requestedBy?: string): Observable<StockLevel> {
    return this.api.post<ApiResponse<any>>('stock/levels/adjust', { stockLevelId, newQuantity, reason }).pipe(map(r => mapNiveauToStockLevel(r.donnees)));
  }

  transferToBuffer(id: number, quantity: number): Observable<void> {
    return this.api.post<ApiResponse<any>>(`stock/levels/${id}/transfer-to-buffer?quantity=${quantity}`, {}).pipe(map(r => r.donnees!));
  }

  /**
   * « Article en stock » et « niveau de stock » désignent la même chose côté
   * serveur : une quantité d'un produit dans un entrepôt. Il n'existe qu'une
   * ressource, `stock/levels` — ces trois méthodes s'y rattachent plutôt que
   * de faire vivre une seconde nomenclature en parallèle.
   */
  getAllStockItems(): Observable<StockLevel[]> {
    return this.getStockLevels();
  }

  getStockItemsByLocation(locationId: string): Observable<StockLevel[]> {
    const entrepotId = Number(locationId);
    return Number.isFinite(entrepotId) && entrepotId > 0
      ? this.getStockLevelsByWarehouse(entrepotId)
      // L'écran a transmis un code d'entrepôt et non son identifiant : on
      // charge tout et on trie sur le code, plutôt que d'appeler une route
      // avec un identifiant qui n'en est pas un.
      : this.getStockLevels().pipe(
          map(niveaux => niveaux.filter(n =>
            n.warehouseName === locationId || String(n.warehouseId) === locationId))
        );
  }

  /**
   * Les conditionnements appartiennent au produit, pas à l'entrepôt : le même
   * sachet a le même SKU partout. `locationId` est donc ignoré — il l'était
   * déjà, la route qui le portait n'ayant jamais existé.
   */
  getPackagingsForProduct(_locationId: string, productSku: string): Observable<any[]> {
    return this.api.get<ApiResponse<any>>(`stock/produits/${encodeURIComponent(productSku)}`).pipe(
      map(r => (r.donnees as any)?.conditionnements ?? []),
      catchError(() => of([]))
    );
  }

  replenishBuffer(productSku: string, quantity: number, requestedBy: string, notes?: string): Observable<any> {
    return this.api.post('stock/transferts', {
      codeProduit: productSku,
      quantite: quantity,
      matriculeDemandeur: requestedBy,
      notes
    }).pipe(catchError(() => of({})));
  }

  addToBuffer(productSku: string, quantity: number, requestedBy: string, notes?: string): Observable<any> {
    return this.api.post('stock/transferts', {
      codeProduit: productSku,
      quantite: quantity,
      matriculeDemandeur: requestedBy,
      notes
    }).pipe(catchError(() => of({})));
  }

  /**
   * Valorisation — dérivée des niveaux de stock, seule source de quantités.
   *
   * Le paramètre `entrepot` de `stock/levels` est un **code** d'entrepôt, pas
   * un type : lui passer « TAMPON » ne filtrait rien de ce qu'on croyait. On
   * charge donc l'ensemble une fois et on regroupe sur `typeEntrepot`, que le
   * serveur rend déjà avec chaque ligne.
   */
  private niveauxBruts(): Observable<any[]> {
    return this.api.get<ApiResponse<any[]>>('stock/levels').pipe(
      map(r => r.donnees ?? []),
      catchError(() => of([] as any[]))
    );
  }

  /** Valorise un ensemble de lignes, éventuellement avec des prix imposés. */
  private valoriser(lignes: any[], prixImposes?: Record<string, number>): BufferValuationResponse {
    const items: BufferValuationItem[] = lignes.map((d: any) => {
      const sku = d.sku || d.codeProduit || '';
      const quantite = d.quantite ?? 0;
      const prixImpose = prixImposes?.[sku];
      const unitPrice = prixImpose ?? d.prixUnitaire ?? 0;
      const totalValue = prixImpose != null
        ? quantite * prixImpose
        : (d.valeurStock != null ? Number(d.valeurStock) : quantite * unitPrice);
      return {
        productSku: sku,
        productName: d.designation || '',
        quantity: quantite,
        unitPrice,
        totalValue,
        priceType: prixImpose != null ? 'saisi' : 'detaillant',
        currency: 'XAF',
      };
    });
    return {
      totalValue: items.reduce((somme, i) => somme + (i.totalValue || 0), 0),
      itemCount: items.length,
      currency: 'XAF',
      items,
    };
  }

  private lignesDuType(lignes: any[], type: 'CENTRAL' | 'TAMPON' | 'MOBILE'): any[] {
    return lignes.filter(d => String(d.typeEntrepot ?? '').toUpperCase() === type);
  }

  getValuationByType(type: 'CENTRAL' | 'TAMPON' | 'MOBILE',
                     prixImposes?: Record<string, number>): Observable<BufferValuationResponse> {
    return this.niveauxBruts().pipe(map(l => this.valoriser(this.lignesDuType(l, type), prixImposes)));
  }

  /** Valeur par type d'entrepôt et total, en un seul appel. */
  getValuationSummary(prixImposes?: Record<string, number>): Observable<{
    centralValue: number; bufferValue: number; mobileValue: number; totalValue: number;
  }> {
    return this.niveauxBruts().pipe(map(lignes => {
      const central = this.valoriser(this.lignesDuType(lignes, 'CENTRAL'), prixImposes).totalValue;
      const tampon = this.valoriser(this.lignesDuType(lignes, 'TAMPON'), prixImposes).totalValue;
      const mobile = this.valoriser(this.lignesDuType(lignes, 'MOBILE'), prixImposes).totalValue;
      return {
        centralValue: central,
        bufferValue: tampon,
        mobileValue: mobile,
        // Somme des trois types, et non de toutes les lignes : une ligne dont
        // le type serait inconnu ne doit pas gonfler un total dont aucune part
        // ne la porte.
        totalValue: central + tampon + mobile,
      };
    }));
  }

  getBufferValuation(): Observable<BufferValuationResponse> {
    return this.getValuationByType('TAMPON');
  }

  // ── RECEPTIONS (/api/stock/receptions) ────────────────────────────────
  //
  // Ces appels visaient une API qui n'existe pas : préfixe « v1/ », routes de
  // filtrage dédiées, création unique, noms de validation anglais. Les réponses
  // arrivaient en 500 que le `catchError` transformait en liste vide — l'écran
  // paraissait fonctionner sur un stock réellement vide.
  //
  // Le serveur (`ReceptionController`) expose : `GET /` filtré par `statut` et
  // `bonCommande`, `GET /{numero}`, `POST /fournisseur`, `POST /production`,
  // `POST /{numero}/valider-etape1`, `/valider-etape2`, `/{numero}/rejeter`.

  private listerReceptions(params?: Record<string, string>): Observable<Receipt[]> {
    return this.api.get<any[]>('stock/receptions', params).pipe(
      map(r => {
        const raw = r as any;
        const list = Array.isArray(raw) ? raw : (raw?.donnees ?? []);
        return list.map(mapReceptionToReceipt);
      }),
      catchError(() => of([] as Receipt[]))
    );
  }

  /**
   * Le serveur ne filtre pas par catégorie : le tri sur `type` est fait ici,
   * après réception. Envoyer un paramètre qu'il ignore aurait rendu une liste
   * complète en la présentant comme filtrée.
   */
  private parCategorie(receipts: Receipt[], type?: ReceptionType): Receipt[] {
    return type ? receipts.filter(r => r.receptionType === type) : receipts;
  }

  getReceipts(type?: ReceptionType, status?: ReceiptStatus): Observable<Receipt[]> {
    const params: Record<string, string> = {};
    if (status) params['statut'] = STATUT_VERS_SERVEUR[status] ?? status;
    return this.listerReceptions(params).pipe(map(l => this.parCategorie(l, type)));
  }

  getPendingFirstValidation(type?: ReceptionType): Observable<Receipt[]> {
    return this.listerReceptions({ statut: 'EN_ATTENTE_VALIDATION_1' })
      .pipe(map(l => this.parCategorie(l, type)));
  }

  getPendingSecondValidation(type?: ReceptionType): Observable<Receipt[]> {
    return this.listerReceptions({ statut: 'EN_ATTENTE_VALIDATION_2' })
      .pipe(map(l => this.parCategorie(l, type)));
  }

  getReceiptByNumber(receiptNumber: string): Observable<Receipt> {
    return this.api.get<any>(`stock/receptions/${encodeURIComponent(receiptNumber)}`).pipe(
      map(r => {
        const raw = r as any;
        return mapReceptionToReceipt(raw?.donnees ?? raw);
      })
    );
  }

  /**
   * Le serveur sépare les deux origines : une réception fournisseur est
   * rattachée à un bon de commande, une réception de production à un lot. Elles
   * n'ont ni les mêmes champs obligatoires ni le même contrôle. La route est
   * choisie sur la présence d'un numéro de lot.
   */
  createReceipt(data: any): Observable<Receipt> {
    const numeroLot = data?.numeroLotProduction ?? data?.productionBatchNumber ?? data?.sourceId;
    const versProduction = !!numeroLot && (data?.source === 'PRODUCTION' || !!data?.numeroLotProduction);

    const lignes = (data?.lignes ?? data?.items ?? []).map((l: any) => ({
      codeProduit: l.codeProduit ?? l.productSku ?? l.productId,
      sku: l.sku ?? l.productSku,
      quantiteAttendue: l.quantiteAttendue ?? l.orderedQty ?? 0,
      quantiteRecue: l.quantiteRecue ?? l.receivedQty ?? 0,
      ...(versProduction ? {} : { idLigneBC: l.idLigneBC ?? l.orderLineId }),
    }));

    const corps = versProduction
      ? {
          numeroLotProduction: numeroLot,
          codeEntrepotCible: data?.codeEntrepotCible ?? data?.destinationLocationId,
          lignes,
        }
      : {
          categorie: data?.categorie ?? data?.receptionType,
          codeFournisseur: data?.codeFournisseur ?? data?.supplierCode ?? data?.sourceId,
          numeroBonCommande: data?.numeroBonCommande ?? data?.purchaseOrderNumber,
          codeEntrepotCible: data?.codeEntrepotCible ?? data?.destinationLocationId,
          lignes,
        };

    return this.api.post<any>(`stock/receptions/${versProduction ? 'production' : 'fournisseur'}`, corps).pipe(
      map(r => {
        const raw = r as any;
        return mapReceptionToReceipt(raw?.donnees ?? raw);
      })
    );
  }

  /**
   * Les deux validations acceptent un corps facultatif ne portant que les
   * observations du validateur — le serveur identifie celui-ci par le jeton.
   * Les observations sont conservées avec la réception et ressortent en
   * `firstValidationNotes` / `secondValidationNotes`.
   *
   * Il n'y a plus de code de confirmation : dans la version de référence il
   * n'était comparé à rien, et le système ne comporte pas de code secret par
   * fonctionnalité.
   */
  validateFirst(receiptNumber: string, notes?: string): Observable<Receipt> {
    return this.api.post<any>(
      `stock/receptions/${encodeURIComponent(receiptNumber)}/valider-etape1`, corpsNotes(notes)
    ).pipe(
      map(r => {
        const raw = r as any;
        return mapReceptionToReceipt(raw?.donnees ?? raw);
      })
    );
  }

  validateSecond(receiptNumber: string, notes?: string): Observable<Receipt> {
    return this.api.post<any>(
      `stock/receptions/${encodeURIComponent(receiptNumber)}/valider-etape2`, corpsNotes(notes)
    ).pipe(
      map(r => {
        const raw = r as any;
        return mapReceptionToReceipt(raw?.donnees ?? raw);
      })
    );
  }

  /** Le motif voyage en paramètre de requête, pas dans le corps. */
  rejectReceipt(receiptNumber: string, reason: string): Observable<Receipt> {
    return this.api.post<any>(
      `stock/receptions/${encodeURIComponent(receiptNumber)}/rejeter`, {}, { motif: reason }
    ).pipe(
      map(r => {
        const raw = r as any;
        return mapReceptionToReceipt(raw?.donnees ?? raw);
      })
    );
  }

  // ── STOCK LOCATIONS (/api/stock/entrepots) ─────────────
  getStockLocationsByType(type: 'STOCK_CENTRAL' | 'STOCK_BUFFER' | 'STOCK_MOBILE'): Observable<StockLocationDto[]> {
    return this.api.get<any>('stock/entrepots', { type: typeEntrepotServeur(type) }).pipe(
      map(r => {
        const raw = r as any;
        const list = Array.isArray(raw) ? raw : (raw?.donnees ?? []);
        return list.map(mapEntrepotToLocation);
      }),
      catchError(() => of([] as StockLocationDto[]))
    );
  }

  // ── PRODUCTION BATCHES (via /api/production/lots) ────
  getBatches(): Observable<ProductionBatch[]> {
    return this.api.get<ApiResponse<any[]>>('production/lots').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }
  getPendingBatches(): Observable<ProductionBatch[]> {
    return this.api.get<ApiResponse<any[]>>('production/lots/a-valider').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }
  getBatchById(id: number): Observable<ProductionBatch> { return this.api.get<ApiResponse<any>>(`production/lots/${id}`).pipe(map(r => r.donnees!)); }
  declareBatch(data: any): Observable<ProductionBatch> { return this.api.post<ApiResponse<any>>('production/lots', data).pipe(map(r => r.donnees!)); }
  validateBatch(id: number, notes = ''): Observable<ProductionBatch> {
    return this.api.patch<ApiResponse<any>>(`production/lots/${encodeURIComponent(id)}/valider`, {}).pipe(map(r => r.donnees!));
  }
  rejectBatch(id: number, reason: string): Observable<ProductionBatch> {
    return this.api.patch<ApiResponse<any>>(`production/lots/${encodeURIComponent(id)}/rejeter?motif=${encodeURIComponent(reason)}`, {}).pipe(map(r => r.donnees!));
  }
  getBatchStats(): Observable<Record<string, unknown>> {
    return this.api.get<ApiResponse<any[]>>('production/lots').pipe(map(r => ({ total: (r.donnees ?? []).length })), catchError(() => of({})));
  }

  // ── INTERNAL ORDERS (/api/stock/internal-orders) ────────
  getOrders(): Observable<InternalOrder[]> {
    return this.api.get<ApiResponse<InternalOrder[]>>('stock/internal-orders').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }
  getActiveOrders(): Observable<InternalOrder[]> {
    return this.api.get<ApiResponse<InternalOrder[]>>('stock/internal-orders/active').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }
  getOrderById(id: number): Observable<InternalOrder> {
    return this.api.get<ApiResponse<InternalOrder>>(`stock/internal-orders/${id}`).pipe(map(r => r.donnees!));
  }
  createOrder(data: any): Observable<InternalOrder> {
    return this.api.post<ApiResponse<InternalOrder>>('stock/internal-orders', data).pipe(map(r => r.donnees!));
  }
  approveOrder(id: number, approverId: string, approverName: string): Observable<InternalOrder> {
    return this.api.post<ApiResponse<InternalOrder>>(`stock/internal-orders/${id}/approve`, { approverId, approverName }).pipe(map(r => r.donnees!));
  }
  cancelOrder(id: number, cancelledBy: string, reason: string): Observable<InternalOrder> {
    return this.api.post<ApiResponse<InternalOrder>>(`stock/internal-orders/${id}/cancel`, { cancelledBy, reason }).pipe(map(r => r.donnees!));
  }
  deliverOrder(id: number, productId: number, deliveredQty: number): Observable<InternalOrder> {
    return this.api.post<ApiResponse<InternalOrder>>(`stock/internal-orders/${id}/deliver`, { productId, deliveredQty }).pipe(map(r => r.donnees!));
  }

  // ── MOVEMENTS (/api/stock/mouvements) ─────────────────────
  getMovements(): Observable<StockMovement[]> {
    return this.api.get<ApiResponse<PageResponse<any>>>('stock/mouvements', this.pageParams).pipe(map(r => (r.donnees?.contenu ?? []).map(mapMouvementToStockMovement)), catchError(() => of([])));
  }

  // ── COMMERCIALS ───────────────────────────────────────────
  getCommercials(): Observable<Commercial[]> {
    return this.api.get<ApiResponse<Commercial[]>>('stock/commercials').pipe(
      map(r => r.donnees ?? []),
      catchError(() =>
        this.api.get<ApiResponse<MobileStockSummary[]>>('stock/stock-mobile/suivi/tous').pipe(
          map(r => r.donnees ?? []),
          catchError(() => of([] as MobileStockSummary[])),
          map(list => list.map(s => ({
            id: this.matriculeToId(s.commercialMatricule),
            matricule: s.commercialMatricule,
            name: s.commercialName || s.commercialMatricule,
            phone: s.phone || '',
            vehicle: s.vehicle || '',
            active: true
          })))
        )
      )
    );
  }

  getInfoProduits(date?: string): Observable<InfoProduits[]> {
    const fallback = catchError<InfoProduits[], Observable<InfoProduits[]>>(() => of([]));
    return date
      ? this.api.get<ApiResponse<InfoProduits[]>>(`stock/info-produits?date=${date}`).pipe(map(r => r.donnees ?? []), fallback)
      : this.api.get<ApiResponse<InfoProduits[]>>('stock/info-produits').pipe(map(r => r.donnees ?? []), fallback);
  }

  // ── MOBILE STOCK TRACKING ───────────────────────────────────
  getMobileStockAll(): Observable<MobileStockSummary[]> {
    return this.api.get<ApiResponse<MobileStockSummary[]>>('stock/stock-mobile/suivi/tous').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  getMobileStockByCommercial(matricule: string): Observable<MobileStockSummary> {
    return this.api.get<MobileStockSummary>(`stock/stock-mobile/${matricule}`);
  }

  getMobileStockRotation(matricule: string, days = 30): Observable<MobileStockRotation> {
    return this.api.get<MobileStockRotation>(`stock/stock-mobile/suivi/commercial/${matricule}/courant`, { days: String(days) });
  }

  getSlowStockCommercials(daysThreshold = 30, salesThreshold = 10): Observable<SlowStockCommercial[]> {
    return this.api.get<SlowStockCommercial[]>('stock/stock-mobile/suivi/tous', {
      daysThreshold: String(daysThreshold),
      salesThreshold: String(salesThreshold)
    }).pipe(catchError(() => of([])));
  }

  private matriculeToId(matricule: string): number {
    let hash = 0;
    for (let i = 0; i < matricule.length; i++) {
      hash = ((hash << 5) - hash) + matricule.charCodeAt(i);
      hash |= 0;
    }
    return Math.abs(hash);
  }

  getFinishedProducts(): Observable<Product[]> {
    return this.getProducts().pipe(
      map(ps => ps.filter((p: Product) => p.materialType === 'PRODUIT_FINI')),
      catchError(() => of([]))
    );
  }

  // ── DOTATIONS (/api/stock/dotations/...) ─────────────────
  //
  // Le circuit servi par le serveur compte trois états : SOUMISE, puis
  // APPROUVEE ou REFUSEE. L'écran en attendait sept — vérification du paiement,
  // validation des quantités, examen, approbation, exécution — chacun confié à
  // un acteur distinct.
  //
  // Ce n'est pas un écart de vocabulaire : c'est un circuit de contrôle qui
  // n'existe pas côté serveur. Les étapes absentes échouent donc explicitement.
  // Les faire retomber sur « approuver » reviendrait à franchir en un geste
  // quatre contrôles que quelqu'un a voulu séparer.
  //
  // Autre point : l'écran passait l'identité de l'acteur en paramètre
  // (approverId, rejecterId…). Le serveur la prend dans le jeton et ignore ce
  // qu'on lui envoie — c'est ce qu'il faut : une identité fournie par le client
  // laisserait n'importe qui approuver au nom de n'importe qui.

  private etapeAbsente<T>(etape: string): Observable<T> {
    return throwError(() => new Error(
      `${etape} : cette étape n'existe pas dans le circuit de dotation du serveur, ` +
      `qui va de « soumise » à « approuvée » ou « refusée ».`));
  }

  getDotations(): Observable<DotationRequest[]> {
    return this.api.get<ApiResponse<any[]>>('stock/dotations').pipe(map(r => (r.donnees ?? []).map(mapDotation)), catchError(() => of([])));
  }
  getDotationById(numero: string | number): Observable<DotationRequest> {
    return this.api.get<ApiResponse<any>>(`stock/dotations/${encodeURIComponent(String(numero))}`).pipe(map(r => mapDotation(r.donnees)));
  }
  getPendingDotations(): Observable<DotationRequest[]> {
    return this.api.get<ApiResponse<any[]>>('stock/dotations', { statut: 'SOUMISE' }).pipe(map(r => (r.donnees ?? []).map(mapDotation)), catchError(() => of([])));
  }
  getPaymentVerifiedDotations(): Observable<DotationRequest[]> {
    return this.etapeAbsente('Dotations à paiement vérifié');
  }
  getQuantityValidatedDotations(): Observable<DotationRequest[]> {
    return this.etapeAbsente('Dotations à quantités validées');
  }
  getReviewedDotations(): Observable<DotationRequest[]> {
    return this.etapeAbsente('Dotations examinées');
  }
  getDotationsByCommercial(matricule: string): Observable<DotationRequest[]> {
    return this.api.get<ApiResponse<any[]>>('stock/dotations', { commercial: matricule }).pipe(map(r => (r.donnees ?? []).map(mapDotation)), catchError(() => of([])));
  }
  createDotation(data: CreateDotationRequest): Observable<DotationRequest> {
    return this.api.post<ApiResponse<any>>('stock/dotations', data).pipe(map(r => mapDotation(r.donnees)));
  }

  verifyPayment(_numero: string | number, _verifierId: string): Observable<DotationRequest> {
    return this.etapeAbsente('Vérification du paiement');
  }

  /**
   * Le serveur ne valide pas les quantités d'un bloc : il modifie une ligne à
   * la fois, avec son motif (`PATCH /{numero}/ligne`). Chaque ligne dont la
   * quantité change est donc envoyée séparément, et la dernière réponse — celle
   * qui porte l'état complet après toutes les modifications — est rendue.
   */
  validateQuantities(numero: string | number, _validatorId: string, comments: string, items?: any[]): Observable<DotationRequest> {
    const lignes = (items ?? []).filter(l => l?.id != null && l?.approvedQuantity != null);
    if (lignes.length === 0) return this.etapeAbsente('Validation des quantités sans ligne modifiée');
    const requetes = lignes.map(l => this.api.patch<ApiResponse<any>>(
      `stock/dotations/${encodeURIComponent(String(numero))}/ligne`,
      { idLigne: String(l.id), nouvelleQuantite: l.approvedQuantity, nouveauSku: l.productSku, motif: comments }
    ).pipe(map(r => mapDotation(r.donnees))));
    return concat(...requetes).pipe(last());
  }

  reviewDotation(numero: string | number, validatorId: string, reviewComments: string, items?: any[]): Observable<DotationRequest> {
    return this.validateQuantities(numero, validatorId, reviewComments, items);
  }

  approveDotation(numero: string | number, _approverId: string): Observable<DotationRequest> {
    return this.api.post<ApiResponse<DotationRequest>>(
      `stock/dotations/${encodeURIComponent(String(numero))}/approuver`, {}
    ).pipe(map(r => mapDotation(r.donnees)));
  }

  reviewAndApproveDotation(numero: string | number, managerId: string, reviewComments: string, items?: any[]): Observable<DotationRequest> {
    return this.validateQuantities(numero, managerId, reviewComments, items).pipe(
      switchMap(() => this.approveDotation(numero, managerId))
    );
  }

  rejectDotation(numero: string | number, _rejecterId: string, reason: string): Observable<DotationRequest> {
    return this.api.post<ApiResponse<DotationRequest>>(
      `stock/dotations/${encodeURIComponent(String(numero))}/refuser`, {}, { motif: reason }
    ).pipe(map(r => mapDotation(r.donnees)));
  }

  /**
   * L'approbation débite déjà le tampon et crédite le stock mobile : il n'y a
   * pas d'exécution distincte à déclencher ensuite.
   */
  executeDotation(_numero: string | number, _executorId: string): Observable<DotationRequest> {
    return this.etapeAbsente("Exécution d'une dotation");
  }

  deleteDotation(_numero: string | number): Observable<void> {
    return this.etapeAbsente("Suppression d'une dotation");
  }

  // ── RAPPORTS (/api/stock/rapports/...) ────────────────────
  //
  // Le serveur ne tient pas de registre de rapports : il n'y a ni table, ni
  // identifiant, ni historique. Chaque rapport est calculé à la demande et
  // renvoyé directement en fichier. Les quatre méthodes de registre ci-dessous
  // n'ont donc rien à interroger — elles échouent explicitement plutôt que de
  // rendre une liste vide qui se lirait « aucun rapport pour l'instant ».

  private aucunRegistre<T>(quoi: string): Observable<T> {
    return throwError(() => new Error(
      `${quoi} : le serveur ne conserve pas d'historique de rapports. ` +
      `Les rapports sont générés à la demande depuis les boutons d'export.`));
  }

  getAllReports(): Observable<ReportData[]> { return this.aucunRegistre('Liste des rapports'); }
  getReportsByUser(_generatedBy: string): Observable<ReportData[]> { return this.aucunRegistre('Rapports par utilisateur'); }
  getReportsByType(_type: string): Observable<ReportData[]> { return this.aucunRegistre('Rapports par type'); }
  getReportById(_id: number): Observable<ReportData> { return this.aucunRegistre("Consultation d'un rapport"); }
  generateReport(_data: GenerateReportRequest): Observable<ReportData> { return this.aucunRegistre('Génération différée'); }
  deleteReport(_id: number): Observable<void> { return this.aucunRegistre("Suppression d'un rapport"); }
  downloadReportFile(_id: number): Observable<Blob> { return this.aucunRegistre('Téléchargement par identifiant'); }

  // ── EXPORTS (/api/stock/rapports/...) ─────────────────────
  //
  // Le serveur sert les rapports en français, sous deux contrôleurs :
  //  - `rapports/etat-stock`, `rapports/mouvements`, `rapports/fiche-ntfoods`
  //    (paramétrables, format pdf|xlsx|csv|json) ;
  //  - `rapports/<sujet>/<format>` pour les tableaux préparés.
  // Les méthodes ci-dessous y renvoient. Celles qui n'ont pas de contrepartie
  // le disent au lieu de télécharger un fichier vide.

  private aucunRapport(quoi: string): Observable<Blob> {
    return throwError(() => new Error(
      `${quoi} : ce rapport n'existe pas encore côté serveur.`));
  }

  exportFicheSyntheseStock(locationType: string, motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/etat-stock', {
      type: typeEntrepotServeur(locationType), format: 'pdf', ...(motif ? { motif } : {})
    });
  }

  exportFicheSyntheseSorties(periodStart: string, periodEnd: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/mouvements', {
      type: 'SORTIE', debut: jourSeul(periodStart), fin: jourSeul(periodEnd), format: 'pdf'
    });
  }

  exportFicheSyntheseEntrees(periodStart: string, periodEnd: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/mouvements', {
      type: 'ENTREE', debut: jourSeul(periodStart), fin: jourSeul(periodEnd), format: 'pdf'
    });
  }

  /** Entrées et sorties confondues : le rapport mouvements sans filtre de type. */
  exportFicheSyntheseGlobale(periodStart: string, periodEnd: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/mouvements', {
      debut: jourSeul(periodStart), fin: jourSeul(periodEnd), format: 'pdf'
    });
  }

  exportFicheSyntheseNTFoods(motif?: string, nom?: string, ville?: string, zone?: string, nombreColis?: number): Observable<Blob> {
    const params: Record<string, string> = {};
    if (motif) params['motif'] = motif;
    if (nom) params['nom'] = nom;
    if (ville) params['ville'] = ville;
    if (zone) params['zone'] = zone;
    if (nombreColis != null) params['nombreColis'] = String(nombreColis);
    return this.api.getBlob('stock/rapports/fiche-ntfoods', params);
  }

  /** `locationId` est ici un code d'entrepôt, pas un type. */
  exportStockItems(locationId: string, format: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/etat-stock', { entrepot: locationId, format });
  }

  exportStockMovements(format: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/mouvements', { format });
  }

  /** La valorisation n'est servie qu'en Excel, quel que soit l'emplacement. */
  exportRapportValorisation(_locationType: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/valorisation/excel');
  }

  exportRapportAlertes(locationType: string): Observable<Blob> {
    return this.aucunRapport(`Rapport d'alertes (${locationType})`);
  }

  /** Le rapport d'inventaire servi est celui des écarts constatés au comptage. */
  exportInventaireComplet(_locationType: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/ecarts-inventaire/excel');
  }

  exportRapportRotation(_locationType: string, _periodStart: string, _periodEnd: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/rotation/excel');
  }

  exportRapportReceptions(periodStart: string, periodEnd: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/mouvements', {
      type: 'ENTREE_RECEPTION_FOURNISSEUR', debut: jourSeul(periodStart), fin: jourSeul(periodEnd), format: 'pdf'
    });
  }

  /** Dotations rapprochées des ventes — le seul rapport de dotation servi. */
  exportRapportDotations(_periodStart: string, _periodEnd: string, _motif?: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/dotations-ventes/excel');
  }

  exportRapportReapprovisionnement(_motif?: string): Observable<Blob> {
    return this.aucunRapport('Rapport de réapprovisionnement du tampon');
  }

  exportRapportTransferts(locationType: string, _periodStart: string, _periodEnd: string, _motif?: string): Observable<Blob> {
    return this.aucunRapport(`Rapport de transferts (${locationType})`);
  }

  // ── EXPORTS EXCEL ─────────────────────────────────────────
  exportExcelItems(locationType: string): Observable<Blob> {
    const t = typeEntrepotServeur(locationType);
    if (t === 'CENTRAL') return this.api.getBlob('stock/rapports/stock-central/excel');
    if (t === 'TAMPON') return this.api.getBlob('stock/rapports/tampon/excel');
    return this.api.getBlob('stock/rapports/stocks-mobiles/excel');
  }

  exportExcelMovements(): Observable<Blob> {
    return this.api.getBlob('stock/rapports/mouvements/excel');
  }

  exportExcelValorisation(_locationType: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/valorisation/excel');
  }

  /** Le tableau de performances tient lieu de rapport global multi-feuilles. */
  exportExcelGlobal(_locationType: string, _periodStart: string, _periodEnd: string): Observable<Blob> {
    return this.api.getBlob('stock/rapports/performances/excel');
  }

  // ── LOCATIONS (/api/stock/entrepots) ──────────────────────
  getLocations(): Observable<StockLocationDto[]> {
    return this.api.get<any>('stock/entrepots').pipe(
      map(r => {
        const raw = r as any;
        const list = Array.isArray(raw) ? raw : (raw?.donnees ?? []);
        return list.map(mapEntrepotToLocation);
      }),
      catchError(() => of([] as StockLocationDto[]))
    );
  }

  /**
   * Crée un entrepôt. Pour un entrepôt mobile, le serveur exige le matricule du
   * commercial et dérive lui-même le code et le libellé.
   */
  createLocation(data: { type: string; name?: string; code?: string; managerMatricule?: string }): Observable<StockLocationDto> {
    return this.api.post<ApiResponse<any>>('stock/entrepots', {
      type: typeEntrepotServeur(data.type),
      code: data.code,
      libelle: data.name,
      matriculeCommercial: data.managerMatricule,
    }).pipe(map(r => mapEntrepotToLocation(r.donnees)));
  }

  getStockAlerts(): Observable<StockAlert[]> {
    return this.getAlerts().pipe(
      map(levels => levels.map((sl: StockLevel) => ({
        produitId: sl.productId,
        designation: sl.productName || sl.productSku || '',
        magasin: sl.warehouseName || '',
        stockActuel: sl.quantity,
        stockMinimum: sl.reorderPoint,
        niveau: sl.alertLevel || 'NORMAL',
        leadTimeDays: sl.leadTimeDays
      })))
    );
  }

  // ── LOTS (/api/stock/lots/...) ────────────────────────────
  //
  // Le suivi par lot était le seul manque fonctionnel réel du module Stock :
  // les autres familles d'appels avaient déjà leur contrepartie française.
  // Le serveur désigne un lot par son numéro (« LOT-20260818-0001 »), pas par
  // son identifiant technique.

  getStockBatches(): Observable<StockBatch[]> {
    return this.api.get<ApiResponse<any[]>>('stock/lots').pipe(
      map(r => (r.donnees ?? []).map(mapLotToBatch)), catchError(() => of([]))
    );
  }

  getStockBatch(numeroLot: string): Observable<StockBatch> {
    return this.api.get<ApiResponse<any>>(`stock/lots/${encodeURIComponent(numeroLot)}`)
      .pipe(map(r => mapLotToBatch(r.donnees)));
  }

  /** Le serveur filtre par code produit ; l'identifiant numérique ne lui parle pas. */
  getBatchesByProduct(codeProduit: string): Observable<StockBatch[]> {
    return this.api.get<ApiResponse<any[]>>('stock/lots', { produit: codeProduit }).pipe(
      map(r => (r.donnees ?? []).map(mapLotToBatch)), catchError(() => of([]))
    );
  }

  getBatchesBySku(productSku: string): Observable<StockBatch[]> {
    return this.api.get<ApiResponse<any[]>>('stock/lots', { sku: productSku }).pipe(
      map(r => (r.donnees ?? []).map(mapLotToBatch)), catchError(() => of([]))
    );
  }

  getBatchesByLocation(locationId: string): Observable<StockBatch[]> {
    return this.api.get<ApiResponse<any[]>>('stock/lots', { entrepot: locationId }).pipe(
      map(r => (r.donnees ?? []).map(mapLotToBatch)), catchError(() => of([]))
    );
  }

  getExpiringSoonBatches(days = 30): Observable<StockBatch[]> {
    return this.api.get<ApiResponse<any[]>>('stock/lots/perimant-bientot', { jours: String(days) }).pipe(
      map(r => (r.donnees ?? []).map(mapLotToBatch)), catchError(() => of([]))
    );
  }

  getExpiredBatches(): Observable<StockBatch[]> {
    return this.api.get<ApiResponse<any[]>>('stock/lots/perimes').pipe(
      map(r => (r.donnees ?? []).map(mapLotToBatch)), catchError(() => of([]))
    );
  }

  createStockBatch(data: Partial<StockBatch>): Observable<StockBatch> {
    return this.api.post<ApiResponse<any>>('stock/lots', {
      numeroLot: data.batchNumber,
      codeProduit: data.productCode ?? data.productSku,
      sku: data.productSku,
      designation: data.productName,
      codeFournisseur: data.supplierName,
      codeEntrepot: data.locationId,
      dateFabrication: data.manufactureDate,
      datePeremption: data.expiryDate,
      quantiteInitiale: data.initialQuantity,
      observations: data.notes,
    }).pipe(map(r => mapLotToBatch(r.donnees)));
  }

  /**
   * Prélève au plus proche de la péremption. Le serveur rend les lots entamés :
   * la quantité sortie de chacun se lit dans leur reste.
   */
  consumeBatchQuantity(payload: { productSku: string; locationId: string; quantity: number; motif?: string }): Observable<StockBatch[]> {
    return this.api.post<ApiResponse<any[]>>('stock/lots/consommer', {
      sku: payload.productSku,
      codeEntrepot: payload.locationId,
      quantite: payload.quantity,
      motif: payload.motif,
    }).pipe(map(r => (r.donnees ?? []).map(mapLotToBatch)));
  }

  updateStockBatchStatus(numeroLot: string, status: BatchStatus | string): Observable<StockBatch> {
    return this.api.patch<ApiResponse<any>>(
      `stock/lots/${encodeURIComponent(numeroLot)}/statut`, {}, { statut: STATUT_LOT_VERS_SERVEUR[status as BatchStatus] ?? String(status) }
    ).pipe(map(r => mapLotToBatch(r.donnees)));
  }

  /** Retire le lot du suivi sans l'effacer : refusé s'il reste de la marchandise. */
  deleteStockBatch(numeroLot: string): Observable<StockBatch> {
    return this.api.delete<ApiResponse<any>>(`stock/lots/${encodeURIComponent(numeroLot)}`)
      .pipe(map(r => mapLotToBatch(r.donnees)));
  }

  // ── JOURNAL D'AUDIT (/api/audit/...) ──────────────────────
  // Le journal n'est pas propre au Stock : un aspect intercepte tout appel de
  // tout contrôleur et l'enregistre. On interroge donc le journal général en
  // se restreignant au module « stock ».
  //
  // Réservé au Directeur Général, au Contrôleur Général et à l'Administrateur.
  // Un autre rôle reçoit 403 — c'est voulu : un journal d'audit consultable par
  // ceux qu'il surveille ne surveille plus rien.

  private journalStock(chemin: string, params?: Record<string, string>): Observable<StockAuditLog[]> {
    return this.api.get<ApiResponse<{ contenu?: any[]; content?: any[] }>>(chemin, { size: '200', ...params }).pipe(
      map(r => {
        const page = r.donnees as any;
        const lignes: any[] = page?.contenu ?? page?.content ?? [];
        return lignes.map(mapJournalToAuditLog);
      }),
      catchError(() => of([] as StockAuditLog[]))
    );
  }

  getAuditLogs(): Observable<StockAuditLog[]> {
    return this.journalStock('audit/module/stock');
  }

  /**
   * Le journal indexe par module, utilisateur et type d'action — pas par
   * entité. Le tri sur la ressource et sa référence se fait donc ici, sur les
   * entrées du module Stock.
   */
  getAuditLogsForEntity(entityType: string, entityId: string): Observable<StockAuditLog[]> {
    return this.getAuditLogs().pipe(
      map(l => l.filter(e => e.entityType === entityType && e.entityId === entityId))
    );
  }

  getAuditLogsByEntityType(entityType: string): Observable<StockAuditLog[]> {
    return this.getAuditLogs().pipe(map(l => l.filter(e => e.entityType === entityType)));
  }

  getAuditLogsByUser(userMatricule: string): Observable<StockAuditLog[]> {
    return this.journalStock(`audit/utilisateur/${encodeURIComponent(userMatricule)}`);
  }

  getAuditLogsByAction(action: string): Observable<StockAuditLog[]> {
    return this.journalStock(`audit/type/${encodeURIComponent(action)}`);
  }

  // ── BACKEND ALERTES (/api/stock/alertes/...) ────────────────
  getBackendAlerts(): Observable<StockAlertEntity[]> {
    return this.api.get<ApiResponse<StockAlertEntity[]>>('stock/alertes', { statut: 'NOUVELLE' }).pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  getCriticalBackendAlerts(): Observable<StockAlertEntity[]> {
    return this.api.get<ApiResponse<StockAlertEntity[]>>('stock/levels/alerts/critical').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  getUnacknowledgedBackendAlerts(): Observable<StockAlertEntity[]> {
    return this.api.get<ApiResponse<StockAlertEntity[]>>('stock/alertes', { statut: 'NOUVELLE' }).pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  acknowledgeBackendAlert(reference: string | number, userId: string): Observable<StockAlertEntity> {
    return this.api.post<ApiResponse<StockAlertEntity>>(`stock/alertes/${reference}/lue`, {}).pipe(map(r => r.donnees!));
  }

  resolveBackendAlert(reference: string | number): Observable<StockAlertEntity> {
    return this.api.post<ApiResponse<StockAlertEntity>>(`stock/alertes/${reference}/traiter`, {}).pipe(map(r => r.donnees!));
  }

  resolveAlertsByProduct(productId: number): Observable<number> {
    return of(0);
  }

  resolveAlertsByProductSku(productSku: string): Observable<number> {
    return of(0);
  }

  triggerAlertChecks(): Observable<void> {
    return of(undefined);
  }

  getPerformanceStats(): Observable<Record<string, unknown>> {
    return of({});
  }

  // ── THRESHOLDS (/api/stock/seuils) ─────────────────────
  setThresholds(locationId: string, productSku: string, reorderPoint: number, safetyStock: number): Observable<unknown> {
    return this.api.post('stock/seuils', { codeProduit: productSku, seuilReapprovisionnement: reorderPoint, stockSecurite: safetyStock });
  }

  setDefaultThresholds(locationId: string, reorderPoint: number, safetyStock: number): Observable<unknown> {
    return this.api.post('stock/seuils', { seuilReapprovisionnement: reorderPoint, stockSecurite: safetyStock });
  }

  getItemsWithThresholds(locationId: string): Observable<unknown[]> {
    return this.api.get<ApiResponse<unknown[]>>('stock/seuils').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  // ── THRESHOLDS (/api/stock/seuils) ───────────────────────
  getAllThresholds(): Observable<StockThresholdDto[]> {
    return this.api.get<ApiResponse<StockThresholdDto[]>>('stock/seuils').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }
  getThresholdsByLocation(locationId: string): Observable<StockThresholdDto[]> {
    return this.api.get<ApiResponse<StockThresholdDto[]>>('stock/seuils').pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }
  createThreshold(data: { codeProduit: string; typeEntrepot: string; seuilReapprovisionnement: number }): Observable<StockThresholdDto> {
    return this.api.post<ApiResponse<StockThresholdDto>>('stock/seuils', data).pipe(map(r => r.donnees!));
  }
  updateThreshold(id: number, data: { codeProduit: string; typeEntrepot: string; seuilReapprovisionnement: number }): Observable<StockThresholdDto> {
    return this.api.post<ApiResponse<StockThresholdDto>>('stock/seuils', data).pipe(map(r => r.donnees!));
  }
  deleteThreshold(id: number): Observable<unknown> {
    return this.api.delete(`stock/seuils/${id}`);
  }
  checkAllThresholds(): Observable<unknown> {
    return this.api.post('stock/seuils/evaluer', {});
  }

  // ── PHYSICAL INVENTORY (/api/stock/inventaires) ────────
  createPhysicalInventory(locationId: string, countedBy: string, notes?: string): Observable<PhysicalInventory> {
    return this.api.post<ApiResponse<PhysicalInventory>>('stock/inventaires', { type: 'COMPLET', entrepot: locationId, justification: notes }).pipe(map(r => r.donnees!));
  }

  getPhysicalInventories(status?: string, countedBy?: string): Observable<PhysicalInventory[]> {
    const params: Record<string, string> = {};
    if (status) params['statut'] = status;
    return this.api.get<ApiResponse<PhysicalInventory[]>>('stock/inventaires', params).pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  getPhysicalInventory(id: number): Observable<PhysicalInventory> {
    return this.api.get<ApiResponse<PhysicalInventory>>(`stock/inventaires/${id}`).pipe(map(r => r.donnees!));
  }

  getPhysicalInventoryItems(id: number): Observable<PhysicalInventoryItem[]> {
    return this.api.get<ApiResponse<PhysicalInventoryItem[]>>(`stock/inventaires/${id}/items`).pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  getPhysicalInventoryDiscrepancies(id: number): Observable<PhysicalInventoryItem[]> {
    return this.api.get<ApiResponse<PhysicalInventoryItem[]>>(`stock/inventaires/${id}/discrepancies`).pipe(map(r => r.donnees ?? []), catchError(() => of([])));
  }

  getPhysicalInventorySummary(id: number): Observable<PhysicalInventorySummary> {
    return this.api.get<ApiResponse<PhysicalInventorySummary>>(`stock/inventaires/${id}/summary`).pipe(map(r => r.donnees!));
  }

  countPhysicalInventoryItem(itemId: number, countedQuantity: number, notes?: string): Observable<PhysicalInventoryItem> {
    return this.api.put<ApiResponse<PhysicalInventoryItem>>(`stock/inventaires/items/${itemId}/count`, { countedQuantity, notes }).pipe(map(r => r.donnees!));
  }

  completePhysicalInventory(id: number): Observable<PhysicalInventory> {
    return this.api.post<ApiResponse<PhysicalInventory>>(`stock/inventaires/${id}/cloturer-comptage`, {}).pipe(map(r => r.donnees!));
  }

  validatePhysicalInventory(id: number, validatedBy: string, applyCorrections: boolean): Observable<PhysicalInventory> {
    return this.api.post<ApiResponse<PhysicalInventory>>(`stock/inventaires/${id}/valider`, {}).pipe(map(r => r.donnees!));
  }

  // ── CUSTOM REPORT (/api/stock/rapports) ──────────────
  generateCustomReport(criteria: CustomReportCriteriaDto): Observable<ReportData> {
    return this.api.post<ReportData>('stock/rapports/custom', criteria);
  }
}

export interface PhysicalInventory {
  id: number;
  reference: string;
  locationId: string;
  locationName: string;
  status: 'IN_PROGRESS' | 'COMPLETED' | 'VALIDATED';
  countedBy: string;
  validatedBy?: string;
  startedAt: string;
  completedAt?: string;
  validatedAt?: string;
  totalItems?: number;
  countedItems?: number;
  discrepancyCount?: number;
  notes?: string;
}

export interface PhysicalInventoryItem {
  id: number;
  inventoryId: number;
  stockItemId: number;
  productId: number;
  productSku: string;
  productName: string;
  packagingType: string;
  systemQuantity: number;
  countedQuantity?: number;
  discrepancy?: number;
  status: 'PENDING' | 'COUNTED' | 'VALIDATED';
  countedAt?: string;
  notes?: string;
}

export interface PhysicalInventorySummary {
  inventoryId: number;
  reference: string;
  status: string;
  totalItems: number;
  countedItems: number;
  pendingItems: number;
  positiveDiscrepancies: number;
  negativeDiscrepancies: number;
  totalDiscrepancies: number;
  discrepancyValue: number;
}

export interface CustomReportCriteriaDto {
  locationTypes?: string[];
  materialTypes?: string[];
  alertLevels?: string[];
  productSku?: string;
  productName?: string;
  minQuantity?: number;
  maxQuantity?: number;
  minValue?: number;
  maxValue?: number;
  periodStart?: string;
  periodEnd?: string;
  generatedBy: string;
  format?: string;
}
