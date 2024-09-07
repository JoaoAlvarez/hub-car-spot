import { IStatusDocumento, NewStatusDocumento } from './status-documento.model';

export const sampleWithRequiredData: IStatusDocumento = {
  id: '57e0e677-eff8-4a33-a141-95b1a85b9ded',
  instituicaoId: 'boom',
};

export const sampleWithPartialData: IStatusDocumento = {
  id: '92edd88e-b5ec-469d-9280-20a3a4a1ace4',
  instituicaoId: 'hit conflate',
  nome: 'hairy afterwards that',
};

export const sampleWithFullData: IStatusDocumento = {
  id: '44c113b0-8038-4b8c-b846-6b21f2438c04',
  instituicaoId: 'now clothe before',
  nome: 'eavesdropper',
};

export const sampleWithNewData: NewStatusDocumento = {
  instituicaoId: 'beyond right cleverly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
