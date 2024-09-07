import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInstituicao, NewInstituicao } from '../instituicao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInstituicao for edit and NewInstituicaoFormGroupInput for create.
 */
type InstituicaoFormGroupInput = IInstituicao | PartialWithRequiredKeyOf<NewInstituicao>;

type InstituicaoFormDefaults = Pick<NewInstituicao, 'id'>;

type InstituicaoFormGroupContent = {
  id: FormControl<IInstituicao['id'] | NewInstituicao['id']>;
  nome: FormControl<IInstituicao['nome']>;
  telefone: FormControl<IInstituicao['telefone']>;
  cnpj: FormControl<IInstituicao['cnpj']>;
  cep: FormControl<IInstituicao['cep']>;
  endereco: FormControl<IInstituicao['endereco']>;
  bairro: FormControl<IInstituicao['bairro']>;
  cidade: FormControl<IInstituicao['cidade']>;
  numero: FormControl<IInstituicao['numero']>;
  uf: FormControl<IInstituicao['uf']>;
  complemento: FormControl<IInstituicao['complemento']>;
};

export type InstituicaoFormGroup = FormGroup<InstituicaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InstituicaoFormService {
  createInstituicaoFormGroup(instituicao: InstituicaoFormGroupInput = { id: null }): InstituicaoFormGroup {
    const instituicaoRawValue = {
      ...this.getFormDefaults(),
      ...instituicao,
    };
    return new FormGroup<InstituicaoFormGroupContent>({
      id: new FormControl(
        { value: instituicaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(instituicaoRawValue.nome),
      telefone: new FormControl(instituicaoRawValue.telefone, {
        validators: [Validators.required],
      }),
      cnpj: new FormControl(instituicaoRawValue.cnpj),
      cep: new FormControl(instituicaoRawValue.cep),
      endereco: new FormControl(instituicaoRawValue.endereco),
      bairro: new FormControl(instituicaoRawValue.bairro),
      cidade: new FormControl(instituicaoRawValue.cidade),
      numero: new FormControl(instituicaoRawValue.numero),
      uf: new FormControl(instituicaoRawValue.uf),
      complemento: new FormControl(instituicaoRawValue.complemento),
    });
  }

  getInstituicao(form: InstituicaoFormGroup): IInstituicao | NewInstituicao {
    return form.getRawValue() as IInstituicao | NewInstituicao;
  }

  resetForm(form: InstituicaoFormGroup, instituicao: InstituicaoFormGroupInput): void {
    const instituicaoRawValue = { ...this.getFormDefaults(), ...instituicao };
    form.reset(
      {
        ...instituicaoRawValue,
        id: { value: instituicaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InstituicaoFormDefaults {
    return {
      id: null,
    };
  }
}
