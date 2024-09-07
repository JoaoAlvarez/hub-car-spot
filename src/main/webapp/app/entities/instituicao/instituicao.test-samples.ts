import { IInstituicao, NewInstituicao } from './instituicao.model';

export const sampleWithRequiredData: IInstituicao = {
  id: 'c7ababb4-9939-4076-96c8-d44d9decc4a1',
  telefone: 'excitedly fancy flake',
};

export const sampleWithPartialData: IInstituicao = {
  id: 'a644a293-7ad5-4990-8926-fc16c37e8656',
  telefone: 'afore heavenly',
  cep: 'kiddingly but',
  bairro: 'stencil like till',
  cidade: 'gee deceivingly',
  numero: 'dimly fast',
};

export const sampleWithFullData: IInstituicao = {
  id: '220ede19-250c-45d9-a1e8-00493f1f7bf6',
  nome: 'deliberately ballot molecule',
  telefone: 'mayonnaise versus efface',
  cnpj: 'giant to rule',
  cep: 'incidentally sap',
  endereco: 'offensively',
  bairro: 'duh',
  cidade: 'bother phony',
  numero: 'which clipboard small',
  uf: 'hateful ounce',
  complemento: 'fishing',
};

export const sampleWithNewData: NewInstituicao = {
  telefone: 'amid',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
