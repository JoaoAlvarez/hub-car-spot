import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../status-documento.test-samples';

import { StatusDocumentoFormService } from './status-documento-form.service';

describe('StatusDocumento Form Service', () => {
  let service: StatusDocumentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatusDocumentoFormService);
  });

  describe('Service methods', () => {
    describe('createStatusDocumentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStatusDocumentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            instituicaoId: expect.any(Object),
            nome: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });

      it('passing IStatusDocumento should create a new form with FormGroup', () => {
        const formGroup = service.createStatusDocumentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            instituicaoId: expect.any(Object),
            nome: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getStatusDocumento', () => {
      it('should return NewStatusDocumento for default StatusDocumento initial value', () => {
        const formGroup = service.createStatusDocumentoFormGroup(sampleWithNewData);

        const statusDocumento = service.getStatusDocumento(formGroup) as any;

        expect(statusDocumento).toMatchObject(sampleWithNewData);
      });

      it('should return NewStatusDocumento for empty StatusDocumento initial value', () => {
        const formGroup = service.createStatusDocumentoFormGroup();

        const statusDocumento = service.getStatusDocumento(formGroup) as any;

        expect(statusDocumento).toMatchObject({});
      });

      it('should return IStatusDocumento', () => {
        const formGroup = service.createStatusDocumentoFormGroup(sampleWithRequiredData);

        const statusDocumento = service.getStatusDocumento(formGroup) as any;

        expect(statusDocumento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStatusDocumento should not enable id FormControl', () => {
        const formGroup = service.createStatusDocumentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStatusDocumento should disable id FormControl', () => {
        const formGroup = service.createStatusDocumentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
