import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { StockApiRepository } from '../../../infrastructure/repositories/stock-api.repository';
import { Receipt } from '../../../domain/models/stock.models';
@Injectable({ providedIn: 'root' })
export class CreateReceptionUseCase {
  private readonly repo = inject(StockApiRepository);
  execute(data: Partial<Receipt>): Observable<Receipt> { return this.repo.createReceipt(data); }
}
