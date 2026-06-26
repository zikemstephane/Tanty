import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DosageService } from '../../../../core/services/dosage.service';
import { PphService } from '../../../../core/services/pph.service';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastService } from '../../../../core/services/toast.service';
import { SessionDosage, PPH, FORMULES_DOSAGE, TYPE_MELANGE_LABELS } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-dosage-registres',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './dosage-registres.component.html',
  styleUrls: ['./dosage-registres.component.scss']
})
export class DosageRegistresComponent implements OnInit {
  loading     = signal(true);
  sessions    = signal<SessionDosage[]>([]);
  pphs        = signal<PPH[]>([]);
  selectedPPH = signal('');
  filterType  = signal('');
  sessionDetail = signal<SessionDosage | null>(null);

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Dosage', url: '/dosage' },
    { label: 'Registres de Dosage' }
  ];

  TYPE_MELANGE_LABELS = TYPE_MELANGE_LABELS;
  FORMULES = FORMULES_DOSAGE;

  constructor(
    private svc:   DosageService,
    private pphSvc: PphService,
    public  auth:  AuthService,
    private toast: ToastService
  ) {}

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
    this.svc.listerParPPH(this.selectedPPH()).subscribe(s => {
      this.sessions.set(s);
      this.loading.set(false);
    });
  }

  get filteredSessions(): SessionDosage[] {
    return this.sessions().filter(s => !this.filterType() || s.typeMelange === this.filterType());
  }

  totalFuts(): number    { return this.filteredSessions.reduce((s, x) => s + x.nombreFuts, 0); }
  totalPoids(): number   { return this.filteredSessions.reduce((s, x) => s + x.poidsNetTotalKg, 0); }
  tauxConformite(): number {
    const all = this.filteredSessions;
    return all.length ? Math.round(all.filter(s => s.compositionConforme).length / all.length * 100) : 0;
  }

  ouvrirDetail(s: SessionDosage): void { this.sessionDetail.set(s); }

  compositionConforme(s: SessionDosage): { mais: boolean; soja: boolean; arachide: boolean } {
    const f = this.FORMULES[s.typeMelange];
    return {
      mais:     Math.abs(s.composition.maisKg - f.mais * s.nombreFuts) < 2,
      soja:     Math.abs(s.composition.sojaKg - f.soja * s.nombreFuts) < 2,
      arachide: Math.abs(s.composition.arachideKg - f.arachide * s.nombreFuts) < 2
    };
  }
}
