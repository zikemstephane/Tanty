import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockApiRepository } from '../../../infrastructure/repositories/stock-api.repository';

/**
 * Une classification n'est pas une entité à part : c'est la façon dont un
 * produit se range dans la hiérarchie marque → gamme → variété. Elle se lit
 * donc dans le catalogue, et ne s'y crée ni ne s'y supprime séparément.
 */
interface Classification {
  id: number;
  brand: string;
  range: string;
  variety: string;
  packaging: string;
  quantityPerCarton: number;
  classificationCode: string;
  designation?: string;
}

@Component({
  selector: 'app-classification-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './classification-list.component.html',
  styleUrls: ['./classification-list.component.css']
})
export class ClassificationListComponent implements OnInit {
  private readonly repo = inject(StockApiRepository);

  classifications = signal<Classification[]>([]);
  brands = signal<string[]>([]);
  ranges = signal<string[]>([]);
  loading = signal(true);
  showForm = signal(false);
  saving = signal(false);
  selectedBrand = '';
  selectedRange = '';
  toastMsg = signal('');
  toastType = signal<'success' | 'error'>('success');

  showDeleteModal = signal(false);
  deleteTarget = signal<Classification | null>(null);

  newClassification: any = {
    brand: '', range: '', variety: '', packaging: '', packagingDetails: '',
    quantityPerCarton: 1, unitWeight: '', volume: '', cartonsPerAssortiment: 1
  };

  ngOnInit(): void {
    this.loadClassifications();
    this.loadBrands();
  }

  loadClassifications(): void {
    this.loading.set(true);
    this.repo.getClassifications().subscribe({
      next: (data) => {
        this.classifications.set(data || []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Erreur chargement classifications', 'error');
      }
    });
  }

  loadBrands(): void {
    this.repo.getBrandNames().subscribe({
      next: (data) => this.brands.set(data || []),
      error: () => this.showToast('Erreur chargement marques', 'error')
    });
  }

  onBrandChange(): void {
    if (this.selectedBrand) {
      this.repo.getRangeNames(this.selectedBrand).subscribe({
        next: (data) => this.ranges.set(data || []),
        error: () => this.showToast('Erreur chargement gammes', 'error')
      });
    } else {
      this.ranges.set([]);
    }
  }

  onRangeChange(): void {
    if (this.selectedRange) {
      this.loading.set(true);
      this.repo.getClassifications(this.selectedBrand, this.selectedRange).subscribe({
        next: (data) => {
          this.classifications.set(data || []);
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.showToast('Erreur lors du filtrage par gamme', 'error');
        }
      });
    } else if (this.selectedBrand) {
      this.filterByBrand();
    } else {
      this.loadClassifications();
    }
  }

  filterByBrand(): void {
    if (!this.selectedBrand) {
      this.loadClassifications();
      return;
    }
    this.loading.set(true);
    this.repo.getClassifications(this.selectedBrand).subscribe({
      next: (data) => {
        this.classifications.set(data || []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Erreur lors du filtrage', 'error');
      }
    });
  }

  toggleForm(): void {
    this.showForm.update(v => !v);
  }

  createClassification(): void {
    if (!this.newClassification.brand) {
      this.showToast('Marque requise', 'error');
      return;
    }
    // Créer une classification, c'est créer un produit : marque, gamme et
    // variété n'existent que portées par lui. Le catalogue est le seul endroit
    // où cela se fait, sinon deux nomenclatures se mettraient à diverger.
    this.showToast(
      'Une classification naît avec le produit : créez-le dans le catalogue produits.', 'error');
  }

  openDeleteModal(item: Classification): void {
    this.deleteTarget.set(item);
    this.showDeleteModal.set(true);
  }

  closeDeleteModal(): void {
    this.showDeleteModal.set(false);
    this.deleteTarget.set(null);
  }

  confirmDelete(): void {
    const item = this.deleteTarget();
    if (!item) return;
    this.closeDeleteModal();
    this.showToast(
      "Une classification ne se supprime pas seule : désactivez le produit dans le catalogue.", 'error');
  }

  resetForm(): void {
    this.newClassification = {
      brand: '', range: '', variety: '', packaging: '', packagingDetails: '',
      quantityPerCarton: 1, unitWeight: '', volume: '', cartonsPerAssortiment: 1
    };
  }

  get totalClassifications(): number {
    return this.classifications().length;
  }

  get totalBrands(): number {
    return this.brands().length;
  }

  private showToast(msg: string, type: 'success' | 'error'): void {
    this.toastMsg.set(msg);
    this.toastType.set(type);
    setTimeout(() => this.toastMsg.set(''), 4000);
  }
}
