import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVendaVeiculo } from '../venda-veiculo.model';
import { VendaVeiculoService } from '../service/venda-veiculo.service';

const vendaVeiculoResolve = (route: ActivatedRouteSnapshot): Observable<null | IVendaVeiculo> => {
  const id = route.params.id;
  if (id) {
    return inject(VendaVeiculoService)
      .find(id)
      .pipe(
        mergeMap((vendaVeiculo: HttpResponse<IVendaVeiculo>) => {
          if (vendaVeiculo.body) {
            return of(vendaVeiculo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vendaVeiculoResolve;
