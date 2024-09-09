import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVendaVeiculo, NewVendaVeiculo } from '../venda-veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVendaVeiculo for edit and NewVendaVeiculoFormGroupInput for create.
 */
type VendaVeiculoFormGroupInput = IVendaVeiculo | PartialWithRequiredKeyOf<NewVendaVeiculo>;

type VendaVeiculoFormDefaults = Pick<NewVendaVeiculo, 'id'>;

type VendaVeiculoFormGroupContent = {
  id: FormControl<IVendaVeiculo['id'] | NewVendaVeiculo['id']>;
  kmSaida: FormControl<IVendaVeiculo['kmSaida']>;
  valorCompra: FormControl<IVendaVeiculo['valorCompra']>;
  valorTabela: FormControl<IVendaVeiculo['valorTabela']>;
  valorVenda: FormControl<IVendaVeiculo['valorVenda']>;
  dataVenda: FormControl<IVendaVeiculo['dataVenda']>;
  condicaoRecebimento: FormControl<IVendaVeiculo['condicaoRecebimento']>;
  valorEntrada: FormControl<IVendaVeiculo['valorEntrada']>;
  valorFinanciado: FormControl<IVendaVeiculo['valorFinanciado']>;
  veiculo: FormControl<IVendaVeiculo['veiculo']>;
  filial: FormControl<IVendaVeiculo['filial']>;
  financeira: FormControl<IVendaVeiculo['financeira']>;
};

export type VendaVeiculoFormGroup = FormGroup<VendaVeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VendaVeiculoFormService {
  createVendaVeiculoFormGroup(vendaVeiculo: VendaVeiculoFormGroupInput = { id: null }): VendaVeiculoFormGroup {
    const vendaVeiculoRawValue = {
      ...this.getFormDefaults(),
      ...vendaVeiculo,
    };
    return new FormGroup<VendaVeiculoFormGroupContent>({
      id: new FormControl(
        { value: vendaVeiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      kmSaida: new FormControl(vendaVeiculoRawValue.kmSaida, {
        validators: [Validators.required],
      }),
      valorCompra: new FormControl(vendaVeiculoRawValue.valorCompra, {
        validators: [Validators.required],
      }),
      valorTabela: new FormControl(vendaVeiculoRawValue.valorTabela),
      valorVenda: new FormControl(vendaVeiculoRawValue.valorVenda, {
        validators: [Validators.required],
      }),
      dataVenda: new FormControl(vendaVeiculoRawValue.dataVenda, {
        validators: [Validators.required],
      }),
      condicaoRecebimento: new FormControl(vendaVeiculoRawValue.condicaoRecebimento, {
        validators: [Validators.required],
      }),
      valorEntrada: new FormControl(vendaVeiculoRawValue.valorEntrada),
      valorFinanciado: new FormControl(vendaVeiculoRawValue.valorFinanciado),
      veiculo: new FormControl(vendaVeiculoRawValue.veiculo),
      filial: new FormControl(vendaVeiculoRawValue.filial),
      financeira: new FormControl(vendaVeiculoRawValue.financeira),
    });
  }

  getVendaVeiculo(form: VendaVeiculoFormGroup): IVendaVeiculo | NewVendaVeiculo {
    return form.getRawValue() as IVendaVeiculo | NewVendaVeiculo;
  }

  resetForm(form: VendaVeiculoFormGroup, vendaVeiculo: VendaVeiculoFormGroupInput): void {
    const vendaVeiculoRawValue = { ...this.getFormDefaults(), ...vendaVeiculo };
    form.reset(
      {
        ...vendaVeiculoRawValue,
        id: { value: vendaVeiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VendaVeiculoFormDefaults {
    return {
      id: null,
    };
  }
}
