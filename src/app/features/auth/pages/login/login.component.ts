import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastService } from '../../../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: FormGroup;
  loading = signal(false);
  showPassword = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {
    this.form = this.fb.group({
      matricule: ['TAN2026001', [Validators.required, Validators.minLength(5)]],
      motDePasse: ['Admin1234!', [Validators.required, Validators.minLength(8)]]
    });
  }

  togglePassword(): void {
    this.showPassword.update(v => !v);
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true);
    this.errorMessage.set(null);
    this.auth.loginAndStore(this.form.value).subscribe({
      next: (user) => {
        this.toast.success(`Bienvenue, ${user.nomComplet} !`);
        this.router.navigate(['/dashboard']);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set(err.message || 'Identifiants incorrects');
        this.loading.set(false);
      }
    });
  }

  get matricule() { return this.form.get('matricule')!; }
  get motDePasse() { return this.form.get('motDePasse')!; }
}
