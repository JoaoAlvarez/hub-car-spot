import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICombustivel, NewCombustivel } from '../combustivel.model';

export type PartialUpdateCombustivel = Partial<ICombustivel> & Pick<ICombustivel, 'id'>;

export type EntityResponseType = HttpResponse<ICombustivel>;
export type EntityArrayResponseType = HttpResponse<ICombustivel[]>;

@Injectable({ providedIn: 'root' })
export class CombustivelService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/combustivels');

  create(combustivel: NewCombustivel): Observable<EntityResponseType> {
    return this.http.post<ICombustivel>(this.resourceUrl, combustivel, { observe: 'response' });
  }

  update(combustivel: ICombustivel): Observable<EntityResponseType> {
    return this.http.put<ICombustivel>(`${this.resourceUrl}/${this.getCombustivelIdentifier(combustivel)}`, combustivel, {
      observe: 'response',
    });
  }

  partialUpdate(combustivel: PartialUpdateCombustivel): Observable<EntityResponseType> {
    return this.http.patch<ICombustivel>(`${this.resourceUrl}/${this.getCombustivelIdentifier(combustivel)}`, combustivel, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICombustivel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICombustivel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCombustivelIdentifier(combustivel: Pick<ICombustivel, 'id'>): string {
    return combustivel.id;
  }

  compareCombustivel(o1: Pick<ICombustivel, 'id'> | null, o2: Pick<ICombustivel, 'id'> | null): boolean {
    return o1 && o2 ? this.getCombustivelIdentifier(o1) === this.getCombustivelIdentifier(o2) : o1 === o2;
  }

  addCombustivelToCollectionIfMissing<Type extends Pick<ICombustivel, 'id'>>(
    combustivelCollection: Type[],
    ...combustivelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const combustivels: Type[] = combustivelsToCheck.filter(isPresent);
    if (combustivels.length > 0) {
      const combustivelCollectionIdentifiers = combustivelCollection.map(combustivelItem => this.getCombustivelIdentifier(combustivelItem));
      const combustivelsToAdd = combustivels.filter(combustivelItem => {
        const combustivelIdentifier = this.getCombustivelIdentifier(combustivelItem);
        if (combustivelCollectionIdentifiers.includes(combustivelIdentifier)) {
          return false;
        }
        combustivelCollectionIdentifiers.push(combustivelIdentifier);
        return true;
      });
      return [...combustivelsToAdd, ...combustivelCollection];
    }
    return combustivelCollection;
  }
}
