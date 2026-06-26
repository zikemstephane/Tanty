import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BroyageService } from '../../../../core/services/broyage.service';
import { PphService } from '../../../../core/services/pph.service';
import { AuthService } from '../../../../core/services/auth.service';
import { JourneeProduction, PPH } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-broyage-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './broyage-dashboard.component.html',
  styleUrls: ['./broyage-dashboard.component.scss']
})
export class BroyageDashboardComponent implements OnInit {
  loading   = signal(true);
  journees  = signal<JourneeProduction[]>([]);
  activePPH = signal<PPH | null>(null);

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Broyage' }
  ];

  constructor(
    private broyageSvc: BroyageService,
    private pphSvc: PphService,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    this.pphSvc.lister('EN_COURS').subscribe(pphs => {
      if (pphs.length) {
        this.activePPH.set(pphs[0]);
        this.broyageSvc.listerParPPH(pphs[0].reference).subscribe(j => {
          this.journees.set(j);
          this.loading.set(false);
        });
      } else {
        this.loading.set(false);
      }
    });
  }

  get totalPoudre(): number {
    return this.journees().reduce((s, j) => s + (j.totalPoudreNetteKg ?? 0), 0);
  }

  get tauxMoyen(): number {
    const closed = this.journees().filter(j => j.tauxPerteGlobalPourcentage !== null);
    if (!closed.length) return 0;
    return +(closed.reduce((s, j) => s + j.tauxPerteGlobalPourcentage!, 0) / closed.length).toFixed(1);
  }

  statutClass(s: string): string { return s === 'CLOTUREE' ? 'badge--valide' : 'badge--warning'; }
}
