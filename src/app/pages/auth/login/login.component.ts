import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { ROLE_HOMES, UserRole } from '../../../core/models/user.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  private readonly route = inject(ActivatedRoute);

  loginForm: FormGroup;
  loading = false;
  showPassword = false;
  errorMessage = '';

  constructor() {
    this.loginForm = this.fb.group({
      matricule: ['', [Validators.required]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    const reason = this.route.snapshot.queryParamMap.get('reason');
    if (reason === 'forbidden') {
      this.errorMessage = "Vous n'avez pas les permissions nécessaires pour accéder à cette page.";
    } else if (reason === 'expired') {
      this.errorMessage = 'Votre session a expiré. Veuillez vous reconnecter.';
    } else if (reason === 'unauthorized') {
      this.errorMessage = 'Connexion requise. Veuillez vous authentifier.';
    } else if (reason === 'mobile-only') {
      this.errorMessage = "Le rôle Commercial est réservé à l'application mobile (Flutter). Veuillez utiliser l'app mobile TANTY Digital pour vous connecter.";
    } else if (reason === 'locked') {
      this.errorMessage = 'Votre compte est bloqué, contactez un administrateur.';
    }
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const matricule = (this.loginForm.value.matricule as string).trim().toUpperCase();
    const motDePasse = this.loginForm.value.motDePasse as string;

    this.auth.login({ matricule, motDePasse }).subscribe({
      next: (res) => {
        this.loading = false;
        const destination = ROLE_HOMES[res.role as UserRole] ?? '/auth/login';
        console.log("utilisateur connecter : ",this.auth.user())
        this.router.navigate([destination]);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err?.error?.erreur ||
          err?.error?.message ||
          err?.message ||
          'Matricule ou mot de passe incorrect.';
      }
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  goToForgotPassword() {
    this.router.navigate(['/auth/forgot-password']);
  }
}
