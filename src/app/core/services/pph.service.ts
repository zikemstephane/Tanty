import { Injectable, signal } from '@angular/core';
import { Observable, of, throwError, delay } from 'rxjs';
import { PPH, AjusterQuantitePPHRequest } from '../models';
import MOCK_PPH from '../../features/bon-commande/mocks/pphs.mock';

@Injectable({ providedIn: 'root' })
export class PphService {
  private pphs = signal<PPH[]>(structuredClone(MOCK_PPH));

  lister(statut?: string): Observable<PPH[]> {
    let list = this.pphs();
    if (statut) list = list.filter(p => p.statut === statut);
    return of([...list]).pipe(delay(300));
  }

  consulter(reference: string): Observable<PPH> {
    const pph = this.pphs().find(p => p.reference === reference);
    if (!pph) return throwError(() => new Error(`PPH ${reference} introuvable`));
    return of({ ...pph }).pipe(delay(200));
  }

  listerParBC(referenceBC: string): Observable<PPH[]> {
    return of(this.pphs().filter(p => p.referenceBC === referenceBC)).pipe(delay(200));
  }

  ajusterQuantite(referencePPH: string, req: AjusterQuantitePPHRequest): Observable<PPH> {
    this.pphs.update(list => list.map(p => {
      if (p.reference !== referencePPH) return p;
      return {
        ...p,
        statut: 'AJUSTE' as any,
        lignes: p.lignes.map(l =>
          l.id === req.lignePPHId
            ? { ...l, quantiteCibleCartons: req.nouvelleQuantite }
            : l
        )
      };
    }));
    const pph = this.pphs().find(p => p.reference === referencePPH);
    return of({ ...pph! }).pipe(delay(400));
  }
}
