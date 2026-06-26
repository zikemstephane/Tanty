import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProduitService } from '../../../../core/services/produit.service';
import { Produit, CategorieProduit, CATEGORIE_LABELS } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-produits-list',
  standalone: true,
  imports: [CommonModule, FormsModule, BreadcrumbComponent],
  templateUrl: './produits-list.component.html',
  styleUrls: ['./produits-list.component.scss']
})
export class ProduitsListComponent implements OnInit {
  produits    = signal<Produit[]>([]);
  searchQuery = signal('');
  filterCat   = signal('');
  viewMode    = signal<'grid' | 'table'>('grid');

  breadcrumbs = [{ label: 'Tableau de bord', url: '/dashboard', icon: 'home' }, { label: 'Catalogue Produits' }];

  categories: { value: string; label: string }[] = [
    { value: '', label: 'Toutes les catégories' },
    ...Object.entries(CATEGORIE_LABELS).map(([v, l]) => ({ value: v, label: l }))
  ];

  constructor(private svc: ProduitService) {}

  ngOnInit(): void { this.svc.getAll().subscribe(p => this.produits.set(p)); }

  get filtered(): Produit[] {
    const q = this.searchQuery().toLowerCase();
    return this.produits().filter(p =>
      (!this.filterCat() || p.categorie === this.filterCat()) &&
      (!q || p.nom.toLowerCase().includes(q) || p.code.toLowerCase().includes(q))
    );
  }

  catClass(cat: CategorieProduit): string {
    return { BOUILLIE_SOJA: 'badge--valide', CHOCOLAT: 'badge--attente', INGREDIENTS: 'badge--info', A_GRIGNOTER: 'badge--warning' }[cat] ?? '';
  }

  getCatLabel(cat: string): string { return CATEGORIE_LABELS[cat as CategorieProduit] ?? cat; }
}
