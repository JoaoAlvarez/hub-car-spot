import { IMarca, NewMarca } from './marca.model';

export const sampleWithRequiredData: IMarca = {
  id: '1f2a6569-14b2-4a4e-8c37-8a7dcd344c18',
};

export const sampleWithPartialData: IMarca = {
  id: 'ad084a62-4aae-4252-8ba5-4048adac1c85',
};

export const sampleWithFullData: IMarca = {
  id: 'c5aacdf4-d220-46a3-a43a-b64e2d406a1d',
  nome: 'oof fully and',
};

export const sampleWithNewData: NewMarca = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
