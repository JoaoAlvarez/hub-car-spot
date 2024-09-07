import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../usuario-instituicao.test-samples';

import { UsuarioInstituicaoFormService } from './usuario-instituicao-form.service';

describe('UsuarioInstituicao Form Service', () => {
  let service: UsuarioInstituicaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuarioInstituicaoFormService);
  });

  describe('Service methods', () => {
    describe('createUsuarioInstituicaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identificador: expect.any(Object),
            isMaster: expect.any(Object),
            role: expect.any(Object),
            read: expect.any(Object),
            write: expect.any(Object),
            update: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });

      it('passing IUsuarioInstituicao should create a new form with FormGroup', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identificador: expect.any(Object),
            isMaster: expect.any(Object),
            role: expect.any(Object),
            read: expect.any(Object),
            write: expect.any(Object),
            update: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getUsuarioInstituicao', () => {
      it('should return NewUsuarioInstituicao for default UsuarioInstituicao initial value', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup(sampleWithNewData);

        const usuarioInstituicao = service.getUsuarioInstituicao(formGroup) as any;

        expect(usuarioInstituicao).toMatchObject(sampleWithNewData);
      });

      it('should return NewUsuarioInstituicao for empty UsuarioInstituicao initial value', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup();

        const usuarioInstituicao = service.getUsuarioInstituicao(formGroup) as any;

        expect(usuarioInstituicao).toMatchObject({});
      });

      it('should return IUsuarioInstituicao', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup(sampleWithRequiredData);

        const usuarioInstituicao = service.getUsuarioInstituicao(formGroup) as any;

        expect(usuarioInstituicao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUsuarioInstituicao should not enable id FormControl', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUsuarioInstituicao should disable id FormControl', () => {
        const formGroup = service.createUsuarioInstituicaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
