import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import VendaVeiculoResolve from './route/venda-veiculo-routing-resolve.service';

const vendaVeiculoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/venda-veiculo.component').then(m => m.VendaVeiculoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/venda-veiculo-detail.component').then(m => m.VendaVeiculoDetailComponent),
    resolve: {
      vendaVeiculo: VendaVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/venda-veiculo-update.component').then(m => m.VendaVeiculoUpdateComponent),
    resolve: {
      vendaVeiculo: VendaVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/venda-veiculo-update.component').then(m => m.VendaVeiculoUpdateComponent),
    resolve: {
      vendaVeiculo: VendaVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vendaVeiculoRoute;
