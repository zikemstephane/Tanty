import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ProductionAgentService } from '../../../../../core/services/production-agent.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { FicheProduit } from '../../../../../core/models';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-fiche-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  template: `
<div class="page-container">
  <app-breadcrumb [items]="breadcrumbs()" />
  <app-page-loader [loading]="loading()" />

  @if (!loading() && fiche()) {
    <div class="page-header">
      <div>
        <h1 class="page-header__title">Fiche du {{ fiche()!.date }}</h1>
        <p class="page-header__subtitle">PPH : {{ fiche()!.referencePPH }} · Agent : {{ fiche()!.agentNom }}</p>
      </div>
      <div class="page-header__actions">
        <a routerLink="/production/fiches" class="btn btn-ghost"><span class="material-icons">arrow_back</span> Retour</a>
        <a routerLink="/production/fiches/nouveau" class="btn btn-primary"><span class="material-icons">add</span> Nouvelle fiche</a>
      </div>
    </div>

    <div class="grid grid--3" style="margin-bottom:1.5rem;">
      <div class="stat-card">
        <div class="stat-card__icon stat-card__icon--green"><span class="material-icons">inventory_2</span></div>
        <span class="stat-card__label">Total produits</span>
        <span class="stat-card__value">{{ fiche()!.totalProduit | number }}</span>
        <span class="stat-card__change stat-card__change--flat">unités déclarées</span>
      </div>
      <div class="stat-card">
        <div class="stat-card__icon stat-card__icon--yellow"><span class="material-icons">list</span></div>
        <span class="stat-card__label">Lignes produit</span>
        <span class="stat-card__value">{{ fiche()!.lignes.length }}</span>
      </div>
      <div class="stat-card">
        <div class="stat-card__icon stat-card__icon--blue"><span class="material-icons">event</span></div>
        <span class="stat-card__label">Date</span>
        <span class="stat-card__value" style="font-size:1.1rem;">{{ fiche()!.date }}</span>
      </div>
    </div>

    <div class="tanty-card" style="padding:0;overflow:hidden;">
      <div style="padding:1.5rem 1.5rem 0.75rem;">
        <h3 class="tanty-card__title">Détail des productions journalières</h3>
      </div>
      <table class="tanty-table">
        <thead><tr><th>Produit</th><th>Code</th><th>Cartons</th><th>Unités</th><th>Observation</th></tr></thead>
        <tbody>
          @for (l of fiche()!.lignes; track l.produitId) {
            <tr>
              <td class="font-medium">{{ l.produitNom }}</td>
              <td class="text-sm font-mono text-muted">{{ l.produitCode }}</td>
              <td class="font-semibold">{{ l.quantiteCartons }}</td>
              <td class="font-semibold text-success">{{ l.quantiteUnites | number }}</td>
              <td class="text-sm text-muted">{{ l.observation || '—' }}</td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  }
</div>`,
  styles: [`@use '../../../../../../styles/variables' as *; .text-success{color:$color-success;} .font-mono{font-family:monospace;}`]
})
export class FicheDetailComponent implements OnInit {
  loading = signal(true);
  fiche   = signal<FicheProduit | null>(null);
  breadcrumbs = signal([
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Fiches', url: '/production/fiches' },
    { label: '…' }
  ]);

  constructor(private route: ActivatedRoute, private router: Router, private svc: ProductionAgentService, private toast: ToastService) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.svc.consulterFiche(id).subscribe({
      next: f => {
        this.fiche.set(f);
        this.breadcrumbs.update(b => [...b.slice(0,2), { label: `Fiche #${id}` }]);
        this.loading.set(false);
      },
      error: () => { this.toast.error('Fiche introuvable'); this.router.navigate(['/production/fiches']); }
    });
  }
}
