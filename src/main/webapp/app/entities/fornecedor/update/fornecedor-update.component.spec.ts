import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { FornecedorService } from '../service/fornecedor.service';
import { IFornecedor } from '../fornecedor.model';
import { FornecedorFormService } from './fornecedor-form.service';

import { FornecedorUpdateComponent } from './fornecedor-update.component';

describe('Fornecedor Management Update Component', () => {
  let comp: FornecedorUpdateComponent;
  let fixture: ComponentFixture<FornecedorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fornecedorFormService: FornecedorFormService;
  let fornecedorService: FornecedorService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FornecedorUpdateComponent],
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
      .overrideTemplate(FornecedorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FornecedorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fornecedorFormService = TestBed.inject(FornecedorFormService);
    fornecedorService = TestBed.inject(FornecedorService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const fornecedor: IFornecedor = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '356ca205-80c9-4c96-8706-8041819062f3' };
      fornecedor.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: '3b43eef8-d9c0-4472-8ed7-d0d2879f213a' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fornecedor });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fornecedor: IFornecedor = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '120bc96e-47d4-4ac2-ae35-641c5e82db07' };
      fornecedor.instituicao = instituicao;

      activatedRoute.data = of({ fornecedor });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.fornecedor).toEqual(fornecedor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFornecedor>>();
      const fornecedor = { id: 'ABC' };
      jest.spyOn(fornecedorFormService, 'getFornecedor').mockReturnValue(fornecedor);
      jest.spyOn(fornecedorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fornecedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fornecedor }));
      saveSubject.complete();

      // THEN
      expect(fornecedorFormService.getFornecedor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fornecedorService.update).toHaveBeenCalledWith(expect.objectContaining(fornecedor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFornecedor>>();
      const fornecedor = { id: 'ABC' };
      jest.spyOn(fornecedorFormService, 'getFornecedor').mockReturnValue({ id: null });
      jest.spyOn(fornecedorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fornecedor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fornecedor }));
      saveSubject.complete();

      // THEN
      expect(fornecedorFormService.getFornecedor).toHaveBeenCalled();
      expect(fornecedorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFornecedor>>();
      const fornecedor = { id: 'ABC' };
      jest.spyOn(fornecedorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fornecedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fornecedorService.update).toHaveBeenCalled();
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
