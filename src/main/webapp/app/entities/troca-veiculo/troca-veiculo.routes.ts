import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import TrocaVeiculoResolve from './route/troca-veiculo-routing-resolve.service';

const trocaVeiculoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/troca-veiculo.component').then(m => m.TrocaVeiculoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/troca-veiculo-detail.component').then(m => m.TrocaVeiculoDetailComponent),
    resolve: {
      trocaVeiculo: TrocaVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/troca-veiculo-update.component').then(m => m.TrocaVeiculoUpdateComponent),
    resolve: {
      trocaVeiculo: TrocaVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/troca-veiculo-update.component').then(m => m.TrocaVeiculoUpdateComponent),
    resolve: {
      trocaVeiculo: TrocaVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trocaVeiculoRoute;
