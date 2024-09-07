import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVeiculo, NewVeiculo } from '../veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVeiculo for edit and NewVeiculoFormGroupInput for create.
 */
type VeiculoFormGroupInput = IVeiculo | PartialWithRequiredKeyOf<NewVeiculo>;

type VeiculoFormDefaults = Pick<NewVeiculo, 'id'>;

type VeiculoFormGroupContent = {
  id: FormControl<IVeiculo['id'] | NewVeiculo['id']>;
  createdAt: FormControl<IVeiculo['createdAt']>;
  especie: FormControl<IVeiculo['especie']>;
  placa: FormControl<IVeiculo['placa']>;
  marca: FormControl<IVeiculo['marca']>;
  modelo: FormControl<IVeiculo['modelo']>;
  anoFabricacao: FormControl<IVeiculo['anoFabricacao']>;
  anoModelo: FormControl<IVeiculo['anoModelo']>;
  cor: FormControl<IVeiculo['cor']>;
  combustivel: FormControl<IVeiculo['combustivel']>;
  cambio: FormControl<IVeiculo['cambio']>;
  status: FormControl<IVeiculo['status']>;
  chassi: FormControl<IVeiculo['chassi']>;
  renavam: FormControl<IVeiculo['renavam']>;
  numeroMotor: FormControl<IVeiculo['numeroMotor']>;
  numeroCambio: FormControl<IVeiculo['numeroCambio']>;
  quilometraegem: FormControl<IVeiculo['quilometraegem']>;
  kmSaida: FormControl<IVeiculo['kmSaida']>;
  cavalos: FormControl<IVeiculo['cavalos']>;
  motorizacao: FormControl<IVeiculo['motorizacao']>;
  adicional: FormControl<IVeiculo['adicional']>;
  descritivoCurtoAcessorios: FormControl<IVeiculo['descritivoCurtoAcessorios']>;
};

export type VeiculoFormGroup = FormGroup<VeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VeiculoFormService {
  createVeiculoFormGroup(veiculo: VeiculoFormGroupInput = { id: null }): VeiculoFormGroup {
    const veiculoRawValue = {
      ...this.getFormDefaults(),
      ...veiculo,
    };
    return new FormGroup<VeiculoFormGroupContent>({
      id: new FormControl(
        { value: veiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createdAt: new FormControl(veiculoRawValue.createdAt, {
        validators: [Validators.required],
      }),
      especie: new FormControl(veiculoRawValue.especie, {
        validators: [Validators.required],
      }),
      placa: new FormControl(veiculoRawValue.placa, {
        validators: [Validators.required],
      }),
      marca: new FormControl(veiculoRawValue.marca, {
        validators: [Validators.required],
      }),
      modelo: new FormControl(veiculoRawValue.modelo),
      anoFabricacao: new FormControl(veiculoRawValue.anoFabricacao, {
        validators: [Validators.required],
      }),
      anoModelo: new FormControl(veiculoRawValue.anoModelo, {
        validators: [Validators.required],
      }),
      cor: new FormControl(veiculoRawValue.cor),
      combustivel: new FormControl(veiculoRawValue.combustivel, {
        validators: [Validators.required],
      }),
      cambio: new FormControl(veiculoRawValue.cambio),
      status: new FormControl(veiculoRawValue.status),
      chassi: new FormControl(veiculoRawValue.chassi),
      renavam: new FormControl(veiculoRawValue.renavam),
      numeroMotor: new FormControl(veiculoRawValue.numeroMotor),
      numeroCambio: new FormControl(veiculoRawValue.numeroCambio),
      quilometraegem: new FormControl(veiculoRawValue.quilometraegem),
      kmSaida: new FormControl(veiculoRawValue.kmSaida),
      cavalos: new FormControl(veiculoRawValue.cavalos),
      motorizacao: new FormControl(veiculoRawValue.motorizacao),
      adicional: new FormControl(veiculoRawValue.adicional),
      descritivoCurtoAcessorios: new FormControl(veiculoRawValue.descritivoCurtoAcessorios),
    });
  }

  getVeiculo(form: VeiculoFormGroup): IVeiculo | NewVeiculo {
    return form.getRawValue() as IVeiculo | NewVeiculo;
  }

  resetForm(form: VeiculoFormGroup, veiculo: VeiculoFormGroupInput): void {
    const veiculoRawValue = { ...this.getFormDefaults(), ...veiculo };
    form.reset(
      {
        ...veiculoRawValue,
        id: { value: veiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VeiculoFormDefaults {
    return {
      id: null,
    };
  }
}
