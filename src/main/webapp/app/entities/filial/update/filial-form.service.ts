import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFilial, NewFilial } from '../filial.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFilial for edit and NewFilialFormGroupInput for create.
 */
type FilialFormGroupInput = IFilial | PartialWithRequiredKeyOf<NewFilial>;

type FilialFormDefaults = Pick<NewFilial, 'id'>;

type FilialFormGroupContent = {
  id: FormControl<IFilial['id'] | NewFilial['id']>;
  nome: FormControl<IFilial['nome']>;
  telefone: FormControl<IFilial['telefone']>;
  cnpj: FormControl<IFilial['cnpj']>;
  cep: FormControl<IFilial['cep']>;
  endereco: FormControl<IFilial['endereco']>;
  bairro: FormControl<IFilial['bairro']>;
  cidade: FormControl<IFilial['cidade']>;
  numero: FormControl<IFilial['numero']>;
  uf: FormControl<IFilial['uf']>;
  instituicao: FormControl<IFilial['instituicao']>;
};

export type FilialFormGroup = FormGroup<FilialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FilialFormService {
  createFilialFormGroup(filial: FilialFormGroupInput = { id: null }): FilialFormGroup {
    const filialRawValue = {
      ...this.getFormDefaults(),
      ...filial,
    };
    return new FormGroup<FilialFormGroupContent>({
      id: new FormControl(
        { value: filialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(filialRawValue.nome),
      telefone: new FormControl(filialRawValue.telefone, {
        validators: [Validators.required],
      }),
      cnpj: new FormControl(filialRawValue.cnpj),
      cep: new FormControl(filialRawValue.cep),
      endereco: new FormControl(filialRawValue.endereco),
      bairro: new FormControl(filialRawValue.bairro),
      cidade: new FormControl(filialRawValue.cidade),
      numero: new FormControl(filialRawValue.numero),
      uf: new FormControl(filialRawValue.uf),
      instituicao: new FormControl(filialRawValue.instituicao),
    });
  }

  getFilial(form: FilialFormGroup): IFilial | NewFilial {
    return form.getRawValue() as IFilial | NewFilial;
  }

  resetForm(form: FilialFormGroup, filial: FilialFormGroupInput): void {
    const filialRawValue = { ...this.getFormDefaults(), ...filial };
    form.reset(
      {
        ...filialRawValue,
        id: { value: filialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FilialFormDefaults {
    return {
      id: null,
    };
  }
}
