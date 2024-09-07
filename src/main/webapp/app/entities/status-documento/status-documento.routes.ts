import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import StatusDocumentoResolve from './route/status-documento-routing-resolve.service';

const statusDocumentoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/status-documento.component').then(m => m.StatusDocumentoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/status-documento-detail.component').then(m => m.StatusDocumentoDetailComponent),
    resolve: {
      statusDocumento: StatusDocumentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/status-documento-update.component').then(m => m.StatusDocumentoUpdateComponent),
    resolve: {
      statusDocumento: StatusDocumentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/status-documento-update.component').then(m => m.StatusDocumentoUpdateComponent),
    resolve: {
      statusDocumento: StatusDocumentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default statusDocumentoRoute;
