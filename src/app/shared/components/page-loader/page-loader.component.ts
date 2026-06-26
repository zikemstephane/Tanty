import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-loader',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (loading()) {
      <div class="page-loader">
        <div class="spinner"></div>
        @if (message()) { <p class="page-loader__msg">{{ message() }}</p> }
      </div>
    }`,
  styles: [`
    @use '../../../../../styles/variables' as *;
    .page-loader {
      display: flex; flex-direction: column; align-items: center;
      justify-content: center; padding: $spacing-16;
      &__msg { margin-top: $spacing-4; color: $tanty-gray-600; font-size: $font-size-sm; }
    }
  `]
})
export class PageLoaderComponent {
  loading = input(true);
  message = input('Chargement…');
}
