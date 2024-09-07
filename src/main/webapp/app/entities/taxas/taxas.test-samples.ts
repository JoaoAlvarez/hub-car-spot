import { ITaxas, NewTaxas } from './taxas.model';

export const sampleWithRequiredData: ITaxas = {
  id: '1c8eeb34-d3f4-4a67-9693-f445d8d36901',
};

export const sampleWithPartialData: ITaxas = {
  id: '67259e0a-c4a4-4419-881a-05f55f6c9e7e',
};

export const sampleWithFullData: ITaxas = {
  id: '41f3f9f7-58f0-41c2-bffc-cc75008f183c',
  nome: 'understated uselessly repentant',
  valor: 4586.11,
};

export const sampleWithNewData: NewTaxas = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
