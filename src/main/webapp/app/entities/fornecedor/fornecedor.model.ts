import { IInstituicao } from 'app/entities/instituicao/instituicao.model';

export interface IFornecedor {
  id: string;
  nome?: string | null;
  cnpj?: string | null;
  telefone?: string | null;
  cep?: string | null;
  endereco?: string | null;
  bairro?: string | null;
  cidade?: string | null;
  numero?: string | null;
  uf?: string | null;
  instituicao?: IInstituicao | null;
}

export type NewFornecedor = Omit<IFornecedor, 'id'> & { id: null };
