import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BonCommandeService } from '../../../../core/services/bon-commande.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { BonDeCommande, StatutBC } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-bc-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent, EmptyStateComponent, ConfirmDialogComponent, PageLoaderComponent],
  templateUrl: './bc-list.component.html',
  styleUrls: ['./bc-list.component.scss']
})
export class BcListComponent implements OnInit {
  loading    = signal(true);
  bcs        = signal<BonDeCommande[]>([]);
  filterStatut = signal('');
  searchQuery  = signal('');
  showConfirm  = signal(false);
  pendingRef   = signal<string | null>(null);

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Bons de Commande' }
  ];

  statuts: { value: string; label: string }[] = [
    { value: '',                     label: 'Tous les statuts' },
    { value: 'BROUILLON',            label: 'Brouillon' },
    { value: 'EN_ATTENTE_VALIDATION',label: 'En attente' },
    { value: 'VALIDE',               label: 'Validé' },
    { value: 'CLOS',                 label: 'Clos' },
    { value: 'ANNULE',               label: 'Annulé' }
  ];

  constructor(
    private bcSvc: BonCommandeService,
    private toast: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.bcSvc.lister(this.filterStatut() || undefined).subscribe(bcs => {
      this.bcs.set(bcs);
      this.loading.set(false);
    });
  }

  get filteredBCs(): BonDeCommande[] {
    const q = this.searchQuery().toLowerCase();
    return this.bcs().filter(bc =>
      !q || bc.reference.toLowerCase().includes(q) || bc.createurNom.toLowerCase().includes(q)
    );
  }

  onFilterChange(): void { this.load(); }

  soumettre(ref: string): void {
    this.bcSvc.soumettre(ref).subscribe({
      next: () => { this.toast.success(`BC ${ref} soumis pour validation`); this.load(); },
      error: e  => this.toast.error(e.message)
    });
  }

  valider(ref: string, bc: BonDeCommande): void {
    const user = this.auth.currentUser()!;
    this.bcSvc.valider(ref, user.nomComplet, user.matricule).subscribe({
      next: (b) => { this.toast.success(`BC ${ref} validé — ${b.nombreSemaines} PPH(s) générés`); this.load(); },
      error: e  => this.toast.error(e.message)
    });
  }

  askDelete(ref: string): void { this.pendingRef.set(ref); this.showConfirm.set(true); }

  confirmDelete(): void {
    const ref = this.pendingRef();
    if (!ref) return;
    this.bcSvc.supprimer(ref).subscribe({
      next: () => { this.toast.success(`BC ${ref} supprimé`); this.load(); },
      error: e  => this.toast.error(e.message)
    });
    this.showConfirm.set(false);
  }

  statusClass(statut: StatutBC): string {
    const m: Record<StatutBC, string> = {
      BROUILLON: 'badge--brouillon', EN_ATTENTE_VALIDATION: 'badge--attente',
      VALIDE: 'badge--valide', CLOS: 'badge--clos', ANNULE: 'badge--annule'
    };
    return m[statut] ?? '';
  }

  canCreate  = () => this.auth.hasAnyRole('DIRECTEUR_GENERAL','GESTIONNAIRE_STOCK','CHEF_PRODUCTION');
  canSubmit  = (bc: BonDeCommande) => bc.statut === 'BROUILLON' && this.auth.hasAnyRole('GESTIONNAIRE_STOCK','CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  canValidate= (bc: BonDeCommande) => (bc.statut === 'BROUILLON' || bc.statut === 'EN_ATTENTE_VALIDATION') && this.auth.hasAnyRole('CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  canDelete  = (bc: BonDeCommande) => bc.statut === 'BROUILLON' && this.auth.hasAnyRole('GESTIONNAIRE_STOCK','CHEF_PRODUCTION','DIRECTEUR_GENERAL');
}
