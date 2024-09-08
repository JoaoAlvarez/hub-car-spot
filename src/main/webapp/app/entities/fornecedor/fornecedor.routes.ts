import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FornecedorResolve from './route/fornecedor-routing-resolve.service';

const fornecedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fornecedor.component').then(m => m.FornecedorComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fornecedor-detail.component').then(m => m.FornecedorDetailComponent),
    resolve: {
      fornecedor: FornecedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fornecedor-update.component').then(m => m.FornecedorUpdateComponent),
    resolve: {
      fornecedor: FornecedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fornecedor-update.component').then(m => m.FornecedorUpdateComponent),
    resolve: {
      fornecedor: FornecedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fornecedorRoute;
