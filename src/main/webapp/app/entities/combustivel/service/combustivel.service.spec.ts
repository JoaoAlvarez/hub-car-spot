import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICombustivel } from '../combustivel.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../combustivel.test-samples';

import { CombustivelService } from './combustivel.service';

const requireRestSample: ICombustivel = {
  ...sampleWithRequiredData,
};

describe('Combustivel Service', () => {
  let service: CombustivelService;
  let httpMock: HttpTestingController;
  let expectedResult: ICombustivel | ICombustivel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CombustivelService);
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

    it('should create a Combustivel', () => {
      const combustivel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(combustivel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Combustivel', () => {
      const combustivel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(combustivel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Combustivel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Combustivel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Combustivel', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCombustivelToCollectionIfMissing', () => {
      it('should add a Combustivel to an empty array', () => {
        const combustivel: ICombustivel = sampleWithRequiredData;
        expectedResult = service.addCombustivelToCollectionIfMissing([], combustivel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(combustivel);
      });

      it('should not add a Combustivel to an array that contains it', () => {
        const combustivel: ICombustivel = sampleWithRequiredData;
        const combustivelCollection: ICombustivel[] = [
          {
            ...combustivel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCombustivelToCollectionIfMissing(combustivelCollection, combustivel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Combustivel to an array that doesn't contain it", () => {
        const combustivel: ICombustivel = sampleWithRequiredData;
        const combustivelCollection: ICombustivel[] = [sampleWithPartialData];
        expectedResult = service.addCombustivelToCollectionIfMissing(combustivelCollection, combustivel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(combustivel);
      });

      it('should add only unique Combustivel to an array', () => {
        const combustivelArray: ICombustivel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const combustivelCollection: ICombustivel[] = [sampleWithRequiredData];
        expectedResult = service.addCombustivelToCollectionIfMissing(combustivelCollection, ...combustivelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const combustivel: ICombustivel = sampleWithRequiredData;
        const combustivel2: ICombustivel = sampleWithPartialData;
        expectedResult = service.addCombustivelToCollectionIfMissing([], combustivel, combustivel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(combustivel);
        expect(expectedResult).toContain(combustivel2);
      });

      it('should accept null and undefined values', () => {
        const combustivel: ICombustivel = sampleWithRequiredData;
        expectedResult = service.addCombustivelToCollectionIfMissing([], null, combustivel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(combustivel);
      });

      it('should return initial array if no Combustivel is added', () => {
        const combustivelCollection: ICombustivel[] = [sampleWithRequiredData];
        expectedResult = service.addCombustivelToCollectionIfMissing(combustivelCollection, undefined, null);
        expect(expectedResult).toEqual(combustivelCollection);
      });
    });

    describe('compareCombustivel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCombustivel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCombustivel(entity1, entity2);
        const compareResult2 = service.compareCombustivel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCombustivel(entity1, entity2);
        const compareResult2 = service.compareCombustivel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCombustivel(entity1, entity2);
        const compareResult2 = service.compareCombustivel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
