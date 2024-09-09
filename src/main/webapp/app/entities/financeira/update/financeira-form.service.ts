import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFinanceira, NewFinanceira } from '../financeira.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinanceira for edit and NewFinanceiraFormGroupInput for create.
 */
type FinanceiraFormGroupInput = IFinanceira | PartialWithRequiredKeyOf<NewFinanceira>;

type FinanceiraFormDefaults = Pick<NewFinanceira, 'id'>;

type FinanceiraFormGroupContent = {
  id: FormControl<IFinanceira['id'] | NewFinanceira['id']>;
  nome: FormControl<IFinanceira['nome']>;
  telefone: FormControl<IFinanceira['telefone']>;
  cnpj: FormControl<IFinanceira['cnpj']>;
  cep: FormControl<IFinanceira['cep']>;
  endereco: FormControl<IFinanceira['endereco']>;
  bairro: FormControl<IFinanceira['bairro']>;
  cidade: FormControl<IFinanceira['cidade']>;
  numero: FormControl<IFinanceira['numero']>;
  uf: FormControl<IFinanceira['uf']>;
};

export type FinanceiraFormGroup = FormGroup<FinanceiraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FinanceiraFormService {
  createFinanceiraFormGroup(financeira: FinanceiraFormGroupInput = { id: null }): FinanceiraFormGroup {
    const financeiraRawValue = {
      ...this.getFormDefaults(),
      ...financeira,
    };
    return new FormGroup<FinanceiraFormGroupContent>({
      id: new FormControl(
        { value: financeiraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(financeiraRawValue.nome),
      telefone: new FormControl(financeiraRawValue.telefone, {
        validators: [Validators.required],
      }),
      cnpj: new FormControl(financeiraRawValue.cnpj),
      cep: new FormControl(financeiraRawValue.cep),
      endereco: new FormControl(financeiraRawValue.endereco),
      bairro: new FormControl(financeiraRawValue.bairro),
      cidade: new FormControl(financeiraRawValue.cidade),
      numero: new FormControl(financeiraRawValue.numero),
      uf: new FormControl(financeiraRawValue.uf),
    });
  }

  getFinanceira(form: FinanceiraFormGroup): IFinanceira | NewFinanceira {
    return form.getRawValue() as IFinanceira | NewFinanceira;
  }

  resetForm(form: FinanceiraFormGroup, financeira: FinanceiraFormGroupInput): void {
    const financeiraRawValue = { ...this.getFormDefaults(), ...financeira };
    form.reset(
      {
        ...financeiraRawValue,
        id: { value: financeiraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FinanceiraFormDefaults {
    return {
      id: null,
    };
  }
}
