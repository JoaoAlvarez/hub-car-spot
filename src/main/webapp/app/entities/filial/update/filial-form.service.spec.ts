import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../filial.test-samples';

import { FilialFormService } from './filial-form.service';

describe('Filial Form Service', () => {
  let service: FilialFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FilialFormService);
  });

  describe('Service methods', () => {
    describe('createFilialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFilialFormGroup();

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
            instituicao: expect.any(Object),
          }),
        );
      });

      it('passing IFilial should create a new form with FormGroup', () => {
        const formGroup = service.createFilialFormGroup(sampleWithRequiredData);

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
            instituicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getFilial', () => {
      it('should return NewFilial for default Filial initial value', () => {
        const formGroup = service.createFilialFormGroup(sampleWithNewData);

        const filial = service.getFilial(formGroup) as any;

        expect(filial).toMatchObject(sampleWithNewData);
      });

      it('should return NewFilial for empty Filial initial value', () => {
        const formGroup = service.createFilialFormGroup();

        const filial = service.getFilial(formGroup) as any;

        expect(filial).toMatchObject({});
      });

      it('should return IFilial', () => {
        const formGroup = service.createFilialFormGroup(sampleWithRequiredData);

        const filial = service.getFilial(formGroup) as any;

        expect(filial).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFilial should not enable id FormControl', () => {
        const formGroup = service.createFilialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFilial should disable id FormControl', () => {
        const formGroup = service.createFilialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
