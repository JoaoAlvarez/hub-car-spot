import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompraVeiculo } from '../compra-veiculo.model';
import { CompraVeiculoService } from '../service/compra-veiculo.service';

const compraVeiculoResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompraVeiculo> => {
  const id = route.params.id;
  if (id) {
    return inject(CompraVeiculoService)
      .find(id)
      .pipe(
        mergeMap((compraVeiculo: HttpResponse<ICompraVeiculo>) => {
          if (compraVeiculo.body) {
            return of(compraVeiculo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default compraVeiculoResolve;
