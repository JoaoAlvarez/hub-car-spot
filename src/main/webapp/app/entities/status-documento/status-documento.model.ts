import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface IStatusDocumento {
  id: string;
  instituicaoId?: string | null;
  nome?: string | null;
  instituicao?: IInstituicao | null;
}

export type NewStatusDocumento = Omit<IStatusDocumento, 'id'> & { id: null };
