import { Component, input, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Role } from '../../core/models';

interface NavItem {
  label: string;
  icon: string;
  route?: string;
  roles?: Role[];
  badge?: number;
  divider?: boolean;
  isQuickAction?: boolean;
  children?: NavItem[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  collapsed = input(false);
  expandedSection = '';

  // ── Menu complet par section ────────────────────────────────────────────────
  readonly sections: { title: string; items: NavItem[] }[] = [
    {
      title: 'Principal',
      items: [
        { label: 'Tableau de bord', icon: 'dashboard', route: '/dashboard', roles: ['DIRECTEUR_GENERAL','CHEF_PRODUCTION','GESTIONNAIRE_STOCK','AGENT_PRODUCTION','CHEF_MACHINISTE','AGENT_DOSEUR'] },
        { label: 'Statistiques', icon: 'bar_chart', route: '/statistiques', roles: ['DIRECTEUR_GENERAL','CHEF_PRODUCTION','GESTIONNAIRE_STOCK','AGENT_PRODUCTION','CHEF_MACHINISTE','AGENT_DOSEUR'] }
      ]
    },
    {
      title: 'Planification',
      items: [
        { label: 'Bons de Commande', icon: 'receipt_long', route: '/bons-de-commande', roles: ['DIRECTEUR_GENERAL','CHEF_PRODUCTION','GESTIONNAIRE_STOCK'] },
        { label: 'Nouveau BC', icon: 'add_circle', route: '/bons-de-commande/nouveau', roles: ['DIRECTEUR_GENERAL','CHEF_PRODUCTION','GESTIONNAIRE_STOCK'], isQuickAction: true },
        { label: 'Plans (PPH)', icon: 'calendar_today', route: '/pphs', roles: ['DIRECTEUR_GENERAL','CHEF_PRODUCTION','GESTIONNAIRE_STOCK','AGENT_PRODUCTION','CHEF_MACHINISTE','AGENT_DOSEUR'] }
      ]
    },
    {
      title: 'Production',
      items: [
        { label: 'Fiches Produit', icon: 'inventory_2', route: '/production/fiches', roles: ['AGENT_PRODUCTION','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] },
        { label: 'Nouvelle Fiche', icon: 'add_circle', route: '/production/fiches/nouveau', roles: ['AGENT_PRODUCTION','CHEF_PRODUCTION','DIRECTEUR_GENERAL'], isQuickAction: true },
        { label: 'Registres Journaliers', icon: 'people', route: '/production/registres', roles: ['AGENT_PRODUCTION','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] },
        { label: 'Nouveau Registre', icon: 'add_circle', route: '/production/registres/nouveau', roles: ['AGENT_PRODUCTION','CHEF_PRODUCTION','DIRECTEUR_GENERAL'], isQuickAction: true },
        { label: 'Rapports Hebdo', icon: 'assessment', route: '/production/rapports', roles: ['AGENT_PRODUCTION','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] }
      ]
    },
    {
      title: 'Broyage',
      items: [
        { label: 'Journées Broyage', icon: 'precision_manufacturing', route: '/broyage/journees', roles: ['CHEF_MACHINISTE','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] },
        { label: 'Ouvrir Journée', icon: 'add_circle', route: '/broyage/journees/nouveau', roles: ['CHEF_MACHINISTE','CHEF_PRODUCTION','DIRECTEUR_GENERAL'], isQuickAction: true },
        { label: 'Classements', icon: 'emoji_events', route: '/broyage/classements', roles: ['CHEF_MACHINISTE','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] }
      ]
    },
    {
      title: 'Dosage',
      items: [
        { label: 'Sessions Dosage', icon: 'science', route: '/dosage', roles: ['AGENT_DOSEUR','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] },
        { label: 'Nouvelle Session', icon: 'add_circle', route: '/dosage/nouveau', roles: ['AGENT_DOSEUR','CHEF_PRODUCTION','DIRECTEUR_GENERAL'], isQuickAction: true },
        { label: 'Registres Dosage', icon: 'book', route: '/dosage/registres', roles: ['AGENT_DOSEUR','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] },
        { label: 'Statistiques Dosage', icon: 'analytics', route: '/dosage/statistiques', roles: ['AGENT_DOSEUR','CHEF_PRODUCTION','DIRECTEUR_GENERAL'] }
      ]
    },
    {
      title: 'Stock',
      items: [
        { label: 'Réception Produits', icon: 'warehouse', route: '/reception', roles: ['GESTIONNAIRE_STOCK','AGENT_PRODUCTION','DIRECTEUR_GENERAL','CHEF_PRODUCTION'] },
        { label: 'Nouvelle Réception', icon: 'add_circle', route: '/reception/nouveau', roles: ['GESTIONNAIRE_STOCK','DIRECTEUR_GENERAL'], isQuickAction: true },
        { label: 'Catalogue Produits', icon: 'inventory', route: '/produits', roles: ['DIRECTEUR_GENERAL','CHEF_PRODUCTION','GESTIONNAIRE_STOCK','AGENT_PRODUCTION','CHEF_MACHINISTE','AGENT_DOSEUR'] }
      ]
    },
    {
      title: 'Administration',
      items: [
        { label: 'Autorisations OTP', icon: 'admin_panel_settings', route: '/autorisations-otp', roles: ['DIRECTEUR_GENERAL'] },
        { label: 'Utilisateurs', icon: 'group', route: '/utilisateurs', roles: ['DIRECTEUR_GENERAL'] }
      ]
    }
  ];

  visibleSections = computed(() => {
    const role = this.auth.currentRole();
    if (!role) return [];
    return this.sections
      .map(s => ({
        ...s,
        items: s.items.filter(i => !i.roles || i.roles.includes(role))
      }))
      .filter(s => s.items.length > 0);
  });

  constructor(public auth: AuthService) {}
}
