import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';
import { AppLayoutComponent } from './layout/app-layout/app-layout.component';

export const routes: Routes = [
  // ── Auth ──────────────────────────────────────────────────────────────────
  {
    path: 'auth',
    children: [
      { path: 'login',    loadComponent: () => import('./features/auth/pages/login/login.component').then(m => m.LoginComponent) },
      { path: 'register', loadComponent: () => import('./features/auth/pages/register/register.component').then(m => m.RegisterComponent) },
      { path: '', redirectTo: 'login', pathMatch: 'full' }
    ]
  },

  // ── App avec layout ──────────────────────────────────────────────────────
  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [authGuard],
    children: [
      // Dashboard
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },

      // Statistiques (tous les rôles)
      { path: 'statistiques', loadComponent: () => import('./features/statistiques/statistiques.component').then(m => m.StatistiquesComponent) },

      // Autorisations OTP (DG seulement)
      { path: 'autorisations-otp', canActivate: [roleGuard('DIRECTEUR_GENERAL')],
        loadComponent: () => import('./features/otp/pages/otp-manager.component').then(m => m.OtpManagerComponent) },

      // Bons de Commande (pas AGENT_PRODUCTION, pas CHEF_MACHINISTE, pas AGENT_DOSEUR)
      {
        path: 'bons-de-commande',
        canActivate: [roleGuard('DIRECTEUR_GENERAL','GESTIONNAIRE_STOCK','CHEF_PRODUCTION')],
        children: [
          { path: '', loadComponent: () => import('./features/bon-commande/pages/list/bc-list.component').then(m => m.BcListComponent) },
          { path: 'nouveau', loadComponent: () => import('./features/bon-commande/pages/create/bc-create.component').then(m => m.BcCreateComponent) },
          { path: ':reference/modifier', loadComponent: () => import('./features/bon-commande/pages/modifier/bc-modifier.component').then(m => m.BcModifierComponent) },
          { path: ':reference', loadComponent: () => import('./features/bon-commande/pages/detail/bc-detail.component').then(m => m.BcDetailComponent) }
        ]
      },

      // PPH (tous les rôles peuvent voir)
      {
        path: 'pphs',
        children: [
          { path: '', loadComponent: () => import('./features/pph/pages/list/pph-list.component').then(m => m.PphListComponent) },
          { path: ':reference', loadComponent: () => import('./features/pph/pages/detail/pph-detail.component').then(m => m.PphDetailComponent) }
        ]
      },

      // Production Agent (AGENT_PRODUCTION, CHEF_PRODUCTION, DG)
      {
        path: 'production',
        children: [
          { path: '', loadComponent: () => import('./features/production-agent/pages/dashboard/production-dashboard.component').then(m => m.ProductionDashboardComponent) },
          {
            path: 'fiches',
            children: [
              { path: '', loadComponent: () => import('./features/production-agent/pages/fiches/list/fiches-list.component').then(m => m.FichesListComponent) },
              { path: 'nouveau', loadComponent: () => import('./features/production-agent/pages/fiches/create/fiche-create.component').then(m => m.FicheCreateComponent) },
              { path: ':id', loadComponent: () => import('./features/production-agent/pages/fiches/detail/fiche-detail.component').then(m => m.FicheDetailComponent) }
            ]
          },
          {
            path: 'registres',
            children: [
              { path: '', loadComponent: () => import('./features/production-agent/pages/registres/list/registres-list.component').then(m => m.RegistresListComponent) },
              { path: 'nouveau', loadComponent: () => import('./features/production-agent/pages/registres/create/registre-create.component').then(m => m.RegistreCreateComponent) },
              { path: ':id', loadComponent: () => import('./features/production-agent/pages/registres/detail/registre-detail.component').then(m => m.RegistreDetailComponent) }
            ]
          },
          {
            path: 'rapports',
            children: [
              { path: '', loadComponent: () => import('./features/production-agent/pages/rapports/list/rapports-list.component').then(m => m.RapportsListComponent) },
              { path: ':referencePPH', loadComponent: () => import('./features/production-agent/pages/rapports/rapport-hebdo.component').then(m => m.RapportHebdoComponent) }
            ]
          }
        ]
      },

      // Broyage (CHEF_MACHINISTE, CHEF_PRODUCTION, DG)
      {
        path: 'broyage',
        children: [
          { path: '', loadComponent: () => import('./features/broyage/pages/dashboard/broyage-dashboard.component').then(m => m.BroyageDashboardComponent) },
          {
            path: 'journees',
            children: [
              { path: '', loadComponent: () => import('./features/broyage/pages/dashboard/broyage-dashboard.component').then(m => m.BroyageDashboardComponent) },
              { path: 'nouveau', canActivate: [roleGuard('CHEF_MACHINISTE','CHEF_PRODUCTION','DIRECTEUR_GENERAL')],
                loadComponent: () => import('./features/broyage/pages/journees/create/journee-create.component').then(m => m.JourneeCreateComponent) },
              { path: ':id', loadComponent: () => import('./features/broyage/pages/journees/journee-detail.component').then(m => m.JourneeDetailComponent) }
            ]
          },
          { path: 'classements', loadComponent: () => import('./features/broyage/pages/classements/classement.component').then(m => m.ClassementComponent) }
        ]
      },

      // Dosage (AGENT_DOSEUR, CHEF_PRODUCTION, DG — pas CHEF_MACHINISTE, pas AGENT_PRODUCTION)
      {
        path: 'dosage',
        children: [
          { path: '', loadComponent: () => import('./features/dosage/pages/dashboard/dosage-dashboard.component').then(m => m.DosageDashboardComponent) },
          { path: 'nouveau', canActivate: [roleGuard('AGENT_DOSEUR','CHEF_PRODUCTION','DIRECTEUR_GENERAL')],
            loadComponent: () => import('./features/dosage/pages/dashboard/dosage-dashboard.component').then(m => m.DosageDashboardComponent) },
          { path: 'statistiques', loadComponent: () => import('./features/dosage/pages/dashboard/dosage-dashboard.component').then(m => m.DosageDashboardComponent) },
          { path: 'registres', loadComponent: () => import('./features/dosage/pages/registres/dosage-registres.component').then(m => m.DosageRegistresComponent) }
        ]
      },

      // Réception produits finis
      {
        path: 'reception',
        children: [
          { path: '', loadComponent: () => import('./features/reception/pages/dashboard/reception-dashboard.component').then(m => m.ReceptionDashboardComponent) },
          { path: 'nouveau', canActivate: [roleGuard('GESTIONNAIRE_STOCK','DIRECTEUR_GENERAL')],
            loadComponent: () => import('./features/reception/pages/registre-reception/create/registre-reception-create.component').then(m => m.RegistreReceptionCreateComponent) },
          { path: ':id', loadComponent: () => import('./features/reception/pages/registre-reception/detail/registre-reception-detail.component').then(m => m.RegistreReceptionDetailComponent) }
        ]
      },

      // Produits (catalogue)
      { path: 'produits', loadComponent: () => import('./features/produits/pages/list/produits-list.component').then(m => m.ProduitsListComponent) },

      // Utilisateurs (DG seulement)
      { path: 'utilisateurs', canActivate: [roleGuard('DIRECTEUR_GENERAL')],
        loadComponent: () => import('./features/utilisateurs/pages/list/utilisateurs-list.component').then(m => m.UtilisateursListComponent) },

      // Pages d'erreur
      { path: 'unauthorized', loadComponent: () => import('./features/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent) },
      { path: '404',          loadComponent: () => import('./features/not-found/not-found.component').then(m => m.NotFoundComponent) },

      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: '**', redirectTo: '404' }
    ]
  },
  { path: '**', redirectTo: '/auth/login' }
];
