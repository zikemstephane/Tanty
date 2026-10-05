import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { StockApiRepository, BufferValuationItem } from '../../../infrastructure/repositories/stock-api.repository';
import {
  DotationRequest, CreateDotationRequest, DotationStatus,
  Commercial, Product, StockLevel
} from '../../../domain/models';

@Injectable({ providedIn: 'root' })
export class DotationUseCase {
  private repo = inject(StockApiRepository);

  getAll(): Observable<DotationRequest[]> {
    return this.repo.getDotations();
  }

  getPending(): Observable<DotationRequest[]> {
    return this.repo.getPendingDotations();
  }

  getPaymentVerified(): Observable<DotationRequest[]> {
    return this.repo.getPaymentVerifiedDotations();
  }

  getQuantityValidated(): Observable<DotationRequest[]> {
    return this.repo.getQuantityValidatedDotations();
  }

  getReviewed(): Observable<DotationRequest[]> {
    return this.repo.getReviewedDotations();
  }

  getById(reference: string): Observable<DotationRequest> {
    return this.repo.getDotationById(reference);
  }

  getByCommercial(matricule: string): Observable<DotationRequest[]> {
    return this.repo.getDotationsByCommercial(matricule);
  }

  create(data: CreateDotationRequest): Observable<DotationRequest> {
    return this.repo.createDotation(data);
  }

  verifyPayment(reference: string, verifierId: string): Observable<DotationRequest> {
    return this.repo.verifyPayment(reference, verifierId);
  }

  validateQuantities(reference: string, validatorId: string, comments: string, items?: any[]): Observable<DotationRequest> {
    return this.repo.validateQuantities(reference, validatorId, comments, items);
  }

  review(reference: string, reviewerId: string, reviewComments: string, items?: any[]): Observable<DotationRequest> {
    return this.repo.reviewDotation(reference, reviewerId, reviewComments, items);
  }

  approve(reference: string, approverId: string): Observable<DotationRequest> {
    return this.repo.approveDotation(reference, approverId);
  }

  reviewAndApprove(reference: string, managerId: string, reviewComments: string, items?: any[]): Observable<DotationRequest> {
    return this.repo.reviewAndApproveDotation(reference, managerId, reviewComments, items);
  }

  reject(reference: string, rejecterId: string, reason: string): Observable<DotationRequest> {
    return this.repo.rejectDotation(reference, rejecterId, reason);
  }

  execute(reference: string, executorId: string): Observable<DotationRequest> {
    return this.repo.executeDotation(reference, executorId);
  }

  delete(reference: string): Observable<void> {
    return this.repo.deleteDotation(reference);
  }

  getCommercials(): Observable<Commercial[]> {
    return this.repo.getCommercials();
  }

  getProducts(): Observable<Product[]> {
    return this.repo.getProducts();
  }

  getStockLevels(): Observable<StockLevel[]> {
    return this.repo.getStockLevels();
  }

  getBufferStockItems(): Observable<BufferValuationItem[]> {
    return this.repo.getBufferValuation().pipe(
      map(r => r.items || [])
    );
  }
}
