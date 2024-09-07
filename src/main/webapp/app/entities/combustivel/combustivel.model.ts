export interface ICombustivel {
  id: string;
  nome?: string | null;
}

export type NewCombustivel = Omit<ICombustivel, 'id'> & { id: null };
