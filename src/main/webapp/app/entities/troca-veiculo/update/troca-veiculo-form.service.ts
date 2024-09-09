import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITrocaVeiculo, NewTrocaVeiculo } from '../troca-veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrocaVeiculo for edit and NewTrocaVeiculoFormGroupInput for create.
 */
type TrocaVeiculoFormGroupInput = ITrocaVeiculo | PartialWithRequiredKeyOf<NewTrocaVeiculo>;

type TrocaVeiculoFormDefaults = Pick<NewTrocaVeiculo, 'id'>;

type TrocaVeiculoFormGroupContent = {
  id: FormControl<ITrocaVeiculo['id'] | NewTrocaVeiculo['id']>;
  carroEntradaId: FormControl<ITrocaVeiculo['carroEntradaId']>;
  carroSaidaId: FormControl<ITrocaVeiculo['carroSaidaId']>;
  dataTroca: FormControl<ITrocaVeiculo['dataTroca']>;
  condicaoPagamento: FormControl<ITrocaVeiculo['condicaoPagamento']>;
  valorPago: FormControl<ITrocaVeiculo['valorPago']>;
  valorRecebido: FormControl<ITrocaVeiculo['valorRecebido']>;
  veiculoEntrada: FormControl<ITrocaVeiculo['veiculoEntrada']>;
  veiculoSaida: FormControl<ITrocaVeiculo['veiculoSaida']>;
  filial: FormControl<ITrocaVeiculo['filial']>;
};

export type TrocaVeiculoFormGroup = FormGroup<TrocaVeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrocaVeiculoFormService {
  createTrocaVeiculoFormGroup(trocaVeiculo: TrocaVeiculoFormGroupInput = { id: null }): TrocaVeiculoFormGroup {
    const trocaVeiculoRawValue = {
      ...this.getFormDefaults(),
      ...trocaVeiculo,
    };
    return new FormGroup<TrocaVeiculoFormGroupContent>({
      id: new FormControl(
        { value: trocaVeiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      carroEntradaId: new FormControl(trocaVeiculoRawValue.carroEntradaId, {
        validators: [Validators.required],
      }),
      carroSaidaId: new FormControl(trocaVeiculoRawValue.carroSaidaId, {
        validators: [Validators.required],
      }),
      dataTroca: new FormControl(trocaVeiculoRawValue.dataTroca, {
        validators: [Validators.required],
      }),
      condicaoPagamento: new FormControl(trocaVeiculoRawValue.condicaoPagamento),
      valorPago: new FormControl(trocaVeiculoRawValue.valorPago),
      valorRecebido: new FormControl(trocaVeiculoRawValue.valorRecebido),
      veiculoEntrada: new FormControl(trocaVeiculoRawValue.veiculoEntrada),
      veiculoSaida: new FormControl(trocaVeiculoRawValue.veiculoSaida),
      filial: new FormControl(trocaVeiculoRawValue.filial),
    });
  }

  getTrocaVeiculo(form: TrocaVeiculoFormGroup): ITrocaVeiculo | NewTrocaVeiculo {
    return form.getRawValue() as ITrocaVeiculo | NewTrocaVeiculo;
  }

  resetForm(form: TrocaVeiculoFormGroup, trocaVeiculo: TrocaVeiculoFormGroupInput): void {
    const trocaVeiculoRawValue = { ...this.getFormDefaults(), ...trocaVeiculo };
    form.reset(
      {
        ...trocaVeiculoRawValue,
        id: { value: trocaVeiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrocaVeiculoFormDefaults {
    return {
      id: null,
    };
  }
}
