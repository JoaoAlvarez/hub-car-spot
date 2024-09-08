import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'instituicao',
    data: { pageTitle: 'Instituicaos' },
    loadChildren: () => import('./instituicao/instituicao.routes'),
  },
  {
    path: 'usuario-instituicao',
    data: { pageTitle: 'UsuarioInstituicaos' },
    loadChildren: () => import('./usuario-instituicao/usuario-instituicao.routes'),
  },
  {
    path: 'fornecedor',
    data: { pageTitle: 'Fornecedors' },
    loadChildren: () => import('./fornecedor/fornecedor.routes'),
  },
  {
    path: 'financeira',
    data: { pageTitle: 'Financeiras' },
    loadChildren: () => import('./financeira/financeira.routes'),
  },
  {
    path: 'taxas',
    data: { pageTitle: 'Taxas' },
    loadChildren: () => import('./taxas/taxas.routes'),
  },
  {
    path: 'combustivel',
    data: { pageTitle: 'Combustivels' },
    loadChildren: () => import('./combustivel/combustivel.routes'),
  },
  {
    path: 'marca',
    data: { pageTitle: 'Marcas' },
    loadChildren: () => import('./marca/marca.routes'),
  },
  {
    path: 'status-documento',
    data: { pageTitle: 'StatusDocumentos' },
    loadChildren: () => import('./status-documento/status-documento.routes'),
  },
  {
    path: 'local',
    data: { pageTitle: 'Locals' },
    loadChildren: () => import('./local/local.routes'),
  },
  {
    path: 'filial',
    data: { pageTitle: 'Filials' },
    loadChildren: () => import('./filial/filial.routes'),
  },
  {
    path: 'compra-veiculo',
    data: { pageTitle: 'CompraVeiculos' },
    loadChildren: () => import('./compra-veiculo/compra-veiculo.routes'),
  },
  {
    path: 'venda-veiculo',
    data: { pageTitle: 'VendaVeiculos' },
    loadChildren: () => import('./venda-veiculo/venda-veiculo.routes'),
  },
  {
    path: 'troca-veiculo',
    data: { pageTitle: 'TrocaVeiculos' },
    loadChildren: () => import('./troca-veiculo/troca-veiculo.routes'),
  },
  {
    path: 'veiculo',
    data: { pageTitle: 'Veiculos' },
    loadChildren: () => import('./veiculo/veiculo.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
