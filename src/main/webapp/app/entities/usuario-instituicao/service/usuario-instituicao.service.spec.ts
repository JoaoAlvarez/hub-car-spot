import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUsuarioInstituicao } from '../usuario-instituicao.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../usuario-instituicao.test-samples';

import { UsuarioInstituicaoService } from './usuario-instituicao.service';

const requireRestSample: IUsuarioInstituicao = {
  ...sampleWithRequiredData,
};

describe('UsuarioInstituicao Service', () => {
  let service: UsuarioInstituicaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IUsuarioInstituicao | IUsuarioInstituicao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UsuarioInstituicaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a UsuarioInstituicao', () => {
      const usuarioInstituicao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(usuarioInstituicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UsuarioInstituicao', () => {
      const usuarioInstituicao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(usuarioInstituicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UsuarioInstituicao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UsuarioInstituicao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UsuarioInstituicao', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUsuarioInstituicaoToCollectionIfMissing', () => {
      it('should add a UsuarioInstituicao to an empty array', () => {
        const usuarioInstituicao: IUsuarioInstituicao = sampleWithRequiredData;
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing([], usuarioInstituicao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioInstituicao);
      });

      it('should not add a UsuarioInstituicao to an array that contains it', () => {
        const usuarioInstituicao: IUsuarioInstituicao = sampleWithRequiredData;
        const usuarioInstituicaoCollection: IUsuarioInstituicao[] = [
          {
            ...usuarioInstituicao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing(usuarioInstituicaoCollection, usuarioInstituicao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UsuarioInstituicao to an array that doesn't contain it", () => {
        const usuarioInstituicao: IUsuarioInstituicao = sampleWithRequiredData;
        const usuarioInstituicaoCollection: IUsuarioInstituicao[] = [sampleWithPartialData];
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing(usuarioInstituicaoCollection, usuarioInstituicao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioInstituicao);
      });

      it('should add only unique UsuarioInstituicao to an array', () => {
        const usuarioInstituicaoArray: IUsuarioInstituicao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const usuarioInstituicaoCollection: IUsuarioInstituicao[] = [sampleWithRequiredData];
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing(usuarioInstituicaoCollection, ...usuarioInstituicaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const usuarioInstituicao: IUsuarioInstituicao = sampleWithRequiredData;
        const usuarioInstituicao2: IUsuarioInstituicao = sampleWithPartialData;
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing([], usuarioInstituicao, usuarioInstituicao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioInstituicao);
        expect(expectedResult).toContain(usuarioInstituicao2);
      });

      it('should accept null and undefined values', () => {
        const usuarioInstituicao: IUsuarioInstituicao = sampleWithRequiredData;
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing([], null, usuarioInstituicao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioInstituicao);
      });

      it('should return initial array if no UsuarioInstituicao is added', () => {
        const usuarioInstituicaoCollection: IUsuarioInstituicao[] = [sampleWithRequiredData];
        expectedResult = service.addUsuarioInstituicaoToCollectionIfMissing(usuarioInstituicaoCollection, undefined, null);
        expect(expectedResult).toEqual(usuarioInstituicaoCollection);
      });
    });

    describe('compareUsuarioInstituicao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUsuarioInstituicao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareUsuarioInstituicao(entity1, entity2);
        const compareResult2 = service.compareUsuarioInstituicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareUsuarioInstituicao(entity1, entity2);
        const compareResult2 = service.compareUsuarioInstituicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareUsuarioInstituicao(entity1, entity2);
        const compareResult2 = service.compareUsuarioInstituicao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
