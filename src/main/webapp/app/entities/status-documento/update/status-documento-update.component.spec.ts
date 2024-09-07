import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { StatusDocumentoService } from '../service/status-documento.service';
import { IStatusDocumento } from '../status-documento.model';
import { StatusDocumentoFormService } from './status-documento-form.service';

import { StatusDocumentoUpdateComponent } from './status-documento-update.component';

describe('StatusDocumento Management Update Component', () => {
  let comp: StatusDocumentoUpdateComponent;
  let fixture: ComponentFixture<StatusDocumentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statusDocumentoFormService: StatusDocumentoFormService;
  let statusDocumentoService: StatusDocumentoService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StatusDocumentoUpdateComponent],
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
      .overrideTemplate(StatusDocumentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatusDocumentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusDocumentoFormService = TestBed.inject(StatusDocumentoFormService);
    statusDocumentoService = TestBed.inject(StatusDocumentoService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const statusDocumento: IStatusDocumento = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '69c5c337-f5f9-46df-94f0-7639d6000c99' };
      statusDocumento.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: '2ace9940-3333-43f5-a933-a9ff109f73fd' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ statusDocumento });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const statusDocumento: IStatusDocumento = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '11beb26b-2016-40ee-9284-9a2633b1be64' };
      statusDocumento.instituicao = instituicao;

      activatedRoute.data = of({ statusDocumento });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.statusDocumento).toEqual(statusDocumento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusDocumento>>();
      const statusDocumento = { id: 'ABC' };
      jest.spyOn(statusDocumentoFormService, 'getStatusDocumento').mockReturnValue(statusDocumento);
      jest.spyOn(statusDocumentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusDocumento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statusDocumento }));
      saveSubject.complete();

      // THEN
      expect(statusDocumentoFormService.getStatusDocumento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusDocumentoService.update).toHaveBeenCalledWith(expect.objectContaining(statusDocumento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusDocumento>>();
      const statusDocumento = { id: 'ABC' };
      jest.spyOn(statusDocumentoFormService, 'getStatusDocumento').mockReturnValue({ id: null });
      jest.spyOn(statusDocumentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusDocumento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statusDocumento }));
      saveSubject.complete();

      // THEN
      expect(statusDocumentoFormService.getStatusDocumento).toHaveBeenCalled();
      expect(statusDocumentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusDocumento>>();
      const statusDocumento = { id: 'ABC' };
      jest.spyOn(statusDocumentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusDocumento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusDocumentoService.update).toHaveBeenCalled();
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
