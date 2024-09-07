import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface IUsuarioInstituicao {
  id: string;
  identificador?: string | null;
  isMaster?: boolean | null;
  role?: string | null;
  read?: boolean | null;
  write?: boolean | null;
  update?: boolean | null;
  instituicao?: IInstituicao | null;
}

export type NewUsuarioInstituicao = Omit<IUsuarioInstituicao, 'id'> & { id: null };
