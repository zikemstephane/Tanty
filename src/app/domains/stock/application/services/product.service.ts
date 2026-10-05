import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, forkJoin, throwError } from 'rxjs';
import { environment } from '../../../../../environment/environment';
import {
  CreateBrandRequest,
  UpdateBrandRequest,
  CreateProductLineRequest,
  UpdateProductLineRequest,
  CreateProductVariantRequest,
  UpdateProductVariantRequest,
  CreateProductPriceRequest,
  UpdateProductPriceRequest,
  CreateProductRequest,
  UpdateProductRequest,
} from '../../domain/models/product.models';

/**
 * Catalogue produits — traduction de l'écran anglais vers l'API du serveur.
 *
 * Ce service visait `/api/products/**` : marques, gammes, variétés, prix et
 * statistiques, chacun comme une ressource autonome. **Aucun de ces points
 * d'entrée n'existe.** Ils viennent d'une branche parallèle du module Stock
 * dont le back-end n'a jamais été livré ; l'écran Produits appelait donc cinq
 * URL mortes et ne se remplissait jamais, la seule trace étant trois 404 dans
 * la console.
 *
 * Le serveur expose la même matière autrement, et cette différence est un choix
 * de modélisation, pas un décalage de nom :
 *
 *  - `/api/stock/produits` — le produit, ressource réelle et seule persistée ;
 *  - `/api/stock/catalogue/hierarchie` — marque → gamme → variété, **dérivées**
 *    des produits existants. Il n'y a pas de table des marques : une marque
 *    existe parce qu'un produit la porte. Les identifiants sont synthétiques et
 *    ne valent qu'au sein d'une réponse.
 *
 * Deux conséquences assumées, plutôt que maquillées :
 *
 *  1. Créer ou supprimer une marque, une gamme ou une variété n'a pas de sens
 *     ici — on crée un produit qui la porte. Ces méthodes échouent en clair
 *     plutôt que de laisser croire à un enregistrement.
 *  2. Les statistiques sont calculées à partir des produits réellement reçus,
 *     et non demandées à un `/stats` inexistant. Ce sont les mêmes données,
 *     comptées côté client.
 */
@Injectable({ providedIn: 'root' })
export class ProductService {
  private http = inject(HttpClient);
  private readonly produits = `${environment.apiUrl}/stock/produits`;
  private readonly catalogue = `${environment.apiUrl}/stock/catalogue`;

  /** Le serveur enveloppe toute réponse dans `{ succes, message, donnees }`. */
  private donnees<T>(source: Observable<any>): Observable<T> {
    return source.pipe(map(r => (r?.donnees ?? r) as T));
  }

  private hierarchie(): Observable<any> {
    return this.donnees<any>(this.http.get(`${this.catalogue}/hierarchie`));
  }

  /**
   * La hiérarchie n'étant pas administrable, toute écriture dessus est refusée
   * avec le motif. Un `of(null)` silencieux aurait affiché « enregistré » sur
   * un écran où rien ne l'aurait été.
   */
  private hierarchieNonAdministrable(acte: string): Observable<never> {
    return throwError(() => new Error(
      `${acte} : la hiérarchie du catalogue (marque, gamme, variété) est déduite ` +
      `des produits et n'est pas administrable directement. Créez ou modifiez ` +
      `le produit qui la porte.`
    ));
  }

  // ── Marques ────────────────────────────────────────────────────────────────
  getAllBrands(): Observable<any[]> {
    return this.hierarchie().pipe(
      map(h => (h?.marques ?? []).map((m: any) => ({
        id: m.id, name: m.nom, code: m.code, active: true,
      })))
    );
  }

  getActiveBrands(): Observable<any[]> {
    return this.getAllBrands();
  }

  createBrand(_request: CreateBrandRequest): Observable<any> {
    return this.hierarchieNonAdministrable('Création de marque');
  }

  updateBrand(_id: number, _request: UpdateBrandRequest): Observable<any> {
    return this.hierarchieNonAdministrable('Modification de marque');
  }

  deleteBrand(_id: number): Observable<void> {
    return this.hierarchieNonAdministrable('Suppression de marque');
  }

  // ── Gammes ─────────────────────────────────────────────────────────────────
  getAllProductLines(brandId?: number): Observable<any[]> {
    return this.hierarchie().pipe(
      map(h => (h?.gammes ?? [])
        .filter((g: any) => brandId == null || g.marqueId === brandId)
        .map((g: any) => ({
          id: g.id, brandId: g.marqueId, name: g.nom, code: g.code, active: true,
        })))
    );
  }

  createProductLine(_request: CreateProductLineRequest): Observable<any> {
    return this.hierarchieNonAdministrable('Création de gamme');
  }

