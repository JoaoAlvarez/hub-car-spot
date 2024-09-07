import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface ILocal {
  id: string;
  nome?: string | null;
  instituicao?: IInstituicao | null;
}

export type NewLocal = Omit<ILocal, 'id'> & { id: null };
