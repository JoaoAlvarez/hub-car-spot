import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFilial, NewFilial } from '../filial.model';

export type PartialUpdateFilial = Partial<IFilial> & Pick<IFilial, 'id'>;

export type EntityResponseType = HttpResponse<IFilial>;
export type EntityArrayResponseType = HttpResponse<IFilial[]>;

@Injectable({ providedIn: 'root' })
export class FilialService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/filials');

  create(filial: NewFilial): Observable<EntityResponseType> {
    return this.http.post<IFilial>(this.resourceUrl, filial, { observe: 'response' });
  }

  update(filial: IFilial): Observable<EntityResponseType> {
    return this.http.put<IFilial>(`${this.resourceUrl}/${this.getFilialIdentifier(filial)}`, filial, { observe: 'response' });
  }

  partialUpdate(filial: PartialUpdateFilial): Observable<EntityResponseType> {
    return this.http.patch<IFilial>(`${this.resourceUrl}/${this.getFilialIdentifier(filial)}`, filial, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFilial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFilial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFilialIdentifier(filial: Pick<IFilial, 'id'>): string {
    return filial.id;
  }

  compareFilial(o1: Pick<IFilial, 'id'> | null, o2: Pick<IFilial, 'id'> | null): boolean {
    return o1 && o2 ? this.getFilialIdentifier(o1) === this.getFilialIdentifier(o2) : o1 === o2;
  }

  addFilialToCollectionIfMissing<Type extends Pick<IFilial, 'id'>>(
    filialCollection: Type[],
    ...filialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const filials: Type[] = filialsToCheck.filter(isPresent);
    if (filials.length > 0) {
      const filialCollectionIdentifiers = filialCollection.map(filialItem => this.getFilialIdentifier(filialItem));
      const filialsToAdd = filials.filter(filialItem => {
        const filialIdentifier = this.getFilialIdentifier(filialItem);
        if (filialCollectionIdentifiers.includes(filialIdentifier)) {
          return false;
        }
        filialCollectionIdentifiers.push(filialIdentifier);
        return true;
      });
      return [...filialsToAdd, ...filialCollection];
    }
    return filialCollection;
  }
}
