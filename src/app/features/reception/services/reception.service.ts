import { Injectable, signal } from '@angular/core';
import { Observable, of, throwError, delay } from 'rxjs';
import {
  RegistreReception, CreerRegistreReceptionRequest,
  JustificationAgentRequest, LigneRegistreReception,
  StatutLigneReception, StatutReception, DashboardReception
} from '../models/reception.models';
import { FicheProduit } from '../../../core/models';
import MOCK from '../mocks/reception.mock';

@Injectable({ providedIn: 'root' })
export class ReceptionService {
  private registres = signal<RegistreReception[]>(structuredClone(MOCK));
  private nextId    = MOCK.length + 1;
  private nextRef   = MOCK.length + 1;

  lister(statut?: string): Observable<RegistreReception[]> {
    let list = this.registres();
    if (statut) list = list.filter(r => r.statut === statut);
    return of([...list].sort((a, b) => b.id - a.id)).pipe(delay(300));
  }

  consulter(id: number): Observable<RegistreReception> {
    const r = this.registres().find(r => r.id === id);
    if (!r) return throwError(() => new Error('Registre introuvable'));
    return of({ ...r }).pipe(delay(200));
  }

  consulterParReference(reference: string): Observable<RegistreReception> {
    const r = this.registres().find(r => r.reference === reference);
    if (!r) return throwError(() => new Error('Registre introuvable'));
    return of({ ...r }).pipe(delay(200));
  }

  /** Crée un registre de réception à partir d'une fiche de production */
  creerRegistre(
    req: CreerRegistreReceptionRequest,
    fiche: FicheProduit,
    gestionnaireNom: string,
    gestionnaireMatricule: string
  ): Observable<RegistreReception> {
    // Calcul des lignes avec écarts
    const lignes: LigneRegistreReception[] = req.lignes.map((rl, i) => {
      const ficheL = fiche.lignes.find(fl => fl.produitId === rl.produitId);
      const decCartons = ficheL?.quantiteCartons ?? 0;
      const decUnites  = ficheL?.quantiteUnites  ?? 0;
      const ecartC = rl.quantiteRecueCartons - decCartons;
      const ecartU = rl.quantiteRecueUnites  - decUnites;
      const ecartPct = decUnites > 0 ? (ecartU / decUnites) * 100 : 0;
      let statut: StatutLigneReception = 'CONFORME';
      if (rl.quantiteRecueUnites === 0) statut = 'MANQUANT';
      else if (ecartU > 0)  statut = 'ECART_POSITIF';
      else if (ecartU < 0)  statut = 'ECART_NEGATIF';

      return {
        id: i + 1,
        produitId:   rl.produitId,
        produitCode: ficheL?.produitCode ?? rl.produitId,
        produitNom:  ficheL?.produitNom  ?? rl.produitId,
        categorie:   'BOUILLIE_SOJA',
        conditionnement: '',
        quantiteDeclareeCartons: decCartons,
        quantiteDeclareeUnites:  decUnites,
        quantiteRecueCartons:    rl.quantiteRecueCartons,
        quantiteRecueUnites:     rl.quantiteRecueUnites,
        ecartCartons: ecartC,
        ecartUnites:  ecartU,
        ecartPct:     +ecartPct.toFixed(1),
        statut,
        observationGestionnaire: rl.observationGestionnaire
      };
    });

    const totalDeclaree = lignes.reduce((s, l) => s + l.quantiteDeclareeUnites, 0);
    const totalRecue    = lignes.reduce((s, l) => s + l.quantiteRecueUnites, 0);
    const totalEcart    = totalRecue - totalDeclaree;
    const nbEcart       = lignes.filter(l => l.statut !== 'CONFORME').length;
    const conformite    = totalDeclaree > 0 ? +(totalRecue / totalDeclaree * 100).toFixed(1) : 100;

    const statut: StatutReception = nbEcart > 0 ? 'ECART_SIGNALE' : 'RECUE';

    const registre: RegistreReception = {
      id:        this.nextId++,
      reference: `RECEP-2026-${String(this.nextRef++).padStart(3, '0')}`,
      ficheProductionId:  fiche.id,
      referencePPH:       fiche.referencePPH,
      dateProduction:     fiche.date,
      agentProductionMatricule: fiche.agentMatricule,
      agentProductionNom:       fiche.agentNom,
      gestionnaireMatricule,
      gestionnaireNom,
      dateReception:  req.dateReception,
      heureReception: req.heureReception,
      statut,
      statutLibelle:  statut === 'ECART_SIGNALE' ? 'Écart signalé' : 'Reçue',
      totalDeclareeUnites: totalDeclaree,
      totalRecueUnites:    totalRecue,
      totalEcartUnites:    totalEcart,
      tauxConformite:      conformite,
      nbLignesEcart:       nbEcart,
      lignes,
      commentaireGestionnaire: req.commentaireGestionnaire
    };

    this.registres.update(list => [registre, ...list]);
    return of({ ...registre }).pipe(delay(700));
  }

  /** Agent de production soumet sa justification pour un écart */
  soumettreJustification(req: JustificationAgentRequest): Observable<RegistreReception> {
    this.registres.update(list => list.map(r =>
      r.id === req.registreId
        ? { ...r, justificationAgent: req.justificationAgent, dateJustification: new Date().toISOString().split('T')[0], statut: 'CLOTUREE' as StatutReception, statutLibelle: 'Clôturée' }
        : r
    ));
    return this.consulter(req.registreId);
  }

  /** Clôture un registre sans écart ou après justification */
  cloturer(id: number): Observable<RegistreReception> {
    this.registres.update(list => list.map(r =>
      r.id === id ? { ...r, statut: 'CLOTUREE' as StatutReception, statutLibelle: 'Clôturée' } : r
    ));
    return this.consulter(id);
  }

  /** Vérifie si une fiche de production a déjà un registre de réception */
  ficheADejaRegistre(ficheId: number): boolean {
    return this.registres().some(r => r.ficheProductionId === ficheId);
  }

  getDashboard(): Observable<DashboardReception> {
    const all = this.registres();
    return of({
      totalFichesEnAttente:         all.filter(r => r.statut === 'EN_ATTENTE').length,
      totalFichesRecues:            all.filter(r => r.statut === 'RECUE').length,
      totalEcartsSignales:          all.filter(r => r.statut === 'ECART_SIGNALE').length,
      totalJustificationsEnAttente: all.filter(r => r.statut === 'ECART_SIGNALE' && !r.justificationAgent).length,
      derniersRegistres:            all.slice(0, 5)
    }).pipe(delay(300));
  }

  listerParPPH(referencePPH: string): Observable<RegistreReception[]> {
    return of(this.registres().filter(r => r.referencePPH === referencePPH)).pipe(delay(200));
  }
}
