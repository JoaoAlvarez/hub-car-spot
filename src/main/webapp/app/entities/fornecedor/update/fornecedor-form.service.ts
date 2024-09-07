import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFornecedor, NewFornecedor } from '../fornecedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFornecedor for edit and NewFornecedorFormGroupInput for create.
 */
type FornecedorFormGroupInput = IFornecedor | PartialWithRequiredKeyOf<NewFornecedor>;

type FornecedorFormDefaults = Pick<NewFornecedor, 'id'>;

type FornecedorFormGroupContent = {
  id: FormControl<IFornecedor['id'] | NewFornecedor['id']>;
  nome: FormControl<IFornecedor['nome']>;
  cnpj: FormControl<IFornecedor['cnpj']>;
  telefone: FormControl<IFornecedor['telefone']>;
  cep: FormControl<IFornecedor['cep']>;
  endereco: FormControl<IFornecedor['endereco']>;
  bairro: FormControl<IFornecedor['bairro']>;
  cidade: FormControl<IFornecedor['cidade']>;
  numero: FormControl<IFornecedor['numero']>;
  uf: FormControl<IFornecedor['uf']>;
  instituicao: FormControl<IFornecedor['instituicao']>;
};

export type FornecedorFormGroup = FormGroup<FornecedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FornecedorFormService {
  createFornecedorFormGroup(fornecedor: FornecedorFormGroupInput = { id: null }): FornecedorFormGroup {
    const fornecedorRawValue = {
      ...this.getFormDefaults(),
      ...fornecedor,
    };
    return new FormGroup<FornecedorFormGroupContent>({
      id: new FormControl(
        { value: fornecedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(fornecedorRawValue.nome),
      cnpj: new FormControl(fornecedorRawValue.cnpj),
      telefone: new FormControl(fornecedorRawValue.telefone, {
        validators: [Validators.required],
      }),
      cep: new FormControl(fornecedorRawValue.cep),
      endereco: new FormControl(fornecedorRawValue.endereco),
      bairro: new FormControl(fornecedorRawValue.bairro),
      cidade: new FormControl(fornecedorRawValue.cidade),
      numero: new FormControl(fornecedorRawValue.numero),
      uf: new FormControl(fornecedorRawValue.uf),
      instituicao: new FormControl(fornecedorRawValue.instituicao),
    });
  }

  getFornecedor(form: FornecedorFormGroup): IFornecedor | NewFornecedor {
    return form.getRawValue() as IFornecedor | NewFornecedor;
  }

  resetForm(form: FornecedorFormGroup, fornecedor: FornecedorFormGroupInput): void {
    const fornecedorRawValue = { ...this.getFormDefaults(), ...fornecedor };
    form.reset(
      {
        ...fornecedorRawValue,
        id: { value: fornecedorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FornecedorFormDefaults {
    return {
      id: null,
    };
  }
}
