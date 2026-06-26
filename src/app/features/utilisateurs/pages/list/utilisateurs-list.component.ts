import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UtilisateurService } from '../../../../core/services/utilisateur.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Utilisateur, ROLE_LABELS, Role } from '../../../../core/models';
import { BreadcrumbComponent } from '../../../../shared/components/breadcrumb/breadcrumb.component';
import { PageLoaderComponent } from '../../../../shared/components/page-loader/page-loader.component';

@Component({
  selector: 'app-utilisateurs-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, BreadcrumbComponent, PageLoaderComponent],
  templateUrl: './utilisateurs-list.component.html',
  styleUrls: ['./utilisateurs-list.component.scss']
})
export class UtilisateursListComponent implements OnInit {
  loading     = signal(true);
  users       = signal<Utilisateur[]>([]);
  filterRole  = signal('');
  searchQuery = signal('');

  breadcrumbs = [
    { label: 'Tableau de bord', url: '/dashboard', icon: 'home' },
    { label: 'Utilisateurs' }
  ];

  roles: { value: string; label: string }[] = [
    { value: '', label: 'Tous les rôles' },
    ...Object.entries(ROLE_LABELS).map(([v, l]) => ({ value: v, label: l }))
  ];

  constructor(private svc: UtilisateurService, public auth: AuthService) {}

  ngOnInit(): void {
    this.svc.getAll().subscribe(u => { this.users.set(u); this.loading.set(false); });
  }

  get filtered(): Utilisateur[] {
    const q = this.searchQuery().toLowerCase();
    return this.users().filter(u =>
      (!this.filterRole() || u.role === this.filterRole()) &&
      (!q || u.nomComplet.toLowerCase().includes(q) || u.matricule.toLowerCase().includes(q) || u.email.toLowerCase().includes(q))
    );
  }

  roleClass(role: string): string {
    const m: Record<string, string> = {
      DIRECTEUR_GENERAL:  'role--dg',
      GESTIONNAIRE_STOCK: 'role--gs',
      CHEF_PRODUCTION:    'role--cp',
      AGENT_PRODUCTION:   'role--ap',
      CHEF_MACHINISTE:    'role--cm',
      AGENT_DOSEUR:       'role--ad'
    };
    return m[role] ?? '';
  }

  getRoleLabel(role: string): string { return ROLE_LABELS[role as Role] ?? role; }

  getInitials(nom: string): string {
    const p = nom.split(' ');
    return p.length >= 2 ? p[0][0] + p[1][0] : nom[0];
  }
}
