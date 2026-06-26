import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  sidebarCollapsed = input(false);
  toggleSidebar    = output<void>();

  constructor(public auth: AuthService) {}

  onLogout(): void {
    this.auth.logout();
  }

  getRoleColor(): string {
    const role = this.auth.currentRole();
    const map: Record<string, string> = {
      DIRECTEUR_GENERAL:  '#C62828',
      GESTIONNAIRE_STOCK: '#F57F17',
      CHEF_PRODUCTION:    '#2E7D32',
      AGENT_PRODUCTION:   '#1565C0',
      CHEF_MACHINISTE:    '#6A1B9A',
      AGENT_DOSEUR:       '#00695C'
    };
    return role ? (map[role] ?? '#616161') : '#616161';
  }

  getInitials(): string {
    const name = this.auth.nomComplet();
    if (!name) return 'U';
    const parts = name.split(' ');
    return parts.length >= 2
      ? parts[0][0].toUpperCase() + parts[1][0].toUpperCase()
      : name[0].toUpperCase();
  }
}
