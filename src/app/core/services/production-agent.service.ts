import { Injectable, signal } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import {
  FicheProduit, RegistreProduction, RapportHebdomadaire,
  EnregistrerFicheProduitRequest
} from '../models';
import MOCK from '../../features/production-agent/mocks/production-agent.mock';

@Injectable({ providedIn: 'root' })
export class ProductionAgentService {
  private fiches    = signal<FicheProduit[]>(structuredClone(MOCK.fiches));
  private registres = signal<RegistreProduction[]>(structuredClone(MOCK.registres));
  private nextFicheId = MOCK.fiches.length + 1;
  private nextRegId   = MOCK.registres.length + 1;

  // ── Fiches ────────────────────────────────────────────────────────────────
  listerFichesParPPH(referencePPH: string): Observable<FicheProduit[]> {
    return of(this.fiches().filter(f => f.referencePPH === referencePPH)).pipe(delay(300));
  }

  consulterFiche(id: number): Observable<FicheProduit> {
    const f = this.fiches().find(f => f.id === id);
    return of(f!).pipe(delay(200));
  }

  enregistrerFiche(req: EnregistrerFicheProduitRequest, agentNom: string, agentMatricule: string): Observable<FicheProduit> {
    const fiche: FicheProduit = {
      id: this.nextFicheId++,
      referencePPH: req.referencePPH,
      date: req.date,
      agentMatricule,
      agentNom,
      totalProduit: req.lignes.reduce((s, l) => s + l.quantiteUnites, 0),
      lignes: req.lignes.map(l => ({ ...l, produitCode: l.produitId, produitNom: l.produitId }))
    };
    this.fiches.update(list => [...list, fiche]);
    return of({ ...fiche }).pipe(delay(600));
  }

  // ── Registres ─────────────────────────────────────────────────────────────
  listerRegistresParPPH(referencePPH: string): Observable<RegistreProduction[]> {
    return of(this.registres().filter(r => r.referencePPH === referencePPH)).pipe(delay(300));
  }

  consulterRegistre(id: number): Observable<RegistreProduction> {
    return of(this.registres().find(r => r.id === id)!).pipe(delay(200));
  }

  enregistrerRegistre(req: any, agentNom: string, agentMatricule: string): Observable<RegistreProduction> {
    const reg: RegistreProduction = {
      id: this.nextRegId++,
      referencePPH: req.referencePPH,
      date: req.date,
      agentMatricule,
      agentNom,
      employes: req.employes || []
    };
    this.registres.update(list => [...list, reg]);
    return of({ ...reg }).pipe(delay(600));
  }

  // ── Rapports ──────────────────────────────────────────────────────────────
  rapportHebdomadaire(referencePPH: string): Observable<RapportHebdomadaire> {
  const rapport = MOCK.rapportsHebdomadaires.find(
    r => r.referencePPH === referencePPH
  );

  if (rapport) {
    const normalized: RapportHebdomadaire = {
      ...rapport,
      classementEmployes: (rapport.classementEmployes ?? []).map(e => ({
        rang: e.rang,
        nom: e.nom,
        totalProduction: e.totalProduction,
        score: (e as any).score ?? (e as any).scoreMoyen ?? 0
      }))
    };

    return of(normalized).pipe(delay(400));
  }

  // Generate a safe fallback
  const mock: RapportHebdomadaire = {
    referencePPH,
    numeroSemaine: 24,
    annee: 2026,
    dateDebut: '2026-06-16',
    dateFin: '2026-06-22',
    totalPredite: 0,
    totalRealisee: 0,
    tauxRealisation: 0,
    classementEmployes: [],
    lignes: []
  };

  return of(mock).pipe(delay(400));
}
}
