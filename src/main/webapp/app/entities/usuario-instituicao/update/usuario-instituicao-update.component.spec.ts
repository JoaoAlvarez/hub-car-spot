import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { UsuarioInstituicaoService } from '../service/usuario-instituicao.service';
import { IUsuarioInstituicao } from '../usuario-instituicao.model';
import { UsuarioInstituicaoFormService } from './usuario-instituicao-form.service';

import { UsuarioInstituicaoUpdateComponent } from './usuario-instituicao-update.component';

describe('UsuarioInstituicao Management Update Component', () => {
  let comp: UsuarioInstituicaoUpdateComponent;
  let fixture: ComponentFixture<UsuarioInstituicaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioInstituicaoFormService: UsuarioInstituicaoFormService;
  let usuarioInstituicaoService: UsuarioInstituicaoService;
  let instituicaoService: InstituicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UsuarioInstituicaoUpdateComponent],
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
      .overrideTemplate(UsuarioInstituicaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioInstituicaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioInstituicaoFormService = TestBed.inject(UsuarioInstituicaoFormService);
    usuarioInstituicaoService = TestBed.inject(UsuarioInstituicaoService);
    instituicaoService = TestBed.inject(InstituicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instituicao query and add missing value', () => {
      const usuarioInstituicao: IUsuarioInstituicao = { id: 'CBA' };
      const instituicao: IInstituicao = { id: 'a24cf811-d789-48f0-916c-92d15fe7204b' };
      usuarioInstituicao.instituicao = instituicao;

      const instituicaoCollection: IInstituicao[] = [{ id: 'e76da7ae-df0c-4684-bcfb-5ac753459042' }];
      jest.spyOn(instituicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: instituicaoCollection })));
      const additionalInstituicaos = [instituicao];
      const expectedCollection: IInstituicao[] = [...additionalInstituicaos, ...instituicaoCollection];
      jest.spyOn(instituicaoService, 'addInstituicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioInstituicao });
      comp.ngOnInit();

      expect(instituicaoService.query).toHaveBeenCalled();
      expect(instituicaoService.addInstituicaoToCollectionIfMissing).toHaveBeenCalledWith(
        instituicaoCollection,
        ...additionalInstituicaos.map(expect.objectContaining),
      );
      expect(comp.instituicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuarioInstituicao: IUsuarioInstituicao = { id: 'CBA' };
      const instituicao: IInstituicao = { id: '829f5ff1-1f56-4b99-9c08-1cb2e703b59b' };
      usuarioInstituicao.instituicao = instituicao;

      activatedRoute.data = of({ usuarioInstituicao });
      comp.ngOnInit();

      expect(comp.instituicaosSharedCollection).toContain(instituicao);
      expect(comp.usuarioInstituicao).toEqual(usuarioInstituicao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuarioInstituicao>>();
      const usuarioInstituicao = { id: 'ABC' };
      jest.spyOn(usuarioInstituicaoFormService, 'getUsuarioInstituicao').mockReturnValue(usuarioInstituicao);
      jest.spyOn(usuarioInstituicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioInstituicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioInstituicao }));
      saveSubject.complete();

      // THEN
      expect(usuarioInstituicaoFormService.getUsuarioInstituicao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioInstituicaoService.update).toHaveBeenCalledWith(expect.objectContaining(usuarioInstituicao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuarioInstituicao>>();
      const usuarioInstituicao = { id: 'ABC' };
      jest.spyOn(usuarioInstituicaoFormService, 'getUsuarioInstituicao').mockReturnValue({ id: null });
      jest.spyOn(usuarioInstituicaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioInstituicao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioInstituicao }));
      saveSubject.complete();

      // THEN
      expect(usuarioInstituicaoFormService.getUsuarioInstituicao).toHaveBeenCalled();
      expect(usuarioInstituicaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuarioInstituicao>>();
      const usuarioInstituicao = { id: 'ABC' };
      jest.spyOn(usuarioInstituicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioInstituicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioInstituicaoService.update).toHaveBeenCalled();
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
