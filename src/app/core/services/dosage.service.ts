import { Injectable, signal } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { SessionDosage, EnregistrerSessionDosageRequest, StatistiqueDosage, FORMULES_DOSAGE } from '../models';
import MOCK from '../../features/dosage/mocks/dosage.mock';

@Injectable({ providedIn: 'root' })
export class DosageService {
  private sessions = signal<SessionDosage[]>(structuredClone(MOCK.sessions));
  private nextId = MOCK.sessions.length + 1;

  listerParPPH(referencePPH: string): Observable<SessionDosage[]> {
    return of(this.sessions().filter(s => s.referencePPH === referencePPH)).pipe(delay(300));
  }

  consulter(id: number): Observable<SessionDosage> {
    return of(this.sessions().find(s => s.id === id)!).pipe(delay(200));
  }

  enregistrerSession(req: EnregistrerSessionDosageRequest, agentNom: string, agentMatricule: string): Observable<SessionDosage> {
    const formule = FORMULES_DOSAGE[req.typeMelange];
    const expectedMais = formule.mais * req.nombreFuts;
    const expectedSoja = formule.soja * req.nombreFuts;
    const expectedArachide = formule.arachide * req.nombreFuts;
    const conforme =
      Math.abs(req.composition.maisKg - expectedMais) < 2 &&
      Math.abs(req.composition.sojaKg - expectedSoja) < 2 &&
      Math.abs(req.composition.arachideKg - expectedArachide) < 2;

    const session: SessionDosage = {
      id: this.nextId++,
      referencePPH: req.referencePPH,
      date: req.date,
      agentMatricule,
      agentNom,
      typeMelange: req.typeMelange,
      typeMelangeLibelle: req.typeMelange === 'BOUILLIE' ? 'Bouillie' : 'Mélange Arachide',
      nombreFuts: req.nombreFuts,
      poidsNetTotalKg: req.nombreFuts * 50,
      compositionConforme: conforme,
      avertissementComposition: conforme ? null : 'Composition non conforme à la formule standard',
      composition: req.composition
    };
    this.sessions.update(list => [...list, session]);
    return of({ ...session }).pipe(delay(600));
  }

  corrigerSession(id: number, req: EnregistrerSessionDosageRequest, agentNom: string, agentMatricule: string): Observable<SessionDosage> {
    return this.enregistrerSession(req, agentNom, agentMatricule);
  }

  statistiquesSemaine(date: Date | string): Observable<StatistiqueDosage> {
    return of({ ...MOCK.statistiques.semaine } as any).pipe(delay(400));
  }

  statistiquesMois(annee: number, mois: number): Observable<StatistiqueDosage> {
    return of({ ...MOCK.statistiques.semaine, dateDebut: `${annee}-${mois}-01`, dateFin: `${annee}-${mois}-30` } as any).pipe(delay(400));
  }
}
