import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../../core/http/api.service';

interface StockLocation {
  id: string;
  type: string;
  typeLabel: string;
  name: string;
  description?: string;
}

@Component({
  selector: 'app-localisation-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './localisation-list.component.html',
  styleUrls: ['./localisation-list.component.css']
})
export class LocalisationListComponent implements OnInit {
  private readonly api = inject(ApiService);

  locations = signal<StockLocation[]>([]);
  loading = signal(true);
  showForm = signal(false);
  saving = signal(false);
  toastMsg = signal('');
  toastType = signal<'success' | 'error'>('success');

  newLocation: any = { type: 'STOCK_CENTRAL', name: '', matriculeCommercial: '' };

  locationTypes = [
    { value: 'STOCK_CENTRAL', label: 'Stock Central' },
    { value: 'STOCK_BUFFER', label: 'Stock Tampon' },
    { value: 'STOCK_MOBILE', label: 'Stock Mobile' }
  ];

  ngOnInit(): void {
    this.loadLocations();
  }

  loadLocations(): void {
    this.loading.set(true);
    this.api.get<any>('stock/entrepots').subscribe({
      next: (res) => {
        const data = res?.donnees ?? res ?? [];
        this.locations.set(data || []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Erreur chargement localisations', 'error');
      }
    });
  }

  toggleForm(): void {
    this.showForm.update(v => !v);
  }

  createLocation(): void {
    if (!this.newLocation.name) {
      this.showToast('Nom requis', 'error');
      return;
    }
    this.saving.set(true);
    const payload: any = {
      type: this.newLocation.type,
      name: this.newLocation.name
    };
    if (this.newLocation.type === 'STOCK_MOBILE' && this.newLocation.matriculeCommercial) {
      payload.matriculeCommercial = this.newLocation.matriculeCommercial;
    }
    this.api.post('stock/entrepots', payload).subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);
        this.newLocation = { type: 'STOCK_CENTRAL', name: '', matriculeCommercial: '' };
        this.loadLocations();
        this.showToast('Localisation créée avec succès', 'success');
      },
      error: () => {
        this.saving.set(false);
        this.showToast('Erreur lors de la création', 'error');
      }
    });
  }

  initializeDefaults(): void {
    this.showToast('Initialisation non disponible via l\'API', 'error');
  }

  assignCommercial(locationId: string, commercialId: string): void {
    if (!commercialId) return;
    this.api.post(`stock/entrepots`, { type: 'STOCK_MOBILE', matriculeCommercial: commercialId }).subscribe({
      next: () => {
        this.loadLocations();
        this.showToast('Commercial assigné avec succès', 'success');
      },
      error: () => this.showToast('Erreur lors de l\'assignation', 'error')
    });
  }

  getTypeLabel(type: string): string {
    const found = this.locationTypes.find(t => t.value === type);
    return found ? found.label : type;
  }

  getTypeClass(type: string): string {
    const m: Record<string, string> = {
      STOCK_CENTRAL: 'badge-central',
      STOCK_BUFFER: 'badge-buffer',
      STOCK_MOBILE: 'badge-mobile'
    };
    return m[type] || 'badge-neutral';
  }

  get centralCount(): number { return this.locations().filter(l => l.type === 'STOCK_CENTRAL').length; }
  get bufferCount(): number { return this.locations().filter(l => l.type === 'STOCK_BUFFER').length; }
  get mobileCount(): number { return this.locations().filter(l => l.type === 'STOCK_MOBILE').length; }
  get magasinCount(): number { return 0; }

  private showToast(msg: string, type: 'success' | 'error'): void {
    this.toastMsg.set(msg);
    this.toastType.set(type);
    setTimeout(() => this.toastMsg.set(''), 4000);
  }
}
