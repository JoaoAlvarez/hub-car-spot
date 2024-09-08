import dayjs from 'dayjs/esm';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { IFilial } from 'app/entities/filial/filial.model';

export interface ICompraVeiculo {
  id: string;
  kmEntrada?: number | null;
  valor?: number | null;
  valorEstimado?: number | null;
  enderecoCrlv?: string | null;
  cidadeCrlv?: string | null;
  ufCrlv?: string | null;
  cpfCrlv?: string | null;
  dataCompra?: dayjs.Dayjs | null;
  condicaoPagamento?: string | null;
  valorPago?: number | null;
  instituicao?: IInstituicao | null;
  veiculo?: IVeiculo | null;
  filial?: IFilial | null;
}

export type NewCompraVeiculo = Omit<ICompraVeiculo, 'id'> & { id: null };
