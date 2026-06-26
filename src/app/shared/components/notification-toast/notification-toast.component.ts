import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-notification-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-toast.component.html',
  styleUrls: ['./notification-toast.component.scss']
})
export class NotificationToastComponent {
  constructor(public toastSvc: ToastService) {}

  iconFor(type: string): string {
    return { success: 'check_circle', error: 'error', warning: 'warning', info: 'info' }[type] ?? 'info';
  }
}
