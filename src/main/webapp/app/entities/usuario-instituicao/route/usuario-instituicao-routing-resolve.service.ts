import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioInstituicao } from '../usuario-instituicao.model';
import { UsuarioInstituicaoService } from '../service/usuario-instituicao.service';

const usuarioInstituicaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IUsuarioInstituicao> => {
  const id = route.params.id;
  if (id) {
    return inject(UsuarioInstituicaoService)
      .find(id)
      .pipe(
        mergeMap((usuarioInstituicao: HttpResponse<IUsuarioInstituicao>) => {
          if (usuarioInstituicao.body) {
            return of(usuarioInstituicao.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default usuarioInstituicaoResolve;
