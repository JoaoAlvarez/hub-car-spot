import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { FinanceiraService } from '../service/financeira.service';
import { IFinanceira } from '../financeira.model';
import { FinanceiraFormService } from './financeira-form.service';

import { FinanceiraUpdateComponent } from './financeira-update.component';

describe('Financeira Management Update Component', () => {
  let comp: FinanceiraUpdateComponent;
  let fixture: ComponentFixture<FinanceiraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financeiraFormService: FinanceiraFormService;
  let financeiraService: FinanceiraService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FinanceiraUpdateComponent],
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
      .overrideTemplate(FinanceiraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinanceiraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financeiraFormService = TestBed.inject(FinanceiraFormService);
    financeiraService = TestBed.inject(FinanceiraService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const financeira: IFinanceira = { id: 'CBA' };
      const instituicao: IInstituicao = { id: 'd66ce669-3a93-48e9-8183-fae684835ec6' };
      financeira.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: 'a85918e9-e88d-4b36-9ad9-a87980e23c62' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financeira });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const financeira: IFinanceira = { id: 'CBA' };
      const instituicao: IInstituicao = { id: 'ca0a3b61-6dc9-4539-bda9-917f25fe9c92' };
      financeira.instituicao = instituicao;

      activatedRoute.data = of({ financeira });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.financeira).toEqual(financeira);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinanceira>>();
      const financeira = { id: 'ABC' };
      jest.spyOn(financeiraFormService, 'getFinanceira').mockReturnValue(financeira);
      jest.spyOn(financeiraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financeira });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financeira }));
      saveSubject.complete();

      // THEN
      expect(financeiraFormService.getFinanceira).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(financeiraService.update).toHaveBeenCalledWith(expect.objectContaining(financeira));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinanceira>>();
      const financeira = { id: 'ABC' };
      jest.spyOn(financeiraFormService, 'getFinanceira').mockReturnValue({ id: null });
      jest.spyOn(financeiraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financeira: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financeira }));
      saveSubject.complete();

      // THEN
      expect(financeiraFormService.getFinanceira).toHaveBeenCalled();
      expect(financeiraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinanceira>>();
      const financeira = { id: 'ABC' };
      jest.spyOn(financeiraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financeira });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(financeiraService.update).toHaveBeenCalled();
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
  });
});
