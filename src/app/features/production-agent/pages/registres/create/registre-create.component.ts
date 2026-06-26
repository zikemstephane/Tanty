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
  selector: 'app-registre-create',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent],
  templateUrl: './registre-create.component.html',
  styleUrls: ['./registre-create.component.scss']
})
export class RegistreCreateComponent implements OnInit {
  form!: FormGroup;
  loading  = signal(false);
  pphs     = signal<PPH[]>([]);
  produits = signal<Produit[]>([]);

  breadcrumbs = [
    { label: 'Production', url: '/production', icon: 'inventory_2' },
    { label: 'Registres', url: '/production/registres' },
    { label: 'Nouveau registre' }
  ];

  constructor(
    private fb: FormBuilder,
    private svc: ProductionAgentService,
    private pphSvc: PphService,
    private produitSvc: ProduitService,
    private toast: ToastService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      referencePPH: ['', Validators.required],
      date: [new Date().toISOString().split('T')[0], Validators.required],
      employes: this.fb.array([])
    });
    this.pphSvc.lister().subscribe(p => this.pphs.set(p));
    this.produitSvc.getActifs().subscribe(p => { this.produits.set(p); this.ajouterEmploye(); });
  }

  get employes(): FormArray { return this.form.get('employes') as FormArray; }

  ajouterEmploye(): void {
    this.employes.push(this.fb.group({
      nom:          ['', Validators.required],
      produitsTravailles: this.fb.array([this.nouvelleLigneEmploye()])
    }));
  }

  supprimerEmploye(i: number): void { if (this.employes.length > 1) this.employes.removeAt(i); }

  getProduits(empIndex: number): FormArray {
    return this.employes.at(empIndex).get('produitsTravailles') as FormArray;
  }

  nouvelleLigneEmploye(): FormGroup {
    return this.fb.group({
      produitCode:     ['', Validators.required],
      produitNom:      [''],
      quantiteRealise: [0, [Validators.required, Validators.min(0)]],
      unite:           ['sachets']
    });
  }

  ajouterProduitEmploye(empIndex: number): void {
    this.getProduits(empIndex).push(this.nouvelleLigneEmploye());
  }

  supprimerProduitEmploye(empI: number, prodI: number): void {
    const arr = this.getProduits(empI);
    if (arr.length > 1) arr.removeAt(prodI);
  }

  onProduitChange(empI: number, prodI: number): void {
    const code = this.getProduits(empI).at(prodI).get('produitCode')?.value;
    const p    = this.produits().find(p => p.code === code);
    if (p) this.getProduits(empI).at(prodI).patchValue({ produitNom: p.nom });
  }

  totalEmploye(empIndex: number): number {
    return this.getProduits(empIndex).controls.reduce((s, c) => s + (+c.get('quantiteRealise')?.value || 0), 0);
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const user = this.auth.currentUser()!;
    const val  = this.form.value;
    const employes = val.employes.map((e: any) => ({
      nom: e.nom,
      produitsTravailles: e.produitsTravailles.map((p: any) => ({
        produitCode: p.produitCode, produitNom: p.produitNom,
        quantiteRealise: p.quantiteRealise, unite: p.unite
      })),
      totalPoints: e.produitsTravailles.reduce((s: number, p: any) => s + (+p.quantiteRealise || 0), 0),
      observation: null
    }));
    this.loading.set(true);
    this.svc.enregistrerRegistre({ referencePPH: val.referencePPH, date: val.date, employes }, user.nomComplet, user.matricule).subscribe({
      next: r => { this.toast.success(`Registre enregistré — ${r.employes.length} employé(s)`); this.router.navigate(['/production/registres', r.id]); },
      error: e => { this.toast.error(e.message); this.loading.set(false); }
    });
  }
}
