import { ILocal, NewLocal } from './local.model';

export const sampleWithRequiredData: ILocal = {
  id: '9fffddfe-c4f0-47ae-b0ec-95fcee815618',
};

export const sampleWithPartialData: ILocal = {
  id: '9aca634c-1501-465c-abc9-457fc87cc427',
  nome: 'bless potentially',
};

export const sampleWithFullData: ILocal = {
  id: '01d02c37-29a5-45e4-b150-8b84fc4a585d',
  nome: 'whereas',
};

export const sampleWithNewData: NewLocal = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
