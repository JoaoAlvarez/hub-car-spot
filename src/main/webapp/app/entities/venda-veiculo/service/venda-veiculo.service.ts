import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVendaVeiculo, NewVendaVeiculo } from '../venda-veiculo.model';

export type PartialUpdateVendaVeiculo = Partial<IVendaVeiculo> & Pick<IVendaVeiculo, 'id'>;

type RestOf<T extends IVendaVeiculo | NewVendaVeiculo> = Omit<T, 'dataVenda'> & {
  dataVenda?: string | null;
};

export type RestVendaVeiculo = RestOf<IVendaVeiculo>;

export type NewRestVendaVeiculo = RestOf<NewVendaVeiculo>;

export type PartialUpdateRestVendaVeiculo = RestOf<PartialUpdateVendaVeiculo>;

export type EntityResponseType = HttpResponse<IVendaVeiculo>;
export type EntityArrayResponseType = HttpResponse<IVendaVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class VendaVeiculoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/venda-veiculos');

  create(vendaVeiculo: NewVendaVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendaVeiculo);
    return this.http
      .post<RestVendaVeiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vendaVeiculo: IVendaVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendaVeiculo);
    return this.http
      .put<RestVendaVeiculo>(`${this.resourceUrl}/${this.getVendaVeiculoIdentifier(vendaVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vendaVeiculo: PartialUpdateVendaVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendaVeiculo);
    return this.http
      .patch<RestVendaVeiculo>(`${this.resourceUrl}/${this.getVendaVeiculoIdentifier(vendaVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestVendaVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVendaVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVendaVeiculoIdentifier(vendaVeiculo: Pick<IVendaVeiculo, 'id'>): string {
    return vendaVeiculo.id;
  }

  compareVendaVeiculo(o1: Pick<IVendaVeiculo, 'id'> | null, o2: Pick<IVendaVeiculo, 'id'> | null): boolean {
    return o1 && o2 ? this.getVendaVeiculoIdentifier(o1) === this.getVendaVeiculoIdentifier(o2) : o1 === o2;
  }

  addVendaVeiculoToCollectionIfMissing<Type extends Pick<IVendaVeiculo, 'id'>>(
    vendaVeiculoCollection: Type[],
    ...vendaVeiculosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vendaVeiculos: Type[] = vendaVeiculosToCheck.filter(isPresent);
    if (vendaVeiculos.length > 0) {
      const vendaVeiculoCollectionIdentifiers = vendaVeiculoCollection.map(vendaVeiculoItem =>
        this.getVendaVeiculoIdentifier(vendaVeiculoItem),
      );
      const vendaVeiculosToAdd = vendaVeiculos.filter(vendaVeiculoItem => {
        const vendaVeiculoIdentifier = this.getVendaVeiculoIdentifier(vendaVeiculoItem);
        if (vendaVeiculoCollectionIdentifiers.includes(vendaVeiculoIdentifier)) {
          return false;
        }
        vendaVeiculoCollectionIdentifiers.push(vendaVeiculoIdentifier);
        return true;
      });
      return [...vendaVeiculosToAdd, ...vendaVeiculoCollection];
    }
    return vendaVeiculoCollection;
  }

  protected convertDateFromClient<T extends IVendaVeiculo | NewVendaVeiculo | PartialUpdateVendaVeiculo>(vendaVeiculo: T): RestOf<T> {
    return {
      ...vendaVeiculo,
      dataVenda: vendaVeiculo.dataVenda?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVendaVeiculo: RestVendaVeiculo): IVendaVeiculo {
    return {
      ...restVendaVeiculo,
      dataVenda: restVendaVeiculo.dataVenda ? dayjs(restVendaVeiculo.dataVenda) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVendaVeiculo>): HttpResponse<IVendaVeiculo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVendaVeiculo[]>): HttpResponse<IVendaVeiculo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
