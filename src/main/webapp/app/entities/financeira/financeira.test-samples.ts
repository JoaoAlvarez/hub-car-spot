import { IFinanceira, NewFinanceira } from './financeira.model';

export const sampleWithRequiredData: IFinanceira = {
  id: '3e536005-ac42-4945-a6e3-0774f9f8ed65',
  telefone: 'hurtful gee',
};

export const sampleWithPartialData: IFinanceira = {
  id: '0ba7e705-d7c6-4001-b22b-796b1f397e00',
  telefone: 'athwart quaintly',
  endereco: 'yahoo melon boo',
  bairro: 'stable',
  numero: 'yuck pfft brr',
};

export const sampleWithFullData: IFinanceira = {
  id: 'cf1f8284-cd61-4865-b7ed-ece89d624347',
  nome: 'lively question',
  telefone: 'unless despite ew',
  cnpj: 'inasmuch catacomb',
  cep: 'as whenever',
  endereco: 'deeply and',
  bairro: 'reassuringly oh',
  cidade: 'failing',
  numero: 'towards',
  uf: 'doll',
};

export const sampleWithNewData: NewFinanceira = {
  telefone: 'preen yahoo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
