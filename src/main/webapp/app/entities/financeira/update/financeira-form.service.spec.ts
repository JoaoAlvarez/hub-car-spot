import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../financeira.test-samples';

import { FinanceiraFormService } from './financeira-form.service';

describe('Financeira Form Service', () => {
  let service: FinanceiraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinanceiraFormService);
  });

  describe('Service methods', () => {
    describe('createFinanceiraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFinanceiraFormGroup();

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

      it('passing IFinanceira should create a new form with FormGroup', () => {
        const formGroup = service.createFinanceiraFormGroup(sampleWithRequiredData);

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

    describe('getFinanceira', () => {
      it('should return NewFinanceira for default Financeira initial value', () => {
        const formGroup = service.createFinanceiraFormGroup(sampleWithNewData);

        const financeira = service.getFinanceira(formGroup) as any;

        expect(financeira).toMatchObject(sampleWithNewData);
      });

      it('should return NewFinanceira for empty Financeira initial value', () => {
        const formGroup = service.createFinanceiraFormGroup();

        const financeira = service.getFinanceira(formGroup) as any;

        expect(financeira).toMatchObject({});
      });

      it('should return IFinanceira', () => {
        const formGroup = service.createFinanceiraFormGroup(sampleWithRequiredData);

        const financeira = service.getFinanceira(formGroup) as any;

        expect(financeira).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFinanceira should not enable id FormControl', () => {
        const formGroup = service.createFinanceiraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFinanceira should disable id FormControl', () => {
        const formGroup = service.createFinanceiraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
