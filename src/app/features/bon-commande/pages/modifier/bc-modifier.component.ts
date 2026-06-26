import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { BonCommandeService } from '../../../../core/services/bon-commande.service';
import { ProduitService } from '../../../../core/services/produit.service';
import { OtpService } from '../../../../core/services/otp.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { BonDeCommande, Produit } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

type EtapeModif = 'verification-otp' | 'modification';

@Component({
  selector: 'app-bc-modifier',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './bc-modifier.component.html',
  styleUrls: ['./bc-modifier.component.scss']
})
export class BcModifierComponent implements OnInit {
  loading   = signal(true);
  bc        = signal<BonDeCommande | null>(null);
  produits  = signal<Produit[]>([]);
  etape     = signal<EtapeModif>('verification-otp');
  saving    = signal(false);
  otpValid  = signal(false);

  otpForm!:    FormGroup;
  modifForm!:  FormGroup;

  breadcrumbs = signal([
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Bons de Commande', url: '/bons-de-commande' },
    { label: '…', url: '' },
    { label: 'Modifier (OTP requis)' }
  ]);

  constructor(
    private route:    ActivatedRoute,
    private router:   Router,
    private bcSvc:    BonCommandeService,
    private prodSvc:  ProduitService,
    private otpSvc:   OtpService,
    private toast:    ToastService,
    public  auth:     AuthService,
    private fb:       FormBuilder
  ) {}

  ngOnInit(): void {
    const ref = this.route.snapshot.paramMap.get('reference')!;

    this.otpForm = this.fb.group({
      codeOTP: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
      motif:   ['', [Validators.required, Validators.minLength(10)]]
    });

    this.modifForm = this.fb.group({
      dateDebutProduction: ['', Validators.required],
      dateFinProduction:   ['', Validators.required],
      lignes: this.fb.array([])
    });

    this.prodSvc.getActifs().subscribe(p => this.produits.set(p));

    this.bcSvc.consulter(ref).subscribe({
      next: bc => {
        if (bc.statut !== 'VALIDE') {
          this.toast.error('Seul un BC Validé peut être modifié avec OTP');
          this.router.navigate(['/bons-de-commande', ref]);
          return;
        }
        if (!this.auth.hasAnyRole('CHEF_PRODUCTION', 'DIRECTEUR_GENERAL')) {
          this.router.navigate(['/unauthorized']);
          return;
        }
        this.bc.set(bc);
        this.breadcrumbs.update(b => {
          const updated = [...b];
          updated[2] = { label: ref, url: `/bons-de-commande/${ref}` };
          return updated;
        });
        this.preRemplirFormulaire(bc);
        this.loading.set(false);
      },
      error: () => { this.toast.error('BC introuvable'); this.router.navigate(['/bons-de-commande']); }
    });
  }

  preRemplirFormulaire(bc: BonDeCommande): void {
    this.modifForm.patchValue({
      dateDebutProduction: bc.dateDebutProduction,
      dateFinProduction:   bc.dateFinProduction
    });
    const lignesArray = this.modifForm.get('lignes') as FormArray;
    lignesArray.clear();
    bc.lignes.forEach(l => {
      lignesArray.push(this.fb.group({
        produitId:       [l.produitId, Validators.required],
        produitCode:     [l.produitCode],
        produitNom:      [l.produitNom],
        categorie:       [l.categorie],
        conditionnement: [l.conditionnement],
        quantiteCartons: [l.quantiteCartons, [Validators.required, Validators.min(1)]]
      }));
    });
  }

  validerOTP(): void {
    if (this.otpForm.invalid) { this.otpForm.markAllAsTouched(); return; }
    const matricule = this.auth.currentUser()!.matricule;
    const code      = this.otpForm.get('codeOTP')!.value;

    const result = this.otpSvc.validerOTP({ matriculeChef: matricule, codeOTP: code });

    if (result.valide) {
      this.otpValid.set(true);
      this.etape.set('modification');
      this.toast.success('Code OTP valide — vous pouvez maintenant modifier le BC');
    } else {
      this.toast.error(result.raison ?? 'Code OTP invalide');
    }
  }

  get lignes(): FormArray { return this.modifForm.get('lignes') as FormArray; }

  ajouterLigne(): void {
    this.lignes.push(this.fb.group({
      produitId: ['', Validators.required], produitCode: [''], produitNom: [''],
      categorie: [''], conditionnement: [''], quantiteCartons: [1, [Validators.required, Validators.min(1)]]
    }));
  }

  supprimerLigne(i: number): void { if (this.lignes.length > 1) this.lignes.removeAt(i); }

  onProduitChange(i: number): void {
    const pid = this.lignes.at(i).get('produitId')!.value;
    const p   = this.produits().find(p => p.id === pid);
    if (p) this.lignes.at(i).patchValue({ produitCode: p.code, produitNom: p.nom, categorie: p.categorie, conditionnement: p.conditionnement });
  }

  sauvegarder(): void {
    if (this.modifForm.invalid) { this.modifForm.markAllAsTouched(); return; }
    const ref   = this.bc()!.reference;
    const motif = this.otpForm.get('motif')!.value;
    this.saving.set(true);
    this.bcSvc.modifierApresValidation(ref, { lignes: this.modifForm.value.lignes, codeOTP: this.otpForm.get('codeOTP')!.value }).subscribe({
      next: bc => {
        this.toast.success(`BC ${ref} modifié avec succès`);
        this.router.navigate(['/bons-de-commande', ref]);
      },
      error: e => { this.toast.error(e.message); this.saving.set(false); }
    });
  }

  getNomProduit(id: string): string { return this.produits().find(p => p.id === id)?.nom ?? id; }
}
