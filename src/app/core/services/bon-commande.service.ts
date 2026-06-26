import { Injectable, signal } from '@angular/core';
import { Observable, of, throwError, delay } from 'rxjs';
import { BonDeCommande, CreerBCRequest, ModifierBCRequest, StatutBC } from '../models';
import MOCK from '../../features/bon-commande/mocks/bons-de-commande.mock';

@Injectable({ providedIn: 'root' })
export class BonCommandeService {
  private bcs = signal<BonDeCommande[]>(structuredClone(MOCK));
  private nextId = MOCK.length + 1;

  lister(statut?: string): Observable<BonDeCommande[]> {
    let list = this.bcs();
    if (statut) list = list.filter(b => b.statut === statut);
    return of([...list]).pipe(delay(300));
  }

  consulter(reference: string): Observable<BonDeCommande> {
    const bc = this.bcs().find(b => b.reference === reference);
    if (!bc) return throwError(() => new Error(`BC ${reference} introuvable`));
    return of({ ...bc }).pipe(delay(200));
  }

  creer(req: CreerBCRequest, createurNom: string, createurMatricule: string): Observable<BonDeCommande> {
    const ref = `BC-2026-${String(this.nextId++).padStart(3, '0')}`;
    const bc: BonDeCommande = {
      id: this.nextId,
      reference: ref,
      dateCreation: new Date().toISOString().split('T')[0],
      dateDebutProduction: req.dateDebutProduction,
      dateFinProduction: req.dateFinProduction,
      nombreSemaines: this.calculerSemaines(req.dateDebutProduction, req.dateFinProduction),
      statut: 'BROUILLON',
      statutLibelle: 'Brouillon',
      createurMatricule,
      createurNom,
      validateurMatricule: null,
      validateurNom: null,
      dateValidation: null,
      lignes: req.lignes.map((l, i) => ({ ...l, id: i + 1, numeroligne: i + 1 })),
      pphReferences: []
    };
    this.bcs.update(list => [...list, bc]);
    return of({ ...bc }).pipe(delay(500));
  }

  soumettre(reference: string): Observable<BonDeCommande> {
    return this.transition(reference, 'EN_ATTENTE_VALIDATION', 'En attente de validation');
  }

  valider(reference: string, validateurNom: string, validateurMatricule: string): Observable<BonDeCommande> {
    this.bcs.update(list => list.map(b => {
      if (b.reference !== reference) return b;
      const semaines = b.nombreSemaines;
      const pphs = Array.from({ length: semaines }, (_, i) => `${reference.replace('BC-', 'PPH-')}-S${i+1}`);
      return {
        ...b,
        statut: 'VALIDE' as StatutBC,
        statutLibelle: 'Validé',
        validateurMatricule,
        validateurNom,
        dateValidation: new Date().toISOString().split('T')[0],
        pphReferences: pphs
      };
    }));
    const bc = this.bcs().find(b => b.reference === reference);
    return of({ ...bc! }).pipe(delay(600));
  }

  supprimer(reference: string): Observable<void> {
    const bc = this.bcs().find(b => b.reference === reference);
    if (!bc) return throwError(() => new Error('BC introuvable'));
    if (bc.statut !== 'BROUILLON') return throwError(() => new Error('Seul un brouillon peut être supprimé'));
    this.bcs.update(list => list.filter(b => b.reference !== reference));
    return of(undefined).pipe(delay(300));
  }

  modifierApresValidation(reference: string, req: ModifierBCRequest): Observable<BonDeCommande> {
    if (!req.codeOTP || req.codeOTP.length < 4)
      return throwError(() => new Error('Code OTP invalide'));
    this.bcs.update(list => list.map(b =>
      b.reference === reference ? { ...b, lignes: req.lignes.map((l, i) => ({ ...l, id: i + 1, numeroligne: i + 1 })) } : b
    ));
    const bc = this.bcs().find(b => b.reference === reference);
    return of({ ...bc! }).pipe(delay(500));
  }

  genererOTP(matriculeCible: string): Observable<{ codeOTP: string; matriculeCible: string }> {
    const code = Math.floor(100000 + Math.random() * 900000).toString();
    return of({ codeOTP: code, matriculeCible }).pipe(delay(400));
  }

  private transition(ref: string, statut: StatutBC, libelle: string): Observable<BonDeCommande> {
    this.bcs.update(list => list.map(b =>
      b.reference === ref ? { ...b, statut, statutLibelle: libelle } : b
    ));
    const bc = this.bcs().find(b => b.reference === ref);
    return of({ ...bc! }).pipe(delay(400));
  }

  private calculerSemaines(debut: string, fin: string): number {
    const d1 = new Date(debut), d2 = new Date(fin);
    const diffMs = d2.getTime() - d1.getTime();
    return Math.max(1, Math.ceil(diffMs / (7 * 24 * 3600 * 1000)));
  }
}
