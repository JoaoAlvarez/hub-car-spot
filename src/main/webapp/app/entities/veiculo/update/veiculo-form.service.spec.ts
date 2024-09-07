import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../veiculo.test-samples';

import { VeiculoFormService } from './veiculo-form.service';

describe('Veiculo Form Service', () => {
  let service: VeiculoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VeiculoFormService);
  });

  describe('Service methods', () => {
    describe('createVeiculoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVeiculoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            especie: expect.any(Object),
            placa: expect.any(Object),
            marca: expect.any(Object),
            modelo: expect.any(Object),
            anoFabricacao: expect.any(Object),
            anoModelo: expect.any(Object),
            cor: expect.any(Object),
            combustivel: expect.any(Object),
            cambio: expect.any(Object),
            status: expect.any(Object),
            chassi: expect.any(Object),
            renavam: expect.any(Object),
            numeroMotor: expect.any(Object),
            numeroCambio: expect.any(Object),
            quilometraegem: expect.any(Object),
            kmSaida: expect.any(Object),
            cavalos: expect.any(Object),
            motorizacao: expect.any(Object),
            adicional: expect.any(Object),
            descritivoCurtoAcessorios: expect.any(Object),
          }),
        );
      });

      it('passing IVeiculo should create a new form with FormGroup', () => {
        const formGroup = service.createVeiculoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            especie: expect.any(Object),
            placa: expect.any(Object),
            marca: expect.any(Object),
            modelo: expect.any(Object),
            anoFabricacao: expect.any(Object),
            anoModelo: expect.any(Object),
            cor: expect.any(Object),
            combustivel: expect.any(Object),
            cambio: expect.any(Object),
            status: expect.any(Object),
            chassi: expect.any(Object),
            renavam: expect.any(Object),
            numeroMotor: expect.any(Object),
            numeroCambio: expect.any(Object),
            quilometraegem: expect.any(Object),
            kmSaida: expect.any(Object),
            cavalos: expect.any(Object),
            motorizacao: expect.any(Object),
            adicional: expect.any(Object),
            descritivoCurtoAcessorios: expect.any(Object),
          }),
        );
      });
    });

    describe('getVeiculo', () => {
      it('should return NewVeiculo for default Veiculo initial value', () => {
        const formGroup = service.createVeiculoFormGroup(sampleWithNewData);

        const veiculo = service.getVeiculo(formGroup) as any;

        expect(veiculo).toMatchObject(sampleWithNewData);
      });

      it('should return NewVeiculo for empty Veiculo initial value', () => {
        const formGroup = service.createVeiculoFormGroup();

        const veiculo = service.getVeiculo(formGroup) as any;

        expect(veiculo).toMatchObject({});
      });

      it('should return IVeiculo', () => {
        const formGroup = service.createVeiculoFormGroup(sampleWithRequiredData);

        const veiculo = service.getVeiculo(formGroup) as any;

        expect(veiculo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVeiculo should not enable id FormControl', () => {
        const formGroup = service.createVeiculoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVeiculo should disable id FormControl', () => {
        const formGroup = service.createVeiculoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
