import { Component, OnInit, OnDestroy, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Utilisateur, BonDeCommande } from '../../../core/models';
import { AuthService } from '../../../core/services/auth.service';
import { BonCommandeService } from '../../../core/services/bon-commande.service';
import { OtpService, OTPRecord } from '../../../core/services/otp.service';
import { ToastService } from '../../../core/services/toast.service';
import { UtilisateurService } from '../../../core/services/utilisateur.service';
import { BreadcrumbComponent } from '../../../shared/components/breadcrumb/breadcrumb.component';


@Component({
  selector: 'app-otp-manager',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent],
  templateUrl: './otp-manager.component.html',
  styleUrls: ['./otp-manager.component.scss']
})
export class OtpManagerComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  loading       = signal(false);
  otpGenere     = signal<{ code: string; expiresAt: Date; matriculeCible: string; validite: number } | null>(null);
  chefs         = signal<Utilisateur[]>([]);
  bcsValides    = signal<BonDeCommande[]>([]);
  countdown     = signal(0);
  showHistorique = signal(false);
  private timerRef?: ReturnType<typeof setInterval>;

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Gestion des Autorisations OTP' }
  ];

  historique = computed(() => this.otpSvc.getHistorique());

  constructor(
    private fb: FormBuilder,
    private otpSvc: OtpService,
    private userSvc: UtilisateurService,
    private bcSvc: BonCommandeService,
    public auth: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      matriculeCible: ['', Validators.required],
      referenceBC:    [''],
      motif:          ['', [Validators.required, Validators.minLength(10)]]
    });

    // Charger les chefs de production
    this.userSvc.getByRole('CHEF_PRODUCTION').subscribe(c => this.chefs.set(c));

    // Charger les BC validés
    this.bcSvc.lister('VALIDE').subscribe(bcs => this.bcsValides.set(bcs));
  }


toggleHistorique() {
  this.showHistorique.update(v => !v);
}
  ngOnDestroy(): void {
    if (this.timerRef) clearInterval(this.timerRef);
  }

  generer(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const user = this.auth.currentUser()!;
    this.loading.set(true);
    this.otpSvc.generer(this.form.value, user.matricule, user.nomComplet).subscribe({
      next: resp => {
        this.otpGenere.set({
          code:           resp.codeOTP,
          expiresAt:      resp.expiresAt,
          matriculeCible: resp.matriculeCible,
          validite:       resp.validiteMinutes
        });
        this.loading.set(false);
        this.demarrerCompteur(resp.validiteMinutes * 60);
        this.toast.success(`OTP généré pour ${resp.matriculeCible}`);
      },
      error: e => { this.toast.error(e.message); this.loading.set(false); }
    });
  }

  private demarrerCompteur(secondes: number): void {
    if (this.timerRef) clearInterval(this.timerRef);
    this.countdown.set(secondes);
    this.timerRef = setInterval(() => {
      this.countdown.update(v => {
        if (v <= 1) { clearInterval(this.timerRef); this.otpGenere.set(null); return 0; }
        return v - 1;
      });
    }, 1000);
  }

  formatCountdown(sec: number): string {
    const m = Math.floor(sec / 60).toString().padStart(2, '0');
    const s = (sec % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  }

  copierCode(): void {
    const code = this.otpGenere()?.code;
    if (!code) return;
    navigator.clipboard.writeText(code).then(() => this.toast.info('Code copié dans le presse-papiers'));
  }

  reinitialiser(): void {
    if (this.timerRef) clearInterval(this.timerRef);
    this.otpGenere.set(null);
    this.countdown.set(0);
    this.form.reset();
  }

  getStatutClass(otp: OTPRecord): string {
    const s = this.otpSvc.getStatut(otp);
    return { actif: 'badge--valide', consomme: 'badge--clos', expire: 'badge--annule' }[s] ?? '';
  }

  getStatutLabel(otp: OTPRecord): string {
    const s = this.otpSvc.getStatut(otp);
    return { actif: 'Actif', consomme: 'Consommé', expire: 'Expiré' }[s] ?? '';
  }

  getNomChef(matricule: string): string {
    return this.chefs().find(c => c.matricule === matricule)?.nomComplet ?? matricule;
  }
}
