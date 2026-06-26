import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ProductionAgentService } from '../../../../../core/services/production-agent.service';
import { PphService } from '../../../../../core/services/pph.service';
import { ProduitService } from '../../../../../core/services/produit.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { PPH, Produit } from '../../../../../core/models';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-fiche-create',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent],
  templateUrl: './fiche-create.component.html',
  styleUrls: ['./fiche-create.component.scss']
})
export class FicheCreateComponent implements OnInit {
  form!: FormGroup;
  loading  = signal(false);
  pphs     = signal<PPH[]>([]);
  produits = signal<Produit[]>([]);

  breadcrumbs = [
    { label: 'Production', url: '/production', icon: 'inventory_2' },
    { label: 'Fiches Produit', url: '/production/fiches' },
    { label: 'Nouvelle fiche' }
  ];

  constructor(
    private fb: FormBuilder,
    private prodSvc: ProductionAgentService,
    private pphSvc: PphService,
    private produitSvc: ProduitService,
    private toast: ToastService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      referencePPH: ['', Validators.required],
      date:         [new Date().toISOString().split('T')[0], Validators.required],
      lignes:       this.fb.array([])
    });
    this.pphSvc.lister('EN_COURS').subscribe(p => this.pphs.set(p));
    this.produitSvc.getActifs().subscribe(p => {
      this.produits.set(p);
      this.ajouterLigne();
    });
  }

  get lignes(): FormArray { return this.form.get('lignes') as FormArray; }

  ajouterLigne(): void {
    this.lignes.push(this.fb.group({
      produitId:       ['', Validators.required],
      quantiteCartons: [0, [Validators.required, Validators.min(0)]],
      quantiteUnites:  [0, [Validators.required, Validators.min(0)]],
      observation:     ['']
    }));
  }

  get totalQuantiteUnites(): number {
  return this.lignes.controls.reduce((sum, l) => {
    return sum + (+l.get('quantiteUnites')?.value || 0);
  }, 0);
}
  supprimerLigne(i: number): void { if (this.lignes.length > 1) this.lignes.removeAt(i); }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const user = this.auth.currentUser()!;
    this.loading.set(true);
    this.prodSvc.enregistrerFiche(this.form.value, user.nomComplet, user.matricule).subscribe({
      next: f => { this.toast.success(`Fiche enregistrée — ${f.totalProduit} unités`); this.router.navigate(['/production/fiches', f.id]); },
      error: e => { this.toast.error(e.message); this.loading.set(false); }
    });
  }

  getNomProduit(id: string): string { return this.produits().find(p => p.id === id)?.nom ?? id; }
}
