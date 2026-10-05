import { Component, OnInit, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockApiRepository, StockLocationDto } from '../../../infrastructure/repositories/stock-api.repository';
import { AuthService } from '../../../../../core/auth/auth.service';

/**
 * Cet écran a été écrit pour des magasins de détail — adresse, téléphone,
 * courriel, responsable. Le serveur ne connaît que des entrepôts : un code, un
 * libellé, un type (central, tampon, mobile), un propriétaire et un état actif.
 *
 * Les entrepôts sont donc listés et créés pour de vrai ; les coordonnées, elles,
 * n'ont nulle part où être conservées et restent vides. Modification et
 * désactivation n'ont pas non plus de route serveur : elles le disent plutôt
 * que de faire semblant.
 */
interface Magasin {
  id: string;
  type: string;
  typeLabel: string;
  name: string;
  description: string;
  managerId?: string;
  address?: string;
  phone?: string;
  email?: string;
  active: boolean;
  itemCount: number;
}

@Component({
  selector: 'app-magasin-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './magasin-list.component.html',
  styleUrls: ['./magasin-list.component.css']
})
export class MagasinListComponent implements OnInit {
  private repo = inject(StockApiRepository);
  private auth = inject(AuthService);

  magasins = signal<Magasin[]>([]);
  loading = signal(true);
  error = signal('');
  showForm = signal(false);
  editingId = signal<string | null>(null);
  saving = signal(false);
  toastMsg = signal('');
  toastType = signal<'success' | 'error'>('success');

  currentUser = computed(() => this.auth.getCurrentUser());
  userMatricule = computed(() => this.currentUser()?.matricule || '');

  form = {
    // Le type d'entrepôt est ce que le serveur attend en premier.
    type: 'CENTRAL',
    name: '',
    description: '',
    managerId: '',
    address: '',
    phone: '',
    email: ''
  };

  stats = computed(() => {
    const all = this.magasins();
    return {
      total: all.length,
      active: all.filter(m => m.active).length,
      inactive: all.filter(m => !m.active).length,
      totalItems: all.reduce((sum, m) => sum + (m.itemCount || 0), 0)
    };
  });

  ngOnInit(): void {
    this.loadMagasins();
  }

  loadMagasins(): void {
    this.loading.set(true);
    this.error.set('');
    this.repo.getLocations().subscribe({
      next: (data: StockLocationDto[]) => {
        this.magasins.set((data || []).map((e: StockLocationDto) => ({
          id: String(e.id),
          type: e.type ?? '',
          typeLabel: e.typeLabel ?? e.type ?? '',
          name: e.name ?? '',
          description: e.code ?? '',
          managerId: e.assignedUserId ?? undefined,
          address: undefined,
          phone: undefined,
          email: undefined,
          active: e.active ?? true,
          itemCount: 0,
        })));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur lors du chargement des magasins');
        this.loading.set(false);
        this.showToast('Erreur lors du chargement des magasins', 'error');
      }
    });
  }

  openCreateForm(): void {
    this.editingId.set(null);
    this.form = {
      type: 'CENTRAL',
      name: '',
      description: '',
      managerId: this.userMatricule(),
      address: '',
      phone: '',
      email: ''
    };
    this.showForm.set(true);
  }

  openEditForm(magasin: Magasin): void {
    this.editingId.set(magasin.id);
    this.form = {
      type: magasin.type || 'CENTRAL',
      name: magasin.name,
      description: magasin.description || '',
      managerId: magasin.managerId || '',
      address: magasin.address || '',
      phone: magasin.phone || '',
      email: magasin.email || ''
    };
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.editingId.set(null);
  }

  saveMagasin(): void {
    if (!this.form.name.trim()) {
      this.showToast('Le nom du magasin est requis', 'error');
      return;
    }
    this.saving.set(true);
    const editId = this.editingId();
    if (editId) {
      this.saving.set(false);
      this.showToast("Le serveur n'offre pas de modification d'entrepôt : "
        + 'créez-en un nouveau ou faites évoluer la route côté serveur.', 'error');
      return;
    }
    this.repo.createLocation({
      type: this.form.type || 'CENTRAL',
      name: this.form.name,
      code: this.form.description || undefined,
      managerMatricule: this.form.managerId || undefined,
    }).subscribe({
      next: () => {
        this.saving.set(false);
        this.closeForm();
        this.loadMagasins();
        this.showToast('Entrepôt créé. Adresse, téléphone et courriel ne sont pas conservés '
          + "par le serveur et n'ont pas été enregistrés.", 'success');
      },
      error: () => {
        this.saving.set(false);
        this.showToast('Erreur lors de la création', 'error');
      }
    });
  }

  toggleActive(_magasin: Magasin): void {
    this.showToast("Activer ou désactiver un entrepôt n'a pas de route serveur aujourd'hui.", 'error');
  }

  showDeactivateModal = signal(false);
  deactivateTarget = signal<Magasin | null>(null);

  openDeactivateModal(magasin: Magasin): void {
    this.deactivateTarget.set(magasin);
    this.showDeactivateModal.set(true);
  }

  closeDeactivateModal(): void {
    this.showDeactivateModal.set(false);
    this.deactivateTarget.set(null);
  }

  confirmDeactivate(): void {
    if (!this.deactivateTarget()) return;
    this.closeDeactivateModal();
    this.showToast("Désactiver un entrepôt n'a pas de route serveur aujourd'hui.", 'error');
  }

  private showToast(msg: string, type: 'success' | 'error'): void {
    this.toastMsg.set(msg);
    this.toastType.set(type);
    setTimeout(() => this.toastMsg.set(''), 3000);
  }
}
