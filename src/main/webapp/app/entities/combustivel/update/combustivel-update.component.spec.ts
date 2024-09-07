import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CombustivelService } from '../service/combustivel.service';
import { ICombustivel } from '../combustivel.model';
import { CombustivelFormService } from './combustivel-form.service';

import { CombustivelUpdateComponent } from './combustivel-update.component';

describe('Combustivel Management Update Component', () => {
  let comp: CombustivelUpdateComponent;
  let fixture: ComponentFixture<CombustivelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let combustivelFormService: CombustivelFormService;
  let combustivelService: CombustivelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CombustivelUpdateComponent],
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
      .overrideTemplate(CombustivelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CombustivelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    combustivelFormService = TestBed.inject(CombustivelFormService);
    combustivelService = TestBed.inject(CombustivelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const combustivel: ICombustivel = { id: 'CBA' };

      activatedRoute.data = of({ combustivel });
      comp.ngOnInit();

      expect(comp.combustivel).toEqual(combustivel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICombustivel>>();
      const combustivel = { id: 'ABC' };
      jest.spyOn(combustivelFormService, 'getCombustivel').mockReturnValue(combustivel);
      jest.spyOn(combustivelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ combustivel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: combustivel }));
      saveSubject.complete();

      // THEN
      expect(combustivelFormService.getCombustivel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(combustivelService.update).toHaveBeenCalledWith(expect.objectContaining(combustivel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICombustivel>>();
      const combustivel = { id: 'ABC' };
      jest.spyOn(combustivelFormService, 'getCombustivel').mockReturnValue({ id: null });
      jest.spyOn(combustivelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ combustivel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: combustivel }));
      saveSubject.complete();

      // THEN
      expect(combustivelFormService.getCombustivel).toHaveBeenCalled();
      expect(combustivelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICombustivel>>();
      const combustivel = { id: 'ABC' };
      jest.spyOn(combustivelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ combustivel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(combustivelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
