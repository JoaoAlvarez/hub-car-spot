import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstituicao } from '../instituicao.model';
import { InstituicaoService } from '../service/instituicao.service';

const instituicaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IInstituicao> => {
  const id = route.params.id;
  if (id) {
    return inject(InstituicaoService)
      .find(id)
      .pipe(
        mergeMap((instituicao: HttpResponse<IInstituicao>) => {
          if (instituicao.body) {
            return of(instituicao.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default instituicaoResolve;
