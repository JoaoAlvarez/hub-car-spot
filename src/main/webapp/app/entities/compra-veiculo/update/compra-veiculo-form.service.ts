import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICompraVeiculo, NewCompraVeiculo } from '../compra-veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompraVeiculo for edit and NewCompraVeiculoFormGroupInput for create.
 */
type CompraVeiculoFormGroupInput = ICompraVeiculo | PartialWithRequiredKeyOf<NewCompraVeiculo>;

type CompraVeiculoFormDefaults = Pick<NewCompraVeiculo, 'id'>;

type CompraVeiculoFormGroupContent = {
  id: FormControl<ICompraVeiculo['id'] | NewCompraVeiculo['id']>;
  kmEntrada: FormControl<ICompraVeiculo['kmEntrada']>;
  valor: FormControl<ICompraVeiculo['valor']>;
  valorEstimado: FormControl<ICompraVeiculo['valorEstimado']>;
  enderecoCrlv: FormControl<ICompraVeiculo['enderecoCrlv']>;
  cidadeCrlv: FormControl<ICompraVeiculo['cidadeCrlv']>;
  ufCrlv: FormControl<ICompraVeiculo['ufCrlv']>;
  cpfCrlv: FormControl<ICompraVeiculo['cpfCrlv']>;
  dataCompra: FormControl<ICompraVeiculo['dataCompra']>;
  condicaoPagamento: FormControl<ICompraVeiculo['condicaoPagamento']>;
  valorPago: FormControl<ICompraVeiculo['valorPago']>;
  veiculo: FormControl<ICompraVeiculo['veiculo']>;
  filial: FormControl<ICompraVeiculo['filial']>;
};

export type CompraVeiculoFormGroup = FormGroup<CompraVeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompraVeiculoFormService {
  createCompraVeiculoFormGroup(compraVeiculo: CompraVeiculoFormGroupInput = { id: null }): CompraVeiculoFormGroup {
    const compraVeiculoRawValue = {
      ...this.getFormDefaults(),
      ...compraVeiculo,
    };
    return new FormGroup<CompraVeiculoFormGroupContent>({
      id: new FormControl(
        { value: compraVeiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      kmEntrada: new FormControl(compraVeiculoRawValue.kmEntrada, {
        validators: [Validators.required],
      }),
      valor: new FormControl(compraVeiculoRawValue.valor, {
        validators: [Validators.required],
      }),
      valorEstimado: new FormControl(compraVeiculoRawValue.valorEstimado),
      enderecoCrlv: new FormControl(compraVeiculoRawValue.enderecoCrlv),
      cidadeCrlv: new FormControl(compraVeiculoRawValue.cidadeCrlv),
      ufCrlv: new FormControl(compraVeiculoRawValue.ufCrlv),
      cpfCrlv: new FormControl(compraVeiculoRawValue.cpfCrlv),
      dataCompra: new FormControl(compraVeiculoRawValue.dataCompra, {
        validators: [Validators.required],
      }),
      condicaoPagamento: new FormControl(compraVeiculoRawValue.condicaoPagamento, {
        validators: [Validators.required],
      }),
      valorPago: new FormControl(compraVeiculoRawValue.valorPago),
      veiculo: new FormControl(compraVeiculoRawValue.veiculo),
      filial: new FormControl(compraVeiculoRawValue.filial),
    });
  }

  getCompraVeiculo(form: CompraVeiculoFormGroup): ICompraVeiculo | NewCompraVeiculo {
    return form.getRawValue() as ICompraVeiculo | NewCompraVeiculo;
  }

  resetForm(form: CompraVeiculoFormGroup, compraVeiculo: CompraVeiculoFormGroupInput): void {
    const compraVeiculoRawValue = { ...this.getFormDefaults(), ...compraVeiculo };
    form.reset(
      {
        ...compraVeiculoRawValue,
        id: { value: compraVeiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompraVeiculoFormDefaults {
    return {
      id: null,
    };
  }
}
