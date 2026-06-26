import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReceptionService } from '../../services/reception.service';
import { AuthService } from '../../../../core/services/auth.service';
import { RegistreReception, DashboardReception, STATUT_RECEPTION_LABELS } from '../../models/reception.models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-reception-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './reception-dashboard.component.html',
  styleUrls: ['./reception-dashboard.component.scss']
})
export class ReceptionDashboardComponent implements OnInit {
  loading   = signal(true);
  dashboard = signal<DashboardReception | null>(null);
  registres = signal<RegistreReception[]>([]);
  filterStatut = signal('');

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Réception Produits Finis' }
  ];

  statuts = [
    { value: '', label: 'Tous' },
    { value: 'EN_ATTENTE', label: 'En attente' },
    { value: 'RECUE', label: 'Reçue' },
    { value: 'ECART_SIGNALE', label: 'Écart signalé' },
    { value: 'CLOTUREE', label: 'Clôturée' }
  ];

  constructor(private svc: ReceptionService, public auth: AuthService) {}

  ngOnInit(): void {
    this.svc.getDashboard().subscribe(d => this.dashboard.set(d));
    this.chargerRegistres();
  }

  chargerRegistres(): void {
    this.loading.set(true);
    this.svc.lister(this.filterStatut() || undefined).subscribe(r => {
      this.registres.set(r);
      this.loading.set(false);
    });
  }

  statutClass(statut: string): string {
    return {
      EN_ATTENTE:    'badge--attente',
      RECUE:         'badge--valide',
      ECART_SIGNALE: 'badge--error',
      CLOTUREE:      'badge--clos'
    }[statut] ?? '';
  }

  conformiteClass(taux: number): string {
    if (taux >= 99) return 'text-success';
    if (taux >= 90) return 'text-warning';
    return 'text-error';
  }

  canCreateReception = () => this.auth.hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL');
  isAgent = () => this.auth.hasAnyRole('AGENT_PRODUCTION');
}
