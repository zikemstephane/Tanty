import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { StockApiRepository } from '../../../infrastructure/repositories/stock-api.repository';
import { Receipt } from '../../../domain/models/stock.models';
@Injectable({ providedIn: 'root' })
export class GetReceptionsUseCase {
  private readonly repo = inject(StockApiRepository);
  execute(): Observable<Receipt[]> { return this.repo.getReceipts(); }
  executeEnAttente(): Observable<Receipt[]> {
    return this.repo.getReceipts().pipe(
      map(receipts => receipts.filter(r =>
        r.status === 'PENDING_FIRST_VALIDATION' || r.status === 'PENDING_SECOND_VALIDATION'
      ))
    );
  }
}
