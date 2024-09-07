import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStatusDocumento } from '../status-documento.model';
import { StatusDocumentoService } from '../service/status-documento.service';

const statusDocumentoResolve = (route: ActivatedRouteSnapshot): Observable<null | IStatusDocumento> => {
  const id = route.params.id;
  if (id) {
    return inject(StatusDocumentoService)
      .find(id)
      .pipe(
        mergeMap((statusDocumento: HttpResponse<IStatusDocumento>) => {
          if (statusDocumento.body) {
            return of(statusDocumento.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default statusDocumentoResolve;
