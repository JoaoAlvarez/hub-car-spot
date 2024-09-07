import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITaxas, NewTaxas } from '../taxas.model';

export type PartialUpdateTaxas = Partial<ITaxas> & Pick<ITaxas, 'id'>;

export type EntityResponseType = HttpResponse<ITaxas>;
export type EntityArrayResponseType = HttpResponse<ITaxas[]>;

@Injectable({ providedIn: 'root' })
export class TaxasService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/taxas');

  create(taxas: NewTaxas): Observable<EntityResponseType> {
    return this.http.post<ITaxas>(this.resourceUrl, taxas, { observe: 'response' });
  }

  update(taxas: ITaxas): Observable<EntityResponseType> {
    return this.http.put<ITaxas>(`${this.resourceUrl}/${this.getTaxasIdentifier(taxas)}`, taxas, { observe: 'response' });
  }

  partialUpdate(taxas: PartialUpdateTaxas): Observable<EntityResponseType> {
    return this.http.patch<ITaxas>(`${this.resourceUrl}/${this.getTaxasIdentifier(taxas)}`, taxas, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ITaxas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaxas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTaxasIdentifier(taxas: Pick<ITaxas, 'id'>): string {
    return taxas.id;
  }

  compareTaxas(o1: Pick<ITaxas, 'id'> | null, o2: Pick<ITaxas, 'id'> | null): boolean {
    return o1 && o2 ? this.getTaxasIdentifier(o1) === this.getTaxasIdentifier(o2) : o1 === o2;
  }

  addTaxasToCollectionIfMissing<Type extends Pick<ITaxas, 'id'>>(
    taxasCollection: Type[],
    ...taxasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const taxas: Type[] = taxasToCheck.filter(isPresent);
    if (taxas.length > 0) {
      const taxasCollectionIdentifiers = taxasCollection.map(taxasItem => this.getTaxasIdentifier(taxasItem));
      const taxasToAdd = taxas.filter(taxasItem => {
        const taxasIdentifier = this.getTaxasIdentifier(taxasItem);
        if (taxasCollectionIdentifiers.includes(taxasIdentifier)) {
          return false;
        }
        taxasCollectionIdentifiers.push(taxasIdentifier);
        return true;
      });
      return [...taxasToAdd, ...taxasCollection];
    }
    return taxasCollection;
  }
}
