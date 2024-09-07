import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { FilialService } from '../service/filial.service';
import { IFilial } from '../filial.model';
import { FilialFormService } from './filial-form.service';

import { FilialUpdateComponent } from './filial-update.component';

describe('Filial Management Update Component', () => {
  let comp: FilialUpdateComponent;
  let fixture: ComponentFixture<FilialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filialFormService: FilialFormService;
  let filialService: FilialService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FilialUpdateComponent],
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
      .overrideTemplate(FilialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FilialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filialFormService = TestBed.inject(FilialFormService);
    filialService = TestBed.inject(FilialService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const filial: IFilial = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '908bc1dc-7786-4049-b203-fcee49a827ab' };
      filial.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: '3d99bb98-f306-4742-8c08-b96172ee161a' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ filial });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const filial: IFilial = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '37ef6a5b-2ffd-4833-868f-513d8c91b4f3' };
      filial.instituicao = instituicao;

      activatedRoute.data = of({ filial });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.filial).toEqual(filial);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFilial>>();
      const filial = { id: 'ABC' };
      jest.spyOn(filialFormService, 'getFilial').mockReturnValue(filial);
      jest.spyOn(filialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filial }));
      saveSubject.complete();

      // THEN
      expect(filialFormService.getFilial).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(filialService.update).toHaveBeenCalledWith(expect.objectContaining(filial));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFilial>>();
      const filial = { id: 'ABC' };
      jest.spyOn(filialFormService, 'getFilial').mockReturnValue({ id: null });
      jest.spyOn(filialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filial: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filial }));
      saveSubject.complete();

      // THEN
      expect(filialFormService.getFilial).toHaveBeenCalled();
      expect(filialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFilial>>();
      const filial = { id: 'ABC' };
      jest.spyOn(filialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filialService.update).toHaveBeenCalled();
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
