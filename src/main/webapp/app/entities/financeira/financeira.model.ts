import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface IFinanceira {
  id: string;
  nome?: string | null;
  telefone?: string | null;
  cnpj?: string | null;
  cep?: string | null;
  endereco?: string | null;
  bairro?: string | null;
  cidade?: string | null;
  numero?: string | null;
  uf?: string | null;
  instituicao?: IInstituicao | null;
}

export type NewFinanceira = Omit<IFinanceira, 'id'> & { id: null };
