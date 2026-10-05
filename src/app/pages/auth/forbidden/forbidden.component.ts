import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-forbidden',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="forbidden-container">
      <div class="forbidden-card">
        <div class="icon-wrapper">
          <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
            <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
          </svg>
        </div>
        <h1>Accès refusé</h1>
        <p class="message">{{ message }}</p>
        <p class="hint">Vous n'avez pas les permissions nécessaires pour effectuer cette action.</p>
        <button class="back-btn" (click)="goBack()">Retour</button>
      </div>
    </div>
  `,
  styles: [`
    .forbidden-container {
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #f5f5f5, #e0e0e0);
    }
    .forbidden-card {
      background: #fff;
      border-radius: 16px;
      padding: 48px 40px;
      max-width: 420px;
      text-align: center;
      box-shadow: 0 4px 24px rgba(0,0,0,0.08);
    }
    .icon-wrapper {
      color: #d32f2f;
      margin-bottom: 24px;
    }
    h1 {
      font-size: 1.75rem;
      font-weight: 700;
      color: #1a1a1a;
      margin: 0 0 12px;
    }
    .message {
      font-size: 1rem;
      color: #d32f2f;
      margin: 0 0 8px;
      font-weight: 500;
    }
    .hint {
      font-size: 0.875rem;
      color: #666;
      margin: 0 0 32px;
    }
    .back-btn {
      background: #C9A227;
      color: #fff;
      border: none;
      border-radius: 8px;
      padding: 12px 32px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }
    .back-btn:hover {
      background: #B8911E;
    }
  `]
})
export class ForbiddenComponent {
  message = 'Accès refusé';

  constructor(
    private readonly router: Router,
    private readonly auth: AuthService,
  ) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as { message?: string } | undefined;
    if (state?.message) {
      this.message = state.message;
    }
  }

  goBack(): void {
    if (this.auth.isAuthenticated()) {
      window.history.back();
    } else {
      this.router.navigate(['/auth/login']);
    }
  }
}
