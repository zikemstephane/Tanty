import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (visible()) {
      <div class="overlay" (click)="cancel.emit()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal__header">
            <h3 class="modal__title">{{ title() }}</h3>
            <button class="btn btn-ghost btn-icon" (click)="cancel.emit()">
              <span class="material-icons">close</span>
            </button>
          </div>
          <div class="modal__body">
            <p>{{ message() }}</p>
          </div>
          <div class="modal__footer">
            <button class="btn btn-ghost" (click)="cancel.emit()">Annuler</button>
            <button class="btn" [class]="'btn-' + (danger() ? 'danger' : 'primary')" (click)="confirm.emit()">
              {{ confirmLabel() }}
            </button>
          </div>
        </div>
      </div>
    }`,
  styles: [`@use '../../../../../styles/variables' as *;`]
})
export class ConfirmDialogComponent {
  visible      = input(false);
  title        = input('Confirmation');
  message      = input('Êtes-vous sûr de vouloir effectuer cette action ?');
  confirmLabel = input('Confirmer');
  danger       = input(false);
  confirm      = output<void>();
  cancel       = output<void>();
}
