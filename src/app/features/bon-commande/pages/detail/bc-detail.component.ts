import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BonCommandeService } from '../../../../core/services/bon-commande.service';
import { PphService } from '../../../../core/services/pph.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { BonDeCommande, PPH, StatutBC } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-bc-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, ConfirmDialogComponent, PageLoaderComponent],
  templateUrl: './bc-detail.component.html',
  styleUrls: ['./bc-detail.component.scss']
})
export class BcDetailComponent implements OnInit {
  loading   = signal(true);
  bc        = signal<BonDeCommande | null>(null);
  pphs      = signal<PPH[]>([]);
  showConfirmDelete = signal(false);
  showOTPModal      = signal(false);
  otpForm: FormGroup;

  breadcrumbs = signal([
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Bons de Commande', url: '/bons-de-commande' },
    { label: '…' }
  ]);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bcSvc: BonCommandeService,
    private pphSvc: PphService,
    private toast: ToastService,
    public auth: AuthService,
    private fb: FormBuilder
  ) {
    this.otpForm = this.fb.group({ codeOTP: ['', [Validators.required, Validators.minLength(4)]] });
  }

  ngOnInit(): void {
    const ref = this.route.snapshot.paramMap.get('reference')!;
    this.bcSvc.consulter(ref).subscribe({
      next: bc => {
        this.bc.set(bc);
        this.breadcrumbs.update(b => [...b.slice(0,2), { label: ref }]);
        this.loading.set(false);
        this.pphSvc.listerParBC(ref).subscribe(p => this.pphs.set(p));
      },
      error: () => { this.toast.error('BC introuvable'); this.router.navigate(['/bons-de-commande']); }
    });
  }

  soumettre(): void {
    const ref = this.bc()!.reference;
    this.bcSvc.soumettre(ref).subscribe({
      next: bc => { this.bc.set(bc); this.toast.success(`BC ${ref} soumis pour validation`); },
      error: e  => this.toast.error(e.message)
    });
  }

  valider(): void {
    const user = this.auth.currentUser()!;
    const ref  = this.bc()!.reference;
    this.bcSvc.valider(ref, user.nomComplet, user.matricule).subscribe({
      next: bc => { this.bc.set(bc); this.toast.success(`BC ${ref} validé — ${bc.nombreSemaines} PPH(s) créés`); },
      error: e  => this.toast.error(e.message)
    });
  }

  supprimer(): void {
    this.bcSvc.supprimer(this.bc()!.reference).subscribe({
      next: () => { this.toast.success('BC supprimé'); this.router.navigate(['/bons-de-commande']); },
      error: e  => this.toast.error(e.message)
    });
    this.showConfirmDelete.set(false);
  }

  statusClass(statut: StatutBC): string {
    const m: Record<StatutBC, string> = {
      BROUILLON: 'badge--brouillon', EN_ATTENTE_VALIDATION: 'badge--attente',
      VALIDE: 'badge--valide', CLOS: 'badge--clos', ANNULE: 'badge--annule'
    };
    return m[statut] ?? '';
  }

  pphStatusClass(statut: string): string {
    return { EN_COURS: 'badge--warning', TERMINE: 'badge--valide', PLANIFIE: 'badge--info', AJUSTE: 'badge--attente' }[statut] ?? '';
  }

  canSubmit   = () => this.bc()?.statut === 'BROUILLON' && this.auth.hasAnyRole('GESTIONNAIRE_STOCK','CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  canValidate = () => (this.bc()?.statut === 'BROUILLON' || this.bc()?.statut === 'EN_ATTENTE_VALIDATION') && this.auth.hasAnyRole('CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  canDelete   = () => this.bc()?.statut === 'BROUILLON' && this.auth.hasAnyRole('GESTIONNAIRE_STOCK','CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  canEdit     = () => (this.bc()?.statut === 'BROUILLON') && this.auth.hasAnyRole('GESTIONNAIRE_STOCK','CHEF_PRODUCTION','DIRECTEUR_GENERAL');
  canEditOTP  = () => this.bc()?.statut === 'VALIDE' && this.auth.hasAnyRole('CHEF_PRODUCTION','DIRECTEUR_GENERAL');
}
