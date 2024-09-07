import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICombustivel, NewCombustivel } from '../combustivel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICombustivel for edit and NewCombustivelFormGroupInput for create.
 */
type CombustivelFormGroupInput = ICombustivel | PartialWithRequiredKeyOf<NewCombustivel>;

type CombustivelFormDefaults = Pick<NewCombustivel, 'id'>;

type CombustivelFormGroupContent = {
  id: FormControl<ICombustivel['id'] | NewCombustivel['id']>;
  nome: FormControl<ICombustivel['nome']>;
};

export type CombustivelFormGroup = FormGroup<CombustivelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CombustivelFormService {
  createCombustivelFormGroup(combustivel: CombustivelFormGroupInput = { id: null }): CombustivelFormGroup {
    const combustivelRawValue = {
      ...this.getFormDefaults(),
      ...combustivel,
    };
    return new FormGroup<CombustivelFormGroupContent>({
      id: new FormControl(
        { value: combustivelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(combustivelRawValue.nome),
    });
  }

  getCombustivel(form: CombustivelFormGroup): ICombustivel | NewCombustivel {
    return form.getRawValue() as ICombustivel | NewCombustivel;
  }

  resetForm(form: CombustivelFormGroup, combustivel: CombustivelFormGroupInput): void {
    const combustivelRawValue = { ...this.getFormDefaults(), ...combustivel };
    form.reset(
      {
        ...combustivelRawValue,
        id: { value: combustivelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CombustivelFormDefaults {
    return {
      id: null,
    };
  }
}
