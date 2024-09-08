import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStatusDocumento, NewStatusDocumento } from '../status-documento.model';

export type PartialUpdateStatusDocumento = Partial<IStatusDocumento> & Pick<IStatusDocumento, 'id'>;

export type EntityResponseType = HttpResponse<IStatusDocumento>;
export type EntityArrayResponseType = HttpResponse<IStatusDocumento[]>;

@Injectable({ providedIn: 'root' })
export class StatusDocumentoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/status-documentos');

  create(statusDocumento: NewStatusDocumento): Observable<EntityResponseType> {
    return this.http.post<IStatusDocumento>(this.resourceUrl, statusDocumento, { observe: 'response' });
  }

  update(statusDocumento: IStatusDocumento): Observable<EntityResponseType> {
    return this.http.put<IStatusDocumento>(`${this.resourceUrl}/${this.getStatusDocumentoIdentifier(statusDocumento)}`, statusDocumento, {
      observe: 'response',
    });
  }

  partialUpdate(statusDocumento: PartialUpdateStatusDocumento): Observable<EntityResponseType> {
    return this.http.patch<IStatusDocumento>(`${this.resourceUrl}/${this.getStatusDocumentoIdentifier(statusDocumento)}`, statusDocumento, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IStatusDocumento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStatusDocumento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStatusDocumentoIdentifier(statusDocumento: Pick<IStatusDocumento, 'id'>): string {
    return statusDocumento.id;
  }

  compareStatusDocumento(o1: Pick<IStatusDocumento, 'id'> | null, o2: Pick<IStatusDocumento, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatusDocumentoIdentifier(o1) === this.getStatusDocumentoIdentifier(o2) : o1 === o2;
  }

  addStatusDocumentoToCollectionIfMissing<Type extends Pick<IStatusDocumento, 'id'>>(
    statusDocumentoCollection: Type[],
    ...statusDocumentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statusDocumentos: Type[] = statusDocumentosToCheck.filter(isPresent);
    if (statusDocumentos.length > 0) {
      const statusDocumentoCollectionIdentifiers = statusDocumentoCollection.map(statusDocumentoItem =>
        this.getStatusDocumentoIdentifier(statusDocumentoItem),
      );
      const statusDocumentosToAdd = statusDocumentos.filter(statusDocumentoItem => {
        const statusDocumentoIdentifier = this.getStatusDocumentoIdentifier(statusDocumentoItem);
        if (statusDocumentoCollectionIdentifiers.includes(statusDocumentoIdentifier)) {
          return false;
        }
        statusDocumentoCollectionIdentifiers.push(statusDocumentoIdentifier);
        return true;
      });
      return [...statusDocumentosToAdd, ...statusDocumentoCollection];
    }
    return statusDocumentoCollection;
  }
}
