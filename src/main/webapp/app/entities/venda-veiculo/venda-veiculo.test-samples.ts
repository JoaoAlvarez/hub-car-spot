import dayjs from 'dayjs/esm';

import { IVendaVeiculo, NewVendaVeiculo } from './venda-veiculo.model';

export const sampleWithRequiredData: IVendaVeiculo = {
  id: 'ff16ca29-727a-41e0-879e-d6d48446e065',
  kmSaida: 4945,
  valorCompra: 13358.31,
  valorVenda: 10936.39,
  dataVenda: dayjs('2024-09-07'),
  condicaoRecebimento: 'except modulo',
};

export const sampleWithPartialData: IVendaVeiculo = {
  id: '14c8c345-f8fd-4ea6-9908-e9af35c22449',
  kmSaida: 21730,
  valorCompra: 21509.43,
  valorTabela: 644.12,
  valorVenda: 7330.86,
  dataVenda: dayjs('2024-09-07'),
  condicaoRecebimento: 'fooey coolly',
};

export const sampleWithFullData: IVendaVeiculo = {
  id: '9dc740b0-2a85-49ae-8cc3-d2c30eb1421f',
  kmSaida: 7891,
  valorCompra: 29390.53,
  valorTabela: 5134.64,
  valorVenda: 29069.89,
  dataVenda: dayjs('2024-09-06'),
  condicaoRecebimento: 'till',
  valorEntrada: 30069.64,
  valorFinanciado: 21924.12,
};

export const sampleWithNewData: NewVendaVeiculo = {
  kmSaida: 3084,
  valorCompra: 4960.27,
  valorVenda: 1498.99,
  dataVenda: dayjs('2024-09-06'),
  condicaoRecebimento: 'animated productive gah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
