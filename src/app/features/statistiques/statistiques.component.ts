import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { BonCommandeService } from '../../core/services/bon-commande.service';
import { PphService } from '../../core/services/pph.service';
import { BroyageService } from '../../core/services/broyage.service';
import { DosageService } from '../../core/services/dosage.service';
import { ProductionAgentService } from '../../core/services/production-agent.service';
import { ReceptionService } from '../reception/services/reception.service';
import { BreadcrumbComponent } from '../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-statistiques',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './statistiques.component.html',
  styleUrls: ['./statistiques.component.scss']
})
export class StatistiquesComponent implements OnInit {
  loading = signal(true);

  stats = signal({
    // BC
    totalBC: 0, bcBrouillon: 0, bcEnAttente: 0, bcValides: 0, bcClos: 0,
    // PPH
    totalPPH: 0, pphEnCours: 0, pphTermines: 0,
    tauxRealisationMoyen: 0,
    // Production
    totalFiches: 0, totalProduction: 0,
    // Broyage
    totalJournees: 0, totalPoudreNette: 0, tauxPerteMoyen: 0,
    // Dosage
    totalFuts: 0, totalPoidsKg: 0,
    // Reception
    totalReceptions: 0, tauxConformite: 0, totalEcarts: 0
  });

  // Pour les graphiques SVG
  bcRepartition = signal<{ label: string; value: number; color: string; pct: number }[]>([]);
  pphTaux       = signal<{ ref: string; taux: number; statut: string }[]>([]);
  evolutionProd = signal<{ label: string; value: number }[]>([]);

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Statistiques' }
  ];

  constructor(
    public auth: AuthService,
    private bcSvc: BonCommandeService,
    private pphSvc: PphService,
    private broyageSvc: BroyageService,
    private dosageSvc: DosageService,
    private prodSvc: ProductionAgentService,
    private recepSvc: ReceptionService
  ) {}

  ngOnInit(): void {
    let done = 0;
    const total = 5;
    const check = () => { done++; if (done >= total) this.loading.set(false); };

    this.bcSvc.lister().subscribe(bcs => {
      const s = this.stats();
      const brouillon = bcs.filter(b => b.statut === 'BROUILLON').length;
      const enAttente = bcs.filter(b => b.statut === 'EN_ATTENTE_VALIDATION').length;
      const valides   = bcs.filter(b => b.statut === 'VALIDE').length;
      const clos      = bcs.filter(b => b.statut === 'CLOS').length;
      this.stats.update(s => ({ ...s, totalBC: bcs.length, bcBrouillon: brouillon, bcEnAttente: enAttente, bcValides: valides, bcClos: clos }));
      const total = bcs.length || 1;
      this.bcRepartition.set([
        { label: 'Brouillon',  value: brouillon, color: '#78909C', pct: Math.round(brouillon / total * 100) },
        { label: 'En attente', value: enAttente, color: '#F57F17', pct: Math.round(enAttente / total * 100) },
        { label: 'Validé',     value: valides,   color: '#2E7D32', pct: Math.round(valides / total * 100) },
        { label: 'Clos',       value: clos,      color: '#455A64', pct: Math.round(clos / total * 100) }
      ]);
      check();
    });

    this.pphSvc.lister().subscribe(pphs => {
      const enCours  = pphs.filter(p => p.statut === 'EN_COURS').length;
      const termines = pphs.filter(p => p.statut === 'TERMINE').length;
      const allLignes = pphs.flatMap(p => p.lignes);
      const tauxMoyen = allLignes.length ? Math.round(allLignes.reduce((s, l) => s + l.tauxRealisation, 0) / allLignes.length) : 0;
      this.stats.update(s => ({ ...s, totalPPH: pphs.length, pphEnCours: enCours, pphTermines: termines, tauxRealisationMoyen: tauxMoyen }));
      this.pphTaux.set(pphs.slice(0, 6).map(p => ({
        ref: p.reference,
        taux: p.lignes.length ? Math.round(p.lignes.reduce((s, l) => s + l.tauxRealisation, 0) / p.lignes.length) : 0,
        statut: p.statut
      })));
      check();
    });

    this.broyageSvc.listerParPPH('PPH-2026-001-S1').subscribe(journees => {
      const totPoudre   = journees.reduce((s, j) => s + (j.totalPoudreNetteKg ?? 0), 0);
      const closed      = journees.filter(j => j.tauxPerteGlobalPourcentage !== null);
      const tauxPerte   = closed.length ? +(closed.reduce((s, j) => s + j.tauxPerteGlobalPourcentage!, 0) / closed.length).toFixed(1) : 0;
      this.stats.update(s => ({ ...s, totalJournees: journees.length, totalPoudreNette: totPoudre, tauxPerteMoyen: tauxPerte }));
      check();
    });

    this.dosageSvc.statistiquesSemaine(new Date()).subscribe(stats => {
      this.stats.update(s => ({ ...s, totalFuts: stats.totalFuts, totalPoidsKg: stats.totalPoidsKg }));
      check();
    });

    this.recepSvc.getDashboard().subscribe(d => {
      const allReg = [d.totalFichesEnAttente, d.totalFichesRecues, d.totalEcartsSignales];
      const total  = allReg.reduce((s, v) => s + v, 0) || 1;
      this.stats.update(s => ({
        ...s,
        totalReceptions: total,
        tauxConformite: Math.round(d.totalFichesRecues / total * 100),
        totalEcarts: d.totalEcartsSignales
      }));
      this.evolutionProd.set([
        { label: 'Lun', value: 320 }, { label: 'Mar', value: 480 }, { label: 'Mer', value: 410 },
        { label: 'Jeu', value: 560 }, { label: 'Ven', value: 490 }, { label: 'Sam', value: 380 }
      ]);
      check();
    });
  }

  maxProd(): number { return Math.max(...this.evolutionProd().map(e => e.value), 1); }

  // Construire donut SVG path
  donutSegments(): { path: string; color: string; label: string; pct: number }[] {
    const data = this.bcRepartition().filter(d => d.value > 0);
    const total = data.reduce((s, d) => s + d.value, 0);
    if (!total) return [];
    const segments: { path: string; color: string; label: string; pct: number }[] = [];
    let startAngle = -Math.PI / 2;
    const cx = 100, cy = 100, r = 75, innerR = 42;
    data.forEach(d => {
      const angle = (d.value / total) * 2 * Math.PI;
      const endAngle = startAngle + angle;
      const x1 = cx + r * Math.cos(startAngle), y1 = cy + r * Math.sin(startAngle);
      const x2 = cx + r * Math.cos(endAngle),   y2 = cy + r * Math.sin(endAngle);
      const xi1 = cx + innerR * Math.cos(endAngle),   yi1 = cy + innerR * Math.sin(endAngle);
      const xi2 = cx + innerR * Math.cos(startAngle), yi2 = cy + innerR * Math.sin(startAngle);
      const large = angle > Math.PI ? 1 : 0;
      const path = `M${x1},${y1} A${r},${r} 0 ${large},1 ${x2},${y2} L${xi1},${yi1} A${innerR},${innerR} 0 ${large},0 ${xi2},${yi2} Z`;
      segments.push({ path, color: d.color, label: d.label, pct: d.pct });
      startAngle = endAngle;
    });
    return segments;
  }
}
