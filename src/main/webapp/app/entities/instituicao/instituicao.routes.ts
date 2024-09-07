import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import InstituicaoResolve from './route/instituicao-routing-resolve.service';

const instituicaoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/instituicao.component').then(m => m.InstituicaoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/instituicao-detail.component').then(m => m.InstituicaoDetailComponent),
    resolve: {
      instituicao: InstituicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/instituicao-update.component').then(m => m.InstituicaoUpdateComponent),
    resolve: {
      instituicao: InstituicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/instituicao-update.component').then(m => m.InstituicaoUpdateComponent),
    resolve: {
      instituicao: InstituicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default instituicaoRoute;
