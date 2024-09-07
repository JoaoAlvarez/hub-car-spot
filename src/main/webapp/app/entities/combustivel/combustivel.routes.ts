import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CombustivelResolve from './route/combustivel-routing-resolve.service';

const combustivelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/combustivel.component').then(m => m.CombustivelComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/combustivel-detail.component').then(m => m.CombustivelDetailComponent),
    resolve: {
      combustivel: CombustivelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/combustivel-update.component').then(m => m.CombustivelUpdateComponent),
    resolve: {
      combustivel: CombustivelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/combustivel-update.component').then(m => m.CombustivelUpdateComponent),
    resolve: {
      combustivel: CombustivelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default combustivelRoute;
