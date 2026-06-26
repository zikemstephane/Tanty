import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="empty-state">
      <span class="material-icons empty-state__icon">{{ icon() }}</span>
      <h3 class="empty-state__title">{{ title() }}</h3>
      <p class="empty-state__desc">{{ description() }}</p>
      @if (actionLabel()) {
        <button class="btn btn-primary" (click)="action.emit()">
          @if (actionIcon()) { <span class="material-icons">{{ actionIcon() }}</span> }
          {{ actionLabel() }}
        </button>
      }
    </div>`,
  styles: [`
    @use '../../../../../styles/variables' as *;
    .empty-state {
      display: flex; flex-direction: column; align-items: center;
      justify-content: center; text-align: center; padding: $spacing-16 $spacing-8;
      &__icon { font-size: 64px; color: $tanty-gray-300; margin-bottom: $spacing-4; }
      &__title { font-size: $font-size-xl; font-weight: 600; color: $tanty-gray-700; margin-bottom: $spacing-2; }
      &__desc { color: $tanty-gray-500; margin-bottom: $spacing-6; max-width: 400px; }
    }
  `]
})
export class EmptyStateComponent {
  icon        = input('inbox');
  title       = input('Aucun élément');
  description = input('Il n\'y a rien à afficher pour le moment.');
  actionLabel = input('');
  actionIcon  = input('');
  action      = output<void>();
}
