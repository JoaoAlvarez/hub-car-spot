import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinanceira, NewFinanceira } from '../financeira.model';

export type PartialUpdateFinanceira = Partial<IFinanceira> & Pick<IFinanceira, 'id'>;

export type EntityResponseType = HttpResponse<IFinanceira>;
export type EntityArrayResponseType = HttpResponse<IFinanceira[]>;

@Injectable({ providedIn: 'root' })
export class FinanceiraService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/financeiras');

  create(financeira: NewFinanceira): Observable<EntityResponseType> {
    return this.http.post<IFinanceira>(this.resourceUrl, financeira, { observe: 'response' });
  }

  update(financeira: IFinanceira): Observable<EntityResponseType> {
    return this.http.put<IFinanceira>(`${this.resourceUrl}/${this.getFinanceiraIdentifier(financeira)}`, financeira, {
      observe: 'response',
    });
  }

  partialUpdate(financeira: PartialUpdateFinanceira): Observable<EntityResponseType> {
    return this.http.patch<IFinanceira>(`${this.resourceUrl}/${this.getFinanceiraIdentifier(financeira)}`, financeira, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFinanceira>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFinanceira[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFinanceiraIdentifier(financeira: Pick<IFinanceira, 'id'>): string {
    return financeira.id;
  }

  compareFinanceira(o1: Pick<IFinanceira, 'id'> | null, o2: Pick<IFinanceira, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinanceiraIdentifier(o1) === this.getFinanceiraIdentifier(o2) : o1 === o2;
  }

  addFinanceiraToCollectionIfMissing<Type extends Pick<IFinanceira, 'id'>>(
    financeiraCollection: Type[],
    ...financeirasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const financeiras: Type[] = financeirasToCheck.filter(isPresent);
    if (financeiras.length > 0) {
      const financeiraCollectionIdentifiers = financeiraCollection.map(financeiraItem => this.getFinanceiraIdentifier(financeiraItem));
      const financeirasToAdd = financeiras.filter(financeiraItem => {
        const financeiraIdentifier = this.getFinanceiraIdentifier(financeiraItem);
        if (financeiraCollectionIdentifiers.includes(financeiraIdentifier)) {
          return false;
        }
        financeiraCollectionIdentifiers.push(financeiraIdentifier);
        return true;
      });
      return [...financeirasToAdd, ...financeiraCollection];
    }
    return financeiraCollection;
  }
}
