import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITaxas, NewTaxas } from '../taxas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITaxas for edit and NewTaxasFormGroupInput for create.
 */
type TaxasFormGroupInput = ITaxas | PartialWithRequiredKeyOf<NewTaxas>;

type TaxasFormDefaults = Pick<NewTaxas, 'id'>;

type TaxasFormGroupContent = {
  id: FormControl<ITaxas['id'] | NewTaxas['id']>;
  nome: FormControl<ITaxas['nome']>;
  valor: FormControl<ITaxas['valor']>;
  instituicao: FormControl<ITaxas['instituicao']>;
};

export type TaxasFormGroup = FormGroup<TaxasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaxasFormService {
  createTaxasFormGroup(taxas: TaxasFormGroupInput = { id: null }): TaxasFormGroup {
    const taxasRawValue = {
      ...this.getFormDefaults(),
      ...taxas,
    };
    return new FormGroup<TaxasFormGroupContent>({
      id: new FormControl(
        { value: taxasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(taxasRawValue.nome),
      valor: new FormControl(taxasRawValue.valor),
      instituicao: new FormControl(taxasRawValue.instituicao),
    });
  }

  getTaxas(form: TaxasFormGroup): ITaxas | NewTaxas {
    return form.getRawValue() as ITaxas | NewTaxas;
  }

  resetForm(form: TaxasFormGroup, taxas: TaxasFormGroupInput): void {
    const taxasRawValue = { ...this.getFormDefaults(), ...taxas };
    form.reset(
      {
        ...taxasRawValue,
        id: { value: taxasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TaxasFormDefaults {
    return {
      id: null,
    };
  }
}
