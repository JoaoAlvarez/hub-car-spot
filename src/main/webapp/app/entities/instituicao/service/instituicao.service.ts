import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstituicao, NewInstituicao } from '../instituicao.model';

export type PartialUpdateInstituicao = Partial<IInstituicao> & Pick<IInstituicao, 'id'>;

export type EntityResponseType = HttpResponse<IInstituicao>;
export type EntityArrayResponseType = HttpResponse<IInstituicao[]>;

@Injectable({ providedIn: 'root' })
export class InstituicaoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/instituicaos');

  create(instituicao: NewInstituicao): Observable<EntityResponseType> {
    return this.http.post<IInstituicao>(this.resourceUrl, instituicao, { observe: 'response' });
  }

  update(instituicao: IInstituicao): Observable<EntityResponseType> {
    return this.http.put<IInstituicao>(`${this.resourceUrl}/${this.getInstituicaoIdentifier(instituicao)}`, instituicao, {
      observe: 'response',
    });
  }

  partialUpdate(instituicao: PartialUpdateInstituicao): Observable<EntityResponseType> {
    return this.http.patch<IInstituicao>(`${this.resourceUrl}/${this.getInstituicaoIdentifier(instituicao)}`, instituicao, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInstituicao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstituicao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInstituicaoIdentifier(instituicao: Pick<IInstituicao, 'id'>): string {
    return instituicao.id;
  }

  compareInstituicao(o1: Pick<IInstituicao, 'id'> | null, o2: Pick<IInstituicao, 'id'> | null): boolean {
    return o1 && o2 ? this.getInstituicaoIdentifier(o1) === this.getInstituicaoIdentifier(o2) : o1 === o2;
  }

  addInstituicaoToCollectionIfMissing<Type extends Pick<IInstituicao, 'id'>>(
    instituicaoCollection: Type[],
    ...instituicaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const instituicaos: Type[] = instituicaosToCheck.filter(isPresent);
    if (instituicaos.length > 0) {
      const instituicaoCollectionIdentifiers = instituicaoCollection.map(instituicaoItem => this.getInstituicaoIdentifier(instituicaoItem));
      const instituicaosToAdd = instituicaos.filter(instituicaoItem => {
        const instituicaoIdentifier = this.getInstituicaoIdentifier(instituicaoItem);
        if (instituicaoCollectionIdentifiers.includes(instituicaoIdentifier)) {
          return false;
        }
        instituicaoCollectionIdentifiers.push(instituicaoIdentifier);
        return true;
      });
      return [...instituicaosToAdd, ...instituicaoCollection];
    }
    return instituicaoCollection;
  }
}
