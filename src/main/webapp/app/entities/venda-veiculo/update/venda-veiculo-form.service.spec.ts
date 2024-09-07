import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../venda-veiculo.test-samples';

import { VendaVeiculoFormService } from './venda-veiculo-form.service';

describe('VendaVeiculo Form Service', () => {
  let service: VendaVeiculoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VendaVeiculoFormService);
  });

  describe('Service methods', () => {
    describe('createVendaVeiculoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVendaVeiculoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kmSaida: expect.any(Object),
            valorCompra: expect.any(Object),
            valorTabela: expect.any(Object),
            valorVenda: expect.any(Object),
            dataVenda: expect.any(Object),
            condicaoRecebimento: expect.any(Object),
            valorEntrada: expect.any(Object),
            valorFinanciado: expect.any(Object),
            veiculo: expect.any(Object),
            instituicao: expect.any(Object),
            filial: expect.any(Object),
            financeira: expect.any(Object),
          }),
        );
      });

      it('passing IVendaVeiculo should create a new form with FormGroup', () => {
        const formGroup = service.createVendaVeiculoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kmSaida: expect.any(Object),
            valorCompra: expect.any(Object),
            valorTabela: expect.any(Object),
            valorVenda: expect.any(Object),
            dataVenda: expect.any(Object),
            condicaoRecebimento: expect.any(Object),
            valorEntrada: expect.any(Object),
            valorFinanciado: expect.any(Object),
            veiculo: expect.any(Object),
            instituicao: expect.any(Object),
            filial: expect.any(Object),
            financeira: expect.any(Object),
          }),
        );
      });
    });

    describe('getVendaVeiculo', () => {
      it('should return NewVendaVeiculo for default VendaVeiculo initial value', () => {
        const formGroup = service.createVendaVeiculoFormGroup(sampleWithNewData);

        const vendaVeiculo = service.getVendaVeiculo(formGroup) as any;

        expect(vendaVeiculo).toMatchObject(sampleWithNewData);
      });

      it('should return NewVendaVeiculo for empty VendaVeiculo initial value', () => {
        const formGroup = service.createVendaVeiculoFormGroup();

        const vendaVeiculo = service.getVendaVeiculo(formGroup) as any;

        expect(vendaVeiculo).toMatchObject({});
      });

      it('should return IVendaVeiculo', () => {
        const formGroup = service.createVendaVeiculoFormGroup(sampleWithRequiredData);

        const vendaVeiculo = service.getVendaVeiculo(formGroup) as any;

        expect(vendaVeiculo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVendaVeiculo should not enable id FormControl', () => {
        const formGroup = service.createVendaVeiculoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVendaVeiculo should disable id FormControl', () => {
        const formGroup = service.createVendaVeiculoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
