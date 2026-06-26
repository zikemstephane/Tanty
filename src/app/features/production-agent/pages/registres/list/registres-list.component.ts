import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductionAgentService } from '../../../../../core/services/production-agent.service';
import { PphService } from '../../../../../core/services/pph.service';
import { RegistreProduction, PPH } from '../../../../../core/models';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';
import { EmptyStateComponent } from '../../../../../shared/components/empty-state/empty-state.component';
import { PageLoaderComponent } from '../../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-registres-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent, EmptyStateComponent, PageLoaderComponent],
  template: `
<div class="page-container">
  <app-breadcrumb [items]="breadcrumbs" />
  <div class="page-header">
    <div>
      <h1 class="page-header__title">Registres Journaliers</h1>
      <p class="page-header__subtitle">Suivi de la production par employé</p>
    </div>
    <div class="page-header__actions">
      <select class="form-control" style="min-width:220px;" [ngModel]="selectedPPH()" (ngModelChange)="selectedPPH.set($event); charger()">
        <option value="">Sélectionner un PPH</option>
        @for (p of pphs(); track p.id) {
          <option [value]="p.reference">{{ p.reference }} (S{{ p.numeroSemaine }})</option>
        }
      </select>
      <a routerLink="/production/registres/nouveau" class="btn btn-primary">
        <span class="material-icons">add</span> Nouveau registre
      </a>
    </div>
  </div>

  <app-page-loader [loading]="loading()" />

  @if (!loading()) {
    @if (registres().length === 0) {
      <app-empty-state icon="people" title="Aucun registre" description="Créez un registre journalier pour suivre la production de chaque employé."
        actionLabel="Nouveau registre" actionIcon="add" (action)="null" />
    } @else {
      <div class="tanty-card" style="padding:0;overflow:hidden;">
        <table class="tanty-table">
          <thead>
            <tr><th>Date</th><th>PPH</th><th>Agent</th><th>Employés</th><th>Actions</th></tr>
          </thead>
          <tbody>
            @for (r of registres(); track r.id) {
              <tr>
                <td class="font-semibold">{{ r.date }}</td>
                <td class="text-sm text-muted">{{ r.referencePPH }}</td>
                <td class="text-sm">{{ r.agentNom }}</td>
                <td>
                  <span class="badge badge--info">{{ r.employes.length }} employé(s)</span>
                </td>
                <td>
                  <a [routerLink]="['/production/registres', r.id]" class="btn btn-ghost btn-sm">
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
  styles: [`@use '../../../../../../styles/variables' as *;`]
})
export class RegistresListComponent implements OnInit {
  loading     = signal(true);
  registres   = signal<RegistreProduction[]>([]);
  pphs        = signal<PPH[]>([]);
  selectedPPH = signal('');

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Production', url: '/production' },
    { label: 'Registres Journaliers' }
  ];

  constructor(private svc: ProductionAgentService, private pphSvc: PphService) {}

  ngOnInit(): void {
    this.pphSvc.lister().subscribe(p => {
      this.pphs.set(p);
      if (p.length) { this.selectedPPH.set(p[0].reference); this.charger(); }
      else this.loading.set(false);
    });
  }

  charger(): void {
    if (!this.selectedPPH()) { this.loading.set(false); return; }
    this.loading.set(true);
    this.svc.listerRegistresParPPH(this.selectedPPH()).subscribe(r => {
      this.registres.set(r);
      this.loading.set(false);
    });
  }
}
