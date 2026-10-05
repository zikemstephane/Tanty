import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { StockApiRepository } from '../../../infrastructure/repositories/stock-api.repository';
import { Supplier } from '../../../domain/models/stock.models';
@Injectable({ providedIn: 'root' })
export class GetFournisseursUseCase {
  private readonly repo = inject(StockApiRepository);
  execute(): Observable<Supplier[]> { return this.repo.getSuppliers(); }
}
