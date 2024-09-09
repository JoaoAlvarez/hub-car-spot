import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUsuarioInstituicao, NewUsuarioInstituicao } from '../usuario-instituicao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuarioInstituicao for edit and NewUsuarioInstituicaoFormGroupInput for create.
 */
type UsuarioInstituicaoFormGroupInput = IUsuarioInstituicao | PartialWithRequiredKeyOf<NewUsuarioInstituicao>;

type UsuarioInstituicaoFormDefaults = Pick<NewUsuarioInstituicao, 'id' | 'isMaster' | 'read' | 'write' | 'update'>;

type UsuarioInstituicaoFormGroupContent = {
  id: FormControl<IUsuarioInstituicao['id'] | NewUsuarioInstituicao['id']>;
  identificador: FormControl<IUsuarioInstituicao['identificador']>;
  isMaster: FormControl<IUsuarioInstituicao['isMaster']>;
  role: FormControl<IUsuarioInstituicao['role']>;
  read: FormControl<IUsuarioInstituicao['read']>;
  write: FormControl<IUsuarioInstituicao['write']>;
  update: FormControl<IUsuarioInstituicao['update']>;
};

export type UsuarioInstituicaoFormGroup = FormGroup<UsuarioInstituicaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsuarioInstituicaoFormService {
  createUsuarioInstituicaoFormGroup(usuarioInstituicao: UsuarioInstituicaoFormGroupInput = { id: null }): UsuarioInstituicaoFormGroup {
    const usuarioInstituicaoRawValue = {
      ...this.getFormDefaults(),
      ...usuarioInstituicao,
    };
    return new FormGroup<UsuarioInstituicaoFormGroupContent>({
      id: new FormControl(
        { value: usuarioInstituicaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identificador: new FormControl(usuarioInstituicaoRawValue.identificador, {
        validators: [Validators.required],
      }),
      isMaster: new FormControl(usuarioInstituicaoRawValue.isMaster, {
        validators: [Validators.required],
      }),
      role: new FormControl(usuarioInstituicaoRawValue.role, {
        validators: [Validators.required],
      }),
      read: new FormControl(usuarioInstituicaoRawValue.read, {
        validators: [Validators.required],
      }),
      write: new FormControl(usuarioInstituicaoRawValue.write, {
        validators: [Validators.required],
      }),
      update: new FormControl(usuarioInstituicaoRawValue.update, {
        validators: [Validators.required],
      }),
    });
  }

  getUsuarioInstituicao(form: UsuarioInstituicaoFormGroup): IUsuarioInstituicao | NewUsuarioInstituicao {
    return form.getRawValue() as IUsuarioInstituicao | NewUsuarioInstituicao;
  }

  resetForm(form: UsuarioInstituicaoFormGroup, usuarioInstituicao: UsuarioInstituicaoFormGroupInput): void {
    const usuarioInstituicaoRawValue = { ...this.getFormDefaults(), ...usuarioInstituicao };
    form.reset(
      {
        ...usuarioInstituicaoRawValue,
        id: { value: usuarioInstituicaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UsuarioInstituicaoFormDefaults {
    return {
      id: null,
      isMaster: false,
      read: false,
      write: false,
      update: false,
    };
  }
}
