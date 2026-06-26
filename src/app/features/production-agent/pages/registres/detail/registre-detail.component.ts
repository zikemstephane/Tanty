import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ProductionAgentService } from '../../../../../core/services/production-agent.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { RegistreProduction } from '../../../../../core/models';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-registre-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  template: `
<div class="page-container">
  <app-breadcrumb [items]="breadcrumbs()" />
  <app-page-loader [loading]="loading()" />

  @if (!loading() && registre()) {
    <div class="page-header">
      <div>
        <h1 class="page-header__title">Registre du {{ registre()!.date }}</h1>
        <p class="page-header__subtitle">PPH : {{ registre()!.referencePPH }} · Agent : {{ registre()!.agentNom }}</p>
      </div>
      <div class="page-header__actions">
        <a routerLink="/production/registres" class="btn btn-ghost"><span class="material-icons">arrow_back</span> Retour</a>
      </div>
    </div>

    <div class="grid grid--3" style="margin-bottom:1.5rem;">
      <div class="stat-card">
        <div class="stat-card__icon stat-card__icon--green"><span class="material-icons">people</span></div>
        <span class="stat-card__label">Employés</span>
        <span class="stat-card__value">{{ registre()!.employes.length }}</span>
      </div>
      <div class="stat-card">
        <div class="stat-card__icon stat-card__icon--yellow"><span class="material-icons">inventory</span></div>
        <span class="stat-card__label">Total production</span>
        <span class="stat-card__value">{{ totalGeneral() | number }}</span>
      </div>
      <div class="stat-card">
        <div class="stat-card__icon stat-card__icon--blue"><span class="material-icons">event</span></div>
        <span class="stat-card__label">Date</span>
        <span class="stat-card__value" style="font-size:1.1rem;">{{ registre()!.date }}</span>
      </div>
    </div>

    <div class="employes-detail">
      @for (emp of registre()!.employes; track emp.nom) {
        <div class="emp-card">
          <div class="emp-card__header">
            <div class="emp-rank">{{ $index + 1 }}</div>
            <div class="emp-info">
              <div class="font-semibold">{{ emp.nom }}</div>
              @if (emp.observation) { <div class="text-xs text-muted">{{ emp.observation }}</div> }
            </div>
            <div class="emp-total">
              <span class="text-xs text-muted">Production totale</span>
              <span class="font-bold" style="font-size:1.25rem;color:#2E7D32;">{{ emp.totalPoints | number }}</span>
            </div>
          </div>
          <table class="tanty-table" style="margin-top:0.5rem;">
            <thead><tr><th>Produit</th><th>Code</th><th>Quantité</th><th>Unité</th></tr></thead>
            <tbody>
              @for (p of emp.produitsTravailles; track p.produitCode) {
                <tr>
                  <td class="font-medium text-sm">{{ p.produitNom }}</td>
                  <td class="text-xs font-mono text-muted">{{ p.produitCode }}</td>
                  <td class="font-bold">{{ p.quantiteRealise | number }}</td>
                  <td class="text-muted text-sm">{{ p.unite }}</td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      }
    </div>

    <!-- Classement interne -->
    <div class="tanty-card" style="margin-top:1rem;">
      <h3 class="tanty-card__title" style="margin-bottom:1rem;">
        <span class="material-icons">emoji_events</span> Classement du jour
      </h3>
      <div class="classement-day">
        @for (emp of sorted(); track emp.nom) {
          <div class="classement-row" [class.classement-row--top]="$index === 0">
            <span class="rang">{{ ['🥇','🥈','🥉'][$index] ?? '#' + ($index+1) }}</span>
            <span class="nom flex-1">{{ emp.nom }}</span>
            <span class="pts font-bold">{{ emp.totalPoints | number }}</span>
          </div>
        }
      </div>
    </div>
  }
</div>`,
  styles: [`
    @use '../../../../../../styles/variables' as *;
    .employes-detail { display: flex; flex-direction: column; gap: $spacing-3; }
    .emp-card { background: $color-surface; border: 1px solid $color-border; border-radius: $border-radius-lg; padding: $spacing-4; }
    .emp-card__header { display: flex; align-items: center; gap: $spacing-3; margin-bottom: $spacing-2; flex-wrap: wrap; }
    .emp-rank { width: 32px; height: 32px; border-radius: 50%; background: $tanty-green; color: $tanty-white; font-weight: 700; display: flex; align-items: center; justify-content: center; font-size: $font-size-sm; flex-shrink: 0; }
    .emp-info { flex: 1; }
    .emp-total { text-align: right; display: flex; flex-direction: column; }
    .classement-day { display: flex; flex-direction: column; gap: $spacing-2; }
    .classement-row { display: flex; align-items: center; gap: $spacing-3; padding: $spacing-2 $spacing-3; border-radius: $border-radius; background: $tanty-gray-50; &--top { background: rgba(#FFD600,0.1); } }
    .rang { font-size: 1.25rem; width: 32px; flex-shrink: 0; }
    .pts  { color: $tanty-green; font-size: $font-size-lg; }
    .font-mono { font-family: monospace; }
  `]
})
export class RegistreDetailComponent implements OnInit {
  loading  = signal(true);
  registre = signal<RegistreProduction | null>(null);
  breadcrumbs = signal([
    { label: 'Production', url: '/production', icon: 'inventory_2' },
    { label: 'Registres', url: '/production/registres' },
    { label: '…' }
  ]);

  constructor(private route: ActivatedRoute, private router: Router, private svc: ProductionAgentService, private toast: ToastService) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.svc.consulterRegistre(id).subscribe({
      next: r => {
        this.registre.set(r);
        this.breadcrumbs.update(b => [...b.slice(0,2), { label: `Registre #${id}` }]);
        this.loading.set(false);
      },
      error: () => { this.toast.error('Registre introuvable'); this.router.navigate(['/production/registres']); }
    });
  }

  totalGeneral(): number {
    return this.registre()?.employes.reduce((s, e) => s + e.totalPoints, 0) ?? 0;
  }

  sorted(): any[] {
    return [...(this.registre()?.employes ?? [])].sort((a, b) => b.totalPoints - a.totalPoints);
  }
}
