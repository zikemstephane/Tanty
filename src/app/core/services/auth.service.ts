import { Injectable, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, throwError, delay } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse, Utilisateur, Role } from '../models';

const MOCK_USERS: { matricule: string; motDePasse: string; user: LoginResponse }[] = [
  {
    matricule: 'TAN2026001', motDePasse: 'Admin1234!',
    user: { token: 'mock-jwt-dg-001', matricule: 'TAN2026001', nomComplet: 'Jean-Pierre MBARGA', role: 'DIRECTEUR_GENERAL', roleLibelle: 'Directeur Général' }
  },
  {
    matricule: 'TAN2026002', motDePasse: 'Stock1234!',
    user: { token: 'mock-jwt-gs-002', matricule: 'TAN2026002', nomComplet: 'Marie-Claire FOUDA', role: 'GESTIONNAIRE_STOCK', roleLibelle: 'Gestionnaire de Stock' }
  },
  {
    matricule: 'TAN2026003', motDePasse: 'Chef1234!',
    user: { token: 'mock-jwt-cp-003', matricule: 'TAN2026003', nomComplet: 'Paul NKOMO', role: 'CHEF_PRODUCTION', roleLibelle: 'Chef de Production' }
  },
  {
    matricule: 'TAN2026004', motDePasse: 'Agent1234!',
    user: { token: 'mock-jwt-ap-004', matricule: 'TAN2026004', nomComplet: 'Bernadette ESSAMA', role: 'AGENT_PRODUCTION', roleLibelle: 'Agent de Production' }
  },
  {
    matricule: 'TAN2026005', motDePasse: 'Mach1234!',
    user: { token: 'mock-jwt-cm-005', matricule: 'TAN2026005', nomComplet: 'Théodore OWONA', role: 'CHEF_MACHINISTE', roleLibelle: 'Chef Machiniste' }
  },
  {
    matricule: 'TAN2026006', motDePasse: 'Dose1234!',
    user: { token: 'mock-jwt-ad-006', matricule: 'TAN2026006', nomComplet: 'Sandrine BIYA', role: 'AGENT_DOSEUR', roleLibelle: 'Agent Doseur' }
  }
];

const TOKEN_KEY = 'tanty_token';
const USER_KEY  = 'tanty_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly _currentUser = signal<LoginResponse | null>(this.loadUser());
  private nextId = 9;

  readonly currentUser  = this._currentUser.asReadonly();
  readonly isLoggedIn   = computed(() => this._currentUser() !== null);
  readonly currentRole  = computed(() => this._currentUser()?.role ?? null);
  readonly matricule    = computed(() => this._currentUser()?.matricule ?? null);
  readonly nomComplet   = computed(() => this._currentUser()?.nomComplet ?? '');

  constructor(private router: Router) {}

  login(req: LoginRequest): Observable<LoginResponse> {
    const found = MOCK_USERS.find(u =>
      u.matricule.toUpperCase() === req.matricule.toUpperCase() &&
      u.motDePasse === req.motDePasse
    );
    if (!found) return throwError(() => new Error('Matricule ou mot de passe incorrect'));
    return of(found.user).pipe(
      delay(600),
      // store on success handled in component
    );
  }

  loginAndStore(req: LoginRequest): Observable<LoginResponse> {
    const found = MOCK_USERS.find(u =>
      u.matricule.toUpperCase() === req.matricule.toUpperCase() &&
      u.motDePasse === req.motDePasse
    );
    if (!found) return throwError(() => new Error('Matricule ou mot de passe incorrect'));
    const resp = found.user;
    localStorage.setItem(TOKEN_KEY, resp.token);
    localStorage.setItem(USER_KEY, JSON.stringify(resp));
    this._currentUser.set(resp);
    return of(resp).pipe(delay(600));
  }

  register(req: RegisterRequest): Observable<RegisterResponse> {
    if (req.motDePasse !== req.confirmationMotDePasse)
      return throwError(() => new Error('Les mots de passe ne correspondent pas'));
    const matricule = `TAN2026${String(this.nextId++).padStart(3, '0')}`;
    MOCK_USERS.push({
      matricule,
      motDePasse: req.motDePasse,
      user: {
        token: `mock-jwt-${Math.random().toString(36).slice(2)}`,
        matricule,
        nomComplet: `${req.prenom} ${req.nom}`,
        role: req.role as Role,
        roleLibelle: req.role
      }
    });
    return of({ matricule, message: `Compte créé avec succès. Matricule : ${matricule}` }).pipe(delay(800));
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this._currentUser.set(null);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  hasRole(...roles: Role[]): boolean {
    const r = this.currentRole();
    return r !== null && roles.includes(r);
  }

  hasAnyRole(...roles: Role[]): boolean {
    return this.hasRole(...roles);
  }

  private loadUser(): LoginResponse | null {
    try {
      const raw = localStorage.getItem(USER_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch { return null; }
  }
}
