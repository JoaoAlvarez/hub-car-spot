import dayjs from 'dayjs/esm';

import { ITrocaVeiculo, NewTrocaVeiculo } from './troca-veiculo.model';

export const sampleWithRequiredData: ITrocaVeiculo = {
  id: 'f352613c-fac8-4b61-a998-17c9ba6a5e3d',
  carroEntradaId: 'lest pleasant',
  carroSaidaId: 'slimy',
  dataTroca: dayjs('2024-09-07'),
};

export const sampleWithPartialData: ITrocaVeiculo = {
  id: '5ede156e-2533-4cdc-8177-d8208101b2da',
  carroEntradaId: 'hope inasmuch',
  carroSaidaId: 'nod over instead',
  dataTroca: dayjs('2024-09-07'),
  valorRecebido: 18479.41,
};

export const sampleWithFullData: ITrocaVeiculo = {
  id: 'f5e93883-ed36-4640-b719-07ec834bf4ee',
  carroEntradaId: 'woot',
  carroSaidaId: 'smoothly imaginative',
  dataTroca: dayjs('2024-09-07'),
  condicaoPagamento: 'knowledgeably',
  valorPago: 10529.74,
  valorRecebido: 26502.62,
};

export const sampleWithNewData: NewTrocaVeiculo = {
  carroEntradaId: 'owl alternate',
  carroSaidaId: 'will',
  dataTroca: dayjs('2024-09-07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
