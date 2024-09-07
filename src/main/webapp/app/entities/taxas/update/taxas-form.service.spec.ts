import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../taxas.test-samples';

import { TaxasFormService } from './taxas-form.service';

describe('Taxas Form Service', () => {
  let service: TaxasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaxasFormService);
  });

  describe('Service methods', () => {
    describe('createTaxasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTaxasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            valor: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });

      it('passing ITaxas should create a new form with FormGroup', () => {
        const formGroup = service.createTaxasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            valor: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTaxas', () => {
      it('should return NewTaxas for default Taxas initial value', () => {
        const formGroup = service.createTaxasFormGroup(sampleWithNewData);

        const taxas = service.getTaxas(formGroup) as any;

        expect(taxas).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaxas for empty Taxas initial value', () => {
        const formGroup = service.createTaxasFormGroup();

        const taxas = service.getTaxas(formGroup) as any;

        expect(taxas).toMatchObject({});
      });

      it('should return ITaxas', () => {
        const formGroup = service.createTaxasFormGroup(sampleWithRequiredData);

        const taxas = service.getTaxas(formGroup) as any;

        expect(taxas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaxas should not enable id FormControl', () => {
        const formGroup = service.createTaxasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaxas should disable id FormControl', () => {
        const formGroup = service.createTaxasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
