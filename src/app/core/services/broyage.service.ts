import { Injectable, signal } from '@angular/core';
import { Observable, of, throwError, delay } from 'rxjs';
import {
  JourneeProduction, OuvrirJourneeRequest, EnregistrerResultatRequest,
  CorrigerResultatRequest, Classement
} from '../models';
import MOCK from '../../features/broyage/mocks/broyage.mock';

@Injectable({ providedIn: 'root' })
export class BroyageService {
  private journees = signal<JourneeProduction[]>(structuredClone(MOCK.journeesProduction));
  private nextId = MOCK.journeesProduction.length + 1;

  listerParPPH(referencePPH: string): Observable<JourneeProduction[]> {
    return of(this.journees().filter(j => j.referencePPH === referencePPH)).pipe(delay(300));
  }

  consulter(id: number): Observable<JourneeProduction> {
    const j = this.journees().find(j => j.id === id);
    if (!j) return throwError(() => new Error('Journée introuvable'));
    return of({ ...j }).pipe(delay(200));
  }

  ouvrirJournee(req: OuvrirJourneeRequest, chefNom: string, chefMatricule: string): Observable<JourneeProduction> {
    const journee: JourneeProduction = {
      id: this.nextId++,
      referencePPH: req.referencePPH,
      date: req.date,
      statut: 'OUVERTE',
      statutLibelle: 'Ouverte',
      chefMachinisteMatricule: chefMatricule,
      chefMachinisteNom: chefNom,
      totalPoudreNetteKg: null,
      tauxPerteGlobalPourcentage: null,
      machinistes: req.assignations.map((a, i) => ({
        id: i + 1,
        machinisteMatricule: a.machinisteMatricule,
        machinisteNom: a.machinisteMatricule,
        typePoudre: a.typePoudre,
        typePoudreLibelle: a.typePoudre,
        quantiteDepart: null, quantiteObtenue: null,
        perteKg: null, tauxPertePct: null, score: null, correction: null
      }))
    };
    this.journees.update(list => [...list, journee]);
    return of({ ...journee }).pipe(delay(500));
  }

  enregistrerResultat(id: number, req: EnregistrerResultatRequest): Observable<JourneeProduction> {
    this.journees.update(list => list.map(j => {
      if (j.id !== id) return j;
      const perte = req.quantiteDepart - req.quantiteObtenue;
      const tauxPerte = (perte / req.quantiteDepart) * 100;
      return {
        ...j,
        machinistes: j.machinistes.map(m =>
          m.id === req.productionMachinisteId
            ? { ...m, quantiteDepart: req.quantiteDepart, quantiteObtenue: req.quantiteObtenue,
                perteKg: perte, tauxPertePct: tauxPerte, score: 100 - tauxPerte }
            : m
        )
      };
    }));
    return this.consulter(id);
  }

  corrigerResultat(id: number, req: CorrigerResultatRequest): Observable<JourneeProduction> {
    this.journees.update(list => list.map(j => {
      if (j.id !== id) return j;
      const perte = req.nouvelleQuantiteDepart - req.nouvelleQuantiteObtenue;
      const tauxPerte = (perte / req.nouvelleQuantiteDepart) * 100;
      return {
        ...j,
        machinistes: j.machinistes.map(m =>
          m.id === req.productionMachinisteId
            ? { ...m, quantiteDepart: req.nouvelleQuantiteDepart, quantiteObtenue: req.nouvelleQuantiteObtenue,
                perteKg: perte, tauxPertePct: tauxPerte, score: 100 - tauxPerte,
                correction: req.motif }
            : m
        )
      };
    }));
    return this.consulter(id);
  }

  cloturerJournee(id: number): Observable<JourneeProduction> {
    this.journees.update(list => list.map(j => {
      if (j.id !== id) return j;
      const totNet = j.machinistes.reduce((s, m) => s + (m.quantiteObtenue ?? 0), 0);
      const totDepart = j.machinistes.reduce((s, m) => s + (m.quantiteDepart ?? 0), 0);
      const tauxPerte = totDepart > 0 ? ((totDepart - totNet) / totDepart) * 100 : 0;
      return { ...j, statut: 'CLOTUREE' as any, statutLibelle: 'Clôturée', totalPoudreNetteKg: totNet, tauxPerteGlobalPourcentage: tauxPerte };
    }));
    return this.consulter(id);
  }

  classementHebdomadaire(referencePPH: string): Observable<Classement> {
    const mock = MOCK.classements?.hebdomadaire;
    if (mock) return of({ periode: mock.semaine, entries: mock.entries }).pipe(delay(300));
    return of({ periode: referencePPH, entries: [] }).pipe(delay(300));
  }

  classementMensuel(annee: number, mois: number): Observable<Classement> {
    return of({ periode: `${annee}/${mois}`, entries: [] }).pipe(delay(300));
  }
}
