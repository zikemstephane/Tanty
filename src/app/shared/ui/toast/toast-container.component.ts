import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService, ToastType } from './toast.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      @for (toast of toastService.toasts(); track toast.id) {
        <div
          class="toast toast-{{ toast.type }}"
          role="alert"
          (click)="toastService.dismiss(toast.id)"
        >
          <span class="toast-icon">{{ iconFor(toast.type) }}</span>
          <span class="toast-msg">{{ toast.message }}</span>
          <span class="toast-close">&times;</span>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      top: 22px;
      right: 22px;
      z-index: 2000;
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-width: 380px;
    }

    .toast {
      display: flex;
      align-items: center;
      gap: 11px;
      padding: 14px 20px;
      border-radius: var(--r-md);
      font-size: 13px;
      font-weight: 600;
      box-shadow: var(--sh-lg);
      cursor: pointer;
      animation: fadeInUp 0.3s ease;
      transition: opacity 0.3s, transform 0.3s;
    }

    .toast:hover {
      opacity: 0.92;
    }

    .toast-success {
      background: var(--s-gh);
      color: var(--s);
      border: 1.5px solid var(--s);
    }

    .toast-error {
      background: var(--r-gh);
      color: var(--r);
      border: 1.5px solid var(--r);
    }

    .toast-info {
      background: var(--b-gh);
      color: var(--b);
      border: 1.5px solid var(--b);
    }

    .toast-warning {
      background: var(--o-gh);
      color: var(--o);
      border: 1.5px solid var(--o);
    }

    .toast-icon {
      font-size: 18px;
      flex-shrink: 0;
      line-height: 1;
    }

    .toast-msg {
      flex: 1;
      line-height: 1.4;
    }

    .toast-close {
      font-size: 18px;
      opacity: 0.5;
      flex-shrink: 0;
    }

    @keyframes fadeInUp {
      from { opacity: 0; transform: translateY(14px); }
      to { opacity: 1; transform: translateY(0); }
    }
  `],
})
export class ToastContainerComponent {
  readonly toastService = inject(ToastService);

  iconFor(type: ToastType): string {
    switch (type) {
      case 'success': return '✓';
      case 'error': return '✕';
      case 'warning': return '⚠';
      case 'info': return 'ℹ';
    }
  }
}
