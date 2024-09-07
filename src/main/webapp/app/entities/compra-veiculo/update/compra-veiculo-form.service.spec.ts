import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compra-veiculo.test-samples';

import { CompraVeiculoFormService } from './compra-veiculo-form.service';

describe('CompraVeiculo Form Service', () => {
  let service: CompraVeiculoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompraVeiculoFormService);
  });

  describe('Service methods', () => {
    describe('createCompraVeiculoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompraVeiculoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kmEntrada: expect.any(Object),
            valor: expect.any(Object),
            valorEstimado: expect.any(Object),
            enderecoCrlv: expect.any(Object),
            cidadeCrlv: expect.any(Object),
            ufCrlv: expect.any(Object),
            cpfCrlv: expect.any(Object),
            dataCompra: expect.any(Object),
            condicaoPagamento: expect.any(Object),
            valorPago: expect.any(Object),
            instituicao: expect.any(Object),
            veiculo: expect.any(Object),
            filial: expect.any(Object),
          }),
        );
      });

      it('passing ICompraVeiculo should create a new form with FormGroup', () => {
        const formGroup = service.createCompraVeiculoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kmEntrada: expect.any(Object),
            valor: expect.any(Object),
            valorEstimado: expect.any(Object),
            enderecoCrlv: expect.any(Object),
            cidadeCrlv: expect.any(Object),
            ufCrlv: expect.any(Object),
            cpfCrlv: expect.any(Object),
            dataCompra: expect.any(Object),
            condicaoPagamento: expect.any(Object),
            valorPago: expect.any(Object),
            instituicao: expect.any(Object),
            veiculo: expect.any(Object),
            filial: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompraVeiculo', () => {
      it('should return NewCompraVeiculo for default CompraVeiculo initial value', () => {
        const formGroup = service.createCompraVeiculoFormGroup(sampleWithNewData);

        const compraVeiculo = service.getCompraVeiculo(formGroup) as any;

        expect(compraVeiculo).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompraVeiculo for empty CompraVeiculo initial value', () => {
        const formGroup = service.createCompraVeiculoFormGroup();

        const compraVeiculo = service.getCompraVeiculo(formGroup) as any;

        expect(compraVeiculo).toMatchObject({});
      });

      it('should return ICompraVeiculo', () => {
        const formGroup = service.createCompraVeiculoFormGroup(sampleWithRequiredData);

        const compraVeiculo = service.getCompraVeiculo(formGroup) as any;

        expect(compraVeiculo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompraVeiculo should not enable id FormControl', () => {
        const formGroup = service.createCompraVeiculoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompraVeiculo should disable id FormControl', () => {
        const formGroup = service.createCompraVeiculoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
