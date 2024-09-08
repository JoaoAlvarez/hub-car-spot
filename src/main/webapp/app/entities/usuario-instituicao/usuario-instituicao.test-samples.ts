import { IUsuarioInstituicao, NewUsuarioInstituicao } from './usuario-instituicao.model';

export const sampleWithRequiredData: IUsuarioInstituicao = {
  id: '86a93b4c-a765-4c04-b2bc-37317afcdd02',
  identificador: 'and abrogation',
  isMaster: true,
  role: 'gondola for impression',
  read: true,
  write: false,
  update: true,
};

export const sampleWithPartialData: IUsuarioInstituicao = {
  id: 'b6786b84-1219-4af8-b44e-fb0e6a12d642',
  identificador: 'inside',
  isMaster: false,
  role: 'for',
  read: false,
  write: false,
  update: false,
};

export const sampleWithFullData: IUsuarioInstituicao = {
  id: 'c9105414-0c33-4a16-997b-e5c2c029372b',
  identificador: 'sometimes because',
  isMaster: false,
  role: 'whenever past unacceptable',
  read: false,
  write: false,
  update: true,
};

export const sampleWithNewData: NewUsuarioInstituicao = {
  identificador: 'indeed',
  isMaster: false,
  role: 'snarling roughly bah',
  read: true,
  write: false,
  update: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
