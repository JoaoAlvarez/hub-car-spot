import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompraVeiculo, NewCompraVeiculo } from '../compra-veiculo.model';

export type PartialUpdateCompraVeiculo = Partial<ICompraVeiculo> & Pick<ICompraVeiculo, 'id'>;

type RestOf<T extends ICompraVeiculo | NewCompraVeiculo> = Omit<T, 'dataCompra'> & {
  dataCompra?: string | null;
};

export type RestCompraVeiculo = RestOf<ICompraVeiculo>;

export type NewRestCompraVeiculo = RestOf<NewCompraVeiculo>;

export type PartialUpdateRestCompraVeiculo = RestOf<PartialUpdateCompraVeiculo>;

export type EntityResponseType = HttpResponse<ICompraVeiculo>;
export type EntityArrayResponseType = HttpResponse<ICompraVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class CompraVeiculoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compra-veiculos');

  create(compraVeiculo: NewCompraVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compraVeiculo);
    return this.http
      .post<RestCompraVeiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(compraVeiculo: ICompraVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compraVeiculo);
    return this.http
      .put<RestCompraVeiculo>(`${this.resourceUrl}/${this.getCompraVeiculoIdentifier(compraVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(compraVeiculo: PartialUpdateCompraVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compraVeiculo);
    return this.http
      .patch<RestCompraVeiculo>(`${this.resourceUrl}/${this.getCompraVeiculoIdentifier(compraVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestCompraVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCompraVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompraVeiculoIdentifier(compraVeiculo: Pick<ICompraVeiculo, 'id'>): string {
    return compraVeiculo.id;
  }

  compareCompraVeiculo(o1: Pick<ICompraVeiculo, 'id'> | null, o2: Pick<ICompraVeiculo, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompraVeiculoIdentifier(o1) === this.getCompraVeiculoIdentifier(o2) : o1 === o2;
  }

  addCompraVeiculoToCollectionIfMissing<Type extends Pick<ICompraVeiculo, 'id'>>(
    compraVeiculoCollection: Type[],
    ...compraVeiculosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compraVeiculos: Type[] = compraVeiculosToCheck.filter(isPresent);
    if (compraVeiculos.length > 0) {
      const compraVeiculoCollectionIdentifiers = compraVeiculoCollection.map(compraVeiculoItem =>
        this.getCompraVeiculoIdentifier(compraVeiculoItem),
      );
      const compraVeiculosToAdd = compraVeiculos.filter(compraVeiculoItem => {
        const compraVeiculoIdentifier = this.getCompraVeiculoIdentifier(compraVeiculoItem);
        if (compraVeiculoCollectionIdentifiers.includes(compraVeiculoIdentifier)) {
          return false;
        }
        compraVeiculoCollectionIdentifiers.push(compraVeiculoIdentifier);
        return true;
      });
      return [...compraVeiculosToAdd, ...compraVeiculoCollection];
    }
    return compraVeiculoCollection;
  }

  protected convertDateFromClient<T extends ICompraVeiculo | NewCompraVeiculo | PartialUpdateCompraVeiculo>(compraVeiculo: T): RestOf<T> {
    return {
      ...compraVeiculo,
      dataCompra: compraVeiculo.dataCompra?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCompraVeiculo: RestCompraVeiculo): ICompraVeiculo {
    return {
      ...restCompraVeiculo,
      dataCompra: restCompraVeiculo.dataCompra ? dayjs(restCompraVeiculo.dataCompra) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCompraVeiculo>): HttpResponse<ICompraVeiculo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCompraVeiculo[]>): HttpResponse<ICompraVeiculo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
