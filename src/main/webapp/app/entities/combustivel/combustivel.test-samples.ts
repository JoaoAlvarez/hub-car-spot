import { ICombustivel, NewCombustivel } from './combustivel.model';

export const sampleWithRequiredData: ICombustivel = {
  id: 'c9054e26-68b0-47b0-b017-5d2f331ff935',
};

export const sampleWithPartialData: ICombustivel = {
  id: '1ac4a0af-5fa7-4bd2-ab33-078bc8386012',
};

export const sampleWithFullData: ICombustivel = {
  id: '4939612b-4c5d-474e-929f-b911ab187be8',
  nome: 'modulo times till',
};

export const sampleWithNewData: NewCombustivel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
