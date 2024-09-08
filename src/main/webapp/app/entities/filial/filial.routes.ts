import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FilialResolve from './route/filial-routing-resolve.service';

const filialRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/filial.component').then(m => m.FilialComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/filial-detail.component').then(m => m.FilialDetailComponent),
    resolve: {
      filial: FilialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/filial-update.component').then(m => m.FilialUpdateComponent),
    resolve: {
      filial: FilialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/filial-update.component').then(m => m.FilialUpdateComponent),
    resolve: {
      filial: FilialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default filialRoute;
