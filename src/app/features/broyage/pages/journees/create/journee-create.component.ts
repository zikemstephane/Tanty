import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { PPH, Utilisateur, TYPE_POUDRE_LABELS, TypePoudre } from '../../../../../core/models';
import { AuthService } from '../../../../../core/services/auth.service';
import { BroyageService } from '../../../../../core/services/broyage.service';
import { PphService } from '../../../../../core/services/pph.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { UtilisateurService } from '../../../../../core/services/utilisateur.service';
import { BreadcrumbComponent } from '../../../../../shared/components/breadcrumb/breadcrumb.component';


@Component({
  selector: 'app-journee-create',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, BreadcrumbComponent],
  templateUrl: './journee-create.component.html',
  styleUrls: ['./journee-create.component.scss']
})
export class JourneeCreateComponent implements OnInit {
  form!:       FormGroup;
  loading    = signal(false);
  pphs       = signal<PPH[]>([]);
  machinistes = signal<Utilisateur[]>([]);
  typesPoudre = Object.entries(TYPE_POUDRE_LABELS).map(([v, l]) => ({ value: v as TypePoudre, label: l }));

  breadcrumbs = [
    { label: 'Broyage', url: '/broyage', icon: 'precision_manufacturing' },
    { label: 'Journées', url: '/broyage/journees' },
    { label: 'Ouvrir une journée' }
  ];

  constructor(
    private fb: FormBuilder,
    private svc: BroyageService,
    private pphSvc: PphService,
    private userSvc: UtilisateurService,
    private toast: ToastService,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      referencePPH: ['', Validators.required],
      date:         [new Date().toISOString().split('T')[0], Validators.required],
      assignations: this.fb.array([])
    });
    this.pphSvc.lister('EN_COURS').subscribe(p => {
      this.pphs.set(p);
      if (p.length) this.form.patchValue({ referencePPH: p[0].reference });
    });
    this.userSvc.getAll().subscribe(u => {
      this.machinistes.set(u.filter(x => ['CHEF_MACHINISTE','AGENT_PRODUCTION'].includes(x.role)));
    });
    this.ajouterAssignation();
  }

  get assignations(): FormArray { return this.form.get('assignations') as FormArray; }

  ajouterAssignation(): void {
    this.assignations.push(this.fb.group({
      machinisteMatricule: ['', Validators.required],
      typePoudre:          ['MAIS', Validators.required]
    }));
  }

  supprimerAssignation(i: number): void { if (this.assignations.length > 1) this.assignations.removeAt(i); }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const user = this.auth.currentUser()!;
    this.loading.set(true);
    this.svc.ouvrirJournee(this.form.value, user.nomComplet, user.matricule).subscribe({
      next: j => {
        this.toast.success(`Journée du ${j.date} ouverte — ${j.machinistes.length} machiniste(s) assigné(s)`);
        this.router.navigate(['/broyage/journees', j.id]);
      },
      error: e => { this.toast.error(e.message); this.loading.set(false); }
    });
  }

  getNomMachiniste(matricule: string): string {
    return this.machinistes().find(m => m.matricule === matricule)?.nomComplet ?? matricule;
  }
}
