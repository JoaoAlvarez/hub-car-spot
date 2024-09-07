import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { LocalService } from '../service/local.service';
import { ILocal } from '../local.model';
import { LocalFormService } from './local-form.service';

import { LocalUpdateComponent } from './local-update.component';

describe('Local Management Update Component', () => {
  let comp: LocalUpdateComponent;
  let fixture: ComponentFixture<LocalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localFormService: LocalFormService;
  let localService: LocalService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LocalUpdateComponent],
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
      .overrideTemplate(LocalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localFormService = TestBed.inject(LocalFormService);
    localService = TestBed.inject(LocalService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const local: ILocal = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '59f52476-0863-451c-a2bc-c86cd030d778' };
      local.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: 'a94da484-f035-4bc4-baa1-c7b2f3c671f8' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ local });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const local: ILocal = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '05165d65-35a8-47e6-8dd7-c27dd124fcd8' };
      local.instituicao = instituicao;

      activatedRoute.data = of({ local });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.local).toEqual(local);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocal>>();
      const local = { id: 'ABC' };
      jest.spyOn(localFormService, 'getLocal').mockReturnValue(local);
      jest.spyOn(localService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ local });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: local }));
      saveSubject.complete();

      // THEN
      expect(localFormService.getLocal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(localService.update).toHaveBeenCalledWith(expect.objectContaining(local));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocal>>();
      const local = { id: 'ABC' };
      jest.spyOn(localFormService, 'getLocal').mockReturnValue({ id: null });
      jest.spyOn(localService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ local: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: local }));
      saveSubject.complete();

      // THEN
      expect(localFormService.getLocal).toHaveBeenCalled();
      expect(localService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocal>>();
      const local = { id: 'ABC' };
      jest.spyOn(localService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ local });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localService.update).toHaveBeenCalled();
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
