import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../troca-veiculo.test-samples';

import { TrocaVeiculoFormService } from './troca-veiculo-form.service';

describe('TrocaVeiculo Form Service', () => {
  let service: TrocaVeiculoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrocaVeiculoFormService);
  });

  describe('Service methods', () => {
    describe('createTrocaVeiculoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrocaVeiculoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            carroEntradaId: expect.any(Object),
            carroSaidaId: expect.any(Object),
            dataTroca: expect.any(Object),
            condicaoPagamento: expect.any(Object),
            valorPago: expect.any(Object),
            valorRecebido: expect.any(Object),
            veiculoEntrada: expect.any(Object),
            veiculoSaida: expect.any(Object),
            filial: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });

      it('passing ITrocaVeiculo should create a new form with FormGroup', () => {
        const formGroup = service.createTrocaVeiculoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            carroEntradaId: expect.any(Object),
            carroSaidaId: expect.any(Object),
            dataTroca: expect.any(Object),
            condicaoPagamento: expect.any(Object),
            valorPago: expect.any(Object),
            valorRecebido: expect.any(Object),
            veiculoEntrada: expect.any(Object),
            veiculoSaida: expect.any(Object),
            filial: expect.any(Object),
            instituicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrocaVeiculo', () => {
      it('should return NewTrocaVeiculo for default TrocaVeiculo initial value', () => {
        const formGroup = service.createTrocaVeiculoFormGroup(sampleWithNewData);

        const trocaVeiculo = service.getTrocaVeiculo(formGroup) as any;

        expect(trocaVeiculo).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrocaVeiculo for empty TrocaVeiculo initial value', () => {
        const formGroup = service.createTrocaVeiculoFormGroup();

        const trocaVeiculo = service.getTrocaVeiculo(formGroup) as any;

        expect(trocaVeiculo).toMatchObject({});
      });

      it('should return ITrocaVeiculo', () => {
        const formGroup = service.createTrocaVeiculoFormGroup(sampleWithRequiredData);

        const trocaVeiculo = service.getTrocaVeiculo(formGroup) as any;

        expect(trocaVeiculo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrocaVeiculo should not enable id FormControl', () => {
        const formGroup = service.createTrocaVeiculoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrocaVeiculo should disable id FormControl', () => {
        const formGroup = service.createTrocaVeiculoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
