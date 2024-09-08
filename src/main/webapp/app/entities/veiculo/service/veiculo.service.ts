import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVeiculo, NewVeiculo } from '../veiculo.model';

export type PartialUpdateVeiculo = Partial<IVeiculo> & Pick<IVeiculo, 'id'>;

type RestOf<T extends IVeiculo | NewVeiculo> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestVeiculo = RestOf<IVeiculo>;

export type NewRestVeiculo = RestOf<NewVeiculo>;

export type PartialUpdateRestVeiculo = RestOf<PartialUpdateVeiculo>;

export type EntityResponseType = HttpResponse<IVeiculo>;
export type EntityArrayResponseType = HttpResponse<IVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/veiculos');

  create(veiculo: NewVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(veiculo);
    return this.http
      .post<RestVeiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(veiculo: IVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(veiculo);
    return this.http
      .put<RestVeiculo>(`${this.resourceUrl}/${this.getVeiculoIdentifier(veiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(veiculo: PartialUpdateVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(veiculo);
    return this.http
      .patch<RestVeiculo>(`${this.resourceUrl}/${this.getVeiculoIdentifier(veiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVeiculoIdentifier(veiculo: Pick<IVeiculo, 'id'>): string {
    return veiculo.id;
  }

  compareVeiculo(o1: Pick<IVeiculo, 'id'> | null, o2: Pick<IVeiculo, 'id'> | null): boolean {
    return o1 && o2 ? this.getVeiculoIdentifier(o1) === this.getVeiculoIdentifier(o2) : o1 === o2;
  }

  addVeiculoToCollectionIfMissing<Type extends Pick<IVeiculo, 'id'>>(
    veiculoCollection: Type[],
    ...veiculosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const veiculos: Type[] = veiculosToCheck.filter(isPresent);
    if (veiculos.length > 0) {
      const veiculoCollectionIdentifiers = veiculoCollection.map(veiculoItem => this.getVeiculoIdentifier(veiculoItem));
      const veiculosToAdd = veiculos.filter(veiculoItem => {
        const veiculoIdentifier = this.getVeiculoIdentifier(veiculoItem);
        if (veiculoCollectionIdentifiers.includes(veiculoIdentifier)) {
          return false;
        }
        veiculoCollectionIdentifiers.push(veiculoIdentifier);
        return true;
      });
      return [...veiculosToAdd, ...veiculoCollection];
    }
    return veiculoCollection;
  }

  protected convertDateFromClient<T extends IVeiculo | NewVeiculo | PartialUpdateVeiculo>(veiculo: T): RestOf<T> {
    return {
      ...veiculo,
      createdAt: veiculo.createdAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVeiculo: RestVeiculo): IVeiculo {
    return {
      ...restVeiculo,
      createdAt: restVeiculo.createdAt ? dayjs(restVeiculo.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVeiculo>): HttpResponse<IVeiculo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVeiculo[]>): HttpResponse<IVeiculo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
