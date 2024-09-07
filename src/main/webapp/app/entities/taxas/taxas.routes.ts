import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import TaxasResolve from './route/taxas-routing-resolve.service';

const taxasRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/taxas.component').then(m => m.TaxasComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/taxas-detail.component').then(m => m.TaxasDetailComponent),
    resolve: {
      taxas: TaxasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/taxas-update.component').then(m => m.TaxasUpdateComponent),
    resolve: {
      taxas: TaxasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/taxas-update.component').then(m => m.TaxasUpdateComponent),
    resolve: {
      taxas: TaxasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default taxasRoute;
