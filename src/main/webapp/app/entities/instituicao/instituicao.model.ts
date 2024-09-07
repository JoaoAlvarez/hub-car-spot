export interface IInstituicao {
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
  complemento?: string | null;
}

export type NewInstituicao = Omit<IInstituicao, 'id'> & { id: null };
