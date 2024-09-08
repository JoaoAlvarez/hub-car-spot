import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IUsuarioInstituicao, NewUsuarioInstituicao } from '../usuario-instituicao.model';

export type PartialUpdateUsuarioInstituicao = Partial<IUsuarioInstituicao> & Pick<IUsuarioInstituicao, 'id'>;

export type EntityResponseType = HttpResponse<IUsuarioInstituicao>;
export type EntityArrayResponseType = HttpResponse<IUsuarioInstituicao[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioInstituicaoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-instituicaos');

  create(usuarioInstituicao: NewUsuarioInstituicao): Observable<EntityResponseType> {
    return this.http.post<IUsuarioInstituicao>(this.resourceUrl, usuarioInstituicao, { observe: 'response' });
  }

  update(usuarioInstituicao: IUsuarioInstituicao): Observable<EntityResponseType> {
    return this.http.put<IUsuarioInstituicao>(
      `${this.resourceUrl}/${this.getUsuarioInstituicaoIdentifier(usuarioInstituicao)}`,
      usuarioInstituicao,
      { observe: 'response' },
    );
  }

  partialUpdate(usuarioInstituicao: PartialUpdateUsuarioInstituicao): Observable<EntityResponseType> {
    return this.http.patch<IUsuarioInstituicao>(
      `${this.resourceUrl}/${this.getUsuarioInstituicaoIdentifier(usuarioInstituicao)}`,
      usuarioInstituicao,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IUsuarioInstituicao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  listarInstiuicoes(username: string): Observable<EntityArrayResponseType> {
    return this.http.get<IUsuarioInstituicao[]>(`${this.resourceUrl}/instituicoes/${username}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUsuarioInstituicao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUsuarioInstituicaoIdentifier(usuarioInstituicao: Pick<IUsuarioInstituicao, 'id'>): string {
    return usuarioInstituicao.id;
  }

  compareUsuarioInstituicao(o1: Pick<IUsuarioInstituicao, 'id'> | null, o2: Pick<IUsuarioInstituicao, 'id'> | null): boolean {
    return o1 && o2 ? this.getUsuarioInstituicaoIdentifier(o1) === this.getUsuarioInstituicaoIdentifier(o2) : o1 === o2;
  }

  addUsuarioInstituicaoToCollectionIfMissing<Type extends Pick<IUsuarioInstituicao, 'id'>>(
    usuarioInstituicaoCollection: Type[],
    ...usuarioInstituicaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const usuarioInstituicaos: Type[] = usuarioInstituicaosToCheck.filter(isPresent);
    if (usuarioInstituicaos.length > 0) {
      const usuarioInstituicaoCollectionIdentifiers = usuarioInstituicaoCollection.map(usuarioInstituicaoItem =>
        this.getUsuarioInstituicaoIdentifier(usuarioInstituicaoItem),
      );
      const usuarioInstituicaosToAdd = usuarioInstituicaos.filter(usuarioInstituicaoItem => {
        const usuarioInstituicaoIdentifier = this.getUsuarioInstituicaoIdentifier(usuarioInstituicaoItem);
        if (usuarioInstituicaoCollectionIdentifiers.includes(usuarioInstituicaoIdentifier)) {
          return false;
        }
        usuarioInstituicaoCollectionIdentifiers.push(usuarioInstituicaoIdentifier);
        return true;
      });
      return [...usuarioInstituicaosToAdd, ...usuarioInstituicaoCollection];
    }
    return usuarioInstituicaoCollection;
  }
}
