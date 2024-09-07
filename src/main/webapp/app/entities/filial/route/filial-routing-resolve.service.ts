import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFilial } from '../filial.model';
import { FilialService } from '../service/filial.service';

const filialResolve = (route: ActivatedRouteSnapshot): Observable<null | IFilial> => {
  const id = route.params.id;
  if (id) {
    return inject(FilialService)
      .find(id)
      .pipe(
        mergeMap((filial: HttpResponse<IFilial>) => {
          if (filial.body) {
            return of(filial.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default filialResolve;
