import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FinanceiraResolve from './route/financeira-routing-resolve.service';

const financeiraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/financeira.component').then(m => m.FinanceiraComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/financeira-detail.component').then(m => m.FinanceiraDetailComponent),
    resolve: {
      financeira: FinanceiraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/financeira-update.component').then(m => m.FinanceiraUpdateComponent),
    resolve: {
      financeira: FinanceiraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/financeira-update.component').then(m => m.FinanceiraUpdateComponent),
    resolve: {
      financeira: FinanceiraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default financeiraRoute;