  updateProductLine(_id: number, _request: UpdateProductLineRequest): Observable<any> {
    return this.hierarchieNonAdministrable('Modification de gamme');
  }

  deleteProductLine(_id: number): Observable<void> {
    return this.hierarchieNonAdministrable('Suppression de gamme');
  }

  // ── Variétés ───────────────────────────────────────────────────────────────
  getAllProductVariants(productLineId?: number): Observable<any[]> {
    return this.hierarchie().pipe(
      map(h => (h?.varietes ?? [])
        .filter((v: any) => productLineId == null || v.gammeId === productLineId)
        .map((v: any) => ({
          id: v.id, productLineId: v.gammeId, name: v.nom, code: v.code, active: true,
        })))
    );
  }

  createProductVariant(_request: CreateProductVariantRequest): Observable<any> {
    return this.hierarchieNonAdministrable('Création de variété');
  }

  updateProductVariant(_id: number, _request: UpdateProductVariantRequest): Observable<any> {
    return this.hierarchieNonAdministrable('Modification de variété');
  }

  deleteProductVariant(_id: number): Observable<void> {
    return this.hierarchieNonAdministrable('Suppression de variété');
  }

  // ── Produits ───────────────────────────────────────────────────────────────
  /**
   * Le produit du serveur, présenté sous les noms qu'attend l'écran. Le `code`
   * du catalogue tient lieu de `sku` : c'est lui qui identifie le produit, le
   * SKU au sens strict désignant un conditionnement.
   */
  private versEcran(p: any): any {
    const commercial = (p.conditionnements ?? [])
      .find((c: any) => c.sku === p.conditionnementCommercial);
    return {
      id: p.id,
      code: p.code,
      sku: p.code,
      designation: p.designation,
      brand: p.marque,
      productLine: p.gamme,
      variant: p.variete,
      category: p.categorieLibelle ?? p.categorie,
      categoryCode: p.categorie,
      materialType: p.categorieMetier,
      materialTypeLabel: p.categorieMetierLibelle,
      active: p.actif,
      unit: p.uniteVenteCommerciale,
      unitPrice: p.prixDetaillant ?? p.prixGrossiste ?? p.prixDistributeur ?? 0,
      prixDistributeur: p.prixDistributeur,
      prixGrossiste: p.prixGrossiste,
      prixDetaillant: p.prixDetaillant,
      tva: p.tva,
      packagingType: p.conditionnementCommercial,
      qtyPerCarton: commercial?.facteurConversionVersBase ?? null,
      unitWeight: commercial?.poidsUnitaireGrammes ?? null,
      barcode: commercial?.codeBarres ?? null,
      conditionnements: p.conditionnements ?? [],
      createdAt: p.dateCreation,
      updatedAt: p.dateModification,
    };
  }

  getAllProducts(materialType?: string, active?: boolean): Observable<any[]> {
    return this.donnees<any[]>(this.http.get(this.produits)).pipe(
      map(liste => (liste ?? [])
        .map(p => this.versEcran(p))
        .filter(p => !materialType || p.materialType === materialType)
        .filter(p => active === undefined || p.active === active))
    );
  }

  /**
   * Le serveur adresse un produit par son code, non par un identifiant
   * numérique. On accepte les deux : l'écran manipule des `id` issus des
   * listes, le reste de l'application manipule des codes.
   */
  getProductById(id: number | string): Observable<any> {
    if (typeof id === 'string') return this.getProductBySku(id);
    return this.getAllProducts().pipe(
      map(liste => liste.find(p => p.id === id))
    );
  }

  getProductBySku(code: string): Observable<any> {
    return this.donnees<any>(this.http.get(`${this.produits}/${encodeURIComponent(code)}`))
      .pipe(map(p => this.versEcran(p)));
  }

  createProduct(request: CreateProductRequest | any): Observable<any> {
    return this.donnees<any>(this.http.post(this.produits, this.versServeur(request, true)))
      .pipe(map(p => this.versEcran(p)));
  }

  updateProduct(code: number | string, request: UpdateProductRequest | any): Observable<any> {
    return this.donnees<any>(
      this.http.put(`${this.produits}/${encodeURIComponent(String(code))}`,
                    this.versServeur(request, false))
    ).pipe(map(p => this.versEcran(p)));
  }

  /** Désactivation, pas effacement : un produit vendu reste dans l'historique. */
  deleteProduct(code: number | string): Observable<void> {
    return this.donnees<void>(
      this.http.delete(`${this.produits}/${encodeURIComponent(String(code))}`)
    );
  }

