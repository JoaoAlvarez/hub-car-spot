import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VeiculoService } from '../service/veiculo.service';
import { IVeiculo } from '../veiculo.model';
import { VeiculoFormService } from './veiculo-form.service';

import { VeiculoUpdateComponent } from './veiculo-update.component';

describe('Veiculo Management Update Component', () => {
  let comp: VeiculoUpdateComponent;
  let fixture: ComponentFixture<VeiculoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let veiculoFormService: VeiculoFormService;
  let veiculoService: VeiculoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VeiculoUpdateComponent],
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
      .overrideTemplate(VeiculoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VeiculoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    veiculoFormService = TestBed.inject(VeiculoFormService);
    veiculoService = TestBed.inject(VeiculoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const veiculo: IVeiculo = { id: 'CBA' };

      activatedRoute.data = of({ veiculo });
      comp.ngOnInit();

      expect(comp.veiculo).toEqual(veiculo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVeiculo>>();
      const veiculo = { id: 'ABC' };
      jest.spyOn(veiculoFormService, 'getVeiculo').mockReturnValue(veiculo);
      jest.spyOn(veiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ veiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: veiculo }));
      saveSubject.complete();

      // THEN
      expect(veiculoFormService.getVeiculo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(veiculoService.update).toHaveBeenCalledWith(expect.objectContaining(veiculo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVeiculo>>();
      const veiculo = { id: 'ABC' };
      jest.spyOn(veiculoFormService, 'getVeiculo').mockReturnValue({ id: null });
      jest.spyOn(veiculoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ veiculo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: veiculo }));
      saveSubject.complete();

      // THEN
      expect(veiculoFormService.getVeiculo).toHaveBeenCalled();
      expect(veiculoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVeiculo>>();
      const veiculo = { id: 'ABC' };
      jest.spyOn(veiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ veiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(veiculoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
