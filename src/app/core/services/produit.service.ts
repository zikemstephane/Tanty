import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Produit, CategorieProduit } from '../models';
import PRODUITS from '../../../assets/mock-data/produits.json';

@Injectable({ providedIn: 'root' })
export class ProduitService {
  private readonly produits: Produit[] = (PRODUITS as any).produits;

  getAll(): Observable<Produit[]> {
    return of([...this.produits]);
  }

  getById(id: string): Observable<Produit | undefined> {
    return of(this.produits.find(p => p.id === id));
  }

  getByCategorie(categorie: CategorieProduit): Observable<Produit[]> {
    return of(this.produits.filter(p => p.categorie === categorie));
  }

  getActifs(): Observable<Produit[]> {
    return of(this.produits.filter(p => p.actif));
  }

  searchByNom(query: string): Observable<Produit[]> {
    const q = query.toLowerCase();
    return of(this.produits.filter(p => p.nom.toLowerCase().includes(q) || p.code.toLowerCase().includes(q)));
  }

  getStats(): { total: number; parCategorie: Record<string, number> } {
    const stats: Record<string, number> = {};
    this.produits.forEach(p => {
      stats[p.categorie] = (stats[p.categorie] || 0) + 1;
    });
    return { total: this.produits.length, parCategorie: stats };
  }
}
