import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { ROLE_HOMES, UserRole } from '../../../core/models/user.models';
import { extractApiError } from '../../../core/http/api-error-parser';

@Component({
  selector: 'app-two-fa',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './two-fa.component.html',
  styleUrl: './two-fa.component.css'
})
export class TwoFaComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);

  otpForm: FormGroup;
  loading = false;
  countdown = 60;
  canResend = false;
  errorMessage = '';
  matricule = sessionStorage.getItem('tanty_otp_matricule') || '';

  constructor() {
    this.otpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]]
    });
    this.startCountdown();
  }

  onSubmit() {
    if (this.otpForm.invalid) {
      this.otpForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    const otpCode = this.otpForm.value.otp as string;

    this.auth.verifyOtp(this.matricule, otpCode).subscribe({
      next: (res) => {
        this.loading = false;
        const destination = ROLE_HOMES[res.role as UserRole] ?? '/auth/login';
        this.router.navigate([destination]);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err?.error?.erreur ||
          err?.error?.message ||
          err?.message ||
          'Code OTP invalide ou expiré.';
      }
    });
  }

  startCountdown() {
    const interval = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        clearInterval(interval);
        this.canResend = true;
      }
    }, 1000);
  }

  resendOtp() {
    if (this.canResend && this.matricule) {
      this.canResend = false;
      this.countdown = 60;
      this.startCountdown();
      this.auth.requestOtp(this.matricule).subscribe({
        error: (e) => {
          this.errorMessage = extractApiError(e);
        }
      });
    }
  }

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }
}
