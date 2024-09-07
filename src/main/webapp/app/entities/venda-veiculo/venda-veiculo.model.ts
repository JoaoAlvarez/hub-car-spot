import dayjs from 'dayjs/esm';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { IFilial } from 'app/entities/filial/filial.model';
import { IFinanceira } from 'app/entities/financeira/financeira.model';

export interface IVendaVeiculo {
  id: string;
  kmSaida?: number | null;
  valorCompra?: number | null;
  valorTabela?: number | null;
  valorVenda?: number | null;
  dataVenda?: dayjs.Dayjs | null;
  condicaoRecebimento?: string | null;
  valorEntrada?: number | null;
  valorFinanciado?: number | null;
  veiculo?: IVeiculo | null;
  instituicao?: IInstituicao | null;
  filial?: IFilial | null;
  financeira?: IFinanceira | null;
}

export type NewVendaVeiculo = Omit<IVendaVeiculo, 'id'> & { id: null };
