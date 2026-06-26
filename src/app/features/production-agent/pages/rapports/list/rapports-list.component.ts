import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PphService } from '../../../../../core/services/pph.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { PPH } from '../../../../../core/models';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-rapports-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent],
  template: `
<div class="page-container">
  <app-breadcrumb [items]="breadcrumbs" />
  <div class="page-header">
    <div>
      <h1 class="page-header__title">Rapports Hebdomadaires</h1>
      <p class="page-header__subtitle">Sélectionnez un plan de production pour consulter le rapport de la semaine</p>
    </div>
  </div>

  <div class="pphs-grid">
    @for (p of pphs(); track p.id) {
      <a class="pph-rapport-card" [routerLink]="['/production/rapports', p.reference]">
        <div class="pph-rapport-card__top">
          <div>
            <div class="font-bold" style="color:#2E7D32;">{{ p.reference }}</div>
            <div class="text-xs text-muted">Semaine {{ p.numeroSemaine }}</div>
          </div>
          <span class="badge"
                [class.badge--valide]="p.statut==='TERMINE'"
                [class.badge--warning]="p.statut==='EN_COURS'"
                [class.badge--info]="p.statut==='PLANIFIE'">
            {{ p.statutLibelle }}
          </span>
        </div>
        <div class="text-xs text-muted" style="margin-top:0.5rem;">
          <span class="material-icons" style="font-size:13px;vertical-align:middle;">schedule</span>
          {{ p.dateDebut }} → {{ p.dateFin }}
        </div>
        <div class="taux-row">
          @let taux = tauxMoyen(p);
          <div class="progress" style="flex:1;height:10px;">
            <div class="progress__bar" [style.width]="taux+'%'"
                 [style.background]="taux>=90?'#2E7D32':taux>=70?'#F57F17':'#C62828'"></div>
          </div>
          <span class="font-bold text-sm"
                [class.text-success]="taux>=90" [class.text-warning]="taux<90&&taux>=70" [class.text-error]="taux<70">
            {{ taux | number:'1.0-0' }}%
          </span>
        </div>
        <div class="pph-rapport-card__info">
          <span class="text-xs text-muted">{{ p.lignes.length }} produit(s) ·{{ getTotalCartons(p) }}cartons cibles
          </span>
        </div>
        <div class="pph-rapport-card__action">
          <span class="material-icons">assessment</span> Consulter le rapport
        </div>
      </a>
    }
    @if (pphs().length === 0) {
      <div class="tanty-card" style="text-align:center;padding:3rem;grid-column:1/-1;">
        <span class="material-icons" style="font-size:48px;color:#E0E0E0;">assessment</span>
        <p class="text-muted" style="margin-top:1rem;">Aucun plan de production disponible</p>
        <a routerLink="/pphs" class="btn btn-outline" style="margin-top:1rem;">Voir les PPH</a>
      </div>
    }
  </div>
</div>`,
  styles: [`
    @use '../../../../../../styles/variables' as *;
    .pphs-grid { display: grid; grid-template-columns: repeat(auto-fill,minmax(300px,1fr)); gap: $spacing-4; }
    .pph-rapport-card {
      background:$color-surface; border:1px solid $color-border; border-radius:$border-radius-lg;
      padding:$spacing-5; text-decoration:none; color:inherit;
      display:flex; flex-direction:column; gap:$spacing-3; cursor:pointer; transition:all $transition-fast;
      &:hover { border-color:$tanty-green; box-shadow:$shadow-md; transform:translateY(-2px); text-decoration:none; }
      &__top   { display:flex; justify-content:space-between; align-items:flex-start; }
      &__action { display:flex; align-items:center; gap:$spacing-1; color:$tanty-green;
        font-size:$font-size-sm; font-weight:600; .material-icons { font-size:18px; } }
      &__info { font-size:$font-size-xs; color:$tanty-gray-500; }
    }
    .taux-row { display:flex; align-items:center; gap:$spacing-2; }
    .text-success{color:#2E7D32;} .text-warning{color:#F57F17;} .text-error{color:#C62828;}
  `]
})
export class RapportsListComponent implements OnInit {
  pphs = signal<PPH[]>([]);
  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Production', url: '/production' },
    { label: 'Rapports Hebdomadaires' }
  ];

  getTotalCartons(p: any): number {
  return p.lignes.reduce((sum: number, l: any) => {
    return sum + (l.quantiteCibleCartons || 0);
  }, 0);
}

  constructor(private pphSvc: PphService, public auth: AuthService) {}
  ngOnInit(): void { this.pphSvc.lister().subscribe(p => this.pphs.set(p)); }
  tauxMoyen(p: PPH): number {
    if (!p.lignes.length) return 0;
    return Math.round(p.lignes.reduce((s, l) => s + l.tauxRealisation, 0) / p.lignes.length);
  }
}
