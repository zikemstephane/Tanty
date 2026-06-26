import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastService } from '../../../../core/services/toast.service';
import { Role, ROLE_LABELS } from '../../../../core/models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  form: FormGroup;
  loading  = signal(false);
  success  = signal<string | null>(null);
  errorMsg = signal<string | null>(null);
  showPwd  = signal(false);

  roles: { value: Role; label: string }[] = Object.entries(ROLE_LABELS).map(
    ([value, label]) => ({ value: value as Role, label })
  );

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {
    this.form = this.fb.group({
      nom:                     ['', [Validators.required, Validators.minLength(3)]],
      prenom:                  ['', [Validators.required, Validators.minLength(2)]],
      email:                   ['', [Validators.required, Validators.email]],
      motDePasse:              ['', [Validators.required, Validators.minLength(8)]],
      confirmationMotDePasse:  ['', Validators.required],
      role:                    ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const v = this.form.value;
    if (v.motDePasse !== v.confirmationMotDePasse) {
      this.errorMsg.set('Les mots de passe ne correspondent pas');
      return;
    }
    this.loading.set(true);
    this.errorMsg.set(null);
    this.auth.register(v).subscribe({
      next: (res) => {
        this.success.set(`Compte créé ! Matricule : ${res.matricule}`);
        this.toast.success(res.message);
        this.loading.set(false);
        setTimeout(() => this.router.navigate(['/auth/login']), 2500);
      },
      error: (err) => {
        this.errorMsg.set(err.message);
        this.loading.set(false);
      }
    });
  }
  toggleShowPwd() { 
this.showPwd.update(v => !v); 
  }

  get nom()   { return this.form.get('nom')!; }
  get prenom(){ return this.form.get('prenom')!; }
  get email() { return this.form.get('email')!; }
  get mdp()   { return this.form.get('motDePasse')!; }
  get confirm(){ return this.form.get('confirmationMotDePasse')!; }
  get role()  { return this.form.get('role')!; }
}
