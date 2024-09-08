import { IFilial, NewFilial } from './filial.model';

export const sampleWithRequiredData: IFilial = {
  id: '52e7079a-46db-4499-a3e9-407746e22256',
  telefone: 'along phooey afore',
};

export const sampleWithPartialData: IFilial = {
  id: '4e32afc0-7943-4e85-bc7e-4179bf58a15a',
  telefone: 'scene down',
  cnpj: 'quaint',
  cep: 'smoothly church sidecar',
  uf: 'that pace along',
};

export const sampleWithFullData: IFilial = {
  id: 'e721d419-ea8b-42c9-b4cc-d4685d17a473',
  nome: 'prosperity sometimes scratch',
  telefone: 'of loosen',
  cnpj: 'like clavicle',
  cep: 'rent',
  endereco: 'indeed gaiters staid',
  bairro: 'inure',
  cidade: 'yum and officially',
  numero: 'beneath fast',
  uf: 'fortunately stamina',
};

export const sampleWithNewData: NewFilial = {
  telefone: 'up control inasmuch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
