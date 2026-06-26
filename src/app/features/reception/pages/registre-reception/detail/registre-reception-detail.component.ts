import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../../core/services/auth.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';
import { ConfirmDialogComponent } from '../../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { PageLoaderComponent } from '../../../../../shared/components/page-loader/page-loader.component';
import { RegistreReception } from '../../../models/reception.models';
import { ReceptionService } from '../../../services/reception.service';


@Component({
  selector: 'app-registre-reception-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent, ConfirmDialogComponent, PageLoaderComponent],
  templateUrl: './registre-reception-detail.component.html',
  styleUrls: ['./registre-reception-detail.component.scss']
})
export class RegistreReceptionDetailComponent implements OnInit {
  loading          = signal(true);
  saving           = signal(false);
  registre         = signal<RegistreReception | null>(null);
  showJustifForm   = signal(false);
  showClotureConfirm = signal(false);
  justifForm!: FormGroup;

  breadcrumbs = signal([
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Réception', url: '/reception' },
    { label: '…' }
  ]);

  constructor(
    private route:  ActivatedRoute,
    private router: Router,
    private svc:    ReceptionService,
    private toast:  ToastService,
    public  auth:   AuthService,
    private fb:     FormBuilder
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.justifForm = this.fb.group({
      justificationAgent: ['', [Validators.required, Validators.minLength(20)]]
    });
    this.charger(id);
  }

  charger(id: number): void {
    this.svc.consulter(id).subscribe({
      next: r => {
        this.registre.set(r);
        this.breadcrumbs.update(b => [...b.slice(0, 2), { label: r.reference }]);
        this.loading.set(false);
      },
      error: () => { this.toast.error('Registre introuvable'); this.router.navigate(['/reception']); }
    });
  }

  soumettreJustification(): void {
    if (this.justifForm.invalid) { this.justifForm.markAllAsTouched(); return; }
    this.saving.set(true);
    this.svc.soumettreJustification({
      registreId:         this.registre()!.id,
      justificationAgent: this.justifForm.get('justificationAgent')!.value
    }).subscribe({
      next: r => {
        this.registre.set(r);
        this.toast.success('Justification soumise — le registre est maintenant clôturé');
        this.showJustifForm.set(false);
        this.saving.set(false);
      },
      error: e => { this.toast.error(e.message); this.saving.set(false); }
    });
  }

  cloturer(): void {
    this.svc.cloturer(this.registre()!.id).subscribe({
      next: r => {
        this.registre.set(r);
        this.toast.success('Registre clôturé');
        this.showClotureConfirm.set(false);
      },
      error: e => this.toast.error(e.message)
    });
  }

  // ── Helpers ───────────────────────────────────────────────────────────────
  statutClass(statut: string): string {
    return { EN_ATTENTE: 'badge--attente', RECUE: 'badge--valide', ECART_SIGNALE: 'badge--annule', CLOTUREE: 'badge--clos' }[statut] ?? '';
  }

  ligneStatutClass(statut: string): string {
    return { CONFORME: 'badge--valide', ECART_POSITIF: 'badge--attente', ECART_NEGATIF: 'badge--annule', MANQUANT: 'badge--annule' }[statut] ?? '';
  }

  ligneStatutLabel(statut: string): string {
    return { CONFORME: 'Conforme', ECART_POSITIF: 'Surplus', ECART_NEGATIF: 'Déficit', MANQUANT: 'Manquant' }[statut] ?? statut;
  }

  conformiteClass(taux: number): string {
    if (taux >= 99) return 'text-success';
    if (taux >= 90) return 'text-warning';
    return 'text-error';
  }

  // Est-ce que l'agent connecté est l'agent de cette fiche ?
  isAgentConcerne(): boolean {
    return this.auth.matricule() === this.registre()?.agentProductionMatricule;
  }

  // L'agent peut soumettre une justification si : écart signalé + pas encore justifié + c'est bien lui
  canJustifier(): boolean {
    const r = this.registre();
    return !!r && r.statut === 'ECART_SIGNALE' && !r.justificationAgent &&
      (this.isAgentConcerne() || this.auth.hasAnyRole('DIRECTEUR_GENERAL'));
  }

  // Le gestionnaire peut clôturer manuellement une réception conforme
  canCloturer(): boolean {
    const r = this.registre();
    return !!r && (r.statut === 'RECUE') && this.auth.hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL');
  }
}
