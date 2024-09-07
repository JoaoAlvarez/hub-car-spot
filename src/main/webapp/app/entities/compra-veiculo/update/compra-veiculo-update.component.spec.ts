import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { ICompraVeiculo } from '../compra-veiculo.model';
import { CompraVeiculoService } from '../service/compra-veiculo.service';
import { CompraVeiculoFormService } from './compra-veiculo-form.service';

import { CompraVeiculoUpdateComponent } from './compra-veiculo-update.component';

describe('CompraVeiculo Management Update Component', () => {
  let comp: CompraVeiculoUpdateComponent;
  let fixture: ComponentFixture<CompraVeiculoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compraVeiculoFormService: CompraVeiculoFormService;
  let compraVeiculoService: CompraVeiculoService;
  let instituicaoService: InstituicaoService;
  let veiculoService: VeiculoService;
  let filialService: FilialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CompraVeiculoUpdateComponent],
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
      .overrideTemplate(CompraVeiculoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompraVeiculoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compraVeiculoFormService = TestBed.inject(CompraVeiculoFormService);
    compraVeiculoService = TestBed.inject(CompraVeiculoService);
    instituicaoService = TestBed.inject(InstituicaoService);
    veiculoService = TestBed.inject(VeiculoService);
    filialService = TestBed.inject(FilialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const compraVeiculo: ICompraVeiculo = { id: 'CBA' };
      const instituicao: IInstituicao = { id: 'c76a6ed3-582d-4a1a-b3fb-1cffacacd438' };
      compraVeiculo.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: '0f9aa0a2-9f00-4b3b-8b79-11f4cf24ebe7' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compraVeiculo });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Veiculo query and add missing value', () => {
      const compraVeiculo: ICompraVeiculo = { id: 'CBA' };
      const veiculo: IVeiculo = { id: 'a948f44a-d351-4241-b661-8d38310c3374' };
      compraVeiculo.veiculo = veiculo;

      const veiculoCollection: IVeiculo[] = [{ id: '049932de-4650-4822-bae0-d9d3805b8d2e' }];
      jest.spyOn(veiculoService, 'query').mockReturnValue(of(new HttpResponse({ body: veiculoCollection })));
      const additionalVeiculos = [veiculo];
      const expectedCollection: IVeiculo[] = [...additionalVeiculos, ...veiculoCollection];
      jest.spyOn(veiculoService, 'addVeiculoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compraVeiculo });
      comp.ngOnInit();

      expect(veiculoService.query).toHaveBeenCalled();
      expect(veiculoService.addVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
        veiculoCollection,
        ...additionalVeiculos.map(expect.objectContaining),
      );
      expect(comp.veiculosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Filial query and add missing value', () => {
      const compraVeiculo: ICompraVeiculo = { id: 'CBA' };
      const filial: IFilial = { id: 'a816048d-9cef-4335-a5de-344942c72af4' };
      compraVeiculo.filial = filial;

      const filialCollection: IFilial[] = [{ id: '2dfd27d4-4f63-4b3f-93e3-a2a32595babe' }];
      jest.spyOn(filialService, 'query').mockReturnValue(of(new HttpResponse({ body: filialCollection })));
      const additionalFilials = [filial];
      const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
      jest.spyOn(filialService, 'addFilialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compraVeiculo });
      comp.ngOnInit();

      expect(filialService.query).toHaveBeenCalled();
      expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(
        filialCollection,
        ...additionalFilials.map(expect.objectContaining),
      );
      expect(comp.filialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compraVeiculo: ICompraVeiculo = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '6001529c-1f2a-4cf3-9210-70459841edc4' };
      compraVeiculo.instituicao = instituicao;
      const veiculo: IVeiculo = { id: 'dec8a34a-cc86-4e9b-80bd-17b1c07bc5e4' };
      compraVeiculo.veiculo = veiculo;
      const filial: IFilial = { id: '8eb04cdf-de29-4382-a96b-882aaa3cff96' };
      compraVeiculo.filial = filial;

      activatedRoute.data = of({ compraVeiculo });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.veiculosSharedCollection).toContain(veiculo);
      expect(comp.filialsSharedCollection).toContain(filial);
      expect(comp.compraVeiculo).toEqual(compraVeiculo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompraVeiculo>>();
      const compraVeiculo = { id: 'ABC' };
      jest.spyOn(compraVeiculoFormService, 'getCompraVeiculo').mockReturnValue(compraVeiculo);
      jest.spyOn(compraVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compraVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compraVeiculo }));
      saveSubject.complete();

      // THEN
      expect(compraVeiculoFormService.getCompraVeiculo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compraVeiculoService.update).toHaveBeenCalledWith(expect.objectContaining(compraVeiculo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompraVeiculo>>();
      const compraVeiculo = { id: 'ABC' };
      jest.spyOn(compraVeiculoFormService, 'getCompraVeiculo').mockReturnValue({ id: null });
      jest.spyOn(compraVeiculoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compraVeiculo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compraVeiculo }));
      saveSubject.complete();

      // THEN
      expect(compraVeiculoFormService.getCompraVeiculo).toHaveBeenCalled();
      expect(compraVeiculoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompraVeiculo>>();
      const compraVeiculo = { id: 'ABC' };
      jest.spyOn(compraVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compraVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compraVeiculoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInstituicao', () => {
      it('Should forward to instituicaoService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(instituicaoService, 'compareInstituicao');
        comp.compareInstituicao(entity, entity2);
        expect(instituicaoService.compareInstituicao).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
  });
});
