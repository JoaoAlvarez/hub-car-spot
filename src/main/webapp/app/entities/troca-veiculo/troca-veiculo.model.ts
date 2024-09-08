import dayjs from 'dayjs/esm';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { IFilial } from 'app/entities/filial/filial.model';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface ITrocaVeiculo {
  id: string;
  carroEntradaId?: string | null;
  carroSaidaId?: string | null;
  dataTroca?: dayjs.Dayjs | null;
  condicaoPagamento?: string | null;
  valorPago?: number | null;
  valorRecebido?: number | null;
  veiculoEntrada?: IVeiculo | null;
  veiculoSaida?: IVeiculo | null;
  filial?: IFilial | null;
  instituicao?: IInstituicao | null;
}

export type NewTrocaVeiculo = Omit<ITrocaVeiculo, 'id'> & { id: null };