  private versServeur(r: any, creation: boolean): any {
    const corps: any = {
      marque: r.brand ?? r.marque,
      gamme: r.productLine ?? r.gamme,
      variete: r.variant ?? r.variete,
      designation: r.designation,
      prixDistributeur: r.prixDistributeur ?? r.unitPrice,
      prixGrossiste: r.prixGrossiste ?? r.unitPrice,
      prixDetaillant: r.prixDetaillant ?? r.unitPrice,
      tva: r.tva ?? false,
      conditionnementCommercial: r.packagingType ?? r.conditionnementCommercial,
      uniteVenteCommerciale: r.unit ?? r.uniteVenteCommerciale,
    };
    if (creation) {
      corps.code = r.code ?? r.sku;
      corps.categorie = r.categoryCode ?? r.category ?? r.categorie;
      // Exigée par le domaine : elle détermine le circuit de production.
      corps.categorieMetier = r.materialType ?? r.categorieMetier;
      corps.conditionnements = r.conditionnements ?? [];
    }
    return corps;
  }

  // ── Prix ───────────────────────────────────────────────────────────────────
  /**
   * Les prix ne sont pas une ressource distincte : ils sont portés par le
   * produit (distributeur, grossiste, détaillant). Le prix de vente opposable
   * au commercial, lui, relève de la grille tarifaire homologuée
   * (`/api/commercial/tarification/grille`), qui est un autre acte et un autre
   * pouvoir — celui de la Chargée RP et du DG, pas du magasinier.
   */
  getProductPrices(productId: number | string): Observable<any[]> {
    return this.getProductById(productId).pipe(
      map(p => !p ? [] : [
        { id: 1, priceType: 'DISTRIBUTOR', amount: p.prixDistributeur ?? 0, currency: 'XAF', active: true },
        { id: 2, priceType: 'WHOLESALER', amount: p.prixGrossiste ?? 0, currency: 'XAF', active: true },
        { id: 3, priceType: 'RETAILER', amount: p.prixDetaillant ?? 0, currency: 'XAF', active: true },
      ].filter(x => x.amount > 0))
    );
  }

  getActiveProductPrices(productId: number | string): Observable<any[]> {
    return this.getProductPrices(productId);
  }

  getProductPriceByType(productId: number | string, priceType: string): Observable<any> {
    return this.getProductPrices(productId).pipe(
      map(liste => liste.find(p => p.priceType === priceType) ?? null)
    );
  }

  private champPrix(priceType: string): string {
    if (priceType === 'DISTRIBUTOR') return 'prixDistributeur';
    if (priceType === 'WHOLESALER') return 'prixGrossiste';
    return 'prixDetaillant';
  }

  createProductPrice(productId: number | string, request: CreateProductPriceRequest | any): Observable<any> {
    return this.updateProduct(productId, { [this.champPrix(request.priceType)]: request.amount });
  }

  updateProductPrice(productId: number | string, _priceId: number, request: UpdateProductPriceRequest | any): Observable<any> {
    return this.updateProduct(productId, { [this.champPrix(request.priceType)]: request.amount });
  }

  deleteProductPrice(productId: number | string, priceId: number): Observable<any> {
    const type = priceId === 1 ? 'DISTRIBUTOR' : priceId === 2 ? 'WHOLESALER' : 'RETAILER';
    return this.updateProduct(productId, { [this.champPrix(type)]: 0 });
  }

  // ── Statistiques ───────────────────────────────────────────────────────────
  /**
   * Comptées sur les produits réellement retournés. Le serveur n'expose pas de
   * `/stats` ; en fabriquer un côté client à partir de la même liste est exact,
   * inventer des chiffres ne le serait pas.
   */
  getStats(): Observable<any> {
    return forkJoin({
      produits: this.getAllProducts(),
      hierarchie: this.hierarchie(),
    }).pipe(
      map(({ produits, hierarchie }) => {
        const parType: Record<string, number> = {};
        const parCategorie: Record<string, number> = {};
        for (const p of produits) {
          const t = p.materialType || 'NON_CLASSE';
          const c = p.category || 'Non classée';
          parType[t] = (parType[t] ?? 0) + 1;
          parCategorie[c] = (parCategorie[c] ?? 0) + 1;
        }
        return {
          totalProducts: produits.length,
          activeProducts: produits.filter(p => p.active).length,
          inactiveProducts: produits.filter(p => !p.active).length,
          totalBrands: (hierarchie?.marques ?? []).length,
          totalProductLines: (hierarchie?.gammes ?? []).length,
          totalVariants: (hierarchie?.varietes ?? []).length,
          byMaterialType: parType,
          byCategory: parCategorie,
        };
      })
    );
  }
}
