import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrocaVeiculo, NewTrocaVeiculo } from '../troca-veiculo.model';

export type PartialUpdateTrocaVeiculo = Partial<ITrocaVeiculo> & Pick<ITrocaVeiculo, 'id'>;

type RestOf<T extends ITrocaVeiculo | NewTrocaVeiculo> = Omit<T, 'dataTroca'> & {
  dataTroca?: string | null;
};

export type RestTrocaVeiculo = RestOf<ITrocaVeiculo>;

export type NewRestTrocaVeiculo = RestOf<NewTrocaVeiculo>;

export type PartialUpdateRestTrocaVeiculo = RestOf<PartialUpdateTrocaVeiculo>;

export type EntityResponseType = HttpResponse<ITrocaVeiculo>;
export type EntityArrayResponseType = HttpResponse<ITrocaVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class TrocaVeiculoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/troca-veiculos');

  create(trocaVeiculo: NewTrocaVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trocaVeiculo);
    return this.http
      .post<RestTrocaVeiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trocaVeiculo: ITrocaVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trocaVeiculo);
    return this.http
      .put<RestTrocaVeiculo>(`${this.resourceUrl}/${this.getTrocaVeiculoIdentifier(trocaVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trocaVeiculo: PartialUpdateTrocaVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trocaVeiculo);
    return this.http
      .patch<RestTrocaVeiculo>(`${this.resourceUrl}/${this.getTrocaVeiculoIdentifier(trocaVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestTrocaVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrocaVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrocaVeiculoIdentifier(trocaVeiculo: Pick<ITrocaVeiculo, 'id'>): string {
    return trocaVeiculo.id;
  }

  compareTrocaVeiculo(o1: Pick<ITrocaVeiculo, 'id'> | null, o2: Pick<ITrocaVeiculo, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrocaVeiculoIdentifier(o1) === this.getTrocaVeiculoIdentifier(o2) : o1 === o2;
  }

  addTrocaVeiculoToCollectionIfMissing<Type extends Pick<ITrocaVeiculo, 'id'>>(
    trocaVeiculoCollection: Type[],
    ...trocaVeiculosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trocaVeiculos: Type[] = trocaVeiculosToCheck.filter(isPresent);
    if (trocaVeiculos.length > 0) {
      const trocaVeiculoCollectionIdentifiers = trocaVeiculoCollection.map(trocaVeiculoItem =>
        this.getTrocaVeiculoIdentifier(trocaVeiculoItem),
      );
      const trocaVeiculosToAdd = trocaVeiculos.filter(trocaVeiculoItem => {
        const trocaVeiculoIdentifier = this.getTrocaVeiculoIdentifier(trocaVeiculoItem);
        if (trocaVeiculoCollectionIdentifiers.includes(trocaVeiculoIdentifier)) {
          return false;
        }
        trocaVeiculoCollectionIdentifiers.push(trocaVeiculoIdentifier);
        return true;
      });
      return [...trocaVeiculosToAdd, ...trocaVeiculoCollection];
    }
    return trocaVeiculoCollection;
  }

  protected convertDateFromClient<T extends ITrocaVeiculo | NewTrocaVeiculo | PartialUpdateTrocaVeiculo>(trocaVeiculo: T): RestOf<T> {
    return {
      ...trocaVeiculo,
      dataTroca: trocaVeiculo.dataTroca?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTrocaVeiculo: RestTrocaVeiculo): ITrocaVeiculo {
    return {
      ...restTrocaVeiculo,
      dataTroca: restTrocaVeiculo.dataTroca ? dayjs(restTrocaVeiculo.dataTroca) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrocaVeiculo>): HttpResponse<ITrocaVeiculo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrocaVeiculo[]>): HttpResponse<ITrocaVeiculo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
