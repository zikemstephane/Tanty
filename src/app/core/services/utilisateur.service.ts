import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { Utilisateur, Role } from '../models';
import MOCK_USERS from '../../../assets/mock-data/utilisateurs.json';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private users: Utilisateur[] = (MOCK_USERS as any).utilisateurs;

  getAll(): Observable<Utilisateur[]> {
    return of([...this.users]).pipe(delay(200));
  }

  getByRole(...roles: Role[]): Observable<Utilisateur[]> {
    return of(this.users.filter(u => roles.includes(u.role as Role))).pipe(delay(200));
  }

  getByMatricule(matricule: string): Observable<Utilisateur | undefined> {
    return of(this.users.find(u => u.matricule === matricule));
  }

  getMachinistes(): Observable<Utilisateur[]> {
    return this.getByRole('CHEF_MACHINISTE', 'AGENT_PRODUCTION');
  }
}
