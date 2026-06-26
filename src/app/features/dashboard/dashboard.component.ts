import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { BonCommandeService } from '../../core/services/bon-commande.service';
import { PphService } from '../../core/services/pph.service';
import { BroyageService } from '../../core/services/broyage.service';
import { DosageService } from '../../core/services/dosage.service';
import { ProduitService } from '../../core/services/produit.service';
import { BreadcrumbComponent } from '../../shared/components/breadcrumb/breadcrumb.component';
import { BonDeCommande, PPH, StatutBC } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  loading = signal(true);
  bcs     = signal<BonDeCommande[]>([]);
  pphs    = signal<PPH[]>([]);
today = new Date();
  stats = signal({
    totalBC: 0,
    bcEnAttente: 0,
    bcValides: 0,
    pphEnCours: 0,
    totalProduits: 43,
    tauxRealisation: 0
  });

  recentBCs = computed(() => this.bcs().slice(0, 5));
  recentPPHs = computed(() => this.pphs().filter(p => p.statut === 'EN_COURS').slice(0, 4));

  breadcrumbs = [{ label: 'Accueil', icon: 'home' }, { label: 'Tableau de bord' }];

  constructor(
    public auth: AuthService,
    private bcSvc: BonCommandeService,
    private pphSvc: PphService,
    private produitSvc: ProduitService
  ) {}

  ngOnInit(): void {
    this.bcSvc.lister().subscribe(bcs => {
      this.bcs.set(bcs);
      const enAttente = bcs.filter(b => b.statut === 'EN_ATTENTE_VALIDATION').length;
      const valides   = bcs.filter(b => b.statut === 'VALIDE').length;
      this.stats.update(s => ({ ...s, totalBC: bcs.length, bcEnAttente: enAttente, bcValides: valides }));
    });
    this.pphSvc.lister().subscribe(pphs => {
      this.pphs.set(pphs);
      const enCours = pphs.filter(p => p.statut === 'EN_COURS').length;
      const totalCible  = pphs.reduce((s, p) => s + p.lignes.reduce((a, l) => a + l.quantiteCibleUnites, 0), 0);
      const totalReal   = pphs.reduce((s, p) => s + p.lignes.reduce((a, l) => a + l.quantiteRealiseeUnites, 0), 0);
      const taux = totalCible > 0 ? Math.round((totalReal / totalCible) * 100) : 0;
      this.stats.update(s => ({ ...s, pphEnCours: enCours, tauxRealisation: taux }));
      this.loading.set(false);
    });
  }

  getStatutClass(statut: StatutBC): string {
    const m: Record<StatutBC, string> = {
      BROUILLON: 'badge--brouillon', EN_ATTENTE_VALIDATION: 'badge--attente',
      VALIDE: 'badge--valide', CLOS: 'badge--clos', ANNULE: 'badge--annule'
    };
    return m[statut] ?? '';
  }

  getPPHStatusClass(statut: string): string {
    return { EN_COURS: 'badge--warning', TERMINE: 'badge--valide', PLANIFIE: 'badge--info', AJUSTE: 'badge--attente' }[statut] ?? '';
  }

  greetingMessage(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Bonjour';
    if (h < 18) return 'Bon après-midi';
    return 'Bonsoir';
  }
}
