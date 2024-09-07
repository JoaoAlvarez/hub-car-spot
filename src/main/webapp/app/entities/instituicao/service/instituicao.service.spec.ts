import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInstituicao } from '../instituicao.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../instituicao.test-samples';

import { InstituicaoService } from './instituicao.service';

const requireRestSample: IInstituicao = {
  ...sampleWithRequiredData,
};

describe('Instituicao Service', () => {
  let service: InstituicaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IInstituicao | IInstituicao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InstituicaoService);
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

    it('should create a Instituicao', () => {
      const instituicao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(instituicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Instituicao', () => {
      const instituicao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(instituicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Instituicao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Instituicao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Instituicao', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInstituicaoToCollectionIfMissing', () => {
      it('should add a Instituicao to an empty array', () => {
        const instituicao: IInstituicao = sampleWithRequiredData;
        expectedResult = service.addInstituicaoToCollectionIfMissing([], instituicao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instituicao);
      });

      it('should not add a Instituicao to an array that contains it', () => {
        const instituicao: IInstituicao = sampleWithRequiredData;
        const instituicaoCollection: IInstituicao[] = [
          {
            ...instituicao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInstituicaoToCollectionIfMissing(instituicaoCollection, instituicao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Instituicao to an array that doesn't contain it", () => {
        const instituicao: IInstituicao = sampleWithRequiredData;
        const instituicaoCollection: IInstituicao[] = [sampleWithPartialData];
        expectedResult = service.addInstituicaoToCollectionIfMissing(instituicaoCollection, instituicao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instituicao);
      });

      it('should add only unique Instituicao to an array', () => {
        const instituicaoArray: IInstituicao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const instituicaoCollection: IInstituicao[] = [sampleWithRequiredData];
        expectedResult = service.addInstituicaoToCollectionIfMissing(instituicaoCollection, ...instituicaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const instituicao: IInstituicao = sampleWithRequiredData;
        const instituicao2: IInstituicao = sampleWithPartialData;
        expectedResult = service.addInstituicaoToCollectionIfMissing([], instituicao, instituicao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instituicao);
        expect(expectedResult).toContain(instituicao2);
      });

      it('should accept null and undefined values', () => {
        const instituicao: IInstituicao = sampleWithRequiredData;
        expectedResult = service.addInstituicaoToCollectionIfMissing([], null, instituicao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instituicao);
      });

      it('should return initial array if no Instituicao is added', () => {
        const instituicaoCollection: IInstituicao[] = [sampleWithRequiredData];
        expectedResult = service.addInstituicaoToCollectionIfMissing(instituicaoCollection, undefined, null);
        expect(expectedResult).toEqual(instituicaoCollection);
      });
    });

    describe('compareInstituicao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInstituicao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareInstituicao(entity1, entity2);
        const compareResult2 = service.compareInstituicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareInstituicao(entity1, entity2);
        const compareResult2 = service.compareInstituicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareInstituicao(entity1, entity2);
        const compareResult2 = service.compareInstituicao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
