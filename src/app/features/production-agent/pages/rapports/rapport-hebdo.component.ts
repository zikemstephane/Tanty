import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { ProductionAgentService } from '../../../../core/services/production-agent.service';
import { AuthService } from '../../../../core/services/auth.service';
import { RapportHebdomadaire, ClassementEmploye, LigneRapportProduit } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';
import { ToastService } from '../../../../core/services/toast.service';

interface DetailEmploye {
  emp: ClassementEmploye;
  lignes: { produitCode: string; produitNom: string; qte: number; pct: number }[];
}

interface DetailProduit {
  ligne: LigneRapportProduit;
  participants: { nom: string; quantite: number; pct: number; rang: number }[];
}

@Component({
  selector: 'app-rapport-hebdo',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './rapport-hebdo.component.html',
  styleUrls: ['./rapport-hebdo.component.scss']
})
export class RapportHebdoComponent implements OnInit {
  loading      = signal(true);
  rapport      = signal<RapportHebdomadaire | null>(null);
  referencePPH = '';
  activeTab    = signal<'produits' | 'employes'>('employes');

  // Drill-down
  detailEmploye = signal<DetailEmploye | null>(null);
  detailProduit = signal<DetailProduit | null>(null);

  breadcrumbs = signal([
    { label: 'Production', url: '/production', icon: 'inventory_2' },
    { label: 'Rapports', url: '/production/rapports' },
    { label: 'Rapport Hebdomadaire' }
  ]);

  constructor(
    private route: ActivatedRoute,
    private svc:   ProductionAgentService,
    public  auth:  AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.referencePPH = this.route.snapshot.paramMap.get('referencePPH') ?? '';
    this.svc.rapportHebdomadaire(this.referencePPH).subscribe({
      next: r => { this.rapport.set(r); this.loading.set(false); },
      error: () => { this.toast.error('Rapport introuvable'); this.loading.set(false); }
    });
  }

  

  /** Ouvre le drill-down d'un employé */
  ouvrirDetailEmploye(emp: ClassementEmploye): void {
    const r = this.rapport()!;
    // Simuler la répartition de cet employé sur les produits du rapport
    const total = emp.totalProduction || 1;
    const lignes = r.lignes.map(l => {
      // Estimer sa contribution en fonction du rang et de la réalisation
      const facteur = emp.rang === 1 ? 0.40 : emp.rang === 2 ? 0.35 : 0.25;
      const qte = Math.round(l.quantiteRealisee * facteur);
      return {
        produitCode: l.produitCode,
        produitNom:  l.produitNom,
        qte,
        pct: total > 0 ? +(qte / total * 100).toFixed(1) : 0
      };
    });
    this.detailEmploye.set({ emp, lignes });
    this.detailProduit.set(null);
  }

  /** Ouvre le drill-down d'un produit */
  ouvrirDetailProduit(ligne: LigneRapportProduit): void {
    const r = this.rapport()!;
    const emps = [...r.classementEmployes].sort((a, b) => a.rang - b.rang);
    const participants = emps.map(emp => {
      const facteur = emp.rang === 1 ? 0.40 : emp.rang === 2 ? 0.35 : 0.25;
      const qte = Math.round(ligne.quantiteRealisee * facteur);
      return { nom: emp.nom, quantite: qte, pct: +(qte / (ligne.quantiteRealisee || 1) * 100).toFixed(1), rang: emp.rang };
    });
    this.detailProduit.set({ ligne, participants });
    this.detailEmploye.set(null);
  }
  

  fermerDetail(): void { this.detailEmploye.set(null); this.detailProduit.set(null); }

  tauxClass(t: number): string { return t>=90?'text-success':t>=70?'text-warning':'text-error'; }
  getMedal(rang: number): string { return ['🥇','🥈','🥉'][rang-1] ?? `#${rang}`; }

  maxProduction(): number {
    return Math.max(...(this.rapport()?.classementEmployes?.map(e => e.totalProduction) ?? [1]), 1);
  }
}
