import dayjs from 'dayjs/esm';

import { ICompraVeiculo, NewCompraVeiculo } from './compra-veiculo.model';

export const sampleWithRequiredData: ICompraVeiculo = {
  id: '48c77c0e-8a71-4d34-a437-052c0d350e61',
  kmEntrada: 9610,
  valor: 7542.06,
  dataCompra: dayjs('2024-09-06'),
  condicaoPagamento: 'indeed of',
};

export const sampleWithPartialData: ICompraVeiculo = {
  id: 'deb357df-1197-439e-b806-84f781f17ff8',
  kmEntrada: 32523,
  valor: 637.95,
  valorEstimado: 13219.51,
  enderecoCrlv: 'hence near mysterious',
  cidadeCrlv: 'um',
  ufCrlv: 'dangerous',
  dataCompra: dayjs('2024-09-06'),
  condicaoPagamento: 'subexpression hm',
};

export const sampleWithFullData: ICompraVeiculo = {
  id: 'fd733148-994d-444f-b6ef-58afeef34e75',
  kmEntrada: 10339,
  valor: 11867.98,
  valorEstimado: 13028.9,
  enderecoCrlv: 'concerning that',
  cidadeCrlv: 'worse',
  ufCrlv: 'because except',
  cpfCrlv: 'cloves',
  dataCompra: dayjs('2024-09-06'),
  condicaoPagamento: 'disability bashfully verve',
  valorPago: 27448.95,
};

export const sampleWithNewData: NewCompraVeiculo = {
  kmEntrada: 13679,
  valor: 11470.68,
  dataCompra: dayjs('2024-09-07'),
  condicaoPagamento: 'now trash off',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
