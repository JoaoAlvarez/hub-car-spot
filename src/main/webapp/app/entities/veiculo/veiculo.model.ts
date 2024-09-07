import dayjs from 'dayjs/esm';
import { EspecieVeiculo } from 'app/entities/enumerations/especie-veiculo.model';
import { StatusVeiculo } from 'app/entities/enumerations/status-veiculo.model';

export interface IVeiculo {
  id: string;
  createdAt?: dayjs.Dayjs | null;
  especie?: keyof typeof EspecieVeiculo | null;
  placa?: string | null;
  marca?: string | null;
  modelo?: string | null;
  anoFabricacao?: number | null;
  anoModelo?: number | null;
  cor?: string | null;
  combustivel?: string | null;
  cambio?: string | null;
  status?: keyof typeof StatusVeiculo | null;
  chassi?: string | null;
  renavam?: string | null;
  numeroMotor?: string | null;
  numeroCambio?: string | null;
  quilometraegem?: number | null;
  kmSaida?: number | null;
  cavalos?: string | null;
  motorizacao?: string | null;
  adicional?: string | null;
  descritivoCurtoAcessorios?: string | null;
}

export type NewVeiculo = Omit<IVeiculo, 'id'> & { id: null };
