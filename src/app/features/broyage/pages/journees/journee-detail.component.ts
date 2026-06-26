import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BroyageService } from '../../../../core/services/broyage.service';
import { UtilisateurService } from '../../../../core/services/utilisateur.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { JourneeProduction, ProductionMachiniste, TYPE_POUDRE_LABELS } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-journee-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, ConfirmDialogComponent, PageLoaderComponent],
  templateUrl: './journee-detail.component.html',
  styleUrls: ['./journee-detail.component.scss']
})
export class JourneeDetailComponent implements OnInit {
  loading          = signal(true);
  journee          = signal<JourneeProduction | null>(null);
  showResultatForm = signal(false);
  showCorrigerForm = signal(false);
  showConfirmClot  = signal(false);
  selectedMachId   = signal<number | null>(null);
  resultatForm!: FormGroup;
  corrigerForm!: FormGroup;
  readonly TYPE_POUDRE_LABELS = TYPE_POUDRE_LABELS;

  breadcrumbs = signal([
    { label: 'Broyage', url: '/broyage', icon: 'precision_manufacturing' },
    { label: 'Journées', url: '/broyage/journees' },
    { label: '…' }
  ]);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private svc: BroyageService,
    private toast: ToastService,
    public auth: AuthService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.buildForms();
    this.svc.consulter(id).subscribe({
      next: j => {
        this.journee.set(j);
        this.breadcrumbs.update(b => [...b.slice(0, 2), { label: `Journée ${j.date}` }]);
        this.loading.set(false);
      },
      error: () => { this.toast.error('Journée introuvable'); this.router.navigate(['/broyage']); }
    });
  }

  buildForms(): void {
    this.resultatForm = this.fb.group({
      productionMachinisteId: ['', Validators.required],
      quantiteDepart:         [0, [Validators.required, Validators.min(1)]],
      quantiteObtenue:        [0, [Validators.required, Validators.min(0)]]
    });
    this.corrigerForm = this.fb.group({
      productionMachinisteId:     ['', Validators.required],
      nouvelleQuantiteDepart:     [0, [Validators.required, Validators.min(1)]],
      nouvelleQuantiteObtenue:    [0, [Validators.required, Validators.min(0)]],
      motif:                      ['', Validators.required]
    });
  }

  openResultat(mach: ProductionMachiniste): void {
    this.resultatForm.patchValue({ productionMachinisteId: mach.id });
    this.showResultatForm.set(true);
  }

  openCorriger(mach: ProductionMachiniste): void {
    this.corrigerForm.patchValue({
      productionMachinisteId:  mach.id,
      nouvelleQuantiteDepart:  mach.quantiteDepart,
      nouvelleQuantiteObtenue: mach.quantiteObtenue
    });
    this.showCorrigerForm.set(true);
  }

  enregistrerResultat(): void {
    if (this.resultatForm.invalid) { this.resultatForm.markAllAsTouched(); return; }
    const id = this.journee()!.id;
    this.svc.enregistrerResultat(id, this.resultatForm.value).subscribe({
      next: j => {
        this.journee.set(j);
        this.toast.success('Résultat enregistré');
        this.showResultatForm.set(false);
        this.resultatForm.reset();
      },
      error: e => this.toast.error(e.message)
    });
  }

  corrigerResultat(): void {
    if (this.corrigerForm.invalid) { this.corrigerForm.markAllAsTouched(); return; }
    const id = this.journee()!.id;
    this.svc.corrigerResultat(id, this.corrigerForm.value).subscribe({
      next: j => {
        this.journee.set(j);
        this.toast.success('Résultat corrigé');
        this.showCorrigerForm.set(false);
      },
      error: e => this.toast.error(e.message)
    });
  }

  cloturer(): void {
    const id = this.journee()!.id;
    this.svc.cloturerJournee(id).subscribe({
      next: j => {
        this.journee.set(j);
        this.toast.success(`Journée clôturée — ${j.totalPoudreNetteKg?.toFixed(1)} kg poudre nette`);
        this.showConfirmClot.set(false);
      },
      error: e => this.toast.error(e.message)
    });
  }

  scoreClass(score: number | null): string {
    if (score === null) return '';
    if (score >= 97) return 'text-success';
    if (score >= 93) return 'text-warning';
    return 'text-error';
  }

  canSaisirResultat = () => this.journee()?.statut === 'OUVERTE' && this.auth.hasAnyRole('CHEF_MACHINISTE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL');
  canCloturer       = () => this.journee()?.statut === 'OUVERTE' && this.auth.hasAnyRole('CHEF_MACHINISTE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL');
}
