import { Injectable, signal } from '@angular/core';
import { ToastMessage } from '../models';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly _toasts = signal<ToastMessage[]>([]);
  readonly toasts = this._toasts.asReadonly();

  success(message: string, duration = 4000): void {
    this.add({ type: 'success', message, duration });
  }

  error(message: string, duration = 6000): void {
    this.add({ type: 'error', message, duration });
  }

  warning(message: string, duration = 5000): void {
    this.add({ type: 'warning', message, duration });
  }

  info(message: string, duration = 4000): void {
    this.add({ type: 'info', message, duration });
  }

  remove(id: string): void {
    this._toasts.update(list => list.filter(t => t.id !== id));
  }

  private add(partial: Omit<ToastMessage, 'id'>): void {
    const id = Math.random().toString(36).slice(2);
    const toast: ToastMessage = { id, ...partial };
    this._toasts.update(list => [...list, toast]);
    if (toast.duration) {
      setTimeout(() => this.remove(id), toast.duration);
    }
  }
}
