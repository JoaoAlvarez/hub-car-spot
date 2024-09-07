import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { ITrocaVeiculo } from '../troca-veiculo.model';
import { TrocaVeiculoService } from '../service/troca-veiculo.service';
import { TrocaVeiculoFormService } from './troca-veiculo-form.service';

import { TrocaVeiculoUpdateComponent } from './troca-veiculo-update.component';

describe('TrocaVeiculo Management Update Component', () => {
  let comp: TrocaVeiculoUpdateComponent;
  let fixture: ComponentFixture<TrocaVeiculoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trocaVeiculoFormService: TrocaVeiculoFormService;
  let trocaVeiculoService: TrocaVeiculoService;
  let veiculoService: VeiculoService;
  let filialService: FilialService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrocaVeiculoUpdateComponent],
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
      .overrideTemplate(TrocaVeiculoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrocaVeiculoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trocaVeiculoFormService = TestBed.inject(TrocaVeiculoFormService);
    trocaVeiculoService = TestBed.inject(TrocaVeiculoService);
    veiculoService = TestBed.inject(VeiculoService);
    filialService = TestBed.inject(FilialService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Veiculo query and add missing value', () => {
      const trocaVeiculo: ITrocaVeiculo = { id: 'CBA' };
      const veiculoEntrada: IVeiculo = { id: '47cb4612-a757-44cf-93fe-9fcf12337bbc' };
      trocaVeiculo.veiculoEntrada = veiculoEntrada;
      const veiculoSaida: IVeiculo = { id: '2fb978e4-6e80-4f55-b33a-6e4ce6d59571' };
      trocaVeiculo.veiculoSaida = veiculoSaida;

      const veiculoCollection: IVeiculo[] = [{ id: '6e773a5a-6c78-43ac-acc4-6be42fc0f589' }];
      jest.spyOn(veiculoService, 'query').mockReturnValue(of(new HttpResponse({ body: veiculoCollection })));
      const additionalVeiculos = [veiculoEntrada, veiculoSaida];
      const expectedCollection: IVeiculo[] = [...additionalVeiculos, ...veiculoCollection];
      jest.spyOn(veiculoService, 'addVeiculoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trocaVeiculo });
      comp.ngOnInit();

      expect(veiculoService.query).toHaveBeenCalled();
      expect(veiculoService.addVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
        veiculoCollection,
        ...additionalVeiculos.map(expect.objectContaining),
      );
      expect(comp.veiculosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Filial query and add missing value', () => {
      const trocaVeiculo: ITrocaVeiculo = { id: 'CBA' };
      const filial: IFilial = { id: '77d6fed6-dd4e-4d4f-a139-cdf7e0d10572' };
      trocaVeiculo.filial = filial;

      const filialCollection: IFilial[] = [{ id: 'd8db4e25-4e1d-4667-88bf-18c487f3648f' }];
      jest.spyOn(filialService, 'query').mockReturnValue(of(new HttpResponse({ body: filialCollection })));
      const additionalFilials = [filial];
      const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
      jest.spyOn(filialService, 'addFilialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trocaVeiculo });
      comp.ngOnInit();

      expect(filialService.query).toHaveBeenCalled();
      expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(
        filialCollection,
        ...additionalFilials.map(expect.objectContaining),
      );
      expect(comp.filialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Instituicao query and add missing value', () => {
      const trocaVeiculo: ITrocaVeiculo = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '83367d61-8c3c-4d4b-ad7d-47e0a39224a5' };
      trocaVeiculo.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: 'f0a6a2e5-d4c0-4d38-95b9-e49572dd63cf' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trocaVeiculo });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trocaVeiculo: ITrocaVeiculo = { id: 'CBA' };
      const veiculoEntrada: IVeiculo = { id: '2555b5b1-67ab-486a-aac3-391a4dd92ae7' };
      trocaVeiculo.veiculoEntrada = veiculoEntrada;
      const veiculoSaida: IVeiculo = { id: '3d06bd46-7a5a-4761-bee3-22d2c5a39621' };
      trocaVeiculo.veiculoSaida = veiculoSaida;
      const filial: IFilial = { id: '3dc37c9d-4f1a-4d96-88e6-37a56bd78755' };
      trocaVeiculo.filial = filial;
      const instituicao: IInstituicao = { id: '353adeb3-5a9b-42a3-9f17-7967a45a7bdb' };
      trocaVeiculo.instituicao = instituicao;

      activatedRoute.data = of({ trocaVeiculo });
      comp.ngOnInit();

      expect(comp.veiculosSharedCollection).toContain(veiculoEntrada);
      expect(comp.veiculosSharedCollection).toContain(veiculoSaida);
      expect(comp.filialsSharedCollection).toContain(filial);
      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.trocaVeiculo).toEqual(trocaVeiculo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrocaVeiculo>>();
      const trocaVeiculo = { id: 'ABC' };
      jest.spyOn(trocaVeiculoFormService, 'getTrocaVeiculo').mockReturnValue(trocaVeiculo);
      jest.spyOn(trocaVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trocaVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trocaVeiculo }));
      saveSubject.complete();

      // THEN
      expect(trocaVeiculoFormService.getTrocaVeiculo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trocaVeiculoService.update).toHaveBeenCalledWith(expect.objectContaining(trocaVeiculo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrocaVeiculo>>();
      const trocaVeiculo = { id: 'ABC' };
      jest.spyOn(trocaVeiculoFormService, 'getTrocaVeiculo').mockReturnValue({ id: null });
      jest.spyOn(trocaVeiculoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trocaVeiculo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trocaVeiculo }));
      saveSubject.complete();

      // THEN
      expect(trocaVeiculoFormService.getTrocaVeiculo).toHaveBeenCalled();
      expect(trocaVeiculoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrocaVeiculo>>();
      const trocaVeiculo = { id: 'ABC' };
      jest.spyOn(trocaVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trocaVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trocaVeiculoService.update).toHaveBeenCalled();
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

    describe('compareFilial', () => {
      it('Should forward to filialService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(filialService, 'compareFilial');
        comp.compareFilial(entity, entity2);
        expect(filialService.compareFilial).toHaveBeenCalledWith(entity, entity2);
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
  });
});
