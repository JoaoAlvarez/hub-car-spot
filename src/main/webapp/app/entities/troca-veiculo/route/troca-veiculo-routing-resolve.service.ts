import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrocaVeiculo } from '../troca-veiculo.model';
import { TrocaVeiculoService } from '../service/troca-veiculo.service';

const trocaVeiculoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrocaVeiculo> => {
  const id = route.params.id;
  if (id) {
    return inject(TrocaVeiculoService)
      .find(id)
      .pipe(
        mergeMap((trocaVeiculo: HttpResponse<ITrocaVeiculo>) => {
          if (trocaVeiculo.body) {
            return of(trocaVeiculo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trocaVeiculoResolve;
