import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import LocalResolve from './route/local-routing-resolve.service';

const localRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/local.component').then(m => m.LocalComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/local-detail.component').then(m => m.LocalDetailComponent),
    resolve: {
      local: LocalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/local-update.component').then(m => m.LocalUpdateComponent),
    resolve: {
      local: LocalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/local-update.component').then(m => m.LocalUpdateComponent),
    resolve: {
      local: LocalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default localRoute;
