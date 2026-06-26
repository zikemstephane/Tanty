import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PphService } from '../../../../core/services/pph.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { PPH } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

interface RepartitionJour {
  jour: string;
  date: string;
  lignes: { produitCode: string; produitNom: string; kgPoudre: number; cartonsCible: number }[];
  totalKg: number;
}

@Component({
  selector: 'app-pph-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './pph-detail.component.html',
  styleUrls: ['./pph-detail.component.scss']
})
export class PphDetailComponent implements OnInit {
  loading      = signal(true);
  pph          = signal<PPH | null>(null);
  showAjuster  = signal(false);
  ajusterForm: FormGroup;
  activeTab    = signal<'produits' | 'repartition' | 'emballages' | 'matieres'>('produits');
  repartition  = signal<RepartitionJour[]>([]);
  jourSelectionne = signal<RepartitionJour | null>(null);

  breadcrumbs = signal([
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'PPH', url: '/pphs' }, { label: '...' }
  ]);

  constructor(
    private route: ActivatedRoute, private router: Router,
    private pphSvc: PphService, private toast: ToastService,
    public auth: AuthService, private fb: FormBuilder
  ) {
    this.ajusterForm = this.fb.group({
      lignePPHId:       ['', Validators.required],
      nouvelleQuantite: [1, [Validators.required, Validators.min(1)]],
      motif:            ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  getPctSem(pph: any, ligne: any): number {
  return pph?.lignes?.find((l: any) => l.produitCode === ligne.produitCode)
    ?.quantiteCibleCartons || 1;
  }
  
  ngOnInit(): void {
    const ref = this.route.snapshot.paramMap.get('reference')!;
    this.pphSvc.consulter(ref).subscribe({
      next: pph => {
        this.pph.set(pph);
        this.breadcrumbs.update(b => [...b.slice(0,2), { label: ref }]);
        this.calculerRepartition(pph);
        this.loading.set(false);
      },
      error: () => { this.toast.error('PPH introuvable'); this.router.navigate(['/pphs']); }
    });
  }

  /** Répartit les charges de poudre par jour ouvré de la semaine */
  calculerRepartition(pph: PPH): void {
    if (!pph.besoinsMatieresPremieres) return;
    const debut = new Date(pph.dateDebut);
    const jours: string[] = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi'];
    // Kg de poudre par jour (5 jours ouvrés) - par produit
    const repartition: RepartitionJour[] = jours.map((jour, i) => {
      const date = new Date(debut);
      date.setDate(debut.getDate() + i);
      const dateStr = date.toISOString().split('T')[0];
      // Répartir les cartons de chaque produit sur les 5 jours
      const lignes = pph.lignes.map(l => {
        const cartonsParJour = Math.round(l.quantiteCibleCartons / 5);
        // Estimer kg de poudre : ~2.2 kg par carton de sachets, ~8 kg pour seaux
        const kgParCarton = l.produitNom?.includes('seau') || l.produitNom?.includes('2L') || l.produitNom?.includes('5L') ? 8 : 2.2;
        return {
          produitCode:  l.produitCode,
          produitNom:   l.produitNom,
          kgPoudre:     +(cartonsParJour * kgParCarton).toFixed(1),
          cartonsCible: cartonsParJour
        };
      });
      return {
        jour,
        date: dateStr,
        lignes,
        totalKg: +lignes.reduce((s, l) => s + l.kgPoudre, 0).toFixed(1)
      };
    });
    this.repartition.set(repartition);
    if (repartition.length) this.jourSelectionne.set(repartition[0]);
  }

  totalKgSemaine(): number {
    return +this.repartition().reduce((s, j) => s + j.totalKg, 0).toFixed(1);
  }

  maxKg(): number {
    return Math.max(...this.repartition().map(j => j.totalKg), 1);
  }

  ajuster(): void {
    if (this.ajusterForm.invalid) { this.ajusterForm.markAllAsTouched(); return; }
    const ref = this.pph()!.reference;
    this.pphSvc.ajusterQuantite(ref, this.ajusterForm.value).subscribe({
      next: p => {
        this.pph.set(p);
        this.calculerRepartition(p);
        this.toast.success('Quantité ajustée avec succès');
        this.showAjuster.set(false);
        this.ajusterForm.reset();
      },
      error: e => this.toast.error(e.message)
    });
  }

  getEmballageEstime(ligne: any): { sachets: number; cartons: number } {
    const qteCartons = ligne.quantiteCibleCartons || 0;
    const nom = ligne.produitNom || '';
    const uniteParCarton = nom.includes('80') ? 80 : nom.includes('25') ? 25 : nom.includes('24') ? 24 : nom.includes('12') ? 12 : nom.includes('14') ? 14 : 25;
    return { cartons: qteCartons, sachets: qteCartons * uniteParCarton };
  }

  getTotalEmballages(): { cartons: number; sachets: number } {
    const pph = this.pph();
    if (!pph) return { cartons: 0, sachets: 0 };
    return pph.lignes.reduce((acc, l) => {
      const e = this.getEmballageEstime(l);
      return { cartons: acc.cartons + e.cartons, sachets: acc.sachets + e.sachets };
    }, { cartons: 0, sachets: 0 });
  }

  statusClass(s: string): string {
    return {EN_COURS:'badge--warning',TERMINE:'badge--valide',PLANIFIE:'badge--info',AJUSTE:'badge--attente'}[s]??'';
  }
  tauxClass(t: number): string { return t>=90?'text-success':t>=60?'text-warning':'text-error'; }

  // L'agent peut ajuster SEULEMENT si statut PLANIFIE
  canAjuster = () => {
    const s = this.pph()?.statut;
    return s === 'PLANIFIE' && this.auth.hasAnyRole('AGENT_PRODUCTION','CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  };

  isPlanifie = () => this.pph()?.statut === 'PLANIFIE';
}
