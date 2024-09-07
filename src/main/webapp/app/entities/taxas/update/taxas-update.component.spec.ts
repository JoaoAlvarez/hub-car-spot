import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { TaxasService } from '../service/taxas.service';
import { ITaxas } from '../taxas.model';
import { TaxasFormService } from './taxas-form.service';

import { TaxasUpdateComponent } from './taxas-update.component';

describe('Taxas Management Update Component', () => {
  let comp: TaxasUpdateComponent;
  let fixture: ComponentFixture<TaxasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taxasFormService: TaxasFormService;
  let taxasService: TaxasService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TaxasUpdateComponent],
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
      .overrideTemplate(TaxasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaxasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taxasFormService = TestBed.inject(TaxasFormService);
    taxasService = TestBed.inject(TaxasService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const taxas: ITaxas = { id: 'CBA' };
      const instituicao: IInstituicao = { id: 'c560e699-5311-443f-b079-4934f3eec310' };
      taxas.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: 'f7cc8472-d185-4cfb-b996-48ab68b07ffc' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxas });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const taxas: ITaxas = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '24a55be7-28db-4ee3-a5dc-467210987a69' };
      taxas.instituicao = instituicao;

      activatedRoute.data = of({ taxas });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.taxas).toEqual(taxas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxas>>();
      const taxas = { id: 'ABC' };
      jest.spyOn(taxasFormService, 'getTaxas').mockReturnValue(taxas);
      jest.spyOn(taxasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taxas }));
      saveSubject.complete();

      // THEN
      expect(taxasFormService.getTaxas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taxasService.update).toHaveBeenCalledWith(expect.objectContaining(taxas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxas>>();
      const taxas = { id: 'ABC' };
      jest.spyOn(taxasFormService, 'getTaxas').mockReturnValue({ id: null });
      jest.spyOn(taxasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taxas }));
      saveSubject.complete();

      // THEN
      expect(taxasFormService.getTaxas).toHaveBeenCalled();
      expect(taxasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxas>>();
      const taxas = { id: 'ABC' };
      jest.spyOn(taxasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taxasService.update).toHaveBeenCalled();
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
