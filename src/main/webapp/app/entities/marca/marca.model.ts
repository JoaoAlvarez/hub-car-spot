export interface IMarca {
  id: string;
  nome?: string | null;
}

export type NewMarca = Omit<IMarca, 'id'> & { id: null };
