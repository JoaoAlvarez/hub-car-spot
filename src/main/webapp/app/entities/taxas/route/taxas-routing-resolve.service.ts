import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaxas } from '../taxas.model';
import { TaxasService } from '../service/taxas.service';

const taxasResolve = (route: ActivatedRouteSnapshot): Observable<null | ITaxas> => {
  const id = route.params.id;
  if (id) {
    return inject(TaxasService)
      .find(id)
      .pipe(
        mergeMap((taxas: HttpResponse<ITaxas>) => {
          if (taxas.body) {
            return of(taxas.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default taxasResolve;
