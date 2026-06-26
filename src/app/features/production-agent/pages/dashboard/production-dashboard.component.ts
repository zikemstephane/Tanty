import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductionAgentService } from '../../../../core/services/production-agent.service';
import { PphService } from '../../../../core/services/pph.service';
import { AuthService } from '../../../../core/services/auth.service';
import { FicheProduit, PPH } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-production-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './production-dashboard.component.html',
  styleUrls: ['./production-dashboard.component.scss']
})
export class ProductionDashboardComponent implements OnInit {
  loading = signal(true);
  pphs    = signal<PPH[]>([]);
  fiches  = signal<FicheProduit[]>([]);
  activePPH = signal<PPH | null>(null);

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Production Agent' }
  ];

  constructor(
    private prodSvc: ProductionAgentService,
    private pphSvc: PphService,
    public auth: AuthService
  ) {}

  get totalProduits(): number {
  return this.fiches().reduce((sum, f) => sum + (f.totalProduit || 0), 0);
}
  ngOnInit(): void {
    this.pphSvc.lister('EN_COURS').subscribe(pphs => {
      this.pphs.set(pphs);
      if (pphs.length) {
        this.activePPH.set(pphs[0]);
        this.prodSvc.listerFichesParPPH(pphs[0].reference).subscribe(f => {
          this.fiches.set(f);
          this.loading.set(false);
        });
      } else {
        this.loading.set(false);
      }
    });
  }
}
