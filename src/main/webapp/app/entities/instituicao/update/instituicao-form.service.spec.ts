import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../instituicao.test-samples';

import { InstituicaoFormService } from './instituicao-form.service';

describe('Instituicao Form Service', () => {
  let service: InstituicaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstituicaoFormService);
  });

  describe('Service methods', () => {
    describe('createInstituicaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInstituicaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            telefone: expect.any(Object),
            cnpj: expect.any(Object),
            cep: expect.any(Object),
            endereco: expect.any(Object),
            bairro: expect.any(Object),
            cidade: expect.any(Object),
            numero: expect.any(Object),
            uf: expect.any(Object),
            complemento: expect.any(Object),
          }),
        );
      });

      it('passing IInstituicao should create a new form with FormGroup', () => {
        const formGroup = service.createInstituicaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            telefone: expect.any(Object),
            cnpj: expect.any(Object),
            cep: expect.any(Object),
            endereco: expect.any(Object),
            bairro: expect.any(Object),
            cidade: expect.any(Object),
            numero: expect.any(Object),
            uf: expect.any(Object),
            complemento: expect.any(Object),
          }),
        );
      });
    });

    describe('getInstituicao', () => {
      it('should return NewInstituicao for default Instituicao initial value', () => {
        const formGroup = service.createInstituicaoFormGroup(sampleWithNewData);

        const instituicao = service.getInstituicao(formGroup) as any;

        expect(instituicao).toMatchObject(sampleWithNewData);
      });

      it('should return NewInstituicao for empty Instituicao initial value', () => {
        const formGroup = service.createInstituicaoFormGroup();

        const instituicao = service.getInstituicao(formGroup) as any;

        expect(instituicao).toMatchObject({});
      });

      it('should return IInstituicao', () => {
        const formGroup = service.createInstituicaoFormGroup(sampleWithRequiredData);

        const instituicao = service.getInstituicao(formGroup) as any;

        expect(instituicao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInstituicao should not enable id FormControl', () => {
        const formGroup = service.createInstituicaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInstituicao should disable id FormControl', () => {
        const formGroup = service.createInstituicaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
