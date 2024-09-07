import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface ITaxas {
  id: string;
  nome?: string | null;
  valor?: number | null;
  instituicao?: IInstituicao | null;
}

export type NewTaxas = Omit<ITaxas, 'id'> & { id: null };
