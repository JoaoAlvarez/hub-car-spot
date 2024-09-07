import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinanceira } from '../financeira.model';
import { FinanceiraService } from '../service/financeira.service';

const financeiraResolve = (route: ActivatedRouteSnapshot): Observable<null | IFinanceira> => {
  const id = route.params.id;
  if (id) {
    return inject(FinanceiraService)
      .find(id)
      .pipe(
        mergeMap((financeira: HttpResponse<IFinanceira>) => {
          if (financeira.body) {
            return of(financeira.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default financeiraResolve;
