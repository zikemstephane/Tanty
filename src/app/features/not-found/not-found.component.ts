import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="error-page">
      <div class="error-page__code">404</div>
      <h1 class="error-page__title">Page introuvable</h1>
      <p class="error-page__desc">La page que vous cherchez n'existe pas ou a été déplacée.</p>
      <a routerLink="/dashboard" class="btn btn-primary">
        <span class="material-icons">home</span> Retour au tableau de bord
      </a>
    </div>`,
  styles: [`
    @use '../../../styles/variables' as *;
    .error-page { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 80vh; text-align: center; gap: $spacing-4; }
    .error-page__code { font-size: 120px; font-weight: 900; color: $tanty-gray-200; line-height: 1; }
    .error-page__title { font-size: $font-size-3xl; font-weight: 700; color: $tanty-gray-800; }
    .error-page__desc { color: $tanty-gray-500; max-width: 400px; }
  `]
})
export class NotFoundComponent {}
