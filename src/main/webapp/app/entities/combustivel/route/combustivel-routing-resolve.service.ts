import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICombustivel } from '../combustivel.model';
import { CombustivelService } from '../service/combustivel.service';

const combustivelResolve = (route: ActivatedRouteSnapshot): Observable<null | ICombustivel> => {
  const id = route.params.id;
  if (id) {
    return inject(CombustivelService)
      .find(id)
      .pipe(
        mergeMap((combustivel: HttpResponse<ICombustivel>) => {
          if (combustivel.body) {
            return of(combustivel.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default combustivelResolve;
