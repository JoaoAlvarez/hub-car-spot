import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CompraVeiculoResolve from './route/compra-veiculo-routing-resolve.service';

const compraVeiculoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compra-veiculo.component').then(m => m.CompraVeiculoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compra-veiculo-detail.component').then(m => m.CompraVeiculoDetailComponent),
    resolve: {
      compraVeiculo: CompraVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compra-veiculo-update.component').then(m => m.CompraVeiculoUpdateComponent),
    resolve: {
      compraVeiculo: CompraVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compra-veiculo-update.component').then(m => m.CompraVeiculoUpdateComponent),
    resolve: {
      compraVeiculo: CompraVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default compraVeiculoRoute;
