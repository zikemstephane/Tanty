import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { BonCommandeService } from '../../../../core/services/bon-commande.service';
import { ProduitService } from '../../../../core/services/produit.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Produit } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-bc-create',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent],
  templateUrl: './bc-create.component.html',
  styleUrls: ['./bc-create.component.scss']
})
export class BcCreateComponent implements OnInit {
  form!: FormGroup;
  loading  = signal(false);
  produits = signal<Produit[]>([]);
  produitsFiltres = signal<Produit[]>([]);

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Bons de Commande', url: '/bons-de-commande' },
    { label: 'Nouveau BC' }
  ];

  constructor(
    private fb: FormBuilder,
    private bcSvc: BonCommandeService,
    private produitSvc: ProduitService,
    private toast: ToastService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      dateDebutProduction: ['', Validators.required],
      dateFinProduction:   ['', Validators.required],
      lignes: this.fb.array([])
    });
    this.produitSvc.getActifs().subscribe(p => {
      this.produits.set(p);
      this.produitsFiltres.set(p);
    });
    this.ajouterLigne();
  }

  get lignes(): FormArray { return this.form.get('lignes') as FormArray; }

  ajouterLigne(): void {
    this.lignes.push(this.fb.group({
      produitId:       ['', Validators.required],
      produitCode:     [''],
      produitNom:      [''],
      categorie:       [''],
      conditionnement: [''],
      quantiteCartons: [1, [Validators.required, Validators.min(1)]]
    }));
  }

  supprimerLigne(i: number): void {
    if (this.lignes.length > 1) this.lignes.removeAt(i);
  }

  onProduitChange(i: number): void {
    const pid = this.lignes.at(i).get('produitId')!.value;
    const p   = this.produits().find(p => p.id === pid);
    if (p) {
      this.lignes.at(i).patchValue({
        produitCode: p.code, produitNom: p.nom,
        categorie: p.categorie, conditionnement: p.conditionnement
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const user = this.auth.currentUser()!;
    this.loading.set(true);
    const val = this.form.value;
    this.bcSvc.creer(val, user.nomComplet, user.matricule).subscribe({
      next: bc => {
        this.toast.success(`BC ${bc.reference} créé avec succès`);
        this.router.navigate(['/bons-de-commande', bc.reference]);
      },
      error: e => { this.toast.error(e.message); this.loading.set(false); }
    });
  }

  getNomProduit(id: string): string {
    return this.produits().find(p => p.id === id)?.nom ?? id;
  }
}
