import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { InstituicaoService } from '../service/instituicao.service';
import { IInstituicao } from '../instituicao.model';
import { InstituicaoFormService } from './instituicao-form.service';

import { InstituicaoUpdateComponent } from './instituicao-update.component';

describe('Instituicao Management Update Component', () => {
  let comp: InstituicaoUpdateComponent;
  let fixture: ComponentFixture<InstituicaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let instituicaoFormService: InstituicaoFormService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InstituicaoUpdateComponent],
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
      .overrideTemplate(InstituicaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstituicaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    instituicaoFormService = TestBed.inject(InstituicaoFormService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const instituicao: IInstituicao = { id: 'CBA' };

      activatedRoute.data = of({ instituicao });
      comp.ngOnInit();

      expect(comp.instituicao).toEqual(instituicao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstituicao>>();
      const instituicao = { id: 'ABC' };
      jest.spyOn(instituicaoFormService, 'getInstituicao').mockReturnValue(instituicao);
      jest.spyOn(instituicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instituicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instituicao }));
      saveSubject.complete();

      // THEN
      expect(instituicaoFormService.getInstituicao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(instituicaoService.update).toHaveBeenCalledWith(expect.objectContaining(instituicao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstituicao>>();
      const instituicao = { id: 'ABC' };
      jest.spyOn(instituicaoFormService, 'getInstituicao').mockReturnValue({ id: null });
      jest.spyOn(instituicaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instituicao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instituicao }));
      saveSubject.complete();

      // THEN
      expect(instituicaoFormService.getInstituicao).toHaveBeenCalled();
      expect(instituicaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstituicao>>();
      const instituicao = { id: 'ABC' };
      jest.spyOn(instituicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instituicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(instituicaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
