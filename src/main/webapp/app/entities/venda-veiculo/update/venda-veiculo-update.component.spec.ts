import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IFinanceira } from 'app/entities/financeira/financeira.model';
import { FinanceiraService } from 'app/entities/financeira/service/financeira.service';
import { IVendaVeiculo } from '../venda-veiculo.model';
import { VendaVeiculoService } from '../service/venda-veiculo.service';
import { VendaVeiculoFormService } from './venda-veiculo-form.service';

import { VendaVeiculoUpdateComponent } from './venda-veiculo-update.component';

describe('VendaVeiculo Management Update Component', () => {
  let comp: VendaVeiculoUpdateComponent;
  let fixture: ComponentFixture<VendaVeiculoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vendaVeiculoFormService: VendaVeiculoFormService;
  let vendaVeiculoService: VendaVeiculoService;
  let veiculoService: VeiculoService;
  let instituicaoService: InstituicaoService;
  let filialService: FilialService;
  let financeiraService: FinanceiraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VendaVeiculoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VendaVeiculoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendaVeiculoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vendaVeiculoFormService = TestBed.inject(VendaVeiculoFormService);
    vendaVeiculoService = TestBed.inject(VendaVeiculoService);
    veiculoService = TestBed.inject(VeiculoService);
    instituicaoService = TestBed.inject(InstituicaoService);
    filialService = TestBed.inject(FilialService);
    financeiraService = TestBed.inject(FinanceiraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Veiculo query and add missing value', () => {
      const vendaVeiculo: IVendaVeiculo = { id: 'CBA' };
      const veiculo: IVeiculo = { id: '7f44e6a9-c944-4675-b026-0c84f88b7514' };
      vendaVeiculo.veiculo = veiculo;

      const veiculoCollection: IVeiculo[] = [{ id: 'fc17520e-c7b6-464d-ada0-531c056ecafe' }];
      jest.spyOn(veiculoService, 'query').mockReturnValue(of(new HttpResponse({ body: veiculoCollection })));
      const additionalVeiculos = [veiculo];
      const expectedCollection: IVeiculo[] = [...additionalVeiculos, ...veiculoCollection];
      jest.spyOn(veiculoService, 'addVeiculoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      expect(veiculoService.query).toHaveBeenCalled();
      expect(veiculoService.addVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
        veiculoCollection,
        ...additionalVeiculos.map(expect.objectContaining),
      );
      expect(comp.veiculosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Instituicao query and add missing value', () => {
      const vendaVeiculo: IVendaVeiculo = { id: 'CBA' };
      const instituicao: IInstituicao = { id: 'd94ee287-ca7d-4d80-9d50-465f0c68540f' };
      vendaVeiculo.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: 'af834270-80cb-486a-ad4f-16beeacc2ea1' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Filial query and add missing value', () => {
      const vendaVeiculo: IVendaVeiculo = { id: 'CBA' };
      const filial: IFilial = { id: '6f7dc1cb-cb4b-45c7-aa97-d9530c9cd6f4' };
      vendaVeiculo.filial = filial;

      const filialCollection: IFilial[] = [{ id: '60cb1891-468d-4c66-adc9-03d3d6241c5b' }];
      jest.spyOn(filialService, 'query').mockReturnValue(of(new HttpResponse({ body: filialCollection })));
      const additionalFilials = [filial];
      const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
      jest.spyOn(filialService, 'addFilialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      expect(filialService.query).toHaveBeenCalled();
      expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(
        filialCollection,
        ...additionalFilials.map(expect.objectContaining),
      );
      expect(comp.filialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Financeira query and add missing value', () => {
      const vendaVeiculo: IVendaVeiculo = { id: 'CBA' };
      const financeira: IFinanceira = { id: 'cf20bcb3-5d9f-4773-984a-e654f9798da5' };
      vendaVeiculo.financeira = financeira;

      const financeiraCollection: IFinanceira[] = [{ id: 'ee6be88d-3381-4037-aec9-33741fd61d36' }];
      jest.spyOn(financeiraService, 'query').mockReturnValue(of(new HttpResponse({ body: financeiraCollection })));
      const additionalFinanceiras = [financeira];
      const expectedCollection: IFinanceira[] = [...additionalFinanceiras, ...financeiraCollection];
      jest.spyOn(financeiraService, 'addFinanceiraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      expect(financeiraService.query).toHaveBeenCalled();
      expect(financeiraService.addFinanceiraToCollectionIfMissing).toHaveBeenCalledWith(
        financeiraCollection,
        ...additionalFinanceiras.map(expect.objectContaining),
      );
      expect(comp.financeirasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vendaVeiculo: IVendaVeiculo = { id: 'CBA' };
      const veiculo: IVeiculo = { id: 'f9d9607a-07cf-424e-84b3-8875d7f33cdc' };
      vendaVeiculo.veiculo = veiculo;
      const instituicao: IInstituicao = { id: '05ecb290-5f54-47c8-b9b8-c2035cbf575c' };
      vendaVeiculo.instituicao = instituicao;
      const filial: IFilial = { id: '2d87b24e-1ce1-4bda-86c7-e896fbc05750' };
      vendaVeiculo.filial = filial;
      const financeira: IFinanceira = { id: 'ae890a09-5e20-4d50-89c8-c3d1d48d34ad' };
      vendaVeiculo.financeira = financeira;

      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      expect(comp.veiculosSharedCollection).toContain(veiculo);
      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.filialsSharedCollection).toContain(filial);
      expect(comp.financeirasSharedCollection).toContain(financeira);
      expect(comp.vendaVeiculo).toEqual(vendaVeiculo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVendaVeiculo>>();
      const vendaVeiculo = { id: 'ABC' };
      jest.spyOn(vendaVeiculoFormService, 'getVendaVeiculo').mockReturnValue(vendaVeiculo);
      jest.spyOn(vendaVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendaVeiculo }));
      saveSubject.complete();

      // THEN
      expect(vendaVeiculoFormService.getVendaVeiculo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vendaVeiculoService.update).toHaveBeenCalledWith(expect.objectContaining(vendaVeiculo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVendaVeiculo>>();
      const vendaVeiculo = { id: 'ABC' };
      jest.spyOn(vendaVeiculoFormService, 'getVendaVeiculo').mockReturnValue({ id: null });
      jest.spyOn(vendaVeiculoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendaVeiculo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendaVeiculo }));
      saveSubject.complete();

      // THEN
      expect(vendaVeiculoFormService.getVendaVeiculo).toHaveBeenCalled();
      expect(vendaVeiculoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVendaVeiculo>>();
      const vendaVeiculo = { id: 'ABC' };
      jest.spyOn(vendaVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendaVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vendaVeiculoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVeiculo', () => {
      it('Should forward to veiculoService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(veiculoService, 'compareVeiculo');
        comp.compareVeiculo(entity, entity2);
        expect(veiculoService.compareVeiculo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInstituicao', () => {
      it('Should forward to instituicaoService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(instituicaoService, 'compareInstituicao');
        comp.compareInstituicao(entity, entity2);
        expect(instituicaoService.compareInstituicao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFilial', () => {
      it('Should forward to filialService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(filialService, 'compareFilial');
        comp.compareFilial(entity, entity2);
        expect(filialService.compareFilial).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFinanceira', () => {
      it('Should forward to financeiraService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(financeiraService, 'compareFinanceira');
        comp.compareFinanceira(entity, entity2);
        expect(financeiraService.compareFinanceira).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
