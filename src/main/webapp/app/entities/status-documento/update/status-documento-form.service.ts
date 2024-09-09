import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStatusDocumento, NewStatusDocumento } from '../status-documento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatusDocumento for edit and NewStatusDocumentoFormGroupInput for create.
 */
type StatusDocumentoFormGroupInput = IStatusDocumento | PartialWithRequiredKeyOf<NewStatusDocumento>;

type StatusDocumentoFormDefaults = Pick<NewStatusDocumento, 'id'>;

type StatusDocumentoFormGroupContent = {
  id: FormControl<IStatusDocumento['id'] | NewStatusDocumento['id']>;
  nome: FormControl<IStatusDocumento['nome']>;
};

export type StatusDocumentoFormGroup = FormGroup<StatusDocumentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StatusDocumentoFormService {
  createStatusDocumentoFormGroup(statusDocumento: StatusDocumentoFormGroupInput = { id: null }): StatusDocumentoFormGroup {
    const statusDocumentoRawValue = {
      ...this.getFormDefaults(),
      ...statusDocumento,
    };
    return new FormGroup<StatusDocumentoFormGroupContent>({
      id: new FormControl(
        { value: statusDocumentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(statusDocumentoRawValue.nome),
    });
  }

  getStatusDocumento(form: StatusDocumentoFormGroup): IStatusDocumento | NewStatusDocumento {
    return form.getRawValue() as IStatusDocumento | NewStatusDocumento;
  }

  resetForm(form: StatusDocumentoFormGroup, statusDocumento: StatusDocumentoFormGroupInput): void {
    const statusDocumentoRawValue = { ...this.getFormDefaults(), ...statusDocumento };
    form.reset(
      {
        ...statusDocumentoRawValue,
        id: { value: statusDocumentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StatusDocumentoFormDefaults {
    return {
      id: null,
    };
  }
}
