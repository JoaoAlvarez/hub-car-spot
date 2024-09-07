import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../combustivel.test-samples';

import { CombustivelFormService } from './combustivel-form.service';

describe('Combustivel Form Service', () => {
  let service: CombustivelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CombustivelFormService);
  });

  describe('Service methods', () => {
    describe('createCombustivelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCombustivelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          }),
        );
      });

      it('passing ICombustivel should create a new form with FormGroup', () => {
        const formGroup = service.createCombustivelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          }),
        );
      });
    });

    describe('getCombustivel', () => {
      it('should return NewCombustivel for default Combustivel initial value', () => {
        const formGroup = service.createCombustivelFormGroup(sampleWithNewData);

        const combustivel = service.getCombustivel(formGroup) as any;

        expect(combustivel).toMatchObject(sampleWithNewData);
      });

      it('should return NewCombustivel for empty Combustivel initial value', () => {
        const formGroup = service.createCombustivelFormGroup();

        const combustivel = service.getCombustivel(formGroup) as any;

        expect(combustivel).toMatchObject({});
      });

      it('should return ICombustivel', () => {
        const formGroup = service.createCombustivelFormGroup(sampleWithRequiredData);

        const combustivel = service.getCombustivel(formGroup) as any;

        expect(combustivel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICombustivel should not enable id FormControl', () => {
        const formGroup = service.createCombustivelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCombustivel should disable id FormControl', () => {
        const formGroup = service.createCombustivelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
