import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import VeiculoResolve from './route/veiculo-routing-resolve.service';

const veiculoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/veiculo.component').then(m => m.VeiculoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/veiculo-detail.component').then(m => m.VeiculoDetailComponent),
    resolve: {
      veiculo: VeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/veiculo-update.component').then(m => m.VeiculoUpdateComponent),
    resolve: {
      veiculo: VeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/veiculo-update.component').then(m => m.VeiculoUpdateComponent),
    resolve: {
      veiculo: VeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default veiculoRoute;
