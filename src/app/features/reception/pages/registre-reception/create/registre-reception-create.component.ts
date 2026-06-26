import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { FicheProduit } from '../../../../../core/models';
import { AuthService } from '../../../../../core/services/auth.service';
import { ProductionAgentService } from '../../../../../core/services/production-agent.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../../shared/components/page-loader/page-loader.component';
import { ReceptionService } from '../../../services/reception.service';


@Component({
  selector: 'app-registre-reception-create',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './registre-reception-create.component.html',
  styleUrls: ['./registre-reception-create.component.scss']
})
export class RegistreReceptionCreateComponent implements OnInit {
  step         = signal<'select-fiche' | 'saisie' | 'recap'>('select-fiche');
  loading      = signal(true);
  saving       = signal(false);
  fiches       = signal<FicheProduit[]>([]);
  fichesFiltrees = signal<FicheProduit[]>([]);
  ficheSel     = signal<FicheProduit | null>(null);
  form!:         FormGroup;
  searchFiche  = signal('');

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Réception', url: '/reception' },
    { label: 'Nouvelle réception' }
  ];

  constructor(
    private fb:        FormBuilder,
    private svc:       ReceptionService,
    private prodSvc:   ProductionAgentService,
    private toast:     ToastService,
    public  auth:      AuthService,
    private router:    Router
  ) {}

  onSearch(event: Event) {
  const value = (event.target as HTMLInputElement).value;
  this.searchFiche.set(value);
  this.filtrerFiches();
}
  ngOnInit(): void {
    // Charger toutes les fiches de production disponibles
    // (dans la vraie app, on filtrerait par fiches sans registre de réception)
    const pphs = ['PPH-2026-001-S1', 'PPH-2026-001-S2'];
    const allFiches: FicheProduit[] = [];
    let done = 0;

    pphs.forEach(ref => {
      this.prodSvc.listerFichesParPPH(ref).subscribe(f => {
        allFiches.push(...f);
        done++;
        if (done === pphs.length) {
          // Filtrer celles sans registre de réception
          const disponibles = allFiches.filter(f => !this.svc.ficheADejaRegistre(f.id));
          this.fiches.set(allFiches);
          this.fichesFiltrees.set(disponibles);
          this.loading.set(false);
        }
      });
    });
  }

  filtrerFiches(): void {
    const q = this.searchFiche().toLowerCase();
    this.fichesFiltrees.set(
      this.fiches().filter(f =>
        !this.svc.ficheADejaRegistre(f.id) &&
        (!q || f.referencePPH.toLowerCase().includes(q) || f.date.includes(q) || f.agentNom.toLowerCase().includes(q))
      )
    );
  }

  selectionnerFiche(fiche: FicheProduit): void {
    this.ficheSel.set(fiche);
    this.buildForm(fiche);
    this.step.set('saisie');
  }

  buildForm(fiche: FicheProduit): void {
    const lignesArray = fiche.lignes.map(l =>
      this.fb.group({
        produitId:            [l.produitId],
        produitCode:          [l.produitCode],
        produitNom:           [l.produitNom],
        quantiteDeclareeCartons: [l.quantiteCartons],
        quantiteDeclareeUnites:  [l.quantiteUnites],
        quantiteRecueCartons: [l.quantiteCartons, [Validators.required, Validators.min(0)]],
        quantiteRecueUnites:  [l.quantiteUnites,  [Validators.required, Validators.min(0)]],
        observationGestionnaire: ['']
      })
    );

    this.form = this.fb.group({
      dateReception:  [new Date().toISOString().split('T')[0], Validators.required],
      heureReception: [new Date().toTimeString().slice(0, 5)],
      lignes:         this.fb.array(lignesArray),
      commentaireGestionnaire: ['']
    });
  }

  get lignes(): FormArray { return this.form.get('lignes') as FormArray; }

  calculerEcart(i: number): { cartons: number; unites: number; pct: number } {
    const l = this.lignes.at(i).value;
    const ecartU = (l.quantiteRecueUnites || 0) - (l.quantiteDeclareeUnites || 0);
    const ecartC = (l.quantiteRecueCartons || 0) - (l.quantiteDeclareeCartons || 0);
    const pct    = l.quantiteDeclareeUnites > 0 ? (ecartU / l.quantiteDeclareeUnites) * 100 : 0;
    return { cartons: ecartC, unites: ecartU, pct: +pct.toFixed(1) };
  }

  get totalDeclaree(): number { return this.lignes.controls.reduce((s, l) => s + (l.get('quantiteDeclareeUnites')?.value || 0), 0); }
  get totalRecue(): number    { return this.lignes.controls.reduce((s, l) => s + (l.get('quantiteRecueUnites')?.value || 0), 0); }
  get totalEcart(): number    { return this.totalRecue - this.totalDeclaree; }
  get conformite(): number    { return this.totalDeclaree > 0 ? +(this.totalRecue / this.totalDeclaree * 100).toFixed(1) : 100; }
  get nbEcarts(): number      { return this.lignes.controls.filter(l => (l.get('quantiteRecueUnites')?.value || 0) !== (l.get('quantiteDeclareeUnites')?.value || 0)).length; }

  validerEtPasser(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.step.set('recap');
  }

  enregistrer(): void {
    const user  = this.auth.currentUser()!;
    const fiche = this.ficheSel()!;
    const val   = this.form.value;

    this.saving.set(true);
    this.svc.creerRegistre(
      {
        ficheProductionId: fiche.id,
        dateReception:     val.dateReception,
        heureReception:    val.heureReception,
        lignes: val.lignes.map((l: any) => ({
          produitId:            l.produitId,
          quantiteRecueCartons: l.quantiteRecueCartons,
          quantiteRecueUnites:  l.quantiteRecueUnites,
          observationGestionnaire: l.observationGestionnaire
        })),
        commentaireGestionnaire: val.commentaireGestionnaire
      },
      fiche,
      user.nomComplet,
      user.matricule
    ).subscribe({
      next: r => {
        if (r.nbLignesEcart > 0) {
          this.toast.warning(`Réception enregistrée avec ${r.nbLignesEcart} écart(s). L'agent de production sera notifié.`);
        } else {
          this.toast.success(`Réception ${r.reference} enregistrée — 100% conforme ✓`);
        }
        this.router.navigate(['/reception', r.id]);
      },
      error: e => { this.toast.error(e.message); this.saving.set(false); }
    });
  }

  retourSaisie(): void { this.step.set('saisie'); }
  retourSelection(): void { this.step.set('select-fiche'); this.ficheSel.set(null); }

  ecartClass(ecart: { unites: number }): string {
    if (ecart.unites === 0) return 'ecart--ok';
    if (ecart.unites > 0)  return 'ecart--surplus';
    return 'ecart--deficit';
  }
}
