import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PphService } from '../../../../core/services/pph.service';
import { PPH } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-pph-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent, EmptyStateComponent, PageLoaderComponent],
  templateUrl: './pph-list.component.html',
  styleUrls: ['./pph-list.component.scss']
})
export class PphListComponent implements OnInit {
  loading = signal(true);
  pphs    = signal<PPH[]>([]);
  filterStatut = signal('');

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Plans de Production (PPH)' }
  ];

  statuts = [
    { value: '', label: 'Tous' },
    { value: 'PLANIFIE', label: 'Planifié' },
    { value: 'EN_COURS', label: 'En cours' },
    { value: 'TERMINE',  label: 'Terminé' },
    { value: 'AJUSTE',   label: 'Ajusté' }
  ];

  constructor(private pphSvc: PphService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.pphSvc.lister(this.filterStatut() || undefined).subscribe(pphs => {
      this.pphs.set(pphs);
      this.loading.set(false);
    });
  }

  statusClass(statut: string): string {
    return { EN_COURS: 'badge--warning', TERMINE: 'badge--valide', PLANIFIE: 'badge--info', AJUSTE: 'badge--attente' }[statut] ?? 'badge--info';
  }

  tauxMoyen(pph: PPH): number {
    if (!pph.lignes.length) return 0;
    return Math.round(pph.lignes.reduce((s, l) => s + l.tauxRealisation, 0) / pph.lignes.length);
  }
}
