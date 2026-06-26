import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="error-page">
      <span class="material-icons error-page__icon">lock</span>
      <h1 class="error-page__title">Accès refusé</h1>
      <p class="error-page__desc">Vous ne disposez pas des droits nécessaires pour accéder à cette page.</p>
      <a routerLink="/dashboard" class="btn btn-primary"><span class="material-icons">home</span> Tableau de bord</a>
    </div>`,
  styles: [`
    @use '../../../styles/variables' as *;
    .error-page { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 80vh; text-align: center; gap: $spacing-4; }
    .error-page__icon { font-size: 80px; color: $color-error; opacity: 0.4; }
    .error-page__title { font-size: $font-size-3xl; font-weight: 700; color: $tanty-gray-800; }
    .error-page__desc  { color: $tanty-gray-500; max-width: 400px; }
  `]
})
export class UnauthorizedComponent {}
