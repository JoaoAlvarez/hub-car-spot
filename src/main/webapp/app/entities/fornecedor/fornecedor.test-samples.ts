import { IFornecedor, NewFornecedor } from './fornecedor.model';

export const sampleWithRequiredData: IFornecedor = {
  id: '7258064b-cf71-4f14-b3df-475b05360809',
  telefone: 'oof rewarding',
};

export const sampleWithPartialData: IFornecedor = {
  id: 'a4c8edb6-4ca5-4d5c-881c-bfe611878e57',
  cnpj: 'vainly devoted',
  telefone: 'for',
  endereco: 'gratefully gladly initialize',
  bairro: 'inwardly',
  uf: 'buzzing kitsch',
};

export const sampleWithFullData: IFornecedor = {
  id: '3a9fed51-498d-4504-9859-e52198a88a5d',
  nome: 'grown',
  cnpj: 'following fumbling mellow',
  telefone: 'baulk although warmly',
  cep: 'likewise though',
  endereco: 'unethically pfft unexpectedly',
  bairro: 'blah tragedy cappuccino',
  cidade: 'where',
  numero: 'scorpion authentic at',
  uf: 'supposing',
};

export const sampleWithNewData: NewFornecedor = {
  telefone: 'fast',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
