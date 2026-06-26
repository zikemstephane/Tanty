import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DosageService } from '../../../../core/services/dosage.service';
import { PphService } from '../../../../core/services/pph.service';
import { ToastService } from '../../../../core/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';
import { SessionDosage, StatistiqueDosage, PPH, FORMULES_DOSAGE, TypeMelange } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-dosage-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './dosage-dashboard.component.html',
  styleUrls: ['./dosage-dashboard.component.scss']
})
export class DosageDashboardComponent implements OnInit {
  loading       = signal(true);
  sessions      = signal<SessionDosage[]>([]);
  stats         = signal<StatistiqueDosage | null>(null);
  activePPH     = signal<PPH | null>(null);
  showForm      = signal(false);
  sessionForm!: FormGroup;
  formule       = signal<{ mais: number; soja: number; arachide: number } | null>(null);

  breadcrumbs = [{ label: 'Tableau de bord', url: '/dashboard', icon: 'home' }, { label: 'Dosage' }];

  constructor(
    private dosageSvc: DosageService,
    private pphSvc: PphService,
    private toast: ToastService,
    public auth: AuthService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.pphSvc.lister('EN_COURS').subscribe(pphs => {
      if (pphs.length) {
        this.activePPH.set(pphs[0]);
        this.dosageSvc.listerParPPH(pphs[0].reference).subscribe(s => {
          this.sessions.set(s);
          this.loading.set(false);
        });
      } else {
        this.loading.set(false);
      }
    });
    this.dosageSvc.statistiquesSemaine(new Date()).subscribe(s => this.stats.set(s));
  }

  buildForm(): void {
    this.sessionForm = this.fb.group({
      referencePPH: ['', Validators.required],
      date:         [new Date().toISOString().split('T')[0], Validators.required],
      typeMelange:  ['BOUILLIE', Validators.required],
      nombreFuts:   [1, [Validators.required, Validators.min(1)]],
      maisKg:       [0, [Validators.required, Validators.min(0)]],
      sojaKg:       [0, [Validators.required, Validators.min(0)]],
      arachideKg:   [0, [Validators.required, Validators.min(0)]]
    });
  }

  onTypeMelangeChange(): void {
    const type = this.sessionForm.get('typeMelange')?.value as TypeMelange;
    const futs = +this.sessionForm.get('nombreFuts')?.value || 1;
    const f = FORMULES_DOSAGE[type];
    this.formule.set(f);
    this.sessionForm.patchValue({
      maisKg:     f.mais * futs,
      sojaKg:     f.soja * futs,
      arachideKg: f.arachide * futs
    });
  }

  onFutsChange(): void { this.onTypeMelangeChange(); }

  onSubmit(): void {
    if (this.sessionForm.invalid) { this.sessionForm.markAllAsTouched(); return; }
    const user = this.auth.currentUser()!;
    const v    = this.sessionForm.value;
    this.dosageSvc.enregistrerSession({
      referencePPH: v.referencePPH, date: v.date, typeMelange: v.typeMelange,
      nombreFuts:   v.nombreFuts,
      composition:  { maisKg: v.maisKg, sojaKg: v.sojaKg, arachideKg: v.arachideKg }
    }, user.nomComplet, user.matricule).subscribe({
      next: s => {
        const msg = s.compositionConforme
          ? `Session enregistrée — ${s.nombreFuts} fût(s) · ${s.poidsNetTotalKg} kg`
          : `Session enregistrée avec avertissement : ${s.avertissementComposition}`;
        s.compositionConforme ? this.toast.success(msg) : this.toast.warning(msg);
        this.sessions.update(list => [s, ...list]);
        this.showForm.set(false);
        this.sessionForm.reset();
      },
      error: e => this.toast.error(e.message)
    });
  }

  getPPHRef(): void {
    if (this.activePPH()) {
      this.sessionForm.patchValue({ referencePPH: this.activePPH()!.reference });
    }
    this.onTypeMelangeChange();
  }
}
