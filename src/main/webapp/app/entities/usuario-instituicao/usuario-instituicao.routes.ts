import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import UsuarioInstituicaoResolve from './route/usuario-instituicao-routing-resolve.service';

const usuarioInstituicaoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/usuario-instituicao.component').then(m => m.UsuarioInstituicaoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/usuario-instituicao-detail.component').then(m => m.UsuarioInstituicaoDetailComponent),
    resolve: {
      usuarioInstituicao: UsuarioInstituicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/usuario-instituicao-update.component').then(m => m.UsuarioInstituicaoUpdateComponent),
    resolve: {
      usuarioInstituicao: UsuarioInstituicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/usuario-instituicao-update.component').then(m => m.UsuarioInstituicaoUpdateComponent),
    resolve: {
      usuarioInstituicao: UsuarioInstituicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default usuarioInstituicaoRoute;
