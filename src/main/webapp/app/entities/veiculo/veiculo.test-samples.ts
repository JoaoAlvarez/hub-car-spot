import dayjs from 'dayjs/esm';

import { IVeiculo, NewVeiculo } from './veiculo.model';

export const sampleWithRequiredData: IVeiculo = {
  id: '64454baa-5e87-4a80-8a31-c28a038d03b1',
  createdAt: dayjs('2024-09-07'),
  especie: 'CARRO',
  placa: 'incidentally',
  marca: 'lest',
  anoFabricacao: 24098,
  anoModelo: 1732,
  combustivel: 'ick hoover',
};

export const sampleWithPartialData: IVeiculo = {
  id: 'f7c3d159-a450-45a5-8b65-05057f62f986',
  createdAt: dayjs('2024-09-06'),
  especie: 'MOTO',
  placa: 'athwart wriggle although',
  marca: 'ragged and',
  anoFabricacao: 15061,
  anoModelo: 7768,
  cor: 'tame what dishonest',
  combustivel: 'funny',
  status: 'QUEBRADO',
  numeroMotor: 'possible',
  numeroCambio: 'dredger',
  kmSaida: 9846,
  cavalos: 'unibody basic amongst',
  descritivoCurtoAcessorios: 'aha boohoo too',
};

export const sampleWithFullData: IVeiculo = {
  id: '571dea1f-d90b-4af1-8a1d-bf9772c18d31',
  createdAt: dayjs('2024-09-07'),
  especie: 'PICKUP',
  placa: 'example',
  marca: 'wiggle',
  modelo: 'esteemed species extra-large',
  anoFabricacao: 17221,
  anoModelo: 22732,
  cor: 'loftily',
  combustivel: 'concert corduroy surgery',
  cambio: 'unlike',
  status: 'NOVO',
  chassi: 'disposal kidney grab',
  renavam: 'cleverly',
  numeroMotor: 'alongside',
  numeroCambio: 'by tuba',
  quilometraegem: 18148,
  kmSaida: 24743,
  cavalos: 'keen',
  motorizacao: 'bumpy utterly',
  adicional: 'control fake',
  descritivoCurtoAcessorios: 'dry',
};

export const sampleWithNewData: NewVeiculo = {
  createdAt: dayjs('2024-09-06'),
  especie: 'MOTO',
  placa: 'gah constant',
  marca: 'afore',
  anoFabricacao: 8724,
  anoModelo: 735,
  combustivel: 'which but',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
