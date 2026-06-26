import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BroyageService } from '../../../../core/services/broyage.service';
import { PphService } from '../../../../core/services/pph.service';
import { Classement, PPH } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-classement',
  standalone: true,
  imports: [CommonModule, FormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './classement.component.html',
  styleUrls: ['./classement.component.scss']
})
export class ClassementComponent implements OnInit {
  loading     = signal(true);
  classement  = signal<Classement | null>(null);
  pphs        = signal<PPH[]>([]);
  selectedPPH = signal('');
  modeView    = signal<'hebdo' | 'mensuel'>('hebdo');
  annee       = signal(2026);
  mois        = signal(new Date().getMonth() + 1);

  breadcrumbs = [
    { label: 'Broyage', url: '/broyage', icon: 'precision_manufacturing' },
    { label: 'Classements' }
  ];

  constructor(private svc: BroyageService, private pphSvc: PphService) {}

  ngOnInit(): void {
    this.pphSvc.lister().subscribe(pphs => {
      this.pphs.set(pphs);
      if (pphs.length) {
        this.selectedPPH.set(pphs[0].reference);
        this.charger();
      } else {
        this.loading.set(false);
      }
    });
  }

  charger(): void {
    this.loading.set(true);
    if (this.modeView() === 'hebdo') {
      this.svc.classementHebdomadaire(this.selectedPPH()).subscribe(c => { this.classement.set(c); this.loading.set(false); });
    } else {
      this.svc.classementMensuel(this.annee(), this.mois()).subscribe(c => { this.classement.set(c); this.loading.set(false); });
    }
  }

  getMedal(rang: number): string {
    return ['🥇', '🥈', '🥉'][rang - 1] ?? `#${rang}`;
  }
}
