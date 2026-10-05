import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { StockApiRepository } from '../../../infrastructure/repositories/stock-api.repository';
import { Receipt } from '../../../domain/models/stock.models';
@Injectable({ providedIn: 'root' })
export class ValidateReceptionUseCase {
  private readonly repo = inject(StockApiRepository);
  executeGestionnaire(numero: string, obs?: string): Observable<Receipt> { return this.repo.validateFirst(numero, obs ?? ''); }
  executeChefProduction(numero: string, obs?: string): Observable<Receipt> { return this.repo.validateSecond(numero, obs ?? ''); }
  executeRejet(numero: string, motif: string): Observable<Receipt> { return this.repo.rejectReceipt(numero, motif); }
}
