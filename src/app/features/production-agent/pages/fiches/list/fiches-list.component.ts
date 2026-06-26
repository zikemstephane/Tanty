import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductionAgentService } from '../../../../../core/services/production-agent.service';
import { PphService } from '../../../../../core/services/pph.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { FicheProduit, PPH } from '../../../../../core/models';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';
import { EmptyStateComponent } from '../../../../../shared/components/empty-state/empty-state.component';
import { PageLoaderComponent } from '../../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-fiches-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent, EmptyStateComponent, PageLoaderComponent],
  template: `
<div class="page-container">
  <app-breadcrumb [items]="breadcrumbs" />
  <div class="page-header">
    <div>
      <h1 class="page-header__title">Fiches Info Produit</h1>
      <p class="page-header__subtitle">Saisies journalières de production par l'Agent</p>
    </div>
    <div class="page-header__actions">
      <select class="form-control" style="min-width:220px;" [ngModel]="selectedPPH()" (ngModelChange)="selectedPPH.set($event); charger()">
        <option value="">Tous les PPH</option>
        @for (p of pphs(); track p.id) {
          <option [value]="p.reference">{{ p.reference }} (S{{ p.numeroSemaine }})</option>
        }
      </select>
      <a routerLink="/production/fiches/nouveau" class="btn btn-primary">
        <span class="material-icons">add</span> Nouvelle fiche
      </a>
    </div>
  </div>

  <app-page-loader [loading]="loading()" />

  @if (!loading()) {
    @if (fiches().length === 0) {
      <app-empty-state icon="inventory_2" title="Aucune fiche" description="Créez votre première fiche de production journalière."
        actionLabel="Nouvelle fiche" actionIcon="add" (action)="null" />
    } @else {
      <div class="tanty-card" style="padding:0;overflow:hidden;">
        <table class="tanty-table">
          <thead>
            <tr><th>Date</th><th>PPH</th><th>Agent</th><th>Produits</th><th>Total unités</th><th>Actions</th></tr>
          </thead>
          <tbody>
            @for (f of fiches(); track f.id) {
              <tr>
                <td class="font-semibold">{{ f.date }}</td>
                <td class="text-sm text-muted">{{ f.referencePPH }}</td>
                <td class="text-sm">{{ f.agentNom }}</td>
                <td>
                  @for (l of f.lignes.slice(0,2); track l.produitId) {
                    <div class="text-xs">{{ l.produitCode }} — {{ l.quantiteUnites }} u</div>
                  }
                  @if (f.lignes.length > 2) {
                    <div class="text-xs text-muted">+{{ f.lignes.length - 2 }} autre(s)…</div>
                  }
                </td>
                <td class="font-bold text-success">{{ f.totalProduit | number }}</td>
                <td>
                  <a [routerLink]="['/production/fiches', f.id]" class="btn btn-ghost btn-sm">
                    <span class="material-icons">visibility</span>
                  </a>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    }
  }
</div>`,
  styles: [`@use '../../../../../styles/variables' as *; .text-success { color: #2E7D32; }`]
})
export class FichesListComponent implements OnInit {
  loading     = signal(true);
  fiches      = signal<FicheProduit[]>([]);
  pphs        = signal<PPH[]>([]);
  selectedPPH = signal('');

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Production', url: '/production' },
    { label: 'Fiches Info Produit' }
  ];

  constructor(
    private svc: ProductionAgentService,
    private pphSvc: PphService,
    public auth: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.pphSvc.lister().subscribe(p => {
      this.pphs.set(p);
      if (p.length) { this.selectedPPH.set(p[0].reference); }
      this.charger();
    });
  }

  charger(): void {
    if (!this.selectedPPH()) { this.loading.set(false); return; }
    this.loading.set(true);
    this.svc.listerFichesParPPH(this.selectedPPH()).subscribe(f => {
      this.fiches.set(f);
      this.loading.set(false);
    });
  }
}
